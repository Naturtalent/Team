package it.naturtalent.team.ui.actions;

import java.io.File;

import javax.annotation.PostConstruct;

import org.eclipse.core.resources.IProject;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.action.Action;

import it.naturtalent.team.ui.TeamUtils;

public class ConnectAction extends Action
{
	private IProject iProject = null;
	
	private String message = "Verbindung zum Team herstellen"; //$NON-NLS-N$;
	
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
				// ein Verzeichnis (Workspace) des Repositorys anlegen
				File reposDir = TeamUtils.getProjectReposDirectory(iProject);
				if(!reposDir.exists())
					reposDir.mkdir();
				
				// ein Repository erzeugen
				TeamUtils.createLocalRepository(reposDir, false);
				
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
