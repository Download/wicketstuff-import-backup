package com.inmethod.grid;

import java.io.Serializable;

import org.apache.wicket.Response;
import org.apache.wicket.model.IModel;

/**
 * Lightweight columns return an implementation of this interface to render cell output.
 * 
 * @see IGridColumn#isLightWeight(IModel)
 * @author Matej Knopp
 */
public interface IRenderable<T extends Serializable> {

	/**
	 * Renders the output for given cell model. The implementation must take care of proper escaping
	 * (e.g. translating &lt; to &amp;lt;, etc.) where appropriate.
	 * 
	 * @param rowModel
	 *            model for given row
	 * @param response
	 */
	public void render(IModel<T> rowModel, Response response);

}
