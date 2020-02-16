/**
 */
package it.naturtalent.team.model.team.impl;

import it.naturtalent.team.model.team.OneDrive;
import it.naturtalent.team.model.team.TeamPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>One Drive</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link it.naturtalent.team.model.team.impl.OneDriveImpl#getClientID <em>Client ID</em>}</li>
 *   <li>{@link it.naturtalent.team.model.team.impl.OneDriveImpl#getAuthURL <em>Auth URL</em>}</li>
 *   <li>{@link it.naturtalent.team.model.team.impl.OneDriveImpl#getRedirectURI <em>Redirect URI</em>}</li>
 *   <li>{@link it.naturtalent.team.model.team.impl.OneDriveImpl#getAuthCode <em>Auth Code</em>}</li>
 *   <li>{@link it.naturtalent.team.model.team.impl.OneDriveImpl#getAccessToken <em>Access Token</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OneDriveImpl extends MinimalEObjectImpl.Container implements OneDrive
{
	/**
	 * The default value of the '{@link #getClientID() <em>Client ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getClientID()
	 * @generated
	 * @ordered
	 */
	protected static final String CLIENT_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getClientID() <em>Client ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getClientID()
	 * @generated
	 * @ordered
	 */
	protected String clientID = CLIENT_ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getAuthURL() <em>Auth URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAuthURL()
	 * @generated
	 * @ordered
	 */
	protected static final String AUTH_URL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAuthURL() <em>Auth URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAuthURL()
	 * @generated
	 * @ordered
	 */
	protected String authURL = AUTH_URL_EDEFAULT;

	/**
	 * The default value of the '{@link #getRedirectURI() <em>Redirect URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRedirectURI()
	 * @generated
	 * @ordered
	 */
	protected static final String REDIRECT_URI_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRedirectURI() <em>Redirect URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRedirectURI()
	 * @generated
	 * @ordered
	 */
	protected String redirectURI = REDIRECT_URI_EDEFAULT;

	/**
	 * The default value of the '{@link #getAuthCode() <em>Auth Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAuthCode()
	 * @generated
	 * @ordered
	 */
	protected static final String AUTH_CODE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAuthCode() <em>Auth Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAuthCode()
	 * @generated
	 * @ordered
	 */
	protected String authCode = AUTH_CODE_EDEFAULT;

	/**
	 * The default value of the '{@link #getAccessToken() <em>Access Token</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAccessToken()
	 * @generated
	 * @ordered
	 */
	protected static final String ACCESS_TOKEN_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAccessToken() <em>Access Token</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAccessToken()
	 * @generated
	 * @ordered
	 */
	protected String accessToken = ACCESS_TOKEN_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OneDriveImpl()
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
		return TeamPackage.Literals.ONE_DRIVE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getClientID()
	{
		return clientID;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setClientID(String newClientID)
	{
		String oldClientID = clientID;
		clientID = newClientID;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TeamPackage.ONE_DRIVE__CLIENT_ID, oldClientID, clientID));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getAuthURL()
	{
		return authURL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setAuthURL(String newAuthURL)
	{
		String oldAuthURL = authURL;
		authURL = newAuthURL;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TeamPackage.ONE_DRIVE__AUTH_URL, oldAuthURL, authURL));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getRedirectURI()
	{
		return redirectURI;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRedirectURI(String newRedirectURI)
	{
		String oldRedirectURI = redirectURI;
		redirectURI = newRedirectURI;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TeamPackage.ONE_DRIVE__REDIRECT_URI, oldRedirectURI, redirectURI));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getAuthCode()
	{
		return authCode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setAuthCode(String newAuthCode)
	{
		String oldAuthCode = authCode;
		authCode = newAuthCode;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TeamPackage.ONE_DRIVE__AUTH_CODE, oldAuthCode, authCode));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getAccessToken()
	{
		return accessToken;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setAccessToken(String newAccessToken)
	{
		String oldAccessToken = accessToken;
		accessToken = newAccessToken;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TeamPackage.ONE_DRIVE__ACCESS_TOKEN, oldAccessToken, accessToken));
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
			case TeamPackage.ONE_DRIVE__CLIENT_ID:
				return getClientID();
			case TeamPackage.ONE_DRIVE__AUTH_URL:
				return getAuthURL();
			case TeamPackage.ONE_DRIVE__REDIRECT_URI:
				return getRedirectURI();
			case TeamPackage.ONE_DRIVE__AUTH_CODE:
				return getAuthCode();
			case TeamPackage.ONE_DRIVE__ACCESS_TOKEN:
				return getAccessToken();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue)
	{
		switch (featureID)
		{
			case TeamPackage.ONE_DRIVE__CLIENT_ID:
				setClientID((String)newValue);
				return;
			case TeamPackage.ONE_DRIVE__AUTH_URL:
				setAuthURL((String)newValue);
				return;
			case TeamPackage.ONE_DRIVE__REDIRECT_URI:
				setRedirectURI((String)newValue);
				return;
			case TeamPackage.ONE_DRIVE__AUTH_CODE:
				setAuthCode((String)newValue);
				return;
			case TeamPackage.ONE_DRIVE__ACCESS_TOKEN:
				setAccessToken((String)newValue);
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
			case TeamPackage.ONE_DRIVE__CLIENT_ID:
				setClientID(CLIENT_ID_EDEFAULT);
				return;
			case TeamPackage.ONE_DRIVE__AUTH_URL:
				setAuthURL(AUTH_URL_EDEFAULT);
				return;
			case TeamPackage.ONE_DRIVE__REDIRECT_URI:
				setRedirectURI(REDIRECT_URI_EDEFAULT);
				return;
			case TeamPackage.ONE_DRIVE__AUTH_CODE:
				setAuthCode(AUTH_CODE_EDEFAULT);
				return;
			case TeamPackage.ONE_DRIVE__ACCESS_TOKEN:
				setAccessToken(ACCESS_TOKEN_EDEFAULT);
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
			case TeamPackage.ONE_DRIVE__CLIENT_ID:
				return CLIENT_ID_EDEFAULT == null ? clientID != null : !CLIENT_ID_EDEFAULT.equals(clientID);
			case TeamPackage.ONE_DRIVE__AUTH_URL:
				return AUTH_URL_EDEFAULT == null ? authURL != null : !AUTH_URL_EDEFAULT.equals(authURL);
			case TeamPackage.ONE_DRIVE__REDIRECT_URI:
				return REDIRECT_URI_EDEFAULT == null ? redirectURI != null : !REDIRECT_URI_EDEFAULT.equals(redirectURI);
			case TeamPackage.ONE_DRIVE__AUTH_CODE:
				return AUTH_CODE_EDEFAULT == null ? authCode != null : !AUTH_CODE_EDEFAULT.equals(authCode);
			case TeamPackage.ONE_DRIVE__ACCESS_TOKEN:
				return ACCESS_TOKEN_EDEFAULT == null ? accessToken != null : !ACCESS_TOKEN_EDEFAULT.equals(accessToken);
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
		result.append(" (clientID: ");
		result.append(clientID);
		result.append(", authURL: ");
		result.append(authURL);
		result.append(", redirectURI: ");
		result.append(redirectURI);
		result.append(", authCode: ");
		result.append(authCode);
		result.append(", accessToken: ");
		result.append(accessToken);
		result.append(')');
		return result.toString();
	}

} //OneDriveImpl
