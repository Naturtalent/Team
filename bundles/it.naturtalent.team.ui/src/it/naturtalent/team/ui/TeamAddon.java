 
package it.naturtalent.team.ui;

import java.io.File;

import javax.inject.Inject;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.osgi.service.event.Event;

import it.naturtalent.e4.preferences.IPreferenceRegistry;
import it.naturtalent.team.ui.preferences.TeamPreferenceAdapter;

public class TeamAddon 
{

	// das zentrale Rreaferenzadapter Registry
	private @Inject @Optional IPreferenceRegistry preferenceRegistry;
	
	@Inject
	@Optional
	
	public void applicationStarted(@EventTopic(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE) Event event)
	{
		if(preferenceRegistry != null)	
		{
			// Adapter der Teampraeferenzen zum zentralen Register hinzufuegen
			preferenceRegistry.getPreferenceAdapters().add(new TeamPreferenceAdapter());
			
			// der Praeferenzknoten aller Teampreferenzen
			IEclipsePreferences instancePreferenceNode = InstanceScope.INSTANCE
					.getNode(TeamPreferenceAdapter.ROOT_TEAM_PREFERENCES_NODE);
			
			// sicherstellen, dass das Root-Repositoriesverzeichnis existiert
			File defaultDirectory = new File(System.getProperty(it.naturtalent.application.Activator.NT_PROGRAM_HOME));					
			String rootDirName = instancePreferenceNode.get(
					TeamPreferenceAdapter.PREFERENCE_TEAM_REPOSDIR_KEY,
					defaultDirectory.getPath()+File.separator+TeamPreferenceAdapter.REPOSITORIES_DIRECORYNAME);
			
			// Verzeichnis in dem alle Teamrepositores gespeichert werden (Root)		
			File rootReposDir = new File(rootDirName);
			if(!rootReposDir.exists())
			{
				// Rootverzeichnis anlegen und als Praeferenuz speichern
				if(rootReposDir.mkdir())
					instancePreferenceNode.put(TeamPreferenceAdapter.PREFERENCE_TEAM_REPOSDIR_KEY, rootDirName);	
			}
			
			TeamPreferenceAdapter.initRepositoriesInfrastructure(rootReposDir);
		}
	}
	
	/*
	public void applicationStarted(@EventTopic(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE) Event event)
	{
		if(preferenceRegistry != null)	
		{
			// Adapter der Teampraeferenzen eintragen
			preferenceRegistry.getPreferenceAdapters().add(new TeamPreferenceAdapter());
			
			// Default-Teampraeferenzknoten
			IEclipsePreferences defaultPreferenceNode = DefaultScope.INSTANCE
					.getNode(TeamPreferenceAdapter.ROOT_TEAM_PREFERENCES_NODE);
			
			IEclipsePreferences instancePreferenceNode = InstanceScope.INSTANCE
					.getNode(TeamPreferenceAdapter.ROOT_TEAM_PREFERENCES_NODE);
						
			// Ein Defaultverzeichnis fuer das lokale Repository festlegen			
			File defaultReposDir = new File(System.getProperty(
				it.naturtalent.application.Activator.NT_PROGRAM_HOME),
				TeamPreferenceAdapter.REPOSITORIES_DIRECRORYNAME);
			defaultReposDir = new File(defaultReposDir,TeamPreferenceAdapter.DEFAULT_REPOSITORYNAME);
			String reposDir = defaultReposDir.getAbsolutePath();
			defaultPreferenceNode.put(TeamPreferenceAdapter.PREFERENCE_TEAM_REPOSDIR, reposDir);
		
			// 	sicherstellen, dass das Default Lokalerepository Verzeichnis existiert		
			if(!defaultReposDir.exists())
				defaultReposDir.mkdir();		
		
			// existiert bereits ein Default Local Repository (.git - Verzeichnis im Workspace)
			if(!TeamUtils.isExisitingRepository(reposDir + File.separator + TeamUtils.DOT_GIT))
			{
				try
				{
					// Default Local Repository erzeugen
					TeamUtils.createLocalRepository();
					
					// 'master' - Branch configurieren
					TeamUtils.setBranchConfig("master");
					
					// URI auf Remote-Repository configurieren
					String remoteURI = instancePreferenceNode.get(
							TeamPreferenceAdapter.PREFERENCE_TEAM_REMOTEREPOS_URI,null);
					if(StringUtils.isNotEmpty(remoteURI))
						TeamUtils.setRemoteConfig(remoteURI);
					
					// master-branch erzeugen (mit DummyFile als content)
					//TeamUtils.createMasterDummyFile();
					TeamUtils.addCommand();			
					TeamUtils.commitCommand("initial Master");
					
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			//
			//  Default Remote Repository
			//
						
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
			
			URI remoteDefaultURI = defaultRemoteReposDir.toURI();					
			defaultPreferenceNode.put(
					TeamPreferenceAdapter.PREFERENCE_TEAM_REMOTEREPOS_URI, remoteDefaultURI.toString());
			
			// existiert ein Default Remote Repository
			if(!TeamUtils.isExisitingRepository(remoteDefaultReposDir))
			{
				try
				{
					// Default RemoteURI praferenzieren
					String remoteDirURI = defaultPreferenceNode.get(
							TeamPreferenceAdapter.PREFERENCE_TEAM_REMOTEREPOS_URI, null);
					
					// Default Remote Repository erzeugen
					TeamUtils.createRemoteRepository(remoteDirURI);
					
					// RemoteURI im lokalen Repository konfigurieren
					TeamUtils.setRemoteConfig(remoteDirURI);					
					
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
			
			//
			//  Remote Repository
			//
						
			String remoteURI = instancePreferenceNode.get(
					TeamPreferenceAdapter.PREFERENCE_TEAM_REMOTEREPOS_URI,null);
			
			if(StringUtils.isNotEmpty(remoteURI) && !StringUtils.equals(remoteDefaultReposDir, remoteURI))
			{				
				try
				{
					if(!TeamUtils.isExisitingRemoteRepository(remoteURI))
					{
						// neues Remote Repository anlegen
						TeamUtils.createRemoteRepository(remoteURI);
						
						// RemoteURI im lokalen Repository konfigurieren
						TeamUtils.setRemoteConfig(remoteURI);									
					}
					
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}			
		}
	}
	*/

}
