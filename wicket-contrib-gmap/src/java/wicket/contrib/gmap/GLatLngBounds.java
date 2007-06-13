/*
 * $Id: org.eclipse.jdt.ui.prefs 5004 2006-03-17 20:47:08 -0800 (Fri, 17 Mar
 * 2006) eelco12 $ $Revision: 5004 $ $Date: 2006-03-17 20:47:08 -0800 (Fri, 17
 * Mar 2006) $
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
package wicket.contrib.gmap;

import java.io.Serializable;

/**
 * Bounds of something, represented by latitude and longitude, southWest and
 * northEast
 * 
 * @author Nino Martinez Wael
 */
public class GLatLngBounds implements Serializable
{
	private GLatLng southWest;
	private GLatLng northEast;

	/**
	 * Construct.
	 */
	public GLatLngBounds()
	{
		this(new GLatLng(0, 0), new GLatLng(0, 0));
	}

	/**
	 * Construct.
	 * 
	 * @param sw
	 * @param ne
	 */
	public GLatLngBounds(GLatLng sw, GLatLng ne)
	{
		if (sw.getLatitude() > ne.getLatitude())
			throw new RuntimeException("SouthEast corner must be more south than NorthWest corner");
		this.southWest = sw;
		this.northEast = ne;
	}

	/**
	 * @return
	 */
	public GLatLng getNorthEast()
	{
		return northEast;
	}

	public void setNorthEast(GLatLng ne)
	{
		this.northEast = ne;
	}

	/**
	 * @return
	 */
	public GLatLng getSouthWest()
	{
		return southWest;
	}

	/**
	 * @param sw
	 */
	public void setSouthWest(GLatLng sw)
	{
		this.southWest = sw;
	}

	/**
	 * @return
	 */
	public GLatLng getNorthWest()
	{
		return new GLatLng(northEast.getLatitude(), southWest.getLongitude());
	}

	/**
	 * @return
	 */
	public GLatLng getSouthEast()
	{
		return new GLatLng(southWest.getLatitude(), northEast.getLongitude());
	}

	/*
	 * public boolean containsBounds(GLatLngBounds b) { boolean ret = false; if
	 * (includes(b.getNorthEast()) && includes(b.getNorthWest()) &&
	 * includes(b.getSouthEast()) && includes(b.getSouthWest())) ; { ret = true; }
	 * return ret; }
	 */
	/**
	 * @param other
	 * @return true if given coordinates are inside
	 */
	public boolean includes(GLatLng other)
	{
		if (southWest.getLatitude() > other.getLatitude())
			return false;
		if (northEast.getLatitude() < other.getLatitude())
			return false;

		boolean crossmeridian = southWest.getLongitude() > northEast.getLongitude();

		boolean bigger = other.getLongitude() > southWest.getLongitude()
				&& other.getLongitude() > northEast.getLongitude();

		boolean smaller = other.getLongitude() < southWest.getLongitude()
				&& other.getLongitude() < northEast.getLongitude();

		boolean between = !(smaller || bigger);

		return crossmeridian ^ between;

		/*
		 * 
		 * if (southWest.getLongitude() + delta < northEast.getLongitude()) { if
		 * (southWest.getLongitude() > other.getLongitude() + delta) return
		 * false; if (delta + northEast.getLongitude() < other.getLongitude())
		 * return false; } else { // this object crosses the "dateline" if
		 * (southWest.getLongitude() + delta < other.getLongitude()) return
		 * false; if (northEast.getLongitude() > other.getLongitude() + delta)
		 * return false; } return true;
		 */
	}

	public boolean equals(Object other)
	{
		if (other == null || !(other instanceof GLatLngBounds))
			return false;
		GLatLngBounds bounds = (GLatLngBounds)other;
		return southWest.equals(bounds.getSouthWest()) && northEast.equals(bounds.getNorthEast());
	}

	public String toString()
	{
		return "bounds are:SouthWest:" + southWest + " NorthEast:" + northEast;
	}

}