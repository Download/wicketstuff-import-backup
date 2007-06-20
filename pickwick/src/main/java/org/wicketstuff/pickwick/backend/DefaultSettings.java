package org.wicketstuff.pickwick.backend;

import java.io.File;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.protocol.http.WebRequestCycle;

import com.google.inject.Singleton;

@Singleton
public class DefaultSettings implements Settings {
	public File getImageDirectoryRoot() {
		return new File("src/main/webapp/images");
	}

	public String getBaseURL() {
		// return "http://localhost:8080/";
		return ((WebRequestCycle) RequestCycle.get()).getWebRequest().getHttpServletRequest().getRequestURL()
				.toString();
	}
}
