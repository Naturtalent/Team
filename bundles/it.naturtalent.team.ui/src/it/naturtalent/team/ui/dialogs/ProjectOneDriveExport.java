package it.naturtalent.team.ui.dialogs;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Shell;

import it.naturtalent.e4.project.expimp.dialogs.ExportNtProjectDialog;
import it.naturtalent.e4.project.ui.ws.WorkingSetManager;
import it.naturtalent.e4.project.ui.ws.WorkingSetRoot;
import it.naturtalent.team.ui.OneDriveUtils;

public class ProjectOneDriveExport extends ExportNtProjectDialog
{
	
	public ProjectOneDriveExport(Shell parentShell)
	{
		super(parentShell);
		// TODO Auto-generated constructor stub
	}

	/*
	 * es werden keine Settings unterstuetzt
	 * 
	 */
	@Override
	protected void init()
	{
		// Transferverzeichnis wird vorbelegt
		exportComboDir.setText(OneDriveUtils.getOneDriveExpImpDir().getPath());
		
		// WorkingSet-Sicht wird voreingestellt
		btnWorkingSet.setSelection(true);		
		WorkingSetManager wsManager = it.naturtalent.e4.project.ui.Activator.getWorkingSetManager();
		checkboxTreeViewer.setInput(new WorkingSetRoot(wsManager.getWorkingSets()));
	}

	@Override
	protected void doOkPressed()
	{
		// DialogSettings ausblenden
		// die gecheckten Projekte sichern 
		resultExportProjects = getCheckedProjects();	
		
		// das ausgewaehlte Zielverzeichnis fuer spaetere Verwendung sichern
		resultExportDir = new File(exportComboDir.getText());

	}


	
	

}
