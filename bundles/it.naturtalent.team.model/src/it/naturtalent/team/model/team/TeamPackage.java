/**
 */
package it.naturtalent.team.model.team;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see it.naturtalent.team.model.team.TeamFactory
 * @model kind="package"
 * @generated
 */
public interface TeamPackage extends EPackage
{
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "team";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://it.naturtalent/team";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "team";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	TeamPackage eINSTANCE = it.naturtalent.team.model.team.impl.TeamPackageImpl.init();

	/**
	 * The meta object id for the '{@link it.naturtalent.team.model.team.impl.ReposDataImpl <em>Repos Data</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see it.naturtalent.team.model.team.impl.ReposDataImpl
	 * @see it.naturtalent.team.model.team.impl.TeamPackageImpl#getReposData()
	 * @generated
	 */
	int REPOS_DATA = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REPOS_DATA__NAME = 0;

	/**
	 * The feature id for the '<em><b>Remote URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REPOS_DATA__REMOTE_URI = 1;

	/**
	 * The feature id for the '<em><b>Branches</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REPOS_DATA__BRANCHES = 2;

	/**
	 * The number of structural features of the '<em>Repos Data</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REPOS_DATA_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Repos Data</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REPOS_DATA_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link it.naturtalent.team.model.team.impl.BranchImpl <em>Branch</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see it.naturtalent.team.model.team.impl.BranchImpl
	 * @see it.naturtalent.team.model.team.impl.TeamPackageImpl#getBranch()
	 * @generated
	 */
	int BRANCH = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BRANCH__NAME = 0;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BRANCH__ID = 1;

	/**
	 * The number of structural features of the '<em>Branch</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BRANCH_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Branch</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BRANCH_OPERATION_COUNT = 0;


	/**
	 * The meta object id for the '{@link it.naturtalent.team.model.team.impl.LoginImpl <em>Login</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see it.naturtalent.team.model.team.impl.LoginImpl
	 * @see it.naturtalent.team.model.team.impl.TeamPackageImpl#getLogin()
	 * @generated
	 */
	int LOGIN = 2;

	/**
	 * The feature id for the '<em><b>User</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOGIN__USER = 0;

	/**
	 * The feature id for the '<em><b>Password</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOGIN__PASSWORD = 1;

	/**
	 * The feature id for the '<em><b>Domain</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOGIN__DOMAIN = 2;

	/**
	 * The feature id for the '<em><b>URL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOGIN__URL = 3;

	/**
	 * The number of structural features of the '<em>Login</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOGIN_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Login</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOGIN_OPERATION_COUNT = 0;


	/**
	 * The meta object id for the '{@link it.naturtalent.team.model.team.impl.OneDriveImpl <em>One Drive</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see it.naturtalent.team.model.team.impl.OneDriveImpl
	 * @see it.naturtalent.team.model.team.impl.TeamPackageImpl#getOneDrive()
	 * @generated
	 */
	int ONE_DRIVE = 3;

	/**
	 * The feature id for the '<em><b>Client ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ONE_DRIVE__CLIENT_ID = 0;

	/**
	 * The feature id for the '<em><b>Auth URL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ONE_DRIVE__AUTH_URL = 1;

	/**
	 * The feature id for the '<em><b>Redirect URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ONE_DRIVE__REDIRECT_URI = 2;

	/**
	 * The feature id for the '<em><b>Auth Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ONE_DRIVE__AUTH_CODE = 3;

	/**
	 * The feature id for the '<em><b>Access Token</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ONE_DRIVE__ACCESS_TOKEN = 4;

	/**
	 * The number of structural features of the '<em>One Drive</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ONE_DRIVE_FEATURE_COUNT = 5;

	/**
	 * The number of operations of the '<em>One Drive</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ONE_DRIVE_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link it.naturtalent.team.model.team.ReposData <em>Repos Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Repos Data</em>'.
	 * @see it.naturtalent.team.model.team.ReposData
	 * @generated
	 */
	EClass getReposData();

	/**
	 * Returns the meta object for the attribute '{@link it.naturtalent.team.model.team.ReposData#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see it.naturtalent.team.model.team.ReposData#getName()
	 * @see #getReposData()
	 * @generated
	 */
	EAttribute getReposData_Name();

