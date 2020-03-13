 
package it.naturtalent.team.ui.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.nuxeo.onedrive.client.OneDriveAPI;
import org.nuxeo.onedrive.client.OneDriveAPIException;
import org.nuxeo.onedrive.client.OneDriveBasicAPI;
import org.nuxeo.onedrive.client.OneDriveBusinessAPI;
import org.nuxeo.onedrive.client.OneDriveFolder;
import org.nuxeo.onedrive.client.OneDriveItem;

import it.naturtalent.team.model.team.OneDrive;
import it.naturtalent.team.ui.OneDriveHelper;

public class OneDriveCloudOpen
{
	@Execute
	public void execute()
	{
		OneDrive oneDrive =  OneDriveHelper.loadOneDriveInfo();
		String clientID = oneDrive.getClientID();
		String accessToken = oneDrive.getAccessToken();
		
		//OneDriveAPI api = new OneDriveBasicAPI(accessToken);
		
		
		
		//String url = "https://onedrive.live.com";
		String url = "https://login.microsoftonline.com/common/oauth2/v2.0";
		OneDriveAPI api = new OneDriveBusinessAPI(url, accessToken);
		OneDriveFolder root = OneDriveFolder.getRoot(api);
		OneDriveFolder oneDriveFolder = new OneDriveFolder(api, clientID);
		try
		{
			OneDriveFolder.Metadata metadata = oneDriveFolder.getMetadata();
			String path = metadata.getParentReference().getPath();
			
			System.out.println("Path:"+path);
			
		} catch (OneDriveAPIException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		Iterable<OneDriveItem.Metadata>childeren = oneDriveFolder.getChildren();
		
		System.out.println("ClientID:"+clientID+"  AccessToken: "+accessToken);
	}

}