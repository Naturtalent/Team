/**
 */
package it.naturtalent.team.model.team.impl;

import it.naturtalent.team.model.team.Branch;
import it.naturtalent.team.model.team.ReposData;
import it.naturtalent.team.model.team.TeamPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Repos Data</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link it.naturtalent.team.model.team.impl.ReposDataImpl#getName <em>Name</em>}</li>
 *   <li>{@link it.naturtalent.team.model.team.impl.ReposDataImpl#getRemoteURI <em>Remote URI</em>}</li>
 *   <li>{@link it.naturtalent.team.model.team.impl.ReposDataImpl#getBranches <em>Branches</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ReposDataImpl extends MinimalEObjectImpl.Container implements ReposData
{
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getRemoteURI() <em>Remote URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRemoteURI()
	 * @generated
	 * @ordered
	 */
	protected static final String REMOTE_URI_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRemoteURI() <em>Remote URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRemoteURI()
	 * @generated
	 * @ordered
	 */
	protected String remoteURI = REMOTE_URI_EDEFAULT;

	/**
	 * The cached value of the '{@link #getBranches() <em>Branches</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBranches()
	 * @generated
	 * @ordered
	 */
	protected EList<Branch> branches;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ReposDataImpl()
	{
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass()
	{
		return TeamPackage.Literals.REPOS_DATA;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setName(String newName)
	{
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TeamPackage.REPOS_DATA__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getRemoteURI()
	{
		return remoteURI;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRemoteURI(String newRemoteURI)
	{
		String oldRemoteURI = remoteURI;
		remoteURI = newRemoteURI;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TeamPackage.REPOS_DATA__REMOTE_URI, oldRemoteURI, remoteURI));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Branch> getBranches()
	{
		if (branches == null)
		{
			branches = new EObjectContainmentEList<Branch>(Branch.class, this, TeamPackage.REPOS_DATA__BRANCHES);
		}
		return branches;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
	{
		switch (featureID)
		{
			case TeamPackage.REPOS_DATA__BRANCHES:
				return ((InternalEList<?>)getBranches()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType)
	{
		switch (featureID)
		{
			case TeamPackage.REPOS_DATA__NAME:
				return getName();
			case TeamPackage.REPOS_DATA__REMOTE_URI:
				return getRemoteURI();
			case TeamPackage.REPOS_DATA__BRANCHES:
				return getBranches();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue)
	{
		switch (featureID)
		{
			case TeamPackage.REPOS_DATA__NAME:
				setName((String)newValue);
				return;
			case TeamPackage.REPOS_DATA__REMOTE_URI:
				setRemoteURI((String)newValue);
				return;
			case TeamPackage.REPOS_DATA__BRANCHES:
				getBranches().clear();
				getBranches().addAll((Collection<? extends Branch>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID)
	{
		switch (featureID)
		{
			case TeamPackage.REPOS_DATA__NAME:
				setName(NAME_EDEFAULT);
				return;
			case TeamPackage.REPOS_DATA__REMOTE_URI:
				setRemoteURI(REMOTE_URI_EDEFAULT);
				return;
			case TeamPackage.REPOS_DATA__BRANCHES:
				getBranches().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID)
	{
		switch (featureID)
		{
			case TeamPackage.REPOS_DATA__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case TeamPackage.REPOS_DATA__REMOTE_URI:
				return REMOTE_URI_EDEFAULT == null ? remoteURI != null : !REMOTE_URI_EDEFAULT.equals(remoteURI);
			case TeamPackage.REPOS_DATA__BRANCHES:
				return branches != null && !branches.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString()
	{
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(", remoteURI: ");
		result.append(remoteURI);
		result.append(')');
		return result.toString();
	}

} //ReposDataImpl
