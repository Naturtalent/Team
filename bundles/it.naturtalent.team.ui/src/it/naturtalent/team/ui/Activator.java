package it.naturtalent.team.ui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


public class Activator implements BundleActivator
{
	
	private static Log log = LogFactory.getLog(Activator.class);
	
	// Name des Office - ECP Projekt
	public final static String TEAMPROJECTNAME = "TeamECPProject";
	
	private static BundleContext context;
	
	private static String pluginId;
	
	/**
	 * @return the name of this plugin
	 */
	public static String getPluginId()
	{
		return pluginId;
	}
	
	@Override
	public void start(BundleContext context) throws Exception
	{		
		Activator.context = context;
		pluginId = context.getBundle().getSymbolicName();
	}

	@Override
	public void stop(BundleContext context) throws Exception
	{
		Activator.context = null;
	}

	public static BundleContext getContext()
	{
		return context;
	}
	
	/*
	 * Im OfficeProjekt sind alle Office-Modelle zusammengefasst
	 */
	public static ECPProject getTeamECPProject()
	{
		ECPProject teamECPProject = null;
		
		teamECPProject = ECPUtil.getECPProjectManager().getProject(TEAMPROJECTNAME);
		if(teamECPProject == null)
		{
			teamECPProject = it.naturtalent.e4.project.ui.Activator.createProject(TEAMPROJECTNAME);
			if (teamECPProject == null)
				log.info("es konnte kein Team-ECPProject erzeugt werden");
		}
		
		return teamECPProject;
	}



}
