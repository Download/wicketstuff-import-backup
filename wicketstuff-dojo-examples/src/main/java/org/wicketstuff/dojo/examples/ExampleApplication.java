package org.wicketstuff.dojo.examples;

import org.apache.wicket.ISessionFactory;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;

/**
 * Runs the ExampleApplication when invoked from command line.
 */
public class ExampleApplication extends WebApplication {

	/**
	 * @see wicket.Application#getHomePage()
	 */
	public Class getHomePage() {
		return Index.class;
	}

	/**
	 * @see wicket.protocol.http.WebApplication#getSessionFactory()
	 */
	public ISessionFactory getSessionFactory() {
		return new ISessionFactory() {
			public Session newSession(Request request, Response response) {
				return new ExampleSession(ExampleApplication.this, request, response);
			}
		};
	}
}
