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

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import wicket.contrib.examples.WicketExamplePage;
import wicket.contrib.jasperreports.EmbeddedJasperReportsReport;
import wicket.contrib.jasperreports.JasperReportsResource;
import wicket.protocol.http.WebApplication;

/**
 * Simple Jasper reports example with PDF output and a jasper reports panel..
 * 
 * @author Eelco Hillenius
 */
public class SimplePdfPage extends WicketExamplePage
{
	/**
	 * Constructor.
	 */
	public SimplePdfPage()
	{
		ServletContext context = ((WebApplication) getApplication()).getWicketServlet()
				.getServletContext();
		final File reportFile = new File(context.getRealPath("/reports/example.jasper"));
		final Map parameters = new HashMap();
		JasperReportsResource pdfResource = new JasperReportsResource(reportFile)
				.setReportParameters(parameters).setReportDataSource(
						new ExampleDataSource());
		add(new EmbeddedJasperReportsReport("report", pdfResource));
	}

	/**
	 * @see wicket.Component#isVersioned()
	 */
	public boolean isVersioned()
	{
		return false;
	}
}