package it.naturtalent.team.ui;

import io.netty.buffer.ByteBuf;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:bh322yoo@gmail.com" target="_top">isac322</a>
 */
public class SyncResponse
{
	protected final URL url;

	protected final int code;

	protected final String message;

	protected final Map<String, List<String>> header;

	protected final ByteBuf contentBuf;

	protected String contentString;

	public SyncResponse(URL url, int code, String message,
			Map<String, List<String>> header, ByteBuf contentBuf)
	{
		this.url = url;
		this.code = code;
		this.message = message;
		this.header = header;
		this.contentBuf = contentBuf;
	}

	/**
	 * For programmer's convenience, return decoded HTTP response's body as
	 * {@code String}.<br>
	 * Always decode with <b>UTF-8</b>.<br>
	 * If you want to decode the body with another encoding, you should call
	 * {@code getContent()} and decode manually.
	 *
	 * @return HTTP response body as {@code String}. Body is forced to decode
	 *         with UTF-8.
	 */
	public String getContentString()
	{
		if (contentString == null)
		{
			contentString = new String(contentBuf.array(), 0,
					contentBuf.readableBytes(), StandardCharsets.UTF_8);
		}

		return contentString;
	}

	public byte[] getContent()
	{
		return getContent(true);
	}

	public byte[] getContent(boolean autoRelease)
	{
		byte[] array = contentBuf.array();
		if (autoRelease)
			release();
		return array;
	}

	public void release()
	{
		contentBuf.release();
	}

	public int getCode()
	{
		return code;
	}
	
	
}
