package org.wicketstuff.mergedresources;

import org.apache.wicket.Application;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;

public abstract class AbstractTestApplication extends WebApplication
{    
    /**
     * Constructor
     */
	public AbstractTestApplication()
	{
	}
	
	@Override
	protected void init() {
		// still using deprecated property for CSS
		getResourceSettings().setStripJavascriptCommentsAndWhitespace(strip());
		
		//getResourceSettings().setAddLastModifiedTimeToResourceReferenceUrl(true);
		
		if (merge()) {
			mountResources();
		}
	}

	protected abstract void mountResources();


	protected boolean strip() {
		return true;
	}

	protected boolean merge() {
		return true;
	}

	/**
	 * @see wicket.Application#getHomePage()
	 */
	public Class<? extends WebPage> getHomePage()
	{
		return HomePage.class;
	}

	@Override
	public String getConfigurationType() {
		return Application.DEPLOYMENT;
	}
}