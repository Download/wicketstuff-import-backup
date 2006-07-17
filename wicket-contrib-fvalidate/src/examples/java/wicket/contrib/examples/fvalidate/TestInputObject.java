/*
 * $Id$
 * $Revision$
 * $Date$
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
package wicket.contrib.examples.fvalidate;

import java.io.Serializable;
import java.util.Date;

/**
 * Test object for various inputs.
 */
public final class TestInputObject implements Serializable
{
	private String stringProperty = "test";

	private Integer integerProperty = new Integer(100);

	private Double doubleProperty = new Double(20.5);

	private Date dateProperty = new Date();

	/**
	 * Gets dateProperty.
	 * @return dateProperty
	 */
	public Date getDateProperty()
	{
		return dateProperty;
	}

	/**
	 * Sets dateProperty.
	 * @param dateProperty dateProperty
	 */
	public void setDateProperty(Date dateProperty)
	{
		this.dateProperty = dateProperty;
	}

	/**
	 * Gets doubleProperty.
	 * @return doubleProperty
	 */
	public Double getDoubleProperty()
	{
		return doubleProperty;
	}

	/**
	 * Sets doubleProperty.
	 * @param doubleProperty doubleProperty
	 */
	public void setDoubleProperty(Double doubleProperty)
	{
		this.doubleProperty = doubleProperty;
	}

	/**
	 * Gets integerProperty.
	 * @return integerProperty
	 */
	public Integer getIntegerProperty()
	{
		return integerProperty;
	}

	/**
	 * Sets integerProperty.
	 * @param integerProperty integerProperty
	 */
	public void setIntegerProperty(Integer integerProperty)
	{
		this.integerProperty = integerProperty;
	}

	/**
	 * Gets stringProperty.
	 * @return stringProperty
	 */
	public String getStringProperty()
	{
		return stringProperty;
	}

	/**
	 * Sets stringProperty.
	 * @param stringProperty stringProperty
	 */
	public void setStringProperty(String stringProperty)
	{
		this.stringProperty = stringProperty;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return "TestInputObject{string=" + stringProperty + ",integer=" + integerProperty +
			",double=" + doubleProperty + ",date=" + dateProperty + "}";
	}
}
