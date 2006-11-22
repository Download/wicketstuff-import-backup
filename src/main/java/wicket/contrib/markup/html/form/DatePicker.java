package wicket.contrib.markup.html.form;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import wicket.MarkupContainer;
import wicket.behavior.AttributeAppender;
import wicket.contrib.dojo.toggle.DojoToggle;
import wicket.markup.ComponentTag;
import wicket.markup.html.form.FormComponent;
import wicket.markup.html.form.TextField;
import wicket.model.IModel;
import wicket.model.Model;
import wicket.util.convert.ConversionException;

/**
 * @author <a href="http://www.demay-fr.net/blog">Vincent Demay</a>
 *
 */
public class DatePicker extends TextField{
	
	private SimpleDateFormat formatter;
	private String pattern;

	/**
	 * @param parent
	 * @param id
	 * @param model
	 * @param pattern
	 */
	public DatePicker(String id, IModel model, String pattern)
	{
		super(id, model);
		add(new DatePickerHandler());
		this.setOutputMarkupId(true);
		setDatePattern(pattern);
	}
	
	/**
	 * Set the date pattern
	 * @param pattern date pattern example %d/%m/%y
	 */
	public void setDatePattern(String pattern){
		this.pattern = pattern;
		formatter = new SimpleDateFormat(getSimpleDatePattern());
	}
	
	private String getSimpleDatePattern(){
		return pattern.replace("%d", "dd").replace("%Y", "yyyy").replace("%m", "MM");
	}


	protected String getInputType()
	{
		return "text";
	}

	protected void onComponentTag(ComponentTag tag)
	{
		super.onComponentTag(tag);
		if (getValue() != null){
			tag.put("date",  getValue());
		}else {
			tag.put("date", "");
		}
		tag.put("dojoType", "dropdowndatepicker");
		tag.put("dateFormat", this.pattern);
		tag.put("inputName", this.getId());
	}

	/**
	 * Set the date picker effect
	 * @param toggle
	 */
	public void setToggle(DojoToggle toggle){
		this.add(new AttributeAppender("containerToggle", new Model(toggle.getToggle()),""));
		this.add(new AttributeAppender("containerToggleDuration", new Model(toggle.getDuration() + ""),""));
	}

	/**
	 * @see FormComponent#getModelValue()
	 */
	public final String getModelValue()
	{
		if (getModelObject() != null){
			return formatter.format((Date)getModelObject());
		}
		return null;
	}


	protected Object convertValue(String[] value) throws ConversionException
	{
		if (value != null){
			try
			{
				return formatter.parse(value[0]);
			}
			catch (ParseException e)
			{
				new ConversionException(e);
			}
		}
		return null;
	}
}
