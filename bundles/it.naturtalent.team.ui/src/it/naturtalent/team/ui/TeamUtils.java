package it.naturtalent.team.ui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.CanWriteFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.DeleteBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.MergeCommand;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.RemoteAddCommand;
import org.eclipse.jgit.api.RmCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.dircache.DirCacheEntry;
import org.eclipse.jgit.dircache.DirCacheTree;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.swt.widgets.Display;

import it.naturtalent.e4.preferences.IPreferenceRegistry;
import it.naturtalent.e4.project.INtProject;
import it.naturtalent.e4.project.ui.navigator.ResourceNavigator;
import it.naturtalent.team.ui.dialogs.MergeConflictDialog;
import it.naturtalent.team.ui.preferences.TeamPreferenceAdapter;


/**
 * @author dieter
 *
 */
public class TeamUtils
{
	
	public static String TEST_REMOTE_URL = "file:////home/dieter/Naturtalent/programme/repositories/NtRepository.Git";
	
	//private static String remoteDirPath = "/home/dieter/temp/NtRepositoryRemote";
	
	/** Default name for the Git repository directory */
	public static final String DOT_GIT = ".git";

	// Name der Dummy Datei (wird nur fuer das Erzeugen des Master-Branch benoetigt)
	public static final String MASTER_DUMMYFILE = "master.txt";
	
	// Status Definitionen
	public static enum State 
	{
		ADDED,
		MODIFIED,
		MISSING,
		CONFLICTING,
		UNTRACKED,
	}
	
	public static DirCache addCommand(IProject iProject) throws Exception
	{		
		DirCache dirCache = null;
		Repository repos = getProjectRepos(iProject);
		if(repos != null)
		{
			AddCommand addCommand = new AddCommand(repos);		
			dirCache =  addCommand.addFilepattern(".").call();
		}
		
		return dirCache;
	}

	
	public static Repository getProjectRepos(IProject iProject)
	{
		File localRepos = getRootLocalRepos();
		return (localRepos != null) ? getProjectRepos(localRepos, iProject) : null;
	}

	/**
	 * Rueckgabbe des Projekt-Repositorys
	 * @param localReposDir
	 * @param iProject
	 * @return
	 */
	public static Repository getProjectRepos(File localReposDir, IProject iProject)
	{
		Repository repository = null;
		File gitDir = new File(getRootLocalRepos(), iProject.getName());
		gitDir = new File(gitDir, DOT_GIT);
		FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
		
		try
		{
			repository = repositoryBuilder.setGitDir(gitDir)
						.readEnvironment() // scan environment GIT_* variables
		                .findGitDir() // scan up the file system tree
		                .setMustExist(true)
		                .build();		
	} catch (IOException e)
	{
			MessageDialog.openError(Display.getCurrent().getActiveShell(),
					"Add Command Errer\n", e.getMessage()); // $NON-NLS-N$
	}
		return repository;
	}

	/**
	 * Prueft, ob fuer das Project ein Repository defoniert ist.
	 * Gecheckt wird nur ob ein entsprechendes Verzeichnis (Workspace) in Rootverzeichnis existiert.
	 * 
	 * @param iProject
	 * @return
	 */
	public static boolean existProjectRepository(IProject iProject)
	{
		File projectReposDir =  getProjectReposDirectory(iProject);
		return (projectReposDir.exists());		
	}

	/**
	 * Rueckgabe des Verzeichnisses, in dem das Project-Repository gespeichert ist,
	 * Dies ist  identisch mit dem Workspace des lokalen Repository.
	 * 
	 * @param iProject
	 * @return
	 */
	public static File getProjectReposDirectory(IProject iProject)
	{
		return new File(getRootLocalRepos(), iProject.getName());
	}

	/**
	 * Rueckgabe des Verzeichnisses, in dem alle Project-Repositories gespeichert werden.
	 * Root der lokalen Repositories.
	 * @return
	 */
	public static File getRootLocalRepos()
	{
		File localReposDir = null;
		
		IEclipsePreferences prefNode = InstanceScope.INSTANCE.getNode(TeamPreferenceAdapter.ROOT_TEAM_PREFERENCES_NODE);
		String rootReops = prefNode.get(TeamPreferenceAdapter.PREFERENCE_TEAM_REPOSDIR_KEY, null);
		if(rootReops != null)
			localReposDir = new File(rootReops, TeamPreferenceAdapter.LOCAL_REPOSITORIESNAME);
		
		return localReposDir;
	}
	
	
	/*
	 * Rueckgabe der Status eines Repositories 
	 */
	public static Status statusCommand() throws Exception
	{
		Repository repos = getLocalRepository();
		if(repos != null)
		{
			try(Git git = new Git(repos))
			{
				return git.status().call();
			}
		}
		return null;
	}

	/**
	 * Die aus einem Mergeprozess her
	 * @param branchName
	 * @param conflictingPaths
	 * @return
	 * @throws Exception
	 */
	public static String resolveConflicting(String branchName, List<String>conflictingPaths) throws Exception
	{
		Repository localRepos = getLocalRepository();
		if((localRepos != null) && (StringUtils.isNotEmpty(branchName)) && !conflictingPaths.isEmpty())
		{
			try (Git git = new Git(localRepos))
			{
				MergeConflictDialog mergeDialog = new MergeConflictDialog(
						Display.getDefault().getActiveShell(),conflictingPaths);
				if (mergeDialog.open() == MergeConflictDialog.OK)
				{
					String [] theirFiles = mergeDialog.getTheirFiles();
					if(ArrayUtils.isNotEmpty(theirFiles))
					{
						try
						{
							// auschecken der selektierten theirFiles
							CheckoutCommand checkOutCommand = git.checkout();
							
							// die selektierten Files in CheckoutCommand eintragen
							for(String conflictFile : theirFiles)								
								checkOutCommand.addPath(conflictFile);
							
							// aus dem RemoteBranch auschecken
							checkOutCommand.setUpstreamMode(SetupUpstreamMode.SET_UPSTREAM)
							.setAllPaths(false)
							.setStartPoint("origin/"+branchName)
							.call();
							
						} catch (GitAPIException e1)
						{
							return e1.getMessage();
						}
					}
					
					List<String>ourFiles = mergeDialog.getOurFiles();
					if(!ourFiles.isEmpty())
					{
						AddCommand addCommand = new AddCommand(localRepos);										
						for(String ourFile : ourFiles)
							addCommand.addFilepattern(ourFile);
						addCommand.call();
					}
				}
			}
		}
		return "";
	}

