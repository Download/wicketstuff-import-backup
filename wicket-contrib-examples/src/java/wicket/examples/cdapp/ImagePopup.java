/*
 * $Id$ $Revision$ $Date$
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
package wicket.examples.cdapp;

import wicket.markup.html.WebPage;
import wicket.markup.html.WebResource;
import wicket.markup.html.image.Image;

/**
 * Displays the an image using the provided image resource.
 * 
 * @author Eelco Hillenius
 */
public final class ImagePopup extends WebPage
{
	/**
	 * Construct.
	 * 
	 * @param imageResource
	 *            the image resource to display
	 */
	public ImagePopup(WebResource imageResource)
	{
		super();
		new Image(this, "image", imageResource);
	}
}