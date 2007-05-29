/*
 * $Id$
 * $Revision$ $Date$
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

/**
 * Represents Googles GLatLng class contains Geo location stored in latitude and
 * longtitude
 * 
 * Latitude will be clamped to lie within [-90,90] and longitude will be wrapped to
 * lie within ]-180, 180]
 * 
 * @author Nino Martinez Wael
 */
public class GLatLng implements Serializable
{
	private static final long serialVersionUID = 1L;

	private double longitude;
	private double latitude;

	/**
	 * Construct.
	 * 
	 * @param gLatLng
	 */
	public GLatLng(GLatLng gLatLng)
	{
		this(gLatLng.getLatitude(), gLatLng.getLongitude());
	}

	/**
	 * Construct.
	 * 
	 * @param latitude
	 * @param longitude
	 */
	public GLatLng(double latitude, double longitude)
	{
		setLatitude(latitude);
		setLongitude(longitude);
	}

	/**
	 * @return longitude
	 */
	public double getLongitude()
	{
		return longitude;
	}

	/**
	 * @return latitude
	 */
	public double getLatitude()
	{
		return latitude;
	}

	/**
	 * @see java.lang.Object#toString()
	 * @return returns a javascript representation of creating a new GLatLng
	 * 
	 */
	public String toString()
	{
		return "new GLatLng(" + latitude + ", " + longitude + ")";
	}

	public void setLatitude(double latitude)
	{
		//clamp latitude
		if (latitude < -90)
			latitude = -90;
		if (latitude > 90)
			latitude = 90;

		this.latitude = latitude;
	}

	public void setLongitude(double longitude)
	{
		//wrap longitude
		longitude = longitude%360;
		
		if (longitude > 180) {
			longitude = longitude - 360;
		} else if (longitude <= -180){
			longitude = longitude + 360;
		}
		
		this.longitude = longitude;
	}

	public double distance(GLatLng other)
	{
		double la = Math.abs(latitude - other.getLatitude());
		double ln = Math.abs(longitude - other.getLongitude());
		return Math.max(la, ln);
	}


	public static boolean isSamePosition(GLatLng a, GLatLng b)
	{
		return equalDouble(a.getLatitude(), b.getLatitude())
				&& equalDouble(a.getLongitude(), b.getLongitude());
	}

	private static boolean equalDouble(double a, double b)
	{
		final double delta = 0.000000001;
		return (a >= b - delta && a <= b + delta);
	}

	public boolean equals(Object obj)
	{
		if ((obj == null) || (!(obj instanceof GLatLng)))
			return false;
		return isSamePosition(this, (GLatLng)obj);
	}

}
