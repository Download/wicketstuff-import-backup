package wicket.contrib.examples.tinymce;

import org.apache.wicket.markup.html.link.Link;

import wicket.contrib.examples.WicketExamplePage;

/**
 * @author Iulian-Corneliu COSTAN
 */
public class TinyMCEBasePage extends WicketExamplePage
{

	private static final long serialVersionUID = 1L;

	public TinyMCEBasePage()
	{
		add(new Link("simple")
		{

			public void onClick()
			{
				setResponsePage(SimpleTinyMCEPage.class);
			}
		});
		add(new Link("advanced")
		{

			public void onClick()
			{
				setResponsePage(AdvancedTinyMCEPage.class);
			}
		});
		add(new Link("full")
		{

			public void onClick()
			{
				setResponsePage(FullFeaturedTinyMCEPage.class);
			}
		});
		add(new Link("word")
		{

			public void onClick()
			{
				setResponsePage(WordTinyMCEPage.class);
			}
		});
		add(new Link("exact")
		{
			
			public void onClick()
			{
				setResponsePage(ExactModeTinyMCEPage.class);
			}
		});
		add(new Link("ajax")
		{
			
			public void onClick()
			{
				setResponsePage(AjaxTinyMCEPage.class);
			}
		});
	}
}
