package wicket.contrib.gmap;

import wicket.AttributeModifier;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.markup.html.WebComponent;

/**
 * Base wicket component that match &ltscript&gt html tag.
 * 
 * @author Nino Martinez Wael (nino.martinez@jayway.dk)
 */
abstract class JavaScriptComponentIEFix extends WebComponent
{

	protected AttributeModifier attributeModifier = null;

	/**
	 * Construct.
	 * 
	 * @param id
	 */
	public JavaScriptComponentIEFix(final String id)
	{
		super(id);
	}

	protected void onComponentTag(final ComponentTag tag)
	{
		checkComponentTag(tag, "div");

	}


	protected void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag)
	{
		String js = onJavaScriptComponentTagBody();
		// fix for buggy IE:(
		String prepend = "<span style='display:none'>This is an invisible tag to prevent buggy IE ignoring the script <script type='text/javascript'>";
		String postpend = "</script></span>";
		prepend += js + postpend;
		js = prepend;

		replaceComponentTagBody(markupStream, openTag, js);
	}

	/**
	 * @return
	 */
	public abstract String onJavaScriptComponentTagBody();
}
