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
package org.wicketstuff.table.cell;

import javax.swing.table.TableModel;

import org.apache.wicket.model.IModel;

/**
 * Model responsible to receive and update an value from a table model position.
 * 
 * @author Pedro Henrique Oliveira dos Santos
 * 
 */
public class TableCellModel implements IModel
{
	private TableModel tableModel;
	private int rowIndex;
	private int columnIndex;

	public TableCellModel(TableModel tableModel, int rowIndex, int columnIndex)
	{
		this.tableModel = tableModel;
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
	}

	public Object getObject()
	{
		return tableModel.getValueAt(rowIndex, columnIndex);
	}

	public void setObject(Object object)
	{
		tableModel.setValueAt(object, rowIndex, columnIndex);
	}

	public void detach()
	{

	}

}
