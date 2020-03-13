package it.naturtalent.team.ui;

import static java.net.HttpURLConnection.HTTP_OK;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.net.ssl.TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.ECPProject;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;

import it.naturtalent.team.model.team.OneDrive;






/**
 * @author dieter
 * 
 * Hilskonstruktionen fuer den Zugriff auf MS OneDrive Cloud Zugriffe.
 *
 * URL Dokumentation 'Get access on behalf of a user'- Benutzerzugriff
 * https://docs.microsoft.com/en-us/graph/auth-v2-user?context=graph%2Fapi%2F1.0&view=graph-rest-1.0
 * 
 * MS Azure - MS Registrierungstool 
 * https://portal.azure.com/#blade/Microsoft_AAD_RegisteredApps/ApplicationMenuBlade/Authentication/appId/ecbfdf6a-ebc2-4727-9cb1-3b3b347f1391/isMSAApp/true
 */
public class OneDriveHelper
{

	// Ergebnisse der Registrierung (Azur) und Au­to­ri­sie­rung, hardcoded fuer Tests
	private static final String CLIENT_ID = "ecbfdf6a-ebc2-4727-9cb1-3b3b347f1391"; // in MS-Azure fuer Gabi registriert
	private static final String NATIVECLIENT_CODE = "Maadf061a-5412-ce08-fe75-4eb87642b23e";

	private static final String AUTH_URL = "https://login.microsoftonline.com/common/oauth2/v2.0";
	
	private String auth_url = null;
	private String[] scopes = null;
	private String client_id = null;
	private String redirect_uri = null;
	
	
	// Basic Protocols (Endpunkte)
	private static final String TENANT_COMMON_PROTOCOL = "https://login.microsoftonline.com/common/oauth2/v2.0/";
	private static final String TENANT_ORGANIZATIONS_PROTOCOL = "https://login.microsoftonline.com/organizations/oauth2/v2.0/";
	private static final String TENANT_CONSUMERS_PROTOCOL = "https://login.microsoftonline.com/consumers/oauth2/v2.0/";
	private static final String TENANT_MSIDENTITY_PROTOCOL = "https://login.microsoftonline.com/contoso.onmicrosoft.com/oauth2/v2.0/";
	
	// Prototyp der URL zur Authorisierung (Ziel ist den Autorisierungscode 'code' zu erlagnen
	private static final String AUTHORIZATION_URL = TENANT_COMMON_PROTOCOL+"authorize?client_id={client_id}&scope={scope}&redirect_uri={redirect_uri}&response_type={response_type}";	

	// Prototyp der URL zur Abfrage des Access-Tokens
	private static final String TOKEN_URL = TENANT_COMMON_PROTOCOL+"token?client_id={client_id}&scope={scope}&code={code}&redirect_uri={redirect_uri}&grant_type={grant_type}";
	
	private AuthenticationInfo authInfo;
	
	private String fullToken;
	
	public static final JsonFactory jsonFactory = new JsonFactory();

	/*
	 * 
	 */
	public void requestToken() throws IOException, URISyntaxException 
	{
		StringBuilder scope = new StringBuilder();
		for (String s : scopes) scope.append("%20").append(s);

		String url = String
				.format("%s/authorize?client_id=%s&scope=%s&response_type=code&redirect_uri=%s",
						AUTH_URL, client_id, scope.toString(), redirect_uri)
				.replace(" ", "%20");
		
		
		Desktop.getDesktop().browse(new URI(url));
		
	}


	/*
	 * 
	 */
	public void requestAuth() throws IOException, URISyntaxException 
	{
		StringBuilder scope = new StringBuilder();
		for (String s : scopes) scope.append("%20").append(s);

		String url = String
				.format("%s/authorize?client_id=%s&scope=%s&response_type=code&redirect_uri=%s",
						AUTH_URL, 
						client_id, 
						scope.toString(), 
						redirect_uri)
				.replace(" ", "%20");
				
		Desktop.getDesktop().browse(new URI(url));		
	}
	
	/*
	 * 
	 */
	public String getCode() 
	{
		StringBuilder scope = new StringBuilder();
		for (String s : scopes) scope.append("%20").append(s);

		String url = String
				.format("%s/authorize?client_id=%s&scope=%s&response_type=code&redirect_uri=%s",
						AUTH_URL, client_id, scope.toString(), redirect_uri)
				.replace(" ", "%20");

		Semaphore answerLock = new Semaphore(1);

		AuthServer server = new AuthServer(answerLock);
		server.start();

		try {
			Desktop.getDesktop().browse(new URI(url));
		}
		catch (URISyntaxException e) {
			throw new InternalException(
					"Fail to create URI object. probably wrong url on SDK code, contact the author", e);
		}
		catch (IOException e) {
			throw new UnsupportedOperationException("Can not find default browser for authentication.", e);
		}

		try {
			answerLock.acquire();
		}
		catch (InterruptedException e) {
			// FIXME: custom exception
			throw new RuntimeException(SyncRequest.NETWORK_ERR_MSG + " Lock Error In " + this.getClass().getName());
		}

		String code = server.close();
		answerLock.release();

		if (code == null) {
			// FIXME: custom exception
			throw new RuntimeException(SyncRequest.NETWORK_ERR_MSG);
		}

		return code;
	}
	
