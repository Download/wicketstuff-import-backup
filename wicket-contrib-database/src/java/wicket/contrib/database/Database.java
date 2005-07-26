/*
 * $Id$ $Revision:
 * 1.43 $ $Date$
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
package wicket.contrib.database;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Base class for different kinds of databases.
 */
public abstract class Database
{
	/** Log. */
	private static Log log = LogFactory.getLog(Database.class);

	/**
	 * Drops and recreates database tables
	 */
	public abstract void formatTables();
	
	/**
	 * @return Subclass of HibernateDatabaseSession depending on type of database;
	 */
	public abstract DatabaseSession newDatabaseSession();
}
