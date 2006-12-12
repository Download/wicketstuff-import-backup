package wicket.kronos;

import java.io.IOException;

import javax.jcr.RepositoryException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jackrabbit.core.TransientRepository;
import org.apache.jackrabbit.core.nodetype.InvalidNodeTypeDefException;
import org.apache.jackrabbit.core.nodetype.compact.ParseException;

import wicket.ISessionFactory;
import wicket.Session;
import wicket.authentication.AuthenticatedWebApplication;
import wicket.authentication.AuthenticatedWebSession;
import wicket.authentication.pages.LoginPage;
import wicket.markup.html.WebPage;
import wicket.protocol.http.WebApplication;

import wicket.kronos.adminpage.AdminPage;
import wicket.kronos.frontpage.Frontpage;

/**
 * Application object for your web application. If you want to run this
 * application without deploying, run the Start class.
 * 
 * @see wicket.kronos.Start#main(String[])
 */
public class KronosApplication extends AuthenticatedWebApplication {
	/** Logging */
	private static final Log log = LogFactory.getLog(KronosApplication.class);

	/** A repository from JackRabbit */
	private TransientRepository repository = null;

	@Override
	protected void init()
	{
		try
		{
			this.repository = new TransientRepository();
			DataProcessor.repositoryStartup();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		catch (InvalidNodeTypeDefException e)
		{
			e.printStackTrace();
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		catch (RepositoryException e)
		{
			e.printStackTrace();
		}

		mountBookmarkablePage("/admin", AdminPage.class);

	}

	/**
	 * @see wicket.Application#getHomePage()
	 */
	@Override
	public Class getHomePage()
	{
		return Frontpage.class;
	}

	/**
	 * @see wicket.protocol.http.WebApplication#getSessionFactory()
	 */
	@Override
	public ISessionFactory getSessionFactory()
	{
		return new ISessionFactory() {
			public Session newSession()
			{
				return new KronosSession(KronosApplication.this);
			}
		};
	}

	@Override
	protected Class<? extends WebPage> getSignInPageClass()
	{
		return LoginPage.class;
	}

	@Override
	protected Class<? extends AuthenticatedWebSession> getWebSessionClass()
	{
		return null;
	}

	/**
	 * @return The JackRabbit repository
	 */
	public TransientRepository getRepository()
	{
		return this.repository;
	}

	public static KronosApplication get()
	{
		return (KronosApplication) WebApplication.get();
	}
}