	/*
	 * 
	 */
	public String getToken(String httpBody) 
	{
		SyncResponse response = new SyncRequest(AUTH_URL + "/token")
				.setHeader("Content-Type", "application/x-www-form-urlencoded")
				.doPost(httpBody);

		try
		{
			authInfo = parseAuthAndHandle(response, HTTP_OK);
		} catch (ErrorResponseException e)
		{
			throw new InternalException(
					"failed to acquire login token. check login info", e);
		}

		this.fullToken = authInfo.getTokenType() + ' '
				+ authInfo.getAccessToken();

		return authInfo.getAccessToken();
	}
	
	TrustManager m;
	
	/*
	 * 
	 */
	public AuthenticationInfo parseAuthAndHandle(SyncResponse response, int expectedCode)
			throws ErrorResponseException {
		try {
			JsonParser parser = jsonFactory.createParser(response.getContent());
			parser.nextToken();

			if (response.getCode() == expectedCode) {
				return AuthenticationInfo.deserialize(parser, true);
			}
			else {
				ErrorResponse err = ErrorResponse.deserialize(parser, true);
				throw new ErrorResponseException(expectedCode, response.getCode(), err.getCode(), err.getMessage());
			}
		}
		catch (IOException e) {
			// FIXME: custom exception
			throw new RuntimeException("DEV: Unrecognizable json response.", e);
		}
	}
	
	/*
	 * 
	 */
	public static void authorizationRequest() throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException
	{
		HttpClient httpClient = createTrustAllHttpClientBuilder().build();
		
		//HttpPost request = new HttpPost("https://login.microsoftonline.com/common/oauth2/v2.0/token");
		HttpPost request = new HttpPost("https://login.microsoftonline.com");
		request.setHeader("Content-Type", "application/x-www-form-urlencoded");
	
	
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("client_id", CLIENT_ID));
		pairs.add(new BasicNameValuePair("scope", "user.read"));
		pairs.add(new BasicNameValuePair("code", NATIVECLIENT_CODE));
		pairs.add(new BasicNameValuePair("redirect_uri", getDefaultRedirectURI()));
		pairs.add(new BasicNameValuePair("grant_type", "authorization_code"));		
		request.setEntity(new UrlEncodedFormEntity(pairs));
	
		
		HttpResponse response = httpClient.execute(request);
		if (response.getStatusLine().getStatusCode() == 200) {
			{
				String result = IOUtils.toString(response.getEntity().getContent(), "utf-8");
				System.out.println(result);
			}
		} else {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}
		
		
	}
	
	public static HttpClientBuilder createTrustAllHttpClientBuilder()
			throws NoSuchAlgorithmException, KeyStoreException,
			KeyManagementException
	{
		SSLContextBuilder builder = new SSLContextBuilder();
		builder.loadTrustMaterial(null, (chain, authType) -> true);
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				builder.build(), NoopHostnameVerifier.INSTANCE);
		return HttpClients.custom().setSSLSocketFactory(sslsf);
	}
	
	/*
	 * 
	 */
	public static String getTokenRequestURL()
	{
		// Endpunkt 'common' der Protocol Basis
		String urlToken = TOKEN_URL;
		
		// scope
		String scope = "user.read"+" "+"mail.read";	
		
		// grant_type
		String grant_type = "authorization_code";
		
		// Parameter substituieren
		urlToken = StringUtils.replace(urlToken, "{client_id}", CLIENT_ID);
		urlToken = StringUtils.replace(urlToken, "{scope}", scope);		
		urlToken = StringUtils.replace(urlToken, "{redirect_uri}", getDefaultRedirectURI());
		urlToken = StringUtils.replace(urlToken, "{code}", NATIVECLIENT_CODE);
		urlToken = StringUtils.replace(urlToken, "{grant_type}", grant_type);
		
		urlToken = StringUtils.replace(urlToken," ","%20");
		return urlToken;
	}
	
	/*
	 * Gibt die Default Redirect-URI zurueck (muss auch in Azur konfiguriert werden)
	 * 
	 */
	private static String getDefaultRedirectURI()
	{
		String redirectURI = "https://login.microsoftonline.com/common/oauth2/nativeclient";
		redirectURI = StringUtils.replace(redirectURI,"/","%2F");
		redirectURI = StringUtils.replace(redirectURI,":","%3A");
		return redirectURI;
	}

	public void setScopes(String[] scopes)
	{
		this.scopes = scopes;
	}

	public void setRedirect_uri(String redirect_uri)
	{
		this.redirect_uri = redirect_uri;
	}

	public void setClient_id(String client_id)
	{
		this.client_id = client_id;
	}
	
	public void setAuth_url(String auth_url)
	{
		this.auth_url = auth_url;
	}

	/* 
	 * DriveOne laden
	 */
	public static OneDrive loadOneDriveInfo()
	{
		ECPProject ecpProject = Activator.getTeamECPProject();
		if(ecpProject != null)
		{
			EList<Object>teamObjects = ecpProject.getContents();
			for(Object teamObj : teamObjects)
			{
				if (teamObj instanceof OneDrive)
					return (OneDrive) teamObj;
			}			
		}
		
		return null;
	}
	
	
}
