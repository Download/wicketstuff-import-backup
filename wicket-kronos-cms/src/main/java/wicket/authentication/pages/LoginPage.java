package wicket.authentication.pages;

import wicket.authentication.panel.SignInPanel;
import wicket.markup.html.WebPage;
import wicket.markup.html.panel.FeedbackPanel;

/**
 * @author postma
 */
public class LoginPage extends WebPage {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Add a new SignInPanel and an new FeaadbackPanel
	 */
	public LoginPage()
	{
		add(new SignInPanel("signInPanel"));
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
	}
}