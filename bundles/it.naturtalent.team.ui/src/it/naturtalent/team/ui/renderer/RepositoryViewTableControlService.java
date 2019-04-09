package it.naturtalent.team.ui.renderer;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.view.spi.table.model.VTableControl;
import org.eclipse.emf.ecp.view.spi.table.swt.TableControlService;
import org.eclipse.emfforms.common.Optional;

public class RepositoryViewTableControlService implements TableControlService
{

	@Override
	public Optional<EObject> createNewElement(EClass clazz, EObject eObject,
			EStructuralFeature structuralFeature)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void doubleClick(VTableControl table, EObject eObject)
	{
		System.out.println("DoppelClick");

	}

}
