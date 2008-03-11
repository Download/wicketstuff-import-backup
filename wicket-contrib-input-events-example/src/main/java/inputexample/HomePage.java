package inputexample;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;

import wicket.contrib.input.events.EventType;
import wicket.contrib.input.events.InputBehavior;
import wicket.contrib.input.events.key.KeyType;

/**
 * Homepage
 */
public class HomePage extends WebPage {

	private static final long serialVersionUID = 1L;

	// TODO Add any page properties or variables here

	/**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param parameters
	 *            Page parameters
	 */
	public HomePage(final PageParameters parameters) {

		// Add the simplest type of label
		add(new Label("message",
				"If you see this message wicket is properly configured and running"));

		// TODO Add your page's components here

		final IModel labelModel = new Model("nothing yet!");
		final Label label = new Label("id", labelModel);
		label.setOutputMarkupId(true);
		add(label);
		Form form = new Form("form") {
			@Override
			protected void onSubmit() {
				super.onSubmit();
				labelModel.setObject("form was submitted");
			}
		};
		add(form);
		Button button = new Button("button") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				labelModel.setObject("std btn was clicked");
			}
		}.setDefaultFormProcessing(false);
		button.add(new InputBehavior(new KeyType[] { KeyType.b },
				EventType.click));

		form.add(new InputBehavior(new KeyType[] { KeyType.Ctrl, KeyType.a },
				EventType.submit));
		form.add(button);
		Button button2 = new Button("button2").setDefaultFormProcessing(false);
		button2.add(new AjaxEventBehavior("onClick") {
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				labelModel.setObject("ajax was fired");
				target.addComponent(label);
			}
		});
		button2.add(new InputBehavior(new KeyType[] { KeyType.c }));
		form.add(button2);
		Link link = new Link("link") {
			@Override
			public void onClick() {
				labelModel.setObject("link clicked");

			}
		};
		form.add(new TextField("text", new Model("")).add(new InputBehavior(
				new KeyType[] { KeyType.Ctrl,KeyType.f }, EventType.focus){@Override
				protected Boolean getDisable_in_input() {
					//remember this for all input behaviors, elsewise the shortcut will be triggered in the text field
					// not a problem if combination of keys though
					return true;
				}}));
		
		link.add(new InputBehavior(new KeyType[] { KeyType.e }));
		add(link);

	}
}