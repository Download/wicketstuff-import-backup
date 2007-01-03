package wicket.kronos;

import wicket.markup.html.image.resource.DynamicImageResource;

/**
 * @author postma
 */
public class CMSImageResource extends DynamicImageResource {

	/**
	 * Default serialUID
	 */
	private static final long serialVersionUID = 1L;
	private String imageName = null;
	private boolean selected = false;
	private byte[] imageData = null;

	/**
	 * @param imageData
	 */
	public CMSImageResource(byte[] imageData, String imageName)
	{
		this.imageData = imageData;
		this.imageName = imageName;
	}

	@Override
	protected byte[] getImageData()
	{
		return imageData;
	}

	public String getImageName()
	{
		return imageName;
	}

	public void setImageName(String imageName)
	{
		this.imageName = imageName;
	}

	public void setImageData(byte[] imageData)
	{
		this.imageData = imageData;
	}

	public boolean isSelected()
	{
		return selected;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

}
