package wicket.contrib.gmap;

import java.util.Iterator;

/**
 * @author Nino Martinez Wael
 */
class GMapComponentUpdate extends JavaScriptComponent
{
	private static final long serialVersionUID = -3145330681596539743L;
	private final String functionName="updateGMap()"; 

	private GMap gmap;

	public GMapComponentUpdate(GMap gmap)
	{
		super(ID);
		this.gmap = gmap;
	}

	public String onJavaScriptComponentTagBody()
	{
		// trying to split up function by declaring map as a page variable
		// instead of function variable
		StringBuffer buffer = new StringBuffer("\n//<![CDATA[\n").append(
				"\nfunction "+ functionName +" {\n").append(
				"if (map!=null) {\n").append("\n" + gmapDefinitionUpdate()).append(
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

	/**
	 * updates the Gmap
	 * 
	 * @return gives the gmap update
	 */
	private String gmapDefinitionUpdate()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("map.setCenter(").append(gmap.getCenter().toString()).append(", ").append(
				gmap.getZoomLevel()).append(");\n");

		return buffer.toString();
	}

	public static final String ID = "gmapComponentUpdate";

	public String getFunctionName()
	{
		return functionName;
	}
}
