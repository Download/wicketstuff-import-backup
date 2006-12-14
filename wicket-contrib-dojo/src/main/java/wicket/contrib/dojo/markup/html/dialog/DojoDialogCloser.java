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
package wicket.contrib.dojo.markup.html.dialog;

import wicket.AttributeModifier;
import wicket.MarkupContainer;
import wicket.behavior.AttributeAppender;
import wicket.markup.html.link.Link;
import wicket.model.Model;

/**
 * Link to close � DojoDialog
 * @author vdemay
 *
 */
public class DojoDialogCloser extends Link{

	/**
	 * @param parent
	 * @param id
	 * @param dialog
	 */
	public DojoDialogCloser(MarkupContainer parent, String id, DojoDialog dialog)
	{
		super(parent, id);
		String dialogId = dialog.getMarkupId();
		String onClick = "";
		onClick = "javascript:getDialog('" + dialogId + "').hide(); return false;";
		this.add(new AttributeAppender("onClick", new Model<String>(onClick),""));
		this.add(new AttributeModifier("href", new Model<String>("#")));
	}

	/* (non-Javadoc)
	 * @see wicket.markup.html.link.Link#onClick()
	 */
	public void onClick()
	{
		//DO NOTHING
	}

}
