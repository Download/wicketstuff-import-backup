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
package wicket.contrib.dojo.markup.html.container;

import static wicket.contrib.dojo.DojoIdConstants.DOJO_TYPE;
import static wicket.contrib.dojo.DojoIdConstants.DOJO_TYPE_CONTENTPANE;
import wicket.MarkupContainer;
import wicket.markup.ComponentTag;

/**
 * A simple Dojo container. Used to take place in an other container
 * @author Vincent Demay
 *
 */
public class DojoSimpleContainer extends AbstractDojoContainer
{
	/**
	 * Construct a Dojo container
	 * @param parent parent where container will be added
	 * @param id container id
	 * @param title container title
	 */
	public DojoSimpleContainer(MarkupContainer parent, String id, String title)
	{
		super(parent, id, title);
		add(new DojoSimpleContainerHandler());
	}

	/**
	 * Construct a Dojo container
	 * @param parent parent where container will be added
	 * @param id container id
	 */
	public DojoSimpleContainer(MarkupContainer parent, String id)
	{
		this(parent, id, null);
	}
	
	/**
	 * add attributes on component tag
	 * @param tag 
	 */
	protected void onComponentTag(ComponentTag tag)
	{
		super.onComponentTag(tag);
		tag.put(DOJO_TYPE, DOJO_TYPE_CONTENTPANE);
		tag.put("label", getTitle());
	}

}
