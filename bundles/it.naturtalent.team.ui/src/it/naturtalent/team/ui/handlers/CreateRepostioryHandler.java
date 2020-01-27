 
package it.naturtalent.team.ui.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.widgets.Shell;

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
	
	
	
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) @Optional Shell shell, @Optional MPart part)
	{
		// die Ausfuehrung erfolgt ueber einen Dialg
		CreateRepositoryDialog dialog = new CreateRepositoryDialog(shell);
		if(dialog.open() == CreateRepositoryDialog.OK)
		{
			Object obj = part.getObject();
			if (obj instanceof RemoteRepositoryView)
			{
				RemoteRepositoryView remoteView = (RemoteRepositoryView) obj;
				remoteView.setRemoteUri(dialog.getRemoteURI());
				
			}
		}
	}
}