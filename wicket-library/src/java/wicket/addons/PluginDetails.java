/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.addons;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Map;

import wicket.addons.ServiceLocator;
import org.apache.commons.collections.map.LRUMap;

import wicket.Page;
import wicket.addons.db.Addon;
import wicket.addons.db.Click;
import wicket.addons.utils.ExternalImage;
import wicket.addons.utils.RatingLink;
import wicket.markup.html.basic.Label;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.link.ExternalLink;
import wicket.markup.html.link.IPageLink;
import wicket.markup.html.link.Link;
import wicket.markup.html.link.PageLink;
import wicket.model.Model;
import wicket.protocol.http.WebSession;

/**
 * @author Juergen Donnerstag
 */
public final class PluginDetails extends BaseHtmlPage /* AuthenticateHtmlPage */
{
    private static transient final Map visits = Collections.synchronizedMap(new LRUMap(1000));
    
    /**
     * Constructor
     * @param parameters
      */
    public PluginDetails(Addon addon)
    {
        super(null, "Wicket-Addons: Addon-Details");

        if (addon == null)
        {
            addon = Addon.Factory.newInstance();
        }
        else
        {
            final String key = ((WebSession)this.getSession()).getHttpSession().getId() + "-" + addon.getId();
            if (!visits.containsKey(key))
            {
                visits.put(key, null);
                
                final Click click = Click.Factory.newInstance();
                click.setAddon(addon);
                click.setSessionId(((WebSession)this.getSession()).getHttpSession().getId());
        		ServiceLocator.instance().getClickService().addClick(click);
            }
        }
        
        add(new Label("addonName", addon.getName()));
        add(new ExternalImage("imgUpdated", new Model("images/updated.gif")));
        add(new ExternalLink("homepage", addon.getHomepage(), "plugin-homepage"));
        add(new ExternalLink("reportUpdate", "xxx.html", "report-update"));

        add(new RatingLink("ratingLink", addon));

        final Addon addonX = addon;
        final Link comments = new PageLink("comments", new IPageLink()
		        {
		            public Page getPage()
		            {
		                return new CommentsPage(addonX);
		            }
		            
		            public Class getPageIdentity()
		            {
		                return CommentsPage.class;
		            }
		        });
        
        add(comments);
        final Integer commentCount = getAddonService().getCommentCount(addon);
        comments.add(new Label("commentCount", new Model(commentCount)));
        
        add(new Label("version", addon.getVersion()));
        add(new Label("description", addon.getDescription()));

        final Link categorySearch = new BookmarkablePageLink("categorySearch", Home.class);
        add(categorySearch);
        categorySearch.add(new Label("category", addon.getCategory().getName()));
        
        add(new Label("license", addon.getLicense()));
        add(new Label("created", new SimpleDateFormat().format(addon.getCreateTime())));
        add(new Label("updated", new SimpleDateFormat().format(addon.getLastUpdated())));

        add(new Label("24h", new Model(new Integer(addon.getClicksLast24H()))));
        add(new Label("week", new Model(new Integer(addon.getClicksLastWeek()))));
        add(new Label("month", new Model(new Integer(addon.getClicksLastMonth()))));
        add(new Label("all", new Model(new Integer(addon.getClicksAll()))));

        add(new Label("homepage2", addon.getHomepage()));
    }
}
