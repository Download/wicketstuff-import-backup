package wicket.contrib.data.model.hibernate.binding;

import wicket.markup.html.panel.Panel;
import wicket.model.IModel;

/**
 * A panel that shows a check mark or a check box, depending on the state of its
 * enclosing form.
 * 
 * @author Phil Kulak
 */
public class CheckBoxPanel extends Panel
{
	public CheckBoxPanel(String id, IModel model)
	{
		super(id);
		add(new InlineCheckBox("inlineCheckBox", model));
	}
}
