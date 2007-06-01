/*
 * $Id: org.eclipse.jdt.ui.prefs 5004 2006-03-17 20:47:08 -0800 (Fri, 17 Mar
 * 2006) eelco12 $ $Revision: 5004 $ $Date: 2006-03-17 20:47:08 -0800 (Fri, 17
 * Mar 2006) $
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.contrib.gmap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.Response;
import wicket.ajax.AbstractDefaultAjaxBehavior;
import wicket.ajax.AjaxRequestTarget;
import wicket.util.string.JavascriptUtils;

/**
 * @author syca
 */
public class ClickEventBehavior extends AbstractDefaultAjaxBehavior
{
	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(ClickEventBehavior.class);

	@Override
	protected void onRenderHeadInitContribution(Response response)
	{
		StringBuilder builder = new StringBuilder();
		builder.append("function initListener() {\n");
		builder.append("GEvent.addListener(googleMap, \"click\", function (marker, point) {\n\t");

		CharSequence callbackUrl = getCallbackUrl(true, false);
		builder.append("var callbackUrl = '").append(callbackUrl).append(
				"&x=' + point.x + '&y=' + point.y;\n");
		CharSequence script = getCallbackScript("wicketAjaxGet(callbackUrl", null, null);
		builder.append(script);

		builder.append("\n});\n");
		builder.append("}\n");
		JavascriptUtils.writeJavascript(response, builder.toString());
	}

	@Override
	protected String getImplementationId()
	{
		return "gmap-event-listener";
	}

	@Override
	protected void respond(AjaxRequestTarget target)
	{
		String x = getX();
		String y = getY();
		log.debug("executing onclick handler: " + "x=" + x + ",y=" + y);
		
		String msg = "x=" + x + ":y=" + y;
		target.appendJavascript("alert('" + msg + "');");
	}

	private String getY()
	{
		return getComponent().getRequest().getParameter("y");
	}

	private String getX()
	{
		return getComponent().getRequest().getParameter("x");
	}

	@Override
	protected void onBind()
	{
		getComponent().setOutputMarkupId(true);
	}
}
