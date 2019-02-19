 
package it.naturtalent.team.ui.handlers;

import java.util.List;

import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import it.naturtalent.team.ui.TeamUtils;
import it.naturtalent.team.ui.dialogs.MergeConflictDialog;


public class PushHandler3
{
	@Execute
	public void execute(@Optional EPartService partService, 
			@Named(IServiceConstants.ACTIVE_SHELL) @Optional Shell shell)
	{		
		// das momentan selektierte Projekt
		IProject iProject = TeamUtils.getSelectedIProject(partService);		
		if(iProject != null)
		{
			String message = "Projektdaten wurden hochgeladen "; //$NON-NLS-N$;			
			try
			{		
				
				
				try
				{					
					// Projekt auschecken - HEAD auf den Projektbranch ausrichten
					TeamUtils.checkoutProject(iProject);
					
					// auschecken hat funktionert - auf Konflikte pruefen
					List<String> conflictFiles = TeamUtils.getStatusConflictFiles();	
					if(!conflictFiles.isEmpty())
					{
						// Konfliktloesung (our-/theire Files)
						String result = TeamUtils.resolveConflicting(iProject, conflictFiles);
						if(StringUtils.isNotEmpty(result))
							return;		
					}							
					
				} catch (Exception e)
				{
					if (e instanceof CheckoutConflictException)
					{
						// Konfliktloesung
						CheckoutConflictException checkoutException = (CheckoutConflictException) e;
						List<String> conflictingPaths = checkoutException
								.getConflictingPaths();
						
						String result = TeamUtils.resolveConflicting(iProject, conflictingPaths);
						if(StringUtils.isNotEmpty(result))
							return;
					}

				}
				
				

				// die aktuellen Projektressourcen in den Workspace kopieren
				TeamUtils.copyToRepositoryWorkspace(iProject);

				// Abbruch, wenn anschliessender commit sinnlos, weil es
				// keine Veränderungen gab
				if (TeamUtils.readyForCommit())
				{
					// Committen
					TeamUtils.commitCommand(null);

					// push
					TeamUtils.pushProject(iProject);
				}

			} catch (Exception e)
			{

				// TODO Auto-generated catch block
				// e.printStackTrace();
				message = "Push Error\n" + e.getMessage();
			}
			
			

			MessageDialog.openInformation(shell, "Team upgrade", message); // $NON-NLS-N$
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
		return(TeamUtils.existLocalProjectBracnch(iProject));
	}
		
}