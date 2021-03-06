 
package it.naturtalent.team.ui.handlers;

import javax.inject.Named;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Shell;

import hk.quantr.sharepoint.SPOnline;
import it.naturtalent.team.model.team.Login;
import it.naturtalent.team.model.team.ReposData;
import it.naturtalent.team.ui.LoginManager;
import it.naturtalent.team.ui.TeamModelUtils;
import it.naturtalent.team.ui.dialogs.LoginRemoteReposDialog;
import it.naturtalent.team.ui.preferences.TeamPreferenceAdapter;

public class RefreshHandler
{
	
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) @Optional Shell shell, @Optional IEventBroker eventBroker,
			@Preference(nodePath = TeamPreferenceAdapter.ROOT_TEAM_PREFERENCES_NODE, value = TeamPreferenceAdapter.PREFERENCE_REMOTE_REPOSDIR_KEY) String reposDir)
	{
		
		LoginRemoteReposDialog dialog = new LoginRemoteReposDialog(shell);
		if(dialog.open() == LoginRemoteReposDialog.OK)
		{
			//login(dialog.getLoginRemoteLogin());
			
			LoginManager loginMager = new LoginManager();
			loginMager.login();
			
		}
		
		
		/*
		BusyIndicator.showWhile(shell.getDisplay(), () -> 
		{
			ReposData reposData = TeamModelUtils.getRemoteReposData();
			reposData.setRemoteURI(reposDir);
			eventBroker.post(TeamModelUtils.REFRESH_PROPJECTBRANCH_VIEW_EVENT, reposData);
		});
		*/		
		
		
		//System.out.println("refresh");
	}
	
	private void login(Login loginRemoteLogin)
	{
		String user = loginRemoteLogin.getUser();
		String password = loginRemoteLogin.getPassword();
		String domain = loginRemoteLogin.getDomain();
		
		//user = "Apel, Dieter";
		user = "Dieter.Apel@telekom.de";
		password = "Majestix36";
		domain = "emea1";
				
		
		Pair<String, String> token = SPOnline.login(user, password, domain);		
		System.out.println(token);
		
	}
		
}