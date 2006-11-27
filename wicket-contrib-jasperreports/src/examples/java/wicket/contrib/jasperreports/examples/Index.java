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
package wicket.contrib.jasperreports.examples;

import wicket.contrib.examples.WicketExamplePage;
import wicket.markup.html.link.PageLink;

/**
 * Jasper reports example index page.
 *
 * @author Eelco Hillenius
 * @author <a href="mailto:jlee at antwerkz.com">Justin Lee</a>
 */
public class Index extends WicketExamplePage {
    /**
     * Constructor
     */
    public Index() {
        add(new PageLink("simple", SimplePdfPage.class));
        add(new PageLink("links", ReportLinksPage.class));
    }
}