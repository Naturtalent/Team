package it.naturtalent.team.ui.wizards;

import javax.annotation.PostConstruct;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.spi.ui.util.ECPHandlerHelper;
import org.eclipse.jface.wizard.Wizard;

import it.naturtalent.team.model.team.OneDrive;
import it.naturtalent.team.model.team.TeamPackage;
import it.naturtalent.team.ui.Activator;
import it.naturtalent.team.ui.OneDriveHelper;



public class OneDriveWizard extends Wizard
{

	protected IEclipseContext context;
	
	// der fuer den Wizard erforderliche Datensatz 
	private OneDrive oneDrive;
	
	public OneDriveWizard()
	{
		setWindowTitle("OneDrive Cloud");
		
		// OndeDrive laden oder neu erzeugen
		oneDrive = OneDriveHelper.loadOneDriveInfo();
		if (oneDrive == null)
		{
			EClass oneDriveClass = TeamPackage.eINSTANCE.getOneDrive();
			oneDrive = (OneDrive) EcoreUtil.create(oneDriveClass);
		}		
	}
	
	@PostConstruct
	private void postConstuct(IEclipseContext context, ESelectionService selectionService)
	{
		this.context = context;
	}

	@Override
	public void addPages()
	{
		OneDriveWizardPageCodeRequest codeRequestPage = ContextInjectionFactory.make(OneDriveWizardPageCodeRequest.class, context);
		OneDriveWizardPageTokenRequest tokenRequestPage = ContextInjectionFactory.make(OneDriveWizardPageTokenRequest.class, context);
		
		addPage(codeRequestPage);
		addPage(tokenRequestPage);
	}

	@Override
	public boolean performFinish()
	{
		// den Datensatz OneDrive speichern
		if(OneDriveHelper.loadOneDriveInfo() == null)
		{
			// initial speichern
			ECPProject teamProject = Activator.getTeamECPProject();
			teamProject.getContents().add(oneDrive);			
		}
		ECPHandlerHelper.saveProject(Activator.getTeamECPProject());
		
		return true;
	}

	public OneDrive getOneDrive()
	{
		return oneDrive;
	}
	
	

}
