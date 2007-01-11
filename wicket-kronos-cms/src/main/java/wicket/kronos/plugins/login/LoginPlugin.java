package wicket.kronos.plugins.login;

import wicket.authentication.User;
import wicket.authentication.panel.SignInPanel;
import wicket.kronos.plugins.IPlugin;
import wicket.kronos.KronosSession;

/**
 * A simple plugin that uses the signInPanel from the authentication package if
 * no user is logged in and an loggedInUserPanel if a user is logged in.
 * 
 * @author roeloffzen
 */
public class LoginPlugin extends IPlugin {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default plugin constructor.
	 * 
	 * @param isAdmin
	 * @param pluginUUID
	 * @param pluginname
	 * @param ispublished
	 * @param order
	 * @param areaposition
	 * @param pluginType
	 */
	public LoginPlugin(Boolean isAdmin, String pluginUUID, String pluginname,
			Boolean ispublished, Integer order, Integer areaposition,
			String pluginType)
	{
		super(isAdmin, pluginUUID, pluginname, ispublished, order,
				areaposition, pluginType);
		if(isAdmin)
		{
			add(new LoginAdminPanel("signin", pluginUUID));
		}else {
			if (KronosSession.get().isSignedIn())
			{
				User user = KronosSession.get().getSignedInUser();
				add(new loggedInUserPanel("signin", user.getName()));
			} else
			{
				add(new SignInPanel("signin"));
			}
		}
	}

	@Override
	public boolean isConfigurable()
	{
		return false;
	}

}
