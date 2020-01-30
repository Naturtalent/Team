 
package it.naturtalent.team.ui.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.widgets.Shell;
import org.nuxeo.onedrive.client.OneDriveAPI;
import org.nuxeo.onedrive.client.OneDriveBusinessAPI;
import org.nuxeo.onedrive.client.OneDriveFolder;
import org.nuxeo.onedrive.client.OneDriveItem;

import it.naturtalent.e4.preferences.IPreferenceRegistry;
import it.naturtalent.team.ui.dialogs.CreateRepositoryDialog;
import it.naturtalent.team.ui.parts.RemoteRepositoryView;
import it.naturtalent.team.ui.preferences.TeamPreferenceAdapter;

/**
 * Ein neues Repository erzeugen
 * 
 * @author dieter
 *
 */
public class CreateRepostioryHandler
{
	
	// das zentrale Rreaferenzadapter Registry
	private @Inject @Optional IPreferenceRegistry preferenceRegistry;
	
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) @Optional Shell shell, @Optional MPart part)
	{
		// die Ausfuehrung erfolgt ueber einen Dialg
		CreateRepositoryDialog dialog = new CreateRepositoryDialog(shell);
		if (dialog.open() == CreateRepositoryDialog.OK)
		{
			
			//String url = "https://login.microsoftonline.de/bde4dffc-4b60-4cf6-8b04-a5eeb25f5c4f/oauth2/authorize?client%5Fid=00000003%2D0000%2D0ff1%2Dce00%2D000000000000&response%5Fmode=form%5Fpost&protectedtoken=true&response%5Ftype=code%20id%5Ftoken&resource=00000003%2D0000%2D0ff1%2Dce00%2D000000000000&scope=openid&nonce=48E7EF84844A3E6C4AE0B94257381C813508FDA1513A3F43%2DB1501FD7FBB4A2CA102311550A2FB57C20FE86D697E4C37164B60F50C83D21A3&redirect%5Furi=https%3A%2F%2Ftelekom%2Dmy%2Esharepoint%2Ede%2F%5Fforms%2Fdefault%2Easpx&wsucxt=1&cobrandid=11bd8083%2D87e0%2D41b5%2Dbb78%2D0bc43c8a8e8a&client%2Drequest%2Did=0b23309f%2Df0a2%2D0000%2D0a94%2D13faf3c99cb0";
			String url = "https://telekom-my.sharepoint.de/personal/dieter_apel_telekom_de/_layouts/15/onedrive.aspx";
			String token = "Dieter.Aoel@telekom.de"; 
			
			OneDriveAPI api = new OneDriveBusinessAPI(url, token);
			OneDriveFolder root = OneDriveFolder.getRoot(api);
			OneDriveFolder folder = new OneDriveFolder(api, "testuwe");
			List<String>urls = getChildren(folder);
			
			
			IEclipsePreferences instancePreferenceNode = InstanceScope.INSTANCE
					.getNode(TeamPreferenceAdapter.ROOT_TEAM_PREFERENCES_NODE);

			String remoteReposDirectory = dialog.getRemoteURI();
			if (StringUtils.isEmpty(remoteReposDirectory))
			{
				remoteReposDirectory = instancePreferenceNode.get(TeamPreferenceAdapter.PREFERENCE_TEAM_REPOSDIR_KEY, null);
				File remoteRepos = new File(remoteReposDirectory,TeamPreferenceAdapter.REMOTE_REPOSITORIESNAME);
				remoteReposDirectory = remoteRepos.getPath(); 
			}
			
			Object obj = part.getObject();
			if (obj instanceof RemoteRepositoryView)
			{
				RemoteRepositoryView remoteView = (RemoteRepositoryView) obj;
				remoteView.setRemoteUri(remoteReposDirectory);

				try
				{
					// als Praeferenz eintragen
					instancePreferenceNode.put(TeamPreferenceAdapter.PREFERENCE_REMOTE_REPOSDIR_KEY, remoteReposDirectory);
					instancePreferenceNode.flush();
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}

		}
	}
	
	private List<String> getChildren(OneDriveFolder folder)
	{
		List<String> urls = new ArrayList<>();
		for (OneDriveItem.Metadata metadata : folder) {
			if (metadata.isFile()) {
				urls.add(metadata.asFile().getDownloadUrl());
			} else if (metadata.isFolder()) {
				urls.addAll(getChildren(metadata.asFolder().getResource()));
			}

		}
		return urls;
	}
}