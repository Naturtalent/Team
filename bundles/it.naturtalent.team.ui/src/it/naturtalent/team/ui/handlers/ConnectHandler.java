 
package it.naturtalent.team.ui.handlers;

import javax.inject.Named;

import org.eclipse.core.resources.IProject;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import it.naturtalent.team.ui.TeamUtils;
import it.naturtalent.team.ui.actions.ConnectAction;

public class ConnectHandler
{
	
	@Execute
	public void execute(@Optional IEclipseContext context,
			@Optional EPartService partService,
			@Named(IServiceConstants.ACTIVE_SHELL) @Optional Shell shell)
	{
		IProject iProject = TeamUtils.getSelectedIProject(partService);
		if(iProject != null)
		{
			if (MessageDialog.openQuestion(shell, "Team","Projekt wird beim Team angemeldet")) //$NON-NLS-N$
			{	
				ConnectAction connectAction = ContextInjectionFactory.make(ConnectAction.class, context);
				connectAction.run();
			}
		}
	}

	@CanExecute
	public boolean canExecute(@Optional EPartService partService)
	{
		// Disable, wenn kein IProject selektiert ist
		IProject iProject = TeamUtils.getSelectedIProject(partService);
		if(iProject == null)
			return false;
		
		// Enable, wenn noch kein lokales Repository fuer dieses Projekt existiert
		return(!TeamUtils.existProjectRepository(iProject));
	}

}