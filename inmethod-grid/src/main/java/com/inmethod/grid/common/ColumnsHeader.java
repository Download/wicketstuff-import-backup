package com.inmethod.grid.common;

import java.io.Serializable;
import java.util.Collection;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;

import com.inmethod.grid.IGridColumn;

/**
 * Panel that provides the markup for {@link ColumnsHeaderRepeater}.
 * 
 * @see ColumnsHeaderRepeater  
 * @author Matej Knopp
 */
public abstract class ColumnsHeader<T extends Serializable> extends Panel {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * @param id
	 */
	public ColumnsHeader(String id) {
		super(id);

		setRenderBodyOnly(true);
		
		add(new ColumnsHeaderRepeater<T>("header") {

			private static final long serialVersionUID = 1L;

			@Override
			Collection<IGridColumn<T>> getActiveColumns() {
				return ColumnsHeader.this.getActiveColumns();
			}

			@Override
			int getColumnWidth(IGridColumn<T> column) {
				return ColumnsHeader.this.getColumnWidth(column);
			}
			
			@Override
			protected void sortStateChanged(AjaxRequestTarget target) {
				ColumnsHeader.this.sortStateChanged(target);
			}
			
		});
	}
	
	abstract protected void sortStateChanged(AjaxRequestTarget target);

	abstract Collection<IGridColumn<T>> getActiveColumns();
	
	abstract int getColumnWidth(IGridColumn<T> column);

}
