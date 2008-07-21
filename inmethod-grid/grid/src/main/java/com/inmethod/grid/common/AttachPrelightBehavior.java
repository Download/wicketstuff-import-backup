/**
 * 
 */
package com.inmethod.grid.common;

import org.apache.wicket.Component;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.util.string.JavascriptUtils;

/**
 * When a single item is (rendered using Ajax this behavior makes sure that it has the prelight
 * events attached.
 * 
 * @author Matej Knopp
 */
public final class AttachPrelightBehavior extends AbstractBehavior {
	private static final long serialVersionUID = 1L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onRendered(Component component) {
		WebRequest request = (WebRequest) RequestCycle.get().getRequest();
		Response response = RequestCycle.get().getResponse();
		AbstractGrid grid = (AbstractGrid) component.findParent(AbstractGrid.class);
		if (request.isAjax() && !grid.isRendering()) {
			JavascriptUtils.writeOpenTag(response);
			response.write("var e = Wicket.$('" + component.getMarkupId() + "');");
			response.write("var id = '" + grid.getMarkupId() + "';");
			response.write("var m = InMethod.XTableManager.instance;");
			response.write("m.updateRow(id, e);");
			JavascriptUtils.writeCloseTag(response);
		}
	}
}