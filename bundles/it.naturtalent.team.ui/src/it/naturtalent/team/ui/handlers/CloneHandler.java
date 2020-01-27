 
package it.naturtalent.team.ui.handlers;

import javax.inject.Named;

import org.eclipse.core.resources.IProject;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.widgets.Shell;

import it.naturtalent.e4.project.INtProjectPropertyFactoryRepository;
import it.naturtalent.team.model.team.Branch;
import it.naturtalent.team.ui.TeamModelUtils;
import it.naturtalent.team.ui.actions.CloneAction;

public class CloneHandler
{
	
	@Execute
	public void execute(@Optional IEclipseContext context,
			@Named(IServiceConstants.ACTIVE_SELECTION) @Optional Branch selectedBranch,
			@Named(IServiceConstants.ACTIVE_SHELL) @Optional Shell shell,
			@Optional INtProjectPropertyFactoryRepository projektDataFactoryRepository)			
	{
		
		/*
		if(selectedBranch != null)
		{
			IProject iProject = TeamModelUtils.getProjectBranchProject(selectedBranch);
			if (iProject == null)
			{
				CloneAction cloneAction = ContextInjectionFactory.make(CloneAction.class, context);
				cloneAction.run();
			}
		}
		*/		
	}

	@CanExecute
	public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) @Optional Branch selectedBranch)
	{

		return false;
	}
		
}