/*
 * $Id$
 * $Revision$ $Date$
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
package wicket.contrib.dojo;

import wicket.Application;
import wicket.IInitializer;
import wicket.util.resource.IResourceStream;

/**
 * Initializer for components in wicket dojo library.
 * 
 * @author Eelco Hillenius
 */
public class Initializer implements IInitializer {
	/**
	 * @see wicket.IInitializer#init(wicket.Application)
	 */
	public void init(Application application) {
		// for ajax initialization
		new DojoAjaxHandler() {
			protected IResourceStream getResponse() {
				return null;
			}
		}.init(application);
	}
}
