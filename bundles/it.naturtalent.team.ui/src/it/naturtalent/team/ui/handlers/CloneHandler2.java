 
package it.naturtalent.team.ui.handlers;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkingSet;

import it.naturtalent.e4.project.INtProjectPropertyFactoryRepository;
import it.naturtalent.e4.project.IResourceNavigator;
import it.naturtalent.e4.project.ui.Activator;
import it.naturtalent.e4.project.ui.emf.ImportProjectPropertiesOperation;
import it.naturtalent.e4.project.ui.navigator.ResourceNavigator;
import it.naturtalent.e4.project.ui.navigator.WorkbenchContentProvider;
import it.naturtalent.e4.project.ui.utils.CreateNewProject;
import it.naturtalent.team.model.team.Branch;
import it.naturtalent.team.ui.TeamModelUtils;
import it.naturtalent.team.ui.TeamUtils;
import it.naturtalent.team.ui.actions.CloneAction;
import it.naturtalent.team.ui.actions.ConnectAction;
import it.naturtalent.team.ui.dialogs.CloneTeamDialog;

public class CloneHandler2
{
	
	@Execute
	public void execute(@Optional IEclipseContext context,
			@Named(IServiceConstants.ACTIVE_SELECTION) @Optional Branch selectedBranch,
			@Named(IServiceConstants.ACTIVE_SHELL) @Optional Shell shell,
			@Optional INtProjectPropertyFactoryRepository projektDataFactoryRepository)			
	{
		if(selectedBranch != null)
		{
			// kopieren nur moeglich, wenn NtPrujekt noch nicht existiert
			IProject iProject = ResourcesPlugin.getWorkspace().getRoot().getProject(selectedBranch.getId());
			if(!iProject.exists())
			{
				CloneTeamDialog cloneDialog = new CloneTeamDialog(shell);
				if(cloneDialog.open() == CloneTeamDialog.OK)
				{
					// Projektbranch wird im Arbeitsverzeichnis ausgecheckt
					CloneAction cloneAction =  ContextInjectionFactory.make(CloneAction.class, context);
					cloneAction.run();
					
					MessageDialog.openInformation(shell,"Team", cloneAction.getMessage()); //$NON-NLS-N$	
					
					// NtProjekt erzeugen
					final Map<String,String>createProjectMap = new HashMap<String, String>();
					createProjectMap.put(selectedBranch.getId(), selectedBranch.getName());
					List<IWorkingSet>selectedWorkingSets = cloneDialog.getAssignedWorkingSets();					
					if((selectedWorkingSets != null) && (!selectedWorkingSets.isEmpty()))
						WorkbenchContentProvider.newAssignedWorkingSets = selectedWorkingSets.toArray(new IWorkingSet[selectedWorkingSets.size()]);					
					CreateNewProject.createProject(shell, createProjectMap);
					WorkbenchContentProvider.newAssignedWorkingSets = null;
					
					// Nachbearbeitung
					iProject = ResourcesPlugin.getWorkspace().getRoot().getProject(selectedBranch.getId());
					try
					{
						// die ausgecheckte Resourcen in das NtProjekt kopieren
						TeamUtils.copyFromRepository(iProject);
					} catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					// die Projekteigenschaften importieren 
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
						ResourceNavigator navigator = (ResourceNavigator) Activator.findNavigator();
						navigator.setSelection(iProject);

					} catch (CoreException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}				
			}
		}
		
	}

	@CanExecute
	public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) @Optional Branch selectedBranch)
	{
		if(selectedBranch != null)
		{
			IProject iProject = ResourcesPlugin.getWorkspace().getRoot().getProject(selectedBranch.getId());
			return(!iProject.exists());
		}

		return false;
	}
		
}