/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package wicket.contrib.tinymce.settings;

/**
 * This plugin gives the user a warning if they made modifications to a editor instance but didn't submit them.
 * This plugin will most likely be extended in the future to provide AJAX auto save support.
 *
 * @author Iulian-Corneliu COSTAN
 */
public class AutoSavePlugin extends Plugin {

    public AutoSavePlugin() {
        super("autosave");
    }
}
