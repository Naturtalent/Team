package it.naturtalent.team.ui.actions;

import javax.annotation.PostConstruct;

import org.eclipse.core.resources.IProject;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.action.Action;

import it.naturtalent.team.ui.TeamUtils;

public class DisconnectAction extends Action
{
	private IProject iProject = null;
	
	private String message = "Keine Verbindung zum Team"; //$NON-NLS-N$;
	
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
				// Projektbranch im lokalen Repository entfernen
				TeamUtils.deleteLocalProjectBranch(iProject);
				
				// Tracking Branch im lokalen Repository entfernen
				TeamUtils.deleteRemoteTrackingBranch(iProject);
				
			} catch (Exception e)
			{				
				//e.printStackTrace();
				message = message + "\n" + e.getMessage();
			}
		}
	}
	
	public void disconnectRemote()
	{
		/*
		try
		{
			TeamUtils.deleteRemoteTrackingBranch(iProject);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		*/
	}
	
	public String getMessage()
	{
		return message;
	}
	
	
	
}
