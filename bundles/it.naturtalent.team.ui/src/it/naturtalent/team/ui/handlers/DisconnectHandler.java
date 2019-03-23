 
package it.naturtalent.team.ui.handlers;

import java.util.List;

import javax.inject.Named;

import org.eclipse.core.resources.IProject;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import it.naturtalent.team.ui.TeamUtils;
import it.naturtalent.team.ui.actions.DisconnectAction;
import it.naturtalent.team.ui.actions.SynchronizeAction;
import it.naturtalent.team.ui.dialogs.DisconnectTeamDialog;

public class DisconnectHandler
{
	@Execute
	public void execute(@Optional IEclipseContext context, @Optional EPartService partService,
			@Named(IServiceConstants.ACTIVE_SHELL) @Optional Shell shell)
	{
		IProject iProject = TeamUtils.getSelectedIProject(partService);
		if (iProject != null)
		{
			DisconnectTeamDialog disconnectDialog = new DisconnectTeamDialog(shell);
			if(disconnectDialog.open() == DisconnectTeamDialog.OK)
			{
				DisconnectAction disconnectAction = ContextInjectionFactory
						.make(DisconnectAction.class, context);
				disconnectAction.run();
				
				if(disconnectDialog.isRemoteDisconnect())
					disconnectAction.disconnectRemote();
				
				MessageDialog.openInformation(shell, "Team",
						disconnectAction.getMessage()); // $NON-NLS-N$
			}
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