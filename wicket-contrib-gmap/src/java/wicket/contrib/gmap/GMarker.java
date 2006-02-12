package wicket.contrib.gmap;

import wicket.Component;
import wicket.markup.html.WebComponent;

/**
 * Google Maps API's GMarker is represented by this class. Now you can set a wicket component to be displayed within info window.
 * The layout and the size of the component is up to you,
 * Actually the added component can be any wicket component (even any container, panel or border) but not a wicket page.
 * it can be as big as you want as long as it looks good and fits into the page.
 * There is one restriction, the name of the component has to be "<b>gmarkerInfo</b>", otherwise an exceptionn will be thrown.
 *
 * @author Iulian-Corneliu Costan
 */
public class GMarker extends Overlay
{

    private Component component;
    private GIcon icon;

    /**
     * Creates an empty marker, only the default icon will be displayed and no onClick event handler will be attached.
     *
     * @param point
     */
    public GMarker(GPoint point)
    {
        this(point, new WebComponent("gmarkerInfo"));
    }

    /**
     * Creates an marker that will have an onClick event attached.
     * When user clicks on this marker, wicket <code>component</code> will be rendered.
     *
     * @param point
     * @param component
     */
    public GMarker(GPoint point, Component component)
    {
        super(point);
        this.component = component;
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
     * Get the name of the JavaScript function that will be called in order to create this overlay.
     *
     * @return JavaScript function name
     */
    public String getFactoryMethod()
    {
        return "createMarker" + getOverlayId();
    }
}
