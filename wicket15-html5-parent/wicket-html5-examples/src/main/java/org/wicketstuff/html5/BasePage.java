/*
 * Created by IntelliJ IDEA.
 * User: kinabalu
 * Date: Jan 31, 2010
 * Time: 8:21:02 PM
 */
package org.wicketstuff.html5;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 *
 * @author Andrew Lombardi
 */
public class BasePage extends WebPage {

    public BasePage() {}
    
    public BasePage(final PageParameters parameters) {
    	super(parameters);
    }
    
}