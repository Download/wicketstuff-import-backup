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
	 * @param arg0
	 * @param username
	 */
	public loggedInUserPanel(String arg0, String username)
	{
		super(arg0);
		add(new Label("nameLabel", username));
		add(new signOutForm("signOutForm"));
	}

	class signOutForm extends Form {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
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
				setResponsePage(new Frontpage(currentSession
						.getPageParameters()));
			}
		}
	}

}
