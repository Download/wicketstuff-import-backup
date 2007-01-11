package wicket.kronos;

import wicket.markup.html.WebPage;

/**
 * Base class for all pages in the Kronos application. Any page which subclasses this page can get
 * session properties from KronosSession via getKronosSession().
 */
public abstract class KronosPage extends WebPage {

	/**
	 * Get casted session object as KronosSession object
	 * 
	 * @return KronosSession
	 */
	public KronosSession getKronosSession()
	{
		return (KronosSession) getSession();
	}
}
