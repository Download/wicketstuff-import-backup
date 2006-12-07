package wicket.kronos.plugins.ToDo;

import java.io.Serializable;
import java.util.Calendar;

/**
 * @author postma
 *
 */
@SuppressWarnings("boxing")
public class ToDoItem implements Serializable {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private String todoUUID = null;
	String title = null;
	String subject = null;
	String content = null;
	Boolean done = false;
	Calendar date = null;
	
	/**
	 * 
	 */
	public ToDoItem() 
	{
		
	}
	
	/**
	 * @param todoUUID 
	 * @param title
	 * @param subject
	 * @param content
	 * @param done
	 * @param date
	 */
	public ToDoItem(String todoUUID, String title, String subject, String content, Boolean done, Calendar date) 
	{
		assert(title != null);
		assert(subject != null);
		assert(content != null);
		assert(done != null);
		this.todoUUID = todoUUID;
		this.title = title;
		this.subject = subject;
		this.content = content;
		this.done = done;
		this.date = date;
	}

	/**
	 * @return the todoUUID
	 */
	public String getTodoUUID()
	{
		return todoUUID;
	}

	/**
	 * @param todoUUID the todoUUID to set
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
	 * @param content the content to set
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
	 * @param date the date to set
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
	 * @param done the done to set
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
	 * @param subject the subject to set
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
	 * @param title the title to set
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}
	
}
