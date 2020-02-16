 
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
import org.nuxeo.onedrive.client.OneDriveBasicAPI;
import org.nuxeo.onedrive.client.OneDriveBusinessAPI;
import org.nuxeo.onedrive.client.OneDriveFolder;
import org.nuxeo.onedrive.client.OneDriveItem;

import it.naturtalent.e4.preferences.IPreferenceRegistry;
import it.naturtalent.team.model.team.OneDrive;
import it.naturtalent.team.ui.OneDriveHelper;
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
			
			OneDrive oneDrive = OneDriveHelper.loadOneDriveInfo();
			String token = oneDrive.getAccessToken();
						
			//OneDriveAPI api = new OneDriveBusinessAPI(url, token);
			OneDriveAPI api = new OneDriveBasicAPI(token);
			
			OneDriveFolder root = OneDriveFolder.getRoot(api);
			OneDriveFolder folder = new OneDriveFolder(api, "testuwe");
			List<String>urls = getChildren(folder);
			
			System.out.println(urls);
			
			/*
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
			*/

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