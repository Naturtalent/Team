package it.naturtalent.team.ui.actions;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Shell;

import it.naturtalent.e4.project.INtProject;
import it.naturtalent.e4.project.INtProjectProperty;
import it.naturtalent.e4.project.INtProjectPropertyFactory;
import it.naturtalent.e4.project.INtProjectPropertyFactoryRepository;
import it.naturtalent.e4.project.expimp.ExportResources;
import it.naturtalent.e4.project.expimp.actions.ExportAction;
import it.naturtalent.e4.project.ui.datatransfer.RefreshResourcesOperation;
import it.naturtalent.e4.project.ui.emf.ExportProjectPropertiesOperation;
import it.naturtalent.team.ui.OneDriveUtils;
import it.naturtalent.team.ui.dialogs.ProjectOneDriveExport;
import it.naturtalent.team.ui.handlers.OpenCloudTransferHandler;

/**
 * Mit dieser Aktion koennen Projekte in ein Transferverzeichnis exportiert werden. 
 * 
 * @author dieter
 *
 */
public class TransferExportAction extends ExportAction
{
	private INtProjectPropertyFactoryRepository projektDataFactoryRepository;
	private Shell shell;
	
	@PostConstruct
	public void postConstruct(UISynchronize sync,
			@Named(IServiceConstants.ACTIVE_SHELL) Shell shell,
			@Optional IEventBroker eventBroker,
			@Optional INtProjectPropertyFactoryRepository projektDataFactoryRepository)
	{		
		this.shell = shell;
		this.projektDataFactoryRepository = projektDataFactoryRepository;
		
	}

	@Override
	public void run()
	{
		ProjectOneDriveExport exportDialog = new ProjectOneDriveExport(shell);
		if(exportDialog.open() == ProjectOneDriveExport.OK)
		{
			// Exportverzeichnis
			File oneDriveExpDir = OneDriveUtils.getOneDriveExpImpDir();
			
			// die im Dialog selektierten Projekte abfragen
			IResource[] resources = exportDialog.getResultExportSource();
			if (ArrayUtils.isEmpty(resources))
				return;
			
			// das ausgewaehlte Zielverzeichnis (hierhin werden die Projekte exportiert)
			exportDestDir = exportDialog.getResultDestDir();
			if(exportDestDir == null)
				return;
			
			for(IResource iResource : resources)
			{
				if(iResource.getType() == IResource.PROJECT)
				{
					IProject iProject = (IProject) iResource; 
					File existExportDir = isAlreadyExported(exportDestDir, iProject);					
					if (existExportDir != null)
					{
						// ein bestehendes Verzeichnis loeschen
						File parentDir = new File(existExportDir.getParent());
						try
						{
							FileUtils.deleteDirectory(parentDir);
						} catch (IOException e)
						{							
						}						
					}
					
					// Projektname nur Buchstaben
					String projectName = null;
					try
					{
						projectName = iProject.getPersistentProperty(INtProject.projectNameQualifiedName);
					} catch (CoreException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					projectName = cleanSonderzeichen(projectName);
					projectName = projectName.replaceAll("[^0-9a-zA-Z]", "");
					
					File exportDestDir = new File(oneDriveExpDir, projectName);
					if (!exportDestDir.exists())
					{
						if (!exportDestDir.mkdir())
						{
							MessageDialog.openError(shell,
									"Verzeichnis konnte nicht erstellt werden",
									"ist der Name konform mit dem Filesystem ?");
							return;
						}
					}
					
					exportIProject(exportDestDir, iProject);
				}
			}

			MessageDialog.openInformation(null, "Export", //$NON-NLS-1$
					"Projekte wurde in das selektierte Transfervrzeichnis kopiert\nund kann in OneDrive gezogen werden  " //$NON-NLS-1$
							+ exportDestDir);

			OpenCloudTransferHandler openTransferDirHandler = new OpenCloudTransferHandler();
			openTransferDirHandler.execute();
			
		}
	}
	
	private void exportIProject(File exportDestDir, IProject iProject)
	{
		List<INtProjectPropertyFactory> projectPropertyFactories = projektDataFactoryRepository
				.getAllProjektDataFactories();

		List<INtProjectProperty> projectPropertyAdapters = new ArrayList<INtProjectProperty>();
		for (INtProjectPropertyFactory propertyFactory : projectPropertyFactories)
			projectPropertyAdapters.add(propertyFactory.createNtProjektData());

		IResource[] resources = new IResource[]
			{ iProject };
		List<IResource> iResources = Arrays.asList(resources);
		ExportProjectPropertiesOperation exportPropertiesOperation = new ExportProjectPropertiesOperation(
				iResources, projectPropertyAdapters);

		try
		{
			// Projekteigenschaften im langlaufenden Prozess exportieren
			new ProgressMonitorDialog(shell).run(true, false,
					exportPropertiesOperation);
		} catch (InvocationTargetException e)
		{
			// Error
			Throwable realException = e.getTargetException();
			MessageDialog.openError(shell, "Export Error",
					realException.getMessage());
		} catch (InterruptedException e)
		{
			// Abbruch
			MessageDialog.openError(shell, "Abbruch Error", e.getMessage());
			return;
		}

		/*
		 * da die Eigenschaften in separaten Dateien gespeichert wurden ist ein
		 * Refresh erforderlich
		 */
		RefreshResourcesOperation refreshOperation = new RefreshResourcesOperation(
				iResources);
		try
		{
			new ProgressMonitorDialog(shell).run(true, false, refreshOperation);
		} catch (InvocationTargetException e)
		{
			// Error
			Throwable realException = e.getTargetException();
			MessageDialog.openError(shell, "Export Error",
					realException.getMessage());
		} catch (InterruptedException e)
		{
			// Abbruch
			MessageDialog.openError(shell, "Export Error", e.getMessage());
			return;
		}

		// abschliessend alle zuexportierenden NtProjekte exportieren (kopieren)
		if (shell != null)
		{
			ExportResources exportResource = new ExportResources(shell);
			exportResource.export(shell, iResources, exportDestDir.getPath(),
					false);
		}
	}

	
	// pruefen ob im 'oneDriveExpDir' bereits iProject-Export vorhanden ist
	private File isAlreadyExported(File oneDriveExpDir, IProject iProject)
	{
		String projectID = iProject.getName();
		Iterator<File> rootDirsIter = FileUtils.iterateFilesAndDirs(
				oneDriveExpDir, FileFilterUtils.directoryFileFilter(),
				TrueFileFilter.INSTANCE);
		if (rootDirsIter != null)
		{
			while (rootDirsIter.hasNext())
			{
				File exportDir = rootDirsIter.next();
				if (StringUtils.contains(exportDir.getPath(), projectID))
					return exportDir;
			}
		}
		return null;
	}

	// Umlataue und Sonderzeichen wandeln
	private static final String[] UMLAUTE = new String[]
		{ "Ä", "Ö", "Ü", "ä", "ö", "ü", "ß" };

	private static final String[] UMLAUTE_REPLACEMENT = new String[]
		{ "AE", "OE", "UE", "ae", "oe", "ue", "ss" };

	private String cleanSonderzeichen(String projectName)
	{
		return StringUtils.stripAccents(StringUtils.replaceEach(projectName,
				UMLAUTE, UMLAUTE_REPLACEMENT));
	}
	
}
