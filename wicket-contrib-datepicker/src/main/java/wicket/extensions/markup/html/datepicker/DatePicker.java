/*
 *    Copyright 2005 Wicket Stuff
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import wicket.Component;
import wicket.ResourceReference;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.markup.html.WebComponent;
import wicket.markup.html.panel.Panel;
import wicket.markup.html.resources.CompressedPackageResourceReference;
import wicket.markup.html.resources.JavaScriptReference;
import wicket.markup.html.resources.StyleSheetReference;
import wicket.model.Model;
import wicket.util.convert.converters.DateConverter;
import wicket.util.string.AppendingStringBuffer;

/**
 * Datepicker component. It has two modes: A {@link PopupDatePicker popup} and a
 * {@link FlatDatePicker flat} mode.
 * <p>
 * Customize the looks, localization etc of the datepicker by providing a custom
 * {@link wicket.extensions.markup.html.datepicker.DatePickerSettings} object.
 * </p>
 * <p>
 * This component is based on Dynarch's JSCalendar component, which can be found
 * at <a href="http://www.dynarch.com/">the Dynarch site</a>.
 * </p>
 * 
 * @see wicket.extensions.markup.html.datepicker.DatePickerSettings
 * @author Frank Bille Jensen
 * @author Eelco Hillenius
 * @author Mihai Bazon (creator of the JSCalendar component)
 */
public abstract class DatePicker extends Panel
{
	private static final long serialVersionUID = 1L;

	private static final ResourceReference JAVASCRIPT = new CompressedPackageResourceReference(DatePicker.class, "calendar.js");
	private static final ResourceReference JAVASCRIPT_SETUP = new CompressedPackageResourceReference(DatePicker.class, "calendar-setup.js");

	/**
	 * Outputs the Javascript initialization code.
	 */
	private final class InitScript extends WebComponent
	{
		private static final long serialVersionUID = 1L;

		/**
		 * Construct.
		 * 
		 * @param id
		 *            component id
		 */
		public InitScript(String id)
		{
			super(id);
		}

		/**
		 * @see wicket.Component#onComponentTagBody(wicket.markup.MarkupStream,
		 *      wicket.markup.ComponentTag)
		 */
		protected void onComponentTagBody(final MarkupStream markupStream,
				final ComponentTag openTag)
		{
			replaceComponentTagBody(markupStream, openTag, getInitScript());
		}
	}
	
	protected abstract class CallbackScript extends WebComponent
	{
		private static final long serialVersionUID = 1L;

		private ISelectCallback selectCallback;
		
		/**
		 * Construct.
		 * @param id Component id.
		 */
		public CallbackScript(String id, ISelectCallback selectCallback)
		{
			super(id);
			
			this.selectCallback = selectCallback;
			
			if (selectCallback != null)
			{
				selectCallback.bind(this);
			}
		}
		
		protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag)
		{
			if (selectCallback != null)
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
		}
		
		public boolean isVisible()
		{
			return selectCallback != null;
		}

		public abstract String getCallbackFunctionName();
	}

	/** settings for the javascript datepicker component. */
	private final DatePickerSettings settings;

	private DateConverter dateConverter;
	
	private CallbackScript onSelect;
	private CallbackScript onClose;
	private CallbackScript onUpdate;

	public DatePicker(final String id, final DatePickerSettings settings)
	{
		super(id);

		if (settings == null)
		{
			throw new IllegalArgumentException(
					"Settings must be non null when using this constructor");
		}

		this.settings = settings;

		add(new InitScript("script"));
		add(new JavaScriptReference("calendarMain", JAVASCRIPT));
		add(new JavaScriptReference("calendarSetup", JAVASCRIPT_SETUP));
		add(new JavaScriptReference("calendarLanguage", new Model()
		{
			private static final long serialVersionUID = 1L;

			public Object getObject(Component component)
			{
				return settings.getLanguage(DatePicker.this.getLocale());
			}
		}));
		add(new StyleSheetReference("calendarStyle", settings.getStyle()));
		
		// Add callbacks
		add(onSelect = new CallbackScript("onSelect", settings.getOnSelect())
		{
			private static final long serialVersionUID = 1L;

			public String getCallbackFunctionName()
			{
				return DatePicker.this.getMarkupId() + "_onselect";
			}
		});
		
		add(onClose = new CallbackScript("onClose", settings.getOnClose())
		{
			private static final long serialVersionUID = 1L;

			public String getCallbackFunctionName()
			{
				return DatePicker.this.getMarkupId() + "_onclose";
			}
		});
		
		add(onUpdate = new CallbackScript("onUpdate", settings.getOnUpdate())
		{
			private static final long serialVersionUID = 1L;

			public String getCallbackFunctionName()
			{
				return DatePicker.this.getMarkupId() + "_onupdate";
			}
		});
	}

	/**
	 * Sets the date converter to use for generating the javascript format
	 * string. If this is not set or set to null the default DateConverter will
	 * be used.
	 * 
	 * @param dateConverter
	 */
	public void setDateConverter(DateConverter dateConverter)
	{
		this.dateConverter = dateConverter;
	}

	/**
	 * Gets the initilization javascript.
	 * 
	 * @return the initilization javascript
	 */
	private CharSequence getInitScript()
	{
		Map additionalSettings = new HashMap();
		appendSettings(additionalSettings);

		AppendingStringBuffer b = new AppendingStringBuffer("\nCalendar.setup(\n{");

		// Append specific settings
		for (Iterator iter = additionalSettings.keySet().iterator(); iter.hasNext();)
		{
			String option = (String)iter.next();

			b.append("\n\t\t").append(option).append(" : ").append(additionalSettings.get(option))
					.append(",");
		}
		
		// Callbacks
		if (settings.getOnClose() != null) 
		{
			b.append("\n\t\tonClose : ").append(onClose.getCallbackFunctionName()).append(",");
		}
		if (settings.getOnSelect() != null) 
		{
			b.append("\n\t\tonSelect : ").append(onSelect.getCallbackFunctionName()).append(",");
		}
		if (settings.getOnUpdate() != null) 
		{
			b.append("\n\t\tonUpdate : ").append(onUpdate.getCallbackFunctionName()).append(",");
		}
		
		String pattern = null;
		if (dateConverter == null)
		{
			dateConverter = getDateConverter();
		}
		DateFormat df = dateConverter.getDateFormat(getDatePickerLocale());
		if (df instanceof SimpleDateFormat)
		{
			pattern = ((SimpleDateFormat)df).toPattern();
		}
		b.append(settings.toScript(getDatePickerLocale(), pattern));
		
		int last = b.length() - 1;
		if (',' == b.charAt(last))
		{
			b.deleteCharAt(last);
		}
		b.append("\n});");
		return b;
	}

	protected abstract void appendSettings(Map/* <String,String> */settings);

	protected DateConverter getDateConverter()
	{
		if (dateConverter == null)
		{
			dateConverter = new DateConverter();
		}

		return dateConverter;
	}

	protected Locale getDatePickerLocale()
	{
		return getLocale();
	}
}
