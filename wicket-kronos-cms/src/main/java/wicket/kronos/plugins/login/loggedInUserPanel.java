package wicket.kronos.plugins.login;

import wicket.kronos.KronosSession;
import wicket.kronos.frontpage.Frontpage;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.Form;
import wicket.markup.html.panel.Panel;

/**
 * @author postma
 */
public class loggedInUserPanel extends Panel {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param wicketId
	 * @param username
	 */
	public loggedInUserPanel(String wicketId, String username)
	{
		super(wicketId);
		add(new Label("nameLabel", username));
		add(new signOutForm("signOutForm"));
	}

	class signOutForm extends Form {

		/**
		 * Default serialVersionUID
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor.
		 * 
		 * @param id
		 */
		public signOutForm(String id)
		{
			super(id);
		}

		@Override
		protected void onSubmit()
		{
			KronosSession.get().signout();
			if (!continueToOriginalDestination())
			{
				KronosSession currentSession = KronosSession.get();
				setResponsePage(new Frontpage(currentSession.getPageParameters()));
			}
		}
	}

}
