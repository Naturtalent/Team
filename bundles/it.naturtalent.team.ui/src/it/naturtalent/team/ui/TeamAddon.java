 
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
import it.naturtalent.e4.project.IExportAdapterRepository;
import it.naturtalent.e4.project.IImportAdapterRepository;
import it.naturtalent.team.ui.preferences.TeamPreferenceAdapter;

public class TeamAddon 
{

	// das zentrale Rreaferenzadapter Registry
	private @Inject @Optional IPreferenceRegistry preferenceRegistry;
	
	private @Inject @Optional IImportAdapterRepository importAdapterRepository;
	
	private @Inject @Optional IExportAdapterRepository exportAdapterRepository;
	
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
		
		if(importAdapterRepository != null)
			importAdapterRepository.addImportAdapter(new TransferImportAdapter());		
		
		if(exportAdapterRepository != null)
			exportAdapterRepository.addExportAdapter(new TransferExportAdapter());		
		
		
	}
	

}
