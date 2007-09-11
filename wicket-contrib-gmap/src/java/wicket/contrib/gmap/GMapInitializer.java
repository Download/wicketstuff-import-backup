package wicket.contrib.gmap;

import java.util.Iterator;

import wicket.Component;
import wicket.Response;
import wicket.behavior.AbstractAjaxBehavior;
import wicket.markup.html.WebPage;
import wicket.util.string.JavascriptUtils;

/**
 * @author Iulian-Corneliu Costan
 */
class GMapInitializer extends AbstractAjaxBehavior
{
	private static final long serialVersionUID = 1L;

	private GMap gmap;

	/**
	 * Construct.
	 * 
	 * @param gmap
	 */
	public GMapInitializer(GMap gmap)
	{
		this.gmap = gmap;
	}

	protected void onRenderHeadInitContribution(Response response)
	{
		String initScript = getInitScript();
		String refreshScript = getRefreshScript();
		String gmapRequest = getGMapRequestFunction();
		JavascriptUtils.writeJavascript(response, initScript + gmapRequest, "gmap-initializer");
	}

	protected String getImplementationId()
	{
		return "gmap-initializer";
	}

	/**
	 * @see wicket.behavior.IBehaviorListener#onRequest()
	 */
	public void onRequest()
	{
		// nop
	}

	private String getGMapRequestFunction()
	{
		StringBuffer s = new StringBuffer();
		s.append("\tfunction gmapRequest(componentUrl, componentId) { \n");
		s.append("\t\tfunction success() {\n");
		s.append("\t\t\tvar srcComp = wicketGet(componentId);\n");
		s.append("\t\t\tvar dstComp = wicketGet('dst'+componentId);\n");
		s.append("\t\t\tdstComp.innerHTML = srcComp.innerHTML;\n");
		// s.append("\t\t\tsrcComp.style.display = \"none\";\n");
		s.append("\t\t}\n");
		s.append("\t\tfunction failure() {\n");
		s.append("\t\t\talert('ooops!');\n");
		s.append("\t\t}\n");
		s.append("\t\twicketAjaxGet(componentUrl, success, failure)\n");
		s.append("\n\t}\n");
		return s.toString();
	}

	/**
	 * @return gmap init script
	 */
	public String getInitScript()
	{
		StringBuffer buffer = new StringBuffer("var googleMap = null;\nvar openMarker = null;\n")
				.append("\nfunction initGMap() {\n").append("if (GBrowserIsCompatible()) {\n")
				.append("\n" + gmapDefinition()).append("\n").append("}\n").append("}\n");
		return buffer.toString();
	}