	public static DirCache addCommand() throws Exception
	{		
		Repository repos = getLocalRepository();
		if(repos != null)
		{
			AddCommand addCommand = new AddCommand(repos);		
			return addCommand.addFilepattern(".").call();
		}
		
		return null;
	}

	/**
	 * @param iProject
	 * @return
	 * @throws Exception
	 */
	public static String pullProject(IProject iProject)
	{
		PullResult pullResult = null;
		Repository localRepos = getLocalRepository();
		if((localRepos != null) && (iProject != null))
		{
			String projectName = iProject.getName();
			try (Git git = new Git(localRepos))
			{										
				try
				{
					// PullCommand	ausfuehren
					PullCommand pcmd = git.pull();
					pullResult = pcmd.setRemoteBranchName(projectName).call();
					
				} catch (GitAPIException e)
				{
					if (e instanceof CheckoutConflictException)
					{
						CheckoutConflictException checkoutException = (CheckoutConflictException) e;
						List<String> conflictingPaths = checkoutException.getConflictingPaths();
						MergeConflictDialog mergeDialog = new MergeConflictDialog(
								Display.getDefault().getActiveShell(),conflictingPaths);
						if (mergeDialog.open() == MergeConflictDialog.OK)
						{
							String [] theirFiles = mergeDialog.getTheirFiles();
							if(ArrayUtils.isNotEmpty(theirFiles))
							{
								try
								{
									// auschecken der selektierten theirFiles
									CheckoutCommand checkOutCommand = git.checkout();
									
									// die selektierten Files in CheckoutCommand eintragen
									for(String conflictFile : theirFiles)								
										checkOutCommand.addPath(conflictFile);
									
									// aus dem RemoteBranch auschecken
									checkOutCommand.setUpstreamMode(SetupUpstreamMode.SET_UPSTREAM)
									.setAllPaths(false)
									.setStartPoint("origin/"+projectName)
									.call();
									
								} catch (GitAPIException e1)
								{
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}

							}
							
							List<String>ourFiles = mergeDialog.getOurFiles();
							if(!ourFiles.isEmpty())
							{
								AddCommand addCommand = new AddCommand(localRepos);										
								for(String ourFile : ourFiles)
									addCommand.addFilepattern(ourFile);
								addCommand.call();
							}
							
							commitCommand("resolve conflicts with theirs");
							git.push().setForce(true).call();
							
							return null;
						}
						
						return null;
					}
					
					// Fehlermeldung
					return e.getMessage();
				}
			}
			catch (Exception e2)
			{
				return e2.getMessage();
			}
		}
				
		return null;
	}
	
	/**
	 * Push Projektbranch
	 * 
	 * @param iProject
	 * @throws Exception
	 */
	public static void pushProjectBranch(IProject iProject) throws Exception 
	{
		Repository localRepos = getLocalRepository();
		if((localRepos != null) && (iProject != null))
		{
			String projectName = iProject.getName();
			String branchName = localRepos.getBranch();
			if(StringUtils.equals(projectName, branchName))
			{				
				try (Git git = new Git(localRepos))
				{
					PushCommand pushCommand = git.push();
					pushCommand.setRemote("origin").setPushAll().call();
				}
			}
		}
	}

	/**
	 * 
	 * @param iProject
	 * @throws Exception
	 */
	public static void pushCommand() throws Exception 
	{
		Repository localRepos = getLocalRepository();
		if(localRepos != null)
		{
			try(Git git = new Git(localRepos))
			{
				PushCommand pushCommand = git.push();
				pushCommand.call();				
			}
		}
	}

	/**
	 * 
	 * @param iProject
	 * @throws Exception
	 */
	public static void pullCommand() throws Exception 
	{
		Repository localRepos = getLocalRepository();
		if(localRepos != null)
		{
			try(Git git = new Git(localRepos))
			{
				PullCommand pullCommand = git.pull();
				pullCommand.call();				
			}
		}
	}

	public static void pullCommandOLD(IProject iproject) throws Exception 
	{
		Repository localRepos = getLocalRepository();
		if((localRepos != null)  && (iproject != null))
		{
			try(Git git = new Git(localRepos))
			{
				PullCommand pullCommand = git.pull();
				pullCommand.setRemoteBranchName(iproject.getName()).call();				
			}
		}
	}

	/**
	 * CommitCommand - Commit
	 * Alle anstehenden (staging) Aenderungen werden mit einem Commit festgeschrieben.
	 * 
	 * @param iProject
	 */
	public static RevCommit commitCommand(String stgMessage) throws Exception
	{		
		String message = StringUtils.isEmpty(stgMessage) ? "no message" : stgMessage;
		RevCommit commit = null;
		Repository repos = getLocalRepository();
		if(repos != null)
		{
				Git git = new Git(repos);
				CommitCommand commitCommand = git.commit();
				commitCommand.setAmend(false).setMessage(message).setInsertChangeId(true);
				commit = commitCommand.call();
		}
		return commit;
	}
	
	public static void mergeCommand(IProject iProject) throws Exception
	{
		Repository localRepos = getLocalRepository();
		if((localRepos != null) && (iProject != null))
		{
			try(Git git = new Git(localRepos))
			{
				ObjectId mergeBase = localRepos.resolve("origin/"+iProject.getName());				
				MergeCommand mergeCommand = git.merge();
				  MergeResult merge = git.merge().
	                        include(mergeBase).
	                        setCommit(false).
	                        setFastForward(MergeCommand.FastForwardMode.NO_FF).
	                        //setSquash(false).
	                        //setMessage("Merged changes").
	                        call();
			}
		}					
	}
	

