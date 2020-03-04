 
package it.naturtalent.team.ui.handlers;

import java.io.IOException;

import org.apache.commons.lang3.SystemUtils;
import org.eclipse.e4.core.di.annotations.Execute;

import it.naturtalent.team.ui.OneDriveUtils;

/*
 * Oeffnet das Cloudtransferverzeichnis im Systemdateibrowser und ermoeglicht das Speichern in einer Cloud (z.B. OneDrive) durch
 * Verschieben (Paste and Copy)
 * 
 */
public class OpenCloudTransferHandler
{
	@Execute
	public void execute()
	{
		String destPath = OneDriveUtils.getOneDriveExpImpDir().getPath();
		try
		{
			if (SystemUtils.IS_OS_LINUX)
				Runtime.getRuntime().exec("nautilus " + destPath);
			else
				Runtime.getRuntime().exec("explorer " + destPath);
		} catch (IOException exp)
		{
			if (SystemUtils.IS_OS_LINUX)
				try
				{
					Runtime.getRuntime().exec("nemo " + destPath);
					return;
				} catch (Exception e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			exp.printStackTrace();
		}
	}

}