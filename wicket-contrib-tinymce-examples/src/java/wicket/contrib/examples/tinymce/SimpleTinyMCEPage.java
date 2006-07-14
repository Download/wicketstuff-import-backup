package wicket.contrib.examples.tinymce;

import wicket.contrib.tinymce.TinyMCEPanel;
import wicket.markup.html.form.TextArea;
import wicket.model.Model;

/**
 * @author Iulian-Corneliu COSTAN
 */
public class SimpleTinyMCEPage extends TinyMCEBasePage
{

    public SimpleTinyMCEPage()
    {
        add(new TinyMCEPanel("tinyMCE"));
        add(new TextArea("ta", new Model(TEXT)));
    }

    private String TEXT = "Some <b>element</b>, this is to be editor 1.";
}
