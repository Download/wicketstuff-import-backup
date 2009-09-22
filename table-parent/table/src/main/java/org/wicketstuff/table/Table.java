/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wicketstuff.table;

import java.io.Serializable;
import java.util.Iterator;

import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.RowSorter.SortKey;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.wicketstuff.table.sorter.SerializableTableRowSorter;

/**
 * Table component to present an swing TableModel.
 * 
 * @author Pedro Henrique Oliveira dos Santos
 * 
 */
public class Table extends Panel implements IHeaderContributor
{

	// private static final Logger log = LoggerFactory.getLogger(Table.class);
	private static final long serialVersionUID = 1L;
	public static final ResourceReference TABLE_CSS = new ResourceReference(Table.class,
			"res/table.css");
	public static final ResourceReference ARROW_UP = new ResourceReference(Table.class,
			"res/arrow_up.png");
	public static final ResourceReference ARROW_OFF = new ResourceReference(Table.class,
			"res/arrow_off.png");
	public static final ResourceReference ARROW_DOWN = new ResourceReference(Table.class,
			"res/arrow_down.png");
	private TableListView rowsListView;
	private boolean autoCreateRowSorter;
	private ColumnsModelAdapter columnsModelAdapter;

	/**
	 * @param id
	 * @param swingTableModel
	 *            the tableModel need to be serializable, to exist along the
	 *            session.
	 */
	public Table(String id, TableModel swingTableModel)
	{
		super(id);
		setDefaultModel(new TableModelAdapter(swingTableModel));
		columnsModelAdapter = new ColumnsModelAdapter(getTableModel());
		setOutputMarkupId(true);
		add(new ListView("headers", columnsModelAdapter)
		{
			@Override
			protected void populateItem(final ListItem item)
			{
				final int columnIndex = columnsModelAdapter.convertIndexToModel(item.getIndex());
				String header = getTableModel().getColumnName(columnIndex);
				item.add(new Label("header", new ResourceModel(header, header)));
				item.add(new Image("arrow")
				{
					@Override
					protected ResourceReference getImageResourceReference()
					{
						if (getRowSorter() != null)
						{
							if (getRowSorter().getSortKeys() == null
									|| getRowSorter().getSortKeys().size() == 0)
							{
								return ARROW_OFF;
							}
							else
							{
								for (Iterator i = getRowSorter().getSortKeys().iterator(); i
										.hasNext();)
								{
									SortKey sortKey = (SortKey)i.next();
									if (sortKey.getColumn() == columnIndex)
									{
										if (sortKey.getSortOrder() == SortOrder.ASCENDING)
										{
											return ARROW_UP;
										}
										else if (sortKey.getSortOrder() == SortOrder.DESCENDING)
										{
											return ARROW_DOWN;
										}
									}
								}// for
								return ARROW_OFF;
							}
						}
						else
						{
							return null;
						}
					}

					@Override
					public boolean isVisible()
					{
						return getImageResourceReference() != null;
					}
				});
				item.add(new AjaxEventBehavior("onclick")
				{
					@Override
					protected void onEvent(AjaxRequestTarget target)
					{
						if (getRowSorter() != null)
						{
							getRowSorter().toggleSortOrder(columnIndex);
							target.addComponent(Table.this);
						}
					}
				});
				item.add(new AjaxEventBehavior("ondblclick")
				{
					@Override
					protected void onEvent(AjaxRequestTarget target)
					{
						if (getRowSorter() != null)
						{
							for (Iterator i = getRowSorter().getSortKeys().iterator(); i.hasNext();)
							{
								SortKey sortKey = (SortKey)i.next();
								if (sortKey.getColumn() == columnIndex)
								{
									i.remove();
								}
							}
							target.addComponent(Table.this);
						}
					}
				});
			}
		});
		add(rowsListView = new TableListView("rows", new ListModelAdapter(getTableModel())));
	}

	/**
	 * Repeating component that extends the AjaxSelectableListView. The extended
	 * behavior are the table model rendering complexity partially implemented.
	 * 
	 */
	class TableListView extends AjaxSelectableListView
	{


		public TableListView(String id, IModel model)
		{
			super(id, model);
		}

		@Override
		protected void onSelection(SelectableListItem selectableListItem, AjaxRequestTarget target)
		{
			Table.this.onSelection(selectableListItem.getIndexOnSelectionModel(), target);
		}

