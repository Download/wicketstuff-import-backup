package wicket.addons.hibernate.base;

import org.springframework.dao.DataAccessException;

import wicket.addons.hibernate.dao.WorkitemDAOImpl;
import wicket.addons.hibernate.dao.WorkitemDAO;

/**
 * This class has been automatically generated by Hibernate Synchronizer.
 * For more information or documentation, visit The Hibernate Synchronizer page
 * at http://www.binamics.com/hibernatesync or contact Joe Hudson at joe@binamics.com.
 *
 * This is an automatically generated DAO class which should not be edited.
 */
public abstract class BaseWorkitemDAOImpl extends wicket.addons.hibernate.dao._RootDAO implements BaseWorkitemDAO
{

	public static WorkitemDAO instance;

	/**
	 * Return a singleton of the DAO
	 */
	public static WorkitemDAO getInstance () 
    {
		if (null == instance) 
        {
            instance = new WorkitemDAOImpl();
        }
		return instance;
	}

	/**
	 * wicket.addons.hibernate.dao._RootDAO _RootDAO.getReferenceClass()
	 */
	public Class getReferenceClass () 
    {
		return wicket.addons.hibernate.Workitem.class;
	}
	public wicket.addons.hibernate.Workitem load(java.lang.Integer key)
		throws DataAccessException {
		return (wicket.addons.hibernate.Workitem) load(getReferenceClass(), key);
	}

	/**
	 * Persist the given transient instance, first assigning a generated identifier. (Or using the current value
	 * of the identifier property if the assigned generator is used.) 
	 * @param workitem a transient instance of a persistent class 
	 * @return the class identifier
	 */
	public java.lang.Integer save(wicket.addons.hibernate.Workitem workitem)
		throws DataAccessException 
    {
		return (java.lang.Integer) super.save(workitem);
	}

	/**
	 * Either save() or update() the given instance, depending upon the value of its identifier property. By default
	 * the instance is always saved. This behaviour may be adjusted by specifying an unsaved-value attribute of the
	 * identifier property mapping. 
	 * @param workitem a transient instance containing new or updated state 
	 */
	public void saveOrUpdate(wicket.addons.hibernate.Workitem workitem)
		throws DataAccessException 
    {
		super.saveOrUpdate(workitem);
	}

	/**
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * @param workitem a transient instance containing updated state
	 */
	public void update(wicket.addons.hibernate.Workitem workitem) 
		throws DataAccessException 
    {
		super.update(workitem);
	}

	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * @param id the instance ID to be removed
	 */
	public void delete(java.lang.Integer id)
		throws DataAccessException 
    {
		super.delete(load(id));
	}

	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * @param workitem the instance to be removed
	 */
	public void delete(wicket.addons.hibernate.Workitem workitem)
		throws DataAccessException 
    {
		super.delete(workitem);
	}

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
	public void refresh (wicket.addons.hibernate.Workitem workitem)
		throws DataAccessException 
    {
		super.refresh(workitem);
	}

    public String getDefaultOrderProperty () 
    {
		return null;
    }
}