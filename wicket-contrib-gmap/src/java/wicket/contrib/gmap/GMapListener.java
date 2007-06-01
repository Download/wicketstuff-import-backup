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

import wicket.ajax.AjaxRequestTarget;

/**
 * Listener for events on gmap.
 * 
 * @author Nino Martinez Wael
 */
public interface GMapListener extends Serializable
{
	/**
	 * TODO passing GMap as parameter make no sense here, since gmap instance is
	 * something that user already has, it creates and initializes gmap at the
	 * begining, it is quite reduntant, but still for the moment it is fine, let it go.
	 * 
	 * @param target
	 *            the ajax target that carries this event
	 * @param gMap
	 *            the updated map
	 */
	void onClick(AjaxRequestTarget target, GMap gMap);
}
