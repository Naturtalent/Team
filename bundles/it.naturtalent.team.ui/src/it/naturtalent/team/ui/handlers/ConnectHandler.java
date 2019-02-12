 
package it.naturtalent.team.ui.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import it.naturtalent.team.ui.TeamUtils;

import org.eclipse.core.resources.IProject;
import org.eclipse.e4.core.di.annotations.CanExecute;

public class ConnectHandler
{
	@Execute
	public void execute(@Optional EPartService partService)
	{
		IProject iProject = TeamUtils.getSelectedIProject(partService);
		if(iProject != null)
		{
			try
			{
				TeamUtils.createProjectBranch(iProject);
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
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
		
		// Disable, wenn bereits ein Projectbranch existiert
		return(!TeamUtils.existLocalProjectBracnch(iProject));
	}
		
}