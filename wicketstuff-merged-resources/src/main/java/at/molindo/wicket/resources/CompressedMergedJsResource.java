/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.molindo.wicket.resources;

import java.util.Locale;

import org.apache.wicket.Application;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.string.JavascriptStripper;

public class CompressedMergedJsResource extends CompressedMergedResource {

	private static final long serialVersionUID = 1L;

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory
			.getLogger(CompressedMergedJsResource.class);

	public CompressedMergedJsResource(Class<?> scope, final String path,
			final Locale locale, final String style, final Class<?>[] scopes,
			final String[] files, int cacheDuration) {
		super(scope, path, locale, style, scopes, files, cacheDuration);
	}

	@Override
	protected IResourceStream newResourceStream(final Locale locale,
			final String style, final Class<?>[] scopes, final String[] files) {
		return new MergedResourceStream(scopes, files, locale, style) {
			private static final long serialVersionUID = 1L;

			@Override
			protected String toContent(final String content) {
				try {
					if (Application.get().getResourceSettings()
							.getStripJavascriptCommentsAndWhitespace()) {
						
						// strip comments and whitespace
						return JavascriptStripper
								.stripCommentsAndWhitespace(content);
					} else {
						// don't strip the comments, just return original input
						return content;
					}
				} catch (Exception e) {
					log.error("Error while stripping content", e);
					return content;
				}
			}
		};
	}
}
