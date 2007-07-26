package wicket.contrib.mootools.plugins;

import org.apache.wicket.IClusterable;
import org.apache.wicket.Page;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.Session;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.protocol.http.ClientProperties;
import org.apache.wicket.protocol.http.request.WebClientInfo;

import wicket.contrib.mootools.components.MFXAjaxStatelessLink;
import wicket.contrib.mootools.effects.MFXStyle;
import wicket.contrib.mootools.effects.MFXTransition;

public abstract class MFXDialogBase extends Panel {
	private static final long serialVersionUID = 1L;
	private PageCreator pageCreator;
	private int width;
	private int height;
	private String unit;
	private String title;
	private int offsetTop;
	private String body;

	private ResourceReference PLAINCSS = new CompressedResourceReference(
			MFXDialog.class, "MFXDialog.css");

	public MFXDialogBase(final String id) {
		super(id);

		this.width = 300;
		this.height = 0;
		this.offsetTop = 300;
		this.unit = "px";
		this.title = "Modal Window";

		add(HeaderContributor.forCss(PLAINCSS));
	}

	public interface PageCreator extends IClusterable {
		public Page createPage();
	}

	protected Page createPage() {

		if (pageCreator == null) {
			return null;
		}

		try {
			Page page = pageCreator.createPage();
			return page;
		} catch (Exception e) {
			return null;
		}
	}

	protected String genericOpenJavaScript(final String id,
			final String contentId) {
		return genericOpenJavaScript(id, id, contentId);
	}

	protected String genericCloseWindowJavaScript(final String id) {
		StringBuffer str = new StringBuffer();
		str.append("var win = $('" + id + "');");
		str.append("win.setStyle('display','none');");
		return str.toString();
	}

	protected String genericOpenJavaScript(final String id,
			final String dialogId, final String contentId) {
		StringBuffer str = new StringBuffer();

		Page page = createPage();
		String url = null;
		if (page != null) {
			url = RequestCycle.get().urlFor(page).toString();
		}

		str.append("var elm = $('" + id + "');");
		str.append("var win = $('" + dialogId + "');");
		str.append("elm.setStyle('display','block');");

		str
				.append("win.makeDraggable( { handle: win, 'onBeforeStart': function() { win.setStyle('opacity',0.5); },   "
						+ "'onComplete': function() { win.setStyle('opacity',1); }});");

		MFXStyle style = new MFXStyle("margin-top", 0, 100);

		style.setDuration(1000);
		style.setTransition(MFXTransition.backInOut);
		style.setTarget(dialogId);

		str.append("var effect = " + style.toString());

		WebClientInfo clientInfo = (WebClientInfo) Session.get()
				.getClientInfo();
		ClientProperties properties = clientInfo.getProperties();
		if (properties.isBrowserInternetExplorer()) {
			str.append("var winw = document.body.offsetWidth;");
			str.append("var winh = document.body.offsetHeight;");
		} else {
			str.append("var winw = window.getWidth();");
			str.append("var winh = window.getHeight();");
		}

		if (getWidth() != 0) {
			str.append("win.setStyle('width','" + getWidth() + "" + getUnit()
					+ "');");
		}
		if (getHeight() != 0) {
			str.append("win.setStyle('height','" + getHeight() + "" + getUnit()
					+ "');");
		}
		// window.getHeight()/2 + window.getScrollTop()
		str.append("win.setStyle('left',(winw-" + getWidth() + ")/2);");
		// str.append("win.setStyle('top',winh/2-" + getOffsetTop() + ");");
		str.append("win.setStyle('top',winh/2 + window.getScrollTop() -"
				+ getOffsetTop() + " );");

		if (url != null) {
			MFXAjaxStatelessLink mfxlink = new MFXAjaxStatelessLink("mfxlink",
					contentId, page);
			mfxlink.getOptions().setOnComplete(
					"effect.start(" + style.getStartValue() + ","
							+ style.getEndValue() + ");");
			str.append(mfxlink.mooFunction());
		} else {
			str.append("effect.start(" + style.getStartValue() + ","
					+ style.getEndValue() + ");");
		}

		// return "alert('hello world');";
		return str.toString();
	}

	public PageCreator getPageCreator() {
		return pageCreator;
	}

	public void setPageCreator(final PageCreator pageCreator) {
		this.pageCreator = pageCreator;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(final int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(final int height) {
		this.height = height;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(final String unit) {
		this.unit = unit;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public int getOffsetTop() {
		return offsetTop;
	}

	public void setOffsetTop(final int offsetTop) {
		this.offsetTop = offsetTop;
	}

	public void setBody(final String body) {
		this.body = body;
	}

	public String getBody() {
		return body;
	}

}
