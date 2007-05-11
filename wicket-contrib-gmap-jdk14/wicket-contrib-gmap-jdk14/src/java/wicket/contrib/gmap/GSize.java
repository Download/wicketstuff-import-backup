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
 * A GSize is the size in pixels of a rectangular area of the map. The size
 * object has two parameters, width and height. Width is a difference in the
 * x-coordinate; height is a difference in the y-coordinate, of points.
 * 
 * @author Nino Martinez Wael
 */
public class GSize implements Serializable
{

	private int width;
	private int height;

	public String toString()
	{
		return "new GSize(" + width + ", " + height + ")";
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int y)
	{
		this.height = y;
	}

	public GSize(int width, int height)
	{
		super();
		this.width = width;
		this.height = height;
	}

}
