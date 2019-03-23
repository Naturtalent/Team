/**
 */
package it.naturtalent.team.model.team;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Repos Data</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link it.naturtalent.team.model.team.ReposData#getName <em>Name</em>}</li>
 *   <li>{@link it.naturtalent.team.model.team.ReposData#getRemoteURI <em>Remote URI</em>}</li>
 *   <li>{@link it.naturtalent.team.model.team.ReposData#getBranches <em>Branches</em>}</li>
 * </ul>
 *
 * @see it.naturtalent.team.model.team.TeamPackage#getReposData()
 * @model
 * @generated
 */
public interface ReposData extends EObject
{
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see it.naturtalent.team.model.team.TeamPackage#getReposData_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link it.naturtalent.team.model.team.ReposData#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Remote URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Remote URI</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Remote URI</em>' attribute.
	 * @see #setRemoteURI(String)
	 * @see it.naturtalent.team.model.team.TeamPackage#getReposData_RemoteURI()
	 * @model
	 * @generated
	 */
	String getRemoteURI();

	/**
	 * Sets the value of the '{@link it.naturtalent.team.model.team.ReposData#getRemoteURI <em>Remote URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Remote URI</em>' attribute.
	 * @see #getRemoteURI()
	 * @generated
	 */
	void setRemoteURI(String value);

	/**
	 * Returns the value of the '<em><b>Branches</b></em>' containment reference list.
	 * The list contents are of type {@link it.naturtalent.team.model.team.Branch}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Branches</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Branches</em>' containment reference list.
	 * @see it.naturtalent.team.model.team.TeamPackage#getReposData_Branches()
	 * @model containment="true"
	 * @generated
	 */
	EList<Branch> getBranches();

} // ReposData
