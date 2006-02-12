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
package wicket.contrib.gmap;

import java.io.Serializable;

/**
 * todo to be implemented
 *
 * @author Iulian-Corneliu COSTAN
 */
public class GIcon implements Serializable
{

    private String image;
    private GPoint anchor;

    public GIcon(String image, GPoint anchor)
    {
        this.image = image;
        this.anchor = anchor;
    }

    public String getImage()
    {
        return image;
    }

    public GPoint getAnchor()
    {
        return anchor;
    }
}
