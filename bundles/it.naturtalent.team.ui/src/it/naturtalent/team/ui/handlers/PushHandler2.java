 
package it.naturtalent.team.ui.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.inject.Named;

import org.eclipse.core.resources.IProject;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jgit.api.MergeResult.MergeStatus;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.eclipse.jgit.transport.TrackingRefUpdate;
import org.eclipse.swt.widgets.Shell;

import it.naturtalent.team.ui.TeamUtils;
import it.naturtalent.team.ui.dialogs.StagedFilesDialog;


public class PushHandler2
{
	@Execute
	public void execute(@Optional EPartService partService, 
			@Named(IServiceConstants.ACTIVE_SHELL) @Optional Shell shell)
	{		
		// das momentan selektierte Projekt
		IProject iProject = TeamUtils.getSelectedIProject(partService);
			
		try
		{			
			if(!TeamUtils.existLocalProjectBracnch(iProject))
			{
				// Projektbranch neu erzeugen
				TeamUtils.createProjectBranch(iProject);
				
				TeamUtils.copyToRepositoryWorkspace(iProject);
				TeamUtils.addCommand();
				TeamUtils.commitCommand("initial Project");
				TeamUtils.pushProject(iProject);
				
				MessageDialog.openInformation(shell, "Team", "Projekt wird zum Teamprojekt und hochgeladen"); //$NON-NLS-N$
				return;
			}
			
			// 'master' auschecken - 'loescht' den Workspace 
			TeamUtils.checkoutCommand(null);
			TeamUtils.resetCommand();

			
			// durch auschecken wird Projektbranch zum HEAD-Branch 
			TeamUtils.checkoutProject(iProject);
			
			// Projektdaten in den Workspace kopieren
			TeamUtils.copyToRepositoryWorkspace(iProject);
			
			// Staging
			TeamUtils.addCommand();

			// Abbruch, wenn commit sinnlos, weil es keine Veränderungen gab 
			if(!TeamUtils.readyForCommit())
			{
				MessageDialog.openInformation(shell, "Team", "Abbruch - unveränderter Bearbeitungsstand");
				return;
			}
			
			// Committen
			TeamUtils.commitCommand(null);
			
			// push
			TeamUtils.pushProject(iProject);

			// Info - Push erfolgreich
			MessageDialog.openInformation(shell, "Team", "Bearbeitungsstand wurde festgeschrieben und hochgeladen");			
			
		} catch (Exception e)
		{					
			MessageDialog.openInformation(shell,"Team","Push Error\n" + e.getMessage()); //$NON-NLS-N$
		}
	}

	@CanExecute
	public boolean canExecute(@Optional EPartService partService)
	{
		// Disable wenn kein IProject selektiert ist
		if(TeamUtils.getSelectedIProject(partService) == null)
			return false;
		
		// Enable wenn ein Repository existiert
		return(TeamUtils.existDefaultGit());
	}
		
}