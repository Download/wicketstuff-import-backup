/**
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
package wicket.contrib.groovy.builder.impl.wicket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import wicket.Component;
import wicket.contrib.groovy.builder.WicketComponentBuilderException;
import wicket.contrib.groovy.builder.util.ListConstructors;

public class CheckGroupComponentBuilder extends GenericComponentBuilder
{
	static final public Class[] CONSTRUCTOR = new Class[]{String.class, Collection.class};
	
	public CheckGroupComponentBuilder(Class componentClass)
	{
		super(componentClass);
	}

	/**
	 * This is very non-standard.  Skip the auto-building
	 */
	public List getConstructorParameters(String key, Map attributes)
	{
		List params = new ArrayList();
		params.add(key);
		
		Object collection = attributes.remove("collection");
		if(collection != null)
			params.add(collection);
		
		return params;
	}
}
