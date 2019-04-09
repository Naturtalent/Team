 
package it.naturtalent.team.ui.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Shell;

import it.naturtalent.team.model.team.ReposData;
import it.naturtalent.team.ui.TeamModelUtils;
import it.naturtalent.team.ui.preferences.TeamPreferenceAdapter;

public class RefreshHandler
{
	
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) @Optional Shell shell, @Optional IEventBroker eventBroker,
			@Preference(nodePath = TeamPreferenceAdapter.ROOT_TEAM_PREFERENCES_NODE, value = TeamPreferenceAdapter.PREFERENCE_TEAM_REMOTEREPOS_URI) String reposDir)
	{
		
		BusyIndicator.showWhile(shell.getDisplay(), () -> 
		{
			ReposData reposData = TeamModelUtils.getRemoteReposData();
			reposData.setRemoteURI(reposDir);
			eventBroker.post(TeamModelUtils.REFRESH_PROPJECTBRANCH_VIEW_EVENT, reposData);
		});		
		
		
		System.out.println("refresh");
	}
		
}