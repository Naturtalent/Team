package it.naturtalent.team.ui.preferences;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import it.naturtalent.application.IPreferenceNode;
import it.naturtalent.e4.preferences.AbstractPreferenceAdapter;
import it.naturtalent.e4.preferences.DirectoryEditorComposite;

public class TeamPreferenceAdapter extends AbstractPreferenceAdapter
{
	// Preaferenzknoten und Keys
	public static final String ROOT_TEAM_PREFERENCES_NODE = "it.naturtalent.team"; //$NON-NLS-1$
	
	// Praeferenzkey fuer das Verzeichnis in dem alle Teamrepositories gespeichert werden (Rootverzeichnis)
	public static final String PREFERENCE_TEAM_REPOSDIR_KEY = "prefteamreposdir"; //$NON-NLS-1$

	// Praeferenzkey fuer das Verzeichnis in dem alle Teamrepositories gespeichert werden (Rootverzeichnis)
	public static final String PREFERENCE_REMOTE_REPOSDIR_KEY = "remoteteamreposdir"; //$NON-NLS-1$

	// Name des Verzeichnisses indem alle Repositories gespeichert werden (locale und remotes)
	public static final String REPOSITORIES_DIRECORYNAME = "Repositories"; //$NON-NLS-1$

	// Name des Verzeichnisses indem die Projekt-Repositories gespeichert werden
	public static final String LOCAL_REPOSITORIESNAME = "LocalRepositories"; //$NON-NLS-1$
	
	//  Name des Default-Verzeichnis der Remote-Repositories (Develepment Modus)
	public static final String REMOTE_REPOSITORIESNAME = "RemoteRepositories"; //$NON-NLS-1$
	
	private DirectoryEditorComposite directoryEditorComposite;
	
	//public static final String PREFERENCE_TEAM_REMOTEREPOS_URI = "prefremoteteamreposdir"; //$NON-NLS-1$
	//public static final String DEFAULT_REMOTE_REPOSITORYNAME = LOCAL_REPOSITORIESNAME+".Git"; //$NON-NLS-1$
	
	private DirectoryEditorComposite remoteDirectoryEditorComposite;
			
	// Directory des Repostiores (hier befindet sich das Vertzeichnis .git)
	//private String localReposDirPath;
	
	// Directory des RemoteRepostiores
	private String rootReposDir;

	
	public TeamPreferenceAdapter()
	{
		// Praeferenzknoten fuer diesen Adapter
		instancePreferenceNode = InstanceScope.INSTANCE.getNode(ROOT_TEAM_PREFERENCES_NODE);
		defaultPreferenceNode = DefaultScope.INSTANCE.getNode(ROOT_TEAM_PREFERENCES_NODE);		
	}

	
	@Override
	public String getLabel()
	{
		return "Repository"; //$NON-NLS-N$
	}
	
	@Override
	public String getNodePath()
	{
		return "Team"; //$NON-NLS-N$
	}


	@Override
	public Composite createNodeComposite(IPreferenceNode referenceNode)
	{
		TeamPreferenceComposite referenceComposite = new TeamPreferenceComposite(referenceNode.getParentNode(), SWT.None);
		directoryEditorComposite = referenceComposite.getDirectoryEditorComposite();		

		// praeferenziertes Repositoriesverzeichnis 	
		rootReposDir = instancePreferenceNode.get(PREFERENCE_TEAM_REPOSDIR_KEY, null);
		if(rootReposDir != null)
			directoryEditorComposite.setDirectory(rootReposDir);
		else
			restoreDefaultPressed();
		
		return referenceComposite;
	}

	/**
	 * DefaultRestore - Default Root-Repositoriesverzeichnis wiederherstellen 
	 */
	@Override
	public void restoreDefaultPressed()
	{
		// Default Root-Repository
		File defaultDirectory = new File(System.getProperty(it.naturtalent.application.Activator.NT_PROGRAM_HOME));		
		rootReposDir = new File(defaultDirectory,REPOSITORIES_DIRECORYNAME).getPath();		
		instancePreferenceNode.put(PREFERENCE_TEAM_REPOSDIR_KEY, rootReposDir);
		directoryEditorComposite.setDirectory(rootReposDir);
	}

	/**
	 * Ok - das Verzeichnis aus dem Editor wird praeferenziertes Root-Repositoriesverzeichnis 
	 */
	@Override
	public void appliedPressed()
	{		
		String value = directoryEditorComposite.getDirectory();
		
		if (StringUtils.isNotEmpty(value))
		{
			File defaultDir = new File(value);
			if(defaultDir.isDirectory())
			{				
				if (!defaultDir.exists())
					defaultDir.mkdir();
				
				instancePreferenceNode.put(PREFERENCE_TEAM_REPOSDIR_KEY,value);								
			}
		}
				
		try
		{
			// Praeferenzen festschreiben
			instancePreferenceNode.flush();
		} catch (Exception e)
		{			
			e.printStackTrace();
		}
	}
	
	public static void initRepositoriesInfrastructure(File rootReposDir)
	{
		IEclipsePreferences instancePreferenceNode = InstanceScope.INSTANCE.getNode(ROOT_TEAM_PREFERENCES_NODE);
		
		File localReposDir = new File(rootReposDir,TeamPreferenceAdapter.LOCAL_REPOSITORIESNAME);
		if(!localReposDir.exists())
			localReposDir.mkdir();	
		
		// sicherstellen dass ein Remote-Defaultverzeichnis existiert
		File remoteReposDir = new File(rootReposDir,TeamPreferenceAdapter.REMOTE_REPOSITORIESNAME);
		if(!remoteReposDir.exists())
			remoteReposDir.mkdir();
		
		// ist ein Remoteverzeichnis praeferenziert 			
		String prefValue = instancePreferenceNode.get(TeamPreferenceAdapter.PREFERENCE_REMOTE_REPOSDIR_KEY, "");
		if(StringUtils.isEmpty(prefValue))
		{
			// keine praeferenziertes Remoteverzeichnis . Rueckfall auf Default
			instancePreferenceNode.put(TeamPreferenceAdapter.PREFERENCE_REMOTE_REPOSDIR_KEY,remoteReposDir.getPath());					
		}
						
		try
		{
			// Praeferenzen festschreiben
			instancePreferenceNode.flush();
		} catch (Exception e)
		{			
			e.printStackTrace();
		}


		
	}
	
}
