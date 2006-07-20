package wicket.contrib.data.model.hibernate;

import java.util.Iterator;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

/**
 * A {@Link wicket.contrib.dataview.IDataProvider} with support for criteria
 * queries. This makes it dead easy to implement a DataProvider since there is
 * only one abstract method to implement. Paging and ordering are built using
 * that base criteria. If you would like to make your count query more efficiant
 * (for example, by removing unnecessary joins), consider extending
 * {@link HibernateDataProvider} instead, which will give you that control.
 * 
 * @author Phil Kulak
 */
public abstract class CriteriaDataProvider<T, V> extends HibernateDataProvider<T, V>
{
	public CriteriaDataProvider(IHibernateDao hibernateDao)
	{
		super(hibernateDao);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Iterator<T> iterator(int first, int count, Session session)
	{
		return addOrdering(allItems(session)).setFirstResult(first).setMaxResults(count)
				.list().iterator();
	}

	@Override
	protected Integer size(Session session)
	{
		return (Integer) allItems(session).setProjection(Projections.rowCount())
				.uniqueResult();
	}

	/**
	 * Returns a criteria that will return every item with no pagination or
	 * ordering set up. This base criteria will be used to set projections,
	 * limits, and orderings.
	 * 
	 * @param session
	 *            the session to use to build the criteria
	 * @return a base criteria that will return all entities
	 */
	protected abstract Criteria allItems(Session session);
}
