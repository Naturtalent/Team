 
package it.naturtalent.team.ui.handlers;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.inject.Named;

import org.eclipse.core.resources.IProject;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.MergeResult.MergeStatus;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.TrackingRefUpdate;
import org.eclipse.swt.widgets.Shell;

import it.naturtalent.team.ui.TeamUtils;

public class PullHandler
{
	@Execute
	public void execute(@Optional EPartService partService, 
			@Named(IServiceConstants.ACTIVE_SHELL) @Optional Shell shell)
	{
		String message = "Projekt wurde aktualisiert "; //$NON-NLS-N$;
		
		// das momentan selektierte Projekt
		IProject iProject = TeamUtils.getSelectedIProject(partService);
		
		try
		{
			// Projekt auschecken
			//TeamUtils.checkoutProject(iProject);
			
			// das gesamten Workspace vom externen Repository pullen (fetch und merge)
			PullResult pullResult = TeamUtils.pullRepository(iProject);

			TeamUtils.copyFromRepositoryWorkspace(iProject);
			
		} catch (Exception e)
		{
			message = "Pull Error\n"+e.getMessage();
		}
		
		
		MessageDialog.openInformation(shell,"Repository",message); //$NON-NLS-N$
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