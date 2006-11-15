/*
 *    Copyright 2006 Wicket Stuff
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package wicket.extensions.markup.html.datepicker;

import java.util.Map;

import wicket.Component;
import wicket.markup.html.WebMarkupContainer;

/**
 * Flat mode version of the DatePicker. This means using the datepicker inline
 * on a page instead of having it popping up.
 * <p>
 * For using this you have to specify {@link ISelectCallback}, which will
 * handle the user selecting something in the date picker. There are two default
 * implementations of this callback: {@link AjaxSelectCallback} and
 * SelectCallback.
 * 
 * @author Frank Bille Jensen
 */
public class FlatDatePicker extends DatePicker
{
	private static final long serialVersionUID = 1L;

	private Component flatCalendar;

	public FlatDatePicker(String id, ISelectCallback selectCallback)
	{
		this(id, selectCallback, new DatePickerSettings());
	}

	public FlatDatePicker(String id, final ISelectCallback selectCallback,
			DatePickerSettings settings)
	{
		super(id, settings);

		if (selectCallback == null)
		{
			throw new IllegalArgumentException("ISelectCallback must not be null.");
		}

//		selectCallback.bind(this);

		flatCalendar = new WebMarkupContainer("flatCalendar");
		flatCalendar.setOutputMarkupId(true);
		add(flatCalendar);

		add(new CallbackScript("callbackScript", selectCallback)
		{
			private static final long serialVersionUID = 1L;

			public String getCallbackFunctionName()
			{
				return FlatDatePicker.this.getCallbackFunctionName();
			}
		});
	}

	private String getCallbackFunctionName()
	{
		return getMarkupId() + "_callback";
	}

	protected void appendSettings(Map settings)
	{
		settings.put("flat", "\"" + flatCalendar.getMarkupId() + "\"");
		settings.put("flatCallback", getCallbackFunctionName());
	}

}
