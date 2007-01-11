package wicket.authentication;

import java.io.Serializable;
import java.util.Calendar;

import wicket.authorization.strategies.role.Roles;

/**
 * @author postma
 */
public class User implements Serializable {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String username = null;

	private String name = null;

	private String email = null;

	private Calendar lastVisit = null;

	private Roles roles = null;

	private String hashedPassword = null;

	/**
	 * @param username
	 * @param name
	 * @param email
	 * @param password
	 * @param lastVisit
	 *            date, time from january 1st 1970
	 * @param roles
	 */
	public User(String username, String name, String email, String password, Calendar lastVisit,
			Roles roles)
	{
		assert (username != null);
		assert (name != null);
		assert (email != null);
		assert (lastVisit != null);
		assert (roles != null);
		this.username = username;
		this.name = name;
		this.email = email;
		this.lastVisit = lastVisit;
		this.roles = roles;
		this.hashedPassword = password;
	}

	/**
	 * @return the email
	 */
	public String getEmail()
	{
		return this.email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email)
	{
		assert (email != null);
		this.email = email;
	}

	/**
	 * @return the lastVisit
	 */
	public Calendar getLastVisit()
	{
		return this.lastVisit;
	}

	/**
	 * @param lastVisit
	 *            the lastVisit to set
	 */
	public void setLastVisit(Calendar lastVisit)
	{
		assert (lastVisit != null);
		this.lastVisit = lastVisit;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		assert (name != null);
		this.name = name;
	}

	/**
	 * @return the roles
	 */
	public Roles getRoles()
	{
		return this.roles;
	}

	/**
	 * @param roles
	 *            the roles to set
	 */
	public void setRoles(Roles roles)
	{
		assert (roles != null);
		this.roles = roles;
	}

	/**
	 * @return the username
	 */
	public String getUsername()
	{
		return this.username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username)
	{
		assert (username != null);
		this.username = username;
	}

	/**
	 * @return the hashed password
	 */
	public String getPassword()
	{
		return this.hashedPassword;
	}

	/**
	 * Returns the hashed password
	 * 
	 * @param password
	 */
	public void setPassword(String password)
	{
		assert (password != null);
		this.hashedPassword = password;
	}
}
