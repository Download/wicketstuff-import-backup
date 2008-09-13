/*
    This file is part of Wicket-Contrib-TinyMce. See
    <http://http://wicketstuff.org/confluence/display/STUFFWIKI/wicket-contrib-tinymce>

    Wicket-Contrib-TinyMce is free software: you can redistribute it and/
    or modify it under the terms of the GNU Lesser General Public License
    as published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Wicket-Contrib-TinyMce is distributed in the hope that it will be
    useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Wicket-Contrib-TinyMce.  If not, see
    <http://www.gnu.org/licenses/>.
 */
package wicket.contrib.tinymce.settings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.set.ListOrderedSet;
import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wicket.contrib.tinymce.InPlaceEditBehavior;

/**
 * Settings class for TinyMCE editor. User can add/remove buttons, enable/disable resizing, change positions,
 * orientation, alignment and much more.
 * 
 * @author Iulian-Corneliu Costan (iulian.costan@gmail.com)
 * @author Frank Bille Jensen (fbille@avaleo.net)
 * @see Plugin
 * @see Button
 */
public class TinyMCESettings implements Serializable {
    private static final long serialVersionUID = 2L;
    private static final Logger LOG = LoggerFactory.getLogger(TinyMCESettings.class);
    public static final ResourceReference REFERENCE = new CompressedResourceReference(InPlaceEditBehavior.class,
            "tiny_mce/tiny_mce_src.js");
    public static final ResourceReference REFERENCE_MIN = new CompressedResourceReference(InPlaceEditBehavior.class,
            "tiny_mce/tiny_mce.js");
    public static final Set<Language> languages = new HashSet<Language>(Arrays.asList(Language.values()));

    private Theme theme;
    private Location toolbarLocation;
    private Location statusbarLocation;
    private Align toolbarAlign;
    private Language language;
    private boolean verticalResizing;
    private boolean horizontalResizing;

    private Set plugins;
    private List controls;

    private Set disabledButtons;

    public TinyMCESettings() {
        this(Theme.simple);
    }

    public TinyMCESettings(Theme theme) {
        this(theme, selectLang());
    }

    public TinyMCESettings(Theme theme, Language lang) {
        this.theme = theme;
        this.controls = new LinkedList();
        this.plugins = new ListOrderedSet();
        this.disabledButtons = new ListOrderedSet();
        this.language = lang;
    }

    private static Language selectLang() {
        try {
            return Language.valueOf(Session.get().getLocale().getLanguage());
        } catch (IllegalArgumentException e) {}
        return null;
    }

    public void setToolbarLocation(Location toolbarLocation) {
        this.toolbarLocation = toolbarLocation;
    }

    public void setStatusbarLocation(Location statusbarLocation) {
        this.statusbarLocation = statusbarLocation;
    }

    public void setToolbarAlign(Align toolbarAlign) {
        this.toolbarAlign = toolbarAlign;
    }

    public void setVerticalResizing(boolean resizing) {
        this.verticalResizing = resizing;
    }

