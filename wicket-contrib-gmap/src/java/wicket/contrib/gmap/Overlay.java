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
 * @author Iulian-Corneliu Costan
 */
public abstract class Overlay implements Serializable
{

    private GPoint point;

    Overlay(GPoint point)
    {
        if (point == null)
        {
            new IllegalArgumentException("point cannot be null");
        }
        this.point = point;
    }

    /**
     * Get the name of the JavaScript function that will be called in order to create this overlay.
     *
     * @return name of JavaScript function
     */
    public abstract String getFactoryMethod();

    public GPoint getPoint()
    {
        return point;
    }

    public String getPointAsString()
    {
        return point.toString();
    }

    /**
     * Each overlay on the map has to be unique.
     *
     * @return unique ID for each overlay
     */
    protected String getOverlayId()
    {
        return JSUtil.longitudeAsString(point) + JSUtil.latitudeAsString(point);
    }
}
