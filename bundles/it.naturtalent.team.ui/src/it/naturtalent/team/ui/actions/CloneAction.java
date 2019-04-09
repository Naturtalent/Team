package it.naturtalent.team.ui.actions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkingSet;

import it.naturtalent.e4.project.INtProjectPropertyFactoryRepository;
import it.naturtalent.e4.project.ui.Activator;
import it.naturtalent.e4.project.ui.emf.ImportProjectPropertiesOperation;
import it.naturtalent.e4.project.ui.navigator.ResourceNavigator;
import it.naturtalent.e4.project.ui.navigator.WorkbenchContentProvider;
import it.naturtalent.e4.project.ui.utils.CreateNewProject;
import it.naturtalent.team.model.team.Branch;
import it.naturtalent.team.ui.TeamModelUtils;
import it.naturtalent.team.ui.TeamUtils;
import it.naturtalent.team.ui.dialogs.CloneTeamDialog;

public class CloneAction extends Action
{
	//private IProject iProject = null;
	
	private Branch selectedBranch;
	
	private Shell shell;
	
	private INtProjectPropertyFactoryRepository projektDataFactoryRepository;
	
	private String message = "Projekt vom Teamrepository kopieren"; //$NON-NLS-N$;
	
	@PostConstruct
	public void postConstruction(@Named(IServiceConstants.ACTIVE_SELECTION) @Optional Branch selectedBranch,
			@Named(IServiceConstants.ACTIVE_SHELL) @Optional Shell shell,
			@Optional INtProjectPropertyFactoryRepository projektDataFactoryRepository)
	{
		this.selectedBranch = selectedBranch;
		this.shell = shell;
		this.projektDataFactoryRepository = projektDataFactoryRepository;
	}
	
	@Override
	public void run()
	{
		if(selectedBranch != null)
		{
			// geclont koennen nur Projekte die noch nicht existieren
			IProject iProject = TeamModelUtils.getProjectBranchProject(selectedBranch);
			if (iProject == null)
			{
				CloneTeamDialog cloneDialog = new CloneTeamDialog(shell);
				if (cloneDialog.open() == CloneTeamDialog.OK)
				{
					try
					{
						// Projektbranch erzeugen - HEAD auf den Projektbranch
						// ausrichten
						TeamUtils.createLocalBranch(selectedBranch.getId());

						// RemoteTrackingBranch vorhanden ?
						TeamUtils.checkRemoteTrackingBranch(
								selectedBranch.getId());

						TeamUtils.checkoutProject(selectedBranch.getId());

						// NtProjekt erzeugen
						final Map<String, String> createProjectMap = new HashMap<String, String>();
						createProjectMap.put(selectedBranch.getId(),
								selectedBranch.getName());
						List<IWorkingSet> selectedWorkingSets = cloneDialog
								.getAssignedWorkingSets();
						if ((selectedWorkingSets != null)
								&& (!selectedWorkingSets.isEmpty()))
							WorkbenchContentProvider.newAssignedWorkingSets = selectedWorkingSets
									.toArray(new IWorkingSet[selectedWorkingSets
											.size()]);
						CreateNewProject.createProject(shell, createProjectMap);
						WorkbenchContentProvider.newAssignedWorkingSets = null;
						
						// Nachbearbeitung
						iProject = ResourcesPlugin.getWorkspace().getRoot().getProject(selectedBranch.getId());

						// die ausgecheckte Resourcen in das NtProjekt kopieren
						TeamUtils.copyFromRepository(iProject);
						
						// Eigenschaften importieren
						ImportProjectPropertiesOperation importPropertiesOperation = TeamModelUtils
								.createImportPropertiesOperation(iProject,projektDataFactoryRepository);	
						new ProgressMonitorDialog(shell).run(true, false, importPropertiesOperation);
						
						iProject.refreshLocal(IResource.DEPTH_INFINITE, null);
						ResourceNavigator navigator = (ResourceNavigator) Activator.findNavigator();
						navigator.setSelection(iProject);

					} catch (Exception e)
					{
						// e.printStackTrace();
						message = message + "\n" + e.getMessage();
					}
				}
			}
		}
	}
	
	public String getMessage()
	{
		return message;
	}
	
	
	
}
