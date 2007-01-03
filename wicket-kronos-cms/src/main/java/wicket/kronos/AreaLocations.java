package wicket.kronos;

import java.util.ArrayList;
import java.util.List;

/**
 * @author postma
 */
public final class AreaLocations {
	/**
	 * Page area defenitions, numbered from top to bottom and from left to right
	 */
	public static final int HEADER = 0;

	/**
	 * Integer referring to the left area of the frontpage
	 */
	public static final int LEFT = 1;

	/**
	 * Integer referring to the center area of the frontpage
	 */
	public static final int CENTER = 2;

	/**
	 * Integer referring to the right area of the frontpage
	 */
	public static final int RIGHT = 3;

	/**
	 * Integer referring to the footer area of the frontpage
	 */
	public static final int FOOTER = 4;

	/**
	 * @param location
	 *            number
	 * @return location string
	 */
	public static String getLocationname(int location)
	{
		assert (location > -1);
		assert (location < 5);
		String areaname = null;
		switch (location) {
		case 0:
			areaname = "header";
			break;
		case 1:
			areaname = "left";
			break;
		case 2:
			areaname = "center";
			break;
		case 3:
			areaname = "right";
			break;
		case 4:
			areaname = "footer";
			break;
		default:
			break;
		}
		return areaname;
	}
	
	/**
	 * @return List list with arealocations
	 */
	@SuppressWarnings("unchecked")
	public static List getAreaLocations()
	{
		List locations = new ArrayList();
		
		locations.add(new Integer(0));
		locations.add(new Integer(1));
		locations.add(new Integer(2));
		locations.add(new Integer(3));
		locations.add(new Integer(4));
		
		return locations;
	}
}
