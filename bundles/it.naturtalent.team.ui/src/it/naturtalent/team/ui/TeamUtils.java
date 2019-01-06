package it.naturtalent.team.ui;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jgit.annotations.NonNull;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.RemoteAddCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.StatusCommand;
import org.eclipse.jgit.api.errors.CanceledException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.RefNotAdvertisedException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import it.naturtalent.e4.project.ui.navigator.ResourceNavigator;
import it.naturtalent.team.ui.preferences.TeamPreferenceAdapter;


/**
 * @author dieter
 *
 */
public class TeamUtils
{
	/** Default name for the Git repository directory */
	public static final String DOT_GIT = ".git";

	
	/**
	 * StatusCommand
	 * 
	 * Status des lokalen Respository ermitteln 
	 * @throws Exception
	 */
	public static void checkStatus() throws Exception
	{
		Repository localRepos = getRepository();
		if(localRepos != null)
		{
			Git git = new Git(localRepos); 
			
			StatusCommand command = git.status();
			Status status = command.call();
			
			Set<String> untrackedFiles = new TreeSet<>(status.getUntracked());
			Set<String> untrackedDirs = new TreeSet<>(
					status.getUntrackedFolders());
			
		}
	}
	
	public static void pullRepository() throws Exception
	{
		UsernamePasswordCredentialsProvider user = new UsernamePasswordCredentialsProvider("xxxx", "xxxx");
		
		Repository localRepos = getRepository();
		if(localRepos != null)
		{
			Git git = new Git(localRepos);
		
			PullCommand pcmd = git.pull();
			
			//pcmd.setCredentialsProvider(user);
			pcmd.call();
		}
	}
	
	/**
	 * PushCommand - Alle Aenderungen in den 'origin' Server hochladen.
	 * 
	 * Push lokales Respository in remmote
	 * @throws Exception
	 */
	public static void pushRepository() throws Exception
	{
		Repository localRepos = getRepository();
		if(localRepos != null)
		{
			Git git = new Git(localRepos); 
			
			URL url = new URL("file:////home/dieter/temp/NtRepositoryRemote");
			
			URIish u = new URIish(url);
			
			  // add remote repo:
		    RemoteAddCommand remoteAddCommand = git.remoteAdd();
		    remoteAddCommand.setName("origin");
		    remoteAddCommand.setUri(u);
		    // you can add more settings here if needed
		    remoteAddCommand.call();
		    
		    // push to remote:
		    PushCommand pushCommand = git.push();
		    //pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider("username", "password"));
		    // you can add more settings here if needed
		    pushCommand.call();

		}
	}
	
	/**
	 * CommitCommand - Commit
	 * Alle anstehenden Aenderungen werden mit einem Commit festgeschrieben.
	 * 
	 * @param iProject
	 */
	public static RevCommit commitProject(IProject iProject) throws Exception
	{		
		RevCommit commit = null;
		Repository repos = getRepository();
		if(repos != null)
		{
				Git git = new Git(repos);
				CommitCommand commitCommand = git.commit();
				commitCommand.setAmend(false).setMessage("no message")
						.setInsertChangeId(true);
				commit = commitCommand.call();
		}
		return commit;
	}

	/**
	 * ADDCommand - Staging aller Resourcen eines Projekts
	 * Die Aenderungen kommen in einen Status, die sie beim naechsten Commit festschreiben.
	 * 
	 * @param iProject
	 * @throws GitAPIException 
	 * @throws NoFilepatternException 
	 */
	public static DirCache addProject(IProject iProject) throws NoFilepatternException, GitAPIException
	{
		DirCache dirCache = null;
		Repository repos = getRepository();
		if(repos != null)
		{
			AddCommand addCommand = new AddCommand(repos);		
			
			String filePattern = iProject.getName();			
			if(StringUtils.isEmpty(filePattern))
				filePattern = ".";			
			addCommand.addFilepattern(filePattern);
			
			dirCache = addCommand.call();
		}
		return dirCache;
	}
	
