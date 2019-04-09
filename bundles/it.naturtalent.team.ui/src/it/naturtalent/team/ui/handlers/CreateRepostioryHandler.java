 
package it.naturtalent.team.ui.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.widgets.Shell;

import it.naturtalent.team.ui.dialogs.CreateRepositoryDialog;
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
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) @Optional Shell shell)
	{
		// die Ausfuehrung erfolgt ueber einen Dialg
		CreateRepositoryDialog dialog = new CreateRepositoryDialog(shell);
		dialog.open();
	}
}