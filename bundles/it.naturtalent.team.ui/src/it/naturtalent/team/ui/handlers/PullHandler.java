 
package it.naturtalent.team.ui.handlers;

import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.swt.widgets.Shell;

import it.naturtalent.team.ui.TeamUtils;

public class PullHandler
{
	@Execute
	public void execute(@Optional EPartService partService, 
			@Named(IServiceConstants.ACTIVE_SHELL) @Optional Shell shell)
	{
		// das momentan selektierte Projekt
		IProject iProject = TeamUtils.getSelectedIProject(partService);
		if (iProject != null)
		{
			String message = "Projektdaten wurden heruntergeladen"; // $NON-NLS-N$;
			try
			{
				// Remote-Projektbranch pullen (fetch und merge) und im
				// Workspace
				// auschecken
				String result = TeamUtils.pullProject(iProject);
				if(StringUtils.isNotEmpty(result))
				{
					message = "Pull Error\n" + result;
				
					TeamUtils.statusCommandTEST();
				}
				else
				{
					// Workspace in das Projekt kopieren
					TeamUtils.copyFromRepositoryWorkspace(iProject);
				}
				
			} catch (Exception e)
			{
				message = "Pull Error\n" + e.getMessage();
			}
			
			
			
			MessageDialog.openInformation(shell, "Team downgrade", message); // $NON-NLS-N$
		}
	}

	@CanExecute
	public boolean canExecute(@Optional EPartService partService)
	{
		// Disable, wenn kein IProject selektiert ist
		if(TeamUtils.getSelectedIProject(partService) == null)
			return false;
		
		// Disable, wenn kein lokales Repository existiertk
		return(TeamUtils.existDefaultGit());
	}
		
}