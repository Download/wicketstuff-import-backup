package org.wicketstuff.yui.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the abstract class that helps to generate the notations for name pairs
 * 
 * 1. Inline Styles. eg. padding:1px ; color:#FF0000;
 * 2. Javscript Objects. eg. {name:"josh" , age : "44"}
 * 
 * as both are the same thing except it is used at different points (1) in "style=" and (2) in javascript
 * this abstract class handles the basic concatenation of these name/value pairs and generate the "dhtml" 
 * 
 * @author josh
 * 
 */
public abstract class NameValuePair<T> extends TokenSeparatedValues 
{
	private static final long serialVersionUID = 1L;

	/**
	 * the map used internally
	 */
	Map<String, String> propertyMap = new HashMap<String, String>();

	/**
	 * construct
	 */
	public NameValuePair()
	{
	}

	/**
	 * 
	 * @param element
	 * @param value
	 */
	@SuppressWarnings("unchecked")
	public T add(String element, String value) 
	{
		if (isValid(element, value)) 
		{
			if ((!value.startsWith("{")) &&
				(!value.startsWith("\"")))
			{
				try 
				{
					Float.parseFloat(value);
				}
				catch (Exception e) 
				{
					value = "\"" + value + "\"";
				}
			}
			System.out.println(element + ":" + value);
			propertyMap.put(element, value);
		}
		return (T) this;
	}
	

	@SuppressWarnings("unchecked")
	public T add(NameValuePair another)
	{
		Map<String, String> am = another.propertyMap;
		
		for (Map.Entry<String, String> entry : am.entrySet())
		{
			add(entry.getKey(), entry.getValue());
		}
		return (T)this;
	}
	
	/*
	 * the List to be used by TSV
	 */
	@Override
	public List<String> getValues()
	{
		List<String> list = new ArrayList<String>();
		
		for (Map.Entry<String, String> entry : propertyMap.entrySet())
		{
			list.add(entry.getKey() + getNameValueSeparator() + entry.getValue());
		}
		return list;
	}
	
	/**
	 * this should always be true for now...
	 *  
	 * @param element
	 * @param value
	 * @return
	 */
	public boolean isValid(String key, String value)
	{
		return true;
	}

	/**
	 * 
	 * @return
	 */
	public abstract String getNameValueSeparator();
}
