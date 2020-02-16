package it.naturtalent.team.ui.wizards;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.spi.ui.ECPReferenceServiceImpl;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.ui.view.swt.DefaultReferenceService;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTViewRenderer;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContextFactory;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emf.ecp.view.spi.model.VViewModelProperties;
import org.eclipse.emf.ecp.view.spi.provider.ViewProviderHelper;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import it.naturtalent.team.ui.actions.OneDriveAuthRequestAction;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


/**
 * Wizardseite, mit der mit dem die Autorisierungscodeanforderung gestartet werden kann.
 * 
 * @author dieter
 *
 */
public class OneDriveWizardPageCodeRequest extends WizardPage
{

	public OneDriveWizardPageCodeRequest()
	{
		super("Authorization wizardPage");
		setTitle("Autorisierung");
		setDescription("Autorisierungs-Code über die Auth-URL anfordern");
	}

	@Override
	public void createControl(Composite parent)
	{
		Composite container = new Composite(parent, SWT.NONE);
		setControl(container);
		container.setLayout(new GridLayout(2, false));
		
		try
		{					
			VViewModelProperties properties = VViewFactory.eINSTANCE.createViewModelLoadingProperties();
			properties.addNonInheritableProperty("requestAuthKey", "requestAuthFilter");
			EObject eObject = ((OneDriveWizard)getWizard()).getOneDrive();
			VView view = ViewProviderHelper.getView(eObject, properties);		
			ViewModelContext vmc = ViewModelContextFactory.INSTANCE
					.createViewModelContext(view, eObject, new ECPReferenceServiceImpl());			
			ECPSWTViewRenderer.INSTANCE.render(container, vmc);
			
			new Label(container, SWT.NONE);
			new Label(container, SWT.NONE);
			new Label(container, SWT.NONE);
			
			Button btnRequest = new Button(container, SWT.NONE);
			btnRequest.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(SelectionEvent e)
				{
					OneDriveAuthRequestAction action = new OneDriveAuthRequestAction();
					action.run();
				}
			});
			btnRequest.setToolTipText("startet die Codeanforderung");
			btnRequest.setText("anfordern");
			
		} catch (ECPRendererException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}

}
