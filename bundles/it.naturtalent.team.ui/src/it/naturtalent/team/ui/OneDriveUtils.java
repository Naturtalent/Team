package it.naturtalent.team.ui;

import java.io.File;

import it.naturtalent.application.Activator;

public class OneDriveUtils
{
	
	private static final String ONEDRIVETRANSDIR = "OndeDriveTrans";
	
	private static File oneDriveExpImpDir;
	
	private static String progDir = System.getProperty(Activator.NT_PROGRAM_HOME);

	public static File getOneDriveExpImpDir()
	{
		oneDriveExpImpDir = new File(progDir);
		oneDriveExpImpDir = new File(oneDriveExpImpDir, ONEDRIVETRANSDIR);
		if(!oneDriveExpImpDir.exists())
			oneDriveExpImpDir.mkdir(); 
		
		return oneDriveExpImpDir;
	}



	
}
