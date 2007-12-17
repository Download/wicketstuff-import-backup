package wicket.contrib.scriptaculous.examples;

import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;

/**
 *
 */
public class ScriptaculousExamplesApplication extends WebApplication
{

	/**
	 * @return class
	 */
	public Class getHomePage()
	{
		return ScriptaculousExamplesHomePage.class;
	}

	public Session newSession(Request request, Response response)
	{
		return new ScriptaculousExamplesSession(
				ScriptaculousExamplesApplication.this, request);
	}

	protected void init()
	{
		super.init();
		getResourceSettings().setThrowExceptionOnMissingResource(false);
		getMarkupSettings().setAutomaticLinking(true);
	}
}
