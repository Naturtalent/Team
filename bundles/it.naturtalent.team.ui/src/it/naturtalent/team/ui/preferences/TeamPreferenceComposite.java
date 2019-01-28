package it.naturtalent.team.ui.preferences;


import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import it.naturtalent.e4.preferences.DirectoryEditorComposite;
import it.naturtalent.e4.preferences.EditorDialog;


public class TeamPreferenceComposite extends Composite
{
	
	private DirectoryEditorComposite directoryEditorComposite;
	
	private DirectoryEditorComposite remoteDirectoryEditorComposite;
	
	
	
	
	
	// Validator fuer InputDialog
	protected IInputValidator validator = new IInputValidator()
	{
		public String isValid(String string)
		{
			if (StringUtils.isEmpty(string))						
				return "leeres Eingabefeld"; //$NON-NLS-N$
			
			return null;
		}
	};

	
	public TeamPreferenceComposite(Composite parent, int style)
	{
		super(parent, style);		
		setLayout(null);
					
		// Label Teamverzeichnis (lokalen Repository)
		Label lblReposDir = new Label(this, SWT.NONE);
		lblReposDir.setBounds(5, 15, 400, 17);
		lblReposDir.setText("Teamverzeichnis (lokales Git-Repository)");
		
		//  Speicherort des Arbeitsbereichs des lokalen Repositories 				
		directoryEditorComposite = new DirectoryEditorComposite(this, SWT.NONE);
		directoryEditorComposite.setBounds(5, 40, 550, 60);

		// Label (remote Repository)
		Label lblRemoteRepos = new Label(this, SWT.NONE);
		lblRemoteRepos.setBounds(5, 165, 400, 17);
		lblRemoteRepos.setText("URL des Remote Git-Repository");

		// Speicherort des remote Repository auswählen	
		remoteDirectoryEditorComposite = new SelectRemoteReposDir(this, SWT.NONE);
		remoteDirectoryEditorComposite.setBounds(5, 190, 550, 60);
		
	}

	public DirectoryEditorComposite getDirectoryEditorComposite()
	{
		return directoryEditorComposite;
	}

	public DirectoryEditorComposite getRemoteDirectoryEditorComposite()
	{
		return remoteDirectoryEditorComposite;
	}

	
	
	

}
