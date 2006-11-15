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

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wicket.Component;
import wicket.Request;
import wicket.RequestCycle;
import wicket.WicketRuntimeException;
import wicket.ajax.AbstractDefaultAjaxBehavior;
import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.IAjaxCallDecorator;
import wicket.ajax.calldecorator.AjaxCallDecorator;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.markup.html.WebComponent;
import wicket.markup.html.WebMarkupContainer;
import wicket.util.string.AppendingStringBuffer;
import wicket.util.string.Strings;

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

	/**
	 * Interface which tells what to do when the user has selected something in
	 * the date picker.
	 * 
	 * @author Frank Bille Jensen
	 */
	public static interface ISelectCallback extends Serializable
	{
		/**
		 * Bind the component if needed.
		 * 
		 * @param component
		 *            The component to bind.
		 */
		void bind(Component component);

		/**
		 * Return the javascript which are being invoked when the user clicks on
		 * something in the date picker.
		 * 
		 * @return The javascript which are being invoked when the user clicks
		 *         on something in the date picker.
		 */
		CharSequence handleCallback();
	}
	
	/**
	 * AJAX based {@link ISelectCallback}, which executes an AJAX event when
	 * the user selects something in the date picker.
	 * 
	 * @author Frank Bille Jensen
	 */
	public abstract static class AjaxSelectCallback implements ISelectCallback
	{
		private static final Pattern ajaxScriptPattern = Pattern
				.compile("^var wcall=wicketAjaxGet\\('([^']+)'.*$");
		private static final SimpleDateFormat dateParamFormat = new SimpleDateFormat("yyyy-M-d-H-m");

		private abstract class SelectCallbackBehavior extends AbstractDefaultAjaxBehavior
		{
			public CharSequence getCallbackScript()
			{
				return super.getCallbackScript();
			}
		}

		private SelectCallbackBehavior eventBehavior;

		public final void bind(Component component)
		{
			eventBehavior = new SelectCallbackBehavior()
			{
				private static final long serialVersionUID = 1L;

				protected void respond(AjaxRequestTarget target)
				{
					Request request = RequestCycle.get().getRequest();

					String dateParam = request.getParameter("dateParam");
					Date date = null;

					try
					{
						date = dateParamFormat.parse(dateParam);
					}
					catch (ParseException e)
					{
						throw new WicketRuntimeException(e);
					}

					onEvent(target, date);
				}

				protected IAjaxCallDecorator getAjaxCallDecorator()
				{
					return new AjaxCallDecorator()
					{
						private static final long serialVersionUID = 1L;

						public CharSequence decorateScript(CharSequence script)
						{
							AppendingStringBuffer b = new AppendingStringBuffer();

							Matcher mat = ajaxScriptPattern.matcher(script);
							if (mat.matches())
							{
								String url = mat.group(1);
								String newUrl = url + "&dateParam='+dateParam+'";
								b.append(Strings.replaceAll(script, url, newUrl));
							}
							else
							{
								throw new WicketRuntimeException("Internal error in Wicket");
							}

							return b;
						}

					};
				}
			};

			component.add(eventBehavior);
		}

		public final CharSequence handleCallback()
		{
			return eventBehavior.getCallbackScript();
		}

		protected abstract void onEvent(AjaxRequestTarget target, Date date);
	}

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

		selectCallback.bind(this);

		flatCalendar = new WebMarkupContainer("flatCalendar");
		flatCalendar.setOutputMarkupId(true);
		add(flatCalendar);

		add(new WebComponent("callbackScript")
		{
			private static final long serialVersionUID = 1L;

			protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag)
			{
				AppendingStringBuffer b = new AppendingStringBuffer("\nfunction ");

				String functionName = getCallbackFunctionName();
				b.append(functionName).append("(calendar){\n");
				b.append("\tvar dateParam = calendar.date.getFullYear();\n");
				b.append("\tdateParam += '-';\n");
				b.append("\tdateParam += calendar.date.getMonth()+1;\n");
				b.append("\tdateParam += '-';\n");
				b.append("\tdateParam += calendar.date.getDate();\n");
				b.append("\tdateParam += '-';\n");
				b.append("\tdateParam += calendar.date.getHours();\n");
				b.append("\tdateParam += '-';\n");
				b.append("\tdateParam += calendar.date.getMinutes();\n");

				CharSequence handleCallback = selectCallback.handleCallback();
				b.append("\t").append(handleCallback).append("\n");
				b.append("}\n");

				replaceComponentTagBody(markupStream, openTag, b);
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