	/**
	 * Rueckgabe der dem Commit mitgegebenen Message.
	 * 
	 * @param iProject
	 * @return
	 * @throws Exception
	 */
	public static String getCommitMessage(IProject iProject) throws Exception
	{
		Repository repos = getLocalRepository();
		Ref head = repos.findRef(iProject.getName());
		 
		 // a RevWalk allows to walk over commits based on some filtering that is defined
        try (RevWalk walk = new RevWalk(repos)) {
            RevCommit commit = walk.parseCommit(head.getObjectId());

            System.out.println("\nCommit-Message: " + commit.getFullMessage());

            walk.dispose();
        }
		return null;
	}
	
	/**
	 * Ein Projektbranch im lokalen Repository entfernen.
	 * 
	 * @param iProject
	 * @throws Exception
	 */
	public static void deleteLocalProjectBranch(IProject iProject) throws Exception
	{		
		Repository repos = getLocalRepository();
		if(repos != null)
		{
			try(Git git = new Git(repos))
			{
				git.checkout().setName("master").call();
				DeleteBranchCommand delCommand = git.branchDelete();
				delCommand.setBranchNames(iProject.getName()).setForce(true).call();
			}
		}
	}
	
	/**
	 * Projektbezogener Tracking Branch im lokalen Repository entfernen
	 * 
	 * @param iProject
	 * @throws Exception
	 */
	public static void deleteRemoteTrackingBranch(IProject iProject) throws Exception
	{			
		Repository repos = getLocalRepository();
		if(repos != null)
		{
			try(Git git = new Git(repos))
			{				
				DeleteBranchCommand delCommand = git.branchDelete();
				delCommand.setBranchNames("origin/"+iProject.getName()).setForce(true).call();
			}
		}
	}
	
	/**
	 * Einen Projektbranch im Remote-Reopository entfernen.
	 *  
	 * @param iProject
	 * @throws Exception
	 */
	public static void deleteRemoteProjectBranch(IProject iProject) throws Exception
	{
		Repository repos = getLocalRepository();
		if(repos != null)
		{
			try(Git git = new Git(repos))
			{				
				PushCommand pushCommand = git.push();
				pushCommand.setRemote("origin")
				.add(":refs/heads/"+iProject.getName())
				.call();		
			}
		}
	}


	public static void removeCommand(List<String>removeFiles) throws Exception
	{		
		Repository repos = getLocalRepository();
		if((repos != null) && (!removeFiles.isEmpty()))
		{
			try(Git git = new Git(repos))
			{
				RmCommand rmCommand = git.rm();
				for(String removeFile : removeFiles)
					rmCommand.addFilepattern(removeFile);			
				rmCommand.call();
			}	
		}
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
		Repository repos = getLocalRepository();
		if(repos != null)
		{
			AddCommand addCommand = new AddCommand(repos);		

			String pattern = iProject.getName();
			String filePattern = StringUtils.isEmpty(pattern) ? "." : pattern; 
			
			/*
			String filePattern = iProject.getName();			
			if(StringUtils.isEmpty(filePattern))
				filePattern = ".";
				*/
			
			
			addCommand.setUpdate(false).addFilepattern(filePattern);
			
			dirCache = addCommand.call();
		}
		return dirCache;
	}

