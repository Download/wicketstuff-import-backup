package wicket.kronos.plugins.menu;

import java.io.Serializable;

/**
 * This class is used by the menuPlugin as a as a representation of the menuItems.
 * 
 * @author roeloffzen
 */
public class MenuItem implements Serializable {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private boolean remove;

	private int order;

	private String name;

	private String linkType;

	private String link;

	private boolean isAdmin;

	private String IDType;

	private String ID;

	/**
	 * Basic constructor sets the default values for all the instance variables. (Null or false)
	 */
	public MenuItem()
	{
		this.remove = false;
		this.name = null;
		this.linkType = null;
		this.link = null;
		this.isAdmin = false;
		this.IDType = null;
		this.ID = null;
		this.order = 1;
	}

	/**
	 * @return the remove
	 */
	public boolean isRemove()
	{
		return remove;
	}

	/**
	 * @param remove
	 *            the remove to set
	 */
	public void setRemove(boolean remove)
	{
		this.remove = remove;
	}

	/**
	 * @return ID
	 */
	public String getID()
	{
		return ID;
	}

	/**
	 * @param id
	 */
	public void setID(String id)
	{
		ID = id;
	}

	/**
	 * @return IDType
	 */
	public String getIDType()
	{
		return IDType;
	}

	/**
	 * @param type
	 */
	public void setIDType(String type)
	{
		IDType = type;
	}

	/**
	 * @return link
	 */
	public String getLink()
	{
		return link;
	}

	/**
	 * @param link
	 */
	public void setLink(String link)
	{
		this.link = link;
	}

	/**
	 * @return linkType
	 */
	public String getLinkType()
	{
		return linkType;
	}

	/**
	 * @param linkType
	 */
	public void setLinkType(String linkType)
	{
		this.linkType = linkType;
	}

	/**
	 * @return name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return isAdmin
	 */
	public boolean getIsAdmin()
	{
		return isAdmin;
	}

	/**
	 * @param isAdmin
	 */
	public void setIsAdmin(boolean isAdmin)
	{
		this.isAdmin = isAdmin;
	}

	/**
	 * @return order
	 */
	public int getOrder()
	{
		return order;
	}

	/**
	 * @param order
	 */
	public void setOrder(int order)
	{
		this.order = order;
	}

}
