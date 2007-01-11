package wicket.kronos.plugins.ToDo;

import java.io.Serializable;
import java.util.Calendar;

/**
 * @author postma
 */
@SuppressWarnings("boxing")
public class ToDoItem implements Serializable {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private boolean delete;

	private String todoUUID = null;

	private String title = null;

	private String subject = null;

	private String content = null;

	private Boolean done = false;

	private Calendar date = null;

	/**
	 * Constructor.
	 */
	public ToDoItem()
	{

	}

	/**
	 * Constructor for creating a new todo item for which all parameters are known
	 * 
	 * @param todoUUID
	 * @param title
	 * @param subject
	 * @param content
	 * @param done
	 * @param date
	 */
	public ToDoItem(String todoUUID, String title, String subject, String content, Boolean done,
			Calendar date)
	{
		assert (title != null);
		assert (subject != null);
		assert (content != null);
		assert (done != null);
		this.delete = false;
		this.todoUUID = todoUUID;
		this.title = title;
		this.subject = subject;
		this.content = content;
		this.done = done;
		this.date = date;
	}

	/**
	 * @return the delete
	 */
	public boolean isDelete()
	{
		return delete;
	}

	/**
	 * @param delete
	 */
	public void setDelete(boolean delete)
	{
		this.delete = delete;
	}

	/**
	 * @return the todoUUID
	 */
	public String getTodoUUID()
	{
		return todoUUID;
	}

	/**
	 * @param todoUUID
	 */
	public void setTodoUUID(String todoUUID)
	{
		this.todoUUID = todoUUID;
	}

	/**
	 * @return the content
	 */
	public String getContent()
	{
		return content;
	}

	/**
	 * @param content
	 */
	public void setContent(String content)
	{
		this.content = content;
	}

	/**
	 * @return the date
	 */
	public Calendar getDate()
	{
		return date;
	}

	/**
	 * @param date
	 */
	public void setDate(Calendar date)
	{
		this.date = date;
	}

	/**
	 * @return the done
	 */
	public Boolean getDone()
	{
		return done;
	}

	/**
	 * @param done
	 */
	public void setDone(Boolean done)
	{
		this.done = done;
	}

	/**
	 * @return the subject
	 */
	public String getSubject()
	{
		return subject;
	}

	/**
	 * @param subject
	 */
	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	/**
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param title
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

}
