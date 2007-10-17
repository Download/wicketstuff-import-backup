package org.wicketstuff.yui.examples.pages;


import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;
import org.wicketstuff.yui.examples.WicketExamplePage;
import org.wicketstuff.yui.markup.html.menu2.contextMenu.MenuItem;
import org.wicketstuff.yui.markup.html.menu2.contextMenu.YuiContextMenu;
import org.wicketstuff.yui.markup.html.menu2.contextMenu.YuiContextMenuBehavior;

public class ContextMenu2Page extends WicketExamplePage {
	
	public ContextMenu2Page() {
		
		YuiContextMenu testMenu1 = new YuiContextMenu( "testMenu1" );
		
		testMenu1.add( new MenuItem( "Cut"));
		testMenu1.add( new MenuItem( "Copy"));
		testMenu1.add( new MenuItem( "Paste" ));
		
		YuiContextMenu testMenu2 = new YuiContextMenu( "testMenu2" );

		testMenu2.add( new MenuItem( "Red" ));
		testMenu2.add( new MenuItem( "Green" ));
		testMenu2.add( new MenuItem( "Blue" ));
		
		
		YuiContextMenuBehavior cmBehavior = new YuiContextMenuBehavior(testMenu1, testMenu2);
		
		WebMarkupContainer markup =new WebMarkupContainer( "panel" );
		markup.setOutputMarkupId(true);
		
		cmBehavior.applyAttributes( markup, testMenu1, new Model( "123") );
		
		WebMarkupContainer subPanel = new WebMarkupContainer( "subPanel" );
		subPanel.setOutputMarkupId(true);
		cmBehavior.applyAttributes( subPanel, testMenu2, new Model( "777") );
		markup.add( subPanel );

		
		add( markup );
		markup.add( cmBehavior );
	}

}
