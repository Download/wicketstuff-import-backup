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
package wicketstuff.crud.filter;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.NoFilter;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;


/**
 * Toolbar that is used to filter on crud panel properties
 * 
 * @author Igor Vaynberg (ivaynber)
 * 
 */
public class FilterToolbar extends AbstractToolbar
{
	private static final long serialVersionUID = 1L;
	private static final String FILTER_COMPONENT_ID = "filter";

	public FilterToolbar(final DataTable table, IModel model)
	{
		super(table);
		setModel(model);

		if (table == null)
		{
			throw new IllegalArgumentException("argument [table] cannot be null");
		}

		// populate the toolbar with components provided by filtered columns
		RepeatingView filters = new RepeatingView("filters");
		filters.setRenderBodyOnly(true);
		add(filters);

		IColumn[] cols = table.getColumns();
		for (int i = 0; i < cols.length; i++)
		{
			WebMarkupContainer item = new WebMarkupContainer(filters.newChildId());
			item.setRenderBodyOnly(true);

			IColumn col = cols[i];
			Component filter = null;

			if (col instanceof IFilterableColumn)
			{
				IFilterableColumn filteredCol = (IFilterableColumn)col;
				filter = filteredCol.getFilter(FILTER_COMPONENT_ID, model);
			}

			if (filter == null)
			{
				filter = new NoFilter(FILTER_COMPONENT_ID);
			}
			else
			{
				if (!filter.getId().equals(FILTER_COMPONENT_ID))
				{
					throw new IllegalStateException(
							"filter component returned  with an invalid component id. invalid component id ["
									+ filter.getId() + "] required component id ["
									+ FILTER_COMPONENT_ID + "] generating column ["
									+ col.toString() + "] ");
				}
			}

			item.add(filter);
			filters.add(item);
		}

	}

	protected void onApplyFilter()
	{
		getTable().setCurrentPage(0);
	}

}