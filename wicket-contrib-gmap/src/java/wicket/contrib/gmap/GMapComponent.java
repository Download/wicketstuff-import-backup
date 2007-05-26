package wicket.contrib.gmap;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Iulian-Corneliu Costan
 */
class GMapComponent extends JavaScriptComponent
{
	private static final long serialVersionUID = -3145330681596539743L;
	private static final Log log = LogFactory.getLog(GMapComponent.class);
	
	private GMap gmap;

	public GMapComponent(GMap gmap)
	{
		super(ID);
		this.gmap = gmap;
	}

	public String onJavaScriptComponentTagBody()
	{
		StringBuffer buffer = new StringBuffer("\n//<![CDATA[\n").append("function initGMap() {\n")
				.append("if (GBrowserIsCompatible()) {\n").append("\n" + gmapDefinition()).append(
						"\n" + overlayDefinitions()).append("}\n").append("}\n").append("//]]>\n");
		return buffer.toString();
	}

	private String overlayDefinitions()
	{
		StringBuffer buffer = new StringBuffer("map.clearOverlays();\n");
		Iterator iterator = gmap.getOverlays().iterator();
		while (iterator.hasNext())
		{
			Overlay overlay = (Overlay)iterator.next();
			buffer.append("map.addOverlay(" + overlay.getFactoryMethod() + "());\n");
		}
		return buffer.toString();
	}

	private String gmapDefinition()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("var map = map ? map : new GMap2(document.getElementById(\"map\"));\n");
		if (gmap.isLargeMapControl())
		{
			buffer.append("map.addControl(new GLargeMapControl());\n");
		}
		if (gmap.isTypeControl())
		{
			buffer.append("map.addControl(new GMapTypeControl());\n");
		}
		if (gmap.isScaleControl())
		{
			buffer.append("map.addControl(new GScaleControl());\n");
		}
		if (gmap.isOverviewMapControl())
		{
			buffer.append("map.addControl(new GOverviewMapControl());\n");
		}
		if (gmap.isSmallMapControl())
		{
			buffer.append("map.addControl(new GSmallMapControl());\n");
		}
		if (gmap.isSmallZoomControl())
		{
			buffer.append("map.addControl(new GSmallZoomControl());\n");
		}
		buffer.append("map.setCenter(").append(gmap.getCenter().toString()).append(", ").append(
				gmap.getZoomLevel()).append(");\n");
		// Below registers when the user stoped moving the map, need to update
		// some values, center and bounds
		buffer.append("GEvent.addListener(map, \"moveend\", function () {\n"
				+ "var center = map.getCenter();\n"
				+ "var sW = map.getBounds().getSouthWest();\n"
				+ "var nE = map.getBounds().getNorthEast();\n"
				// set center
				+ "document.getElementById(\"latitudeCenter\").value=center.lat();\n"
				+ "document.getElementById(\"longtitudeCenter\").value=center.lng();\n"
				// set SW bound
				+ "document.getElementById(\"latitudeSW\").value=sW.lat();\n"
				+ "document.getElementById(\"longtitudeSW\").value=sW.lng();\n"
				// set NE bound
				+ "document.getElementById(\"latitudeNE\").value=nE.lat();\n"
				+ "document.getElementById(\"longtitudeNE\").value=nE.lng();\n"

				+ "document.getElementById(\"zoomLevel\").value=map.getZoom();\n"
				+ "document.getElementById(\"gmap_ajaxGMapUpdatingFormSubmit\").onclick();\n" +

				"});\n");
		// Listener for zoom
		buffer.append("GEvent.addListener(map, \"zoomend\", function (oldZoom, newZoom) {\n"
				+ "var center = map.getCenter();\n"
				+ "var sW = map.getBounds().getSouthWest();\n"
				+ "var nE = map.getBounds().getNorthEast();\n"
				// set center
				+ "document.getElementById(\"latitudeCenter\").value=center.lat();\n"
				+ "document.getElementById(\"longtitudeCenter\").value=center.lng();\n"
				// set SW bound
				+ "document.getElementById(\"latitudeSW\").value=sW.lat();\n"
				+ "document.getElementById(\"longtitudeSW\").value=sW.lng();\n"
				// set NE bound
				+ "document.getElementById(\"latitudeNE\").value=nE.lat();\n"
				+ "document.getElementById(\"longtitudeNE\").value=nE.lng();\n"

				+ "document.getElementById(\"zoomLevel\").value=map.getZoom();\n"
				+ "document.getElementById(\"gmap_ajaxGMapUpdatingFormSubmit\").onclick();\n" +

				"});\n");


		// is gmap is in insert model this must be added for the notifier form
		// to be submitted on click
		if (gmap.isInsertMode())
		{
			buffer
					.append("GEvent.addListener(map, \"click\", function (marker, point) {\n"
							+ "if(marker){}\n"
							+ "else{\n"
							+ "document.getElementById(\"clickNotifierLatitude\").value=point.lat();\n"
							+ "document.getElementById(\"clickNotifierLongtitude\").value=point.lng();\n"
							+ "document.getElementById(\"gmap_ajaxGMapClickNotifierFormSubmit\").onclick();\n"
							+ "}});\n");
		}

		log.debug("ZoomLevel are:" + gmap.getZoomLevel());
		// problems with updating the zoomlevel trying to force it!
		// buffer.append("map.setZoom(" + gmap.getZoomLevel() + ");\n");
		// buffer.append("alert('zoomLevel are='+map.getZoom()+', should have
		// been "
		// + gmap.getZoomLevel() + "');\n");

		return buffer.toString();
	}

	public static final String ID = "gmapComponent";
}