		@Override
		protected void populateRow(final ListItem rowItem, final int rowIndex)
		{
			rowItem.add(new ListView("collums", columnsModelAdapter)
			{
				@Override
				protected void populateItem(ListItem dataItem)
				{

					int columnIndex = columnsModelAdapter.convertIndexToModel(dataItem.getIndex());
					Object data = getTableModel().getValueAt(rowIndex, columnIndex);
					/*
					 * TODO from the table model we can get much more
					 * informations. Is possible to add checkboxes for booleans,
					 * image components for images, date components for dates,
					 * etc.
					 */
					if (getTableModel().isCellEditable(rowIndex, columnIndex))
					{
						dataItem.add(new SelfSubmitTextFieldPanel("data", new TableCellModel(
								getTableModel(), rowIndex, columnIndex)));
					}
					else
					{
						dataItem.add(new Label("data", data == null ? null : data.toString()));
					}
				}
			});
		}


	}

	public AjaxPagingNavigator getRowsAjaxPagingNavigator(String id, int rowsPerPage)
	{
		rowsListView.setRowsPerPage(rowsPerPage);
		return new AjaxPagingNavigator(id, rowsListView);
	}

	public AjaxPagingNavigator getColumnsAjaxPagingNavigator(String id, int columnsPerPage)
	{
		columnsModelAdapter.setColumnsPerPage(columnsPerPage);
		return new AjaxPagingNavigator(id, columnsModelAdapter)
		{
			@Override
			protected void onAjaxEvent(AjaxRequestTarget target)
			{
				target.addComponent(Table.this);
				target.addComponent(this);
			}
		};
	}

	/**
	 * Number of rows to be presented per page on table.
	 * 
	 * @param rowsPerPage
	 */
	public void setRowsPerPage(int rowsPerPage)
	{
		rowsListView.setRowsPerPage(rowsPerPage);
	}

	/**
	 * @see {@link javax.swing.JTable#setSelectionMode(int)}
	 */
	public void setSelectionMode(int selectionMode)
	{
		rowsListView.getListSelectionModel().setSelectionMode(selectionMode);
	}

	/**
	 * @see {@link javax.swing.JTable#getSelectionModel()}
	 */
	public ListSelectionModel getListSelectionModel()
	{
		return rowsListView.getListSelectionModel();
	}

	/**
	 * Add a listener to the list that's notified each time a change to the
	 * selection occurs.
	 */
	public void addListSelectionListener(ListSelectionListener x)
	{
		rowsListView.getListSelectionModel().addListSelectionListener(x);
	}

	public void setSelectionIndex(Integer newSelectionIndex)
	{
		rowsListView.getListSelectionModel().setSelectionInterval(newSelectionIndex,
				newSelectionIndex);
	}

	/**
	 * @see {@link javax.swing.JTable#getModel()}
	 */
	public TableModel getTableModel()
	{
		return (TableModel)getDefaultModelObject();
	}

	/**
	 * Adapter model to decorate operations that require row sorter update.
	 * 
	 * @author Pedro Henrique Oliveira dos Santos
	 * 
	 */
	private class TableModelAdapter extends Model
	{
		public TableModelAdapter(TableModel tableModel)
		{
			super((Serializable)tableModel);
			if (autoCreateRowSorter)
			{
				setRowSorter(new SerializableTableRowSorter(tableModel));
			}
		}

		@Override
		public void setObject(Serializable object)
		{
			super.setObject(object);
			if (autoCreateRowSorter)
			{
				setRowSorter(new SerializableTableRowSorter((TableModel)object));
			}
		}
	}

	/**
	 * @see {@link javax.swing.JTable#setAutoCreateRowSorter(boolean)}
	 */
	public void setAutoCreateRowSorter(boolean autoCreateRowSorter)
	{
		this.autoCreateRowSorter = autoCreateRowSorter;
		if (autoCreateRowSorter)
		{
			setRowSorter(new SerializableTableRowSorter(getTableModel()));
		}
	}

	/**
	 * @see {@link javax.swing.JTable#setRowSorter(RowSorter)}
	 */
	public void setRowSorter(RowSorter sorter)
	{
		this.rowsListView.setRowSorter(sorter);
	}

	public RowSorter getRowSorter()
	{
		return this.rowsListView.getRowSorter();
	}

	@Override
	public void renderHead(IHeaderResponse response)
	{
		response.renderCSSReference(getCss());
	}

	@Override
	protected void onComponentTag(ComponentTag tag)
	{
		super.onComponentTag(tag);
		tag.setName("table");
	}

	protected ResourceReference getCss()
	{
		return TABLE_CSS;
	}

	/**
	 * TODO: Consider to work with observer pattern.
	 * 
	 * @param newSelectionIndex
	 * @param target
	 */
	protected void onSelection(int newSelectionIndex, AjaxRequestTarget target)
	{
	}

}
