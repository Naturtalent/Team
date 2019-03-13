 
package it.naturtalent.team.ui.handlers;

import javax.inject.Named;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import it.naturtalent.e4.project.ui.utils.RefreshResource;
import it.naturtalent.team.ui.TeamUtils;
import it.naturtalent.team.ui.actions.SynchronizeAction;

public class SynchronHandler
{
	
	@Execute
	public void execute(@Optional IEclipseContext context, @Optional EPartService partService, 
			@Named(IServiceConstants.ACTIVE_SHELL) @Optional Shell shell)
	{
		IProject iProject = TeamUtils.getSelectedIProject(partService);
		if (iProject != null)
		{
			if (MessageDialog.openQuestion(shell, "Team","Projekt synchronisieren?"))
			{
				SynchronizeAction sysnchronizeAction = ContextInjectionFactory
						.make(SynchronizeAction.class, context);
				sysnchronizeAction.run();
				MessageDialog.openInformation(shell, "Team",
						sysnchronizeAction.getMessage()); // $NON-NLS-N$

				// Projekt im Resourcenviewer refreshen
				//RefreshResource refreshResource = new RefreshResource();
				//refreshResource.refresh(shell, iProject);
				try
				{
					iProject.refreshLocal(IResource.DEPTH_INFINITE, null);
				} catch (CoreException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
		
		// Enable, wenn ein Projectbranch existiert
		return(TeamUtils.existLocalProjectBracnch(iProject));		
	}

		
}