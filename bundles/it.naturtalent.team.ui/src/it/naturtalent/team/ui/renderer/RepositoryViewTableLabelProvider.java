package it.naturtalent.team.ui.renderer;

import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

import it.naturtalent.icons.core.Icon;
import it.naturtalent.icons.core.IconSize;
import it.naturtalent.team.model.team.Branch;
import it.naturtalent.team.ui.TeamModelUtils;

public class RepositoryViewTableLabelProvider extends LabelProvider implements ITableLabelProvider,IFontProvider	
{

	@Override
	public Font getFont(Object element)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex)
	{
		if (columnIndex == 0)
		{
			if (element instanceof Branch)
			{
				Branch branch = (Branch) element;
				if (TeamModelUtils.getProjectBranchProject(branch) != null)
					return Icon.ICON_PROJECT_OPEN.getImage(IconSize._16x16_DefaultIconSize);
			}
		}
		
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex)
	{
		if (element instanceof Branch)
		{
			Branch branch = (Branch) element;
			switch (columnIndex)
				{
					case 0:
						return branch.getName();
						
					case 1:
						return branch.getId();
						
					default:
						break;
				}
			
		}
		return element.toString();
	}

}
