package org.wicketstuff.jquery.resize;

import org.wicketstuff.jquery.Options;

public class ResizeOptions extends Options {
	public static final String ASPECTRATIO_PRESERVE = "preserve";
	
	public ResizeOptions aspectRatio(String r) {
		set("aspectRatio", r);
		return this;
	}
}
