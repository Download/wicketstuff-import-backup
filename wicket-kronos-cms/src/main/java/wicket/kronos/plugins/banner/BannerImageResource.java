package wicket.kronos.plugins.banner;

import wicket.markup.html.image.resource.DynamicImageResource;

/**
 * @author postma
 */
public class BannerImageResource extends DynamicImageResource {

	/**
	 * Default serialUID
	 */
	private static final long serialVersionUID = 1L;

	private byte[] imageData = null;

	/**
	 * @param imageData
	 */
	public BannerImageResource(byte[] imageData)
	{
		this.imageData = imageData;
	}

	@Override
	protected byte[] getImageData()
	{
		return imageData;
	}

}
