/*
 * ==================================================================== Licensed
 * under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the
 * License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.contrib.jasperreports.examples;

import java.io.File;
import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JRDataSource;
import wicket.Component;
import wicket.contrib.examples.WicketExamplePage;
import wicket.contrib.jasperreports.JRCsvResource;
import wicket.contrib.jasperreports.JRHtmlResource;
import wicket.contrib.jasperreports.JRImageResource;
import wicket.contrib.jasperreports.JRPdfResource;
import wicket.contrib.jasperreports.JRRtfResource;
import wicket.contrib.jasperreports.JRTextResource;
import wicket.markup.html.link.ResourceLink;
import wicket.protocol.http.WebApplication;

/**
 * Simple Jasper reports example
 *
 * @author Eelco Hillenius
 * @author Justin Lee
 */
public class ReportLinksPage extends WicketExamplePage {
    /**
     * Constructor.
     */
    public ReportLinksPage() {
        ServletContext context = ((WebApplication)getApplication()).getWicketServlet().getServletContext();
        final File reportFile = new File(context.getRealPath("/reports/example.jrxml"));

        // Override getReportDataSource to avoid serializing the data source to the session
        add(new ResourceLink("linkToPdf", new JRPdfResource(reportFile) {
            public JRDataSource getReportDataSource() {
                return new ExampleDataSource();
            }
        }));

        add(new ResourceLink("linkToRtf", new JRRtfResource(reportFile) {
            public JRDataSource getReportDataSource() {
                return new ExampleDataSource();
            }
        }));

        add(new ResourceLink("linkToHtml", new JRHtmlResource(reportFile) {
            public JRDataSource getReportDataSource() {
                return new ExampleDataSource();
            }
        }));

        add(new ResourceLink("linkToText", new JRTextResource(reportFile) {
            public JRDataSource getReportDataSource() {
                return new ExampleDataSource();
            }
        }));

        JRImageResource jrImageResource = new JRImageResource(reportFile) {
            public JRDataSource getReportDataSource() {
                return new ExampleDataSource();
            }
        };
        // defaults to png but you can change that by setting the format
        jrImageResource.setFormat("jpg");
        add(new ResourceLink("linkToImage", jrImageResource));

        add(new ResourceLink("linkToCsv", new JRCsvResource(reportFile) {
            public JRDataSource getReportDataSource() {
                return new ExampleDataSource();
            }
        }));
    }

    /**
     * @see Component#isVersioned()
     */
    public boolean isVersioned() {
        return false;
    }
}