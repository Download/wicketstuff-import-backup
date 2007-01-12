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

	private String imageName;

	private boolean selected = false;

	private byte[] imageData;

	/**
	 * Constructor.
	 * 
	 * @param imageData
	 * @param imageName
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

	/**
	 * @return String imageName
	 */
	public String getImageName()
	{
		return imageName;
	}

	/**
	 * @param imageName
	 */
	public void setImageName(String imageName)
	{
		this.imageName = imageName;
	}

	/**
	 * @param imageData
	 */
	public void setImageData(byte[] imageData)
	{
		this.imageData = imageData;
	}

	/**
	 * @return boolean
	 */
	public boolean isSelected()
	{
		return selected;
	}

	/**
	 * @param selected
	 */
	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}
}
