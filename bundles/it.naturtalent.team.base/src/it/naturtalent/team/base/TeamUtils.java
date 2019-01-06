package it.naturtalent.team.base;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import org.eclipse.jgit.annotations.NonNull;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class TeamUtils
{

	public static Repository createLocalRepository(File dir) throws IllegalStateException, GitAPIException, IOException
	{		
		File gitDir = Git.init()
				.setDirectory(dir)
				.setBare(false).call()
				.getRepository().getDirectory();		
		
		return FileRepositoryBuilder.create(gitDir);			
	}
	
	
}
