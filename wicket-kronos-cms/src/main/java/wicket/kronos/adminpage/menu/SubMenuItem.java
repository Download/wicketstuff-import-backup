package wicket.kronos.adminpage.menu;

import java.io.Serializable;

/**
 * @author postma
 */
public class SubMenuItem implements Serializable {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String name;

	private String ID;

	private String IDType;

	/**
	 * Constructor.
	 * 
	 * @param name
	 * @param ID
	 * @param IDType
	 */
	public SubMenuItem(String name, String ID, String IDType)
	{
		this.name = name;
		this.ID = ID;
		this.IDType = IDType;
	}

	/**
	 * @return the ID
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
	 * @return the IDType
	 */
	public String getIDType()
	{
		return IDType;
	}

	/**
	 * @param idtype
	 */
	public void setIDType(String idtype)
	{
		IDType = idtype;
	}

	/**
	 * @return the name
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
}
