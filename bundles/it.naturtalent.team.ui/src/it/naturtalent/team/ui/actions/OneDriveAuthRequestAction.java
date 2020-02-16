package it.naturtalent.team.ui.actions;

import java.io.IOException;
import java.net.URISyntaxException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import it.naturtalent.team.model.team.OneDrive;
import it.naturtalent.team.ui.OneDriveHelper;

public class OneDriveAuthRequestAction extends Action
{

	@Override
	public void run()
	{
		
		OneDrive ondeDrive = OneDriveHelper.loadOneDriveInfo();
				 
		OneDriveHelper helper = new OneDriveHelper();
		
		// relevante Parameter an den Helper uebergeben
		String [] scopes = new String [] {"offline_access", "files.readwrite.all"};
		helper.setAuth_url(ondeDrive.getAuthURL());
		helper.setScopes(scopes);
		helper.setClient_id(ondeDrive.getClientID());
		helper.setRedirect_uri(ondeDrive.getRedirectURI());
		
		try
		{
			helper.requestAuth();
			MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Autorisierung", "Autorisierung war offensichtlich erfolgreich\nCode aus der Webseite übernehmen");
			
		} catch (IOException | URISyntaxException e)
		{
			MessageDialog.openError(Display.getDefault().getActiveShell(), "Autorisierung", "Fehler bei der Autorisierung\n"+e.getMessage());			
		}
	}

}
