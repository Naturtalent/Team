 
package it.naturtalent.team.ui.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.swt.widgets.Shell;

import it.naturtalent.team.ui.TeamUtils;

import javax.inject.Named;

import org.eclipse.core.resources.IProject;
import org.eclipse.e4.core.di.annotations.CanExecute;

public class SynchronHandler
{
	@Execute
	public void execute(@Optional EPartService partService, 
			@Named(IServiceConstants.ACTIVE_SHELL) @Optional Shell shell)
	{
		String message = "Projekt wurde synchronisiert"; //$NON-NLS-N$;
		
		IProject iProject = TeamUtils.getSelectedIProject(partService);
		if(iProject != null)
		{
			// Projekt auschecken - HEAD auf den Projektbranch ausrichten
			try
			{
				// Projekt auschecken - HEAD auf den Projektbranch ausrichten
				TeamUtils.checkoutProject(iProject);
				
				// die aktuellen Projektressourcen in den Workspace kopieren
				TeamUtils.copyToRepositoryWorkspace(iProject);
				
				// Remote-Projektbranch pullen (fetch und merge) und im Workspace auschecken
				message = message + "\n" + TeamUtils.synchronizeProject(iProject);

				// Workspace in das Projekt kopieren
				TeamUtils.copyFromRepositoryWorkspace(iProject);
				
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		MessageDialog.openInformation(shell,"Team",message); //$NON-NLS-N$

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