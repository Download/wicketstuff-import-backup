package org.wicketstuff.pickwick.frontend.pages;

import org.apache.wicket.PageParameters;
import org.wicketstuff.dojo.markup.html.container.DojoSimpleContainer;
import org.wicketstuff.dojo.markup.html.container.layout.DojoLayoutContainer;
import org.wicketstuff.dojo.markup.html.container.layout.DojoLayoutContainer.Position;
import org.wicketstuff.pickwick.frontend.panel.FolderTreePanel;
import org.wicketstuff.pickwick.frontend.panel.SequenceGridPanel;

/**
 * Page to display a sequence with image thumbnails
 * 
 * @author <a href="mailto:jbq@apache.org">Jean-Baptiste Quenot</a>
 */
public class SequencePage extends FrontendBasePage {
	public SequencePage(PageParameters parameters) {
		String uri = parameters.getString("uri");
		if (uri == null) {
			uri = "";
		}
		DojoLayoutContainer layout;
		add(layout = new DojoLayoutContainer("mainArea"));
		layout.setHeight("550px");
		layout.setWidth("100%");
		DojoSimpleContainer s1 = new DojoSimpleContainer("thumbnails");
		s1.add(new SequenceGridPanel("thumbnails", uri));
		layout.add(s1, Position.Client);
		DojoSimpleContainer s2 = new DojoSimpleContainer("treePanel");
		s2.add(new FolderTreePanel("treePanel"));
		s2.setWidth("20%");
		layout.add(s2, Position.Left);
	}
}
