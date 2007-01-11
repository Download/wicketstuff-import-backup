package wicket.kronos.plugins.blog.panels;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

/**
 * @author postma
 */
public class BlogPost implements Serializable {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String postUUID = null;

	private Calendar date = null;

	private String title = null;

	private String text = null;

	private String author = null;

	private List<Comment> comments = null;

	private int nrComments = 0;

	/**
	 * Constructor.
	 * 
	 * @param postUUID
	 * @param date
	 * @param title
	 * @param text
	 * @param author
	 * @param comments
	 * @param nrComments
	 */
	public BlogPost(String postUUID, Calendar date, String title, String text, String author,
			List<Comment> comments, int nrComments)
	{
		assert (date != null);
		assert (title != null);
		assert (text != null);
		assert (author != null);
		this.postUUID = postUUID;
		this.date = date;
		this.title = title;
		this.text = text;
		this.author = author;
		this.comments = comments;
		this.nrComments = nrComments;
	}

	/**
	 * @return the postUUID
	 */
	public String getPostUUID()
	{
		return postUUID;
	}

	/**
	 * @param postUUID
	 */
	public void setPostUUID(String postUUID)
	{
		this.postUUID = postUUID;
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
	 * @return the comments
	 */
	public List<Comment> getComments()
	{
		return comments;
	}

	/**
	 * @param comments
	 */
	public void setComments(List<Comment> comments)
	{
		this.comments = comments;
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
	 * @return the nrComments
	 */
	public int getNrComments()
	{
		return nrComments;
	}

	/**
	 * @param nrComments
	 */
	public void setNrComments(int nrComments)
	{
		this.nrComments = nrComments;
	}
}
