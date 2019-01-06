 
package it.naturtalent.team.ui.handlers;

import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jgit.lib.Repository;

import it.naturtalent.team.ui.TeamUtils;

public class PullHandler
{
	@Execute
	public void execute()
	{
		Repository repos = TeamUtils.getRepository();
		Set<String>names = repos.getRemoteNames();
		
		
		System.out.println("Pull");
	}

	@CanExecute
	public boolean canExecute(@Optional EPartService partService)
	{
		// Disable, wenn kein IProject selektiert ist
		if(TeamUtils.getSelectedIProject(partService) == null)
			return false;
		
		// Disable, wenn kein lokales Repository existiertk
		if(!TeamUtils.existDefaultGit())
			return false;
		
		// Disable, wenn kein externes Repository kontaktiert ist
		
			
		// Disable, wenn kein IProjekt im Repository vorhanden ist 
		IProject iProject = TeamUtils.getSelectedIProject(partService);
		return TeamUtils.existProjectInRepository(iProject);
	}
		
}