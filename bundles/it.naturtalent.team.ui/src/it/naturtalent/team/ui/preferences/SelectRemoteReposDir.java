package it.naturtalent.team.ui.preferences;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import it.naturtalent.e4.preferences.DirectoryEditorComposite;
import it.naturtalent.team.ui.TeamUtils;

public class SelectRemoteReposDir extends DirectoryEditorComposite
{

	public SelectRemoteReposDir(Composite parent, int style)
	{
		super(parent, style);
		// TODO Auto-generated constructor stub
	}

	/* 
	 * Nach der Selektion pruefen, ob ein gueltiges Repository adressiert wurde.
	 * 
	 */
	@Override
	protected void postSelection()
	{
		BusyIndicator.showWhile(getShell().getDisplay(), () -> 
		{
			String reposDir = getDirectory();		
			if(!TeamUtils.isExisitingRepository(reposDir))
			{
				MessageDialog.openError(Display.getDefault().getActiveShell(),
						"Team", "kein Repository im ausgewählten Verzeichnis");
			}
		});		
	}
	
	

}
