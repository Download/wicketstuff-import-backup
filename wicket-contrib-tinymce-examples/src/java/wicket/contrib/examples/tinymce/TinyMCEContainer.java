package wicket.contrib.examples.tinymce;

import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;

import wicket.contrib.tinymce.TinyMceBehavior;

public class TinyMCEContainer extends Panel {

	public boolean enableTinymce;

	public TinyMCEContainer(String id) {
		super(id);
		setOutputMarkupId(true);

		TextArea textArea = new TextArea("ta") {

			@Override
			public boolean isVisible() {
				return enableTinymce;
			}
		};
		textArea.add(new TinyMceBehavior(true));
		add(textArea);
	}

	public void setEnableTinymce(boolean enableTinymce) {
		this.enableTinymce = enableTinymce;
	}

}
