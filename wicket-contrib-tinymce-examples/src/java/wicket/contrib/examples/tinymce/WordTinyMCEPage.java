package wicket.contrib.examples.tinymce;

import wicket.contrib.tinymce.TinyMCEPanel;
import wicket.contrib.tinymce.settings.*;
import wicket.markup.html.form.TextArea;
import wicket.model.Model;

/**
 * @author Iulian-Corneliu COSTAN
 */
public class WordTinyMCEPage extends TinyMCEBasePage
{

    public WordTinyMCEPage()
    {
        TinyMCESettings settings = new TinyMCESettings(TinyMCESettings.Theme.advanced);

        ContextMenuPlugin contextMenuPlugin = new ContextMenuPlugin();
        settings.register(contextMenuPlugin);

        // first toolbar
        SavePlugin savePlugin = new SavePlugin();
        settings.add(savePlugin.getSaveButton(), TinyMCESettings.Toolbar.first, TinyMCESettings.Position.before);
        settings.add(TinyMCESettings.newdocument, TinyMCESettings.Toolbar.first, TinyMCESettings.Position.before);
        settings.add(TinyMCESettings.separator, TinyMCESettings.Toolbar.first, TinyMCESettings.Position.before);
        settings.add(TinyMCESettings.fontselect, TinyMCESettings.Toolbar.first, TinyMCESettings.Position.after);
        settings.add(TinyMCESettings.fontsizeselect, TinyMCESettings.Toolbar.first, TinyMCESettings.Position.after);

        // second toolbar
        PastePlugin pastePlugin = new PastePlugin();
        SearchReplacePlugin searchReplacePlugin = new SearchReplacePlugin();
        DateTimePlugin dateTimePlugin = new DateTimePlugin();
        dateTimePlugin.setDateFormat("Date: %m-%d-%Y");
        dateTimePlugin.setTimeFormat("Time: %H:%M");
        PreviewPlugin previewPlugin = new PreviewPlugin();
        ZoomPlugin zoomPlugin = new ZoomPlugin();
        settings.add(TinyMCESettings.cut, TinyMCESettings.Toolbar.second, TinyMCESettings.Position.before);
        settings.add(TinyMCESettings.copy, TinyMCESettings.Toolbar.second, TinyMCESettings.Position.before);
        settings.add(pastePlugin.getPasteButton(), TinyMCESettings.Toolbar.second, TinyMCESettings.Position.before);
        settings.add(pastePlugin.getPasteTextButton(), TinyMCESettings.Toolbar.second, TinyMCESettings.Position.before);
        settings.add(pastePlugin.getPasteWordButton(), TinyMCESettings.Toolbar.second, TinyMCESettings.Position.before);
        settings.add(TinyMCESettings.separator, TinyMCESettings.Toolbar.second, TinyMCESettings.Position.before);
        settings.add(searchReplacePlugin.getSearchButton(), TinyMCESettings.Toolbar.second, TinyMCESettings.Position.before);
        settings.add(searchReplacePlugin.getReplaceButton(), TinyMCESettings.Toolbar.second, TinyMCESettings.Position.before);
        settings.add(TinyMCESettings.separator, TinyMCESettings.Toolbar.second, TinyMCESettings.Position.before);
        settings.add(TinyMCESettings.separator, TinyMCESettings.Toolbar.second, TinyMCESettings.Position.after);
        settings.add(dateTimePlugin.getDateButton(), TinyMCESettings.Toolbar.second, TinyMCESettings.Position.after);
        settings.add(dateTimePlugin.getTimeButton(), TinyMCESettings.Toolbar.second, TinyMCESettings.Position.after);
        settings.add(TinyMCESettings.separator, TinyMCESettings.Toolbar.second, TinyMCESettings.Position.after);
        settings.add(previewPlugin.getPreviewButton(), TinyMCESettings.Toolbar.second, TinyMCESettings.Position.after);
        settings.add(zoomPlugin.getZoomButton(), TinyMCESettings.Toolbar.second, TinyMCESettings.Position.after);
        settings.add(TinyMCESettings.separator, TinyMCESettings.Toolbar.second, TinyMCESettings.Position.after);
        settings.add(TinyMCESettings.forecolor, TinyMCESettings.Toolbar.second, TinyMCESettings.Position.after);
        settings.add(TinyMCESettings.backcolor, TinyMCESettings.Toolbar.second, TinyMCESettings.Position.after);

        // third toolbar
        TablePlugin tablePlugin = new TablePlugin();
        EmotionsPlugin emotionsPlugin = new EmotionsPlugin();
        IESpellPlugin iespellPlugin = new IESpellPlugin();
        FlashPlugin flashPlugin = new FlashPlugin();
        PrintPlugin printPlugin = new PrintPlugin();
        FullScreenPlugin fullScreenPlugin = new FullScreenPlugin();
        DirectionalityPlugin directionalityPlugin = new DirectionalityPlugin();
        settings.add(tablePlugin.getTableControls(), TinyMCESettings.Toolbar.third, TinyMCESettings.Position.before);
        settings.add(emotionsPlugin.getEmotionsButton(), TinyMCESettings.Toolbar.third, TinyMCESettings.Position.after);
        settings.add(iespellPlugin.getIespellButton(), TinyMCESettings.Toolbar.third, TinyMCESettings.Position.after);
        settings.add(flashPlugin.getFlashButton(), TinyMCESettings.Toolbar.third, TinyMCESettings.Position.after);
        settings.add(TinyMCESettings.separator, TinyMCESettings.Toolbar.third, TinyMCESettings.Position.after);
        settings.add(printPlugin.getPrintButton(), TinyMCESettings.Toolbar.third, TinyMCESettings.Position.after);
        settings.add(TinyMCESettings.separator, TinyMCESettings.Toolbar.third, TinyMCESettings.Position.after);
        settings.add(directionalityPlugin.getLtrButton(), TinyMCESettings.Toolbar.third, TinyMCESettings.Position.after);
        settings.add(directionalityPlugin.getRtlButton(), TinyMCESettings.Toolbar.third, TinyMCESettings.Position.after);
        settings.add(TinyMCESettings.separator, TinyMCESettings.Toolbar.third, TinyMCESettings.Position.after);
        settings.add(fullScreenPlugin.getFullscreenButton(), TinyMCESettings.Toolbar.third, TinyMCESettings.Position.after);

        // other settings
        settings.setToolbarAlign(TinyMCESettings.Align.left);
        settings.setToolbarLocation(TinyMCESettings.Location.top);
        settings.setStatusbarLocation(TinyMCESettings.Location.bottom);
        settings.setVerticalResizing(true);

        new TinyMCEPanel(this, "tinyMCE", settings);
        new TextArea(this, "ta", new Model(TEXT));
    }

    private static final String TEXT = "<p>Some paragraph</p>" +
            "<p>Some other paragraph</p>" +
            "<p>Some <strong>element</strong>, this is to be editor 1. <br />" +
            "This editor instance has a 100% width to it. </p>" +
            "<p>Some paragraph. <a href=\"http://www.sourceforge.net/\">Some link</a></p>" +
            "<img src=\"logo.jpg\" border=\"0\" /><p>&nbsp;</p>";
}
