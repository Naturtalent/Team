package it.naturtalent.team.ui.actions;

import java.io.File;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.action.Action;

import it.naturtalent.team.ui.TeamUtils;

public class DisconnectAction extends Action
{
	private IProject iProject = null;
	
	private String message = "Keine Verbindung zum Team"; //$NON-NLS-N$;
	
	@PostConstruct
	public void postConstruction(@Optional EPartService partService)
	{
		iProject = TeamUtils.getSelectedIProject(partService);		
	}
	
	@Override
	public void run()
	{
		if(iProject != null)
		{
			try
			{
				// das gesamte Verzeichnis wird geloescht
				File reposDir = TeamUtils.getProjectReposDirectory(iProject);
				if(reposDir.exists())
					FileUtils.deleteDirectory(reposDir);
				
			} catch (Exception e)
			{				
				//e.printStackTrace();
				message = message + "\n" + e.getMessage();
			}
		}
	}
	
	
	public String getMessage()
	{
		return message;
	}
	
	
	
}
