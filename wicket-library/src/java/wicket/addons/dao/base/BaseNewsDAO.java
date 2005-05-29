package wicket.addons.dao.base;

import java.io.Serializable;
import java.util.Map;

import org.hibernate.type.Type;
import org.springframework.dao.DataAccessException;

/**
 * This class has been automatically generated by Hibernate Synchronizer.
 * For more information or documentation, visit The Hibernate Synchronizer page
 * at http://www.binamics.com/hibernatesync or contact Joe Hudson at joe@binamics.com.
 */
public interface BaseNewsDAO 
{
	/**
	 * Execute a query. 
	 * @param query a query expressed in Hibernate's query language
	 * @return a distinct list of instances (or arrays of instances)
	 */
	public java.util.List find(String query) throws DataAccessException;

	/**
	 * Return all objects related to the implementation of this DAO with no filter.
	 */
	public java.util.List findAll () throws DataAccessException;

	/**
	 * Return all objects related to the implementation of this DAO with no filter.
	 */
	public java.util.List findAll (String orderProperty) throws DataAccessException;

	/**
	 * Obtain an instance of Query for a named query string defined in the mapping file.
	 * @param name the name of a query defined externally 
	 * @return Query
	 */
	public java.util.List getNamedQuery(String name) throws DataAccessException;

	/**
	 * Obtain an instance of Query for a named query string defined in the mapping file.
	 * Use the parameters given.
	 * @param name the name of a query defined externally 
	 * @param params the parameter array
	 * @return Query
	 */
	public java.util.List getNamedQuery(String name, Serializable[] params)
		throws DataAccessException;

	/**
	 * Obtain an instance of Query for a named query string defined in the mapping file.
	 * Use the parameters given.
	 * @param name the name of a query defined externally 
	 * @param params the parameter Map
	 * @return Query
	 */
	public java.util.List getNamedQuery(String name, Map params)
		throws DataAccessException;

	/**
	 * Execute a query.
	 * @param query a query expressed in Hibernate's query language
	 * @return a distinct list of instances (or arrays of instances)
	 */
	public java.util.List find(String query, Object obj, Type type) throws DataAccessException;

	/**
	 * Execute a query.
	 * @param query a query expressed in Hibernate's query language
	 * @return a distinct list of instances (or arrays of instances)
	 */
	public java.util.List find(String query, Object[] obj, Type[] type) throws DataAccessException;

	/**
	 * Return the property of the class you would like to use for default ordering
	 * @return the property name
	 */
	public String getDefaultOrderProperty ();

	public wicket.addons.dao.News load(int key)
		throws DataAccessException;

	/**
	 * Persist the given transient instance, first assigning a generated identifier. (Or using the current value
	 * of the identifier property if the assigned generator is used.) 
	 * @param news a transient instance of a persistent class 
	 * @return the class identifier
	 */
	public java.lang.Integer save(wicket.addons.dao.News news)
		throws DataAccessException;

	/**
	 * Either save() or update() the given instance, depending upon the value of its identifier property. By default
	 * the instance is always saved. This behaviour may be adjusted by specifying an unsaved-value attribute of the
	 * identifier property mapping. 
	 * @param news a transient instance containing new or updated state 
	 */
	public void saveOrUpdate(wicket.addons.dao.News news)
		throws DataAccessException;

	/**
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * @param news a transient instance containing updated state
	 */
	public void update(wicket.addons.dao.News news) 
		throws DataAccessException;

	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * @param id the instance ID to be removed
	 */
	public void delete(int id)
		throws DataAccessException;

	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * @param news the instance to be removed
	 */
	public void delete(wicket.addons.dao.News news)
		throws DataAccessException;

	/**
	 * Re-read the state of the given instance from the underlying database. It is inadvisable to use this to implement
	 * long-running sessions that span many business tasks. This method is, however, useful in certain special circumstances.
	 * For example 
	 * <ul> 
	 * <li>where a database trigger alters the object state upon insert or update</li>
	 * <li>after executing direct SQL (eg. a mass update) in the same session</li>
	 * <li>after inserting a Blob or Clob</li>
	 * </ul>
	 */
	public void refresh (wicket.addons.dao.News news)
		throws DataAccessException;
}