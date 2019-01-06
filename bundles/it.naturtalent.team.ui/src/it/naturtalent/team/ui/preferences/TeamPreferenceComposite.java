package it.naturtalent.team.ui.preferences;


import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import it.naturtalent.e4.preferences.DirectoryEditorComposite;


public class TeamPreferenceComposite extends Composite
{
	private DirectoryEditorComposite directoryEditorComposite;
	
	private TeamRepositoriesComposite listRepositories;
	
	
	
	public TeamPreferenceComposite(Composite parent, int style)
	{
		super(parent, style);		
		setLayout(null);
			
		
		// Liste der Repositories
		Label lblReposDir = new Label(this, SWT.NONE);
		lblReposDir.setBounds(5, 15, 400, 17);
		lblReposDir.setText("Verzeichnis zur Aufnahme der Repositories definieren");
		
		// Verzeichnis indem LibreOffice 'soffice' installiert ist		
		
		directoryEditorComposite = new DirectoryEditorComposite(this, SWT.NONE);
		directoryEditorComposite.setBounds(5, 50, 500, 61);
		
				
		// Liste der Repositories
		Label lblRepositories = new Label(this, SWT.NONE);
		lblRepositories.setBounds(5, 180, 253, 17);
		lblRepositories.setText("vorhandene Repositories");
		
		listRepositories = new TeamRepositoriesComposite(this, SWT.NONE);		
		((GridData) listRepositories.getList().getLayoutData()).heightHint = 261;
		listRepositories.setBounds(5, 200, 560, 357);

	}

	public DirectoryEditorComposite getDirectoryEditorComposite()
	{
		return directoryEditorComposite;
	}

	public TeamRepositoriesComposite getListRepositories()
	{
		return listRepositories;
	}
	
	

}
