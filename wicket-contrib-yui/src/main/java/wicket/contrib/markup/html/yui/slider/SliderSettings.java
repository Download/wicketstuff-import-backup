package wicket.contrib.markup.html.yui.slider;

import java.io.Serializable;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.ResourceReference;
import wicket.contrib.ImageResourceInfo;
import wicket.contrib.InlineStyle;

public class SliderSettings implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Aqua version.
	 * 
	 * @param leftUp
	 *            the left or up number of pixels
	 * @param rightDown
	 *            the right or down number of pixels
	 * @param tick
	 *            the number of pixels for each tick
	 * @return a default look and feel slider bar
	 */
	public static SliderSettings getAqua(int leftUp, int rightDown, int tick) {
		ResourceReference background = new ResourceReference(Slider.class,
				"style/aqua/aqua_bg.png");
		ResourceReference thumb = new ResourceReference(Slider.class,
				"style/aqua/aqua_thumb.gif");
		ResourceReference leftCorner = new ResourceReference(Slider.class,
				"style/aqua/aqua_left.png");
		ResourceReference leftTick = new ResourceReference(Slider.class,
				"style/aqua/aqua_left_tick.png");
		ResourceReference rightCorner = new ResourceReference(Slider.class,
				"style/aqua/aqua_right.png");
		ResourceReference rightTick = new ResourceReference(Slider.class,
				"style/aqua/aqua_right_tick.png");

		SliderSettings defSettings = new SliderSettings();
		defSettings.setResources(leftUp, rightDown, tick, background, thumb,
				leftCorner, leftTick, rightCorner, rightTick, true);
		return defSettings;
	}

	/**
	 * returns a default SliderSettings. This returns all the deafult images for
	 * the slider, but does not include the sizes. at the time this is created,
	 * it only knows the 6 images that make up this deafult looking slider.
	 * 
	 * @param leftUp
	 *            the left or up number of pixels
	 * @param rightDown
	 *            the right or down number of pixels
	 * @param tick
	 *            the number of pixels for each tick
	 * @return a default look and feel slider bar
	 */

	public static SliderSettings getDefault(int leftUp, int rightDown, int tick) {
		ResourceReference background = new ResourceReference(Slider.class,
				"style/bg.gif");
		ResourceReference thumb = new ResourceReference(Slider.class,
				"style/thumb.png");
		ResourceReference leftCorner = new ResourceReference(Slider.class,
				"style/left.gif");
		ResourceReference leftTick = new ResourceReference(Slider.class,
				"style/left_tick.gif");
		ResourceReference rightCorner = new ResourceReference(Slider.class,
				"style/right.gif");
		ResourceReference rightTick = new ResourceReference(Slider.class,
				"style/right_tick.gif");

		SliderSettings defSettings = new SliderSettings();
		defSettings.setResources(leftUp, rightDown, tick, background, thumb,
				leftCorner, leftTick, rightCorner, rightTick, false);
		return defSettings;
	}

	private InlineStyle background = new InlineStyle();

	private ResourceReference backgroundResource;

	private InlineStyle handle = new InlineStyle();

	private ResourceReference leftCornerResource;

	private ResourceReference leftTickResource;

	private String leftUp;

	/* Resources needed to generate the slider bar */
	private ResourceReference rightCornerResource;

	private String rightDown;

	private ResourceReference rightTickResource;

	private boolean showTicks = false;

	private InlineStyle thumb = new InlineStyle();

	private ResourceReference thumbResource;

	private String tick;

	private String tickSize;

	/**
	 * Contructor for custom creation of slider styles, you can use getDefault()
	 * ti get a default SliderSettings.
	 */

	public SliderSettings() {
	}

	public InlineStyle getBackground() {
		return background;
	}

	/*
	 * Accessors
	 * 
	 */

	public ResourceReference getBackgroundResource() {
		return backgroundResource;
	}

	public InlineStyle getHandle() {
		return handle;
	}

	public ResourceReference getLeftCornerResource() {
		return leftCornerResource;
	}

	public ResourceReference getLeftTickResource() {
		return leftTickResource;
	}

	public String getLeftUp() {
		return leftUp;
	}

	public ResourceReference getRightCornerResource() {
		return rightCornerResource;
	}

	public String getRightDown() {
		return rightDown;
	}

	public ResourceReference getRightTickResource() {
		return rightTickResource;
	}

	public InlineStyle getThumb() {
		return thumb;
	}

	public ResourceReference getThumbResource() {
		return thumbResource;
	}

	public String getTick() {
		return tick;
	}

	public String getTickSize() {
		return tickSize;
	}

	public boolean isShowTicks() {
		return showTicks;
	}

	public void setBackground(InlineStyle background) {
		this.background = background;
	}

	public void setBackgroundResource(ResourceReference backgroundResource) {
		this.backgroundResource = backgroundResource;
	}

	public void setHandle(InlineStyle handle) {
		this.handle = handle;
	}

	public void setLeftCornerResource(ResourceReference leftCornerResource) {
		this.leftCornerResource = leftCornerResource;
	}

	public void setLeftTickResource(ResourceReference leftTickResource) {
		this.leftTickResource = leftTickResource;
	}

	public void setLeftUp(String leftUp) {
		this.leftUp = leftUp;
	}

	/**
	 * sets the resources
	 * 
	 * @param background
	 * @param thumb
	 * @param leftCorner
	 * @param leftTick
	 * @param rightCorner
	 * @param rightTick
	 */

	public void setResources(int leftUp, int rightDown, int tick,
			ResourceReference background, ResourceReference thumb,
			ResourceReference leftCorner, ResourceReference leftTick,
			ResourceReference rightCorner, ResourceReference rightTick,
			boolean showTicks) {
		setShowTicks(showTicks);
		/*
		 * sets all the resources
		 */

		setBackgroundResource(background);
		setLeftCornerResource(leftCorner);
		setRightCornerResource(rightCorner);
		setLeftTickResource(leftTick);
		setRightTickResource(rightTick);
		setThumbResource(thumb);

		/*
		 * calculate the width and height
		 */

		ImageResourceInfo bgInfo = new ImageResourceInfo(background);
		ImageResourceInfo thumbInfo = new ImageResourceInfo(thumb);

		int height = bgInfo.getHeight();
		int thumbWidth = thumbInfo.getWidth();
		int left = leftUp - thumbWidth;
		int bgLength = leftUp + rightDown;
		String width = Integer.toString(bgLength - thumbWidth);

		setLeftUp(Integer.toString(leftUp));
		setRightDown(Integer.toString(rightDown));
		setTick(Integer.toString(tick));

		/* background */
		getBackground().add(
				"background",
				"url(" + RequestCycle.get().urlFor(getBackgroundResource())
						+ ") repeat-x");
		getBackground().add("height", height + "px");
		getBackground().add("width", width + "px");

		/* handle */
		getHandle().add("width", thumbWidth + "px");
		getHandle().add("height", height + "px");
		getHandle().add("top", "0px");
		getHandle().add("left", left + "px");

		/* thumb */
		getThumb().add(
				"background",
				"url(" + RequestCycle.get().urlFor(getThumbResource())
						+ ") no-repeat");
		getThumb().add("background-position", "center");
		getThumb().add("height", "100%");
		getThumb().add("width", thumbWidth + "px");
	}

	public void setRightCornerResource(ResourceReference rightCornerResource) {
		this.rightCornerResource = rightCornerResource;
	}

	public void setRightDown(String rightDown) {
		this.rightDown = rightDown;
	}

	public void setRightTickResource(ResourceReference rightTickResource) {
		this.rightTickResource = rightTickResource;
	}

	public void setShowTicks(boolean showTicks) {
		this.showTicks = showTicks;
	}

	public void setThumb(InlineStyle thumb) {
		this.thumb = thumb;
	}

	public void setThumbResource(ResourceReference thumbResource) {
		this.thumbResource = thumbResource;
	}

	public void setTick(String tick) {
		this.tick = tick;
	}

	public void setTickSize(String tickSize) {
		this.tickSize = tickSize;
	}

}
