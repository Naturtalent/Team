package it.naturtalent.team.ui;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import lombok.Getter;


import java.io.IOException;
import java.util.logging.Logger;


/**
 * @author <a href="mailto:bh322yoo@gmail.com" target="_top">isac322</a>
 */
public class AuthenticationInfo {
	@Getter @JsonProperty("token_type") protected  String tokenType;
	@Getter @JsonProperty("expires_in") protected long expiresIn;
	@Getter @JsonProperty("access_token") protected  String accessToken;
	@Getter @JsonProperty("refresh_token") protected  String refreshToken;
	@Getter protected String scope;

	protected AuthenticationInfo( String tokenType, long expiresIn,  String accessToken,
								  String refreshToken,  String scope) {
		this.tokenType = tokenType;
		this.expiresIn = expiresIn;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.scope = scope;
	}

	public static  AuthenticationInfo deserialize( JsonParser parser, boolean autoClose)
			throws IOException {
		 String tokenType = null;
		 Long expiresIn = null;
		 String accessToken = null;
		 String refreshToken = null;
		 String scope = null;

		while (parser.nextToken() != JsonToken.END_OBJECT) {
			String currentName = parser.getCurrentName();
			parser.nextToken();

			switch (currentName) {
				case "token_type":
					tokenType = parser.getText();
					break;
				case "expires_in":
					expiresIn = parser.getLongValue() * 1000 + System.currentTimeMillis();
					break;
				case "access_token":
					accessToken = parser.getText();
					break;
				case "refresh_token":
					refreshToken = parser.getText();
					break;
				case "scope":
					scope = parser.getText();
					break;
				case "ext_expires_in":
					// TODO
					break;
				default:
					Logger.getGlobal().info("Unknown attribute detected in AuthenticationInfo : " + currentName);
			}
		}

		if (autoClose) parser.close();

		assert tokenType != null : "tokenType is null";
		assert expiresIn != null : "expiresIn is null";
		assert accessToken != null : "accessToken is null";
		assert refreshToken != null : "refreshToken is null";
		assert scope != null : "scope is null";

		return new AuthenticationInfo(tokenType, expiresIn, accessToken, refreshToken, scope);
	}

	public String getTokenType()
	{
		return tokenType;
	}

	public String getAccessToken()
	{
		return accessToken;
	}
	
	
}
