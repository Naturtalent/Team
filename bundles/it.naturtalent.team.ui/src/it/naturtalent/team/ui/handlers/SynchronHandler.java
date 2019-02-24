 
package it.naturtalent.team.ui.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.swt.widgets.Shell;

import it.naturtalent.e4.project.ui.utils.RefreshResource;
import it.naturtalent.team.ui.TeamUtils;
import it.naturtalent.team.ui.TeamUtils.State;

import java.io.File;
import java.util.Collection;
import java.util.List;

import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.e4.core.di.annotations.CanExecute;

public class SynchronHandler
{
	@Execute
	public void execute(@Optional EPartService partService, 
			@Named(IServiceConstants.ACTIVE_SHELL) @Optional Shell shell)
	{
		String result;
		String message = "Projekt wurde synchronisiert"; //$NON-NLS-N$;
		
		IProject iProject = TeamUtils.getSelectedIProject(partService);
		if(iProject != null)
		{
			// Projekt auschecken - HEAD auf den Projektbranch ausrichten
			try
			{
				// Projekt auschecken - HEAD auf den Projektbranch ausrichten
				TeamUtils.checkoutProject(iProject);
				
				// die aktuellen Projektressourcen in den Workspace kopieren
				TeamUtils.cleanWorkspace();
				TeamUtils.copyToRepository(iProject);
				
				TeamUtils.statusCommandTEST();
				
				/*
				List<String> conflicting = TeamUtils.getStatus(State.CONFLICTING);
				if(!conflicting.isEmpty())
				{
					result = TeamUtils.resolveConflicting(iProject.getName(), conflicting);
					if(StringUtils.isNotEmpty(result))
						TeamUtils.commitCommand(null);	
				}

				TeamUtils.statusCommandTEST();
				*/

				try
				{
					TeamUtils.pullCommand();
				} catch (Exception e)
				{
					if (e instanceof CheckoutConflictException)
					{	
						CheckoutConflictException checkoutException = (CheckoutConflictException) e;
						List<String> conflicting = checkoutException.getConflictingPaths();
						
						if(!conflicting.isEmpty())
							TeamUtils.resolveConflicting(iProject.getName(), conflicting);
						
						/*
						{
							result = TeamUtils.resolveConflicting(iProject.getName(), conflicting);
							if(StringUtils.isNotEmpty(result))
								TeamUtils.commitCommand(null);	
						}
						*/
					}
					else 
						if (e instanceof WrongRepositoryStateException)
						{
							WrongRepositoryStateException stateExeption = (WrongRepositoryStateException) e;
							if(StringUtils.contains(stateExeption.getLocalizedMessage(), "MERGING_RESOLVED"))
							{
								TeamUtils.commitCommand(null);
								TeamUtils.pushCommand();
								
								// pull misslungen da Repository noch im 'merging' - Status 
								System.out.println("MERGING_RESOLVED");
								
							}
							
							throw(e);
						}
					
					else throw(e);				
				}
				
				/*
				List<String>conflicting = TeamUtils.getStatus(State.CONFLICTING); 
				if(!conflicting.isEmpty())
				{
					result = TeamUtils.resolveConflicting(iProject.getName(), conflicting);
					if(StringUtils.isNotEmpty(result))
						TeamUtils.commitCommand(null);	
				}
				*/
				
				TeamUtils.statusCommandTEST();
			
			
				TeamUtils.staging(State.ADDED);
				TeamUtils.staging(State.MODIFIED);
				TeamUtils.staging(State.UNTRACKED);
				TeamUtils.staging(State.MISSING);
				
				TeamUtils.statusCommandTEST();
				
				TeamUtils.commitCommand(null);
				
				TeamUtils.pushCommand();
				
				TeamUtils.statusCommandTEST();

				/*
				TeamUtils.pullCommand();
				
				TeamUtils.statusCommandTEST();
				
				TeamUtils.mergeCommand(iProject);
								
				TeamUtils.statusCommandTEST();
				*/
				
			
				// Remote-Projektbranch pullen (fetch und merge) und im Workspace auschecken
				//message = message + "\n" + TeamUtils.synchronizeProject(iProject);
				
				// Staging der neu im Workspace aufgenommen Resourcen			
				//TeamUtils.addCommand();

				
				
				
				TeamUtils.statusCommandTEST();

				// Workspace in das Projekt kopieren
				TeamUtils.copyFromRepository(iProject);
				
				// Projekt refreshen
				RefreshResource refreshResource = new RefreshResource();
				refreshResource.refresh(shell, iProject);
				
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				//e.printStackTrace();
				message = message + "\n" + e.getMessage();
			}
		}
		
		MessageDialog.openInformation(shell,"Team",message); //$NON-NLS-N$

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