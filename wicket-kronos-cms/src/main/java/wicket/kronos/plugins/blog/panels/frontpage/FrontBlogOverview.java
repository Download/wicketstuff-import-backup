package wicket.kronos.plugins.blog.panels.frontpage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import wicket.PageParameters;
import wicket.kronos.frontpage.Frontpage;
import wicket.kronos.plugins.blog.panels.BlogPost;
import wicket.markup.html.basic.Label;
import wicket.markup.html.basic.MultiLineLabel;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.markup.html.panel.Panel;

/**
 * Create an overview of all blog postings
 * 
 * @author postma
 */
public class FrontBlogOverview extends Panel {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 * @param blogposts
	 */
	public FrontBlogOverview(String id, List<BlogPost> blogposts)
	{
		super(id);
		ListView blogpostsList = new ListView("blogposts", blogposts) {

			@Override
			protected void populateItem(ListItem item)
			{
				BlogPost blogpost = (BlogPost) item.getModelObject();

				item.add(new Label("title", blogpost.getTitle()));
				String text = blogpost.getText();
				if (text.length() >= 250)
				{
					text = text.substring(0, 250);
					text += ".....";
				}
				item.add(new MultiLineLabel("text", text).setEscapeModelStrings(false));
				Date date = blogpost.getDate().getTime();
				SimpleDateFormat blogDateFormat = new SimpleDateFormat("dd-MM-yyyy");
				item.add(new Label("date", blogDateFormat.format(date)));
				item.add(new Label("author", blogpost.getAuthor()));
				PageParameters param = new PageParameters();
				param.add("IDType", "content");
				param.add("ID", blogpost.getPostUUID());
				item.add(new BookmarkablePageLink("readMoreLink", Frontpage.class, param));
			}
		};

		add(blogpostsList);
	}
}
