/*
 * $Id$ $Revision$ $Date$
 * 
 * ==================================================================== Licensed
 * under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the
 * License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.examples.cdapp;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;

import wicket.AttributeModifier;
import wicket.PageParameters;
import wicket.RequestCycle;
import wicket.examples.cdapp.model.CD;
import wicket.examples.cdapp.model.CdDao;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.TextField;
import wicket.markup.html.link.Link;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.PageableListView;
import wicket.markup.html.navigation.paging.PagingNavigation;
import wicket.markup.html.navigation.paging.PagingNavigationLink;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.model.IModel;
import wicket.model.Model;
import wicket.model.PropertyModel;


/**
 * Page that nests a search form and a pageable and sortable results table.
 * 
 * @author Eelco Hillenius
 */
public class SearchPage extends CdAppBasePage
{
	/** Logger. */
	private static Log log = LogFactory.getLog(SearchPage.class);

	/** list view for search results. */
	private SearchCDResultsListView resultsListView;

	/** search form. */
	private final SearchForm searchForm;

	/** model for searching. */
	private final SearchModel searchModel;

	/**
	 * Construct.
	 */
	public SearchPage()
	{
		this(null);
	}

	/**
	 * Construct.
	 * 
	 * @param pageParameters
	 *            parameters for this page
	 */
	public SearchPage(PageParameters pageParameters)
	{
		super();
		final int rowsPerPage = 8;
		searchModel = new SearchModel(rowsPerPage);

		FeedbackPanel pageFeedback = new FeedbackPanel("feedback");
		searchForm = new SearchForm("searchForm");
		add(searchForm);

		add(pageFeedback);
		resultsListView = new SearchCDResultsListView("results", searchModel, rowsPerPage);
		add(resultsListView);
		WebMarkupContainer resultsTableHeader = new WebMarkupContainer("resultsHeader")
		{
			public boolean isVisible()
			{
				return searchModel.hasResults();
			}
		};
		resultsTableHeader.add(new SortLink("sortOnArtist", "performers"));
		resultsTableHeader.add(new SortLink("sortOnTitle", "title"));
		resultsTableHeader.add(new SortLink("sortOnYear", "year"));
		resultsTableHeader.add(new SortLink("sortOnLabel", "label"));
		resultsTableHeader.setVisible(false); // non-visible as there are no
		// results yet
		add(resultsTableHeader);
		add(new DetailLink("newCdLink", null)); // add with null; the model and
		// the detail page are smart enough to know we want a new one then
		add(new CDTableNavigation("navigation", resultsListView));
	}

	/**
	 * Sets the result page to the first page.
	 */
	public void setCurrentResultPageToFirst()
	{
		resultsListView.setCurrentPage(0);
	}

	/**
	 * Gets the current number of results.
	 * 
	 * @return the current number of results
	 */
	private int getNumberOfResults()
	{
		return ((List)resultsListView.getModelObject()).size();
	}

	/**
	 * Form for search actions.
	 */
	private class SearchForm extends Form
	{
		/** search property to set. */
		private String search;

		/**
		 * Constructor
		 * 
		 * @param id
		 *            id of the form component
		 */
		public SearchForm(final String id)
		{
			super(id);
			add(new TextField("search", new PropertyModel(this, "search")));
		}

		/**
		 * @see wicket.markup.html.form.Form#onSubmit()
		 */
		public final void onSubmit()
		{
			searchModel.setSearchString(search); // set search query on model
			setCurrentResultPageToFirst(); // start with first page
			// SearchPage.this.modelChangedStructure();

			if (search != null && (!search.trim().equals("")))
			{
				info(getNumberOfResults() + " results found for query '" + search + "'");
			}
		}

		/**
		 * Gets search property.
		 * 
		 * @return search property
		 */
		public final String getSearch()
		{
			return search;
		}

		/**
		 * Sets search property.
		 * 
		 * @param search
		 *            search property
		 */
		public final void setSearch(String search)
		{
			this.search = search;
		}
	}

	/**
	 * Table for displaying search results.
	 */
	private class SearchCDResultsListView extends PageableListView
	{
		/**
		 * Construct.
		 * 
		 * @param id
		 *            id of the component
		 * @param model
		 *            the model
		 * @param pageSizeInCells
		 *            page size
		 */
		public SearchCDResultsListView(String id, IModel model, int pageSizeInCells)
		{
			super(id, model, pageSizeInCells);
		}

