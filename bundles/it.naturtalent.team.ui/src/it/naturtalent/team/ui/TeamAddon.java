 
package it.naturtalent.team.ui;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.RemoteAddCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.transport.URIish;
import org.osgi.service.event.Event;

import it.naturtalent.e4.preferences.IPreferenceRegistry;
import it.naturtalent.team.ui.preferences.TeamPreferenceAdapter;

public class TeamAddon 
{

	private @Inject @Optional IPreferenceRegistry preferenceRegistry;
	
	@Inject
	@Optional
	public void applicationStarted(
			@EventTopic(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE) Event event)
	{
		if(preferenceRegistry != null)	
		{
			// Adapter der Teampraeferenzen eintragen
			preferenceRegistry.getPreferenceAdapters().add(new TeamPreferenceAdapter());
			
	
			IEclipsePreferences defaultPreferenceNode = DefaultScope.INSTANCE
					.getNode(TeamPreferenceAdapter.ROOT_TEAM_PREFERENCES_NODE);
						
			// DefaultLocalRepository werden definiert			
			File defaultReposDir = new File(System.getProperty(
				it.naturtalent.application.Activator.NT_PROGRAM_HOME),
				TeamPreferenceAdapter.REPOSITORIES_DIRECRORYNAME);
			defaultReposDir = new File(defaultReposDir,TeamPreferenceAdapter.DEFAULT_REPOSITORYNAME);
			String reposDir = defaultReposDir.getAbsolutePath();
			defaultPreferenceNode.put(TeamPreferenceAdapter.PREFERENCE_TEAM_REPOSDIR, reposDir);
		
			// 	sicherstellen, dass das Default Lokalerepository Verzeichnis existiert		
			if(!defaultReposDir.exists())
				defaultReposDir.mkdir();		
		
			// existiert bereits ein Default Local Repository
			if(!TeamUtils.isExisitingRepository(reposDir + File.separator + TeamUtils.DOT_GIT))
			{
				try
				{
					// Default Local Repository erzeugen
					TeamUtils.createLocalRepository();
					
					// master-branch erzeugen (mit DummyFile als content)
					TeamUtils.createMasterDummyFile();
					TeamUtils.addProjectCommand();			
					TeamUtils.commitCommand("initial Master");
					
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// DefaultRemoteRepository werden definiert
			File defaultRemoteReposDir = new File(System.getProperty(
					it.naturtalent.application.Activator.NT_PROGRAM_HOME),
					TeamPreferenceAdapter.REPOSITORIES_DIRECRORYNAME);
			defaultRemoteReposDir = new File(defaultRemoteReposDir,
					TeamPreferenceAdapter.DEFAULT_REMOTE_REPOSITORYNAME);
			String remoteDefaultReposDir = defaultRemoteReposDir.getAbsolutePath();	
			
			// sicherstellen, dass das Default Remote-Repository Verzeichnis existiert		
			if(!defaultRemoteReposDir.exists())
				defaultRemoteReposDir.mkdir();		
			
			URI remoteURI = defaultRemoteReposDir.toURI();					
			defaultPreferenceNode.put(
					TeamPreferenceAdapter.PREFERENCE_TEAM_REMOTEREPOS_URI, remoteURI.toString());
			
			// Default Remote Repository checken
			if(!TeamUtils.isExisitingRepository(remoteDefaultReposDir))
			{
				try
				{
					// Default Remote Repository erzeugen
					String remoteDirURI = defaultPreferenceNode.get(
							TeamPreferenceAdapter.PREFERENCE_TEAM_REMOTEREPOS_URI, null);
					TeamUtils.createRemoteRepository(remoteDirURI);
					
					// 'master' pushen
					TeamUtils.setRemote(remoteDirURI);
					TeamUtils.pushCommand();
					
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		}
	}

}
