package wicket.contrib.tinymce;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.util.string.JavascriptUtils;

import wicket.contrib.tinymce.settings.TinyMCESettings;

/**
 * @author Iulian-Corneliu Costan (iulian.costan@gmail.com)
 */
public class TinyMceBehavior extends AbstractDefaultAjaxBehavior
{
	private static final long serialVersionUID = 1L;

	/** tinymce editor' settings */
	private TinyMCESettings settings;

	/** reference to the tinymce javascript file. */
	private ResourceReference reference = new ResourceReference(TinyMCEPanel.class,
			"tiny_mce/tiny_mce_src.js");

	/** not used yet, think about it * */
	private boolean ajax;

	/**
	 * Construct.
	 * 
	 * @param ajax
	 *            if ajax enable or not
	 */
	public TinyMceBehavior(boolean ajax)
	{
		this(new TinyMCESettings(), ajax);
	}

	/**
	 * Construct.
	 * 
	 * @param settings
	 * @param ajax
	 *            TODO
	 */
	public TinyMceBehavior(TinyMCESettings settings, boolean ajax)
	{
		this.settings = settings;
		this.ajax = ajax;
	}

	/**
	 * @see org.apache.wicket.behavior.AbstractAjaxBehavior#renderHead(org.apache.wicket.markup.html.IHeaderResponse)
	 */
	public void renderHead(IHeaderResponse response)
	{
		// add wicket-ajax support
		super.renderHead(response);

		// since wicket modifies the src attribute of a pre-loaded script tag
		// then we
		// need this workaround to safely import tinymce script

		// import script
		StringBuilder importBuilder = new StringBuilder();
		importBuilder.append("var script = document.createElement('script');\n");
		importBuilder.append("script.id='tinyMCEScript';\n");
		importBuilder.append("script.src='" + RequestCycle.get().urlFor(reference) + "';\n");
		importBuilder.append("script.type='text/javascript';\n");
		importBuilder.append("document.getElementsByTagName('head')[0].appendChild(script);\n");
		response.renderJavascript(importBuilder.toString(), "import");

		// init script
		StringBuilder initBuilder = new StringBuilder();
		initBuilder.append("tinyMCE.init({" + settings.toJavaScript(ajax) + "\n});\n");
		initBuilder.append(settings.getLoadPluginJavaScript());
		initBuilder.append(settings.getAdditionalPluginJavaScript());
		response.renderJavascript(initBuilder.toString(), "init");
	}

	protected void onComponentRendered()
	{
		if (ajax)
		{
			// load editor script
			StringBuilder builder = new StringBuilder();
			builder.append(getCallbackScript());
			JavascriptUtils.writeJavascript(getComponent().getResponse(), builder.toString(),
					"load");
		}
	}

	protected void respond(AjaxRequestTarget target)
	{
		StringBuilder builder = new StringBuilder();
		builder.append("tinyMCE.onLoad();\n");
		builder.append("tinyMCE.execCommand('mceAddControl', true, '"
				+ getComponent().getMarkupId() + "');");

		target.appendJavascript(JavascriptUtils.SCRIPT_OPEN_TAG);
		target.appendJavascript(builder.toString());
		target.appendJavascript(JavascriptUtils.SCRIPT_CLOSE_TAG);
	}

	protected void onComponentTag(ComponentTag tag)
	{
		if (!ajax)
		{
			tag.put("class", "mceEditor");
		}
	}

	protected void onBind()
	{
		getComponent().setOutputMarkupId(true);
	}

}
