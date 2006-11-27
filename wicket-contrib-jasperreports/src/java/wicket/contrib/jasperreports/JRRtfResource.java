/*
 * $Id: JRRtfResource.java 627 2006-03-20 07:12:13 +0000 (Mon, 20 Mar 2006)
 * eelco12 $ $Revision$ $Date: 2006-03-20 07:12:13 +0000 (Mon, 20 Mar
 * 2006) $
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.contrib.jasperreports;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRRtfExporter;

/**
 * Resource class for jasper reports RTF resources.
 *
 * @author Eelco Hillenius
 * @author Justin Lee
 */
public class JRRtfResource extends JRResource {
    private static final long serialVersionUID = 1L;

    /**
     * Construct without a report. You must provide a report before you can use this resource.
     */
    public JRRtfResource() {
        super();
    }

    /**
     * Construct.
     *
     * @param report the report input stream
     */
    public JRRtfResource(InputStream report) {
        super(report);
    }

    /**
     * Construct.
     *
     * @param report the report input stream
     */
    public JRRtfResource(JasperReport report) {
        super(report);
    }

    /**
     * Construct.
     *
     * @param report the report input stream
     */
    public JRRtfResource(URL report) {
        super(report);
    }

    /**
     * Construct.
     *
     * @param report the report input stream
     */
    public JRRtfResource(File report) {
        super(report);
    }

    public JRAbstractExporter newExporter() {
        return new JRRtfExporter();
    }

    public String getContentType() {
        return "text/rtf";
    }

    public String getExtension() {
        return "rtf";
    }
}
