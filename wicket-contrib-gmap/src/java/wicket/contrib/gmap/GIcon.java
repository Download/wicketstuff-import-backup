/*
 * $Id$ $Revision$ $Date:
 * 2006-02-12 21:46:53 +0100 (Sun, 12 Feb 2006) $
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
 * todo to be implemented
 * 
 * @author Iulian-Corneliu COSTAN
 */
public class GIcon implements Serializable
{

	private String image;
	private GPoint anchor;
	private String shadow;
	private GSize iconSize;
	private GSize shadowSize;

	private GPoint infoWindowAncor;


	public GIcon(String image, GPoint anchor)
	{
		this.image = image;
		this.anchor = anchor;
	}


	public GIcon(String image, GPoint anchor, String shadow, GSize iconSize, GSize shadowSize,
			GPoint infoWindowAncor)
	{
		super();
		this.image = image;
		this.anchor = anchor;
		this.shadow = shadow;
		this.iconSize = iconSize;
		this.shadowSize = shadowSize;
		this.infoWindowAncor = infoWindowAncor;
	}


	public String getImage()
	{
		return image;
	}

	public GPoint getAnchor()
	{
		return anchor;
	}


	public GSize getIconSize()
	{
		return iconSize;
	}


	public void setIconSize(GSize iconSize)
	{
		this.iconSize = iconSize;
	}


	public GPoint getInfoWindowAncor()
	{
		return infoWindowAncor;
	}


	public void setInfoWindowAncor(GPoint infoWindowAncor)
	{
		this.infoWindowAncor = infoWindowAncor;
	}


	public String getShadow()
	{
		return shadow;
	}


	public void setShadow(String shadow)
	{
		this.shadow = shadow;
	}


	public GSize getShadowSize()
	{
		return shadowSize;
	}


	public void setShadowSize(GSize shadowSize)
	{
		this.shadowSize = shadowSize;
	}


	public void setAnchor(GPoint anchor)
	{
		this.anchor = anchor;
	}


	public void setImage(String image)
	{
		this.image = image;
	}
}
