package it.naturtalent.team.ui;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


public class Activator implements BundleActivator
{
	
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
	
	



}
