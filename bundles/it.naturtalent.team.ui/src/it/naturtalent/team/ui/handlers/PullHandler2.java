 
package it.naturtalent.team.ui.handlers;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Named;

import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.MergeResult.MergeStatus;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.TrackingRefUpdate;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Shell;

import it.naturtalent.team.ui.TeamUtils;

public class PullHandler2
{
	@Execute
	public void execute(@Optional EPartService partService, 
			@Named(IServiceConstants.ACTIVE_SHELL) @Optional Shell shell)
	{
		// das momentan selektierte Projekt
		IProject iProject = TeamUtils.getSelectedIProject(partService);
		
		BusyIndicator.showWhile(shell.getDisplay(), () ->
		{
			String message = "Projekt wurde aktualisiert "; //$NON-NLS-N$;
			
			try
			{
				
				// 'master' auschecken - 'loescht' den Workspace 
				TeamUtils.checkoutCommand(null);
				TeamUtils.resetCommand();
				
				// Projekt auschecken - HEAD auf den Projektbranch ausrichten
				TeamUtils.checkoutProject(iProject);
				
				// die aktuellen Projektressourcen in den Workspace kopieren
				TeamUtils.copyToRepository(iProject);
				
				// Remote-Projektbranch pullen (fetch und merge) und im Workspace auschecken
				//PullResult pullResult = TeamUtils.pullProject(iProject);

				// Workspace in das Projekt kopieren
				TeamUtils.copyFromRepository(iProject);
				
			} catch (Exception e)
			{
				if (e instanceof CheckoutConflictException)
				{
					
				}
				message = "Pull Error\n"+e.getMessage();
			}
			
			MessageDialog.openInformation(shell,"Repository",message); //$NON-NLS-N$
			
		});		
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