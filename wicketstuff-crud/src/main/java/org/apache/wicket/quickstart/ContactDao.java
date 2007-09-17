/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.quickstart;

import java.util.Iterator;
import java.util.List;

/**
 * The implementation-independent DAO interface. Defines the operations required
 * to be supported by an implementation.
 * 
 * @author igor
 */
public interface ContactDao
{
	/**
	 * Load a {@link Contact} from the DB, given it's <tt>id</tt>.
	 * 
	 * @param id
	 *            The id of the Contact to load.
	 * @return Contact
	 */
	Contact load(long id);

	/**
	 * Save the contact to the DB
	 * 
	 * @param contact
	 * @return persistent instance of contact
	 */
	Contact save(Contact contact);

	/**
	 * Delete a {@link Contact} from the DB, given it's <tt>id</tt>.
	 * 
	 * @param id
	 *            The id of the Contact to delete.
	 */
	void delete(long id);

	/**
	 * Query the DB, using the supplied query details.
	 * 
	 * @param qp
	 *            Query Paramaters to use.
	 * @return The results of the query as an Iterator.
	 */
	Iterator<Contact> find(QueryParam qp, Contact filter);

	/**
	 * Return the number of Contacts in the DB.
	 * 
	 * @return count
	 */
	int count(Contact filter);

	/**
	 * Returns the list of all unique last names in the database
	 * 
	 * @return the list of all unique last names in the database
	 */
	List<String> getUniqueLastNames();
}
