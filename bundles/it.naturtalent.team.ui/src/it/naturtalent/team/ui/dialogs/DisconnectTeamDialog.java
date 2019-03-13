package it.naturtalent.team.ui.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;

public class DisconnectTeamDialog extends TitleAreaDialog
{
	
	private Button btnCheckLocal;	
	
	private Button btnCheckRemote;
	private boolean remoteDisconnect;
	
	private Button okButton;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public DisconnectTeamDialog(Shell parentShell)
	{
		super(parentShell);
	}
	
	protected void configureShell(Shell shell)
	{
		super.configureShell(shell);
		shell.setText("Team");
	}


	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent)
	{
		setMessage("Projekt nicht mehr veröffentlichen");
		setTitle("Teamstatus zurücknehmen");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new FillLayout(SWT.VERTICAL));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		btnCheckLocal = new Button(container, SWT.CHECK);
		btnCheckLocal.setSelection(true);
		btnCheckLocal.setText("Verbindung mit dem Repository abbrechen");
		btnCheckLocal.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{				
				update();
			}
		});
		
		btnCheckRemote = new Button(container, SWT.CHECK);
		btnCheckRemote.setText("Projekt auch im öffentlichen Repository entfernen");		
		btnCheckRemote.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				update();
			}
		});

		return area;
	}
	
	private void update()
	{
		btnCheckRemote.setEnabled(btnCheckLocal.getSelection());
		okButton.setEnabled(btnCheckLocal.getSelection());	
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent)
	{
		okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize()
	{
		return new Point(450, 300);
	}

	@Override
	protected void okPressed()
	{		
		remoteDisconnect = btnCheckRemote.getSelection();
		super.okPressed();
	}

	public boolean isRemoteDisconnect()
	{
		return remoteDisconnect;
	}


	

}
