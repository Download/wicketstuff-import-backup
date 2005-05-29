package wicket.addons.dao.base;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.type.Type;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * This class has been automatically generated by Hibernate Synchronizer.
 * For more information or documentation, visit The Hibernate Synchronizer page
 * at http://www.binamics.com/hibernatesync or contact Joe Hudson at joe@binamics.com.
 *
 * This class has been automatically generated by Hibernate Synchronizer. For
 * more information or documentation, visit The Hibernate Synchronizer page at
 * http://www.binamics.com/hibernatesync or contact Joe Hudson at
 * joe@binamics.com.
 */
public abstract class _BaseRootDAO extends HibernateDaoSupport
{
	/**
	 * Return the specific Object class that will be used for class-specific
	 * implementation of this DAO.
	 * 
	 * @return the reference Class
	 */
	protected abstract Class getReferenceClass();

	/**
	 * Execute a query.
	 * 
	 * @param query
	 *           a query expressed in Hibernate's query language
	 * @return a distinct list of instances (or arrays of instances)
	 */
	public java.util.List find(String query) throws DataAccessException
	{
		try
		{
			final Session session = getSession();
			return session.createQuery(query).list();
		}
		catch (HibernateException ex)
		{
			throw SessionFactoryUtils.convertHibernateAccessException(ex);
		}
	}

	/**
	 * Return all objects related to the implementation of this DAO with no
	 * filter.
	 */
	public java.util.List findAll() throws DataAccessException
	{
		return findAll(getDefaultOrderProperty());
	}

	/**
	 * Return all objects related to the implementation of this DAO with no
	 * filter.
	 */
	public java.util.List findAll(String orderProperty) throws DataAccessException
	{
		try
		{
			Criteria crit = createCriteria();
			if (null != orderProperty)
			{
				crit.addOrder(Order.asc(orderProperty));
			}
			return crit.list();
		}
		catch (HibernateException ex)
		{
			throw SessionFactoryUtils.convertHibernateAccessException(ex);
		}
	}

	/**
	 * Return all objects related to the implementation of this DAO with a
	 * filter. Use the session given.
	 * 
	 * @param propName
	 *           the name of the property to use for filtering
	 * @param filter
	 *           the value of the filter
	 */
	protected java.util.List findFiltered(String propName, Object filter) throws DataAccessException
	{
		return findFiltered(propName, filter, getDefaultOrderProperty());
	}

	/**
	 * Return all objects related to the implementation of this DAO with a
	 * filter. Use the session given.
	 * 
	 * @param propName
	 *           the name of the property to use for filtering
	 * @param filter
	 *           the value of the filter
	 * @param orderProperty
	 *           the name of the property used for ordering
	 */
	protected java.util.List findFiltered(String propName, Object filter, String orderProperty)
			throws DataAccessException
	{
		try
		{
			Criteria crit = createCriteria();
			crit.add(Expression.eq(propName, filter));
			if (null != orderProperty)
			{
				crit.addOrder(Order.asc(orderProperty));
			}
			return crit.list();
		}
		catch (HibernateException ex)
		{
			throw SessionFactoryUtils.convertHibernateAccessException(ex);
		}
	}

	/**
	 * Obtain an instance of Query for a named query string defined in the
	 * mapping file.
	 * 
	 * @param name
	 *           the name of a query defined externally
	 * @return Query
	 */
	public java.util.List getNamedQuery(String name) throws DataAccessException
	{
		try
		{
			Session session = getSession();
			Query q = session.getNamedQuery(name);
			return q.list();
		}
		catch (HibernateException ex)
		{
			throw SessionFactoryUtils.convertHibernateAccessException(ex);
		}
	}

	/**
	 * Obtain an instance of Query for a named query string defined in the
	 * mapping file. Use the parameters given.
	 * 
	 * @param name
	 *           the name of a query defined externally
	 * @param params
	 *           the parameter array
	 * @return Query
	 */
	public java.util.List getNamedQuery(String name, Serializable[] params)
			throws DataAccessException
	{
		try
		{
			Session session = getSession();
			Query q = session.getNamedQuery(name);
			if (null != params)
			{
				for (int i = 0; i < params.length; i++)
				{
					setParameterValue(q, i, params[i]);
				}
			}
			return q.list();
		}
		catch (HibernateException ex)
		{
			throw SessionFactoryUtils.convertHibernateAccessException(ex);
		}
	}

