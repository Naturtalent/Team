package it.naturtalent.team.ui.dialogs;

import java.io.File;
import java.net.URISyntaxException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jgit.annotations.Nullable;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.osgi.util.NLS;

import it.naturtalent.team.ui.Messages;

public class RepositoryPathChecker
{
	private boolean hasContent;

	private String errorMessage;

	/**
	 * Check if the given directory path is a valid local absolute repository
	 * path
	 *
	 * @param dir
	 *            the directory path to check
	 * @return {@code true} if the repository path is valid
	 */
	public boolean check(String dir)
	{
		hasContent = false;
		errorMessage = null;

		if (dir.length() == 0)
		{
			errorMessage = Messages.CreateRepositoryDialog_this_message;
			return false;
		}

		if (isURL(dir))
		{
			errorMessage = Messages.RepositoryPathChecker_errNotURL;
			return false;
		}

		File testFile = new File(dir);
		IPath path = Path.fromOSString(dir);
		for (String segment : path.segments())
		{
			IStatus status = ResourcesPlugin.getWorkspace()
					.validateName(segment, IResource.FOLDER);
			if (!status.isOK())
			{
				errorMessage = status.getMessage();
				return false;
			}
		}

		if (!path.isAbsolute())
		{
			String em = Messages.RepositoryPathChecker_errNoAbsolutePath;
			String em2 = Messages.RepositoryPathChecker_errNoDirectory;
			
			errorMessage = NLS.bind(Messages.RepositoryPathChecker_errNoAbsolutePath, dir);
			return false;
		}
		if (testFile.exists() && !testFile.isDirectory())
		{
			errorMessage = NLS.bind(Messages.RepositoryPathChecker_errNoDirectory, dir);
			return false;
		}
		hasContent = testFile.exists() && testFile.list().length > 0;
		return true;
	}

	private boolean isURL(String dir)
	{
		try
		{
			URIish u = new URIish(dir);
			return u.isRemote() || u.getScheme() != null;
		} catch (URISyntaxException e)
		{
			// skip
		}
		return false;
	}

	/**
	 * @return {@code true} if the directory exists and already has content
	 */
	public boolean hasContent()
	{
		return hasContent;
	}

	/**
	 * @return the error message if the path is invalid
	 */
	public @Nullable String getErrorMessage()
	{
		return errorMessage;
	}

}