	/**
	 * Das Respository zurueckgeben.
	 * 
	 * @return
	 */
	public static Repository getRepository()
	{
		File gitDir = getDefaultGitDir();		
		if(gitDir != null)
		{
			FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
			Repository repository;
			try
			{
				repository = repositoryBuilder.setGitDir(gitDir)
							.readEnvironment() // scan environment GIT_* variables
			                .findGitDir() // scan up the file system tree
			                .setMustExist(true)
			                .build();
				return repository;
			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
		return null;
	}
	
	/**
	 * Das Standardrepository erzeugen.
	 * Momentan wird nur mit einem einzigen lokalen Repository gearbeitet. Der Name ist in den Praeferenzen definiert.
	 * 
	 * !!! ein bestehendes wird geloscht 
	 *  
	 * @return
	 * @throws IOException 
	 * @throws GitAPIException 
	 * @throws IllegalStateException 
	 */
	public static Repository createDefaultRepository() throws IOException, IllegalStateException, GitAPIException
	{		
		File gitDir = getDefaultRepositoryDir();
		
		if(existDefaultGit())
			FileUtils.deleteDirectory(gitDir);
		
		gitDir.mkdirs();
		return createLocalRepository(gitDir);
	}
	
	/**
	 * Rueckgabe des Git-Directory
	 * 
	 * @return
	 */
	public static File getDefaultGitDir()
	{
		File reposDir = getDefaultRepositoryDir();
		if(reposDir.exists() && (reposDir.isDirectory()))
		{
			File gitDir = new File(reposDir, DOT_GIT);
			if(gitDir.exists() && (gitDir.isDirectory()))
				return gitDir;
		}	
		return null;
	}

	/**
	 * Prueft, ob das Default Git-Verzeichnis existiert
	 * 
	 * @return
	 */
	public static boolean existDefaultGit()
	{
		File reposDir = getDefaultRepositoryDir();
		if(reposDir.exists() && (reposDir.isDirectory()))
		{
			File gitDir = new File(reposDir, DOT_GIT);
			return(gitDir.exists() && (gitDir.isDirectory()));
		}	
		
		return false;
	}
	
	/**
	 * Prueft, ob eine Arbeitskopie des Projects im Workspace des Respositories vorhanden ist.
	 * 
	 * @param partService
	 * @return
	 */
	public static boolean existProjectInRepository(IProject iProject)
	{		
		if(iProject != null)
		{
			String projectPath = getRepositoryProjectPath(iProject);
			if(StringUtils.isNotEmpty(projectPath))
			{
				File checkFile = new File(projectPath);
				return(checkFile.exists() && checkFile.isDirectory());
			}
		}
		
		return false;
	}
	
	/**
	 * Rueckgabe des Workspaceverzeichnisses des lokalen Repository.
	 * In diesem Verzeichnis befindet sich das Verzeichnis .git (das Repository selbst)	
	 * Der Namen des Verzeichnisses 'repos' (hier befinden sich standardmaessig alle Repositories)  
	 * Die Namen der vordefinierten Repositories 'reposname' 
	 * Das hier benutzte lokale Repository hat den Namen Index[0] 
	 * 
	 * @return
	 */
	public static File getDefaultRepositoryDir()
	{
		// Key PREFERENCE_TEAM_REPOSDIR (Verzeichnis aller Repositories)
		String reposDir = InstanceScope.INSTANCE
				.getNode(TeamPreferenceAdapter.ROOT_TEAM_PREFERENCES_NODE)
				.get(TeamPreferenceAdapter.PREFERENCE_TEAM_REPOSDIR, null);

		// Key PREFERENCE_TEAM_REPOSITORIES (Name aller vordefinierten Repositories)
		if (StringUtils.isNotEmpty(reposDir))
		{
			String reposName = InstanceScope.INSTANCE
					.getNode(TeamPreferenceAdapter.ROOT_TEAM_PREFERENCES_NODE)
					.get(TeamPreferenceAdapter.PREFERENCE_TEAM_REPOSITORIES,null);
			if (StringUtils.isNotEmpty(reposName))
			{
				// im Index[0] = Name des lokalen Repositories
				reposName = StringUtils.split(reposName, ",")[0];
				return new File(reposDir,reposName);				
			}
		}
	
		return null;
	}
	
	
	/**
	 * Ein lokales Repository erzeugen.
	 * 
	 * @param dir
	 * @return
	 * @throws IllegalStateException
	 * @throws GitAPIException
	 * @throws IOException
	 */
	private static Repository createLocalRepository(File dir) throws IllegalStateException, GitAPIException, IOException
	{		
		File gitDir = Git.init()
				.setDirectory(dir)
				.setBare(false).call()
				.getRepository().getDirectory();		
		
		return FileRepositoryBuilder.create(gitDir);			
	}
	
	/**
	 * Rueckgabe des momentan im ResourceNavigator selektierten Projekts.
	 * Ist eine Datei oder Verzeichnis selektiert wird das ParentProjekt zurueckgegeben.
	 * 
	 * @param partService
	 * @return
	 */
	public static IProject getSelectedIProject(EPartService partService)
	{
		MPart part = partService.findPart(ResourceNavigator.RESOURCE_NAVIGATOR_ID);
		ESelectionService selectionService = part.getContext().get(ESelectionService.class);
		Object selObject = selectionService.getSelection();
		
		if (selObject instanceof IResource)
			return ((IResource) selObject).getProject();
		
		return null;
	}
	
	/**
	 * Rueckgabe des Projektpfades im Repository Workspace
	 * 
	 * @param iProject
	 * @return
	 */
	public static String getRepositoryProjectPath(IProject iProject)
	{			
		File reposDir = getDefaultRepositoryDir();
		return new File(reposDir, iProject.getName()).getAbsolutePath();
	}
	
	/**
	 * Alle Resourcen in den Workspace des Respositories kopieren.
	 * 
	 * @param iProject
	 * @throws Exception
	 */
	public static void copyToRepositoryWorkspace(IProject iProject) throws Exception
	{
		// Zielverzeichnis ist der Workspace des lokalen Respositories
		File destDir = new File(getRepositoryProjectPath(iProject));
		
		if(!destDir.exists())
			destDir.mkdirs();
		
		// Quellverzeichnis ist das IProject
		File srcDir = iProject.getLocation().toFile();
		
		// alle Resourcen vom IProjekt in den Repository Workspace kopieren
		FileUtils.copyDirectory(srcDir, destDir);
	}


	/**
	 * Alle Resourcen vom Workspace des Respositories zurueck in das IProjekt kopieren.
	 * 
	 * @param iProject
	 * @throws Exception
	 */
	public static void copyFromRepositoryWorkspace(IProject iProject) throws Exception
	{
		// Zielverzeichnis ist das IProject
		File destDir = iProject.getLocation().toFile();
		
		// Quellverzeichnis ist der Workspace des lokalen Respositories
		File srcDir = new File(getRepositoryProjectPath(iProject));
		
		// alle Resourcen vom IProjekt in den Repository Workspace kopieren
		FileUtils.copyDirectory(srcDir, destDir);
	}

}
