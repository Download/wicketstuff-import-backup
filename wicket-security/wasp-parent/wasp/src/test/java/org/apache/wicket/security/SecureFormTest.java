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
package org.apache.wicket.security;

import org.apache.wicket.security.pages.secure.FormPage;
import org.apache.wicket.security.components.markup.html.form.SecureForm;
import org.apache.wicket.security.actions.WaspAction;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.TagTester;

import java.util.Map;
import java.util.HashMap;

/**
 * Test links
 *
 * @author marrink
 */
public class SecureFormTest extends WaspAbstractTestBase {

    /**
     * Test secure form component.
     * Form is not available as a whole = invisible.
     */
    public void testSecureFormInvisible()
    {
        doLogin();
        mock.startPage(FormPage.class);
        mock.assertRenderedPage(FormPage.class);
        mock.assertInvisible("form");
        mock.processRequestCycle();
    }

    /**
     * Test secure form component.
     * Form is visible but it is not possible to submit or enter new values.
     */
    public void testSecureFormVisibleDisabled() {
        doLogin();
        Map authorized = new HashMap();
        authorized.put(SecureForm.class, application.getActionFactory().getAction("access render"));
        login(authorized);
        mock.startPage(FormPage.class);
        mock.assertRenderedPage(FormPage.class);
        mock.dumpPage();
        mock.assertVisible("form");
        TagTester tag = mock.getTagByWicketId("form");
        assertEquals("div", tag.getName());
        tag = mock.getTagByWicketId("text");
        assertEquals("disabled", tag.getAttribute("disabled"));
        tag = mock.getTagByWicketId("area");
        assertEquals("disabled", tag.getAttribute("disabled"));
        tag = mock.getTagByWicketId("button");
        assertEquals("disabled", tag.getAttribute("disabled"));
        // fake form submit since the tag can not
        //TODO seems an issue with the wicket internals
        /*
        FormTester form = mock.newFormTester("form");
        try {
            form.setValue("text", "not allowed");
            form.setValue("area", "also not allowed");
            form.submit();
            fail("should not be able to enter data and submit the form");
        } catch (RuntimeException e)
        {
            assertEquals("Expected a UnauthorizedActionException", "org.apache.wicket.authorization.UnauthorizedActionException", e.getCause().getClass().getName());
        } catch (Throwable t) {
            
        }
        mock.processRequestCycle();
        */
    }

    /**
     * Test secure form component.
     * Form is available and able to process values. 
     */
    public void testSecureFormVisibleEnabled() {
        doLogin();
        Map authorized = new HashMap<Class,WaspAction>();
        authorized.put(SecureForm.class, application.getActionFactory().getAction(
                "access render enable"));
        login(authorized);
        mock.startPage(FormPage.class);
        mock.assertRenderedPage(FormPage.class);
        mock.dumpPage();
        mock.assertVisible("form");
        mock.debugComponentTrees();
        TagTester tag = mock.getTagByWicketId("text");
        //TODO this assert fails
        //assertNull(tag.getAttribute("disabled"));
        tag = mock.getTagByWicketId("area");
        //TODO this assert fails
        //assertNull(tag.getAttribute("disabled"));
        tag = mock.getTagByWicketId("button");
        //TODO this assert fails
        //assertNull(tag.getAttribute("disabled"));
        FormTester form = mock.newFormTester("form");        
        form = mock.newFormTester("form");
        form.setValue("text", "allowed");
        form.setValue("area", "also allowed");
        form.submit();
        mock.assertRenderedPage(FormPage.class);
        //TODO this assert fails
        //assertEquals("allowed", mock.getComponentFromLastRenderedPage("form:text").getDefaultModelObject());
        //TODO this assert fails
        //assertEquals("also allowed", mock.getComponentFromLastRenderedPage("form:area").getDefaultModelObject());
    }

}