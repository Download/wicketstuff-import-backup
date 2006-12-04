package wicket.authentication.panel;

import wicket.authentication.AuthenticatedWebSession;
import wicket.kronos.Frontpage;
import wicket.kronos.KronosSession;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.PasswordTextField;
import wicket.markup.html.form.TextField;
import wicket.markup.html.panel.Panel;
import wicket.model.PropertyModel;
import wicket.util.value.ValueMap;

/**
 * @author roeloffzen
 */
public class SignInPanel extends Panel {
	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/** Field for password. */
	private PasswordTextField password = null;

	/** Field for user name. */
	private TextField username = null;

	/**
	 * Sign in form.
	 */
	public final class SignInForm extends Form {
		private static final long serialVersionUID = 1L;

		/** model for form. */
		private final ValueMap properties = new ValueMap();

		/**
		 * Constructor.
		 * 
		 * @param id
		 *            id of the form component
		 */
		public SignInForm(final String id)
		{
			super(id);

			// Attach textfield components that edit properties map
			// in lieu of a formal beans model
			add(username = new TextField("username", new PropertyModel(
					properties, "username")));
			add(password = new PasswordTextField("password", new PropertyModel(
					properties, "password")));
		}

		/**
		 * @see wicket.markup.html.form.Form#onSubmit()
		 */
		@Override
		public final void onSubmit()
		{
			if (signIn(getUsername(), getPassword()))
			{
				// If login has been called because the user was not yet
				// logged in, than continue to the original destination,
				// otherwise to the Home page
				if (!continueToOriginalDestination())
				{
					KronosSession currentSession = KronosSession.get();
					setResponsePage(new Frontpage(currentSession
							.getPageParameters()));
				}
			} else
			{
				// Try the component based localizer first. If not found try the
				// application localizer. Else use the default
				error(getLocalizer().getString("signInFailed", this,
						"Sign in failed"));
			}
		}
	}

	/**
	 * @param id
	 */
	public SignInPanel(String id)
	{
		super(id);
		add(new SignInForm("signInForm"));
	}

	/**
	 * Convenience method to access the password.
	 * 
	 * @return The password
	 */
	public String getPassword()
	{
		return this.password.getModelObjectAsString();
	}

	/**
	 * Convenience method to access the username.
	 * 
	 * @return The user name
	 */
	public String getUsername()
	{
		return this.username.getModelObjectAsString();
	}

	/**
	 * Sign in user if possible.
	 * 
	 * @param username
	 *            The username
	 * @param password
	 *            The password
	 * @return True if signin was successful
	 */
	public boolean signIn(String username, String password)
	{
		assert (username != null);
		assert (password != null);
		return AuthenticatedWebSession.get().signIn(username, password);
	}
}
