package it.naturtalent.team.ui.wizards;

import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTViewRenderer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import it.naturtalent.team.ui.actions.OneDriveTokenRequestAction;


/**
 * Wizardseite, mit der mit dem die Autorisierungscodeanforderung gestartet werden kann.
 * 
 * @author dieter
 *
 */
public class OneDriveWizardPageTokenRequest extends WizardPage
{

	public OneDriveWizardPageTokenRequest()
	{
		super("Get token wizardPage");
		setTitle("Access Token");
		setDescription("einen Zugriffs-Token anfordern");
	}

	@Override
	public void createControl(Composite parent)
	{
		Composite container = new Composite(parent, SWT.NONE);
		setControl(container);
		container.setLayout(new GridLayout(2, false));
						
		try
		{
			
			ECPSWTViewRenderer.INSTANCE.render(container, ((OneDriveWizard)getWizard()).getOneDrive());
			
			new Label(container, SWT.NONE);
			new Label(container, SWT.NONE);
			new Label(container, SWT.NONE);
			
			Button btnRequest = new Button(container, SWT.NONE);
			btnRequest.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(SelectionEvent e)
				{
					// die eigentliche Anforderung starten 
					OneDriveTokenRequestAction action = new OneDriveTokenRequestAction();
					action.run();
				}
			});
			
			btnRequest.setToolTipText("startet die Tokenanforderung");
			btnRequest.setText("anfordern");
			
		} catch (ECPRendererException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}

}