	/**
	 * Returns the meta object for the attribute '{@link it.naturtalent.team.model.team.ReposData#getRemoteURI <em>Remote URI</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Remote URI</em>'.
	 * @see it.naturtalent.team.model.team.ReposData#getRemoteURI()
	 * @see #getReposData()
	 * @generated
	 */
	EAttribute getReposData_RemoteURI();

	/**
	 * Returns the meta object for the containment reference list '{@link it.naturtalent.team.model.team.ReposData#getBranches <em>Branches</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Branches</em>'.
	 * @see it.naturtalent.team.model.team.ReposData#getBranches()
	 * @see #getReposData()
	 * @generated
	 */
	EReference getReposData_Branches();

	/**
	 * Returns the meta object for class '{@link it.naturtalent.team.model.team.Branch <em>Branch</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Branch</em>'.
	 * @see it.naturtalent.team.model.team.Branch
	 * @generated
	 */
	EClass getBranch();

	/**
	 * Returns the meta object for the attribute '{@link it.naturtalent.team.model.team.Branch#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see it.naturtalent.team.model.team.Branch#getName()
	 * @see #getBranch()
	 * @generated
	 */
	EAttribute getBranch_Name();

	/**
	 * Returns the meta object for the attribute '{@link it.naturtalent.team.model.team.Branch#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see it.naturtalent.team.model.team.Branch#getId()
	 * @see #getBranch()
	 * @generated
	 */
	EAttribute getBranch_Id();

	/**
	 * Returns the meta object for class '{@link it.naturtalent.team.model.team.Login <em>Login</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Login</em>'.
	 * @see it.naturtalent.team.model.team.Login
	 * @generated
	 */
	EClass getLogin();

	/**
	 * Returns the meta object for the attribute '{@link it.naturtalent.team.model.team.Login#getUser <em>User</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>User</em>'.
	 * @see it.naturtalent.team.model.team.Login#getUser()
	 * @see #getLogin()
	 * @generated
	 */
	EAttribute getLogin_User();

	/**
	 * Returns the meta object for the attribute '{@link it.naturtalent.team.model.team.Login#getPassword <em>Password</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Password</em>'.
	 * @see it.naturtalent.team.model.team.Login#getPassword()
	 * @see #getLogin()
	 * @generated
	 */
	EAttribute getLogin_Password();

	/**
	 * Returns the meta object for the attribute '{@link it.naturtalent.team.model.team.Login#getDomain <em>Domain</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Domain</em>'.
	 * @see it.naturtalent.team.model.team.Login#getDomain()
	 * @see #getLogin()
	 * @generated
	 */
	EAttribute getLogin_Domain();

	/**
	 * Returns the meta object for the attribute '{@link it.naturtalent.team.model.team.Login#getURL <em>URL</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>URL</em>'.
	 * @see it.naturtalent.team.model.team.Login#getURL()
	 * @see #getLogin()
	 * @generated
	 */
	EAttribute getLogin_URL();

	/**
	 * Returns the meta object for class '{@link it.naturtalent.team.model.team.OneDrive <em>One Drive</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>One Drive</em>'.
	 * @see it.naturtalent.team.model.team.OneDrive
	 * @generated
	 */
	EClass getOneDrive();

	/**
	 * Returns the meta object for the attribute '{@link it.naturtalent.team.model.team.OneDrive#getClientID <em>Client ID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Client ID</em>'.
	 * @see it.naturtalent.team.model.team.OneDrive#getClientID()
	 * @see #getOneDrive()
	 * @generated
	 */
	EAttribute getOneDrive_ClientID();

	/**
	 * Returns the meta object for the attribute '{@link it.naturtalent.team.model.team.OneDrive#getAuthURL <em>Auth URL</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Auth URL</em>'.
	 * @see it.naturtalent.team.model.team.OneDrive#getAuthURL()
	 * @see #getOneDrive()
	 * @generated
	 */
	EAttribute getOneDrive_AuthURL();

	/**
	 * Returns the meta object for the attribute '{@link it.naturtalent.team.model.team.OneDrive#getRedirectURI <em>Redirect URI</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Redirect URI</em>'.
	 * @see it.naturtalent.team.model.team.OneDrive#getRedirectURI()
	 * @see #getOneDrive()
	 * @generated
	 */
	EAttribute getOneDrive_RedirectURI();