	/**
	 * Obtain an instance of Query for a named query string defined in the
	 * mapping file. Use the parameters given.
	 * 
	 * @param name
	 *           the name of a query defined externally
	 * @param params
	 *           the parameter Map
	 * @return Query
	 */
	public java.util.List getNamedQuery(String name, Map params) throws DataAccessException
	{
		try
		{
			Session session = getSession();
			Query q = session.getNamedQuery(name);
			if (null != params)
			{
				for (Iterator i = params.entrySet().iterator(); i.hasNext();)
				{
					Map.Entry entry = (Map.Entry)i.next();
					setParameterValue(q, (String)entry.getKey(), entry.getValue());
				}
			}
			return q.list();
		}
		catch (HibernateException ex)
		{
			throw SessionFactoryUtils.convertHibernateAccessException(ex);
		}
	}

	/**
	 * Execute a query.
	 * 
	 * @param query
	 *           a query expressed in Hibernate's query language
	 * @return a distinct list of instances (or arrays of instances)
	 */
	public java.util.List find(String query, Object obj, Type type) throws DataAccessException
	{
		try
		{
			Session session = getSession();
			return null; // s.find(query, obj, type);

		}
		catch (HibernateException ex)
		{
			throw SessionFactoryUtils.convertHibernateAccessException(ex);
		}
	}

	/**
	 * Execute a query.
	 * 
	 * @param query
	 *           a query expressed in Hibernate's query language
	 * @return a distinct list of instances (or arrays of instances)
	 */
	public java.util.List find(String query, Object[] obj, Type[] type) throws DataAccessException
	{
		try
		{
			Session session = getSession();
			return null; // s.find(query, obj, type);
		}
		catch (HibernateException ex)
		{
			throw SessionFactoryUtils.convertHibernateAccessException(ex);
		}
	}

	/**
	 * Return a Criteria object that relates to the DAO's table. A session will
	 * be created if an open one is not located. This session must be closed!
	 */
	protected Criteria createCriteria()
	{
		Session session = getSession();
		return session.createCriteria(getReferenceClass());
	}

	/**
	 * Return the property of the class you would like to use for default
	 * ordering
	 * 
	 * @return the property name
	 */
	public String getDefaultOrderProperty()
	{
		return null;
	}

	/**
	 * Convenience method to set paramers in the query given based on the actual
	 * object type in passed in as the value. You may need to add more
	 * functionaly to this as desired (or not use this at all).
	 * 
	 * @param query
	 *           the Query to set
	 * @param position
	 *           the ordinal position of the current parameter within the query
	 * @param value
	 *           the object to set as the parameter
	 */
	protected void setParameterValue(Query query, int position, Object value)
	{
		if (null == value)
		{
			return;
		}
		else if (value instanceof Boolean)
		{
			query.setBoolean(position, ((Boolean)value).booleanValue());
		}
		else if (value instanceof String)
		{
			query.setString(position, (String)value);
		}
		else if (value instanceof Integer)
		{
			query.setInteger(position, ((Integer)value).intValue());
		}
		else if (value instanceof Long)
		{
			query.setLong(position, ((Long)value).longValue());
		}
		else if (value instanceof Float)
		{
			query.setFloat(position, ((Float)value).floatValue());
		}
		else if (value instanceof Double)
		{
			query.setDouble(position, ((Double)value).doubleValue());
		}
		else if (value instanceof BigDecimal)
		{
			query.setBigDecimal(position, (BigDecimal)value);
		}
		else if (value instanceof Byte)
		{
			query.setByte(position, ((Byte)value).byteValue());
		}
		else if (value instanceof Calendar)
		{
			query.setCalendar(position, (Calendar)value);
		}
		else if (value instanceof Character)
		{
			query.setCharacter(position, ((Character)value).charValue());
		}
		else if (value instanceof Timestamp)
		{
			query.setTimestamp(position, (Timestamp)value);
		}
		else if (value instanceof Date)
		{
			query.setDate(position, (Date)value);
		}
		else if (value instanceof Short)
		{
			query.setShort(position, ((Short)value).shortValue());
		}
	}

