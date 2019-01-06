package it.naturtalent.team.ui.preferences;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import it.naturtalent.application.IPreferenceAdapter;
import it.naturtalent.application.IPreferenceNode;
import it.naturtalent.e4.preferences.AbstractPreferenceAdapter;
import it.naturtalent.e4.preferences.DirectoryEditorComposite;
import it.naturtalent.e4.preferences.ListEditorComposite;

public class TeamPreferenceAdapter extends AbstractPreferenceAdapter
{

	public static final String ROOT_TEAM_PREFERENCES_NODE = "it.naturtalent.team"; //$NON-NLS-1$
	public static final String PREFERENCE_TEAM_REPOSDIR = "prefteamreposdir"; //$NON-NLS-1$
	public static final String PREFERENCE_TEAM_REPOSITORIES = "prefteamrepositories"; //$NON-NLS-1$
	
	// Name des Verzeichnisses mit den Repositories
	private static final String REPOSITORIES_DIRECRORYNAME = "repositories"; //$NON-NLS-1$

	// DefaultName des Repository
	private static final String DEFAULT_REPOSITORYNAME = "NtRepository"; //$NON-NLS-1$

	
	private DirectoryEditorComposite directoryEditorComposite;
	
	private ListEditorComposite listRepositories;
	
	
	
	private String reposDirPath;
	
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
		listRepositories = referenceComposite.getListRepositories();
		
		// praeferenziertes Repositoriesverzeichnis abfragen
		reposDirPath = instancePreferenceNode.get(PREFERENCE_TEAM_REPOSDIR, null);
		if(StringUtils.isEmpty(reposDirPath))
		{
			// Defaultverzeichnis als Unterverzeichnis von 'NT_PROGRAM_HOME' 
			File defaultReposFile = new File(
				System.getProperty(it.naturtalent.application.Activator.NT_PROGRAM_HOME),REPOSITORIES_DIRECRORYNAME);
			defaultPreferenceNode.put(PREFERENCE_TEAM_REPOSDIR, defaultReposFile.getAbsolutePath());			
			reposDirPath = defaultReposFile.getAbsolutePath();
		}
		
		// sicherstellen, dass das praeferenzierte Verzeichnis exisitiert
		File reposDir = new File(reposDirPath);
		if(!reposDir.exists())
			reposDir.mkdir();
		
		// das praeferenzierte Verzeichnis n den Editor uebergeben 
		directoryEditorComposite.setDirectory(reposDirPath);
		
		// die praeferenzierten Repository-Namen abfragen
		String reposNames = instancePreferenceNode.get(PREFERENCE_TEAM_REPOSITORIES, null);
		
		if(StringUtils.isEmpty(reposNames))
		{
			reposNames = DEFAULT_REPOSITORYNAME;
			defaultPreferenceNode.put(PREFERENCE_TEAM_REPOSITORIES, reposNames);
		}
		
		// praeferenzierte Namen an den Editor uebergeben
		listRepositories.setValues(reposNames);
				
		return referenceComposite;
	}
	
	@Override
	public void restoreDefaultPressed()
	{
		// Temporaeres Verzeichnis (aus Applicatinpraeferenzen) wird auch RepositoryVerzeichnis	
		String value = defaultPreferenceNode.get(PREFERENCE_TEAM_REPOSDIR, null);
		directoryEditorComposite.setDirectory(value);
		
		value = defaultPreferenceNode.get(PREFERENCE_TEAM_REPOSITORIES, null);
		listRepositories.setValues(value);
	}

	@Override
	public void appliedPressed()
	{
		// Praeferenzen festschreiben
		String value = directoryEditorComposite.getDirectory();
		if(StringUtils.isNotEmpty(value))
			instancePreferenceNode.put(PREFERENCE_TEAM_REPOSDIR, value);	
		
		value = listRepositories.getValues();
		if(StringUtils.isNotEmpty(value))
			instancePreferenceNode.put(PREFERENCE_TEAM_REPOSITORIES, value);

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
