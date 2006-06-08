/*
 * $Id$
 * $Revision$
 * $Date$
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.contrib.phonebook;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * implements {@link ContactDao}.
 * 
 * @author igor
 */
public class HibernateContactDao implements ContactDao {

	private SessionFactory factory;

	/**
	 * Setter for session factory. Spring will use this to inject the session
	 * factory into the dao.
	 * 
	 * @param factory
	 *            hibernate session factory
	 */
	public void setSessionFactory(SessionFactory factory) {
		this.factory = factory;
	}

	/**
	 * Helper method for retrieving hibernate session
	 * 
	 * @return
	 */
	protected Session getSession() {
		return factory.getCurrentSession();
	}

	/**
	 * Load a {@link Contact} from the DB, given it's <tt>id</tt> .
	 * 
	 * @param id
	 *            The id of the Contact to load.
	 * @return Contact
	 */
	public Contact load(long id) {
		return (Contact) getSession().get(Contact.class, new Long(id));
	}

	/**
	 * Save the contact to the DB
	 * 
	 * @param contact
	 * @return persistent instance of contact
	 */
	public Contact save(Contact contact) {
		return (Contact) getSession().merge(contact);
	}

	/**
	 * Delete a {@link Contact} from the DB, given it's <tt>id</tt>.
	 * 
	 * @param id
	 *            The id of the Contact to delete.
	 */
	public void delete(long id) {
		getSession().delete(load(id));
	}

	/**
	 * Query the DB, using the supplied query details.
	 * 
	 * @param qp
	 *            Query Paramaters to use.
	 * @return The results of the query as an Iterator.
	 */
	public Iterator find(final QueryParam qp, Contact filter) {
		return buildFindQuery(qp, filter, false).iterate();

	}

	/**
	 * Return the number of Contacts in the DB.
	 * 
	 * @return count
	 */
	public int count(Contact filter) {
		return ((Integer) buildFindQuery(null, filter, true).uniqueResult())
				.intValue();
	}

	/**
	 * Returns a list of unique last names
	 */
	public List getUniqueLastNames() {
		return getSession().createQuery(
				"select distinct target.lastname "
						+ " from Contact target order by target.lastname")
				.list();
	}

	/**
	 * builds a query object to statify the provided parameters
	 * 
	 * @param qp
	 *            sorting and paging criteria
	 * @param filter
	 *            filter criteria
	 * @param count
	 *            true if this is a query meant to retrieve the number of rows
	 * @return query object that satisfies the provided criteria
	 */
	protected Query buildFindQuery(QueryParam qp, Contact filter, boolean count) {
		StringBuffer hql = new StringBuffer();
		if (count) {
			hql.append("select count(*) ");
		}
		hql.append(" from Contact target where 1=1 ");
		if (filter.getFirstname() != null) {
			hql.append("and upper(target.firstname) like (:firstname)");
		}
		if (filter.getLastname() != null) {
			hql.append("and upper(target.lastname) like (:lastname)");
		}
		if (filter.getPhone() != null) {
			hql.append("and upper(target.phone) like (:phone)");
		}
		if (filter.getEmail() != null) {
			hql.append("and upper(target.email) like (:email)");
		}

		if (!count && qp != null && qp.hasSort()) {
			hql.append("order by upper(target.").append(qp.getSort()).append(
					") ").append((qp.isSortAsc()) ? " asc" : " desc");
		}

		Query query = getSession().createQuery(hql.toString());

		if (!count && qp != null) {
			query.setFirstResult(qp.getFirst()).setMaxResults(qp.getCount());
		}

		if (filter.getFirstname() != null) {
			query.setParameter("firstname", "%"
					+ filter.getFirstname().toUpperCase() + "%");
		}
		if (filter.getLastname() != null) {
			query.setParameter("lastname", "%"
					+ filter.getLastname().toUpperCase() + "%");
		}
		if (filter.getPhone() != null) {
			query.setParameter("phone", "%" + filter.getPhone().toUpperCase()
					+ "%");
		}
		if (filter.getEmail() != null) {
			query.setParameter("email", "%" + filter.getEmail().toUpperCase()
					+ "%");
		}

		return query;
	}

}
