package com.inmethod.grid.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

import com.inmethod.grid.IGridColumn;
import com.inmethod.grid.IRenderable;
import com.inmethod.grid.IGridSortState;

/**
 * Represents container of cell items in one row.
 * 
 * @author Matej Knopp
 */
public abstract class AbstractGridRow extends WebMarkupContainer {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * @param id
	 * @param model
	 */
	public AbstractGridRow(String id, IModel model) {
		super(id, model);
	}

	@Override
	protected void onBeforeRender() {
		// make sure that the child component match currently active columns

		Collection<IGridColumn> activeColumns = getActiveColumns();

		// remove unneeded components
		for (Iterator<?> i = iterator(); i.hasNext();) {
			Component component = (Component) i.next();
			if (isComponentNeeded(component.getId(), activeColumns) == false) {
				i.remove();
			}
		}
		List<Component> components = new ArrayList<Component>(activeColumns.size());

		// create components that might be needed
		for (IGridColumn column : activeColumns) {
			String componentId = componentId(column.getId());
			Component component = null;
			if (!column.isLightWeight(getModel()) && (component = get(componentId)) == null) {
				component = column.newCell(this, componentId, getModel());
				add(component);
			}
			if (component != null) {
				components.add(component);
			}
		}

		super.onBeforeRender();

		// delay adding the actual behaviors
		for (Component component : components) {
			for (IGridColumn column : activeColumns) {
				if (component.getId().equals(componentId(column.getId()))) {
					component.add(new InnerDivClassBehavior(column));
				}
			}
		}
	}

	/**
	 * Returns true if the column is currently being the one sorted with highest priority (i.e. the
	 * most recent one the user clicked)
	 * 
	 * @param column
	 * @return
	 */
	private boolean isColumnBeingSorted(IGridColumn column) {
		if (column.getSortProperty() != null) {
			AbstractGrid dataGrid = (AbstractGrid) findParent(AbstractGrid.class);
			IGridSortState sortState = dataGrid.getSortState();
			return (sortState.getColumns().size() > 0 && sortState.getColumns().get(0).getPropertyName().equals(
					column.getSortProperty()));
		} else {
			return false;
		}
	}

	/**
	 * Renders the table cell opening tag.
	 * 
	 * @param column
	 * @param i
	 *            column index in row
	 * @param columnsSize
	 * @param response
	 * @param hide
	 *            how many cells remain to be hidden (because some of the previous cells had colspan
	 *            set)
	 * @return
	 */
	private int renderOpenTag(IGridColumn column, int i, int columnsSize, Response response, int hide) {
		int originalColspan = column.getColSpan(getModel());
		int colspan = originalColspan;

		// render the opening tag
		if (hide > 0) {
			response.write("<td style=\"display:none\"");
			hide--;
		} else {
			response.write("<td");

			int max = columnsSize - i;
			if (colspan > max)
				colspan = max;
			if (colspan > 1) {
				response.write(" colspan=\"" + colspan + "\"");
				hide = colspan - 1;
			}
		}

		// render the css class

		StringBuilder css = new StringBuilder();

		css.append("imxt-cell");

		String klass = column.getCellCssClass(getModel(), getRowNumber());

		if (klass != null) {
			css.append(" ");
			css.append(klass);
		}

		// the original colspan is used when reordering columns by javascript
		if (originalColspan > 1) {
			css.append(" imxt-colspan-");
			css.append(originalColspan);
		}

		// flag indicatign that the column is being sorted
		if (isColumnBeingSorted(column)) {
			css.append(" imxt-sorted");
		}

		response.write(" class=\"" + css.toString() + "\"");

		response.write(">");

		return hide;
	}

	/**
	 * Behavior that sets the css class attribute to of the actual cell component. This behavior is
	 * temporary (only lasts during render) and it's re-added on every request in onBeforeRender.
	 * 
	 * @author Matej Knopp
	 */
	private class InnerDivClassBehavior extends AbstractBehavior {

		private static final long serialVersionUID = 1L;

		private final IGridColumn column;

		private InnerDivClassBehavior(IGridColumn column) {
			this.column = column;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void onComponentTag(Component component, ComponentTag tag) {
			CharSequence klass = tag.getString("class");
			if (klass == null)
				klass = "";
			if (klass.length() > 0)
				klass = klass + " ";
			klass = klass + getInnerDivClass(column);
			tag.put("class", klass);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isTemporary() {
			return true;
		}
	};

	/**
	 * Returns the css class of cell component (which is attached to the first div element inside
	 * the &lt;td&gt;)
	 * 
	 * @param column
	 * @return
	 */
	private String getInnerDivClass(IGridColumn column) {
		if (column.getWrapText()) {
			return "imxt-a imxt-wrap";
		} else {
			return "imxt-a imxt-nowrap";
		}
	}

	/**
	 * Renders the actual cell components (or {@link IRenderable} instances).
	 */
	@Override
	protected void onRender(MarkupStream markupStream) {

		final int markupStart = markupStream.getCurrentIndex();
		Response response = RequestCycle.get().getResponse();

		boolean rendered = false;
		Collection<IGridColumn> columns = getActiveColumns();

		int hide = 0;
		int i = 0;
		for (IGridColumn column : columns) {
			hide = renderOpenTag(column, i, columns.size(), response, hide);
			
			if (column.isLightWeight(getModel())) {
				// for lightweight columns get the renderable instance and render it
				IRenderable renderable = column.newCell(getModel());
				if (renderable == null) {
					throw new IllegalStateException(
							"Lightweight columns must return valid IRenderable instance in newCell(IModel model)");
				}
				response.write("<div class=\"");
				response.write(getInnerDivClass(column));
				response.write("\">");
				renderable.render(getModel(), response);
				response.write("</div>");
			} else {
				// for non-lightweight components get the actual component and render it
				Component component = get(column.getId());
				if (component == null) {
					throw new IllegalStateException("Column ID has changed during rendering");
				}
				markupStream.setCurrentIndex(markupStart);
				component.render(markupStream);
				rendered = true;
			}

			response.write("</td>");
			++i;
		}
		;

		// if no component was rendered just advance in the markup stream
		if (rendered == false) {
			markupStream.skipComponent();
		}

	}

	private String componentId(String columnId) {
		return columnId;
	}

	protected abstract Collection<IGridColumn> getActiveColumns();

	protected abstract int getRowNumber();

	/**
	 * Returns whether a component with given is needed for any of the columns.
	 * @param componentId
	 * @param activeColumns
	 * @return
	 */
	private boolean isComponentNeeded(final String componentId, Collection<IGridColumn> activeColumns) {
		for (IGridColumn column : activeColumns) {
			if (componentId(column.getId()).equals(componentId) && !column.isLightWeight(getModel())) {
				return true;
			}
		}
		return false;
	}
}
