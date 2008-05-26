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
package org.wicketstuff.objectautocomplete;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Wicket component for selecting a single object of type T with an identifier of type I via
 * autocompletion. The textfield used for autocompletion is nothing more than a <em>search field</em>
 * where the autocomplete menu presents the search results. Selecting an entry of the search results
 * (either via keyboard or mouse click) "picks" an entity, whose id is stored in the model
 * of this component (So, the model object type of this model is I).
 *
 * A subclass must provide the list of T-objects, which are presented in the autocompletion menu.
 *
 * (TODO: Detailed usage example)
 *
 * @author roland
 * @since May 21, 2008
 */
public class ObjectAutoCompleteField<T,I> extends Panel<I>
        implements ObjectAutoCompleteCancelListener {

    // Additional lists of components to update when an object has been selected
    private List<Component<?>> componentsToUpdate = new ArrayList<Component<?>>();

    // Remember old id in case a search operation is aborted
    private I backupObject;

    // Textfield used to search for the object
    private TextField<String> searchTextField;

    /**
     * Package scoper constructor to be used by the builder to create an auto completion fuild via
     * the builder pattern. I.e. use {@link org.wicketstuff.objectautocomplete.ObjectAutoCompleteBuilder#build(String)}
     * for creating an object auto completion component
     *
     * @param pId id of the component to add
     * @param pModel the model
     * @param pBuilder builder object used to create this field.
     */
    ObjectAutoCompleteField(String pId, IModel<I> pModel,ObjectAutoCompleteBuilder<T> pBuilder) {
        super(pId, pModel);

        setOutputMarkupId(true);

        // Register ourself as a cancel listener to restore asn old id
        pBuilder.cancelListener(this);

        // Register all update listener as added to the builder
        for (Component comp : pBuilder.updateOnModelChangeComponents) {
            registerForUpdateOnModelChange(comp);
        }
        // Search Text model contains the text selected
        Model<String> searchTextModel = new Model<String>();
        addSearchTextField(searchTextModel, pBuilder);
        addReadOnlyPanel(searchTextModel,pBuilder);
    }

    /**
     * Register a component that needs to be updated when the model changes, i.e.
     * the use selected an object from the suggestion list
     *
     * @param pComponentToUpdate the component to update
     */
    public void registerForUpdateOnModelChange(Component<?> pComponentToUpdate) {
        componentsToUpdate.add(pComponentToUpdate);
    }

    // ==========================================================================================================

    // the 'search part' if in lookup mode
    private void addSearchTextField(final Model<String> pSearchTextModel, ObjectAutoCompleteBuilder<T> pBuilder) {
        searchTextField = new TextField<String>("search",pSearchTextModel) {
            @Override
            public boolean isVisible() {
                return isSearchMode();
            }
        };
        searchTextField.setOutputMarkupId(true);
        // this disables Firefox autocomplete
        searchTextField.add(new SimpleAttributeModifier("autocomplete", "off"));

        final HiddenField<I> objectField = new HiddenField<I>("hiddenId",getModel());
        objectField.setOutputMarkupId(true);
        add(objectField);

        searchTextField.add(
                pBuilder.buildBehavior(objectField),
                newFormUpdateBehaviour(searchTextField)
        );

        add(searchTextField);
    }

    // the 'read only part' if the object has been selected
    private void addReadOnlyPanel(final Model<String> pSearchTextModel, ObjectAutoCompleteBuilder<T> pBuilder) {
        final WebMarkupContainer wac = new WebMarkupContainer("readOnlyPanel") {
            @Override
            public boolean isVisible() {
                return !isSearchMode();
            }
        };
        wac.setOutputMarkupId(true);

        Label<String> selectedLabel = new Label<String>("selectedValue",pSearchTextModel);
        selectedLabel.setOutputMarkupId(true);

        AjaxFallbackLink deleteLink = new AjaxFallbackLink("searchLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                changeToSearchMode(target);
            }
        };

        Component linkImage = new Image<Void>("searchLinkImage").setVisible(false);
        if (pBuilder.imageResource != null || pBuilder.imageResourceReference != null) {
            linkImage = pBuilder.imageResource != null ?
                    new Image<Void>("searchLinkImage",pBuilder.imageResource) :
                    new Image<Void>("searchLinkImage",pBuilder.imageResourceReference);
            deleteLink.add(new Label(ObjectAutoCompleteBuilder.SEARCH_LINK_PANEL_ID).setVisible(false));
        } else if (pBuilder.searchLinkContent != null) {
            deleteLink.add(pBuilder.searchLinkContent);
        } else {
            deleteLink.add(new Label(ObjectAutoCompleteBuilder.SEARCH_LINK_PANEL_ID,pBuilder.searchLinkText));
        }
        deleteLink.add(linkImage);

        if (pBuilder.searchOnClick) {
            deleteLink.setVisible(false);
            selectedLabel.add(new AjaxEventBehavior("onclick") {
                @Override
                protected void onEvent(AjaxRequestTarget target) {
                    changeToSearchMode(target);
                }
            });
        }
        wac.add(selectedLabel);
        wac.add(deleteLink);
        add(wac);
    }

    private void changeToSearchMode(AjaxRequestTarget target) {
        backupObject = ObjectAutoCompleteField.this.getModelObject();
        ObjectAutoCompleteField.this.setModelObject(null);
        if (target != null) {
            target.addComponent(ObjectAutoCompleteField.this);
            String id = searchTextField.getMarkupId();
            target.appendJavascript(
                    "wicketGet('" +id +"').focus();" +
                            "wicketGet('" + id + "').select();");
        }
    }

    /**
     * Callback called in case the user cancels a search via 'escape'
     *
     * @param pTarget target to which the components to update are added
     */
    public void searchCanceled(AjaxRequestTarget pTarget) {
        if (backupObject != null) {
            setModelObject(backupObject);
            pTarget.addComponent(ObjectAutoCompleteField.this);
        } else {
            searchTextField.clearInput();
            pTarget.addComponent(searchTextField);
        }
        updateDependentComponents(pTarget);
    }

    // mode detection based on the existance of a seleced model
    private boolean isSearchMode() {
        return getModelObject() == null;
    }


    // update the model, when a selection was triggered in the autocomplete
    // menu
    private AjaxFormSubmitBehavior newFormUpdateBehaviour(final TextField<String> pSearchTextField) {
        return new AjaxFormSubmitBehavior("onchange")
        {
            @Override
            protected void onSubmit(AjaxRequestTarget pTarget) {
                pSearchTextField.updateModel();
                pSearchTextField.clearInput();
                pTarget.addComponent(ObjectAutoCompleteField.this);
                updateDependentComponents(pTarget);
            }

            @Override
            protected void onError(AjaxRequestTarget pTarget) {
            }
        };
    }

    @Override
    // ensure that this component is embedded in a <span> ... </span>
    protected void onComponentTag(ComponentTag pTag) {
        super.onComponentTag(pTag);
        pTag.setName("span");
    }

    // add all registered components to the target for update
    private void updateDependentComponents(AjaxRequestTarget pTarget) {
        for (Component comp : componentsToUpdate) {
            pTarget.addComponent(comp);
        }
    }
}