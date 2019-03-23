package it.naturtalent.team.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import it.naturtalent.e4.project.INtProjectProperty;
import it.naturtalent.e4.project.INtProjectPropertyFactory;
import it.naturtalent.e4.project.INtProjectPropertyFactoryRepository;
import it.naturtalent.e4.project.model.project.NtProject;
import it.naturtalent.e4.project.ui.emf.ExpImpUtils;
import it.naturtalent.e4.project.ui.emf.ExportProjectPropertiesOperation;
import it.naturtalent.e4.project.ui.emf.ImportProjectPropertiesOperation;
import it.naturtalent.e4.project.ui.emf.NtProjectProperty;
import it.naturtalent.team.model.team.Branch;
import it.naturtalent.team.model.team.ReposData;
import it.naturtalent.team.model.team.TeamPackage;

public class TeamModelUtils
{

	/**
	 * 
	 * @return
	 */
	public static ReposData getRemoteReposData() 
	{
		EClass reposClass = TeamPackage.eINSTANCE.getReposData();
		ReposData reposData = (ReposData) EcoreUtil.create(reposClass);
		reposData.setName("Remote Repository");
		
		File destDir = TeamUtils.getDefaultLocalRepositoryDir();
		File exportNtProjectFile = new File(destDir, NtProjectProperty.EXPIMP_NTPROJECTDATA_FILE);
		String ntProjectFilePath = exportNtProjectFile.getAbsolutePath(); 
		
		EList<Branch>branches = reposData.getBranches();		
		try
		{
			List<String>remoteBranchNames = TeamUtils.getRemoteBranchNames ();
			for(String name : remoteBranchNames)
			{
				if (!StringUtils.equals("HEAD", name) && !StringUtils.equals("master", name))
				{
					EClass branchClass = TeamPackage.eINSTANCE.getBranch();
					Branch branch = (Branch) EcoreUtil.create(branchClass);
					branch.setId(name);
					branch.setName(name);

					TeamUtils.checkoutProject(name);
					
					File projecFile = new File(ntProjectFilePath);					
					if (projecFile.exists())
					{
						EList<EObject> eObjects = ExpImpUtils.loadEObjectFromResource(ntProjectFilePath);
						NtProject ntProject = (NtProject) eObjects.get(0);
						branch.setName(ntProject.getName());
					}

					branches.add(branch);
				}
			}
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return reposData;
	}

	/**
	 * 
	 * @param iProject
	 * @param projektDataFactoryRepository
	 * @return
	 */
	public static ExportProjectPropertiesOperation createEexportPropertiesOperation(
			IProject iProject,
			INtProjectPropertyFactoryRepository projektDataFactoryRepository)
	{
		ExportProjectPropertiesOperation exportPropertiesOperation = null;
		
		// zuerst alle momentan definierten AdapterFactories aus dem Repository laden
		List<INtProjectPropertyFactory> projectPropertyFactories = projektDataFactoryRepository
				.getAllProjektDataFactories();

		// mit den Factories die Adapter erzeugen und auflisten
		List<INtProjectProperty> projectPropertyAdapters = new ArrayList<INtProjectProperty>();
		for (INtProjectPropertyFactory propertyFactory : projectPropertyFactories)
			projectPropertyAdapters.add(propertyFactory.createNtProjektData());
						
		List<IResource>toExportResources = new ArrayList<IResource>();
		toExportResources.add(iProject);
		exportPropertiesOperation = new ExportProjectPropertiesOperation(toExportResources, projectPropertyAdapters);
		
		return exportPropertiesOperation;
	}

	/**
	 * 
	 * @param iProject
	 * @param projektDataFactoryRepository
	 * @return
	 */
	public static ImportProjectPropertiesOperation createImportPropertiesOperation(
			IProject iProject,
			INtProjectPropertyFactoryRepository projektDataFactoryRepository)
	{
		ImportProjectPropertiesOperation importPropertiesOperation = null;
		
		// zuerst alle definierten AdapterFactories aus dem Repository laden
		List<INtProjectPropertyFactory> projectPropertyFactories = projektDataFactoryRepository
				.getAllProjektDataFactories();
				
		// dann die Adapter selbst erzeugen und auflisten
		List<INtProjectProperty> projectPropertyAdapters = new ArrayList<INtProjectProperty>();
		for (INtProjectPropertyFactory propertyFactory : projectPropertyFactories)
			projectPropertyAdapters.add(propertyFactory.createNtProjektData());
		
		// Runnable zum Importieren der Eigenschaften vorbereiten	
		HashSet idSet = new HashSet<>();
		idSet.add(iProject.getName());
		importPropertiesOperation = new ImportProjectPropertiesOperation(idSet, projectPropertyAdapters);
		
		return importPropertiesOperation;
	}

	
}
