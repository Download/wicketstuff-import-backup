package wicket.contrib.gmap;

/**
 * @author Ryan Gravener (ryangravener)
 */
public class GPlacemark implements Placemark
{

	private static final long serialVersionUID = 1L;

	private String countryNameCode;
	
	private String administrativeAreaName;
	
	private String subAdministrativeAreaName;
	
	private String localityName;
	
	private String thoroughFareName;
	
	private String postalCodeNumber;
	
	private double latitude;
	
	private double longitude;


	/**
	 * @see wicket.contrib.gmap.Placemark#getAdministrativeAreaName()
	 */
	public String getAdministrativeAreaName()
	{
		return administrativeAreaName;
	}

	/**
	 * @see wicket.contrib.gmap.Placemark#setAdministrativeAreaName(java.lang.String)
	 */
	public void setAdministrativeAreaName(String administrativeAreaName)
	{
		this.administrativeAreaName = administrativeAreaName;
	}

	/**
	 * @see wicket.contrib.gmap.Placemark#getCountryNameCode()
	 */
	public String getCountryNameCode()
	{
		return countryNameCode;
	}

	/**
	 * @see wicket.contrib.gmap.Placemark#setCountryNameCode(java.lang.String)
	 */
	public void setCountryNameCode(String countryNameCode)
	{
		this.countryNameCode = countryNameCode;
	}

	/**
	 * @see wicket.contrib.gmap.Placemark#getLocalityName()
	 */
	public String getLocalityName()
	{
		return localityName;
	}

	/**
	 * @see wicket.contrib.gmap.Placemark#setLocalityName(java.lang.String)
	 */
	public void setLocalityName(String localityName)
	{
		this.localityName = localityName;
	}

	/**
	 * @see wicket.contrib.gmap.Placemark#getPostalCodeNumber()
	 */
	public String getPostalCodeNumber()
	{
		return postalCodeNumber;
	}

	/**
	 * @see wicket.contrib.gmap.Placemark#setPostalCodeNumber(java.lang.String)
	 */
	public void setPostalCodeNumber(String postalCodeNumber)
	{
		this.postalCodeNumber = postalCodeNumber;
	}

	/**
	 * @see wicket.contrib.gmap.Placemark#getSubAdministrativeAreaName()
	 */
	public String getSubAdministrativeAreaName()
	{
		return subAdministrativeAreaName;
	}

	/**
	 * @see wicket.contrib.gmap.Placemark#setSubAdministrativeAreaName(java.lang.String)
	 */
	public void setSubAdministrativeAreaName(String subAdministrativeAreaName)
	{
		this.subAdministrativeAreaName = subAdministrativeAreaName;
	}

	/**
	 * @see wicket.contrib.gmap.Placemark#getThoroughFareName()
	 */
	public String getThoroughFareName()
	{
		return thoroughFareName;
	}

	/**
	 * @see wicket.contrib.gmap.Placemark#setThoroughFareName(java.lang.String)
	 */
	public void setThoroughFareName(String thoroughFareName)
	{
		this.thoroughFareName = thoroughFareName;
	}
	
	
	/**
	 * @see wicket.contrib.gmap.Placemark#getLatitude()
	 */
	public double getLatitude()
	{
		return latitude;
	}

	/**
	 * @see wicket.contrib.gmap.Placemark#setLatitude(double)
	 */
	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}

	/**
	 * @see wicket.contrib.gmap.Placemark#getLongitude()
	 */
	public double getLongitude()
	{
		return longitude;
	}

	/**
	 * @see wicket.contrib.gmap.Placemark#setLongitude(double)
	 */
	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}
	
	/**
	 * @return gLatLng
	 */
	public GLatLng toGLatLng() {
		return new GLatLng(latitude,longitude);
	}
}
