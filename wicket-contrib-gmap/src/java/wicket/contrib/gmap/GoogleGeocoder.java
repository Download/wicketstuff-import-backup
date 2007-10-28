package wicket.contrib.gmap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * @author Ryan Gravener (ryangravener)
 */
public class GoogleGeocoder {

	/**
	 * @param apiKey
	 * @param query
	 * @param placemark
	 * @return google status code
	 * @throws IOException 
	 */
	public static int geocode(final String apiKey, final String query, Placemark placemark) throws IOException {
		
		URLConnection conn = null;
		BufferedReader br = null;
		int code = -1;
		try
		{
			conn = new URL("http://maps.google.com/maps/geo?q=" + URLEncoder.encode(query,"UTF-8") + "&output=xml&key=" + apiKey).openConnection();
			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuffer buffer = new StringBuffer();
			
			String line = null;
			while( (line = br.readLine()) != null ) {
				buffer.append(line);
			}
			code = Integer.parseInt(findField(buffer,"code"));
			if(code!=200) {
				try {
					br.close();
				} catch (IOException e) {
				}
				return code;
			}
			placemark.setCountryNameCode(findField(buffer,"CountryNameCode"));
			placemark.setAdministrativeAreaName(findField(buffer,"AdministrativeAreaName"));
			placemark.setSubAdministrativeAreaName(findField(buffer,"SubAdministrativeAreaName"));
			placemark.setLocalityName(findField(buffer,"LocalityName"));
			placemark.setThoroughFareName(findField(buffer,"ThoroughfareName"));
			placemark.setPostalCodeNumber(findField(buffer,"PostalCodeNumber"));
			String coordinates[] = findField(buffer,"coordinates").split(",");
			placemark.setLatitude(Double.parseDouble(coordinates[0]));
			placemark.setLongitude(Double.parseDouble(coordinates[1]));
			// TODO: Implement Accuracy
		}
		catch (MalformedURLException e) {
			throw new MalformedURLException();
		}
		finally {
			if(br!=null) {
				try {
					br.close();
				} catch (IOException e) {
					
				}
			}
			return code;
		}
		
		
	}
	
	/**
	 * 
	 * @param content
	 * @param field
	 * @return String representing data inbetween the <field></field>
	 */
	private static String findField(StringBuffer content, String field) {
		int start = content.indexOf("<" + field + ">");
		if (start < 0) {
			return null;
		}
		int end = content.indexOf("</" + field + ">", start);
		if (end < 0) {
			return null;
		}
		return content.substring(start + 2 + field.length(), end).trim();
	}

}
