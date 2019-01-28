package it.naturtalent.team.ui.dialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;


public class StagedFilesDialog extends TitleAreaDialog
{
	
	private Table table;
	
	private List<String>changedFiles;
	
	private static final Integer STATUS_CHANGED= new Integer(0);
	private static final Integer STATUS_ADD= new Integer(0);
	
	private Map<String, Integer> stagedFiles = new HashMap<String, Integer>();

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public StagedFilesDialog(Shell parentShell)
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
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		TableViewer tableViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableViewer.setContentProvider(new ArrayContentProvider());
		
		List<String> stageList = new ArrayList<String>();
		stageList.addAll(stagedFiles.keySet());
		
		tableViewer.setInput(stageList);
		
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
		return new Point(450, 564);
	}
	
	public void setChanded(Set<String>changed)
	{
		Iterator<String> it = changed.iterator();
		while(it.hasNext())
			stagedFiles.put(it.next(), STATUS_CHANGED);
	}
	
	public void setAdded(Set<String>added)
	{
		Iterator<String> it = added.iterator();
		while(it.hasNext())
			stagedFiles.put(it.next(), STATUS_ADD);
	}


}
