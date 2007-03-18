/*
 * $Id: WicketExampleHeader.java 103 2005-05-27 08:37:29Z eelco12 $
 * $Revision: 103 $
 * $Date: 2005-05-27 01:37:29 -0700 (Fri, 27 May 2005) $
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
package wicket.contrib.examples;

import wicket.markup.html.basic.Label;
import wicket.markup.html.panel.Panel;

/**
 * Navigation panel for the examples project.
 *
 * @author Eelco Hillenius
 */
public final class WicketExampleHeader extends Panel
{
    /**
     * Construct.
     * @param id id of the component
     * @param exampleTitle title of the example
     */
    public WicketExampleHeader(String id, String exampleTitle)
    {
        super(id);
        add(new Label("exampleTitle", exampleTitle));
    }
}
