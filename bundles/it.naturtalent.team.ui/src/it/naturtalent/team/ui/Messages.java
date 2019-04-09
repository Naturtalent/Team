package it.naturtalent.team.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "it.naturtalent.team.ui.messages"; //$NON-NLS-1$
	public static String CreateRepositoryDialog_6;
	public static String CreateRepositoryDialog_BrowseDirectoryMessage;
	public static String CreateRepositoryDialog_lblDirectory_text;
	public static String CreateRepositoryDialog_text_text;
	public static String CreateRepositoryDialog_btnNewButton_text;
	public static String CreateRepositoryDialog_btnCheckButton_text;
	public static String CreateRepositoryDialog_CreateError;
	public static String CreateRepositoryDialog_DefaultRepositoryName;
	public static String CreateRepositoryDialog_NotEmptyMessage;
	public static String CreateRepositoryDialog_TeamWindowTitle;
	public static String CreateRepositoryDialog_this_title;
	public static String CreateRepositoryDialog_this_message;
	public static String RepositoryPathChecker_errNoAbsolutePath;
	public static String RepositoryPathChecker_errNoDirectory;
	public static String RepositoryPathChecker_errNotURL;
	////////////////////////////////////////////////////////////////////////////
	//
	// Constructor
	//
	////////////////////////////////////////////////////////////////////////////
	private Messages() {
		// do not instantiate
	}
	////////////////////////////////////////////////////////////////////////////
	//
	// Class initialization
	//
	////////////////////////////////////////////////////////////////////////////
	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}
