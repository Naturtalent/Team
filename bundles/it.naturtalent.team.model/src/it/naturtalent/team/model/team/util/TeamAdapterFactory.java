/**
 */
package it.naturtalent.team.model.team.util;

import it.naturtalent.team.model.team.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see it.naturtalent.team.model.team.TeamPackage
 * @generated
 */
public class TeamAdapterFactory extends AdapterFactoryImpl
{
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static TeamPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TeamAdapterFactory()
	{
		if (modelPackage == null)
		{
			modelPackage = TeamPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object)
	{
		if (object == modelPackage)
		{
			return true;
		}
		if (object instanceof EObject)
		{
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TeamSwitch<Adapter> modelSwitch =
		new TeamSwitch<Adapter>()
		{
			@Override
			public Adapter caseReposData(ReposData object)
			{
				return createReposDataAdapter();
			}
			@Override
			public Adapter caseBranch(Branch object)
			{
				return createBranchAdapter();
			}
			@Override
			public Adapter caseLogin(Login object)
			{
				return createLoginAdapter();
			}
			@Override
			public Adapter caseOneDrive(OneDrive object)
			{
				return createOneDriveAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object)
			{
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target)
	{
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link it.naturtalent.team.model.team.ReposData <em>Repos Data</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see it.naturtalent.team.model.team.ReposData
	 * @generated
	 */
	public Adapter createReposDataAdapter()
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link it.naturtalent.team.model.team.Branch <em>Branch</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see it.naturtalent.team.model.team.Branch
	 * @generated
	 */
	public Adapter createBranchAdapter()
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link it.naturtalent.team.model.team.Login <em>Login</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see it.naturtalent.team.model.team.Login
	 * @generated
	 */
	public Adapter createLoginAdapter()
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link it.naturtalent.team.model.team.OneDrive <em>One Drive</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see it.naturtalent.team.model.team.OneDrive
	 * @generated
	 */
	public Adapter createOneDriveAdapter()
	{
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter()
	{
		return null;
	}

} //TeamAdapterFactory
