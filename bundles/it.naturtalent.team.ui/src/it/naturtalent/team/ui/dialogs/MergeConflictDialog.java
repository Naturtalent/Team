package it.naturtalent.team.ui.dialogs;

import java.util.List;


import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;

public class MergeConflictDialog extends TitleAreaDialog
{
	private Table tableFailingFiles;
	
	private CheckboxTableViewer checkboxTableViewer;
	
	private List<String>failingPathList;
	
	private String [] selectedFilePath;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public MergeConflictDialog(Shell parentShell)
	{
		super(parentShell);
	}
	
	/**
	 * @wbp.parser.constructor
	 */
	public MergeConflictDialog(Shell parentShell, List<String> failingPathList)
	{
		super(parentShell);
		this.failingPathList = failingPathList;
	}



	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent)
	{
		setMessage("Beim Zusammenführen können Konflikte entstehen, die manuell gelöste werden müssen.");
		setTitle("Konfliktlösung");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		checkboxTableViewer = CheckboxTableViewer.newCheckList(container, SWT.BORDER | SWT.FULL_SELECTION);
		tableFailingFiles = checkboxTableViewer.getTable();
		tableFailingFiles.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		checkboxTableViewer.setContentProvider(new ArrayContentProvider());
		checkboxTableViewer.setInput(failingPathList);
		
		new Label(container, SWT.NONE);
		
		Composite btnComposite = new Composite(container, SWT.NONE);
		btnComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Button btnSelectAll = new Button(btnComposite, SWT.NONE);
		btnSelectAll.setText("alle auswählen");
		
		Button btnNoSelectin = new Button(btnComposite, SWT.NONE);
		btnNoSelectin.setText("keine auswählen");

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
		return new Point(450, 567);
	}

	@Override
	protected void okPressed()
	{
		Object[] result = checkboxTableViewer.getCheckedElements();
		selectedFilePath = new String[result.length];
		System.arraycopy(result, 0, selectedFilePath, 0,result.length);
		
		super.okPressed();
	}

	public String[] getSelectedFilePath()
	{
		return selectedFilePath;
	}



}