	/**
	 * Abfrage und Auswertung diverser Status mit der Entscheidung ob ein Commit sinnvoll ist.
	 *  
	 * @return
	 */
	public static boolean readyForCommit()
	{
		try
		{
			Status status = statusCommand();
			
			if(!status.getAdded().isEmpty())
				return true;

			if(!status.getChanged().isEmpty())
				return true;
			
			if(!status.getRemoved().isEmpty())
				return true;
			
			if(!status.getMissing().isEmpty())
				return true;
			
			if(!status.getUntracked().isEmpty())
				return true;
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static void statusCommandTEST() throws Exception
	{
		Repository repos = getLocalRepository();
		if(repos != null)
		{
			try(Git git = new Git(repos))
			{
			
			Status status = git.status().call();
            System.out.println("Added: " + status.getAdded());
            System.out.println("Changed: " + status.getChanged());
            System.out.println("Conflicting: " + status.getConflicting());
            System.out.println("ConflictingStageState: " + status.getConflictingStageState());
            System.out.println("IgnoredNotInIndex: " + status.getIgnoredNotInIndex());
            System.out.println("Missing: " + status.getMissing());
            System.out.println("Modified: " + status.getModified());
            System.out.println("Removed: " + status.getRemoved());
            System.out.println("Untracked: " + status.getUntracked());
            System.out.println("UntrackedFolders: " + status.getUntrackedFolders());
			}
			
		}
	}

	public static void staging(State state) throws Exception
	{
		Repository repos = getLocalRepository();
		if (repos != null)
		{
			try (Git git = new Git(repos))
			{
				List<String> statusFiles = getStatus(state);
				if ((statusFiles != null) && (!statusFiles.isEmpty()))
				{
					switch (state)
						{
							case ADDED:
							case UNTRACKED:
							case MODIFIED:
							case CONFLICTING:

								AddCommand addCommand = git.add();
								for (String statusFile : statusFiles)
									addCommand.addFilepattern(statusFile);
								addCommand.call();
								break;

							case MISSING:

								RmCommand rmCommand = git.rm();
								for (String removeFile : statusFiles)
									rmCommand.addFilepattern(removeFile);
								rmCommand.call();
								break;

							default:
								break;
						}

					// return addCommand.addFilepattern(".").call();

				}
			}
		}
	}
		
	public static List<String> getStatus(State state)
	{
		Iterator <String> it = null;
		List<String>contents = new ArrayList<String>();
		try
		{
			Status status = statusCommand();			
			switch (state)
				{
					case ADDED:
						it = status.getAdded().iterator();
						break;
						
					case MODIFIED:
						it = status.getModified().iterator();
						break;

					case CONFLICTING:
						it = status.getConflicting().iterator();
						break;

					case MISSING:
						it = status.getMissing().iterator();
						break;

					case UNTRACKED:
						it = status.getUntracked().iterator();
						break;


					default:
						break;
				}
			
			if(it != null)
			{
				while(it.hasNext())
					contents.add(it.next());
			}
			
		} catch (Exception e)
		{
		}
		
		return contents;
	}
	
	
	public static List<String> getStatusConflictFiles()
	{
		List<String> conflictFiles = new ArrayList<String>();
		try
		{
			Status status = statusCommand();
			Iterator <String> it = status.getConflicting().iterator();
			while(it.hasNext())
				conflictFiles.add(it.next());
			
		} catch (Exception e)
		{
		}
		
		return conflictFiles;
	}
	
	public static List<String> getStatusMissingFiles()
	{
		List<String> conflictFiles = new ArrayList<String>();
		try
		{
			Status status = statusCommand();
			Iterator <String> it = status.getMissing().iterator();
			while(it.hasNext())
				conflictFiles.add(it.next());
			
		} catch (Exception e)
		{
		}
		
		return conflictFiles;
	}

	
	
	public static List<String> getStagedFiles(DirCache index)
	{
		List<String>listModFiles = new ArrayList<String>();
		
		DirCacheTree cacheTree = index.getCacheTree(true);
		System.out.println(cacheTree.getNameString());
		System.out.println(cacheTree.getPathString());
		System.out.println(cacheTree.getChildCount());
		cacheTree = cacheTree.getChild(0);
		
		for (int i = 0; i < index.getEntryCount(); i++) 
		{
			DirCacheEntry entry = index.getEntry(i);
			System.out.println(entry+"Stage: "+entry.getStage());
            if (entry.getStage() != DirCacheEntry.STAGE_0) 
            	listModFiles.add(entry.getPathString());
        }
				
		return listModFiles;
	}

	
	/**
	 * Projectbranch im lokalen Repository erzeugen
	 * 
	 * @param iProject
	 * @throws Exception
	 */
	/*
	public static void creatLocalProjectBranch(IProject iProject) throws Exception
	{
		Repository localRepos = getLocalRepository();
		if ((localRepos != null) && (iProject != null))
		{
			Git git = new Git(localRepos);
			git.branchCreate().setName(iProject.getName()).call();
		}
	}
	*/
	
	/**
	 * Prueft ob IProject als Branch im lokalen Repository vorhanden ist.
	 *  
	 * @param iProject
	 * @return
	 */
	public static boolean existLocalProjectBracnch(IProject iProject)
	{
		List<String> branchNames;
		try
		{
			branchNames = getLocalBranchNames();
			return branchNames.contains(iProject.getName());
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Listet alle Branchnamen des lokalen Repositories auf.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public static List<String> getLocalBranchNames () throws Exception 
	{
		List<String>refNames = new ArrayList<>();
		
		Repository localRepos = getLocalRepository();	
		List<Ref> call;
		try(Git git = new Git(localRepos))
		{				
			call = git.branchList().call();
			for (Ref ref : call) 
			{
				String name = ref.getName();
				name = StringUtils.remove(name, "refs/heads/");
				refNames.add(name);	            
			}
		}
		
        return refNames;
	}
	
	/**
	 * Listet alle Branchnamen des lokalen Repositories auf.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public static List<String> getRemoteTrackingBranches () throws Exception 
	{
		List<String>refNames = new ArrayList<>();
		
		Repository localRepos = getLocalRepository();	
		List<Ref> call;
		try(Git git = new Git(localRepos))
		{				
			call = git.branchList().setListMode(ListBranchCommand.ListMode.REMOTE).call();
			for (Ref ref : call) 
			{
				String name = ref.getName();
				name = StringUtils.remove(name, "refs/remotes/origin/");
				refNames.add(name);	            
			}
		}
		
        return refNames;
	}

	
	/**
	 * Prueft ob IProject als Branch im lokalen Repository vorhanden ist.
	 *  
	 * @param iProject
	 * @return
	 */
	public static boolean existRemoteProjectBracnch(IProject iProject)
	{
		List<String> branchNames;
		try
		{
			branchNames = getRemoteBranchNames();
			return branchNames.contains(iProject.getName());
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Listet alle Branchnamen des lokalen Repositories auf.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public static List<String> getRemoteBranchNames () throws Exception 
	{
		List<String>refNames = new ArrayList<>();
		
		Repository localRepos = getLocalRepository();	
		List<Ref> call;
		try(Git git = new Git(localRepos))
		{				
			 Collection<Ref> refs = git.lsRemote().call();
             for (Ref ref : refs) 
             {
            	String name = ref.getName();
 				name = StringUtils.remove(name, "refs/heads/");
 				refNames.add(name);	    
             }
		}
		
        return refNames;
	}

	/**
	 * 
	 * @param iProject
	 * @throws Exception
	 */
	public static void checkoutCommand(String branchName) 
	{
		Repository localRepos = getLocalRepository();		
		if((localRepos != null))
		{
			try (Git git = new Git(localRepos))
			{
				branchName = StringUtils.isEmpty(branchName) ? "master" : branchName;
				git.checkout().setName(branchName).setForce(true).call();
				
			}catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
	}
	
	/**
	 * Der Remote Branch mit dem Namen des Projekts 'iProject' wird ausgecheckt.
	 * Header zeigt jetzt auf den ausgecheckten lokalen Branch
	 * 
	 * @param iProject
	 * @throws Exception
	 */
	public static void checkoutProject(IProject iProject) throws Exception 
	{
		Repository localRepos = getLocalRepository();		
		if((localRepos != null) && (iProject != null))
		{
			try (Git git = new Git(localRepos))
			{
				String projectName = iProject.getName();
				List<String>branchNames = getLocalBranchNames();
				boolean createFlag = !branchNames.contains(projectName);
				git.checkout()
				.setCreateBranch(createFlag)
				.setName(iProject.getName())
				.setUpstreamMode(SetupUpstreamMode.SET_UPSTREAM)
				.setStartPoint("origin/"+iProject.getName())
				.setForce(true)
				.call();
			}
		}
	}
	
	public static void checkoutProject(String projectName) throws Exception 
	{
		Repository localRepos = getLocalRepository();		
		if(localRepos != null)
		{
			try (Git git = new Git(localRepos))
			{
				List<String>branchNames = getLocalBranchNames();
				boolean createFlag = !branchNames.contains(projectName);
				git.checkout()
				.setCreateBranch(createFlag)
				.setName(projectName)
				.setUpstreamMode(SetupUpstreamMode.SET_UPSTREAM)
				.setStartPoint("origin/"+projectName)
				.setForce(true)
				.call();
			}
		}
	}


	/**
	 * Einen lokalen Branch nit dem Projektnamen erzeugen.
	 * 
	 * @param projectName
	 * @throws Exception
	 */
	public static void createProjectBranch(IProject iProject) throws Exception
	{
		if (iProject != null)
		{
			String projectName = iProject.getName();
			createLocalBranch(projectName);
		}
	}
	
	/**
	 * Einen lokalen Branch nit dem Namen 'branchNamen' erzeugen.
	 * 
	 * @param branchName
	 * @throws Exception
	 */
	public static void createLocalBranch(String branchName)  throws Exception
	{
		List<String> branchNames = getLocalBranchNames();		
		if (!branchNames.contains(branchName))
		{
			Repository localRepos = getLocalRepository();
			if (localRepos != null)
			{
				try (Git git = new Git(localRepos))
				{
					// neuen Branch erzeugen
					git.checkout().setCreateBranch(true).setName(branchName).call();
				}
			}
		}
	}
	
	/**
	 * Ueberprueft, ob im lokalen Repository ein RemoteTrackingBranch fuer den Branch 'branchName' vorhanden ist.
	 * Ist dies nicht der Fall, wird durch ein 'fetch' - origin Command TrackingBranches aller im Remoterepository 
	 * gespeicherten Branches generiert.  
	 * 
	 * @param iProject
	 * @throws Exception
	 */
	public static void checkRemoteTrackingBranch(String branchName)  throws Exception
	{	
		// existiert ein RemoteTrackingBranch
		List<String>remoteTrackingNames = getRemoteTrackingBranches();
		if(!remoteTrackingNames.contains(branchName))
		{
			// durch 'fetch' - origin alle verfuegbaren RemoteTrackingBranch 
			Repository localRepos = getLocalRepository();
			if (localRepos != null)
			{
				try (Git git = new Git(localRepos))
				{
					git.fetch().setRemote("origin").setCheckFetchedObjects(true).call();
				}
			}
		}	
	}
	
	/**
	 * Einen remote Branch nit dem Namen 'branchNamen' erzeugen.
	 * 
	 * @param iProject
	 * @throws Exception
	 */
	public static void createRemoteBranch(IProject iProject)  throws Exception
	{	
		if(iProject == null)
			return;
		
		String branchName = iProject.getName();
		
		// existiert der Projectbranch im entferten Repository
		List<String>remoteBranchNames = getRemoteBranchNames();
		if(!remoteBranchNames.contains(branchName))
		{
			TeamUtils.cleanWorkspace();
			TeamUtils.copyToRepository(iProject);
			TeamUtils.addCommand();
			TeamUtils.commitCommand("initial");
			TeamUtils.pushProjectBranch(iProject);
		}
		
		// existiert ein RemoteTrackingBranch
		List<String>remoteTrackingNames = getRemoteTrackingBranches();
		if(!remoteTrackingNames.contains(branchName))
		{
			// durch 'fetch' - origin alle verfuegbaren RemoteTrackingBranch 
			Repository localRepos = getLocalRepository();
			if (localRepos != null)
			{
				try (Git git = new Git(localRepos))
				{
					git.fetch().setRemote("origin").setCheckFetchedObjects(true).call();
				}
			}
		}
	}

	

	public static void cloneRepository() throws Exception
	{
		UsernamePasswordCredentialsProvider user = new UsernamePasswordCredentialsProvider("xxxx", "xxxx");
		
		Repository localRepos = getLocalRepository();
		if(localRepos != null)
		{
			URL url = new URL(TEST_REMOTE_URL);			
			URIish uri = new URIish(url);
	
			Git git = new Git(localRepos); 
			CloneCommand cloneCommand = git.cloneRepository();
			cloneCommand.setURI(uri.toString()).setDirectory(getDefaultGitDir());
			git = cloneCommand.call();
			
			/*
			Git git = Git.cloneRepository()
					.setURI( "ssh://git@bitbucket.example.org:7999/..." )
					.setDirectory( "/path/to/local/repo" )
					.call();
					*/
			
		}
	}

	/**
	 * Das lokale Defaultrespository zurueckgeben.
	 * 
	 * @return
	 */
	public static Repository getLocalRepository()
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
	 * Lokales Repository im Defaultverzeichnis erzeugen.	
	 * Das Defaultverzeichnis ist als Praeferenz definiert. 
	 * 
	 * !!! ein bestehendes wird geloscht 
	 *  
	 * @return
	 * @throws IOException 
	 * @throws GitAPIException 
	 * @throws IllegalStateException 
	 */
	public static Repository createLocalRepositoryOLD() throws IOException, IllegalStateException, GitAPIException
	{		
		Repository localRepos = null;
		
		// File auf das Workspacedirectory 
		File gitDir = getDefaultLocalRepositoryDir();
		
		// ein bestehendes Workspacedirectory loeschen
		if(existDefaultGit())
			FileUtils.deleteDirectory(gitDir);
		
		// Workspacedirectory erstellen
		gitDir.mkdirs();
		
		// Workspace loeschen
		FileUtils.cleanDirectory(gitDir);
		
		// das Reoository erzeugen
		localRepos = createLocalRepository(gitDir,false);
		
		// Configuration fuer das Pushen 
		StoredConfig config = localRepos.getConfig();			
		Set<String>localConfigs = config.getNames("branch","master");
		if((localConfigs != null) && (localConfigs.isEmpty()))
		{
			// 'branch/master Sektion konfigurieren
			config.setString("branch", "master", "merge", "refs/heads/master");
			config.setString("branch", "master", "remote", "origin");
			config.setString("remote", "origin", "fetch", "+refs/heads/*:refs/remotes/origin/*");
			config.setString("fsck", "", "missingEmail", "ignore");
			config.save();
		}

		return localRepos;
	}
	
	/**
	 * Lokales Repository im Defaultverzeichnis erzeugen.	
	 * Das Defaultverzeichnis ist als Praeferenz definiert. 
	 * 
	 * !!! ein bestehendes wird geloscht 
	 *  
	 * @return
	 * @throws IOException 
	 * @throws GitAPIException 
	 * @throws IllegalStateException 
	 */
	public static Repository createLocalRepository() throws IOException, IllegalStateException, GitAPIException
	{		
		Repository localRepos = null;
		
		// File auf das Workspacedirectory 
		File gitDir = getDefaultLocalRepositoryDir();
		
		// ein bestehendes Workspacedirectory loeschen
		if(existDefaultGit())
			FileUtils.deleteDirectory(gitDir);
		
		// Workspacedirectory erstellen
		gitDir.mkdirs();
		
		// Workspace loeschen
		FileUtils.cleanDirectory(gitDir);
		
		// das Reoository erzeugen
		localRepos = createLocalRepository(gitDir, false);
		
		return localRepos;
	}

	
	/**
	 * Ein lokales Repository im Verzeichnis Workspacedirectory 'dir' erzeugen.
	 * 
	 * @param dir = Workspacedirectory
	 * @return
	 * @throws IllegalStateException
	 * @throws GitAPIException
	 * @throws IOException
	 */
	public static Repository createLocalRepository(File dir, boolean bare) throws IllegalStateException, GitAPIException, IOException
	{		
		File gitDir = Git.init()
				.setDirectory(dir)
				.setBare(bare).call()
				.getRepository().getDirectory();		
		
		return FileRepositoryBuilder.create(gitDir);			
	}

	/**
	 * Das Defautl-Remote Repository erstellen.
	 * 
	 * @throws Exception
	 */
	public static Repository createDefaultRemoteRepository() throws Exception
	{
		// Remoteverzeichnis aus den Praeferenzen
		String remoteDirURI = InstanceScope.INSTANCE
				.getNode(TeamPreferenceAdapter.ROOT_TEAM_PREFERENCES_NODE)
				.get(TeamPreferenceAdapter.PREFERENCE_REMOTE_REPOSDIR_KEY, null);

		return createRemoteRepository(remoteDirURI);		
	}

	/**
	 * Ein projectspezifisches-RemoteRepository im RemotePrepository-Verzeichnis erzeugen.
	 * 
	 * 
	 * @param remoteDirURI
	 * @param iProject
	 * @return
	 * @throws Exception
	 */
	public static Repository createRemoteRepository(String remoteDirURI, IProject iProject) throws Exception
	{
		Repository remoteRepository = null;
		
		if(iProject.exists())
		{
			// projektsprezifisches Repo
			String projectDir = remoteDirURI+File.separator+iProject.getName();
			remoteRepository = createRemoteRepository(projectDir);
		}
		
		return remoteRepository;
	}

	/**
	 * Ein Remote Repository erstellen.
	 * 
	 * @throws Exception
	 */
	public static Repository createRemoteRepository(String remoteDirURI) throws Exception
	{
		Repository remoteRepository = null;
		
		String protocol = getProtocol(remoteDirURI);
		if(StringUtils.equals(protocol, "file"))
		{
			URI uri = new URI(remoteDirURI);	
			File remoteDir = new File(uri.getPath());
			if(!remoteDir.exists())
				remoteDir.mkdirs();
			
			// bare Repository
			File gitDir = Git.init().setDirectory(remoteDir).setBare(true).call().getRepository().getDirectory();
			remoteRepository = FileRepositoryBuilder.create(gitDir);			
		}
		
		/*
		if(remoteRepository != null)
		{
			Repository localRepos = getLocalRepository();
			if(localRepos != null)
			{
				StoredConfig config = localRepos.getConfig();		
				Set<String>names = config.getNames("remote","origin");
				if((names != null) && (!names.isEmpty()))
				{
					// 'remote/origin Sektion konfigurieren
					
					//URL url = new URL(remoteDirURI);	
					URL url = new URL(TEST_REMOTE_URL);
					URIish u = new URIish(url);
					
					  // add remote repo:
					Git git = new Git(localRepos); 
				    RemoteAddCommand remoteAddCommand = git.remoteAdd();
				    remoteAddCommand.setName("origin");
				    remoteAddCommand.setUri(u);
				    
				    // you can add more settings here if needed
				    remoteAddCommand.call();  				
				}
			}
		}
		*/
				
		return remoteRepository;
	}
	
	public static void setBranchConfig(String branchName) throws Exception
	{
		Repository localRepos = getLocalRepository();
		
		StoredConfig config = localRepos.getConfig();			
		Set<String>localConfigs = config.getNames("branch", branchName); //$NON-NLS-N$
		if((localConfigs != null) && (localConfigs.isEmpty()))
		{
			// 'branch/branchnmae Sektion konfigurieren
			config.setString("branch", branchName, "merge", "refs/heads/"+branchName); //$NON-NLS-N$
			
			if(!StringUtils.equals(branchName, "master"))
			{
				config.setString("branch", branchName, "rebase", "false"); //$NON-NLS-N$
				config.setString("fsck", "", "missingEmail", "ignore");	//$NON-NLS-N$
			}
			
			config.setString("branch", branchName, "remote", "origin"); //$NON-NLS-N$
			config.save();
		}		
	}
	
	/**
	 * Das lokale Repository wird fuer den Zugriff auf das RemoteRepository konfiguriert.
	 * (@see TeamAddon)
	 * 
	 * @param remoteURI
	 * @throws IOException 
	 * ,
	 */
	public static void setRemoteConfig(String remoteURI) throws Exception
	{
		Repository localRepos = getLocalRepository();
				
		StoredConfig config = localRepos.getConfig();		
		
		/*
		Set<String>localConfigs = config.getNames("branch","master"); //$NON-NLS-N$
		if((localConfigs != null) && (localConfigs.isEmpty()))
		{
			// 'branch/master Sektion konfigurieren
			config.setString("branch", "master", "merge", "refs/heads/master"); //$NON-NLS-N$
			config.setString("branch", "master", "remote", "origin"); //$NON-NLS-N$
			config.setString("fsck", "", "missingEmail", "ignore");	//$NON-NLS-N$
			config.save();
		}
		*/
					
		Set<String> names = config.getNames("remote", "origin"); //$NON-NLS-N$
		if ((names != null) && (names.isEmpty()))
		{
			// 'remote/origin/fetch Sektion konfigurieren
			config.setString("remote", "origin", "fetch",
					"+refs/heads/*:refs/remotes/origin/*"); //$NON-NLS-N$
		}
		
		// bestehende URL loeschen
		config.unset("remote", "origin", "url"); //$NON-NLS-N$

		// URL url = new URL(remoteDirURI);
		URL url = new URL(remoteURI);
		URIish u = new URIish(url);

		// add remote repo:
		Git git = new Git(localRepos);
		RemoteAddCommand remoteAddCommand = git.remoteAdd();
		remoteAddCommand.setName("origin"); //$NON-NLS-N$
		remoteAddCommand.setUri(u);

		// you can add more settings here if needed
		remoteAddCommand.call();		
	}

	/**
	 * Rueckgabe des Git-Directory
	 * 
	 * @return
	 */
	public static File getDefaultGitDir()
	{
		File reposDir = getDefaultLocalRepositoryDir();
		if(reposDir.exists() && (reposDir.isDirectory()))
		{
			File gitDir = new File(reposDir, DOT_GIT);
			if(gitDir.exists() && (gitDir.isDirectory()))
				return gitDir;
		}	
		return null;
	}

	/**
	 * prueft, ob das Default Git-Verzeichnis existiert
	 * (wird vom Handler fuer 'canExecute()' benutzt)
	 * 
	 * @return
	 */
	public static boolean existDefaultGit()
	{
		File reposDir = getDefaultLocalRepositoryDir();
		if(reposDir.exists() && (reposDir.isDirectory()))
		{
			File gitDir = new File(reposDir, DOT_GIT);
			return(gitDir.exists() && (gitDir.isDirectory()));
		}	
		
		return false;
	}
	
	/**
	 * Existiert ein lokales Repository unter dem angegebenen Path.
	 * 'reposPath' ist Workspace-Directory indem das '.git'-Verzeichnis erwartet wird.
	 * 
	 * @param reposPath
	 * @return
	 */
	public static boolean isExisitingRepository(String reposPath)
	{
		File repoDir = new File(reposPath);
		
		if(repoDir.exists() && repoDir.isDirectory())
		{
			FileRepositoryBuilder builder = new FileRepositoryBuilder();
			try (Repository repository = builder.setGitDir(repoDir)
					.readEnvironment() // scan environment GIT_* variables
					.findGitDir() // scan up the file system tree
					//.setMustExist(true)
					.build())
			{				
				// empirisch, wenn ein funktionsfaehiges Repos existiert gibt's auch einen branch
				return (StringUtils.isNotEmpty(repository.getBranch()));
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	/**
	 * Checkt die Existenz des Remote-Repositorys.
	 * 
	 * @param reposURI
	 * @return
	 * @throws Exception
	 */
	public static boolean isExisitingRemoteRepository(String repositoryURL) throws Exception
	{		
		boolean result = false;
		
		URL url = new URL(repositoryURL);
		InputStream input = url.openStream();
		result = true;
		
		return result;		
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
	 * 	
	 * @return
	 */
	public static File getDefaultLocalRepositoryDir()
	{
		String defaultReposDir = DefaultScope.INSTANCE
				.getNode(TeamPreferenceAdapter.ROOT_TEAM_PREFERENCES_NODE)
				.get(TeamPreferenceAdapter.PREFERENCE_TEAM_REPOSDIR_KEY, null);
		
		// Verzeichnis des lokalen Repositories
		String reposDir = InstanceScope.INSTANCE
				.getNode(TeamPreferenceAdapter.ROOT_TEAM_PREFERENCES_NODE)
				.get(TeamPreferenceAdapter.PREFERENCE_TEAM_REPOSDIR_KEY, defaultReposDir);
		return new File(reposDir);	
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
		File reposDir = getDefaultLocalRepositoryDir();
		return new File(reposDir, iProject.getName()).getAbsolutePath();
	}
	
	/**
	 * Alle Projekt-Resourcen in das Arbeitsverzeichnis des lokalen Respositories kopieren.
	 * 
	 * @param iProject
	 * @throws Exception
	 */
	public static void copyToRepository(IProject iProject) throws Exception
	{
		// Zielverzeichnis ist das Arbeitsverzeichnis des lokalen Repositories
		File destDir = getProjectReposDirectory(iProject);
		
		// Projekt in das Arbeitsverzeichnis kopieren
		File srcDir = iProject.getLocation().toFile();
		FileUtils.copyDirectory(srcDir, destDir);
	}
	
	// Filter zum Ausblenden des git-Verzeichnisses beim Kopieren des Workspaces
	private static IOFileFilter gitDirFilter = FileFilterUtils
			.notFileFilter(FileFilterUtils.nameFileFilter(DOT_GIT));
	
	public static List<String>getInUseFiles(IProject iProject)
	{
		List<String>inUseFiles = new ArrayList<String>();
		
		// Zielverzeichnis ist das Arbeitsverzeichnis des lokalen Repositories
		File destDir = iProject.getLocation().toFile();
				
		IOFileFilter trueFilter = FileFilterUtils.trueFileFilter();
		Iterator<File> it = FileUtils.iterateFiles(destDir, trueFilter, gitDirFilter);
		while(it.hasNext())
		{
			File file = it.next();
			System.out.println(file.getName());
			
			

		}
		
		return inUseFiles;
	}
	
	public static void cleanWorkspace()
	{
		// Zielverzeichnis ist das Arbeitsverzeichnis des lokalen Repositories
		File workspaceDir = getDefaultLocalRepositoryDir();
		
		IOFileFilter trueFilter = FileFilterUtils.trueFileFilter();
		Iterator<File> it = FileUtils.iterateFilesAndDirs(workspaceDir, trueFilter, gitDirFilter);
		
		String parentDir = workspaceDir.getName(); 
		while(it.hasNext())
		{
			File file = it.next();
			if(!file.equals(workspaceDir))
			{
				String parentName = file.getParentFile().getName();
				if(StringUtils.equals(parentDir, parentName))
				{
					try
					{
						FileUtils.forceDelete(file);
					} catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}		
	}

	public static void copyFromRepository(IProject iProject) throws Exception
	{
		// Zielverzeichnis ist das IProject
		File destDir = iProject.getLocation().toFile();
		
		// Zielverzeichnis loeschen
		FileUtils.cleanDirectory(destDir);
		
		// Quellverzeichnis ist der Workspace des lokalen Respositories
		File srcDir = getDefaultLocalRepositoryDir();
		
		// alle Resourcen vom IProjekt in den Repository Workspace kopieren
		FileUtils.copyDirectory(srcDir, destDir, gitDirFilter);
	}
	
	public static void deleteDiffFiles(IProject iProject)
	{
		IOFileFilter trueFilter = FileFilterUtils.trueFileFilter();
		
		// Projektdateien in einer Liste sammeln
		List<String>projectFileNames = new ArrayList<String>();		
		File projectDir = iProject.getLocation().toFile();
		Iterator<File> it = FileUtils.listFilesAndDirs(projectDir, trueFilter, trueFilter).iterator();						
		while(it.hasNext())
		{			
			File file = it.next();
			if(!file.equals(projectDir))
				projectFileNames.add(file.getName());
		}

		// die Namen der Workspacefiles in einer Liste sammeln
		List<File>toDeleteFiles = new ArrayList<File>();
		File workspaceDir = getDefaultLocalRepositoryDir();
		Collection<File>workspaceFiles = FileUtils.listFilesAndDirs(workspaceDir, trueFilter, gitDirFilter);
		Iterator<File> itWorkspaceFiles = workspaceFiles.iterator();
		while(itWorkspaceFiles.hasNext())
		{
			File file = itWorkspaceFiles.next();
			if((!projectFileNames.contains(file.getName())) && (!file.equals(workspaceDir)))
				toDeleteFiles.add(file);					
		}

		
		for(File defFile : toDeleteFiles)
			FileUtils.deleteQuietly(defFile);
		
	}
	
	/**
	 * Das Arbeitsverzeichnis wird geloescht mit Ausnahme von '.git'.
	 * 
	 * @param gitWorkspace
	 */
	public static void cleanGitWorkspace(File gitWorkspace)
	{
		File gitDir = new File(gitWorkspace,DOT_GIT);
		try
		{
			gitDir.setWritable(false);	
			FileUtils.cleanDirectory(gitWorkspace);
			
		} catch (IOException e)
		{			
		}finally 
		{
			gitDir.setWritable(true);				
	    }
	}
	

	/**
	 * Eine Dummy-Datei im Workspace erzeugen
	 * 
	 * @throws Exception
	 */
	public static void createMasterDummyFile() throws Exception
	{
		File destDir = getDefaultLocalRepositoryDir();
		File masterDmy = new File(destDir, MASTER_DUMMYFILE);
		FileUtils.writeStringToFile(masterDmy,"master root",(String)null);		
	}
	
	/**
	 * Die DummyDatei im Workspace loeschen.
	 *  
	 * @throws Exception
	 */
	public static void deleteMasterDummyFile() throws Exception
	{
		File destDir = getDefaultLocalRepositoryDir();
		File masterDmy = new File(destDir, MASTER_DUMMYFILE);
		if((masterDmy.exists() && (!masterDmy.isDirectory())))
			FileUtils.forceDelete(masterDmy);
	}

	/**
	 * Alle Resourcen vom Workspace des Respositories zurueck in das IProjekt kopieren.
	 * 
	 * @param iProject
	 * @throws Exception
	 */
	
	public static String [] getNotWritableFiles(IProject iProject)
	{
		File srcDir = iProject.getLocation().toFile();
		
		boolean w = srcDir.getParentFile().canWrite();
		
		String[] files = srcDir.list( CanWriteFileFilter.CANNOT_WRITE );
		return files;
	}
	
	
	private static ObjectId walkRepository(Repository repos, String resolveConstant, String filePath)
	{
		try
		{
			ObjectId lastCommitId = repos.resolve(resolveConstant);
			RevWalk revWalk = new RevWalk(repos);

			// and using commit's tree find the path
			RevCommit commit = revWalk.parseCommit(lastCommitId);
			RevTree tree = commit.getTree();			
			TreeWalk treeWalk = new TreeWalk(repos);			
			treeWalk.addTree(tree);
			treeWalk.setRecursive(true);
			treeWalk.setFilter(PathFilter.create(filePath));
			return (treeWalk.next()) ? treeWalk.getObjectId(0) : null;
			
		} catch (Exception e)
		{			
			e.printStackTrace();
		}
		
		return null;
	}

	  /**
	   * Returns the protocol for a given URI or filename.
	   *
	   * @param source Determine the protocol for this URI or filename.
	   *
	   * @return The protocol for the given source.
	   */
	public static String getProtocol(final String source)
	{
		assert source != null;

		String protocol = null;

		try
		{
			final URI uri = new URI(source);

			if (uri.isAbsolute())
			{
				protocol = uri.getScheme();
			}
			else
			{
				final URL url = new URL(source);
				protocol = url.getProtocol();
			}
		} catch (final Exception e)
		{
			// Could be HTTP, HTTPS?
			if (source.startsWith("//"))
			{
				throw new IllegalArgumentException(
						"Relative context: " + source);
			}
			else
			{
				final File file = new File(source);
				protocol = getProtocol(file);
			}
		}

		return protocol;
	}

	/**
	 * Returns the protocol for a given file.
	 *
	 * @param file
	 *            Determine the protocol for this file.
	 *
	 * @return The protocol for the given file.
	 */
	private static String getProtocol(final File file)
	{
		String result;

		try
		{
			result = file.toURI().toURL().getProtocol();
		} catch (Exception e)
		{
			result = "unknown";
		}

		return result;
	}

}
