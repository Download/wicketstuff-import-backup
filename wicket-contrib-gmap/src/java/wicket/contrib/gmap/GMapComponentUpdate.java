package wicket.contrib.gmap;

import java.util.Iterator;

/**
 * @author Nino Martinez Wael
 */
class GMapComponentUpdate extends JavaScriptComponentIEFix
{
	private static final long serialVersionUID = -3145330681596539743L;

	/**
	 * the name of the gmap refresh function
	 */
	public static final String REFRESH_FUNCTION = "refreshGMap()";
	public static final String OPENINFOWINDOW_FUNCTION = "reOpenInfo()";

	private GMap gmap;

	private boolean openInfoOverlay=false;


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
		StringBuffer buffer = new StringBuffer("\n//<![CDATA[\n")
				.append("\nfunction " + GMapComponentUpdate.REFRESH_FUNCTION + " {\n")
				.append("if (window.googleMap!=null) {\n")
				.append("\n" + gmapDefinitionUpdate())
				.append("\n" + overlayDefinitions())
				.append(
						"}\n else{alert('The Map cannot be shown currently due to a technical error, please try again later!');}")
				.append("}\n").append("//]]>\n");
		return buffer.toString();
	}

	private String overlayDefinitions()
	{
		StringBuffer buffer = new StringBuffer("googleMap.clearOverlays();\n");
		Iterator iterator = gmap.getOverlays().iterator();

		openInfoOverlay = false;
		String openOverlayInfo = "";
		if (gmap.getOpenMarkerInfoWindow() != null)
		{
			openOverlayInfo = gmap.getOpenMarkerInfoWindow().replaceAll("createInfo", "");
		}
		while (iterator.hasNext())
		{
			Overlay overlay = (Overlay)iterator.next();
			buffer.append("googleMap.addOverlay(" + overlay.getFactoryMethod() + "());\n");

			// Below trying to fix the closing of open info windows when moveend
			// triggered by centering on the marker
			if (overlay.getOverlayId().compareTo(openOverlayInfo) == 0)
			{
				openInfoOverlay = true;
			}
		}

		return buffer.toString();
	}

	private String gmapDefinitionUpdate()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("googleMap.panTo(").append(gmap.getCenter().toString()).append(");\n");

		return buffer.toString();
	}

	public static final String ID = "gmapComponentUpdate";


	public boolean isOpenInfoOverlay()
	{
		return openInfoOverlay;
	}
}
