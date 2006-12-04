package wicket.kronos.plugins.blog.panels;

import java.io.Serializable;
import java.util.Calendar;

/**
 * @author postma
 */
public class Comment implements Serializable {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String commentUUID = null;

	private String text = null;

	private String author = null;

	private Calendar date = null;

	/**
	 * @param commentUUID
	 * @param text
	 * @param author
	 * @param date
	 */
	public Comment(String commentUUID, String text, String author, Calendar date)
	{
		assert (text != null);
		assert (author != null);
		assert (date != null);
		this.commentUUID = commentUUID;
		this.text = text;
		this.author = author;
		this.date = date;
	}

	/**
	 * @return the commentUUID
	 */
	public String getCommentUUID()
	{
		return commentUUID;
	}

	/**
	 * @param commentUUID
	 *            the commentUUID to set
	 */
	public void setCommentUUID(String commentUUID)
	{
		this.commentUUID = commentUUID;
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
	 *            the author to set
	 */
	public void setAuthor(String author)
	{
		this.author = author;
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
	 *            the date to set
	 */
	public void setDate(Calendar date)
	{
		this.date = date;
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
	 *            the text to set
	 */
	public void setText(String text)
	{
		this.text = text;
	}

}