	private String gmapDefinition()
	{
		StringBuffer buffer = new StringBuffer();
		buffer
				.append("googleMap = googleMap ? googleMap : new GMap2(document.getElementById(\"map\"));\n");
		if (gmap.isLargeMapControl())
		{
			buffer.append("var gLargeMapControl=new GLargeMapControl();\n");
			buffer.append("googleMap.addControl(gLargeMapControl);\n");
		}
		if (gmap.isTypeControl())
		{
			buffer.append("googleMap.addControl(new GMapTypeControl());\n");
		}
		if (gmap.isScaleControl())
		{
			buffer.append("googleMap.addControl(new GScaleControl());\n");
		}
		if (gmap.isOverviewMapControl())
		{
			buffer.append("googleMap.addControl(new GOverviewMapControl());\n");
		}
		if (gmap.isSmallMapControl())
		{
			buffer.append("googleMap.addControl(new GSmallMapControl());\n");
		}
		if (gmap.isSmallZoomControl())
		{
			buffer.append("googleMap.addControl(new GSmallZoomControl());\n");
		}
		buffer.append("googleMap.setCenter(").append(gmap.getCenter().toString()).append(", ")
				.append(gmap.getZoomLevel()).append(");\n\n");
		// Below registers when the user stoped moving the map, need to update
		// some values, center and bounds
		// needs to be dragend otherwise infoboxes will be closed on map move
		// applied fix for closing of infoboxes when using dragend
		// dragend doesnt work nicely, movend does, not sure why.
		buffer.append("GEvent.addListener(googleMap, \"moveend\", function () {\n"
				+ "var center = googleMap.getCenter();\n"
				+ "var sW = googleMap.getBounds().getSouthWest();\n"
				+ "var nE = googleMap.getBounds().getNorthEast();\n"
				// set center
				+ "document.getElementById(\"latitudeCenter\").value=center.lat();\n"
				+ "document.getElementById(\"longitudeCenter\").value=center.lng();\n"
				// set SW bound
				+ "document.getElementById(\"latitudeSW\").value=sW.lat();\n"
				+ "document.getElementById(\"longitudeSW\").value=sW.lng();\n"
				// set NE bound
				+ "document.getElementById(\"latitudeNE\").value=nE.lat();\n"
				+ "document.getElementById(\"longitudeNE\").value=nE.lng();\n"

				+ "document.getElementById(\"zoomLevel\").value=googleMap.getZoom();\n"
				+ "document.getElementById(\"gmap_ajaxGMapUpdatingFormSubmit\").onclick();\n"
				+ "});\n");
		// if gmap is in insert model this must be added for the notifier form
		// to be submitted on click
		if (gmap.isInsertMode())
		{
			buffer
					.append("GEvent.addListener(googleMap, \"click\", function (marker, point) {\n"
							+ "if(marker){"
							+ "updateOpenInfoWindow(marker,'markerset!');"
							+ "}\n"
							+ "else{\n"
							+ "document.getElementById(\"clickNotifierLatitude\").value=point.lat();\n"
							+ "document.getElementById(\"clickNotifierLongitude\").value=point.lng();\n"
							+ "document.getElementById(\"" + getComponentPrefix() + "gmap_ajaxGMapUpdatingFormSubmit\").onclick();\n"
							+ "}});\n");
		}
		else
		{
			buffer.append("GEvent.addListener(googleMap, \"click\", function (marker, point) {\n"
					+ "if(marker){" + "updateOpenInfoWindow(marker,'markerset!');" + "}\n"
					+ "else{\n" + "}});\n");

		}


		return buffer.toString();
	}

	private String getRefreshScript()
	{
		// trying to split up function by declaring map as a page variable
		// instead of function variable
		StringBuffer buffer = new StringBuffer("\n")
				.append("\nfunction " + GMapComponentUpdate.REFRESH_FUNCTION + " {\n")
				.append("if (window.googleMap!=null) {\n")
				.append("\n" + gmapDefinitionUpdate())
				.append("\n" + overlayDefinitions())
				.append(
						"}\n else{alert('The Map cannot be shown currently due to a technical error, please try again later!');}")
				.append("}\n");
		return buffer.toString();
	}


	private String getComponentPrefix()
	{
		StringBuffer b = new StringBuffer();

		Component c = (Component)getComponent();
		if (c == null)
		{
			throw new UnsupportedOperationException("getComponent should not return null");
		}

		while (c.getParent() != null)
		{
			c = c.getParent();
			if (c instanceof WebPage)
			{
				c = null;
				break;
			}

			b.insert(0, c.getId() + "_");

		}

		return b.toString();
	}


	private String overlayDefinitions()
	{
		StringBuffer buffer = new StringBuffer("googleMap.clearOverlays();\n");
		Iterator iterator = gmap.getOverlays().iterator();
		while (iterator.hasNext())
		{
			Overlay overlay = (Overlay)iterator.next();
			buffer.append("googleMap.addOverlay(" + overlay.getFactoryMethod() + "());\n");
		}
		return buffer.toString();
	}

	/**
	 * updates the Gmap
	 * 
	 * @return gives the gmap update
	 */
	private String gmapDefinitionUpdate()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("googleMap.panTo(").append(gmap.getCenter().toString()).append(");\n");

		return buffer.toString();
	}


	public static final String ID = "gmapComponentInit";
}
