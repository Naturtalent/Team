 
package it.naturtalent.team.ui.handlers;

import java.lang.reflect.InvocationTargetException;

import javax.inject.Named;

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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import it.naturtalent.team.ui.TeamUtils;

public class ConnectHandler
{
	
	// kill Watchdog
	private boolean cancel = false;
	
	String message = "Projekt wurde mit dem Team Repository verbunden"; //$NON-NLS-N$;
		
	@Execute
	public void execute(@Optional EPartService partService, @Named(IServiceConstants.ACTIVE_SHELL) @Optional Shell shell)
	{
		IProject iProject = TeamUtils.getSelectedIProject(partService);
		if(iProject != null)
		{
			if (MessageDialog.openQuestion(shell, "Team","Projekt beim Team Repository anmelden")) //$NON-NLS-N$
			{				
				doConnect(iProject);
				runWatchdog(shell);
				
				MessageDialog.openInformation(shell,"Team",message); //$NON-NLS-N$				
			}			
		}
	}

	@CanExecute
	public boolean canExecute(@Optional EPartService partService)
	{
		/*
		// Disable, wenn kein IProject selektiert ist
		IProject iProject = TeamUtils.getSelectedIProject(partService);
		if(iProject == null)
			return false;
		
		// Disable, wenn bereits ein Projectbranch existiert
		return(!TeamUtils.existLocalProjectBracnch(iProject));
		*/
		return false;
	}
	
	/*
	 * 
	 */
	private void doConnect(IProject iProject)
	{
		final Job j = new Job("Team Job") //$NON-NLS-1$
		{
			@Override
			protected IStatus run(final IProgressMonitor monitor)
			{
				// Projekt auschecken - HEAD auf den Projektbranch ausrichten
				try
				{					
					// Projektbranch erzeugen - HEAD auf den Projektbranch ausrichten
					TeamUtils.createProjectBranch(iProject);
					
					// die aktuellen Projektressourcen in den Workspace kopieren
					TeamUtils.cleanWorkspace();
					TeamUtils.copyToRepository(iProject);

					// Staging
					TeamUtils.addCommand();
					
					// Projektdaten initial committen
					TeamUtils.commitCommand("initial");
					
					// pushen  (RemoteBranch anlegen)
					TeamUtils.pushProject(iProject);					
					
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
	
	/*
	 * 
	 */
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
					monitor.beginTask("Projekt wird verbunden",IProgressMonitor.UNKNOWN);
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