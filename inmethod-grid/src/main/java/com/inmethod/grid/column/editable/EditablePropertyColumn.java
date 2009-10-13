package com.inmethod.grid.column.editable;


import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.inmethod.grid.column.PropertyColumn;

/**
 * Property column that uses a {@link TextFieldPanel} as cell component when the item is selected.
 * @author Matej Knopp
 *
 */
public class EditablePropertyColumn<T extends Serializable, H extends Serializable, P> extends PropertyColumn<T, H, P> {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param columnId
	 *            column identified (must be unique within the grid)
	 * @param headerModel
	 *            model for column header
	 * @param propertyExpression
	 *            property expression used to get the displayed value for row object
	 * @param sortProperty
	 *            optional string that will be returned by {@link ISortState} to indicate that the
	 *            column is being sorted
	 */
	public EditablePropertyColumn(String columnId, IModel<H> headerModel, String propertyExpression, String sortProperty) {
		super(columnId, headerModel, propertyExpression, sortProperty);
	}

	/**
	 * Constructor.
	 * 
	 * @param columnId
	 *            column identified (must be unique within the grid)
	 * @param headerModel
	 *            model for column header
	 * @param propertyExpression
	 *            property expression used to get the displayed value for row object
	 */
	public EditablePropertyColumn(String columnId, IModel<H> headerModel, String propertyExpression) {
		super(columnId, headerModel, propertyExpression);
	}

	/**
	 * Constructor. The column id is omitted in this constructor, because the property expression is
	 * used as column id.
	 * 
	 * @param headerModel
	 *            model for column header
	 * @param propertyExpression
	 *            property expression used to get the displayed value for row object
	 * @param sortProperty
	 *            optional string that will be returned by {@link ISortState} to indicate that the
	 *            column is being sorted
	 */
	public EditablePropertyColumn(IModel<H> headerModel, String propertyExpression, String sortProperty) {
		super(headerModel, propertyExpression, sortProperty);
	}

	/**
	 * Constructor. The column id is omitted in this constructor, because the property expression is
	 * used as column id.
	 * 
	 * @param headerModel
	 *            model for column header
	 * @param propertyExpression
	 *            property expression used to get the displayed value for row object
	 */
	public EditablePropertyColumn(IModel<H> headerModel, String propertyExpression) {
		super(headerModel, propertyExpression);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLightWeight(IModel<T> rowModel) {
		// when the item is selected, we need a textField component, thus
		// this method has to return false.
		return !getGrid().isItemEdited(rowModel);
	}
	
	protected IModel<P> getFieldModel(IModel<T> rowModel) {
		return new PropertyModel<P>(rowModel, getPropertyExpression());
	}
	
	protected EditableCellPanel newCellPanel(String componentId, IModel<T> rowModel, IModel<P> cellModel) {
		return new TextFieldPanel(componentId, cellModel, rowModel, this);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component newCell(WebMarkupContainer parent, String componentId, IModel<T> rowModel) {
		// when this method is called, it means that the row is not lightweight, which in turn
		// means the the row is selected (see implementation of #isLightWeight(IModel).
		
		EditableCellPanel panel = newCellPanel(componentId, rowModel, getFieldModel(rowModel));
		addValidators(panel.getEditComponent());
		return panel;
		
		// for lightweight rows (that are not selected) the lightweight #newCell(IModel) method
		// is called that only displays the item 
	}
	
	protected void addValidators(FormComponent component) {
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCellCssClass(IModel<T> rowModel, int rowNum) {
		String prelight = isClickToEdit() ? "imxt-want-prelight" : "";
		// for selected items we don't want the default cell item padding because we need
		// to put a textfield in it, so the special "imxt-no-padding-cell" CSS class is used. 
		if (getGrid().isItemEdited(rowModel)) {
			return "imxt-no-padding-cell " + prelight + " imxt-edited-cell";
		} else {
			return prelight;
		}
	}
	
	@Override
	public boolean cellClicked(IModel<T> rowModel) {
		if (!isClickToEdit() || (getGrid().isClickRowToSelect() && getGrid().isSelectToEdit())) {
			return false;
		} else {
			getGrid().setItemEdit(rowModel, true);
			getGrid().update();
			return true;
		}
	}
	
	protected boolean isClickToEdit() {
		return true;
	}
}
