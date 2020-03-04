package it.naturtalent.team.ui.actions;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;

import it.naturtalent.e4.project.INtProjectPropertyFactoryRepository;
import it.naturtalent.e4.project.expimp.actions.ImportAction;
import it.naturtalent.e4.project.expimp.dialogs.ProjectImportDialog;
import it.naturtalent.e4.project.model.project.NtProject;
import it.naturtalent.e4.project.ui.emf.ExpImpUtils;
import it.naturtalent.e4.project.ui.emf.NtProjectProperty;
import it.naturtalent.team.ui.OneDriveUtils;
import it.naturtalent.team.ui.dialogs.ProjectOneDriveImport;

public class TransferImportAction extends ImportAction
{
	//private Shell shell;
	//private INtProjectPropertyFactoryRepository projektDataFactoryRepository;
	
	@PostConstruct
	public void postConstruct(UISynchronize sync,
			@Named(IServiceConstants.ACTIVE_SHELL) Shell shell,
			@Optional IEventBroker eventBroker,
			@Optional INtProjectPropertyFactoryRepository projektDataFactoryRepository)
	{
		this.shell = shell;
		this.projektDataFactoryRepository = projektDataFactoryRepository;
	}
		
	public void run()
	{
		File oneDriveExpDir = OneDriveUtils.getOneDriveExpImpDir();

		ProjectOneDriveImport dialog = new ProjectOneDriveImport(shell);		
		if(dialog.open() == ProjectOneDriveImport.OK)
		{
			doRun(dialog);
			
		}
	}

	@Override
	protected Map<String, String[]> prepareProjectResourceMap(File sourceImportDir, EObject[] importObjects)
	{
		// alle Unterverzeichnisse filtern (die IProjekte sind in Unterverzeichnissen gespeichert)
		File[] parentDirs = sourceImportDir.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
		
		// in einer Map werden fuer jede ProjektID = key; und als value die zugehoerigen Resourcen gespeichert 
		Map<String, String[]>mapImportFiles = new HashMap<String, String[]>();
		
		for(EObject eObject : importObjects)
		{
			if (eObject instanceof NtProject)
			{
				// die zuimportierenden Resourcen pro NtProjekt ermitteln und im Map speichern
				String projectID = ((NtProject)eObject).getId();
				File fileDir = getProjectDirectory(parentDirs, projectID);
				if(fileDir != null)
				{
					String [] srcFiles = fileDir.list(new FilenameFilter()
					{						
						@Override
						public boolean accept(File dir, String name)
						{							
							return !name.equals(".project");
						}
					});
					
					// das Projektverzeichnis wird vorangestellt
					for(int i = 0;i < srcFiles.length;i++)
						srcFiles[i] = fileDir.getPath()+File.separator+srcFiles[i];
					
					mapImportFiles.put(projectID, srcFiles);
				}
			}
		}
		
		return mapImportFiles;
	}
	
	private File getProjectDirectory(File[] parentDirs, String projectID)
	{
		for(File parentDir : parentDirs)
		{
			File[] childDirs = parentDir.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
			if (ArrayUtils.isNotEmpty(childDirs))
			{
				for(File childDir : childDirs)
				{
					// wenn eine Property-Datei 'NtProjectProperty.EXPIMP_NTPROJECTDATA_FILE' existiert sind wir im Project
					File propertyFile = new File(childDir, NtProjectProperty.EXPIMP_NTPROJECTDATA_FILE);
					if (propertyFile.exists())
					{					
						// NtProperty lesen und NtProjectID mit 'projectID' vergleichen
						EList<EObject> eObjects = ExpImpUtils.loadEObjectFromResource(propertyFile);
						NtProject ntProject = ((NtProject) eObjects.get(0));
						if(StringUtils.equals(projectID, ntProject.getId()))
							return childDir;
					}
				}
			}
		}
		
		return null;
	}
	
	/*
	 * Im Transferverzeichnis sind die Projekte (IProjects) jeweils im
	 * Unterverzeichnis des Parentordners dessen Name lediglich das Projekt
	 * beschreibt (muss ein guelte Verzeichnisname sein) der gespeichert
	 */
	private List<NtProject> readDefaultPropertyFiles(String importDirPath)
	{
		File propertyFile;
		List<NtProject> ntProjects = new ArrayList<NtProject>();
		File importDir = new File(importDirPath);
		File[] parentDirs = importDir.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
		if (ArrayUtils.isNotEmpty(parentDirs))
		{
			for (File parentDir : parentDirs)
			{
				File[] childDirs = parentDir
						.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
				if (ArrayUtils.isNotEmpty(childDirs))
				{
					for (File childDir : childDirs)
					{
						// Datei mit der Defaulteigenschaft
						propertyFile = new File(childDir,
								NtProjectProperty.EXPIMP_NTPROJECTDATA_FILE);
						if (propertyFile.exists())
						{
							EList<EObject> eObjects = ExpImpUtils
									.loadEObjectFromResource(propertyFile);
							ntProjects.add((NtProject) eObjects.get(0));
						}
					}
				}
			}
		}

		return ntProjects;
	}


}
