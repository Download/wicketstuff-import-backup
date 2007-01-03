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
package wicket.contrib.dojo.markup.html.container.split;

import static wicket.contrib.dojo.DojoIdConstants.DOJO_TYPE;
import static wicket.contrib.dojo.DojoIdConstants.DOJO_TYPE_SPLITCONTAINER;
import wicket.MarkupContainer;
import wicket.contrib.dojo.markup.html.container.AbstractDojoContainer;
import wicket.markup.ComponentTag;

/**
 * <p>
 * TabContainer widget where
 * AbstractDojoContainer should be added 
 * </p>
 * <p>
 * 	<b>Sample</b>
 *  <pre>
 *  public class TabContainerSample extends WebPage {
 *
 *	public TabContainerSample() {
 *		super();
 *		DojoSplitContainer container = new DojoSplitContainer(this,"splitContainer");
 *		container.setHeight("500px");
 *		new DojoSimpleContainer(container, "tab1", "title1");
 *		new DojoSimpleContainer(container, "tab2", "title2");
 *		new DojoPageContainer(container, "tab3", DatePickerShower.class).setTitle("title3");
 *		
 *	}
 *
 *}
 *
 *
 *  </pre>
 * </p>
 * @author Vincent Demay
 *
 */
public class DojoSplitContainer extends AbstractDojoContainer
{
	
	public static final String ORIENTATION_VERTICAL = "vertical";
	public static final String ORIENTATION_HORIZONTAL = "horizontal";
	
	private String orientation = ORIENTATION_HORIZONTAL;
	private int sizerWidth = 5;
	private Boolean activeSizing = new Boolean(false);
	
	/**
	 * Construct a DojoTabContainer
	 * @param parent parent where the container will be added
	 * @param id container id
	 * @param title container title
	 */
	public DojoSplitContainer(MarkupContainer parent, String id, String title)
	{
		super(parent, id, title);
		add(new DojoSplitHandler());
	}

	/**
	 * Construct a DojoTabContainer
	 * @param parent parent where the container will be added
	 * @param id container id
	 */
	public DojoSplitContainer(MarkupContainer parent, String id)
	{
		this(parent, id, null);
	}

	@Override
	protected void onComponentTag(ComponentTag tag)
	{
		super.onComponentTag(tag);
		tag.put(DOJO_TYPE, DOJO_TYPE_SPLITCONTAINER);
		tag.put("label", getTitle());
		tag.put("orientation", orientation);
		tag.put("sizerWidth", Integer.toString(sizerWidth));
		tag.put("activeSizing", activeSizing.toString());
	}

	/**
	 * Return true if resize is done in real time when the handler is moved and false otherwise
	 * @return true if resize is done in real time when the handler is moved and false otherwise
	 */
	public Boolean getActiveSizing()
	{
		return activeSizing;
	}

	/**
	 * set activeSizing to true to resize in real time when the handler is moved
	 * @param activeSizing true to resize in real time when the handler is moved
	 */
	public void setActiveSizing(Boolean activeSizing)
	{
		this.activeSizing = activeSizing;
	}

	/**
	 * return the handler orientation use ORIENTATION_VERTICAL or ORIENTATION_HORIZONTAL constants
	 * @return the handler orientation use ORIENTATION_VERTICAL or ORIENTATION_HORIZONTAL constants
	 */
	public String getOrientation()
	{
		return orientation;
	}

	/**
	 * Set the orientation : use ORIENTATION_VERTICAL or ORIENTATION_HORIZONTAL constants
	 * @param orientation use ORIENTATION_VERTICAL or ORIENTATION_HORIZONTAL constants
	 */
	public void setOrientation(String orientation)
	{
		this.orientation = orientation;
	}

	/**
	 * Return the handler size
	 * @return the handler size
	 */
	public int getSizerWidth()
	{
		return sizerWidth;
	}

	/**
	 * set the handler size
	 * @param sizerWidth set the handler size
	 */
	public void setSizerWidth(int sizerWidth)
	{
		this.sizerWidth = sizerWidth;
	}

}