    public void setHorizontalResizing(boolean horizontalResizing) {
        this.horizontalResizing = horizontalResizing;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    /**
     * Add a default button to tinymce editor. These plugins are defined by tinymce editor and are ready to use.
     * 
     * @param button -
     *            button to be added
     * @param toolbar -
     *            the toolbar where to add this button to
     * @param position -
     *            position of this button
     */
    public void add(Button button, Toolbar toolbar, Position position) {
        controls.add(new Control(button, toolbar, position));
    }

    /**
     * Allow users to add plugin button. Plugin buttons are defined by tinymce plugins and these plugins have to be
     * registered. Fortunately, when new plugin button is added, the plugin that defines the button is automatically
     * registered within tinymce editor.
     * 
     * @param button -
     *            button to be added
     * @param toolbar -
     *            the toolbar where to add this button to
     * @param position -
     *            position of this button
     */
    public void add(PluginButton button, Toolbar toolbar, Position position) {
        register(button.getPlugin());
        controls.add(new Control(button, toolbar, position));
    }

    /**
     * Disable specific button in advanced theme mode.
     * 
     * @param button
     *            button to be disabled
     */
    public void disableButton(Button button) {
        disabledButtons.add(button);
    }

    /**
     * Register a tinymce plugin. In order to reuse a existing plugin it has to be registered before. Usually plugins
     * are registered automatically when a plugin button is added, but there are some plugins that contains no buttons.
     * This method is used to register those plugins. (eg AutoSave)
     * 
     * @param plugin
     */
    public void register(Plugin plugin) {
        plugins.add(plugin);
    }

    // used in testing
    Set getPlugins() {
        return plugins;
    }

    /**
     * Generates the initialisation script. Internal API, do not call.
     */
    public String toJavaScript(Mode mode, Collection<Component> components) {
        StringBuffer buffer = new StringBuffer();

        // mode
        buffer.append("\n\t").append("mode : \"" + mode.getName() + "\"");
        if (Mode.exact.equals(mode))
            addElements(components, buffer);

        // theme
        buffer.append(",\n\t").append("theme : ").append("\"").append(theme.getName()).append("\"");

        // language
        if (language != null)
            buffer.append(",\n\t").append("language : ").append("\"").append(language.toString()).append("\"");

        if (Theme.advanced.equals(theme))
            appendAdvancedSettings(buffer);

        appendPluginSettings(buffer);

        return buffer.toString();
    }

    private void addElements(Collection<Component> components, StringBuffer buffer) {
        if (components.size() > 0) {
            buffer.append(",\n\telements : \"");
            Iterator iterator = components.iterator();
            while (iterator.hasNext()) {
                Component component = (Component)iterator.next();
                buffer.append(component.getMarkupId());
                if (iterator.hasNext()) {
                    buffer.append(", ");
                }
            }
            buffer.append("\"");
        } else
            LOG.warn("tinymce is set to \"exact\" mode but there are no components attached");
    }

    private void appendPluginSettings(StringBuffer buffer) {
        if (plugins.size() > 0) {
            Iterator iterator = plugins.iterator();
            while (iterator.hasNext()) {
                Plugin plugin = (Plugin)iterator.next();
                plugin.definePluginSettings(buffer);
            }
        }
    }

    public String getLoadPluginJavaScript() {
        StringBuffer loadPluginJavaScript = new StringBuffer();

        if (plugins.size() > 0) {
            Iterator iterator = plugins.iterator();
            while (iterator.hasNext()) {
                Plugin plugin = (Plugin)iterator.next();
                String pluginPath = plugin.getPluginPath();

                if (pluginPath != null && pluginPath.equals("") == false) {
                    loadPluginJavaScript.append("tinyMCE.loadPlugin('");
                    loadPluginJavaScript.append(plugin.getName());
                    loadPluginJavaScript.append("','");
                    loadPluginJavaScript.append(pluginPath);
                    loadPluginJavaScript.append("');\n");
                }
            }
        }

        return loadPluginJavaScript.toString();
    }

    /**
     * Get additional javascript from the plugins.
     * 
     * @return The different plugins additional javascript.
     */
    public String getAdditionalPluginJavaScript() {
        StringBuffer buffer = new StringBuffer();

        if (plugins.size() > 0) {
            Iterator iterator = plugins.iterator();
            while (iterator.hasNext()) {
                Plugin plugin = (Plugin)iterator.next();
                plugin.definePluginExtensions(buffer);
            }
        }

        return buffer.toString();
    }

    private void appendAdvancedSettings(StringBuffer buffer) {
        // disable buttons
        addDisabledButtons(buffer);

        // plugins
        addPlugins(buffer);

        // add additional controls
        addButtons1_Before(buffer);
        addButtons1_After(buffer);
        addButtons2_Before(buffer);
        addButtons2_After(buffer);
        addButtons3_Before(buffer);
        addButtons3_After(buffer);
        addButtons4(buffer);

        // toolbar, statusbar
        addToolbarLocation(buffer);
        addStatusbarLocation(buffer);
        addToolbarAlign(buffer);

        // resizing
        addVerticalResizing(buffer);
        addHorizontalResizing(buffer);
    }

    void addPlugins(StringBuffer buffer) {
        if (plugins.size() > 0) {
            String value = enumAsString(plugins);
            buffer.append(",\n\t").append("plugins : ").append("\"").append(value).append("\"");
        }
    }

    private void addButtons1_Before(StringBuffer buffer) {
        ControlPredicate predicate = new ControlPredicate(Toolbar.first, Position.before);
        Collection result = CollectionUtils.select(controls, predicate);
        if (result.size() > 0) {
            buffer.append(",\n\t").append("theme_advanced_buttons1_add_before : ").append("\"").append(
                    controlsAsString(result)).append("\"");
        }
    }

    private void addButtons1_After(StringBuffer buffer) {
        ControlPredicate predicate = new ControlPredicate(Toolbar.first, Position.after);
        Collection result = CollectionUtils.select(controls, predicate);
        if (result.size() > 0) {
            buffer.append(",\n\t").append("theme_advanced_buttons1_add : ").append("\"").append(
                    controlsAsString(result)).append("\"");
        }
    }

    private void addButtons2_Before(StringBuffer buffer) {
        ControlPredicate predicate = new ControlPredicate(Toolbar.second, Position.before);
        Collection result = CollectionUtils.select(controls, predicate);
        if (result.size() > 0) {
            buffer.append(",\n\t").append("theme_advanced_buttons2_add_before: ").append("\"").append(
                    controlsAsString(result)).append("\"");
        }
    }

    private void addButtons2_After(StringBuffer buffer) {
        ControlPredicate predicate = new ControlPredicate(Toolbar.second, Position.after);
        Collection result = CollectionUtils.select(controls, predicate);
        if (result.size() > 0) {
            buffer.append(",\n\t").append("theme_advanced_buttons2_add : ").append("\"").append(
                    controlsAsString(result)).append("\"");
        }
    }

    private void addButtons3_Before(StringBuffer buffer) {
        ControlPredicate predicate = new ControlPredicate(Toolbar.third, Position.before);
        Collection result = CollectionUtils.select(controls, predicate);
        if (result.size() > 0) {
            buffer.append(",\n\t").append("theme_advanced_buttons3_add_before : ").append("\"").append(
                    controlsAsString(result)).append("\"");
        }
    }

    private void addButtons3_After(StringBuffer buffer) {
        ControlPredicate predicate = new ControlPredicate(Toolbar.third, Position.after);
        Collection result = CollectionUtils.select(controls, predicate);
        if (result.size() > 0) {
            buffer.append(",\n\t").append("theme_advanced_buttons3_add : ").append("\"").append(
                    controlsAsString(result)).append("\"");
        }
    }

    private void addButtons4(StringBuffer buffer) {
        ControlPredicate predicate = new ControlPredicate(Toolbar.fourth, Position.before);
        Collection result = CollectionUtils.select(controls, predicate);
        if (result.size() > 0) {
            buffer.append(",\n\t").append("theme_advanced_buttons4 : ").append("\"").append(controlsAsString(result))
                    .append("\"");
        }

        predicate = new ControlPredicate(Toolbar.fourth, Position.after);
        result = CollectionUtils.select(controls, predicate);
        if (result.size() > 0) {
            buffer.append(",\n\t").append("theme_advanced_buttons4 : ").append("\"").append(controlsAsString(result))
                    .append("\"");
        }
    }

    void addDisabledButtons(StringBuffer buffer) {
        if (disabledButtons.size() > 0) {
            String value = enumAsString(disabledButtons);
            buffer.append(",\n\t").append("theme_advanced_disable : ").append("\"").append(value).append("\"");
        }
    }

    private String controlsAsString(Collection controls) {
        List buttons = new ArrayList();
        Iterator iterator = controls.iterator();
        while (iterator.hasNext()) {
            Control control = (Control)iterator.next();
            buttons.add(control.getButton());
        }
        return enumAsString(buttons);
    }

    private String enumAsString(Collection enums) {
        StringBuffer buffer = new StringBuffer();
        Iterator iterator = enums.iterator();
        while (iterator.hasNext()) {
            wicket.contrib.tinymce.settings.Enum enumObject = (Enum)iterator.next();
            if (buffer.length() > 0) {
                buffer.append(", ");
            }
            buffer.append(enumObject.getName());
        }
        return buffer.toString();
    }

    void addHorizontalResizing(StringBuffer buffer) {
        String value = horizontalResizing ? Boolean.TRUE.toString() : Boolean.FALSE.toString();
        buffer.append(",\n\t").append("theme_advanced_resize_horizontal : ").append(value);
    }

    void addVerticalResizing(StringBuffer buffer) {
        String value = verticalResizing ? Boolean.TRUE.toString() : Boolean.FALSE.toString();
        buffer.append(",\n\t").append("theme_advanced_resizing : ").append(value);
    }

    void addToolbarAlign(StringBuffer buffer) {
        if (toolbarAlign != null) {
            buffer.append(",\n\t").append("theme_advanced_toolbar_align : ").append("\"")
                    .append(toolbarAlign.getName()).append("\"");
        }
    }

    void addToolbarLocation(StringBuffer buffer) {
        if (toolbarLocation != null) {
            buffer.append(",\n\t").append("theme_advanced_toolbar_location : ").append("\"").append(
                    toolbarLocation.getName()).append("\"");
        }
    }

    void addStatusbarLocation(StringBuffer buffer) {
        if (statusbarLocation != null) {
            buffer.append(",\n\t").append("theme_advanced_statusbar_location : ").append("\"").append(
                    statusbarLocation.getName()).append("\"");
        }
    }

    public static ResourceReference javaScriptReference() {
        Application app = Application.get();
        if (Application.DEVELOPMENT.equals(app.getConfigurationType()))
            return REFERENCE;
        else
            return REFERENCE_MIN;
    }

    private class ControlPredicate implements Predicate {

        private Toolbar toolbar;
        private Position position;

        public ControlPredicate(Toolbar toolbar, Position position) {
            this.toolbar = toolbar;
            this.position = position;
        }

        public boolean evaluate(Object object) {
            Control control = (Control)object;
            return toolbar.equals(control.getToolbar()) && position.equals(control.getPosition());
        }
    }

    /**
     * This class specifies how elements is to be converted into TinyMCE WYSIWYG editor instances. This option can be
     * set to any of the values below:
     * <ul>
     * <li>textareas - converts all textarea elements to editors when the page loads.</li>
     * <li>exact - exact - Converts only explicit elements, these are listed in the elements option.</li>
     * <li>specific_textares - Converts all textarea elements with the a textarea_trigger attribute set to "true".</li>
     * </ul>
     * At this moment, only <b>textareas</b> and <b>exacat</b> modes are supported.
     */
    public static class Mode extends Enum {
        private static final long serialVersionUID = 1L;

        public static final Mode none = new Mode("none");
        public static final Mode exact = new Mode("exact");

        private Mode(String name) {
            super(name);
        }
    }

    /**
     * Language enum
     */
    public static enum Language {
        ar, bg, bs, ca, ch, cs, da, de, el, en, es, et, fa, fi, fr, gl, he, hr, hu, ia, ii, is, it, ja, ko, lt, lv, mk, ms, nb, nl, nn, pl, pt, ro, ru, sc, se, si, sk, sl, sr, sv, tr, tt, tw, uk, vi, zh
    }

    /**
     * This class enables you to specify what theme to use when rendering the TinyMCE WYSIWYG editor instances. Two
     * themes are supported:
     * <ul>
     * <li>simple - This is the most simple theme for TinyMCE it contains only the basic functions.</li>
     * <li>advanced - This theme enables users to add/remove buttons and panels .</li>
     * </ul>
     */
    public static class Theme extends Enum {
        private static final long serialVersionUID = 1L;

        public static final Theme simple = new Theme("simple");
        public static final Theme advanced = new Theme("advanced");

        private Theme(String name) {
            super(name);
        }
    }

    /**
     * This option enables you to specify where the toolbar should be located. This value can be top or bottom.
     */
    public static class Location extends Enum {
        private static final long serialVersionUID = 1L;

        public static final Location top = new Location("top");
        public static final Location bottom = new Location("bottom");

        private Location(String name) {
            super(name);
        }
    }

    /**
     * This class enables you to specify the alignment of the controls. This value can be left, right or center the
     * default value is center.
     */
    public static class Align extends Enum {
        private static final long serialVersionUID = 1L;

        public static final Align left = new Align("left");
        public static final Align center = new Align("center");
        public static final Align right = new Align("right");

        private Align(String name) {
            super(name);
        }
    }

    /**
     * This class specifies the position of new added control. It can be before or after existing elements.
     */
    public static class Position extends Enum {
        private static final long serialVersionUID = 1L;

        public static final Position before = new Position("before");
        public static final Position after = new Position("after");

        public Position(String name) {
            super(name);
        }
    }

    /**
     * This class specifices the toolbar to add specific control to. TinyMCE editor defines three toolbars named: first,
     * second, third.
     */
    public static class Toolbar extends Enum {
        private static final long serialVersionUID = 1L;

        public static final Toolbar first = new Toolbar("first");
        public static final Toolbar second = new Toolbar("second");
        public static final Toolbar third = new Toolbar("third");
        public static final Toolbar fourth = new Toolbar("fourth");

        public Toolbar(String name) {
            super(name);
        }
    }

    // default tinymce buttons
    public static final Button bold = new Button("bold");
    public static final Button italic = new Button("italic");
    public static final Button underline = new Button("underline");
    public static final Button strikethrough = new Button("strikethrough");
    public static final Button justifyleft = new Button("justifyleft");
    public static final Button justifycenter = new Button("justifycenter");
    public static final Button justifyright = new Button("justifyright");
    public static final Button justifyfull = new Button("justifyfull");
    public static final Button styleselect = new Button("styleselect");
    public static final Button formatselect = new Button("formatselect");
    public static final Button bullist = new Button("bullist");
    public static final Button numlist = new Button("numlist");
    public static final Button outdent = new Button("outdent");
    public static final Button indent = new Button("indent");
    public static final Button undo = new Button("undo");
    public static final Button redo = new Button("redo");
    public static final Button link = new Button("link");
    public static final Button unlink = new Button("unlink");
    public static final Button anchor = new Button("anchor");
    public static final Button image = new Button("image");
    public static final Button cleanup = new Button("cleanup");
    public static final Button help = new Button("help");
    public static final Button code = new Button("code");
    public static final Button hr = new Button("hr");
    public static final Button removeformat = new Button("removeformat");
    public static final Button visualaid = new Button("visualaid");
    public static final Button sub = new Button("sub");
    public static final Button sup = new Button("sup");
    public static final Button charmap = new Button("charmap");
    public static final Button separator = new Button("separator");

    // others buttons added by plugins
    public static final Button newdocument = new Button("newdocument");
    public static final Button cut = new Button("cut");
    public static final Button copy = new Button("copy");
    public static final Button fontselect = new Button("fontselect");
    public static final Button fontsizeselect = new Button("fontsizeselect");
    public static final Button forecolor = new Button("forecolor");
    public static final Button backcolor = new Button("backcolor");

}
