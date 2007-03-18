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
import java.util.List;
import java.util.Map;

import wicket.markup.html.form.Form;

public class SubmitLinkComponentBuilder extends FormComponentComponentBuilder
{
	public SubmitLinkComponentBuilder(Class componentClass)
	{
		super(componentClass);
	}
	
	private static final Class[] CONSTRUCTOR_FORM = new Class[]{String.class, Form.class};
	
	public List getConstructorParameters(String key, Map attributes)
	{
		List params = new ArrayList();
		params.add(key);
		Object form = attributes.remove("form");
		
		if(form != null)
			params.add(form);
		
		return params;
	}
}
