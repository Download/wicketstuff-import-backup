package wicket.contrib.gmap;

import wicket.Component;
import wicket.markup.html.WebComponent;

/**
 * Google Maps API's GMarker is represented by this class. Now you can set a
 * wicket component to be displayed within info window. The layout and the size
 * of the component is up to you, Actually the added component can be any wicket
 * component (even any container, panel or border) but not a wicket page. it can
 * be as big as you want as long as it looks good and fits into the page. There
 * is one restriction, the name of the component has to be "<b>gmarkerInfo</b>",
 * otherwise an exceptionn will be thrown.
 * 
 * @author Iulian-Corneliu Costan
 */
public class GMarker extends Overlay
{
	private static final long serialVersionUID = 1L;

	private Component component;
	private GIcon icon;
	private String toolTip="";

	/**
	 * Creates an empty marker, only the default icon will be displayed and no
	 * onClick event handler will be attached.
	 * 
	 * @param point
	 *            the point on the map where this marker will be anchored
	 * @see GLatLng
	 */
	public GMarker(GLatLng point)
	{
		this(point, new WebComponent("gmarkerInfo"));
	}

	/**
	 * Creates an empty marker, only the custom icon will be displayed and no
	 * onClick event handler will be attached.
	 * 
	 * @param point
	 *            the point on the map where this marker will be anchored
	 * @param gIcon
	 *            custom GIcon
	 * @see GLatLng
	 */
	public GMarker(GLatLng point, GIcon gIcon)
	{
		this(point, new WebComponent("gmarkerInfo"));
		this.icon = gIcon;
	}

	/**
	 * Creates an marker that will have an onClick event attached. When user
	 * clicks on this marker, wicket <code>component</code> will be rendered.
	 * 
	 * @param point
	 *            the point on the map where this marker will be anchored
	 * @param component
	 *            wicket component that needs to be rendered
	 * @see GLatLng
	 */
	public GMarker(GLatLng point, Component component)
	{
		super(point);
		this.component = component;
	}

	/**
	 * Creates an marker that will have an onClick event attached. When user
	 * clicks on this marker, wicket <code>component</code> will be rendered.
	 * 
	 * @param point
	 *            the point on the map where this marker will be anchored
	 * @param component
	 *            wicket component that needs to be rendered
	 * @param gIcon 
	 * 			 custom GIcon
	 * @see GLatLng
	 */
	public GMarker(GLatLng point, Component component, GIcon gIcon)
	{
		super(point);
		this.component = component;
		this.icon= gIcon;
	}

	/**
	 * Returns the attached wicket component.
	 * 
	 * @return component
	 */
	public Component getComponent()
	{
		return component;
	}

	/**
	 * Get the name of the JavaScript function that will be called in order to
	 * create this overlay.
	 * 
	 * @return JavaScript function name
	 */
	public String getFactoryMethod()
	{
		return "createMarker" + getOverlayId();
	}

	public GIcon getIcon()
	{
		return icon;
	}

	public void setIcon(GIcon icon)
	{
		this.icon = icon;
	}

	public String getToolTip()
	{
		return toolTip;
	}

	public void setToolTip(String toolTip)
	{
		this.toolTip = toolTip;
	}
}
