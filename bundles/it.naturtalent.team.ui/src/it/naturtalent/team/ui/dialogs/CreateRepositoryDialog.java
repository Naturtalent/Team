package it.naturtalent.team.ui.dialogs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import it.naturtalent.team.ui.Messages;
import it.naturtalent.team.ui.TeamUtils;
import it.naturtalent.team.ui.preferences.TeamPreferenceAdapter;

import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class CreateRepositoryDialog extends TitleAreaDialog
{
	private Text textDirectory;
	private Button okButton;
	
	private boolean hideBare = false;
	private Shell shell;
	
	private String remoteURI;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public CreateRepositoryDialog(Shell parentShell)
	{		
		super(parentShell);
	}
		
	/*
	 * Windowtitel modifizieren
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	@Override
	protected void configureShell(Shell newShell)
	{
		this.shell = newShell;
		newShell.setText(Messages.CreateRepositoryDialog_TeamWindowTitle);
		super.configureShell(newShell);
	}



	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent)
	{
		setMessage(Messages.CreateRepositoryDialog_this_message);
		setTitle(Messages.CreateRepositoryDialog_this_title);
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		GridData gd_container = new GridData(GridData.FILL_BOTH);
		gd_container.heightHint = 114;
		container.setLayoutData(gd_container);
		new Label(container, SWT.NONE);
		

		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		Label lblDirectory = new Label(container, SWT.NONE);
		lblDirectory.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDirectory.setText(Messages.CreateRepositoryDialog_lblDirectory_text);
		
		textDirectory = new Text(container, SWT.BORDER);
		textDirectory.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		textDirectory.addModifyListener(new ModifyListener()
		{
			@Override
			public void modifyText(ModifyEvent e)
			{
				//checkDialog();
			}
		});
		
		// 'textDirectory' mit dem momentan praeferenzierten Remoteverzeichnis initialisieren
		IEclipsePreferences instancePreferenceNode = InstanceScope.INSTANCE.getNode(TeamPreferenceAdapter.ROOT_TEAM_PREFERENCES_NODE);		
		String remoteReposDirectory = instancePreferenceNode.get(TeamPreferenceAdapter.PREFERENCE_REMOTE_REPOSDIR_KEY, null);
		if(StringUtils.isEmpty(remoteReposDirectory))
		{
			// mit Defaultverzeichnis initialisieren 
			remoteReposDirectory = instancePreferenceNode.get(TeamPreferenceAdapter.PREFERENCE_TEAM_REPOSDIR_KEY, null);
			File remoteRepos = new File(remoteReposDirectory,TeamPreferenceAdapter.REMOTE_REPOSITORIESNAME);
			remoteReposDirectory = remoteRepos.getPath(); 
		}
				
		textDirectory.setText(remoteReposDirectory);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(textDirectory);		
		textDirectory.setFocus();
	
		
		// Browse - Button
		Button btnNew = new Button(container, SWT.NONE);
		btnNew.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				// DirectoryDialog ausfuehren
				String previous = textDirectory.getText();
				File previousFile = new File(previous);
				String result;
				DirectoryDialog dialog = new DirectoryDialog(getShell());
				dialog.setMessage(Messages.CreateRepositoryDialog_BrowseDirectoryMessage);
				if (previousFile.exists() && previousFile.isDirectory())			
					dialog.setFilterPath(previousFile.getPath());
				
				result = dialog.open();
				if (result != null)
					textDirectory.setText(result);				
			}
		});
		btnNew.setText(Messages.CreateRepositoryDialog_btnNewButton_text);
		return area;
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

	private void checkDialog()
	{
		try
		{
			String dir = textDirectory.getText().trim();
			if(StringUtils.isEmpty(dir))
			{
				setErrorMessage(Messages.CreateRepositoryDialog_this_message);
				return;
			}
			
			RepositoryPathChecker checker = new RepositoryPathChecker();
			if (!checker.check(dir))
			{
				setErrorMessage(checker.getErrorMessage());
				return;
			}
			
			
			if (checker.hasContent())
			{
				if (hideBare)
				{					
					setMessage(Messages.CreateRepositoryDialog_NotEmptyMessage, IMessageProvider.ERROR);
					return;
				}
				else
				{
					setMessage(Messages.CreateRepositoryDialog_NotEmptyMessage, IMessageProvider.INFORMATION); 
				}
			}

		} finally
		{
			if(okButton != null)
				okButton.setEnabled((StringUtils.isEmpty(getErrorMessage())));
		}
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize()
	{
		return new Point(759, 300);
	}

	@Override
	protected void okPressed()
	{
		remoteURI = textDirectory.getText();
		super.okPressed();
	}

	public String getRemoteURI()
	{
		return remoteURI;
	}

	
	

}
