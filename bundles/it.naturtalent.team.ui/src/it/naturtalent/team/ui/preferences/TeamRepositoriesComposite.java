package it.naturtalent.team.ui.preferences;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

import it.naturtalent.e4.preferences.EditorDialog;
import it.naturtalent.e4.preferences.ListEditorComposite;

public class TeamRepositoriesComposite extends ListEditorComposite
{

	public TeamRepositoriesComposite(Composite parent, int style)
	{
		super(parent, style);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Rectangle getBounds()
	{
		Rectangle bounds = super.getBounds();
		bounds.height = 200;
		
		// TODO Auto-generated method stub
		return bounds;
	}

	@Override
	protected void doAdd()
	{		
		dialog = new EditorDialog(getShell(), "Repository", "ein neuen Repositoy definieren","",validator);
		if(dialog.open() == EditorDialog.OK)			
			list.add(dialog.getValue());	
	}
	
	


}
