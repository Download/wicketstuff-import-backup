/*
 * $Id$ $Revision$ $Date:
 * 2007-05-28 23:46:51 +0200 (Mon, 28 May 2007) $
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
	private GLatLngBounds bounds = new GLatLngBounds();

	// TODO well, this class is supposed to be a POJO that contains only gmap
	// related info (coordinates, settings, etc). adding wicket models into it
	// makes it little dirty

	// I can see that, when the project Im on are done we'll work on that.
	private boolean insertMode = false;
	private boolean dragEndMode = false;

	/** map controls * */
	private boolean largeMapControl;
	private boolean overviewMapControl;
	private boolean typeControl;
	private boolean scaleControl;
	private boolean smallMapControl;
	private boolean smallZoomControl;

	private int zoomLevel;
	private List overlays = new ArrayList();

	/**
	 * Creates a map in insert mode, the insertModel will be notified, with an
	 * GLatLng object after that the GMapPanel will be refreshed via ajax to
	 * display changes
	 * 
	 * @param center
	 * @param insertModel
	 * @param zoomLevel
	 * @author Nino Martinez Wael
	 * @param mode
	 *            tells if clicknotifier should be used
	 */
	public GMap(GLatLng center, boolean mode, int zoomLevel)
	{
		this.center = center;
		this.insertMode = mode;
		this.zoomLevel = zoomLevel;
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
	 * @return
	 */
	public GLatLng getCenter()
	{
		return center;
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
	 * @return
	 */
	public int getZoomLevel()
	{
		return zoomLevel;
	}

	/**
	 * @return
	 */
	public boolean isInsertMode()
	{
		return insertMode;
	}

	public boolean isLargeMapControl()
	{
		return largeMapControl;
	}

	public boolean isOverviewMapControl()
	{
		return overviewMapControl;
	}

	public boolean isScaleControl()
	{
		return scaleControl;
	}

	public boolean isSmallMapControl()
	{
		return smallMapControl;
	}

	public boolean isSmallZoomControl()
	{
		return smallZoomControl;
	}

	public boolean isTypeControl()
	{
		return typeControl;
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

	/**
	 * display GScaleControl - a map scale
	 * 
	 * @param scaleControl
	 */
	public void setScaleControl(boolean scaleControl)
	{
		this.scaleControl = scaleControl;
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
	 * Show/Hide the type control (right-up corner)
	 * 
	 * @param typeControl
	 */
	public void setTypeControl(boolean typeControl)
	{
		this.typeControl = typeControl;
	}

	public void setZoomLevel(int zoomLevel)
	{
		this.zoomLevel = zoomLevel;
	}

	public GLatLngBounds getBounds()
	{
		return bounds;
	}


	public boolean isDragEndMode()
	{
		return dragEndMode;
	}

	public void setBounds(GLatLngBounds bounds)
	{
		this.bounds = bounds;
	}

	public void setCenter(GLatLng center)
	{
		this.center = center;
	}

	public void setDragEndMode(boolean dragEndMode)
	{
		this.dragEndMode = dragEndMode;
	}

	public void setInsertMode(boolean insertMode)
	{
		this.insertMode = insertMode;
	}
}
