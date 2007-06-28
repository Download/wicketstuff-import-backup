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

/**
 * Represents an Google Maps API's GMarker <a
 * href="http://www.google.com/apis/maps/documentation/reference.html#GMarker">GMarker</a>
 * 
 */
public class GMarker extends GOverlay
{
	private static final long serialVersionUID = 1L;

	private GLatLng gLatLng;

	private String title;

	/**
	 * @param gLatLng
	 *            the point on the map where this marker will be anchored
	 */
	public GMarker(GLatLng gLatLng)
	{
		this(gLatLng, null);
	}

	public GMarker(GLatLng gLatLng, String title)
	{
		super();
		this.gLatLng = gLatLng;
		this.title = title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public GLatLng getLagLng()
	{
		return gLatLng;
	}

	public String getJSConstructor()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("new GMarker(");
		buffer.append(gLatLng.getJSConstructor());
		buffer.append(",{");
		if (title != null)
		{
			buffer.append("title: \"");
			buffer.append(title);
			buffer.append("\"");
		}
		buffer.append("})");
		
		return buffer.toString();
	}
}
