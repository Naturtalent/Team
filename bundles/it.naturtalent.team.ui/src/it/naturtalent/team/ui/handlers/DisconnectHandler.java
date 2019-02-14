 
package it.naturtalent.team.ui.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import it.naturtalent.team.ui.TeamUtils;
import it.naturtalent.team.ui.dialogs.DisconnectTeamDialog;

import javax.inject.Named;

import org.eclipse.core.resources.IProject;
import org.eclipse.e4.core.di.annotations.CanExecute;

public class DisconnectHandler
{
	@Execute
	public void execute(@Optional EPartService partService, @Named(IServiceConstants.ACTIVE_SHELL) @Optional Shell shell)
	{			
		try
		{	
			IProject iProject = TeamUtils.getSelectedIProject(partService);
			if(iProject != null)
			{				
				if(MessageDialog.openQuestion(shell, "Team", "Projekt beim Team abmelden")) //$NON-NLS-N$
					TeamUtils.deleteBranchProjectCommand(iProject);
			}			
			
		} catch (Exception e3)
		{
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
	}

	@CanExecute
	public boolean canExecute(@Optional EPartService partService)
	{
		// Disable, wenn kein IProject selektiert ist
		IProject iProject = TeamUtils.getSelectedIProject(partService);
		if(iProject == null)
			return false;
		
		// Enable, wenn ein Projectbranch existiert
		return(TeamUtils.existLocalProjectBracnch(iProject));
	}

}