package it.naturtalent.team.ui.dialogs;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTViewRenderer;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import it.naturtalent.team.model.team.Login;
import it.naturtalent.team.model.team.TeamPackage;

public class LoginRemoteReposDialog extends TitleAreaDialog
{

	private Login loginRemoteLogin;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public LoginRemoteReposDialog(Shell parentShell)
	{
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent)
	{
		Composite area = (Composite) super.createDialogArea(parent);
		
		try
		{			
			// einen neue Adresse erzeugen
			EClass loginClass = TeamPackage.eINSTANCE.getLogin();
			loginRemoteLogin = (Login) EcoreUtil.create(loginClass);
										
			// Absender im Dialog bearbeiten
			ECPSWTViewRenderer.INSTANCE.render(area, loginRemoteLogin);			
			
		} catch (ECPRendererException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	


		return area;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent)
	{
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize()
	{
		return new Point(600, 400);
	}

	public Login getLoginRemoteLogin()
	{
		return loginRemoteLogin;
	}
	
	

}
