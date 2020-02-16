package it.naturtalent.team.ui.actions;

import java.io.IOException;
import java.net.URISyntaxException;

import org.eclipse.jface.action.Action;

import it.naturtalent.team.model.team.OneDrive;
import it.naturtalent.team.ui.OneDriveHelper;

public class OneDriveTokenRequestAction extends Action
{

	@Override
	public void run()
	{
		
		OneDrive ondeDrive = OneDriveHelper.loadOneDriveInfo();
				 
		OneDriveHelper helper = new OneDriveHelper();
		
		// relevante Parameter an den Helper uebergeben
		String [] scopes = new String [] {"offline_access", "files.readwrite.all"};
		helper.setAuth_url(ondeDrive.getAuthURL());
		helper.setClient_id(ondeDrive.getClientID());		
		helper.setScopes(scopes);		
		helper.setRedirect_uri(ondeDrive.getRedirectURI());
		
		try
		{
			helper.requestToken();
		} catch (IOException | URISyntaxException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
