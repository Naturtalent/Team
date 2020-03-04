package it.naturtalent.team.ui.dialogs;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Shell;

import it.naturtalent.e4.project.expimp.dialogs.ProjectImportDialog;
import it.naturtalent.e4.project.model.project.NtProject;
import it.naturtalent.e4.project.ui.emf.ExpImpUtils;
import it.naturtalent.e4.project.ui.emf.NtProjectProperty;
import it.naturtalent.team.ui.OneDriveUtils;

public class ProjectOneDriveImport extends ProjectImportDialog
{

	public ProjectOneDriveImport(Shell parentShell)
	{
		super(parentShell);
		// TODO Auto-generated constructor stub
	}

	/*
	 * 
	 */
	@Override
	public void initViewer(String importDirPath)
	{
		// das DefaultTransferverzeichnis vorbelegen
		File oneDriveExpDir = OneDriveUtils.getOneDriveExpImpDir();
		comboSourceDir.setText(oneDriveExpDir.getPath());

		List<NtProject> ntProjects = readDefaultPropertyFiles(comboSourceDir.getText());
		checkboxTableViewer.setInput(ntProjects);
		disableExistObjects(ntProjects);
	}

	/*
	 * Im Transferverzeichnis sind die Projekte (IProjects) jeweils im
	 * Unterverzeichnis des Parentordners dessen Name lediglich das Projekt
	 * beschreibt (muss ein guelte Verzeichnisname sein) der gespeichert
	 */
	protected List<NtProject> readDefaultPropertyFiles(String importDirPath)
	{
		File propertyFile;
		List<NtProject> ntProjects = new ArrayList<NtProject>();
		File importDir = new File(importDirPath);
		File[] parentDirs = importDir
				.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
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

	@Override
	protected void storeSettings()
	{
		// Settings werden nicht unterstuetzt		
	}

	

	
	

	

}
