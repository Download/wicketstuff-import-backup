package wicket.kronos.plugins.blog.panels;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Model for all blog input variables
 * 
 * @author postma
 */
public class BlogInputModel implements Serializable {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	Calendar date = null;

	String title = null;

	String author = null;

	String text = null;

	/**
	 * Empty constructor
	 */
	public BlogInputModel()
	{

	}

	/**
	 * Constructor for changing the input
	 * 
	 * @param title
	 * @param text
	 */
	public BlogInputModel(String title, String text)
	{
		assert (title != null);
		assert (text != null);
		this.title = title;
		this.text = text;
	}

	/**
	 * Creating new input, thus setting all parameters
	 * 
	 * @param date
	 * @param title
	 * @param author
	 * @param text
	 */
	public BlogInputModel(Calendar date, String title, String author, String text)
	{
		this.date = date;
		this.title = title;
		this.author = author;
		this.text = text;
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

	/**
	 * @return the author
	 */
	public String getAuthor()
	{
		return author;
	}

	/**
	 * @param author
	 */
	public void setAuthor(String author)
	{
		this.author = author;
	}

	/**
	 * @return the text
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * @param text
	 */
	public void setText(String text)
	{
		this.text = text;
	}
}
