package wicket.kronos.adminpage.menu;



/**
 * @author postma
 *
 */
public class SubMenuItem {
	
	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private String ID;
	private String IDType;
	
	/**
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
	 * @return the iD
	 */
	public String getID()
	{
		return ID;
	}

	/**
	 * @param id the iD to set
	 */
	public void setID(String id)
	{
		ID = id;
	}

	/**
	 * @return the iDType
	 */
	public String getIDType()
	{
		return IDType;
	}

	/**
	 * @param type the iDType to set
	 */
	public void setIDType(String type)
	{
		IDType = type;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	
}
