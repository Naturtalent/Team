package it.naturtalent.team.ui;



import it.naturtalent.e4.project.IImportAdapter;
import it.naturtalent.icons.core.Icon;
import it.naturtalent.icons.core.IconSize;
import it.naturtalent.team.ui.actions.TransferImportAction;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.graphics.Image;

/**
 * Mit diesem Adapter koennen im OneDriveTransfer-Verzeichnis abgelegte Projekt-Dateien importiert werden.
 * 
 * Prinzip: Mit dem Systembrowser werden Projekte aus der OneDrive-Cloud in das Transferverzeichnis gezogen.
 * Von dort werden die sekektierten Projekte mit der Standard-Projektimport Funktion importiert.
 * Datenaustausch mit der Cloud ohne OneDrive-API
 * 
 * @author dieter
 *
 */
public class TransferImportAdapter implements IImportAdapter
{

	@Override
	public String getLabel()
	{				
		return "Projektimport aus Transferverzeichnis";
	}

	@Override
	public Image getImage()
	{
		return Icon.ICON_PROJECT.getImage(IconSize._16x16_DefaultIconSize);			
	}

	@Override
	public String getContext()
	{
		// TODO Auto-generated method stub
		return "Cloud";
	}

	@Override
	public String getMessage()
	{
		return "Projekte importieren";
	}

	@Override
	public Action getImportAction()
	{				
		return new TransferImportAction();
	}

}
