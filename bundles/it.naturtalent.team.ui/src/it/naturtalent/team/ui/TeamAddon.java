 
package it.naturtalent.team.ui;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.ui.workbench.UIEvents;
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
			preferenceRegistry.getPreferenceAdapters().add(new TeamPreferenceAdapter());
		}
	}

}