		/**
		 * @see wicket.Component#isVersioned()
		 */
		public boolean isVersioned()
		{
			return true;
		}

		/**
		 * @see PageableListView#populateItem(ListItem)
		 * @param item
		 */
		public void populateItem(final ListItem item)
		{
			final CD cd = (CD)item.getModelObject();
			final Long id = cd.getId();

			// add links to the details
			item.add(new DetailLink("title", id).add(new Label("title", cd.getTitle())));
			item.add(new DetailLink("performers", id).add(new Label("performers", cd
					.getPerformers())));
			item.add(new DetailLink("label", id).add(new Label("label", cd.getLabel())));
			item.add(new DetailLink("year", id).add(new Label("year", (cd.getYear() != null) ? cd
					.getYear().toString() : "")));

			// add a delete link for each found record
			DeleteLink deleteLink = new DeleteLink("delete", cd);
			item.add(deleteLink);
		}
	}

	/** link to detail edit page. */
	private final class DetailLink extends Link
	{
		/**
		 * Construct.
		 * 
		 * @param name
		 *            name of the component
		 * @param id
		 *            the id of the cd
		 */
		public DetailLink(String name, Long id)
		{
			super(name, new Model(id));
		}

		/**
		 * @see wicket.markup.html.link.Link#onClick()
		 */
		public void onClick()
		{
			final RequestCycle requestCycle = getRequestCycle();
			final Long id = (Long)getModelObject();
			requestCycle.setResponsePage(new EditPage(SearchPage.this, id));
		}
	}

	/** Link for deleting a row. */
	private final class DeleteLink extends Link
	{
		/**
		 * Construct.
		 * 
		 * @param name
		 *            name of the component
		 * @param cd
		 *            the cd
		 */
		public DeleteLink(String name, CD cd)
		{
			super(name, new Model(cd.getId()));
			String msg = "if(!confirm('delete cd " + cd.getTitle() + " ?')) return false;";
			add(new AttributeModifier("onclick", true, new Model(msg)));
		}

		/**
		 * @see wicket.markup.html.link.Link#onClick()
		 */
		public void onClick()
		{
			final Long id = (Long)getModelObject();

			CdDao cdDao = getCdDao();
			CD cd = null;
			try
			{
				cd = cdDao.load(id);
			}
			catch (HibernateException e)
			{
				// For some reason (back button, concurrent acces?) the object
				// does not exist
				// anymore. Report an error and return
				SearchPage.this.error("could not delete cd with id " + id
						+ "; it was not found in the database");
				return;
			}

			// inform the list component that a change in its model is about to
			// happen
			resultsListView.modelChanging();

			getCdDao().delete(cd);

			// infor the list component that a change in its model has happened
			resultsListView.modelChanged();

			SearchPage.this.info(" cd deleted");

		}
	}

	/** Link for sorting on a column. */
	private final class SortLink extends Link
	{
		/** order by field. */
		private final String field;

		/**
		 * Construct.
		 * 
		 * @param id
		 *            name of component
		 * @param field
		 *            order by field
		 */
		public SortLink(String id, String field)
		{
			super(id);
			this.field = field;
		}

		/**
		 * Add order by field to query of list.
		 * 
		 * @see wicket.markup.html.link.Link#onClick()
		 */
		public void onClick()
		{
			searchModel.addOrdering(field);
			// SearchPage.this.modelChangedStructure();
		}
	}

	/**
	 * Custom table navigation class that adds extra labels.
	 */
	private static class CDTableNavigation extends PagingNavigation
	{
		/**
		 * Construct.
		 * 
		 * @param id
		 *            the name of the component
		 * @param table
		 *            the table
		 */
		public CDTableNavigation(String id, PageableListView table)
		{
			super(id, table);
		}

		/**
		 * @see wicket.markup.html.list.Loop#populateItem(wicket.markup.html.list.Loop.LoopItem)
		 */
		protected void populateItem(final LoopItem iteration)
		{
			final PagingNavigationLink link = new PagingNavigationLink("pageLink", pageable,
					iteration.getIteration());

			if (iteration.getIteration() > 0)
			{
				iteration.add(new Label("separator", "|"));
			}
			else
			{
				iteration.add(new Label("separator", ""));
			}
			link.add(new Label("pageNumber", String.valueOf(iteration.getIteration() + 1)));
			link.add(new Label("pageLabel", "page"));
			iteration.add(link);
		}
	}
}