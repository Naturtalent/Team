package it.naturtalent.team.ui;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import lombok.Getter;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * <a href="https://dev.onedrive.com/misc/errors.htm">explain of error types</a>
 *
 * @author <a href="mailto:bh322yoo@gmail.com" target="_top">isac322</a>
 */
public class ErrorResponse
{
	
	protected final String code;

	
	protected final String message;

	@Getter
	protected final String requestId;

	@Getter
	protected final String date;

	protected ErrorResponse(String code, String message, String requestId,
			String date)
	{
		this.code = code;
		this.message = message;
		this.requestId = requestId;
		this.date = date;
	}

	public static ErrorResponse deserialize(JsonParser parser,
			boolean autoClose) throws IOException
	{
		String code = null;
		String message = null;
		String requestId = null;
		String date = null;

		while (parser.nextToken() != JsonToken.END_OBJECT)
		{
			String currentName = parser.getCurrentName();
			parser.nextToken();

			switch (currentName)
				{
					case "error":
						while (parser.nextToken() != JsonToken.END_OBJECT)
						{
							String fieldName = parser.getCurrentName();
							parser.nextToken();

							switch (fieldName)
								{
									case "code":
										code = parser.getText();
										break;
									case "message":
										message = parser.getText();
										break;
									case "innerError":
										// TODO
										break;
									case "request-id":
										requestId = parser.getText();
										break;
									case "date":
										date = parser.getText();
										break;
									default:
										Logger.getGlobal().info(String.format(
												"Unknown attribute detected in inner ErrorResponse : %s(%s)",
												fieldName, parser.getText()));
								}
						}
						break;
					default:
						Logger.getGlobal().info(String.format(
								"Unknown attribute detected in ErrorResponse : %s(%s)",
								currentName, parser.getText()));
				}
		}

		if (autoClose)
			parser.close();

		assert code != null : "code is null";
		assert message != null : "message is null";
		assert requestId != null : "requestId is null";
		assert date != null : "date is null";

		return new ErrorResponse(code, message, requestId, date);
	}

	public String getCode()
	{
		return code;
	}

	public String getMessage()
	{
		return message;
	}
	
	
}
