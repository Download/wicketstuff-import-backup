package wicket.kronos.plugins.ToDo.adminpage;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import wicket.extensions.markup.html.datepicker.DatePicker;
import wicket.kronos.KronosSession;
import wicket.kronos.adminpage.AdminPage;
import wicket.kronos.plugins.ToDo.ToDoItem;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.form.CheckBox;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.TextArea;
import wicket.markup.html.form.TextField;
import wicket.markup.html.panel.Panel;
import wicket.model.CompoundPropertyModel;

/**
 * @author postma
 *
 */
public class AdminToDoEdit extends Panel {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private InputForm inputForm = null;

	/**
	 * @param wicketId
	 * @param todoItem
	 */
	public AdminToDoEdit(String wicketId, ToDoItem todoItem)
	{
		super(wicketId);
		
		inputForm = new InputForm("admintodoedit", todoItem);
		add(inputForm);
	}

	private class InputForm extends Form {
		
		/**
		 * Default serialVersionUID
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * @param name
		 * @param todoItem
		 */
		public InputForm(String name, ToDoItem todoItem)
		{
			super(name, new CompoundPropertyModel(todoItem));
			add(new TextField("title"));
			add(new TextField("subject"));
			add(new TextArea("content"));
			add(new CheckBox("done"));
		}
		
		public void onSubmit()
		{
			Session jcrSession = KronosSession.get().getJCRSession();
			ToDoItem item = (ToDoItem)this.getModelObject();
			try
			{
				Node todoItemNode = jcrSession.getNodeByUUID(item.getTodoUUID());
				todoItemNode.setProperty("kronos:name", item.getTitle());
				todoItemNode.setProperty("kronos:subject", item.getSubject());
				todoItemNode.setProperty("kronos:content", item.getContent());
				todoItemNode.setProperty("kronos:done", item.getDone());
				
				jcrSession.save();
				
				setResponsePage(AdminPage.class);
			}
			catch (ItemNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (RepositoryException e)
			{
				e.printStackTrace();
			}
		}
	}
}
