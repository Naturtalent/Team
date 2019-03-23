package it.naturtalent.team.ui.actions;

import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.core.resources.IProject;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.action.Action;

import it.naturtalent.team.ui.TeamUtils;

public class ConnectAction extends Action
{
	private IProject iProject = null;
	
	private String message = "Verbindung zum Team"; //$NON-NLS-N$;
	
	@PostConstruct
	public void postConstruction(@Optional EPartService partService)
	{
		iProject = TeamUtils.getSelectedIProject(partService);		
	}
	
	@Override
	public void run()
	{
		if(iProject != null)
		{
			try
			{
				String branchName = iProject.getName();
								
				// Projektbranch erzeugen - HEAD auf den Projektbranch ausrichten
				TeamUtils.createLocalBranch(branchName);
				
				TeamUtils.createRemoteBranch(iProject);
				
				
			} catch (Exception e)
			{				
				//e.printStackTrace();
				message = message + "\n" + e.getMessage();
			}
		}
	}
	
	public String getMessage()
	{
		return message;
	}
	
	
	
}
