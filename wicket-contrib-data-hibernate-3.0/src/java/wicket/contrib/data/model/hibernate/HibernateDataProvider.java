package wicket.contrib.data.model.hibernate;

import java.util.Iterator;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import wicket.contrib.data.model.hibernate.IHibernateDao.IHibernateCallback;
import wicket.extensions.markup.html.repeater.data.sort.SortParam;
import wicket.extensions.markup.html.repeater.data.sort.SortableDataProvider;
import wicket.model.IModel;
import wicket.model.Model;

/**
 * A data provider for working with Hibernate 3.
 * 
 * @author Phil Kulak
 */
public abstract class HibernateDataProvider extends SortableDataProvider
{
	private IModel hibernateDao;
	
	private boolean unproxy = false;
	
	/**
	 * Set to true to have all models unproxied.
	 */
	public void setUnproxy(boolean unproxy)
	{
		this.unproxy = unproxy;
	}

	public HibernateDataProvider(IHibernateDao hibernateDao)
	{
		this.hibernateDao = new Model(hibernateDao);
	}

	public HibernateDataProvider(IModel hibernateDao)
	{
		this.hibernateDao = hibernateDao;
	}

	public IModel model(Object object)
	{
		return new HibernateModel(object, hibernateDao, unproxy);
	}

	public Iterator iterator(final int first, final int count)
	{
		return (Iterator) getHibernateDao().execute(new IHibernateCallback()
		{
			public Object execute(Session session)
			{
				return iterator(first, count, session);
			}
		});
	}

	public int size()
	{
		Integer result = (Integer) getHibernateDao().execute(new IHibernateCallback()
		{
			public Object execute(Session session)
			{
				return size(session);
			}
		});

		return result.intValue();
	}
	
	/**
	 * Formats the current sort states an a query string suitable for appending
	 * at the end of SQL, HQL or other queries.
	 * 
	 * @param alias the column alias each sort field will reference
	 * @return an ORDER BY query fragment
	 */
	protected String getSqlOrderBy(String alias)
	{
		StringBuffer orderBy = new StringBuffer();
		if (getSortList().size() > 0)
		{
			orderBy.append(" ORDER BY");
			boolean addComma = false;
			for (Iterator i = getSortList().iterator(); i.hasNext();)
			{
				SortParam sortParam = (SortParam) i.next();
				if (addComma) orderBy.append(",");
				orderBy.append(" ");
				
				if (alias != null)
				{
					orderBy.append(alias + ".");
				}
				
				orderBy.append(sortParam.getProperty());
				orderBy.append(sortParam.isAscending() ? " ASC" : " DESC");
			
				addComma = true;
			}
		}
		return orderBy.toString();
	}
	
	/**
	 * Formats the current sort states an a query string suitable for appending
	 * at the end of SQL, HQL or other queries.
	 * 
	 * @return an ORDER BY query fragment
	 */
	protected String getSqlOrderBy()
	{
		return getSqlOrderBy(null);
	}
	
	/**
	 * Adds all the current orderings to the criteria.
	 * 
	 * @param criteria the criteria to add the orderings to
	 * @return the criteria with the added orderings
	 */
	protected final Criteria addOrdering(Criteria criteria)
	{
		for (Iterator i = getSortList().iterator(); i.hasNext();)
		{
			criteria.addOrder(makeOrder((SortParam) i.next()));
		}
		return criteria;
	}

	private Order makeOrder(SortParam param)
	{
		return param.isAscending() ? Order.asc(param.getProperty()) : Order.desc(param
				.getProperty());
	}
	
	private IHibernateDao getHibernateDao() {
		return (IHibernateDao) hibernateDao.getObject(null);
	}

	protected abstract Iterator iterator(int first, int count, Session session);

	protected abstract Object size(Session session);
}
