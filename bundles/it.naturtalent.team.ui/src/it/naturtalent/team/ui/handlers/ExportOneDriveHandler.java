
package it.naturtalent.team.ui.handlers;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Shell;

import it.naturtalent.e4.project.INtProject;
import it.naturtalent.e4.project.INtProjectProperty;
import it.naturtalent.e4.project.INtProjectPropertyFactory;
import it.naturtalent.e4.project.INtProjectPropertyFactoryRepository;
import it.naturtalent.e4.project.expimp.ExportResources;
import it.naturtalent.e4.project.ui.datatransfer.RefreshResourcesOperation;
import it.naturtalent.e4.project.ui.emf.ExportProjectPropertiesOperation;
import it.naturtalent.team.ui.OneDriveUtils;

@Deprecated
// wurde ersetzt durch it.naturtalent.team.ui.actions.TransferExportAction
public class ExportOneDriveHandler
{
	private INtProjectPropertyFactoryRepository projektDataFactoryRepository;

	private Shell shell;

	@Execute
	public void execute(
			@Named(IServiceConstants.ACTIVE_SELECTION) @Optional IProject iProject,
			@Optional INtProjectPropertyFactoryRepository projektDataFactoryRepository,
			@Named(IServiceConstants.ACTIVE_SHELL) @Optional Shell shell)
	{
		if (iProject != null)
		{
			this.projektDataFactoryRepository = projektDataFactoryRepository;
			this.shell = shell;

			try
			{
				// Exportverzeichnis
				File oneDriveExpDir = OneDriveUtils.getOneDriveExpImpDir();

				File existExportDir = isAlreadyExported(oneDriveExpDir,iProject);
				if (existExportDir != null)
					try
					{
						File parentDir = new File(existExportDir.getParent());
						FileUtils.deleteDirectory(parentDir);
					} catch (IOException e)
					{
						MessageDialog.openError(shell,
								"Export Project nach OneDrive",
								"ein bestehendes Projekt konnte nicht gelöscht werden");
						return;
					}

				// Projektname nur Buchstaben
				String projectName = iProject.getPersistentProperty(
						INtProject.projectNameQualifiedName);
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

			} catch (CoreException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
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

		String destPath = exportDestDir.getPath();
		try
		{
			if (SystemUtils.IS_OS_LINUX)
				Runtime.getRuntime().exec("nautilus " + destPath);
			else
				Runtime.getRuntime().exec("explorer " + destPath);
		} catch (IOException exp)
		{
			if (SystemUtils.IS_OS_LINUX)
				try
				{
					Runtime.getRuntime().exec("nemo " + destPath);
					return;
				} catch (Exception e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			exp.printStackTrace();

		}

		MessageDialog.openInformation(null, "Export", //$NON-NLS-1$
				"Projekt wurde in das selektierte Verzeichnis kopiert\nund kann in OneDrive gezogen werden  " //$NON-NLS-1$
						+ exportDestDir);

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

	@CanExecute
	public boolean canExecute(
			@Named(IServiceConstants.ACTIVE_SELECTION) @Optional IProject iProject)
	{
		if (iProject == null)
			return false;

		return true;
	}

}