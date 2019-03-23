package it.naturtalent.team.ui.actions;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.eclipse.core.resources.IProject;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.action.Action;

import it.naturtalent.team.model.team.Branch;
import it.naturtalent.team.ui.TeamUtils;

public class CloneAction extends Action
{
	//private IProject iProject = null;
	
	private Branch selectedBranch;
	
	private String message = "Projekt vom Teamrepository kopieren"; //$NON-NLS-N$;
	
	@PostConstruct
	public void postConstruction(@Named(IServiceConstants.ACTIVE_SELECTION) @Optional Branch selectedBranch)
	{
		this.selectedBranch = selectedBranch;
	}
	
	@Override
	public void run()
	{
		if(selectedBranch != null)
		{
			try
			{
				// Projektbranch erzeugen - HEAD auf den Projektbranch ausrichten
				TeamUtils.createLocalBranch(selectedBranch.getId());
				
				// RemoteTrackingBranch vorhanden ?
				TeamUtils.checkRemoteTrackingBranch(selectedBranch.getId());
				
				TeamUtils.checkoutProject(selectedBranch.getId());
				
				
				
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
