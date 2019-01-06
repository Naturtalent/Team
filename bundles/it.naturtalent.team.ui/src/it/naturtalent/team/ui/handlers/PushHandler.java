 
package it.naturtalent.team.ui.handlers;

import javax.inject.Named;

import org.eclipse.core.resources.IProject;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jgit.api.StatusCommand;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.swt.widgets.Shell;

import it.naturtalent.team.ui.TeamUtils;

public class PushHandler
{
	@Execute
	public void execute(@Optional EPartService partService, 
			@Named(IServiceConstants.ACTIVE_SHELL) @Optional Shell shell)
	{		
		try
		{			
			// das momentan selektierte Projekt
			IProject iProject = TeamUtils.getSelectedIProject(partService);
		
			// Projektinhalt in den RepositoryWorkspace kopieren
			TeamUtils.copyToRepositoryWorkspace(iProject);
			
			// die Resourcen des Projekts stagen
			DirCache dirCache = TeamUtils.addProject(iProject);
			
			// Projekt comitten
			RevCommit revCommit = TeamUtils.commitProject(iProject);
			
			// in das RemoteRepository pushen
			TeamUtils.pushRepository();
			
			MessageDialog.openInformation(shell,"Repository","Projekt wurde in das Repository übertragen"); //$NON-NLS-N$
			
		} catch (Exception e)
		{
			MessageDialog.openError(shell,"Repository","Error committing"); //$NON-NLS-N$
		}
	}

	@CanExecute
	public boolean canExecute(@Optional EPartService partService)
	{
		// Disable wenn kein IProject selektiert ist
		if(TeamUtils.getSelectedIProject(partService) == null)
			return false;
		
		// Enable wenn ein Repository existiert
		return(TeamUtils.existDefaultGit());
	}
		
}