package wicket.kronos.plugins.ToDo.adminpage;

import java.util.List;

import wicket.kronos.plugins.ToDo.ToDoItem;
import wicket.markup.html.panel.Panel;

/**
 * @author postma
 *
 */
public class AdminToDoPanel extends Panel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * @param wicketId
	 * @param todoItems 
	 */
	public AdminToDoPanel(String wicketId, List<ToDoItem> todoItems)
	{
		super(wicketId);
		add(new AdminToDoOverview("admintodopanel", todoItems));
	}
	
	/**
	 * @param wicketId
	 * @param todoItem
	 */
	public AdminToDoPanel(String wicketId, ToDoItem todoItem)
	{
		super(wicketId);
		if (todoItem.equals(null))
		{
			add(new AdminNewToDo("admintodopanel"));
		} else
		{
			add(new AdminToDoEdit("admintodopanel", todoItem));
		}
	}
}
