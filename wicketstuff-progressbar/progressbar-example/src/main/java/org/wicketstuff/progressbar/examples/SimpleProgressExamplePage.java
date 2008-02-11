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
package org.wicketstuff.progressbar.examples;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.wicketstuff.progressbar.ProgressBar;
import org.wicketstuff.progressbar.Progression;
import org.wicketstuff.progressbar.ProgressionModel;


/**
 * <p>
 * Simple example of an active progress bar without using a dedicated spring
 * task service.
 * </p>
 *
 * <p>
 * The progress is stored directly in the page and the task is started in a new
 * thread (not desirable!).
 * </p>
 *
 * @author Christopher Hlubek (hlubek)
 *
 */
public class SimpleProgressExamplePage extends PageSupport {

	/**
	 * The current progress is stored here
	 */
	int progress = 0;

	public SimpleProgressExamplePage() {
		final Form form = new Form("form");
		final ProgressBar bar;
		form.add(bar = new ProgressBar("bar", new ProgressionModel() {
			// Get current progress from page field
			@Override
			protected Progression getProgression() {
				return new Progression(progress);
			}
		}) {
			@Override
			protected void onFinished(AjaxRequestTarget target) {
				// Hide progress bar after finish
				setVisible(false);
				// Add some JavaScript after finish
				target.appendJavascript("alert('Task done!')");

				// re-enable button
				Component button = form.get("submit");
				button.setEnabled(true);
				target.addComponent(button);
			}
		});
		// Hide progress bar initially
		bar.setVisible(false);

		form.add(new IndicatingAjaxButton("submit", form) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				// Start the progress bar, will set visibility to true
				bar.start(target);
				// Thread holds reference to page :(
				new Thread() {
					@Override
					public void run() {
						for(int i = 0; i <= 100; i++) {
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) { }
							progress = i;
						}
						// The bar is stopped automatically, if progress is done
					}
				}.start();

				// disable button
				setEnabled(false);
			}
		});
		form.setOutputMarkupId(true);
		add(form);
	}
}
