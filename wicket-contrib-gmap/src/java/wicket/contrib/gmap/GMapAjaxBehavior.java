package wicket.contrib.gmap;

import wicket.ajax.AbstractDefaultAjaxBehavior;
import wicket.ajax.AjaxRequestTarget;


/**
 * @author Iulian-Corneliu Costan
 */
class GMapAjaxBehavior extends AbstractDefaultAjaxBehavior
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see wicket.ajax.AbstractDefaultAjaxBehavior#respond(wicket.ajax.AjaxRequestTarget)
	 */
	protected void respond(AjaxRequestTarget target)
	{

		GMarkerContainer component = (GMarkerContainer)getComponent();
		component.toggleVisibility();

		target.addComponent(component);
	}

}
