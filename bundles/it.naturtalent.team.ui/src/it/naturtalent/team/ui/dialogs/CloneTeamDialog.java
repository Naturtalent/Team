package it.naturtalent.team.ui.dialogs;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.ui.internal.workbench.swt.WorkbenchSWTActivator;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkingSet;

import it.naturtalent.e4.project.IResourceNavigator;
import it.naturtalent.e4.project.ui.dialogs.ConfigureWorkingSetDialog;
import it.naturtalent.e4.project.ui.dialogs.SelectWorkingSetDialog;
import it.naturtalent.e4.project.ui.ws.IWorkingSetManager;
import it.naturtalent.icons.core.Icon;
import it.naturtalent.icons.core.IconSize;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class CloneTeamDialog extends TitleAreaDialog
{
	// Dialogsettings
	public static final String TEAM_WORKINGSET_SETTINGS = "teamworkingsettings"; //$NON-NLS-1$
	private IDialogSettings settings = WorkbenchSWTActivator.getDefault().getDialogSettings();
	
	// Liste der zugeordneten WorkingSets	
	private ArrayList<IWorkingSet> assignedWorkingSets = new ArrayList<IWorkingSet>();
	
	private CCombo comboWorkingSets;
	private Button btnBrowseWorkingSet;
	private Button btnWorkingSets;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public CloneTeamDialog(Shell parentShell)
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
		setTitleImage(Icon.WIZBAN_PULL_GIT.getImage(IconSize._75x66_TitleDialogIconSize));
		setMessage("Ein Projekt aus dem Teamrepository kopieren.");
		setTitle("Ein Projekt klonen");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Group grpWorkingsets = new Group(container, SWT.NONE);
		grpWorkingsets.setText("Workingsets");
		grpWorkingsets.setLayout(new GridLayout(3, false));
		GridData gd_grpWorkingsets = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_grpWorkingsets.heightHint = 90;
		grpWorkingsets.setLayoutData(gd_grpWorkingsets);
		
		btnWorkingSets = new Button(grpWorkingsets, SWT.CHECK);
		btnWorkingSets.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));	
		btnWorkingSets.setText("zu einem Workingset hinzufügen");
		btnWorkingSets.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				comboWorkingSets.setEnabled(btnWorkingSets.getSelection());
				btnBrowseWorkingSet.setEnabled(btnWorkingSets.getSelection());
			}
		});
		
		Label lblWorkingSet = new Label(grpWorkingsets, SWT.NONE);
		lblWorkingSet.setText("Workingsets");
		
		comboWorkingSets = new CCombo(grpWorkingsets, SWT.BORDER);
		comboWorkingSets.setEditable(false);
		comboWorkingSets.setEnabled(false);
		GridData gd_comboWorkingSets = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_comboWorkingSets.widthHint = 83;
		comboWorkingSets.setLayoutData(gd_comboWorkingSets);
		
		btnBrowseWorkingSet = new Button(grpWorkingsets, SWT.NONE);
		btnBrowseWorkingSet.setEnabled(false);
		btnBrowseWorkingSet.setText("browse");
		btnBrowseWorkingSet.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				SelectWorkingSetDialog dialog = new SelectWorkingSetDialog(
						getShell(), assignedWorkingSets
								.toArray(new IWorkingSet[assignedWorkingSets
										.size()]));				
				if(dialog.open() == ConfigureWorkingSetDialog.OK)
				{
					// die ausgewaehlten WorkingSets in Combo uebernehmen 
					IWorkingSet [] configResults = dialog.getConfigResult();
					assignedWorkingSets.clear();				
					StringBuilder buildName = new StringBuilder(5);
					for(IWorkingSet workingSet : configResults)
					{
						String wsName = workingSet.getName();
						if (!StringUtils.equals(wsName,
								IWorkingSetManager.OTHER_WORKINGSET_NAME))
						{
							if (assignedWorkingSets.size() > 0)
								buildName.append("," + wsName); //$NON-NLS-N$ //$NON-NLS-1$
							else
								buildName.append(wsName);								
							assignedWorkingSets.add(workingSet);
						}	
					}
					String name = buildName.toString();
					comboWorkingSets.add(name);
					comboWorkingSets.setText(buildName.toString());
					comboWorkingSets.setData(name, assignedWorkingSets.clone());					
				}
			}
		});
		
		// Dialogsettings uebenehmen
		String value = settings.get(TEAM_WORKINGSET_SETTINGS);
		if(StringUtils.isNotEmpty(value))
		{
			comboWorkingSets.setText(value);
			
			IResourceNavigator resourceNavigator = it.naturtalent.e4.project.ui.Activator.findNavigator();
			IWorkingSet[] activeWorkingSets = resourceNavigator.getWindowWorkingSets();
			String [] wsSettingNames = StringUtils.split(value);
			for(String wsName : wsSettingNames)
			{
				for(IWorkingSet ws : activeWorkingSets)
				{
					if(StringUtils.equals(ws.getName(), wsName))
						assignedWorkingSets.add(ws);
				}				
			}
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
		return new Point(571, 258);
	}

	@Override
	protected void okPressed()
	{
		// die selektierten WorkingSets (abhaengig vom Status des CheckButtons)
		assignedWorkingSets = (btnWorkingSets.getSelection() ? assignedWorkingSets : null);
		
		// WorkingSet im DialogSetting speichern
		settings.put(TEAM_WORKINGSET_SETTINGS, comboWorkingSets.getText());
		
		super.okPressed();
	}

	public ArrayList<IWorkingSet> getAssignedWorkingSets()
	{
		return assignedWorkingSets;
	}
	
	
	
}
