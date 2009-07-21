package org.wicketstuff.jwicket.ui;


import org.apache.wicket.markup.html.resources.JavascriptResourceReference;
import org.wicketstuff.jwicket.JQueryEmbeddedAjaxBehavior;



public abstract class AbstractJqueryUiEmbeddedBehavior extends JQueryEmbeddedAjaxBehavior {

	private static final long serialVersionUID = 1L;

	public AbstractJqueryUiEmbeddedBehavior(final JavascriptResourceReference... requiredLibraries) {
		super(new JavascriptResourceReference(AbstractJqueryUiEmbeddedBehavior.class, "ui.core.js"), requiredLibraries);
	}

}
