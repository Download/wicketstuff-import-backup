/*
 * $Id: GMap.java 695 2006-04-26 21:14:54Z syca $ $Revision: 695 $ $Date:
 * 2006-02-12 22:46:53 +0200 (Sun, 12 Feb 2006) $
 * 
 * ==================================================================== Licensed
 * under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the
 * License at
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the main Maps API's GMap object. First create an
 * instance of this class by specifying center and zoomLevel after that you can
 * add overlayes that needs to be displayed.
 * 
 * @see Overlay
 * 
 * @author Iulian-Corneliu Costan, Nino Martinez Wael
 * 
 */
public class GMap implements Serializable
{
	private static final long serialVersionUID = 1L;

	private GLatLng center;
	private int zoomLevel;
	private List overlays = new ArrayList();
	private boolean typeControl;
	private boolean smallMapControl;
	private boolean largeMapControl;
	private boolean smallZoomControl;
	private boolean scaleControl;
	private boolean overviewMapControl;


	public boolean isLargeMapControl()
	{
		return largeMapControl;
	}

	/**
	 * display GLargeMapControl - a large pan/zoom control used on Google Maps.
	 * Appears in the top left corner of the map.
	 * 
	 * @param largeMapControl
	 */
	public void setLargeMapControl(boolean largeMapControl)
	{
		this.largeMapControl = largeMapControl;
	}

	public boolean isOverviewMapControl()
	{
		return overviewMapControl;
	}

	/**
	 * display GOverviewMapControl - a collapsible overview map in the corner of
	 * the screen
	 * 
	 * @param overviewMapControl
	 */
	public void setOverviewMapControl(boolean overviewMapControl)
	{
		this.overviewMapControl = overviewMapControl;
	}

	public boolean isScaleControl()
	{
		return scaleControl;
	}

	/**
	 * display GScaleControl - a map scale
	 * 
	 * @param scaleControl
	 */
	public void setScaleControl(boolean scaleControl)
	{
		this.scaleControl = scaleControl;
	}

	public boolean isSmallZoomControl()
	{
		return smallZoomControl;
	}

	/**
	 * display GSmallZoomControl - a small zoom control (no panning controls)
	 * used in the small map blowup windows used to display driving directions
	 * steps on Google Maps.
	 * 
	 * @param smallZoomControl
	 */
	public void setSmallZoomControl(boolean smallZoomControl)
	{
		this.smallZoomControl = smallZoomControl;
	}

	/**
	 * Create GMap component
	 * 
	 * @param center
	 *            of the gmap
	 * @param zoomLevel
	 *            only values between 1 and 18 are allowed.
	 */
	public GMap(GLatLng center, int zoomLevel)
	{
		if (center == null)
		{
			throw new IllegalArgumentException("map's center point cannot be null");
		}
		if (zoomLevel < 0 || zoomLevel > 18)
		{
			throw new IllegalArgumentException("zoomLevel must be 1 < zoomLevel < 18 ");
		}
		this.center = center;
		this.zoomLevel = zoomLevel;
	}

	/**
	 * Add a overlay to this map.
	 * 
	 * @see Overlay
	 * @see GMarker
	 * 
	 * @param overlay
	 */
	public void addOverlay(Overlay overlay)
	{
		overlays.add(overlay);
	}

	/**
	 * Get all overlays.
	 * 
	 * @return overlays
	 */
	public List getOverlays()
	{
		return overlays;
	}

	/**
	 * Show/Hide the type control (right-up corner)
	 * 
	 * @param typeControl
	 */
	public void setTypeControl(boolean typeControl)
	{
		this.typeControl = typeControl;
	}

	/**
	 * Show/Hide the small map control
	 * 
	 * @param smallMapControl
	 */
	public void setSmallMapControl(boolean smallMapControl)
	{
		this.smallMapControl = smallMapControl;
	}

	public boolean isTypeControl()
	{
		return typeControl;
	}

	public boolean isSmallMapControl()
	{
		return smallMapControl;
	}

	public GLatLng getCenter()
	{
		return center;
	}

	public int getZoomLevel()
	{
		return zoomLevel;
	}

	public void setZoomLevel(int zoomLevel)
	{
		this.zoomLevel = zoomLevel;
	}
}
