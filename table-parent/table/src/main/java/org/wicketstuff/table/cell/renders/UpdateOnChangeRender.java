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
package org.wicketstuff.table.cell.renders;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.model.IModel;
import org.wicketstuff.table.SelectableListItem;
import org.wicketstuff.table.cell.CellEditor;
import org.wicketstuff.table.cell.CellRender;

/**
 * Wrapper class for add AjaxFormComponentUpdatingBehavior to forms components
 * for the onchange event
 * 
 * @author Pedro Henrique Oliveira dos Santos
 * 
 */
public class UpdateOnChangeRender implements CellRender, CellEditor
{
	private CellRender cellRender;
	private CellEditor cellEditor;

	public UpdateOnChangeRender(CellRender cellRender, CellEditor cellEditor)
	{
		this.cellRender = cellRender;
		this.cellEditor = cellEditor;
	}

	public UpdateOnChangeRender(CellRender cellRender)
	{
		this.cellRender = cellRender;
		if (cellRender instanceof CellEditor)
		{
			this.cellEditor = (CellEditor)cellRender;
		}
	}


	@Override
	public Component getRenderComponent(String id, IModel model, SelectableListItem parent,
			int row, int column)
	{
		return cellRender.getRenderComponent(id, model, parent, row, column);
	}

	@Override
	public Component getEditorComponent(String id, IModel model, SelectableListItem parent,
			int row, int column)
	{
		return cellEditor.getEditorComponent(id, model, parent, row, column).add(
				new AjaxFormComponentUpdatingBehavior("onchange")
				{
					protected void onUpdate(AjaxRequestTarget target)
					{
						UpdateOnChangeRender.this.onUpdate(target);
					}

					@Override
					protected void onError(AjaxRequestTarget target, RuntimeException e)
					{
						UpdateOnChangeRender.this.onError(target, e);
					}
				});
	}

	protected void onError(AjaxRequestTarget target, RuntimeException e)
	{
	}

	protected void onUpdate(AjaxRequestTarget target)
	{
	}

}
