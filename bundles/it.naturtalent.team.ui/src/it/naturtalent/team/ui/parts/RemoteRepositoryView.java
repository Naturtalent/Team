 
package it.naturtalent.team.ui.parts;

import java.io.File;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTView;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTViewRenderer;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import it.naturtalent.e4.project.model.project.NtProject;
import it.naturtalent.e4.project.ui.emf.ExpImpUtils;
import it.naturtalent.e4.project.ui.emf.NtProjectProperty;
import it.naturtalent.team.model.team.Branch;
import it.naturtalent.team.model.team.ReposData;
import it.naturtalent.team.model.team.TeamPackage;
import it.naturtalent.team.ui.TeamModelUtils;
import it.naturtalent.team.ui.TeamUtils;
import it.naturtalent.team.ui.actions.CloneAction;
import it.naturtalent.team.ui.preferences.TeamPreferenceAdapter;


public class RemoteRepositoryView
{
	private ReposData reposData;
	
	private ViewModelContext viewModelContext;
	
	private IEclipseContext context;
	
	@Inject
	public RemoteRepositoryView()
	{

	}

	@PostConstruct
	public void postConstruct(Composite parent,
			@Preference(nodePath = TeamPreferenceAdapter.ROOT_TEAM_PREFERENCES_NODE, value = TeamPreferenceAdapter.PREFERENCE_REMOTE_REPOSDIR_KEY) String reposDir,
			@Named(IServiceConstants.ACTIVE_SHELL) @Optional Shell shell,
			@Optional ESelectionService selectionService, @Optional IEclipseContext context)
	{

		this.context = context;

		EClass reposClass = TeamPackage.eINSTANCE.getReposData();
		reposData = (ReposData) EcoreUtil.create(reposClass);
		reposData.setName("Remote Repository");
	
		/*
		try
		{
			ECPSWTView view = ECPSWTViewRenderer.INSTANCE.render(parent, reposData);			
			viewModelContext = view.getViewModelContext();
		}
		
		catch (ECPRendererException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/	



	}
	
	public void setRemoteUri(String uri)
	{
		reposData.setRemoteURI(uri);
	}
	
	@Inject
	@Optional
	public void handleRefreshRequest(@UIEventTopic(TeamModelUtils.REFRESH_PROPJECTBRANCH_VIEW_EVENT) ReposData reposData)
	{		
		if((reposData != null) && (viewModelContext != null))
			viewModelContext.changeDomainModel(reposData);	
	}

	@Inject
	@Optional
	public void handleCloneRequest(@UIEventTopic(TeamModelUtils.CLONE_PROPJECTBRANCH_VIEW_EVENT) Object cloneObject)
	{		
		if (cloneObject instanceof Branch)
		{
			Branch branch = (Branch) cloneObject;			
			IProject iProject =  TeamModelUtils.getProjectBranchProject(branch);
			
			// Projektbranch wird im Arbeitsverzeichnis ausgecheckt
			CloneAction cloneAction =  ContextInjectionFactory.make(CloneAction.class, context);
			cloneAction.run();
			
			System.out.println(branch);
			
		}
	}

	/*
	 * 
	 */
	private Table seekTableCtrlRekursiv(Composite comp)
	{		
		Control[] children = comp.getChildren();
		for(Control ctrl : children)
		{
			if (ctrl instanceof Table)
				return (Table) ctrl;
			
			if (ctrl instanceof Composite)
			{
				Object obj = seekTableCtrlRekursiv((Composite) ctrl);
				if (obj instanceof Table)
					return (Table) obj;
			}
		}
		return null;
	}
	
	private ReposData getReposData() 
	{
		EClass reposClass = TeamPackage.eINSTANCE.getReposData();
		ReposData reposData = (ReposData) EcoreUtil.create(reposClass);
		reposData.setName("Remote Repository");
		
		File destDir = TeamUtils.getDefaultLocalRepositoryDir();
		File exportNtProjectFile = new File(destDir, NtProjectProperty.EXPIMP_NTPROJECTDATA_FILE);
		String ntProjectFilePath = exportNtProjectFile.getAbsolutePath(); 
		
		EList<Branch>branches = reposData.getBranches();		
		try
		{
			List<String>remoteBranchNames = TeamUtils.getRemoteBranchNames ();
			for(String name : remoteBranchNames)
			{
				if (!StringUtils.equals("HEAD", name) && !StringUtils.equals("master", name))
				{
					EClass branchClass = TeamPackage.eINSTANCE.getBranch();
					Branch branch = (Branch) EcoreUtil.create(branchClass);
					branch.setName(name);

					TeamUtils.checkoutProject(name);
					
					File projecFile = new File(ntProjectFilePath);					
					if (projecFile.exists())
					{
						EList<EObject> eObjects = ExpImpUtils.loadEObjectFromResource(ntProjectFilePath);
						NtProject ntProject = (NtProject) eObjects.get(0);
						branch.setName(ntProject.getName());
					}

					branches.add(branch);
				}
			}
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return reposData;
	}

}