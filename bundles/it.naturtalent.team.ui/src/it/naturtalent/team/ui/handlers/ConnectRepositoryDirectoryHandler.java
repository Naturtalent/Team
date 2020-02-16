 
package it.naturtalent.team.ui.handlers;

import java.io.IOException;

import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.spi.ui.util.ECPHandlerHelper;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import it.naturtalent.team.ui.Activator;
import it.naturtalent.team.ui.OneDriveHelper;
import it.naturtalent.team.ui.wizards.OneDriveWizard;

public class ConnectRepositoryDirectoryHandler
{
	// Ergebnisse der Authentifizierung
	private static final String CLIENT_ID = "ecbfdf6a-ebc2-4727-9cb1-3b3b347f1391";
	private static final String NATIVECLIENT_CODE = "Mee046302-6ccd-7359-b205-a914f7fe6d2b";
	
	// Basic Protocols (Endpunkte)
	private static final String TENANT_COMMON_PROTOCOL = "https://login.microsoftonline.com/common/oauth2/v2.0/";
	private static final String TENANT_ORGANIZATIONS_PROTOCOL = "https://login.microsoftonline.com/organizations/oauth2/v2.0/";
	private static final String TENANT_CONSUMERS_PROTOCOL = "https://login.microsoftonline.com/consumers/oauth2/v2.0/";
	private static final String TENANT_MSIDENTITY_PROTOCOL = "https://login.microsoftonline.com/contoso.onmicrosoft.com/oauth2/v2.0/";
	
	private static final String AUTHORIZATION_URL = TENANT_COMMON_PROTOCOL+"authorize?client_id={client_id}&scope={scope}&redirect_uri={redirect_uri}&response_type={response_type}";	
	
	
	private static final String AUTH_URL = "https://login.microsoftonline.com/common/oauth2/v2.0/authorize?client_id={client_id}&scope={scope}&response_type={responsetype}";
	
	private static final String SCOPE_OFFLINE_ACCESS = "offline_access";
	private static final String SCOPE_FILES_READ = "files.read";
	private static final String SCOPE_FILES_READ_ALL = "files.read.all";
	private static final String SCOPE_FILES_READ_WRITE = "files.readwrite";
	private static final String SCOPE_FILES_READ_WRITE_ALL = "files.readwrite.all";
	
	@Execute
	public void execute(IEclipseContext context, @Named (IServiceConstants.ACTIVE_SHELL) Shell shell)
	{
		
		// OneDrive Wizard starten
		OneDriveWizard oneDriveWizard = ContextInjectionFactory.make(OneDriveWizard.class, context);
		WizardDialog dialog = new WizardDialog(shell, oneDriveWizard);
		dialog.open();
		
		//doGetCode();
		//String url = getAuthorizationURL(null, null, null);
		//String url = OneDriveUtilities.getTokenRequestURL();
		//System.out.println(url);
	}

	private void doGetCode()
	{
		OneDriveHelper helper = new OneDriveHelper();
		String [] scopes = new String [] {"offline_access", "files.readwrite.all"};
		helper.setScopes(scopes);
		helper.setClient_id(CLIENT_ID);
		helper.setRedirect_uri("https://login.microsoftonline.com/common/oauth2/nativeclient");
		
		String code =  helper.getCode();
	}
		
	private void doGetToken()
	{
		try
		{		
			String client_id = CLIENT_ID;
			String grant_type = "authorization_code";
			String scope = "user.read";
			String code = NATIVECLIENT_CODE;
			String redirect_uri = "https://login.microsoftonline.com/common/oauth2/nativeclient";
			
			String httpBody = String.format("client_id=%s&grant_type=%s&scope=%s&code=%s&redirect_uri=%s",
					client_id, grant_type, scope, code, redirect_uri);
			//System.out.println(httpBody);
			
			OneDriveHelper helper = new OneDriveHelper();
			String token = helper.getToken(httpBody);
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	
	private String getAuthorizationURL(String clientID, String scope, String responsetype)
	{
		// Endpunkt 'common' der Protocol Basis
		String urlToken = AUTHORIZATION_URL;
		
		// scope
		scope = SCOPE_OFFLINE_ACCESS + " " + "user.read";
		
		// redirect_uri (Default fuer native Clients)
		String redirectURI = "https://login.microsoftonline.com/common/oauth2/nativeclient";
		redirectURI = StringUtils.replace(redirectURI,"/","%2F");
		redirectURI = StringUtils.replace(redirectURI,":","%3A");
		
		// response_type
		String response_type = "code";
		
		// ClientID wird von Azur - Konfiguration geliefert
		urlToken = StringUtils.replace(urlToken, "{client_id}", CLIENT_ID);
		
		urlToken = StringUtils.replace(urlToken, "{scope}", scope);
		urlToken = StringUtils.replace(urlToken, "{redirect_uri}", redirectURI);
		urlToken = StringUtils.replace(urlToken, "{response_type}", response_type);
		
		urlToken = StringUtils.replace(urlToken," ","%20");
		return urlToken;
	}
	
	
	
	public void executeTested()
	{
		//Pair<String, String> token = SPOnline.login("massage@gabriele-apel.de", "Majestix$01", "https://onedrive.live.com/?id=15E5D245E01D445A%21107&cid=15E5D245E01D445A");
		//String clientID = "massage@gabriele-apel.de";
		String clientID = "ecbfdf6a-ebc2-4727-9cb1-3b3b347f1391";
		String scope = SCOPE_FILES_READ;
		String responsetype = "token";
		String tokenUrl = getTokenURL(clientID, scope, responsetype);
		System.out.println(tokenUrl);
		//request(tokenUrl);		
	}
	
	private static void authorizationRequest() throws IOException
	{
		String authURL = TENANT_COMMON_PROTOCOL;
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		HttpGet getRequest = new HttpGet(authURL);
		HttpResponse response = httpClient.execute(getRequest);
		if (response.getStatusLine().getStatusCode() == 200) 
		{
			String resp = IOUtils.toString(response.getEntity().getContent(), "utf-8");
			System.out.println(resp);
		}
	}
	
	/*
	GET https://login.microsoftonline.com/common/oauth2/v2.0/authorize?client_id={client_id}&scope={scope}
	    &response_type=token&redirect_uri={redirect_uri}
	    */
	

	private String getTokenURL(String clientID, String scope, String responsetype)
	{
		String urlToken = AUTH_URL;
		urlToken = StringUtils.replace(urlToken, "{client_id}", clientID); 
		urlToken = StringUtils.replace(urlToken, "{scope}", scope);
		urlToken = StringUtils.replace(urlToken, "{responsetype}", responsetype);
		return urlToken;
	}
	
	private void request(String url)
	{
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet getRequest = new HttpGet(url);
		try
		{
			HttpResponse response = httpClient.execute(getRequest);
			System.out.println(response);
		} catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
}