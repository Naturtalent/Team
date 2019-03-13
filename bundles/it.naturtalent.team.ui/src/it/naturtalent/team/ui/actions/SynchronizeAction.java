package it.naturtalent.team.ui.actions;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.action.Action;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;

import it.naturtalent.team.ui.TeamUtils;
import it.naturtalent.team.ui.TeamUtils.State;

public class SynchronizeAction extends Action
{

	private IProject iProject = null;
	
	private String message = "Projektsychronisierung beendet"; //$NON-NLS-N$;
	
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
				// HEAD auf den Projektbranch ausrichten
				TeamUtils.checkoutProject(iProject);
				
				// die aktuellen Projektressourcen in den Workspace kopieren
				TeamUtils.cleanWorkspace();
				TeamUtils.copyToRepository(iProject);
				
				// Projektdaten pullen
				try
				{
					// Projektdaten pullen
					TeamUtils.pullCommand();
				} catch (Exception e)
				{
					if (e instanceof CheckoutConflictException)
					{	
						// Konfliktloesung 
						CheckoutConflictException checkoutException = (CheckoutConflictException) e;
						List<String> conflicting = checkoutException.getConflictingPaths();
						
						if(!conflicting.isEmpty())
							TeamUtils.resolveConflicting(iProject.getName(), conflicting);						
					}
					else 
						if (e instanceof WrongRepositoryStateException)
						{
							WrongRepositoryStateException stateExeption = (WrongRepositoryStateException) e;
							if(StringUtils.contains(stateExeption.getLocalizedMessage(), "MERGING_RESOLVED"))
							{
								// pull misslungen da Repository noch im 'merging' - Status 
								TeamUtils.commitCommand(null);
								TeamUtils.pushCommand();
							}
							
							throw(e);
						}
					
					else throw(e);				
				}
				
				// Staging
				TeamUtils.staging(State.ADDED);
				TeamUtils.staging(State.MODIFIED);
				TeamUtils.staging(State.UNTRACKED);
				TeamUtils.staging(State.MISSING);

				// Projektdaten committen und pushen
				TeamUtils.commitCommand(null);				
				TeamUtils.pushCommand();
				
				// Workspace in das Projekt kopieren
				TeamUtils.copyFromRepository(iProject);
				
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