	/**
	 * Convenience method to set paramers in the query given based on the actual
	 * object type in passed in as the value. You may need to add more
	 * functionaly to this as desired (or not use this at all).
	 * 
	 * @param query
	 *           the Query to set
	 * @param key
	 *           the key name
	 * @param value
	 *           the object to set as the parameter
	 */
	protected void setParameterValue(Query query, String key, Object value)
	{
		if (null == key || null == value)
		{
			return;
		}
		else if (value instanceof Boolean)
		{
			query.setBoolean(key, ((Boolean)value).booleanValue());
		}
		else if (value instanceof String)
		{
			query.setString(key, (String)value);
		}
		else if (value instanceof Integer)
		{
			query.setInteger(key, ((Integer)value).intValue());
		}
		else if (value instanceof Long)
		{
			query.setLong(key, ((Long)value).longValue());
		}
		else if (value instanceof Float)
		{
			query.setFloat(key, ((Float)value).floatValue());
		}
		else if (value instanceof Double)
		{
			query.setDouble(key, ((Double)value).doubleValue());
		}
		else if (value instanceof BigDecimal)
		{
			query.setBigDecimal(key, (BigDecimal)value);
		}
		else if (value instanceof Byte)
		{
			query.setByte(key, ((Byte)value).byteValue());
		}
		else if (value instanceof Calendar)
		{
			query.setCalendar(key, (Calendar)value);
		}
		else if (value instanceof Character)
		{
			query.setCharacter(key, ((Character)value).charValue());
		}
		else if (value instanceof Timestamp)
		{
			query.setTimestamp(key, (Timestamp)value);
		}
		else if (value instanceof Date)
		{
			query.setDate(key, (Date)value);
		}
		else if (value instanceof Short)
		{
			query.setShort(key, ((Short)value).shortValue());
		}
	}

	/**
	 * /** Used by the base DAO classes but here for your modification Load
	 * object matching the given key and return it.
	 */
	protected Object load(Class refClass, Serializable key) throws DataAccessException
	{
		try
		{
			Session session = getSession();
			return session.load(refClass, key);
		}
		catch (HibernateException ex)
		{
			throw SessionFactoryUtils.convertHibernateAccessException(ex);
		}
	}

	/**
	 * Used by the base DAO classes but here for your modification Persist the
	 * given transient instance, first assigning a generated identifier. (Or
	 * using the current value of the identifier property if the assigned
	 * generator is used.)
	 */
	protected Serializable save(Object obj) throws DataAccessException
	{
		try
		{
			Session session = getSession();
			return session.save(obj);
		}
		catch (HibernateException ex)
		{
			throw SessionFactoryUtils.convertHibernateAccessException(ex);
		}
	}

	/**
	 * Used by the base DAO classes but here for your modification Either save()
	 * or update() the given instance, depending upon the value of its identifier
	 * property.
	 */
	protected void saveOrUpdate(Object obj) throws DataAccessException
	{
		try
		{
			Session session = getSession();
			session.saveOrUpdate(obj);
		}
		catch (HibernateException ex)
		{
			throw SessionFactoryUtils.convertHibernateAccessException(ex);
		}
	}

	/**
	 * Used by the base DAO classes but here for your modification Update the
	 * persistent state associated with the given identifier. An exception is
	 * thrown if there is a persistent instance with the same identifier in the
	 * current session.
	 * 
	 * @param obj
	 *           a transient instance containing updated state
	 */
	protected void update(Object obj) throws DataAccessException
	{
		try
		{
			Session session = getSession();
			session.update(obj);
		}
		catch (HibernateException ex)
		{
			throw SessionFactoryUtils.convertHibernateAccessException(ex);
		}
	}

	/**
	 * Used by the base DAO classes but here for your modification Remove a
	 * persistent instance from the datastore. The argument may be an instance
	 * associated with the receiving Session or a transient instance with an
	 * identifier associated with existing persistent state.
	 */
	protected void delete(Object obj) throws DataAccessException
	{
		try
		{
			Session session = getSession();
			session.delete(obj);
		}
		catch (HibernateException ex)
		{
			throw SessionFactoryUtils.convertHibernateAccessException(ex);
		}
	}

	/**
	 * Used by the base DAO classes but here for your modification Re-read the
	 * state of the given instance from the underlying database. It is
	 * inadvisable to use this to implement long-running sessions that span many
	 * business tasks. This method is, however, useful in certain special
	 * circumstances.
	 */
	protected void refresh(Object obj) throws DataAccessException
	{
		try
		{
			Session session = getSession();
			session.refresh(obj);
		}
		catch (HibernateException ex)
		{
			throw SessionFactoryUtils.convertHibernateAccessException(ex);
		}
	}
}