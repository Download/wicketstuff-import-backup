package com.inmethod.grid.examples.pages.datagrid;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.model.ResourceModel;

import com.inmethod.grid.IGridColumn;
import com.inmethod.grid.SizeUnit;
import com.inmethod.grid.column.PropertyColumn;
import com.inmethod.grid.datagrid.DataGrid;
import com.inmethod.grid.datagrid.DefaultDataGrid;
import com.inmethod.grid.examples.contact.Contact;
import com.inmethod.grid.examples.pages.BaseExamplePage;

/**
 * Page with {@link DataGrid} that shows vertical scrolling.
 * 
 * @author Matej Knopp
 */
public class VerticalScrollingDataGridPage extends BaseExamplePage {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public VerticalScrollingDataGridPage() {
		List<IGridColumn<Contact>> columns = new ArrayList<IGridColumn<Contact>>();

		columns.add(new PropertyColumn<Contact, String, Long>(new ResourceModel("id"), "id"));
		columns.add(new PropertyColumn<Contact, String, String>(new ResourceModel("firstName"), "firstName", "firstName"));
		columns.add(new PropertyColumn<Contact, String, String>(new ResourceModel("lastName"), "lastName", "lastName"));
		columns.add(new PropertyColumn<Contact, String, String>(new ResourceModel("homePhone"), "homePhone"));
		columns.add(new PropertyColumn<Contact, String, String>(new ResourceModel("cellPhone"), "cellPhone"));

		DataGrid<Contact> grid = new DefaultDataGrid<Contact>("grid", new ContactDataSource(), columns);
		add(grid);
		
		grid.setRowsPerPage(30);
		grid.setContentHeight(25, SizeUnit.EM);
	}

}
