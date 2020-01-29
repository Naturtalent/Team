 
package it.naturtalent.team.ui.handlers;

import java.util.List;

import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import it.naturtalent.team.ui.TeamUtils;
import it.naturtalent.team.ui.dialogs.MergeConflictDialog;
import it.naturtalent.team.ui.preferences.TeamPreferenceAdapter;


/**
 * Lokales Projekt in ein remote Repository hochladen.
 * 
 * @author dieter
 *
 */
public class PushHandler
{
	@Execute
	public void execute(@Optional EPartService partService, 
			@Named(IServiceConstants.ACTIVE_SHELL) @Optional Shell shell)
	{		
		// das momentan selektierte Projekt
		IProject iProject = TeamUtils.getSelectedIProject(partService);		
		if(iProject != null)
		{
			String message = "ein Projekt hochgeladen "; //$NON-NLS-N$;			
			try
			{		
				// die aktuellen Projektressourcen in den Workspace kopieren
				TeamUtils.copyToRepository(iProject);
				
				TeamUtils.addCommand(iProject);
				
				/*
				
				// Committen
				TeamUtils.commitCommand(null);

				// push
				TeamUtils.pushProjectBranch(iProject);
				
							*/
				
			} catch (Exception e)
			{

				// TODO Auto-generated catch block
				// e.printStackTrace();
				message = "Push Error\n" + e.getMessage();
			}
			
			

			MessageDialog.openInformation(shell, "Team upgrade", message); // $NON-NLS-N$
		}
	}

	@CanExecute
	public boolean canExecute(@Optional EPartService partService)
	{
		// Disable, wenn kein IProject selektiert ist
		IProject iProject = TeamUtils.getSelectedIProject(partService);
		if(iProject == null)
			return false;
		
		// Enable, wenn ein lokales Repository existiertk	
		return(TeamUtils.existProjectRepository(iProject));
	}
	
	/*
	 * prueft ob bereits ein Remote Repoitory vorhanden ist. Wenn nicht wird eins erzeugt.
	 * 
	 */
	private Repository checkAndCreateRemoteRepos(IProject iProject) throws Exception
	{
		IEclipsePreferences instancePreferenceNode = InstanceScope.INSTANCE.getNode(TeamPreferenceAdapter.ROOT_TEAM_PREFERENCES_NODE);		
		String remoteReposDirectory = instancePreferenceNode.get(TeamPreferenceAdapter.PREFERENCE_REMOTE_REPOSDIR_KEY, null);
		if(StringUtils.isNotEmpty(remoteReposDirectory))
		{
			Repository remoteRepository = TeamUtils.createRemoteRepository(remoteReposDirectory, iProject);
		}
		
		return null;
	}
		
}