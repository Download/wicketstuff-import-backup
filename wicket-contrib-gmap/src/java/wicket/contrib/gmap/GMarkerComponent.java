package wicket.contrib.gmap;

import wicket.behavior.AbstractAjaxBehavior;
import wicket.markup.html.PackageResourceReference;
import wicket.util.string.JavascriptUtils;

/**
 * Wicket component for Google's GMarker API. It generates GMarker's JavaScript
 * logic (event handling, info window) that will initiate an AJAX request when
 * gmarker is clicked.
 * 
 * @author Iulian-Corneliu Costan
 */
class GMarkerComponent extends JavaScriptComponent
{
	private static final long serialVersionUID = 1L;
	private AbstractAjaxBehavior behavior;
	private GMarker gmarker;

	/**
	 * thanks to <a href="http://mentalized.net/activity-indicators/">these guys</a>
	 * for so nice indicators.
	 */
	public static final String INDICATOR = "indicator.white.gif";

	private static final PackageResourceReference ref = new PackageResourceReference(
			GMapPanel.class, INDICATOR);

	/**
	 * Construct.
	 * 
	 * @param gmarker
	 * @param behavior
	 */
	public GMarkerComponent(GMarker gmarker, AbstractAjaxBehavior behavior)
	{
		super(ID);
		this.gmarker = gmarker;
		this.behavior = behavior;
	}

	/**
	 * @see wicket.contrib.gmap.JavaScriptComponent#onJavaScriptComponentTagBody()
	 */
	public String onJavaScriptComponentTagBody()
	{
		StringBuffer buffer = new StringBuffer("\n");
		if (gmarker.getIcon() != null)
		{
			buffer.append(createMarkerCustomIcon() + "\n");
		}
		else
		{
			buffer.append(createMarkerDefaultIcon() + "\n");
		}
		buffer.append(createInfoFunction() + "\n");
		return buffer.toString();
	}

	private String createMarkerCustomIcon()
	{
		CharSequence tooltip = JavascriptUtils.escapeQuotes(gmarker.getToolTip());
		GIcon icon = gmarker.getIcon();
		String customIcon = "var icon = new GIcon();\n" + "icon.image = \"" + icon.getImage()
				+ "\";\n" + "icon.shadow = \"" + icon.getShadow() + "\";\n" + "icon.iconSize = "
				+ icon.getIconSize().toString() + ";\n" + "icon.shadowSize = "
				+ icon.getShadowSize().toString() + ";\n" + "icon.iconAnchor = "
				+ icon.getAnchor().toString() + ";\n" + "icon.infoWindowAnchor = "
				+ icon.getInfoWindowAncor().toString() + "\n";
		String customIconPartTwo = "";
		if (tooltip != null && tooltip.length() > 0)
		{
			customIconPartTwo = "var marker = new GMarker(" + gmarker.getPointAsString()
					+ ",{icon:icon, title:'" + tooltip + "'});" + "\n" + getOnClickHandler() + "\n"
					+ "GEvent.addListener(marker, \"click\", onClick);" + "\n" + "return marker;";
		}
		else
		{
			customIconPartTwo = "var marker = new GMarker(" + gmarker.getPointAsString()
					+ ",icon);" + "\n" + getOnClickHandler() + "\n"
					+ "GEvent.addListener(marker, \"click\", onClick);" + "\n" + "return marker;";

		}

		return JSUtil.createFunction(gmarker.getFactoryMethod(), customIcon + customIconPartTwo);
	}

	private String createMarkerDefaultIcon()
	{
		CharSequence tooltip = JavascriptUtils.escapeQuotes(gmarker.getToolTip());
		if (tooltip.length() > 0)
		{
			return JSUtil.createFunction(gmarker.getFactoryMethod(), "var marker = new GMarker("
					+ gmarker.getPointAsString() + ",{title:'" + tooltip + "'});" + "\n"
					+ getOnClickHandler() + "\n"
					+ "GEvent.addListener(marker, \"click\", onClick);" + "\n" + "return marker;");

		}

		return JSUtil.createFunction(gmarker.getFactoryMethod(), "var marker = new GMarker("
				+ gmarker.getPointAsString() + ");" + "\n" + getOnClickHandler() + "\n"
				+ "GEvent.addListener(marker, \"click\", onClick);" + "\n" + "return marker;");
	}

	private String getOnClickHandler()
	{
		return JSUtil.createFunction("onClick", "marker.openInfoWindow(" + getInfoFactoryName()
				+ "());");
	}

	private String createInfoFunction()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("var div = document.createElement(\"div\");\n");
		buffer.append("div.id = \"dst" + gmarker.getOverlayId() + "\";\n");
		buffer.append("var progress = document.createElement(\"img\");\n");
		buffer.append("progress.src = \"" + getPage().urlFor(ref) + "\";\n");
		buffer.append("var waitText = document.createTextNode(\"Please wait ...\");\n");
		buffer.append("var nobr = document.createElement(\"nobr\");\n");
		buffer.append("nobr.appendChild(progress);\n");
		buffer.append("nobr.appendChild(waitText);\n");
		buffer.append("div.appendChild(nobr);\n");
		buffer.append("var script = document.createElement(\"script\");\n");
		buffer.append("var js = document.createTextNode(\"");
		buffer.append("gmapRequest('" + behavior.getCallbackUrl() + "', '" + gmarker.getOverlayId()
				+ "');\")\n");
		buffer.append("script.appendChild(js);\n");
		buffer.append("div.appendChild(script);\n");
		buffer.append("return div;");
		return JSUtil.createFunction(getInfoFactoryName(), buffer.toString());
	}

	// todo implement empty
	private String createEmptyMarker()
	{
		return JSUtil.createFunction(gmarker.getFactoryMethod(), "return new GMarker("
				+ gmarker.toString() + ");");
	}

	private String getInfoFactoryName()
	{
		return "createInfo" + gmarker.getOverlayId();
	}

	public static final String ID = "gmarkerComponent";
}
