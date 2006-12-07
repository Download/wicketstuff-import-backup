package wicket.kronos.plugins.ToDo.frontpage;

import java.util.List;

import wicket.kronos.plugins.ToDo.ToDoItem;
import wicket.markup.html.panel.Panel;

/**
 * @author postma
 *
 */
public class FrontToDoPanel extends Panel {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param wicketId
	 * @param todoItems 
	 */
	public FrontToDoPanel(String wicketId, List<ToDoItem> todoItems)
	{
		super(wicketId);
		add(new FrontToDoOverview("fronttodopanel", todoItems));
	}
	
	/**
	 * @param wicketId
	 */
	public FrontToDoPanel(String wicketId)
	{
		super(wicketId);
		add(new FrontNewToDo("fronttodopanel"));
	}
}
