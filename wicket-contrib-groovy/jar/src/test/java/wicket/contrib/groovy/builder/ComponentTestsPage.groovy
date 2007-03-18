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
package wicket.contrib.groovy.builder;

import wicket.markup.html.WebPage
import wicket.markup.html.basic.Label
import wicket.model.Model
import wicket.markup.html.form.Form


class ComponentTestsPage extends WebPage {

  	public ComponentTestsPage()
  	{ 
  		WicketBuilder builder = new WicketBuilder(page)
		builder.form('baseForm', model:"String", onSubmit:{println "asdf"})
		{
  			listView('testListView', list:['asdf', 'qwert'], populateItem:
  			{
  				label('innerLabel', model:'sometext' )
  			}) 
//			ajaxSubmitLink('ajaxLinkString', form:getCurrent(), onSubmit:"println 'asdf'")
			ajaxSubmitLink('ajaxLinkClosure', form:getCurrent(), onSubmit:{a, b -> println 'in closure'})
			btestAjaxSubmitLink('testAjaxSubmitLink', form:getCurrent(), testMessage:"asdf", testValue:22)
			checkGroup('testCheckGroup', collection:new java.util.ArrayList())
			{
				checkGroupSelector('testCheckGroupSelector')
			}
			
			customComponent('customComponent')
			initLink('initLink', testName:'Steve', testDate:new Date(), testInt:1248)
			
			//externalLink('extLink') fails
			externalLink('extLink1', href:"http://www.cnn.com/")
			externalLink('extLink2', href:"http://www.cnn.com/", label:"CNN")
			
			textArea('testArea', label:"testArea", required:false)
			
			button('normalButton')
			button('onSubmitButton', onSubmit:{println "onSubmitButton"})
			
			textField('textField')
			textField('integerField', type:Integer)
			textField('integerField2', type:"java.lang.Integer")
			passwordTextField('passwordText')
			requiredTextField('requiredText')
			
		}
  		
//  		builder.styleLink("styleLink", class:ComponentTestsPage.class)
  		
  		builder.form('baseForm2')
		{
			
		}
  		
  		builder.form('baseForm3', model:new Model("modelConstructor"))
		{
			
		}
  		
  		builder.form('normalForm')
//		builder.form('onSubmitStringForm', onSubmit:"println 'onSubmitString'")
		builder.form('onSubmitClosureForm', onSubmit:{println 'onSubmitClosure'})
//		builder.form('onErrorStringForm', onError:"println 'onErrorString'")
		builder.form('onErrorClosureForm', onError:{println 'onErrorClosure'})
  		
  		boolean bombedOut = false
  		try
  		{
	  		builder.form('baseForm4', noprop:"asdf")
			{
				
			}
  		}
  		catch(RuntimeException e)
  		{
  			bombedOut = true
  		}
  		
  		if(bombedOut == false)
  			throw new Exception("Noprop didn't fail")
  		
  		
  	}
  	
  	
}

public class BtestAjaxSubmitLink extends wicket.ajax.markup.html.form.AjaxSubmitLink
{
	public BtestAjaxSubmitLink(String key, Form form)
	{
		super(key, form)
	}
	
	public void onSubmit(wicket.ajax.AjaxRequestTarget target, Form form)
	{
		
	}
	
	String testMessage
	Integer testValue
}