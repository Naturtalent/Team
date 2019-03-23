 
package it.naturtalent.team.ui.handlers;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import it.naturtalent.e4.project.ui.utils.RefreshResource;
import it.naturtalent.team.ui.TeamUtils;
import it.naturtalent.team.ui.TeamUtils.State;

public class SynchronHandlerProgressMonitor
{
	// kill Watchdog
	private boolean cancel = false;
	
	String message = "Projekt wurde synchronisiert"; //$NON-NLS-N$;
		
	@Execute
	public void execute(@Optional EPartService partService, 
			@Named(IServiceConstants.ACTIVE_SHELL) @Optional Shell shell)
	{
		IProject iProject = TeamUtils.getSelectedIProject(partService);
		if(iProject != null)
		{
			doSynchronize(iProject);
			runWatchdog(shell);
		
			// Watchdog beenden
			cancel = true;
			
			MessageDialog.openInformation(shell,"Team",message); //$NON-NLS-N$
			
			// Projekt im Resourcenviewer refreshen	
			RefreshResource.refresh(shell, iProject);
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
		//return(TeamUtils.existLocalProjectBracnch(iProject));
		return true;
	}
	
	private void doSynchronize(IProject iProject)
	{
		final Job j = new Job("Team Job") //$NON-NLS-1$
		{
			@Override
			protected IStatus run(final IProgressMonitor monitor)
			{
				// Projekt auschecken - HEAD auf den Projektbranch ausrichten
				try
				{					
					//System.out.println("Start Sync");
					if(!TeamUtils.existLocalProjectBracnch(iProject))
					{
						// Projektbranch erzeugen - HEAD auf den Projektbranch ausrichten
						TeamUtils.createProjectBranch(iProject);
					}
					else
					{					
						// Projekt auschecken - HEAD auf den Projektbranch ausrichten
						TeamUtils.checkoutProject(iProject);
					}
										
					// die aktuellen Projektressourcen in den Workspace kopieren
					TeamUtils.cleanWorkspace();
					TeamUtils.copyToRepository(iProject);
					
					try
					{
						if(!TeamUtils.existRemoteProjectBracnch(iProject))
						{
							// Staging
							TeamUtils.addCommand();
							
							// Projektdaten initial committen
							TeamUtils.commitCommand("initial");
							
							// pushen  (RemoteBranch anlegen)
							TeamUtils.pushProjectBranch(iProject);					

							return Status.OK_STATUS;
						}
												
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
								String message = stateExeption.getLocalizedMessage();
								if(StringUtils.contains(message, "MERGING_RESOLVED"))
								{
									// pull misslungen  
									TeamUtils.commitCommand(null);
									TeamUtils.pushCommand();
								}
								else
									if(StringUtils.contains(message, "MERGING"))
									{
										
										TeamUtils.statusCommandTEST();
										
										// pull misslungen da Repository noch im 'merging' - Status 
										TeamUtils.staging(State.CONFLICTING);										
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
					
					//System.out.println("End Sync");
					
					
				} catch (Exception e)
				{				
					//e.printStackTrace();
					message = message + "\n" + e.getMessage();
				}
				
				return Status.OK_STATUS;
			}
		};
		
		j.schedule();
	}
	
	private void runWatchdog(Shell shell)
	{
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());
		dialog.open();
		try
		{
			dialog.run(true, true, new IRunnableWithProgress()
			{
				@Override
				public void run(IProgressMonitor monitor)
						throws InvocationTargetException,
						InterruptedException
				{
					monitor.beginTask("Projekt wird synchronisiert",IProgressMonitor.UNKNOWN);
					for (int i = 0;; ++i)
					{
						if (monitor.isCanceled())
						{
							throw new InterruptedException();
						}
						
						if (i == 50)
							break;
						if (cancel)
							break;
						try
						{
							Thread.sleep(100);
						} catch (InterruptedException e)
						{
							throw new InterruptedException();
						}
					}
					monitor.done();		
				}
			});
		} catch (Exception e)
		{
			
		}
	}

		
}