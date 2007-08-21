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
package org.wicketstuff.jquery.demo.dnd;

import java.util.List;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.wicketstuff.jquery.dnd.DnDSortableHandler;
import org.wicketstuff.jquery.demo.PageSupport;

@SuppressWarnings("serial")
public class Page4MultiGroup extends PageSupport {
    // Data (for demo purpose)
    private static List<MyGroup> myGroups = null;

    static {
        try {
            myGroups = MyFactory.newMyGroupList("multi", 5, 4);
        } catch (RuntimeException exc) {
            throw exc;
        } catch (Exception exc) {
            throw new RuntimeException("wrap: " + exc.getMessage(), exc);
        }
    }

    // Component
    public Page4MultiGroup() throws Exception {
        // define the action on DnD
        final DnDSortableHandler dnd = new DnDSortableHandler("dnd") {
            private int actionCnt_ = 0;
            @Override
            public boolean onDnD(AjaxRequestTarget target, MarkupContainer srcContainer, int srcPos, MarkupContainer destContainer, int destPos) throws Exception {
                // apply modification on model
                MyGroup srcGroup = (MyGroup) srcContainer.getModelObject();
                MyGroup destGroup = (MyGroup) destContainer.getModelObject();
                MyItem myItem = srcGroup.items.remove(srcPos);
                destGroup.items.add(destPos, myItem);

                // update sizes
                actionCnt_++;
                updateContainerHeader(target, srcContainer, srcGroup);
                if (srcContainer != destContainer) {
                    updateContainerHeader(target, destContainer, destGroup);
                }

                // update feedback message
                String msg = String.format("move '%s' from %d to %d", myItem.label, srcPos, destPos);
                FeedbackPanel feedback = (FeedbackPanel) Page4MultiGroup.this.get("feedback");
                feedback.info(msg);
                if (target != null) {
                    target.addComponent(feedback);
                }
                return false;
            }

            private void updateContainerHeader(AjaxRequestTarget target, MarkupContainer container, MyGroup group) throws Exception {
                Label itemCnt = (Label) container.getParent().get("itemCnt");
                itemCnt.setModelObject(group.items.size());
                target.addComponent(itemCnt);

                Label actionCnt = (Label) container.getParent().get("actionCnt");
                actionCnt.setModelObject(actionCnt_);
                target.addComponent(actionCnt);
            }
        };

        // add the DnD handler to the page
        add(dnd);

        add(new ListView("myGroups", myGroups){
            @Override
            protected void populateItem(ListItem listitem) {
                try {
                    listitem.add(new Panel4MyGroup("myGroup", (MyGroup)listitem.getModelObject(), dnd));
                } catch (RuntimeException exc) {
                    throw exc;
                } catch (Exception exc) {
                    throw new RuntimeException("wrap: " + exc.getMessage(), exc);
                }
            }
        });
    }
}
