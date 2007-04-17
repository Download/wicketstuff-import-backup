/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.javaee.injection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.persistence.PersistenceUnit;

import wicket.injection.IFieldValueFactory;
import wicket.javaee.EntityManagerFactoryLocator;
import wicket.javaee.JavaEEBeanLocator;
import wicket.javaee.JndiObjectLocator;
import wicket.javaee.naming.IJndiNamingStrategy;
import wicket.javaee.naming.StandardJndiNamingStrategy;
import wicket.proxy.IProxyTargetLocator;
import wicket.proxy.LazyInitProxyFactory;

/**
 * {@link IFieldValueFactory} that creates
 * proxies of EJBs based on the {@link javax.persistence.EJB} annotation
 * applied to a field. 
 * 
 * @author Filippo Diotalevi
 * 
 */
public class JavaEEProxyFieldValueFactory implements IFieldValueFactory
{
	private final ConcurrentHashMap<IProxyTargetLocator, Object> cache = new ConcurrentHashMap<IProxyTargetLocator, Object>();
	private IJndiNamingStrategy namingStrategy;

	/**
	 * Constructor
	 */
	public JavaEEProxyFieldValueFactory()
	{
		this(new StandardJndiNamingStrategy());
	}
	
	/**
	 * Constructor
	 */
	public JavaEEProxyFieldValueFactory(IJndiNamingStrategy namingStrategy)
	{
		this.namingStrategy = namingStrategy;
	}

	/**
	 * @see wicket.extensions.injection.IFieldValueFactory#getFieldValue(java.lang.reflect.Field,
	 *      java.lang.Object)
	 */
	public Object getFieldValue(Field field, Object fieldOwner)
	{
		IProxyTargetLocator locator = getProxyTargetLocator(field);
		return getCachedProxy(field.getType(), locator);
	}

	private IProxyTargetLocator getProxyTargetLocator(Field field) {
		if (field.isAnnotationPresent(EJB.class))
		{
			return new JavaEEBeanLocator(field.getAnnotation(EJB.class).name(), field.getType(), namingStrategy);
		}
		
		if (field.isAnnotationPresent(PersistenceUnit.class)) 
		{
			return new EntityManagerFactoryLocator(field.getAnnotation(PersistenceUnit.class).unitName());
		}
		
		if (field.isAnnotationPresent(Resource.class)) 
		{
			return new JndiObjectLocator(field.getAnnotation(Resource.class).name(), field.getType());
		}
		//else
		return null;
	}

	private Object getCachedProxy(Class type, IProxyTargetLocator locator) {
		if (locator ==  null)
			return null;
		
		if (cache.containsKey(locator))
		{
			return cache.get(locator);
		}

		if (!Modifier.isFinal(type.getModifiers()))
		{
			Object proxy = LazyInitProxyFactory.createProxy(type, locator);
			cache.put(locator, proxy);
			return proxy;	
		}
		else
		{
			Object value = locator.locateProxyTarget();
			cache.put(locator, value);
			return value;	
		}	
		
	}


	/**
	 * @see wicket.extensions.injection.IFieldValueFactory#supportsField(java.lang.reflect.Field)
	 */
	public boolean supportsField(Field field)
	{
		return field.isAnnotationPresent(EJB.class) || field.isAnnotationPresent(Resource.class) || field.isAnnotationPresent(PersistenceUnit.class);
	}

}
