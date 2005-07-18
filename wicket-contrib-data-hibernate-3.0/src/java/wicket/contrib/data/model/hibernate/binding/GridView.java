package wicket.contrib.data.model.hibernate.binding;

import java.util.List;

import wicket.Component;
import wicket.IFeedback;
import wicket.WicketRuntimeException;
import wicket.markup.html.form.Form;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.PageableListView;
import wicket.model.IModel;
import wicket.model.Model;

/**
 * A form that contains a list view. The form's model is always the item that's
 * currently being worked on in the list view, or an empty model if no item is
 * being edited. To allow this grid to support edits, each list item must
 * contain an InlineEditLink as well as an InlineSubmitLink. To support
 * deletions, each ListItem must contain an InlineDeleteLink.
 * 
 * @author Phil Kulak
 */
public abstract class GridView extends Form
{
	/** used when no model is being edited */
	public static final IModel EMPTY_MODEL = new Model();

	private FormList listView;

	/**
	 * @param id
	 *            the id of the form
	 * @param list
	 *            the list of items wrapped in a model
	 * @param feedback
	 *            a feedback panel
	 * @param perPage
	 *            the number of items to display per page
	 * @param dao
	 *            the dao to use to interact with Hibernate
	 */
	public GridView(String id, IModel list, IFeedback feedback, int perPage)
	{
		super(id, EMPTY_MODEL, feedback);
		listView = new FormList(list, perPage);
		add(listView);
	}

	/**
	 * @return the list being used internally by this form.
	 */
	public PageableListView getListView()
	{
		return listView;
	}

	/**
	 * Delegated to from the internal list view.
	 * 
	 * @param item
	 *            the item to populate
	 */
	protected abstract void populateItem(ListItem item);
    
    protected abstract IModel getListItemModel(Object object);
    
    public abstract void onSubmit();
    
    protected abstract void delete(Object object);

	/**
	 * This class is private because it delegates all it's functionality to the
	 * ListViewForm so that users don't even have to be aware of it.
	 */
	private final class FormList extends PageableListView
	{
		public FormList(IModel model, int perPage)
		{
			super("rows", model, perPage);
			setOptimizeItemRemoval(true);
		}

		protected void populateItem(ListItem item)
		{
			GridView.this.populateItem(item);
		}
        
        public IModel getListItemModel(IModel listViewModel, int index)
    	{
    		Object object = ((List) listViewModel.getObject(this)).get(index);
            return GridView.this.getListItemModel(object);
    	}
	}
    
    public static void deleteRowModel(Component component)
    {
        ListItem item = findListItem(component);
        item.modelChanging();
        findForm(component).delete(item.getModelObject());
        item.modelChanged();
    }
    
    public static void removeEdit(Component component)
    {
        findForm(component).removeEditModel();
    }
    
    public static void setEdit(Component component)
    {
        findForm(component).setEditModel(findListItem(component).getModel());
    }
    
	/**
	 * @param component
	 *            the component to check
	 * @return true if the component is currently being edited
	 */
	public static boolean isEdit(Component component)
	{
		return findForm(component).isEditModel(findListItem(component).getModel());
	}
    
	/**
	 * @param component
	 *            the component to start the search from
	 * @return the first HibernateGridView parent found
	 */
	private static GridView findForm(Component component)
	{
		GridView form = (GridView) component
				.findParent(GridView.class);
		if (form == null)
		{
			throw new WicketRuntimeException("An inline component must be a "
					+ "child of a ListViewForm.");
		}
		return form;
	}

	/**
	 * This method finds the parent ListView that is closest to the first parent
	 * HibernateGridView. This allows a component to be nested arbitrarilly deep
	 * in lists, but still be aware of its edit status.
	 * 
	 * @param component
	 *            the component to start the search from
	 * @return the ListItem containing the model for this row
	 */
	private static ListItem findListItem(Component component)
	{
		ListItem item = findListItemRec(component);

		if (item == null)
		{
			throw new WicketRuntimeException("A suitable ListItem could not "
					+ "be found in the component tree.");
		}
		return item;
	}

	private static ListItem findListItemRec(Component component)
	{
		ListItem item = null;

		while ((component = component.getParent()) != null)
		{
			if (component instanceof ListItem)
			{
				item = (ListItem) component;
			}
			else if (component instanceof GridView)
			{
				return item;
			}
		}
		return null;
	}
    
	/**
	 * @return true if the given model is the one currently beind edited.
	 */
	protected boolean isEditModel(IModel model)
	{
		if (getModelObject() == null)
		{
			return false;
		}
		return model.getObject(this).equals(getModelObject());
	}

	/**
	 * @param model
	 *            the model to set as the edit.
	 */
	protected void setEditModel(IModel model)
	{
		setModel(model);
	}

	/**
	 * Sets the form's model object to null, removing the edit.
	 */
	protected void removeEditModel()
	{
		setModel(EMPTY_MODEL);
	}
}
