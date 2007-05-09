package wicket.contrib.markup.html.yui.animselect;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.util.template.PackagedTextTemplate;

import wicket.contrib.InlineStyle;
import wicket.contrib.YuiImage;
import wicket.contrib.markup.html.yui.AbstractYuiPanel;

/**
 * Represent each options
 * 
 * @author cptan
 */
public class AnimSelectOptionBox extends AbstractYuiPanel {

	/**
	 * Represent one of the images for each option
	 * 
	 * @author cptan
	 */
	private final class AnimSelectBox extends FormComponent implements
			Serializable {
		private static final long serialVersionUID = 1L;

		public AnimSelectBox(final String id, final int count,
				final String name, YuiImage yuiImage) {
			super(id);
			add(new AttributeModifier("id", true, new AbstractReadOnlyModel() {
				private static final long serialVersionUID = 1L;

				@Override
				public Object getObject() {
					return name + count + "_" + javaScriptId;
				}
			}));
			add(new AttributeModifier("style", true,
					new AbstractReadOnlyModel() {
						private static final long serialVersionUID = 1L;

						@Override
						public Object getObject() {
							if (name.equals("DefaultImg")) {
								List<InlineStyle> aInlineStyleList = settings
										.getDefaultImgStyleList();
								InlineStyle aInlineStyle = aInlineStyleList
										.get(0);
								return aInlineStyle.getStyle();
							} else if (name.equals("DefaultImgOver")) {
								List<InlineStyle> aInlineStyleList = settings
										.getDefaultImgOverStyleList();
								InlineStyle aInlineStyle = aInlineStyleList
										.get(0);
								return aInlineStyle.getStyle();
							} else if (name.equals("SelectedImg")) {
								List<InlineStyle> aInlineStyleList = settings
										.getSelectedImgStyleList();
								InlineStyle aInlineStyle = aInlineStyleList
										.get(0);
								return aInlineStyle.getStyle();
							} else if (name.equals("SelectedImgOver")) {
								List<InlineStyle> aInlineStyleList = settings
										.getSelectedImgOverStyleList();
								InlineStyle aInlineStyle = aInlineStyleList
										.get(0);
								return aInlineStyle.getStyle();
							} else {
								return new String("");
							}
						}
					}));
		}
	}

	/**
	 * Get the image's width and height
	 * 
	 * @author cptan
	 * 
	 */
	private final class ImgStyle extends FormComponent implements Serializable {
		private static final long serialVersionUID = 1L;

		public ImgStyle(final String id) {
			super(id);
			add(new AttributeModifier("style", true,
					new AbstractReadOnlyModel() {
						private static final long serialVersionUID = 1L;

						@Override
						public Object getObject() {
							return "width:" + settings.getWidth()
									+ "px; height:" + settings.getHeight()
									+ "px";
						}
					}));
		}
	}

	private static final long serialVersionUID = 1L;

	private double duration;

	private String easing;

	private String javaScriptId;

	private int maxSelection;

	private String message;

	private AnimSelectSettings settings;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param index
	 * @param animSelectOption
	 * @param settings
	 */
	public AnimSelectOptionBox(String id, final int index,
			AnimSelectOption animSelectOption, AnimSelectSettings settings) {
		super(id);
		this.settings = settings;
		this.easing = settings.getEasing();
		this.duration = settings.getDuration();
		this.maxSelection = settings.getMaxSelection();
		this.message = settings.getMessage();

		ImgStyle style = new ImgStyle("imgStyle");
		add(style);
		style.add(new AnimSelectBox("defaultImg", index, "DefaultImg",
				animSelectOption.getDefaultImg()));
		style.add(new AnimSelectBox("defaultImgOver", index, "DefaultImgOver",
				animSelectOption.getDefaultImgOver()));
		style.add(new AnimSelectBox("selectedImg", index, "SelectedImg",
				animSelectOption.getSelectedImg()));
		style.add(new AnimSelectBox("selectedImgOver", index,
				"SelectedImgOver", animSelectOption.getSelectedImgOver()));

		Label animSelect = new Label("animSelectScript",
				new AbstractReadOnlyModel() {
					private static final long serialVersionUID = 1L;

					@Override
					public Object getObject() {
						return getAnimSelectInitializationScript(index);
					}
				});
		animSelect.setEscapeModelStrings(false);
		add(animSelect);

	}

	/**
	 * Initialize the animselect.js for each option
	 * 
	 * @param boxId
	 * @return
	 */
	protected String getAnimSelectInitializationScript(int boxId) {
		PackagedTextTemplate template = new PackagedTextTemplate(
				AnimSelectOptionBox.class, "animselect.js");
		Map<String, Object> variables = new HashMap<String, Object>(7);
		variables.put("javaScriptId", javaScriptId);
		variables.put("boxId", new Integer(boxId));
		variables.put("easing", "YAHOO.util.Easing." + easing);
		variables.put("duration", new Double(duration));
		variables.put("maxSelection", new Integer(maxSelection));
		variables.put("noOfBoxes", new Integer(settings
				.getAnimSelectOptionList().size()));
		if (message == null || message.equals("")) {
			message = "Up to " + maxSelection + " selections allowed!";
		}
		variables.put("message", message);
		template.interpolate(variables);
		return template.getString();
	}

	/**
	 * Get the markup Id of the super parent class on attach
	 */
	@Override
	protected void onAttach() {
		super.onAttach();
		javaScriptId = findParent(AnimSelectOptionGroup.class).getMarkupId();
	}
}
