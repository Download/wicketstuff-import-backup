package wicket.contrib.gmap;

import java.io.Serializable;

/**
 * @author Ryan Gravener
 */
public interface Placemark extends Serializable {
	
	/**
	 * @return countryNameCode
	 */
	public String getCountryNameCode();
	
	/**
	 * @param countryNameCode
	 */
	public void setCountryNameCode(String countryNameCode);
	
	/**
	 * @return administrativeAreaName
	 */
	public String getAdministrativeAreaName();
	
	
	/**
	 * @param administrativeAreaName
	 */
	public void setAdministrativeAreaName(String administrativeAreaName);
	
	/**
	 * @return subAdministrativeAreaName
	 */
	public String getSubAdministrativeAreaName();
	
	/**
	 * @param subAdminstrativeAreaName
	 */
	public void setSubAdministrativeAreaName(String subAdminstrativeAreaName);
	
	/**
	 * @return localityName
	 */
	public String getLocalityName();
	
	/**
	 * @param localityName
	 */
	public void setLocalityName(String localityName);
	
	/**
	 * @return thoroughFareName
	 */
	public String getThoroughFareName();
	
	/**
	 * @param thoroughFareName
	 */
	public void setThoroughFareName(String thoroughFareName);
	
	/**
	 * @return postalCodeNumber
	 */
	public String getPostalCodeNumber();
	
	/**
	 * @param postalCodeNumber
	 */
	public void setPostalCodeNumber(String postalCodeNumber);
	
	/**
	 * @return latitude;
	 */
	public double getLatitude();
	
	/**
	 * @param latitude
	 */
	public void setLatitude(double latitude);
	
	/**
	 * @return longitude
	 */
	public double getLongitude();
	
	/**
	 * @param longitude
	 */
	public void setLongitude(double longitude);

	// TODO Placemark should have accuracy
		
}