	/**
	 * Returns the meta object for the attribute '{@link it.naturtalent.team.model.team.OneDrive#getAuthCode <em>Auth Code</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Auth Code</em>'.
	 * @see it.naturtalent.team.model.team.OneDrive#getAuthCode()
	 * @see #getOneDrive()
	 * @generated
	 */
	EAttribute getOneDrive_AuthCode();

	/**
	 * Returns the meta object for the attribute '{@link it.naturtalent.team.model.team.OneDrive#getAccessToken <em>Access Token</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Access Token</em>'.
	 * @see it.naturtalent.team.model.team.OneDrive#getAccessToken()
	 * @see #getOneDrive()
	 * @generated
	 */
	EAttribute getOneDrive_AccessToken();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	TeamFactory getTeamFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals
	{
		/**
		 * The meta object literal for the '{@link it.naturtalent.team.model.team.impl.ReposDataImpl <em>Repos Data</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see it.naturtalent.team.model.team.impl.ReposDataImpl
		 * @see it.naturtalent.team.model.team.impl.TeamPackageImpl#getReposData()
		 * @generated
		 */
		EClass REPOS_DATA = eINSTANCE.getReposData();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REPOS_DATA__NAME = eINSTANCE.getReposData_Name();

		/**
		 * The meta object literal for the '<em><b>Remote URI</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REPOS_DATA__REMOTE_URI = eINSTANCE.getReposData_RemoteURI();

		/**
		 * The meta object literal for the '<em><b>Branches</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REPOS_DATA__BRANCHES = eINSTANCE.getReposData_Branches();

		/**
		 * The meta object literal for the '{@link it.naturtalent.team.model.team.impl.BranchImpl <em>Branch</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see it.naturtalent.team.model.team.impl.BranchImpl
		 * @see it.naturtalent.team.model.team.impl.TeamPackageImpl#getBranch()
		 * @generated
		 */
		EClass BRANCH = eINSTANCE.getBranch();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BRANCH__NAME = eINSTANCE.getBranch_Name();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BRANCH__ID = eINSTANCE.getBranch_Id();

		/**
		 * The meta object literal for the '{@link it.naturtalent.team.model.team.impl.LoginImpl <em>Login</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see it.naturtalent.team.model.team.impl.LoginImpl
		 * @see it.naturtalent.team.model.team.impl.TeamPackageImpl#getLogin()
		 * @generated
		 */
		EClass LOGIN = eINSTANCE.getLogin();

		/**
		 * The meta object literal for the '<em><b>User</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOGIN__USER = eINSTANCE.getLogin_User();

		/**
		 * The meta object literal for the '<em><b>Password</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOGIN__PASSWORD = eINSTANCE.getLogin_Password();

		/**
		 * The meta object literal for the '<em><b>Domain</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOGIN__DOMAIN = eINSTANCE.getLogin_Domain();

		/**
		 * The meta object literal for the '<em><b>URL</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOGIN__URL = eINSTANCE.getLogin_URL();

		/**
		 * The meta object literal for the '{@link it.naturtalent.team.model.team.impl.OneDriveImpl <em>One Drive</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see it.naturtalent.team.model.team.impl.OneDriveImpl
		 * @see it.naturtalent.team.model.team.impl.TeamPackageImpl#getOneDrive()
		 * @generated
		 */
		EClass ONE_DRIVE = eINSTANCE.getOneDrive();

		/**
		 * The meta object literal for the '<em><b>Client ID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ONE_DRIVE__CLIENT_ID = eINSTANCE.getOneDrive_ClientID();

		/**
		 * The meta object literal for the '<em><b>Auth URL</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ONE_DRIVE__AUTH_URL = eINSTANCE.getOneDrive_AuthURL();

		/**
		 * The meta object literal for the '<em><b>Redirect URI</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ONE_DRIVE__REDIRECT_URI = eINSTANCE.getOneDrive_RedirectURI();

		/**
		 * The meta object literal for the '<em><b>Auth Code</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ONE_DRIVE__AUTH_CODE = eINSTANCE.getOneDrive_AuthCode();

		/**
		 * The meta object literal for the '<em><b>Access Token</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ONE_DRIVE__ACCESS_TOKEN = eINSTANCE.getOneDrive_AccessToken();

	}

} //TeamPackage
