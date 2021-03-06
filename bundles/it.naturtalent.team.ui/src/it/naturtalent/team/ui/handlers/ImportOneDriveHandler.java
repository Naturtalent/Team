 
package it.naturtalent.team.ui.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkingSet;

import it.naturtalent.e4.project.expimp.dialogs.ProjectImportDialog;
import it.naturtalent.team.ui.OneDriveUtils;
import it.naturtalent.team.ui.dialogs.ProjectOneDriveImport;

import java.io.File;
import java.io.IOException;

import javax.inject.Named;

import org.apache.commons.lang3.SystemUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.core.di.annotations.CanExecute;

public class ImportOneDriveHandler
{
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) @Optional IAdaptable iAdaptable, 
			@Named(IServiceConstants.ACTIVE_SHELL) @Optional Shell shell)
	{
		
		File oneDriveExpDir = OneDriveUtils.getOneDriveExpImpDir();
		
		ProjectOneDriveImport dialog = new ProjectOneDriveImport(shell);
		dialog.create();
		dialog.initViewer(oneDriveExpDir.getPath());
		
		dialog.open();
		
		
		//openImportDir(oneDriveExpDir.getPath());
		
	}
	
	private void openImportDir(String oneDriveExpDir)
	{
		try
		{
			if (SystemUtils.IS_OS_LINUX)
				Runtime.getRuntime().exec("nautilus " + oneDriveExpDir);					
			else
				Runtime.getRuntime().exec("explorer " + oneDriveExpDir);
		} catch (IOException exp)
		{
			if (SystemUtils.IS_OS_LINUX)
				try
				{
					Runtime.getRuntime().exec("nemo " + oneDriveExpDir);
					return;
				} catch (Exception e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			exp.printStackTrace();
		
		}


	}

	@CanExecute
	public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) @Optional IAdaptable iAdaptable)
	{
		return (iAdaptable instanceof IWorkingSet) ? true : false;
	}
		
}