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

import groovy.lang.Binding;
import groovy.lang.Closure;
import groovy.lang.GroovyShell;
import groovy.lang.Reference;
import groovy.lang.Script;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.SingletonMap;

public class ClosureScriptWrapper implements Serializable, ScriptWrapper
{
	static final Class[] CONSTRUCTOR = new Class[]{Object.class, Object.class};
	
	Class closureClass;
	Serializable owner;
	
	transient Closure closure;
	
	public ClosureScriptWrapper(Closure closure)
	{
		this.closureClass = closure.getClass();
		
		//Test for local references (not allowed)
		Constructor[] cons = closureClass.getConstructors();
		for(int i=0; i<cons.length; i++)
		{
			Class[] params = cons[i].getParameterTypes();
			for(int j=0; j<params.length; j++)
			{
				if(params[j].equals(Reference.class))
					throw new WicketComponentBuilderException("Closures cannot have local variable references");
			}
		}
		
		Object tempOwner = closure.getOwner();
		while(tempOwner instanceof Closure && tempOwner != ((Closure)tempOwner).getOwner())
		{
			tempOwner = ((Closure)tempOwner).getOwner();
		}
		
		if(tempOwner instanceof Serializable == false)
			throw new WicketComponentBuilderException("Owning class of closure must be java.io.Serializable");
		
		this.owner = (Serializable) tempOwner;
		//We don't want this to work with the set closure and not one created fromthe class, so don't
		//set here
//		this.closure = closure;
	}
	
	
	/* (non-Javadoc)
	 * @see wicket.contrib.groovy.builder.ScriptWrapper#run(java.lang.Object)
	 */
	public Object run(Object ignore, Object[] args) {
		if(closureClass == null && closure == null)
			return null;
		
		//TODO: A little ugly.  Clean up.
		getClosure(ignore);
		
//		closure.
		
		return closure.call(args);
	}


	public Closure getClosure(Object newOwner)
	{
		if(closure == null || (closure.getOwner() != newOwner))
		{
			System.out.println("creating new closure class instance");
			try
			{
				Constructor cons = closureClass.getConstructor(CONSTRUCTOR);
				cons.setAccessible(true);
				closure = (Closure) cons.newInstance(new Object[]{newOwner, newOwner});
				closure.setDelegate(owner);
			}
			catch (Exception e)
			{
				throw new WicketComponentBuilderException("Can't call closure", e);
			}
		}
		return closure;
	}
	

}
