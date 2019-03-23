 
package it.naturtalent.team.ui.handlers;

import java.lang.reflect.InvocationTargetException;

import javax.inject.Named;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Shell;

import it.naturtalent.e4.project.INtProjectPropertyFactoryRepository;
import it.naturtalent.e4.project.ui.emf.ExportProjectPropertiesOperation;
import it.naturtalent.e4.project.ui.emf.ImportProjectPropertiesOperation;
import it.naturtalent.team.ui.TeamModelUtils;
import it.naturtalent.team.ui.TeamUtils;
import it.naturtalent.team.ui.actions.SynchronizeAction;

public class SynchronHandler
{
	
	@Execute
	public void execute(@Optional IEclipseContext context, @Optional EPartService partService, 
			@Named(IServiceConstants.ACTIVE_SHELL) @Optional Shell shell,
			@Optional INtProjectPropertyFactoryRepository projektDataFactoryRepository)
	{
		IProject iProject = TeamUtils.getSelectedIProject(partService);
		if (iProject != null)
		{
			if (MessageDialog.openQuestion(shell, "Team","Projekt synchronisieren?"))
			{
				
				// Projekteigenschaften exportieren (in speziellen Dateien speicheern)
				try
				{					
					ExportProjectPropertiesOperation exportPropertiesOperation = TeamModelUtils
							.createEexportPropertiesOperation(iProject,projektDataFactoryRepository);								
					new ProgressMonitorDialog(shell).run(true, false,exportPropertiesOperation);
					
				} catch (InvocationTargetException e)
				{
					// Error
					Throwable realException = e.getTargetException();
					MessageDialog.openError(shell, "Team error", realException.getMessage());
				} catch (InterruptedException e)
				{
					// Abbruch
					MessageDialog.openError(shell, "Team cancel",e.getMessage());
					return;
				}

				// Synchronisation starten
				SynchronizeAction sysnchronizeAction = ContextInjectionFactory
						.make(SynchronizeAction.class, context);
				sysnchronizeAction.run();
				MessageDialog.openInformation(shell, "Team",
						sysnchronizeAction.getMessage()); // $NON-NLS-N$
				
				
				// Projekteigenschaften importieren (aus speziellen Dateien einlesen)
				try
				{
					// Eigenschaften importieren
					ImportProjectPropertiesOperation importPropertiesOperation = TeamModelUtils
							.createImportPropertiesOperation(iProject,projektDataFactoryRepository);	
					new ProgressMonitorDialog(shell).run(true, false, importPropertiesOperation);
				} catch (InvocationTargetException e)
				{
					// Error
					Throwable realException = e.getTargetException();
					MessageDialog.openError(shell, "Team error",
							realException.getMessage());
				} catch (InterruptedException e)
				{
					// Abbruch
					MessageDialog.openError(shell, "Team cancel",e.getMessage());
					return;
				}	
				
								
				// Projekt im Resourcenviewer refreshen
				try
				{					
					iProject.refreshLocal(IResource.DEPTH_INFINITE, null);
				} catch (CoreException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
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