package org.wicketstuff.dojo.examples.datePicker;

import java.util.Date;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.wicketstuff.dojo.examples.WicketExamplePage;
import org.wicketstuff.dojo.markup.html.form.DojoDatePicker;
import org.wicketstuff.dojo.toggle.DojoExplodeToggle;
import org.wicketstuff.dojo.toggle.DojoFadeToggle;
import org.wicketstuff.dojo.toggle.DojoWipeToggle;

public class DatePickerShower extends WicketExamplePage {
	
	private Date date1;
	private Date date2;
	private Date date3;
	private Date date4;
		
	public DatePickerShower(PageParameters parameters){

		date1 = null;
		date2 = new Date();
		date3 = new Date();
		date4 = new Date(); 
		
		Form form = new Form("dateform");
		
		DojoDatePicker date1P = new DojoDatePicker("date1", new Model(date1), "dd/MM/yyyy");
		date1P.setRequired(true);
		form.add(date1P);
		
		DojoDatePicker date2P = new DojoDatePicker("date2", new Model(date2), "dd/MM/yyyy");
		date2P.setToggle(new DojoWipeToggle(200));
		form.add(date2P);
		
		DojoDatePicker date3P = new DojoDatePicker("date3", new Model(date3), "dd/MM/yyyy");
		date3P.setToggle(new DojoFadeToggle(600));
		form.add(date3P);
		
		DojoDatePicker date4P = new DojoDatePicker("date4", new Model(date4), "dd/MM/yyyy");
		date4P.setToggle(new DojoExplodeToggle());
		form.add(date4P);
		
		this.add(new FeedbackPanel("feedback"));

		this.add(form);
		
	}
}
