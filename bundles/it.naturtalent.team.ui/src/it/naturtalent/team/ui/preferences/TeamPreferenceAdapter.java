package it.naturtalent.team.ui.preferences;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
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
import it.naturtalent.team.ui.TeamUtils;

public class TeamPreferenceAdapter extends AbstractPreferenceAdapter
{

	public static final String ROOT_TEAM_PREFERENCES_NODE = "it.naturtalent.team"; //$NON-NLS-1$
	public static final String PREFERENCE_TEAM_REPOSDIR = "prefteamreposdir"; //$NON-NLS-1$
	
	// Name des Verzeichnisses mit den Repositories
	public static final String REPOSITORIES_DIRECRORYNAME = "repositories"; //$NON-NLS-1$

	// DefaultName des Repository
	public static final String DEFAULT_REPOSITORYNAME = "NtRepository"; //$NON-NLS-1$
	private DirectoryEditorComposite directoryEditorComposite;
	
	public static final String PREFERENCE_TEAM_REMOTEREPOS_URI = "prefremoteteamreposdir"; //$NON-NLS-1$
	public static final String DEFAULT_REMOTE_REPOSITORYNAME = DEFAULT_REPOSITORYNAME+".Git"; //$NON-NLS-1$
	private DirectoryEditorComposite remoteDirectoryEditorComposite;
			
	// Directory des Repostiores (hier befindet sich das Vertzeichnis .git)
	private String localReposDirPath;
	
	// Directory des RemoteRepostiores
	private String remoteReposDirURI;

	
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

		// praeferenziertes Repositoriesverzeichnis abfragen - Default wenn noch keins definiert
		localReposDirPath = instancePreferenceNode.get(PREFERENCE_TEAM_REPOSDIR,
				defaultPreferenceNode.get(PREFERENCE_TEAM_REPOSDIR, null));
		
		// das praeferenzierte Verzeichnis an den Editor uebergeben 
		directoryEditorComposite.setDirectory(localReposDirPath);
		
		//
		//  Remote
		//

		/*
		// Remote-Defaultverzeichnis ebenfalls im Unterverzeichnis von 'NT_PROGRAM_HOME' anlegen 
		File remoteDefaultReposDir = new File(
			System.getProperty(it.naturtalent.application.Activator.NT_PROGRAM_HOME),REPOSITORIES_DIRECRORYNAME);			
		remoteDefaultReposDir = new File(remoteDefaultReposDir, DEFAULT_REMOTE_REPOSITORYNAME); 
		
		// sicherstellen, dass das praeferenzierte Remote-Verzeichnis exisitiert			
		if(!remoteDefaultReposDir.exists())
			remoteDefaultReposDir.mkdir();		
		
		// File to URI (URI startet mit file:///) als Praeferenz speichern
		URI remoteURI = remoteDefaultReposDir.toURI();		
		defaultPreferenceNode.put(PREFERENCE_TEAM_REMOTEREPOS_URI, remoteURI.toString());	
		*/	
		
		

		// praeferenziertes Remote-Repositoriesverzeichnis in EditorComposite uebernehmen
		remoteReposDirURI = instancePreferenceNode.get(PREFERENCE_TEAM_REMOTEREPOS_URI,
				defaultPreferenceNode.get(PREFERENCE_TEAM_REMOTEREPOS_URI, null));
		remoteDirectoryEditorComposite = referenceComposite.getRemoteDirectoryEditorComposite();
		remoteDirectoryEditorComposite.setDirectory(remoteReposDirURI);
				
		return referenceComposite;
	}
	
	@Override
	public void restoreDefaultPressed()
	{
		// Temporaeres Verzeichnis (aus Applicatinpraeferenzen) wird auch RepositoryVerzeichnis	
		String value = defaultPreferenceNode.get(PREFERENCE_TEAM_REPOSDIR, null);
		directoryEditorComposite.setDirectory(value);
		
		value = defaultPreferenceNode.get(PREFERENCE_TEAM_REMOTEREPOS_URI, null);
		remoteDirectoryEditorComposite.setDirectory(value);
	}

	@Override
	public void appliedPressed()
	{
		// Verzeichnis vom Editor abfragen und als Praeferenzen festschreiben
		String value = directoryEditorComposite.getDirectory();
		if(StringUtils.isNotEmpty(value))
			instancePreferenceNode.put(PREFERENCE_TEAM_REPOSDIR, value);
		else
			instancePreferenceNode.put(PREFERENCE_TEAM_REPOSDIR,
					defaultPreferenceNode.get(PREFERENCE_TEAM_REPOSDIR, null));
		
		// Remote Repository
		value = remoteDirectoryEditorComposite.getDirectory();
				
		// gespeichert werden soll eine URI (Protokoll)
		String protocol = TeamUtils.getProtocol(value);
		if(StringUtils.equals(protocol, "file")) //$NON-NLS-N$
		{			
			try
			{
				File file = new File(value);
				URI uri = file.toURI();
				value = uri.toString();							
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		
		if(StringUtils.isNotEmpty(value))
			instancePreferenceNode.put(PREFERENCE_TEAM_REMOTEREPOS_URI, value);
		else
			instancePreferenceNode.put(PREFERENCE_TEAM_REMOTEREPOS_URI,
					defaultPreferenceNode.get(PREFERENCE_TEAM_REMOTEREPOS_URI, null));
		
		try
		{
			// RemoteURI im lokalen Repository bekanntmachen
			value = instancePreferenceNode.get(PREFERENCE_TEAM_REMOTEREPOS_URI,null);
			TeamUtils.setRemote(value);
			
			// Praeferenzen festschreiben
			instancePreferenceNode.flush();
		} catch (Exception e)
		{			
			e.printStackTrace();
		}
	}
	
}
