package it.naturtalent.team.ui.renderer;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.table.model.VTableControl;
import org.eclipse.emf.ecp.view.spi.table.swt.TableControlSWTRenderer;
import org.eclipse.emf.ecp.view.spi.util.swt.ImageRegistryService;
import org.eclipse.emf.ecp.view.template.model.VTViewTemplateProvider;
import org.eclipse.emf.ecp.view.template.style.tableStyleProperty.model.VTTableStyleProperty;
import org.eclipse.emf.ecp.view.template.style.tableValidation.model.VTTableValidationStyleProperty;
import org.eclipse.emfforms.spi.common.report.ReportService;
import org.eclipse.emfforms.spi.core.services.databinding.EMFFormsDatabinding;
import org.eclipse.emfforms.spi.core.services.databinding.emf.EMFFormsDatabindingEMF;
import org.eclipse.emfforms.spi.core.services.editsupport.EMFFormsEditSupport;
import org.eclipse.emfforms.spi.core.services.label.EMFFormsLabelProvider;
import org.eclipse.jface.viewers.AbstractTableViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import it.naturtalent.icons.core.Icon;
import it.naturtalent.icons.core.IconSize;
import it.naturtalent.team.model.team.Branch;
import it.naturtalent.team.model.team.ReposData;
import it.naturtalent.team.ui.TeamModelUtils;



/**
 * Renderer der die FootNoteItems in einer Tabelle darstellt und somit die eigentliche Footnotes darstellt die in einem
 * Anschreiben dargestellt werden.
 * 
 * @author dieter
 *
 */
public class BranchTableControlRenderer extends TableControlSWTRenderer
{
	private ESelectionService selectionService;
	
	@Inject private IEventBroker eventBroker;
	
	private TableViewer tableViewer;
	
	@Inject
	public BranchTableControlRenderer(VTableControl vElement,
			ViewModelContext viewContext, ReportService reportService,
			EMFFormsDatabinding emfFormsDatabinding,
			EMFFormsLabelProvider emfFormsLabelProvider,
			VTViewTemplateProvider vtViewTemplateProvider,
			ImageRegistryService imageRegistryService,
			EMFFormsEditSupport emfFormsEditSupport)
	{
		super(vElement, viewContext, reportService, (EMFFormsDatabindingEMF) emfFormsDatabinding,
				emfFormsLabelProvider, vtViewTemplateProvider, imageRegistryService,
				emfFormsEditSupport);
		
		// Selectionservice
		MApplication currentApplication = E4Workbench.getServiceContext().get(IWorkbench.class).getApplication();		
		selectionService = currentApplication.getContext().get(ESelectionService.class);

	}

	/*
	 * Validation Spalte unsichtbar machen
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.table.swt.TableControlSWTRenderer#getTableValidationStyleProperty()
	 */
	@Override
	protected VTTableValidationStyleProperty getTableValidationStyleProperty()
	{
		VTTableValidationStyleProperty styleProperty = super.getTableValidationStyleProperty();		
		styleProperty.setColumnWidth(1);		
		return styleProperty;
	}
	
	@Override
	protected void setTableViewer(AbstractTableViewer abstractTableViewer)
	{			
		abstractTableViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{			
			@Override
			public void selectionChanged(SelectionChangedEvent event)
			{
				IStructuredSelection selection = abstractTableViewer.getStructuredSelection();
				Object selObj = selection.getFirstElement();
				if (selObj instanceof Branch)	
				{
					selectionService.setSelection(selObj);	
					
					Branch branch = (Branch) selObj;
					
					Object obj = event.getSource();
					if (obj instanceof TableViewer)
					{
						boolean flag = (TeamModelUtils.getProjectBranchProject(branch) == null) ? true : false;						
						Table table = ((TableViewer) obj).getTable();
						Menu menue = table.getMenu();
						MenuItem mntmCopy = menue.getItem(0);
						mntmCopy.setEnabled(flag);
					}
				}
			}
		});
		
		if (abstractTableViewer instanceof TableViewer)
		{
			tableViewer = ((TableViewer)abstractTableViewer);
			Table table = ((TableViewer)abstractTableViewer).getTable();

			// Spaltenbreite festlegen
			TableColumn column = table.getColumn(0);
			column.setWidth(450);
			
			// Kontextmenue einfuegen
			constructContextMenue(tableViewer);
			
			tableViewer.setLabelProvider(new RepositoryViewTableLabelProvider());		
		}
		
		super.setTableViewer(abstractTableViewer);
	}

	private void constructContextMenue(TableViewer tableViewer)
	{		
		Menu menu = new Menu(tableViewer.getControl());
		tableViewer.getTable().setMenu(menu);
		
		MenuItem mntmCopy = new MenuItem(menu, SWT.NONE);
		mntmCopy.setEnabled(false);
		mntmCopy.setText("kopieren");
		mntmCopy.setImage(Icon.COMMAND_CLONE_GIT.getImage(IconSize._16x16_DefaultIconSize));
		
		mntmCopy.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				IStructuredSelection selection = tableViewer.getStructuredSelection();
				Object selObj = selection.getFirstElement();
				eventBroker.post(TeamModelUtils.CLONE_PROPJECTBRANCH_VIEW_EVENT, selObj);
			}
		});		
	}

}
