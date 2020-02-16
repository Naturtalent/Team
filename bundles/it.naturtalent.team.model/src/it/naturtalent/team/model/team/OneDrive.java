/**
 */
package it.naturtalent.team.model.team;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>One Drive</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link it.naturtalent.team.model.team.OneDrive#getClientID <em>Client ID</em>}</li>
 *   <li>{@link it.naturtalent.team.model.team.OneDrive#getAuthURL <em>Auth URL</em>}</li>
 *   <li>{@link it.naturtalent.team.model.team.OneDrive#getRedirectURI <em>Redirect URI</em>}</li>
 *   <li>{@link it.naturtalent.team.model.team.OneDrive#getAuthCode <em>Auth Code</em>}</li>
 *   <li>{@link it.naturtalent.team.model.team.OneDrive#getAccessToken <em>Access Token</em>}</li>
 * </ul>
 *
 * @see it.naturtalent.team.model.team.TeamPackage#getOneDrive()
 * @model
 * @generated
 */
public interface OneDrive extends EObject
{
	/**
	 * Returns the value of the '<em><b>Client ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Client ID</em>' attribute.
	 * @see #setClientID(String)
	 * @see it.naturtalent.team.model.team.TeamPackage#getOneDrive_ClientID()
	 * @model
	 * @generated
	 */
	String getClientID();

	/**
	 * Sets the value of the '{@link it.naturtalent.team.model.team.OneDrive#getClientID <em>Client ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Client ID</em>' attribute.
	 * @see #getClientID()
	 * @generated
	 */
	void setClientID(String value);

	/**
	 * Returns the value of the '<em><b>Auth URL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Auth URL</em>' attribute.
	 * @see #setAuthURL(String)
	 * @see it.naturtalent.team.model.team.TeamPackage#getOneDrive_AuthURL()
	 * @model
	 * @generated
	 */
	String getAuthURL();

	/**
	 * Sets the value of the '{@link it.naturtalent.team.model.team.OneDrive#getAuthURL <em>Auth URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Auth URL</em>' attribute.
	 * @see #getAuthURL()
	 * @generated
	 */
	void setAuthURL(String value);

	/**
	 * Returns the value of the '<em><b>Redirect URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Redirect URI</em>' attribute.
	 * @see #setRedirectURI(String)
	 * @see it.naturtalent.team.model.team.TeamPackage#getOneDrive_RedirectURI()
	 * @model
	 * @generated
	 */
	String getRedirectURI();

	/**
	 * Sets the value of the '{@link it.naturtalent.team.model.team.OneDrive#getRedirectURI <em>Redirect URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Redirect URI</em>' attribute.
	 * @see #getRedirectURI()
	 * @generated
	 */
	void setRedirectURI(String value);

	/**
	 * Returns the value of the '<em><b>Auth Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Auth Code</em>' attribute.
	 * @see #setAuthCode(String)
	 * @see it.naturtalent.team.model.team.TeamPackage#getOneDrive_AuthCode()
	 * @model
	 * @generated
	 */
	String getAuthCode();

	/**
	 * Sets the value of the '{@link it.naturtalent.team.model.team.OneDrive#getAuthCode <em>Auth Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Auth Code</em>' attribute.
	 * @see #getAuthCode()
	 * @generated
	 */
	void setAuthCode(String value);

	/**
	 * Returns the value of the '<em><b>Access Token</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Access Token</em>' attribute.
	 * @see #setAccessToken(String)
	 * @see it.naturtalent.team.model.team.TeamPackage#getOneDrive_AccessToken()
	 * @model
	 * @generated
	 */
	String getAccessToken();

	/**
	 * Sets the value of the '{@link it.naturtalent.team.model.team.OneDrive#getAccessToken <em>Access Token</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Access Token</em>' attribute.
	 * @see #getAccessToken()
	 * @generated
	 */
	void setAccessToken(String value);

} // OneDrive
