package org.wicketstuff.scriptaculous.inplaceeditor;

import junit.framework.TestCase;

import org.apache.wicket.util.tester.WicketTester;

public class AjaxEditInPlaceLabelTest extends TestCase {

	public void testModelIsNotEscaped() {
		WicketTester tester = new WicketTester();
		tester.startPanel(TestPanel.class);

		tester.assertContains("me & you");
	}
}
