/*
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.contrib.gmap.api;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * Represents an Google Maps API's GMarker
 * <a href="http://www.google.com/apis/maps/documentation/reference.html#GMarker">GMarker</a>
 *
 */
public class GMarker extends GOverlay
{
	private static final long serialVersionUID = 1L;

	private GLatLng gLatLng;
	
	private String title;

	/**
	 * @param point
	 *            the point on the map where this marker will be anchored
	 */
	public GMarker(GLatLng point)
	{
		this(point, null);
	}
	
	public GMarker(GLatLng gLatLng, String title)
	{
		super();
		this.gLatLng = gLatLng;
		this.title = title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getJSConstructor()
	{
		return "new GMarker(" + gLatLng.getJSConstructor() + ",{" + getOptions() + "})";
	}

	public GLatLng getLagLng() {
		return gLatLng;
	}
	
	private String getOptions() {
		StringBuffer options = new StringBuffer();
		
		if (title != null) {
			options.append("title: \"");
			options.append(title);
			options.append("\"");
		}
		
		return options.toString();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof GMarker)
		{
			GMarker t = (GMarker)obj;
			return t.gLatLng.equals(gLatLng) && (t.title == null && title == null || t.title != null && t.title.equals(title));
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return gLatLng.hashCode() ^ (title != null ? title.hashCode() : 1337);
	}
	
	/**
	 * Override this to be called when a marker is clicked on.
	 * 
	 * @param target
	 */
	public void onClick(AjaxRequestTarget target)
	{
		
	}
}
