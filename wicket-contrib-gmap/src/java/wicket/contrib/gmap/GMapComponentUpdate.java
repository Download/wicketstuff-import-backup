package wicket.contrib.gmap;

import java.util.Iterator;

/**
 * @author Nino Martinez Wael
 */
class GMapComponentUpdate extends JavaScriptComponent
{
	private static final long serialVersionUID = -3145330681596539743L;

	/**
	 * the name of the gmap refresh function
	 */
	public static final String REFRESH_FUNCTION = "refreshGMap()";

	private GMap gmap;


	/**
	 * Construct.
	 * 
	 * @param gmap
	 */
	public GMapComponentUpdate(GMap gmap)
	{
		super(ID);
		setOutputMarkupId(true);
		this.gmap = gmap;
	}

	/**
	 * @see wicket.contrib.gmap.JavaScriptComponent#onJavaScriptComponentTagBody()
	 */
	public String onJavaScriptComponentTagBody()
	{
		// trying to split up function by declaring map as a page variable
		// instead of function variable
		StringBuffer buffer = new StringBuffer("\n//<![CDATA[\n").append(
				"\nfunction " + GMapComponentUpdate.REFRESH_FUNCTION + " {\n").append(
				"if (window.googleMap!=null) {\n").append("\n" + gmapDefinitionUpdate()).append(
				"\n" + overlayDefinitions()).append("}\n else{alert('map was null!');}").append(
				"}\n").append("//]]>\n");
		return buffer.toString();
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
		// trying to force redraw
		// buffer.append("googleMap.checkResize();\n");
		return buffer.toString();
	}

	private String gmapDefinitionUpdate()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("googleMap.panTo(").append(gmap.getCenter().toString()).append(");\n");

		return buffer.toString();
	}

	public static final String ID = "gmapComponentUpdate";
}
