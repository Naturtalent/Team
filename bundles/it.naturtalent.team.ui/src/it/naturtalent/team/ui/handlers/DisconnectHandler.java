 
package it.naturtalent.team.ui.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import it.naturtalent.team.ui.TeamUtils;

import org.eclipse.core.resources.IProject;
import org.eclipse.e4.core.di.annotations.CanExecute;

public class DisconnectHandler
{
	@Execute
	public void execute(@Optional EPartService partService)
	{			
		try
		{	
			IProject iProject = TeamUtils.getSelectedIProject(partService);
			TeamUtils.deleteBranchProjectCommand(iProject);
			
		} catch (Exception e3)
		{
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
	}

	@CanExecute
	public boolean canExecute()
	{

		return true;
	}

}