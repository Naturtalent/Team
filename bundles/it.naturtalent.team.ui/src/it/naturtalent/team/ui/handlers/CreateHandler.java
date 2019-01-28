 
package it.naturtalent.team.ui.handlers;

import java.io.IOException;

import javax.inject.Named;

import org.eclipse.core.resources.IProject;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.swt.widgets.Shell;

import it.naturtalent.team.ui.TeamUtils;

public class CreateHandler
{
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) @Optional Shell shell, @Optional EPartService partService)
	{	
		try
		{
			// ein neues lokales Repository im Defaultverzeichnis erzeugen
			TeamUtils.createLocalRepository();
			
			// ein neues remote Repository im Defaultverzeichnis erzeugen
			TeamUtils.createDefaultRemoteRepository();
			
			// master-branch erzeugen (mit DummyFile als content)
			TeamUtils.createMasterDummyFile();
			TeamUtils.addProjectCommand();			
			TeamUtils.commitCommand("initial Master");
			
			MessageDialog.openInformation(shell,"Repository","Ein Repository wurde erfolgreich erzeugt"); //$NON-NLS-N$
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@CanExecute
	public boolean canExecute()
	{
		// Disable, wenn das DefaultRepository vorhanden ist
		return(!TeamUtils.existDefaultGit());
	}

		
}