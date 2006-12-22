package wicket.kronos;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Value;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.apache.jackrabbit.core.nodetype.InvalidNodeTypeDefException;
import org.apache.jackrabbit.core.nodetype.NodeTypeDef;
import org.apache.jackrabbit.core.nodetype.NodeTypeManagerImpl;
import org.apache.jackrabbit.core.nodetype.NodeTypeRegistry;
import org.apache.jackrabbit.core.nodetype.compact.CompactNodeTypeDefReader;
import org.apache.jackrabbit.core.nodetype.compact.ParseException;

import sun.net.www.MimeTable;
import wicket.authentication.User;
import wicket.authorization.strategies.role.Roles;
import wicket.kronos.plugins.IPlugin;
import wicket.kronos.plugins.PluginProperties;

/**
 * @author postma
 */
public final class DataProcessor {

	/**
	 * Generates all the plugins for a certain area that have been published.
	 * All the data is retrieved from the repository
	 * The PluginProperties are sorted by position and order
	 * 
	 * @param area
	 * @return List<IPlugin>
	 */
	@SuppressWarnings("boxing")
	public static List<PluginProperties> getPluginPropertiesObjects()
	{
		List<PluginProperties> plugins = new LinkedList<PluginProperties>();
		
		Session jcrSession = KronosSession.get().getJCRSession();

		try
		{
			Workspace ws = jcrSession.getWorkspace();
			QueryManager qm = ws.getQueryManager();
			Query q = qm.createQuery("//kronos:plugininstantiations/kronos:plugininstance order by @kronos:position ascending, @kronos:order ascending", Query.XPATH);

			QueryResult result = q.execute();
			NodeIterator it = result.getNodes();

			while (it.hasNext())
			{
				Node n = it.nextNode();
				String pluginUUID = null;
				/* Loading the IPlugin extended class */
				pluginUUID = n.getUUID();

				String name = n.getProperty("kronos:name").getString();
				Boolean published = n.getProperty("kronos:published")
						.getBoolean();
				Long order = n.getProperty("kronos:order").getLong();
				Integer intOrder = order.intValue();
				Long position = n.getProperty("kronos:position").getLong();
				Integer intPosition = position.intValue();
				String pluginType = n.getProperty("kronos:pluginType")
						.getString();

				plugins.add(new PluginProperties(pluginUUID, name,
						published, intOrder, intPosition, pluginType));
			}
		}
		catch (RepositoryException e)
		{
			e.printStackTrace();
		}
		return plugins;
	}
	
	public static List<CMSImageResource> getImages()
	{
		List<CMSImageResource> images = new ArrayList<CMSImageResource>();
		
		Session jcrSession = KronosSession.get().getJCRSession();

		try
		{
			Workspace ws = jcrSession.getWorkspace();
			QueryManager qm = ws.getQueryManager();
			Query q = qm.createQuery("//kronos:content/kronos:images/*", Query.XPATH);

			QueryResult result = q.execute();
			NodeIterator it = result.getNodes();

			while (it.hasNext())
			{
				Node imageNode = it.nextNode();
				InputStream input = imageNode.getNode("jcr:content")
				.getProperty("jcr:data").getStream();
		
				/*
				 * Retrieve the image data from the repository and put it into a
				 * byteArray
				 */
				byte[] image;
				String imageName = imageNode.getName();
				ByteArrayOutputStream destination = new ByteArrayOutputStream();
				try
				{
					byte[] buffer = new byte[1024];
					for (int n = input.read(buffer); n != -1; n = input.read(buffer))
					{
						destination.write(buffer, 0, n);
					}
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				finally
				{
					try
					{
						input.close();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				image = destination.toByteArray();
				/*
				 * Create a BannerImageResource with the byteArray as a
				 * parameter
				 */
				images.add(new CMSImageResource(image, imageName));
			}
		}
		catch (RepositoryException e)
		{
			e.printStackTrace();
		}
		
		return images;
	}
	
	public static List getImageNodes()
	{
		List<Node> imageNodes = new ArrayList<Node>();
		
		Session jcrSession = KronosSession.get().getJCRSession();

		try
		{
			Workspace ws = jcrSession.getWorkspace();
			QueryManager qm = ws.getQueryManager();
			Query q = qm.createQuery("//kronos:content/kronos:images/*", Query.XPATH);

			QueryResult result = q.execute();
			NodeIterator it = result.getNodes();

			while (it.hasNext())
			{
				Node n = it.nextNode();
				imageNodes.add(n);
			}
		}
		catch (RepositoryException e)
		{
			e.printStackTrace();
		}
		
		return imageNodes;
	}
	
	/**
	 * 
	 * @param pluginUUID
	 * @return PluginProperties
	 */
	public static PluginProperties getPluginProperties(String pluginUUID)
	{
		PluginProperties properties = null;
		
		Session jcrSession = KronosSession.get().getJCRSession();
		
		Node n;
		try
		{
			n = jcrSession.getNodeByUUID(pluginUUID);
			String name = n.getProperty("kronos:name").getString();
			Boolean published = n.getProperty("kronos:published")
					.getBoolean();
			Long order = n.getProperty("kronos:order").getLong();
			Integer intOrder = order.intValue();
			Long position = n.getProperty("kronos:position").getLong();
			Integer intPosition = position.intValue();
			String pluginType = n.getProperty("kronos:pluginType")
					.getString();
			properties = new PluginProperties(pluginUUID, name,
					published, intOrder, intPosition, pluginType);
		}
		catch (ItemNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (RepositoryException e)
		{
			e.printStackTrace();
		}
		
		return properties;
	}
	
	public static void savePlugin(String canonicalPluginname) 
	{
		Session jcrSession = KronosSession.get().getJCRSession();
		try {
			Node root = jcrSession.getRootNode();
			Node plugin = root.getNode("kronos:cms")
				.getNode("kronos:plugins")
					.getNode("kronos:plugin");
			plugin.setProperty("kronos:canonicalpluginname", canonicalPluginname);
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
	
	public static void removeContent(String contentUUID)
	{	
		Session jcrSession = KronosSession.get().getJCRSession();
		try {
			Node pluginNode = jcrSession.getNodeByUUID(contentUUID);
			pluginNode.remove();
			jcrSession.save();
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
	
	public static void removePluginInstance(String contentUUID)
	{	
		Session jcrSession = KronosSession.get().getJCRSession();
		try {
			Node pluginNode = jcrSession.getNodeByUUID(contentUUID);
			pluginNode.remove();
			jcrSession.save();
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
	
	/**
	 * 
	 * @param properties
	 */
	public static void savePluginProperties(PluginProperties properties, String oldPluginName)
	{
		Session jcrSession = KronosSession.get().getJCRSession();
		
		Node n;
		try
		{
			if(properties.getPluginUUID() == null || properties.getPluginUUID().equals("")) {
				String nodeType = getNodeType(properties.getPluginType());
				n = jcrSession.getRootNode()
					.getNode("kronos:cms")
						.getNode("kronos:plugininstantiations")
							.addNode("kronos:plugininstance", nodeType);
			} else {
				n = jcrSession.getNodeByUUID(properties.getPluginUUID());
			}
			
			
			
			n.setProperty("kronos:name", properties.getName());
			n.setProperty("kronos:published", properties.getPublished());
			n.setProperty("kronos:order", properties.getOrder());
			n.setProperty("kronos:position", properties.getPosition());
			n.setProperty("kronos:pluginType", properties.getPluginType());
			
			if(!oldPluginName.equals(properties.getName()))
			{
				Workspace ws = jcrSession.getWorkspace();
				QueryManager qm = ws.getQueryManager();
				Query q = qm.createQuery("//*[@kronos:pluginname = '" + oldPluginName + "']" , Query.XPATH);
		
				QueryResult result = q.execute();
				NodeIterator it = result.getNodes();
		
				while (it.hasNext())
				{
					n = it.nextNode();
					n.setProperty("kronos:pluginname", properties.getName());			
				}
			}
			jcrSession.save();
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
	
	public static String getNodeType(String canonicalPluginName)
	{
		String nodeType = "";
		
		Session jcrSession = KronosSession.get().getJCRSession();

		try
		{
			Workspace ws = jcrSession.getWorkspace();
			QueryManager qm = ws.getQueryManager();
			Query q = qm.createQuery("//kronos:plugins/kronos:plugin[@kronos:canonicalpluginname = '" + canonicalPluginName + "']", Query.XPATH);

			QueryResult result = q.execute();
			NodeIterator it = result.getNodes();

			if (it.hasNext())
			{
				Node n = it.nextNode();
				nodeType = n.getProperty("kronos:nodetype").getString();
			}
		}
		catch (RepositoryException e)
		{
			e.printStackTrace();
		}
		
		return nodeType;
	}
	
	
	public static List getPluginTypes() 
	{
		List<String> pluginTypes = new ArrayList<String>();
		
		Session jcrSession = KronosSession.get().getJCRSession();
		
		try
		{
			Workspace ws = jcrSession.getWorkspace();
			QueryManager qm = ws.getQueryManager();
			Query q = qm.createQuery("//kronos:plugins/kronos:plugin order by @kronos:canonicalpluginname ascending", Query.XPATH);

			QueryResult result = q.execute();
			NodeIterator it = result.getNodes();

			while (it.hasNext())
			{
				Node n = it.nextNode();
				String pluginType = n.getProperty("kronos:canonicalpluginname").getString();
				
				pluginTypes.add(pluginType);
			}
		}catch(RepositoryException e)
		{
			e.printStackTrace();			
		}
		
		return pluginTypes;
	}
	
	/**
	 * Generates all the plugins for a certain area that have been published.
	 * All the data is retrieved from the repository
	 * 
	 * @param area
	 * @return List<IPlugin>
	 */
	@SuppressWarnings("boxing")
	public static List<IPlugin> getPlugins(int area)
	{
		assert (area >= 0);
		List<IPlugin> plugins = new LinkedList<IPlugin>();
		boolean isAdmin = false;

		Session jcrSession = KronosSession.get().getJCRSession();

		try
		{
			Workspace ws = jcrSession.getWorkspace();
			QueryManager qm = ws.getQueryManager();
			Query q = qm.createQuery("//kronos:plugininstance[@kronos:position='"
					+ area + "' and @kronos:published='true'] order by @kronos:order ascending", Query.XPATH);

			QueryResult result = q.execute();
			NodeIterator it = result.getNodes();

			while (it.hasNext())
			{
				Node n = it.nextNode();
				String pluginUUID = null;
				/* Loading the IPlugin extended class */
				pluginUUID = n.getUUID();

				String name = n.getProperty("kronos:name").getString();
				Boolean published = n.getProperty("kronos:published")
						.getBoolean();
				Long order = n.getProperty("kronos:order").getLong();
				Integer intOrder = order.intValue();
				Long position = n.getProperty("kronos:position").getLong();
				Integer intPosition = position.intValue();
				String pluginType = n.getProperty("kronos:pluginType")
						.getString();

				plugins.add(getPluginObject(isAdmin, pluginUUID, name,
						published, intOrder, intPosition, pluginType));
			}
		}
		catch (RepositoryException e)
		{
			e.printStackTrace();
		}
		return plugins;
	}

	/**
	 * Reading only one specific plugin from repository
	 * 
	 * @param isAdmin
	 * @param pluginUUID
	 * @param pluginId
	 * @return The Plugin from the JackRabbit repository
	 */
	@SuppressWarnings("boxing")
	public static IPlugin getPlugin(boolean isAdmin, String pluginUUID)
	{
		assert (pluginUUID != null);
		IPlugin plugin = null;

		Session jcrSession = KronosSession.get().getJCRSession();
		try
		{

			Node pluginNode = jcrSession.getNodeByUUID(pluginUUID);

			String name = pluginNode.getProperty("kronos:name").getString();
			Boolean published = pluginNode.getProperty("kronos:published")
					.getBoolean();
			Long order = pluginNode.getProperty("kronos:order").getLong();
			Integer intOrder = order.intValue();
			Long position = pluginNode.getProperty("kronos:position").getLong();
			Integer intPosition = position.intValue();
			String pluginType = pluginNode.getProperty("kronos:pluginType")
					.getString();

			plugin = getPluginObject(isAdmin, pluginUUID, name, published, intOrder,
					intPosition, pluginType);
		}
		catch (ItemNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (RepositoryException e)
		{
			e.printStackTrace();
		}

		return plugin;
	}

	/**
	 * Generates a plugin. This plugin only has one piece of content
	 * 
	 * @param isAdmin
	 * @param contentUUID
	 * @return IPlugin
	 */
	public static IPlugin getPluginByContent(boolean isAdmin, String contentUUID)
	{
		assert (contentUUID != null);

		IPlugin plugin = null;

		Session jcrSession = KronosSession.get().getJCRSession();

		try
		{
			Node contentNode = jcrSession.getNodeByUUID(contentUUID);

			String pluginName = contentNode.getProperty("kronos:pluginname")
					.getString();

			Workspace ws = jcrSession.getWorkspace();
			QueryManager qm = ws.getQueryManager();
			Query q = qm.createQuery(
					"//kronos:plugininstantiations/kronos:plugininstance[@kronos:name = '"
							+ pluginName + "']", Query.XPATH);

			QueryResult result = q.execute();
			NodeIterator it = result.getNodes();

			if (it.hasNext())
			{
				Node n = it.nextNode();

				String pluginUUID = n.getUUID();

				String name = n.getProperty("kronos:name").getString();
				Boolean published = n.getProperty("kronos:published")
						.getBoolean();
				Long order = n.getProperty("kronos:order").getLong();
				Integer intOrder = order.intValue();
				Long position = n.getProperty("kronos:position").getLong();
				Integer intPosition = position.intValue();
				String pluginType = n.getProperty("kronos:pluginType")
						.getString();

				plugin = getPluginObject(isAdmin, pluginUUID, name, published,
						intOrder, intPosition, pluginType, contentUUID);
			}

		}
		catch (ItemNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (RepositoryException e)
		{
			e.printStackTrace();
		}

		return plugin;

	}

	/**
	 * Retrieves all the users from the repository
	 * 
	 * @return List<User>
	 */
	public static List<User> getUsers()
	{
		List<User> users = new LinkedList<User>();
		User newUser = null;
		String[] roles = new String[] {};

		Session jcrSession = KronosSession.get().getJCRSession();
		try
		{
			Workspace ws = jcrSession.getWorkspace();
			QueryManager qm = ws.getQueryManager();
			Query q = qm.createQuery("//kronos:user", Query.XPATH);

			QueryResult result = q.execute();
			NodeIterator it = result.getNodes();

			while (it.hasNext())
			{
				Node n = it.nextNode();

				String username = n.getProperty("kronos:username").getString();
				String name = n.getProperty("kronos:fullname").getString();
				String email = n.getProperty("kronos:email").getString();
				Calendar lastvisit = n.getProperty("kronos:lastvisit")
						.getDate();
				Value[] rolevalues = n.getProperty("kronos:roles").getValues();
				String password = n.getProperty("kronos:password").getString();
				for (int i = 0; i <= rolevalues.length; i++)
				{
					Value value = rolevalues[i];
					Node roleNode = jcrSession.getNodeByUUID(value.getString());
					String role = roleNode.getProperty("kronos:name")
							.getString();
					roles[i] = role;
				}

				Roles userroles = new Roles(roles);
				newUser = new User(username, name, password, email, lastvisit,
						userroles);

				users.add(newUser);
			}
		}
		catch (RepositoryException e)
		{
			e.printStackTrace();
		}
		return users;
	}

	/**
	 * Retrieves a specific user from the repository
	 * 
	 * @param username
	 * @return User
	 */
	public static User getUser(String username)
	{
		assert (username != null);
		User user = null;
		String[] roles = new String[100];

		Session jcrSession = KronosSession.get().getJCRSession();

		try
		{
			Workspace ws = jcrSession.getWorkspace();
			QueryManager qm = ws.getQueryManager();
			Query q = qm.createQuery("//kronos:user[@kronos:username = '"
					+ username + "']", Query.XPATH);

			QueryResult result = q.execute();
			NodeIterator it = result.getNodes();

			if (it.hasNext())
			{
				Node n = it.nextNode();

				String storedUsername = n.getProperty("kronos:username")
						.getString();
				String name = n.getProperty("kronos:fullname").getString();
				String email = n.getProperty("kronos:email").getString();
				Calendar lastvisit = n.getProperty("kronos:lastvisit")
						.getDate();
				Node roleNode = n.getProperty("kronos:roles").getNode();
				String password = n.getProperty("kronos:password").getString();
				/*
				 * for (int i=0; i<=rolevalues.length; i++) { Value value =
				 * rolevalues[i]; Node roleNode =
				 * jcrSession.getNodeByUUID(value.getString()); String role =
				 * roleNode.getProperty("kronos:name").getString(); roles[i] =
				 * role; }
				 */
				String role = roleNode.getProperty("kronos:name").getString();
				roles[0] = role;
				Roles userroles = new Roles(roles);
				user = new User(storedUsername, name, email, password,
						lastvisit, userroles);
			}
		}
		catch (RepositoryException e)
		{
			e.printStackTrace();
		}
		return user;
	}

	/**
	 * Retrieves all the roles from the repository
	 * 
	 * @return Roles
	 */
	public static Roles getRoles()
	{
		Roles roles = new Roles();

		Session jcrSession = ((KronosSession) KronosSession.get())
				.getJCRSession();

		try
		{
			Workspace ws = jcrSession.getWorkspace();
			QueryManager qm = ws.getQueryManager();
			Query q = qm.createQuery("//kronos:role", Query.XPATH);

			QueryResult result = q.execute();
			NodeIterator it = result.getNodes();

			while (it.hasNext())
			{
				Node n = it.nextNode();

				String name = n.getProperty("kronos:name").getString();
				roles.add(name);
			}
		}
		catch (RepositoryException e)
		{
			e.printStackTrace();
		}
		return roles;
	}

	/**
	 * Creates a plugin and returns it
	 * 
	 * @param isAdmin
	 * @param pluginUUID
	 * @param name
	 * @param published
	 * @param order
	 * @param position
	 * @param pluginType
	 * @return IPlugin
	 */
	private static IPlugin getPluginObject(Boolean isAdmin, String pluginUUID,
			String name, Boolean published, Integer order, Integer position,
			String pluginType)
	{
		Class[] classParm = {Boolean.class, String.class, String.class,
				Boolean.class, Integer.class, Integer.class, String.class};

		Object[] objectParm = {isAdmin, pluginUUID, name, published, order,
				position, pluginType};
		IPlugin plugin = null;
		try
		{

			Class cl = Class.forName(pluginType);
			java.lang.reflect.Constructor co = cl.getConstructor(classParm);
			plugin = (IPlugin) co.newInstance(objectParm);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return plugin;
	}

	/**
	 * Creates a plugin and returns it
	 * 
	 * @param isAdmin
	 * @param pluginUUID
	 * @param name
	 * @param published
	 * @param order
	 * @param position
	 * @param pluginType
	 * @param contentUUID
	 * @return IPlugin
	 */
	private static IPlugin getPluginObject(Boolean isAdmin, String pluginUUID,
			String name, Boolean published, Integer order, Integer position,
			String pluginType, String contentUUID)
	{
		Class[] classParm = {Boolean.class, String.class, String.class,
				Boolean.class, Integer.class, Integer.class, String.class,
				String.class};

		Object[] objectParm = {isAdmin, pluginUUID, name, published, order,
				position, pluginType, contentUUID};
		IPlugin plugin = null;
		try
		{

			Class cl = Class.forName(pluginType);
			java.lang.reflect.Constructor co = cl.getConstructor(classParm);
			plugin = (IPlugin) co.newInstance(objectParm);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return plugin;
	}

	/**
	 * Temporary function. I used to fill the repository with some trivial data.
	 * 
	 * @throws FileNotFoundException
	 * @throws ParseException
	 * @throws RepositoryException
	 * @throws InvalidNodeTypeDefException
	 */
	public static void repositoryStartup() throws FileNotFoundException,
			ParseException, RepositoryException, InvalidNodeTypeDefException
	{
		Session session = KronosApplication.get().getRepository().login(
				new SimpleCredentials("admin", "".toCharArray()));

		session.getWorkspace().getNamespaceRegistry().registerNamespace(
				"kronos", "http://kronos.com");

		String cndFileName = "nodetype.cnd";

		FileReader fileReader = new FileReader(cndFileName);
		System.out.println("Opening file");

		// Create a CompactNodeTypeDefReader
		CompactNodeTypeDefReader cndReader = new CompactNodeTypeDefReader(
				fileReader, cndFileName);
		System.out.println("Reading file");

		// Get the List of NodeTypeDef objects
		List ntdList = cndReader.getNodeTypeDefs();
		System.out.println("Retrieving nodetypes");

		// Get the NodeTypeManager from the Workspace.
		// Note that it must be cast from the generic JCR NodeTypeManager to the
		// Jackrabbit-specific implementation.
		NodeTypeManagerImpl ntmgr;

		ntmgr = (NodeTypeManagerImpl) session.getWorkspace()
				.getNodeTypeManager();

		System.out.println("Creating manager");

		// Acquire the NodeTypeRegistry
		NodeTypeRegistry ntreg = ntmgr.getNodeTypeRegistry();
		System.out.println("Retrieving registration");

		// Loop through the prepared NodeTypeDefs
		for (Iterator i = ntdList.iterator(); i.hasNext();)
		{

			// Get the NodeTypeDef...
			NodeTypeDef ntd = (NodeTypeDef) i.next();

			// ...and register it
			ntreg.registerNodeType(ntd);
			System.out.println("Registering nodetypes");
		}

		Node root = session.getRootNode();
		Node cms = root.addNode("kronos:cms", "kronos:Cms");

		Node roles = cms.addNode("kronos:roles");
		Node role = roles.addNode("kronos:role");
		role.setProperty("kronos:name", "administrator");
		role.setProperty("kronos:description", "The administrator of the CMS");

		Node users = cms.addNode("kronos:users");
		Node user = users.addNode("kronos:user");
		user.setProperty("kronos:username", "wicket");
		user.setProperty("kronos:fullname", "Teddy");
		user.setProperty("kronos:email", "kronos@kronos.com");

		String hashedPassword = null;
		try
		{
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update("wicket".getBytes());
			BigInteger hash = new BigInteger(1, md5.digest());
			hashedPassword = hash.toString(16);
		}
		catch (NoSuchAlgorithmException nsae)
		{
			// ignore
		}

		user.setProperty("kronos:password", hashedPassword);
		user.setProperty("kronos:lastvisit", new GregorianCalendar());
		user.setProperty("kronos:roles", role);

		user = users.addNode("kronos:user");
		user.setProperty("kronos:username", "rick");
		user.setProperty("kronos:fullname", "RICKYYYYYYY");
		user.setProperty("kronos:email", "kronos@kronos.com");

		try
		{
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update("rick".getBytes());
			BigInteger hash = new BigInteger(1, md5.digest());
			hashedPassword = hash.toString(16);
		}
		catch (NoSuchAlgorithmException nsae)
		{
			// ignore
		}

		user.setProperty("kronos:password", hashedPassword);
		user.setProperty("kronos:lastvisit", new GregorianCalendar());
		user.setProperty("kronos:roles", role);

		Node menus = cms.addNode("kronos:menus");
		Node menu = menus.addNode("kronos:menu");
		menu.setProperty("kronos:menuname", "Menuleft");

		Node menuItem = menu.addNode("kronos:menuitem");
		menuItem.setProperty("kronos:menuitemname", "Home");
		menuItem.setProperty("kronos:linkType", "intern");
		menuItem.setProperty("kronos:isAdmin", false);
		menuItem.setProperty("kronos:IDType", "frontpage");
		
		menuItem = menu.addNode("kronos:menuitem");
		menuItem.setProperty("kronos:menuitemname", "Admin");
		menuItem.setProperty("kronos:linkType", "intern");
		menuItem.setProperty("kronos:isAdmin", false);
		menuItem.setProperty("kronos:IDType", "adminpage");

		menuItem = menu.addNode("kronos:menuitem");
		menuItem.setProperty("kronos:menuitemname", "Tweakers");
		menuItem.setProperty("kronos:linkType", "extern");
		menuItem.setProperty("kronos:link", "http://www.tweakers.net");

		Node plugins = cms.addNode("kronos:plugininstantiations");

		Node bannerPlugin = plugins.addNode("kronos:plugininstance", "kronos:BannerPlugin");
		bannerPlugin.setProperty("kronos:name", "banner");
		bannerPlugin.setProperty("kronos:position", 0);
		bannerPlugin.setProperty("kronos:published", true);
		bannerPlugin.setProperty("kronos:pluginType",
				"wicket.kronos.plugins.banner.BannerPlugin");

		Node plugin = plugins.addNode("kronos:plugininstance");
		plugin.setProperty("kronos:name", "titan");
		plugin.setProperty("kronos:position", 3);
		plugin.setProperty("kronos:published", true);
		plugin.setProperty("kronos:pluginType",
				"wicket.kronos.plugins.titan.TitanPlugin");

		plugin = plugins.addNode("kronos:plugininstance", "kronos:MenuPlugin");
		plugin.setProperty("kronos:name", "Menuleft");
		plugin.setProperty("kronos:position", 1);
		plugin.setProperty("kronos:published", true);
		plugin.setProperty("kronos:pluginType",
				"wicket.kronos.plugins.menu.MenuPlugin");
		plugin.setProperty("kronos:isHorizontal", false);

		plugin = plugins.addNode("kronos:plugininstance");
		plugin.setProperty("kronos:name", "login");
		plugin.setProperty("kronos:position", 1);
		plugin.setProperty("kronos:published", true);
		plugin.setProperty("kronos:order", 2);
		plugin.setProperty("kronos:pluginType",
				"wicket.kronos.plugins.login.LoginPlugin");

		plugin = plugins.addNode("kronos:plugininstance");
		plugin.setProperty("kronos:name", "hellocenter1");
		plugin.setProperty("kronos:position", 2);
		plugin.setProperty("kronos:published", true);
		plugin.setProperty("kronos:pluginType",
				"wicket.kronos.plugins.helloworld.HelloWorld");

		Node menuItem2 = menu.addNode("kronos:menuitem");
		menuItem2.setProperty("kronos:menuitemname", "HelloCenter 1");
		menuItem2.setProperty("kronos:linkType", "intern");
		menuItem2.setProperty("kronos:isAdmin", false);
		menuItem2.setProperty("kronos:IDType", "plugin");
		menuItem2.setProperty("kronos:ID", plugin.getUUID());

		Node blogPlugin = plugins.addNode("kronos:plugininstance");
		blogPlugin.setProperty("kronos:name", "blog");
		blogPlugin.setProperty("kronos:position", 2);
		blogPlugin.setProperty("kronos:order", 2);
		blogPlugin.setProperty("kronos:published", true);
		blogPlugin.setProperty("kronos:pluginType",
				"wicket.kronos.plugins.blog.BlogPlugin");

		Node menuItem3 = menu.addNode("kronos:menuitem");
		menuItem3.setProperty("kronos:menuitemname", "Blog");
		menuItem3.setProperty("kronos:linkType", "intern");
		menuItem3.setProperty("kronos:isAdmin", false);
		menuItem3.setProperty("kronos:IDType", "plugin");
		menuItem3.setProperty("kronos:ID", blogPlugin.getUUID());

		plugin = plugins.addNode("kronos:plugininstance");
		plugin.setProperty("kronos:name", "version");
		plugin.setProperty("kronos:position", 4);
		plugin.setProperty("kronos:published", true);
		plugin.setProperty("kronos:pluginType",
				"wicket.kronos.plugins.version.VersionPlugin");

		Node content = cms.addNode("kronos:content");
		Node images = content.addNode("kronos:images");
		
		/* Inserting First image */
		File file = new File("./header-1024x125.png");
		MimeTable mt = MimeTable.getDefaultTable();
		String mimeType = mt.getContentTypeFor(file.getName());
		if (mimeType == null) mimeType = "application/octet-stream";

		Node fileNode = images.addNode(file.getName(), "nt:file");
		fileNode.addMixin("mix:referenceable");
		Node resNode = fileNode.addNode("jcr:content", "nt:resource");
		resNode.setProperty("jcr:mimeType", mimeType);
		resNode.setProperty("jcr:encoding", "");
		resNode.setProperty("jcr:data", new FileInputStream(file));
		Calendar lastModified = new GregorianCalendar();
		lastModified.setTimeInMillis(file.lastModified());
		resNode.setProperty("jcr:lastModified", lastModified);

		/* Inserting banner content */
		Node pluginContent = content.addNode("kronos:plugin");
		
		bannerPlugin.setProperty("kronos:bannerimage", fileNode);
		
		/* Inserting second image */
		file = new File("./osmbanner2.png");
		mt = MimeTable.getDefaultTable();
		mimeType = mt.getContentTypeFor(file.getName());
		if (mimeType == null) mimeType = "application/octet-stream";

		fileNode = images.addNode(file.getName(), "nt:file");
		fileNode.addMixin("mix:referenceable");
		resNode = fileNode.addNode("jcr:content", "nt:resource");
		resNode.setProperty("jcr:mimeType", mimeType);
		resNode.setProperty("jcr:encoding", "");
		resNode.setProperty("jcr:data", new FileInputStream(file));
		lastModified.setTimeInMillis(file.lastModified());
		resNode.setProperty("jcr:lastModified", lastModified);
		
		/* Inserting third image */ 
		file = new File("./header_short.jpg");
		mt = MimeTable.getDefaultTable();
		mimeType = mt.getContentTypeFor(file.getName());
		if (mimeType == null) mimeType = "application/octet-stream";

		fileNode = images.addNode(file.getName(), "nt:file");
		fileNode.addMixin("mix:referenceable");
		resNode = fileNode.addNode("jcr:content", "nt:resource");
		resNode.setProperty("jcr:mimeType", mimeType);
		resNode.setProperty("jcr:encoding", "");
		resNode.setProperty("jcr:data", new FileInputStream(file));
		lastModified.setTimeInMillis(file.lastModified());
		resNode.setProperty("jcr:lastModified", lastModified);

		Node pluginContent1 = content.addNode("kronos:plugin");
		Node itemNode = pluginContent1.addNode("kronos:blogpostings",
				"kronos:BlogPostings");
		Node blogNode = itemNode.addNode("kronos:blogpost");
		Calendar date = new GregorianCalendar();
		blogNode.setProperty("kronos:pluginname", "blog");
		blogNode.setProperty("kronos:date", date);
		blogNode.setProperty("kronos:title", "The standard Lorem Ipsum passage");
		blogNode.setProperty("kronos:text",
			"Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
		blogNode.setProperty("kronos:author", "minime");
		Node comments = blogNode.addNode("kronos:comments");
		Node comment = comments.addNode("kronos:comment");
		comment.setProperty("kronos:text", "This is the first comment");
		comment.setProperty("kronos:author", "Dr. Evil");
		comment.setProperty("kronos:date", date);

		blogNode = itemNode.addNode("kronos:blogpost");

		blogNode.setProperty("kronos:pluginname", "blog");
		blogNode.setProperty("kronos:date", date);
		blogNode.setProperty("kronos:title", "de Finibus Bonorum et Malorum");
		blogNode.setProperty("kronos:text",
				"Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?");
		blogNode.setProperty("kronos:author", "minime");
		comments = blogNode.addNode("kronos:comments");
		comment = comments.addNode("kronos:comment");
		comment.setProperty("kronos:text", "This is the first comment");
		comment.setProperty("kronos:author", "Dr. Evil");
		comment.setProperty("kronos:date", date);

		comment = comments.addNode("kronos:comment");
		comment.setProperty("kronos:text", "This is the second comment");
		comment.setProperty("kronos:author", "Austin 'Danger' Powers");
		comment.setProperty("kronos:date", date);

		blogNode = itemNode.addNode("kronos:blogpost");

		blogNode.setProperty("kronos:pluginname", "blog");
		blogNode.setProperty("kronos:date", date);
		blogNode.setProperty("kronos:title", "Introduction of V");
		blogNode
				.setProperty(
						"kronos:text",
						"Voilà! In view, a humble vaudevillian veteran, cast vicariously as both victim and villain by the vicissitudes of fate. This visage, no mere veneer of vanity, is a vestige of the vox populi, now vacant, vanished. However, this valorous visitation of a bygone vexation stands vivified, and has vowed to vanquish these venal and virulent vermin vanguarding vice and vouchsafing the violently vicious and voracious violation of volition. The only verdict is vengeance; a vendetta held as a votive, not in vain, for the value and veracity of such shall one day vindicate the vigilant and the virtuous. Verily, this vichyssoise of verbiage veers most verbose, so let me simply add that it's my very good honor to meet you and you may call me V");
		blogNode.setProperty("kronos:author", "V");
		comments = blogNode.addNode("kronos:comments");
		comment = comments.addNode("kronos:comment");
		comment.setProperty("kronos:text", "Are you like a crazy person?");
		comment.setProperty("kronos:author", "Evey");
		comment.setProperty("kronos:date", date);

		blogNode = itemNode.addNode("kronos:blogpost");

		blogNode.setProperty("kronos:pluginname", "blog");
		blogNode.setProperty("kronos:date", date);
		blogNode.setProperty("kronos:title", "Why are you here?");
		blogNode
				.setProperty(
						"kronos:text",
						"Your life is the sum of a remainder of an unbalanced equation inherent to the programming of the matrix. You are the eventuality of an anomaly, which despite my sincerest efforts I have been unable to eliminate from what is otherwise a harmony of mathematical precision. While it remains a burden assiduously avoided, it is not unexpected, and thus not beyond a measure of control. Which has led you, inexorably, here.");
		blogNode.setProperty("kronos:author", "The architect");
		comments = blogNode.addNode("kronos:comments");
		comment = comments.addNode("kronos:comment");
		comment.setProperty("kronos:text", "You haven't answered my question");
		comment.setProperty("kronos:author", "Neo");
		comment.setProperty("kronos:date", date);

		comment = comments.addNode("kronos:comment");
		comment.setProperty("kronos:text",
				"Quite right. Interesting. That was quicker than the others");
		comment.setProperty("kronos:author", "The architect");
		comment.setProperty("kronos:date", date);

		blogNode = itemNode.addNode("kronos:blogpost");

		blogNode.setProperty("kronos:pluginname", "blog");
		blogNode.setProperty("kronos:date", date);
		blogNode.setProperty("kronos:title", "Big lorem Ipsum");
		blogNode
				.setProperty(
						"kronos:text",
						"Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Praesent faucibus aliquam turpis. Nam accumsan commodo lorem. Donec sed erat. Sed vel ante. Sed metus nulla, tincidunt a, cursus tempor, pharetra placerat, diam. In dignissim felis non elit. Fusce consectetuer leo sed sem. Nunc vel est sed mi congue convallis. Ut vehicula libero ut tellus. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos hymenaeos. Integer rutrum odio ac libero. Ut et massa. Duis eu nisl. Maecenas orci erat, sagittis ut, commodo sed, aliquam id, nibh. Sed tristique dui in velit. Vivamus eu ligula. Nunc quis dui vitae neque tempus posuere. Nunc metus neque, ultrices ut, ultricies et, fermentum sed, libero. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Sed aliquam, nunc eget dignissim vehicula, nunc ligula vehicula erat, ac auctor ipsum arcu et felis. Aliquam vel nulla. Maecenas pretium. Mauris augue ante, tincidunt ut, pellentesque vel, cursus et, erat. Aliquam erat volutpat. Praesent interdum massa in ligula. Sed non quam vel velit viverra pulvinar. Donec nunc.\n Pellentesque cursus faucibus felis. Sed dapibus risus et metus. \nSuspendisse in nisi quis urna ornare pellentesque. Etiam sagittis nisi id mauris. Aliquam convallis. Morbi nonummy justo pretium massa. Donec lobortis. Integer pharetra sapien quis magna. Praesent tempor vehicula ligula. Curabitur pharetra. Praesent luctus, enim sit amet sodales pulvinar, mi eros sagittis orci, vitae mollis leo lorem vitae erat. Donec porttitor, tellus in porta pretium, augue leo interdum velit, ac blandit enim justo vitae odio. Mauris vel lorem. \n Phasellus sodales, elit vitae hendrerit commodo, lorem turpis fringilla sapien, ac iaculis eros nisi vel urna. Nam eu metus ut nisl bibendum posuere. Morbi magna. Pellentesque sit amet lacus. Suspendisse ut arcu. Nullam euismod, tellus accumsan pharetra malesuada, urna nunc lobortis lacus, quis dictum metus massa nec nulla. Praesent mi dolor, blandit nec, pulvinar nec, porta sit amet, ligula. Nunc rhoncus sollicitudin est. Sed a est sit amet dolor auctor vulputate. Maecenas consequat leo ac lacus. Nullam id est. Phasellus vitae purus. Quisque non diam. Mauris quam. Praesent auctor consequat leo. Nullam sagittis quam dapibus sem. Pellentesque quis pede. Nunc sit amet metus. \nCras tempor, tellus varius interdum eleifend, magna risus euismod est, ut lobortis eros magna et turpis. Sed eu turpis in turpis consequat elementum. Pellentesque justo neque, condimentum vel, dapibus vitae, mattis nec, felis. Morbi mi. Vivamus malesuada tincidunt augue. Integer dignissim tellus a velit. Ut elementum sem in metus. In hac habitasse platea dictumst. Suspendisse sit amet ante ac nibh feugiat dignissim. Nullam rhoncus ligula. Duis interdum metus a nisi. Morbi risus. Praesent sollicitudin, mi et posuere nonummy, metus lacus varius tellus, et accumsan neque mauris sed dolor. Suspendisse sed ipsum in pede ultricies luctus. Integer nec enim. Vivamus condimentum erat a purus. \n Aenean blandit, purus id hendrerit lobortis, tortor nulla laoreet mauris, sit amet mattis enim risus luctus urna. Fusce venenatis, erat et facilisis fringilla, diam mi tristique metus, ac tristique arcu orci eget neque. Curabitur vulputate volutpat tellus. Mauris nisl. Nam at libero. Pellentesque accumsan arcu vitae purus. Sed vestibulum tortor vel ante. In nunc. Donec et justo. Nam nec nisl et sem nonummy imperdiet. Nulla facilisi. Duis varius facilisis libero. Mauris pulvinar. Nulla eu ante. Suspendisse imperdiet, lorem et egestas bibendum, risus sem vehicula arcu, tempor fermentum leo augue vitae purus. Etiam adipiscing turpis sit amet tellus. Nulla mi ipsum, ultricies ut, euismod et, molestie eu, nisl. Nam ultrices vehicula tellus. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Cras tellus. \nMaecenas nonummy bibendum lectus. Mauris pellentesque rhoncus ligula. Nulla facilisi. Maecenas mattis rhoncus lectus. Nulla vitae erat in neque egestas luctus. Quisque fringilla mauris mattis lectus. Cras rutrum, dui at placerat cursus, risus ante hendrerit sem, at ultrices pede turpis ut ipsum. Nunc et ligula. Suspendisse potenti. Phasellus id neque nec nisi ultrices condimentum. In hac habitasse platea dictumst. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos hymenaeos. Etiam erat dui, sodales in, vehicula tincidunt, tempus sit amet, pede. \n Proin semper. Vivamus venenatis lectus in ipsum. Curabitur eu elit. Aliquam et sapien. Aliquam erat volutpat. Vivamus hendrerit nonummy dolor. Donec vulputate lacinia lacus. Nunc nunc neque, tincidunt eget, posuere commodo, consectetuer non, tellus. Aliquam non felis non augue sollicitudin auctor. Nunc enim nisi, rhoncus auctor, ullamcorper vitae, semper non, erat. Pellentesque tempor leo eget nisl. Duis egestas sem id tellus. \n Nulla malesuada, elit nec ultrices sollicitudin, sapien nulla laoreet nibh, sed ullamcorper sem urna sit amet lectus. Suspendisse vulputate nisi ut eros. Phasellus neque. Curabitur dapibus sodales leo. Sed ac dolor sit amet ligula convallis luctus. Quisque ac purus at enim mattis rutrum. Vivamus sit amet ante. Morbi et lorem. Fusce porta, nisl non varius consequat, felis sem euismod tortor, ullamcorper pellentesque nulla sem sed ligula. Donec vel velit. Fusce odio purus, faucibus sit amet, porta eu, tincidunt sed, quam. Curabitur lectus massa, consectetuer in, ultrices nec, viverra non, ligula. \n Etiam euismod enim sed ligula. Vivamus iaculis scelerisque sem. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos hymenaeos. Duis rhoncus porttitor diam. Pellentesque eros. Suspendisse libero. Quisque luctus, ante vel fermentum accumsan, magna enim condimentum nisl, vel vehicula augue orci a augue. Nam placerat ante facilisis libero. Pellentesque eget urna non nisl dapibus rhoncus. Sed quam felis, faucibus non, ultrices sed, imperdiet id, sapien. \n Sed ultrices mollis sapien. Donec volutpat faucibus lacus. Ut eget elit sed magna pharetra nonummy. Etiam vel sapien ac nisi sollicitudin tempor. Nullam et est eget dolor suscipit pretium. In ipsum. Proin tincidunt purus non augue. Pellentesque lacinia lobortis massa. Aliquam nibh. Integer nonummy arcu id massa. Duis suscipit. Sed tempor dui ac risus. Nullam volutpat sem sed tellus. Phasellus varius. Morbi laoreet. Pellentesque ac lacus. Morbi lacus elit, tincidunt nec, facilisis id, egestas at, sapien. Pellentesque sit amet mauris et ante imperdiet cursus. Fusce ut lectus. Aliquam id augue. \n Mauris lectus est, rutrum sit amet, faucibus et, viverra a, est. Proin vehicula. Phasellus eu leo. In orci enim, blandit eu, hendrerit et, convallis sit amet, nunc. Curabitur in libero. Suspendisse aliquet. Morbi faucibus velit id magna. Vestibulum lectus. Pellentesque vitae turpis. Suspendisse potenti. Maecenas non magna. Donec elit dui, dignissim nec, ultricies quis, aliquet quis, ipsum. In in tortor eget nunc gravida vehicula. Duis dapibus. Suspendisse potenti. \n Morbi egestas. Aliquam at tellus. Maecenas sodales fermentum quam. Sed tempus. Aenean sodales, pede sit amet fermentum placerat, nunc justo consequat eros, eget ultricies tellus mauris id risus. Nullam nonummy lacus nec turpis. Donec lorem leo, malesuada non, ultrices at, vulputate at, erat. Pellentesque eu justo. Donec tristique ullamcorper dolor. Mauris ipsum purus, egestas eleifend, lobortis vel, hendrerit eu, enim. Maecenas condimentum leo ut arcu. Donec ornare, purus non ultricies gravida, dui lectus viverra orci, a ullamcorper felis ante non nisi. Quisque feugiat. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. \n Sed consectetuer mauris blandit justo. Aliquam pharetra semper velit. Etiam at nunc. Maecenas porta ullamcorper nisi. Aenean bibendum fermentum ipsum. Mauris adipiscing erat. Suspendisse felis. Pellentesque a erat. Donec vulputate accumsan quam. Aliquam tincidunt. Duis convallis imperdiet nulla. Phasellus fringilla egestas sem. Sed quis arcu quis odio laoreet congue. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Praesent scelerisque leo ut sapien. Phasellus facilisis, nulla quis aliquet lobortis, dolor nulla fermentum eros, non sodales orci eros id libero. \n Morbi at sapien at urna cursus pretium. Nunc dapibus, nisi ut sollicitudin elementum, felis neque malesuada elit, sit amet pellentesque orci velit vitae risus. Ut placerat dolor a nibh. In imperdiet neque at nulla. Pellentesque porta vehicula orci. Vivamus consequat, lacus ut eleifend lacinia, dolor dui porttitor magna, viverra sollicitudin felis odio ac eros. In hac habitasse platea dictumst. Quisque metus lorem, consectetuer eget, sollicitudin vitae, sodales eget, ante. Nunc ornare augue vestibulum lectus. Sed nisi odio, tempus ac, luctus ac, congue eu, elit. Duis blandit. Pellentesque massa. \n Vivamus aliquet, tortor ut egestas egestas, purus ligula commodo quam, at sollicitudin libero elit ut est. Praesent lectus elit, blandit id, lacinia vitae, malesuada a, ligula. Quisque pretium, velit vel venenatis dignissim, augue mauris adipiscing elit, ultrices fermentum ligula purus id orci. Sed auctor fringilla sapien. Aliquam porta. In laoreet metus quis diam. Morbi eros tortor, suscipit nec, dapibus in, cursus at, felis. Maecenas est odio, ullamcorper sagittis, ultricies et, sodales id, dolor. In nunc nibh, fermentum a, tempus non, vulputate in, urna. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Aliquam erat volutpat. In hac habitasse platea dictumst. In mi dui, ultricies at, pretium eget, elementum id, tellus. In in nibh ac erat nonummy pharetra. Donec mattis nibh a metus. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. \n Maecenas dictum felis eget neque. Aliquam lacus lacus, lobortis ut, commodo vitae, vestibulum ut, augue. Aliquam euismod lectus ut dui. Nulla imperdiet tristique eros. Sed a nisl. Proin gravida pretium leo. Vivamus nunc. Maecenas pulvinar cursus magna. Sed semper. Fusce varius enim eu ipsum. Nam sed quam. Phasellus eget quam. Aenean leo. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. \n Nulla aliquet, neque vitae molestie tincidunt, sem justo pharetra risus, id luctus lacus ipsum a est. Maecenas semper. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos hymenaeos. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Proin ipsum. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec id metus. Fusce blandit leo feugiat metus. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Vestibulum eu turpis ac nunc aliquet interdum. Sed laoreet. Morbi sit amet neque. Cras ac orci. Duis ultrices, lectus in rutrum feugiat, tellus justo tincidunt risus, non dapibus turpis eros auctor magna. Curabitur orci dolor, tempus id, bibendum pretium, aliquet semper, ante.  \n In quis ipsum. Maecenas ullamcorper, augue ut pharetra dictum, enim ipsum accumsan quam, ac pulvinar neque odio non sapien. Cras sagittis risus in dolor. Mauris sollicitudin nibh nec mi sollicitudin tristique. Donec elementum ullamcorper ante. Proin risus dui, suscipit et, varius sed, egestas eu, mauris. Nunc eget eros non nibh euismod tincidunt. Mauris vulputate ligula a lectus. Fusce egestas eros sed elit. Integer sit amet velit et mauris ultricies iaculis. Ut in lacus ut nibh dictum rhoncus. \n Vestibulum adipiscing. Nam nisl pede, faucibus eget, viverra sit amet, nonummy at, diam. In nec ipsum. Nam libero nulla, euismod vel, fermentum ac, porttitor et, erat. Proin mattis, neque nec vestibulum rhoncus, pede libero fringilla eros, ac varius est odio at mi. Aliquam ut velit quis elit pulvinar ullamcorper. Duis lacus. Maecenas at elit non libero lacinia condimentum. Pellentesque quis est nec lectus varius interdum. Aenean euismod mauris a elit. Donec a orci in neque tincidunt molestie. Phasellus porta dapibus lorem. Suspendisse potenti. \n Maecenas nibh nibh, vulputate a, facilisis id, cursus dapibus, dolor. Integer hendrerit pretium velit. Donec suscipit erat ac turpis. Nunc sed velit eget lectus laoreet congue. Nullam adipiscing enim non nunc. Donec nulla. Vestibulum tempus odio non odio posuere feugiat. Nunc enim. Etiam ultrices rhoncus metus. Nullam a dui eu tellus bibendum placerat. Nam euismod est eu purus. Curabitur auctor, pede sit amet lacinia tristique, erat elit auctor ipsum, a eleifend lorem ante ac velit. Pellentesque adipiscing. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Maecenas turpis magna, hendrerit vel, feugiat at, blandit luctus, erat. Nullam tincidunt faucibus metus. Nunc ut dui. \n Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Aliquam molestie, massa ac dictum adipiscing, sapien purus eleifend pede, quis dapibus tortor odio in neque. Morbi ac neque. Integer varius purus at enim. Morbi posuere. Vivamus eleifend molestie ipsum. Vivamus a libero ac leo pulvinar ullamcorper. Ut ac justo. Nullam aliquam pretium quam. Duis sit amet orci. \n Curabitur sapien risus, viverra ac, malesuada in, pellentesque ac, odio. Phasellus euismod rutrum urna. Phasellus arcu. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Ut consequat. Sed venenatis. Vivamus dictum consectetuer diam. Curabitur pharetra nisi eu lorem. Sed ipsum nulla, faucibus at, blandit a, viverra eu, urna. Suspendisse eleifend orci sed massa. Nunc felis tellus, molestie vel, condimentum non, mattis at, est. Aenean nec est. Praesent eros tortor, vestibulum sit amet, accumsan sit amet, faucibus id, justo. Donec tellus massa, sodales nec, auctor non, lacinia eu, nibh. \n Phasellus massa felis, consequat commodo, tempus et, suscipit in, urna. Nulla facilisi. Suspendisse tincidunt, felis sed tincidunt dignissim, ante mi tincidunt neque, sed varius justo libero id ligula. In sapien neque, malesuada et, interdum sit amet, ornare in, nulla. Donec mauris. Nunc a diam ac enim convallis rhoncus. Integer congue libero vel magna. Fusce at diam et lectus blandit volutpat. Nunc volutpat consectetuer justo. Maecenas et nibh. Aliquam tristique augue non augue. Etiam at erat. Duis scelerisque nisi sed massa. Ut tincidunt ipsum id tortor. In mollis erat ut magna. Phasellus a dolor eu tellus ultrices tincidunt. \n Donec sit amet nisi at ligula faucibus feugiat. Vestibulum a leo. Donec vel augue. Etiam dapibus dolor. Etiam accumsan scelerisque quam. Mauris scelerisque, libero sit amet euismod placerat, nunc magna aliquam augue, id interdum turpis lectus sit amet urna. Suspendisse nulla orci, molestie tristique, tempor a, nonummy vel, magna. Morbi aliquam ullamcorper nibh. Aliquam magna ante, tincidunt sit amet, iaculis vel, feugiat at, elit. Nullam ac elit sit amet neque commodo facilisis. Pellentesque sit amet nunc in odio dignissim ullamcorper. Duis ullamcorper egestas mauris. Phasellus malesuada ullamcorper lacus. Vivamus aliquam porta sapien. Mauris malesuada eros et urna. \n Donec laoreet, velit ac iaculis malesuada, turpis nunc mattis tellus, ac varius est tortor sed orci. Quisque dignissim. Suspendisse libero ipsum, hendrerit non, lobortis non, dignissim vitae, purus. Sed vitae ante. Morbi aliquet arcu eu risus. Sed varius interdum risus. Quisque egestas consequat ipsum. Maecenas fermentum nulla ut arcu. Phasellus varius faucibus tortor. Aliquam rhoncus justo nec metus. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Cras faucibus. Donec feugiat semper metus. Mauris auctor metus ac odio. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos hymenaeos. Phasellus ut dui sit amet ligula placerat tempus. Etiam pulvinar magna quis lectus elementum consectetuer. Quisque velit tortor, elementum eu, interdum ut, consectetuer ut, magna. Nunc a lacus. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. \n Curabitur fringilla, ipsum eget gravida tristique, tortor est placerat lacus, sed tincidunt enim dui id turpis. Nulla iaculis elit et eros. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Integer mollis est. Donec hendrerit leo fringilla sapien. Maecenas a lectus. Etiam accumsan rhoncus magna. Cras mollis dignissim quam. Pellentesque laoreet magna at nulla. Curabitur eget ante. Nulla non turpis vel risus tristique commodo. Nulla sem. Donec egestas. Donec turpis est, pulvinar id, lacinia sit amet, ultricies non, libero. Donec condimentum. Quisque odio purus, mattis id, tincidunt nec, ultricies rhoncus, mauris. Proin tellus. \n Nullam in diam. Duis dolor ligula, consectetuer sit amet, consequat sit amet, congue at, sem. Sed at lectus vitae orci aliquam sagittis. Maecenas enim. In sodales tellus ac mauris. Aliquam erat volutpat. Fusce sodales condimentum elit. Integer vel lectus. Suspendisse potenti. Vestibulum ut urna. Vestibulum congue nisi non neque elementum malesuada. \n Cras tincidunt. Sed ullamcorper nisl quis arcu euismod lobortis. Integer tortor dolor, mollis vitae, dictum non, suscipit at, nisi. Praesent malesuada dolor et lacus. Aliquam scelerisque odio vitae libero. Aliquam eros felis, hendrerit vel, porta eu, rutrum ac, arcu. Aenean dapibus tortor ac nisi. In convallis varius ipsum. Integer hendrerit lectus ut sapien. Donec massa lectus, sagittis vel, varius in, placerat sit amet, nulla. Vestibulum turpis. In magna. Praesent ac purus vitae justo consectetuer hendrerit. \n Sed est metus, sagittis non, condimentum sed, sagittis eu, dolor. Nulla nec velit a ligula tristique posuere. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Integer lacinia auctor lorem. Integer id elit. Suspendisse mollis. Sed convallis, quam at ullamcorper varius, felis nisi euismod eros, sit amet luctus velit sem nec nisl. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos hymenaeos. Sed eleifend rhoncus nibh. Nunc iaculis volutpat justo. Proin a ipsum. \n Sed porttitor purus vel erat. Pellentesque sagittis, augue suscipit tincidunt eleifend, velit est semper est, eget dignissim nibh lacus ut massa. Vestibulum facilisis. Fusce est leo, blandit ut, interdum pharetra, fringilla et, nulla. Nullam lorem sapien, tincidunt eget, fringilla ut, auctor sit amet, felis. Aliquam erat volutpat. Cras vel orci vel lorem aliquet elementum. Aliquam imperdiet urna eu velit mattis mollis. Vestibulum eu nunc at metus accumsan euismod. Nunc sollicitudin tristique nisi. \n Nulla neque neque, dictum sit amet, rutrum nec, bibendum et, quam. Nunc lobortis iaculis tortor. Sed enim enim, dapibus sed, volutpat eu, adipiscing malesuada, neque. Curabitur laoreet pretium quam. Pellentesque convallis lectus a purus. Morbi vitae mi lacinia augue rhoncus mattis. Suspendisse sit amet turpis. Vivamus eu purus a pede interdum pulvinar. Cras pellentesque neque ut sapien. Vivamus non odio sit amet augue egestas eleifend. \n Etiam neque libero, congue nec, dictum et, placerat eget, dui. Sed nisi lectus, iaculis non, mattis eget, porta sit amet, sem. Etiam pede urna, vehicula vel, semper vel, luctus at, dolor. Suspendisse potenti. Phasellus non libero. Praesent ut est nec pede faucibus lobortis. Nulla facilisi. Quisque et ipsum et sapien eleifend imperdiet. Donec neque sem, varius vitae, vehicula at, interdum eu, urna. Nam tempor, ante sit amet fringilla consectetuer, leo nibh vulputate mauris, vitae eleifend erat lectus nec quam. Nulla facilisi. Aliquam adipiscing nunc et mauris. Donec volutpat posuere nulla. Aenean porta tempus leo. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos hymenaeos. \n Aliquam dignissim elit vel risus. Donec quam odio, tincidunt ut, congue sit amet, aliquet et, eros. In eu metus nec justo dignissim accumsan. Vestibulum arcu. Proin turpis. Integer vulputate dui quis risus. Nunc congue felis vel tortor. Vivamus et quam. Donec in nisl vitae orci accumsan rhoncus. In molestie. Donec a ipsum. Fusce mi quam, luctus sed, commodo quis, dignissim tempor, neque. Aliquam luctus consequat sem. Suspendisse mi nisi, adipiscing porta, tempus ut, rutrum quis, mauris. Proin commodo. Donec molestie condimentum tortor. In nec nulla ut ipsum mattis molestie. Mauris id nunc. Nulla sodales. Nullam tempus adipiscing mauris. \n Donec tempus interdum dui. Mauris risus. Donec viverra. Phasellus magna nisl, tristique a, hendrerit sed, pretium eu, sapien. Aliquam velit. In nonummy sodales neque. In erat lectus, mattis non, molestie ut, vestibulum ac, libero. Vivamus nec leo in libero porta pretium. Nam tortor. Cras ut nulla. Vestibulum gravida ultricies pede. Pellentesque tempus lorem in dui. \n Curabitur tempor lacus eu arcu. Sed pede enim, rhoncus pellentesque, condimentum at, pulvinar vitae, diam. Etiam ultricies. Phasellus malesuada laoreet lectus. Donec at diam. Nullam a velit. Phasellus turpis ipsum, mattis eu, mattis vel, consequat et, nisl. Fusce lectus enim, aliquam quis, imperdiet ut, interdum non, felis. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Donec eu diam. Nullam at erat quis urna vestibulum egestas. Nullam elit urna, fermentum a, faucibus quis, sodales lacinia, leo. Vivamus mi. Nunc lobortis. Quisque adipiscing, nulla tempor feugiat consequat, diam ante bibendum est, in malesuada lectus odio id odio. Morbi pretium luctus nisl. Donec mollis. Vestibulum tempor. \n Sed adipiscing, enim non bibendum tristique, diam sapien placerat velit, vitae cursus ligula est nec enim. Quisque volutpat justo a velit pellentesque tempor. Fusce in risus. Vestibulum tempus eros in urna. Vestibulum iaculis. Cras metus pede, malesuada sit amet, tempor ac, mollis vel, diam. Vivamus posuere metus non nisi. Nam et metus. Pellentesque auctor massa quis nunc. Donec nulla. \n Morbi justo elit, adipiscing ut, condimentum vel, dictum sit amet, lacus. Sed elit. Etiam in pede eget dui gravida facilisis. Vivamus pharetra, nisi non varius dapibus, odio erat elementum pede, vitae adipiscing massa mauris nec leo. Maecenas vitae metus a quam pretium sagittis. Suspendisse potenti. Curabitur orci mauris, dignissim et, venenatis eu, semper in, mi. Vestibulum vitae est. Curabitur eleifend nisi at arcu. Aliquam erat volutpat. Aliquam non lorem. Etiam faucibus neque. Nullam suscipit volutpat elit. Phasellus tempor felis. \n Cras suscipit, neque quis fermentum facilisis, nisi enim laoreet urna, sed interdum magna massa non leo. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Phasellus accumsan iaculis magna. Ut fringilla urna ut felis. Duis auctor, lectus sed gravida blandit, justo turpis facilisis elit, vitae adipiscing quam tellus et nisi. Suspendisse sit amet dolor sed turpis lobortis bibendum. Ut adipiscing urna ut turpis. Vivamus semper pede quis massa. Praesent blandit, orci quis venenatis tincidunt, ligula lorem pellentesque enim, id consectetuer ligula quam vel risus. Donec orci. Fusce consectetuer. Fusce sed urna. Suspendisse pulvinar. Maecenas cursus mi quis lorem. Donec consectetuer aliquam orci. Duis justo. \n Donec porta turpis. Donec mauris massa, aliquam quis, viverra a, congue vitae, sem. Pellentesque sodales dignissim nisl. Proin ut leo eget ipsum varius varius. Cras magna urna, rutrum quis, rhoncus eget, facilisis quis, nisi. Quisque lectus. Aliquam congue dui quis dui. Morbi vitae quam. Duis euismod condimentum metus. Nullam arcu odio, consequat a, condimentum sed, volutpat vel, tellus. Nunc in pede vitae nulla semper accumsan. Nam rutrum, leo id convallis feugiat, augue nulla ornare diam, id scelerisque est erat eu massa. Vivamus iaculis, enim sit amet luctus consequat, diam metus accumsan magna, a tempus enim sem non sapien. Nam placerat magna cursus libero. Integer blandit auctor elit. Praesent dictum. Proin sit amet pede ut lorem elementum bibendum. \n Donec libero. Vivamus in enim. Aenean faucibus venenatis nunc. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos hymenaeos. Mauris eros enim, commodo in, aliquet suscipit, interdum vel, dolor. Proin ante. Nam in massa quis est vehicula rutrum. Pellentesque sed nibh. Aliquam erat volutpat. Cras vel leo vulputate sapien porta vestibulum. Proin eleifend, nisi in mollis ultrices, dui eros tincidunt urna, a mollis mi sem quis lorem. Aenean fermentum luctus justo. Sed quis dui. Vestibulum euismod mi blandit libero. Curabitur nibh pede, consequat nec, suscipit sed, interdum malesuada, neque. Fusce eget nisl sed magna sodales dapibus. \n Duis venenatis lectus et turpis. In hac habitasse platea dictumst. Donec consectetuer tempus libero. Etiam in mi at nibh cursus elementum. Nulla volutpat dui ac tortor. Pellentesque eget elit sit amet turpis facilisis semper. Fusce quis felis. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Vivamus auctor convallis tellus. Aliquam eros. Aenean interdum. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Nullam non augue. Phasellus volutpat placerat libero. Integer risus. Aliquam tempor eleifend odio. Nullam elementum velit eu nulla. Nullam pulvinar. Nunc ipsum. Pellentesque dictum consectetuer enim. \n Suspendisse in erat et mi fermentum elementum. Curabitur tempor pulvinar eros. Sed ac turpis. Mauris et orci. Donec id nunc sit amet quam ultrices ultricies. Etiam rhoncus dui. Donec sit amet lorem et ipsum consequat vulputate. Vivamus ultrices velit ut purus. Cras massa. Sed hendrerit metus eget est. Maecenas justo elit, euismod eget, semper sit amet, tristique eget, augue. \n Donec hendrerit tincidunt erat. Nulla faucibus, justo eu convallis eleifend, pede metus tempus nunc, non scelerisque orci magna at purus. Nunc mauris nulla, condimentum et, posuere a, convallis in, velit. Ut suscipit justo mattis dolor. Mauris sit amet pede. Praesent gravida aliquet nunc. Donec tempor lorem at tortor. Nullam nisl sem, volutpat a, consectetuer at, cursus vitae, diam. Aenean eros erat, suscipit ut, iaculis a, adipiscing vel, nisl. Donec non mauris. Praesent ac purus non risus molestie cursus. Sed nunc lectus, eleifend quis, rutrum sed, commodo sed, dolor. Nullam faucibus neque ac dui. Nam pede nulla, scelerisque ut, varius nec, fermentum ut, magna. Proin scelerisque commodo augue. Vestibulum vel metus. Nam quis enim non ipsum facilisis gravida. Donec ultricies mattis pede. Sed id mi. \nPhasellus eu odio. Sed iaculis pulvinar ipsum. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Ut consequat placerat libero. Aliquam erat volutpat. Vestibulum sed ligula et nibh elementum molestie. Proin nibh. Nulla turpis. Nulla fringilla venenatis purus. In hac habitasse platea dictumst. Nullam et nulla at magna blandit volutpat. Donec elementum, arcu ut dapibus luctus, arcu enim sagittis nulla, suscipit lobortis turpis dui id urna. Vivamus vel lectus et elit iaculis gravida. Suspendisse potenti. Duis et pede. Pellentesque semper sem ac nunc. Donec commodo congue nisi. Fusce vel eros. Duis placerat ante vel magna. Donec enim risus, vestibulum interdum, lacinia eget, tincidunt eu, ipsum. \n Ut ligula. Donec ligula. Nulla mi leo, gravida imperdiet, lacinia sed, lacinia id, risus. Donec condimentum est a nunc interdum vulputate. Donec ut nulla. Aliquam vel nisi. Aliquam id quam. Cras interdum libero blandit ipsum. Donec bibendum eros. Phasellus erat leo, porttitor id, condimentum malesuada, fringilla quis, dui. Cras nonummy. Vivamus neque dui, vehicula at, aliquam ac, consequat et, elit. Sed vitae purus. Cras lacinia. Fusce non lacus. Quisque aliquet libero non nibh vestibulum volutpat. Nunc et augue quis enim posuere vestibulum. Nunc eget tellus non eros pretium cursus.\n Praesent aliquam ante accumsan eros. Donec sagittis elit sed est. Nunc bibendum molestie erat. Phasellus est sapien, condimentum sit amet, accumsan non, fringilla vel, est. Fusce enim mauris, commodo interdum, ultricies a, nonummy quis, enim. Suspendisse lectus dolor, tincidunt suscipit, posuere at, pharetra vitae, tellus. Nulla auctor urna a arcu. Phasellus imperdiet, ligula sed iaculis auctor, tellus lacus eleifend ligula, et ultricies felis lectus at elit. Phasellus urna lorem, mattis sit amet, varius ac, semper vel, justo. Proin congue ipsum non velit. Morbi tempor quam a tortor. Proin risus. Vestibulum tempor. Etiam sit amet est fringilla neque laoreet interdum. \n Integer pretium enim at sapien. Donec elementum mi bibendum diam. Sed rutrum, lacus ut malesuada tempor, nulla ante vehicula lectus, non suscipit odio nisl vel enim. Ut malesuada risus a tellus. Nullam pretium auctor justo. Integer luctus, erat sit amet malesuada venenatis, turpis neque cursus lacus, et varius tortor massa quis enim. Aliquam erat volutpat. Fusce in tellus nec felis scelerisque laoreet. In fermentum imperdiet neque. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Mauris ligula. Duis euismod, mauris eget aliquet faucibus, nisl orci viverra augue, ut sagittis sem arcu in enim. Donec erat lorem, aliquam in, bibendum nec, laoreet quis, justo. \n Nulla et tortor. Nulla ligula lacus, mollis ut, congue id, aliquet sed, diam. Maecenas sagittis faucibus metus. Mauris eu purus. Nunc posuere semper urna. Mauris magna mi, lobortis ac, gravida at, mollis sed, nibh. In at metus quis enim tincidunt viverra. Vestibulum dapibus magna at velit. Integer volutpat nisl ac erat. Nulla neque nisl, molestie ut, tincidunt sed, aliquet sed, neque. Cras vehicula, sapien quis suscipit consequat, velit ligula imperdiet arcu, at consequat tellus dolor vel enim. Pellentesque ornare venenatis nunc. \n Aenean iaculis eros quis dui. Mauris luctus ultrices ligula. Nullam in eros. Praesent ipsum. Sed ornare nunc vitae purus. Ut felis. Nulla non velit. Nulla hendrerit quam. Sed rutrum, odio at ullamcorper rutrum, lacus pede gravida nibh, ac posuere sem sem sed magna. Vivamus nunc augue, iaculis sagittis, suscipit eget, lacinia a, leo. Mauris rutrum, diam vel vehicula blandit, felis lacus sagittis magna, sit amet tincidunt erat arcu et mi. Sed consequat quam et dolor. Fusce et nunc vitae dui vulputate fermentum. Aliquam arcu mi, euismod et, congue dapibus, eleifend eu, lacus. Nullam ultrices mauris vitae turpis. Donec pede orci, vehicula ac, commodo sit amet, sollicitudin sit amet, odio. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. \n Praesent tempor, libero sit amet adipiscing convallis, quam leo varius pede, ac malesuada lectus lectus et nulla. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Fusce congue, justo vel hendrerit consectetuer, turpis elit consequat orci, eget bibendum erat ligula a metus. Donec lectus. Nullam metus. Phasellus ultrices, dolor quis viverra gravida, risus dui tempus augue, eu suscipit nunc erat at nisi. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Suspendisse nisi mi, eleifend eu, pellentesque nec, adipiscing quis, sem. Fusce aliquam, mi lobortis gravida convallis, eros ante congue augue, non pretium magna nisi ut enim. Nullam porttitor imperdiet pede. \n Suspendisse pulvinar ipsum vitae neque. Morbi ultricies euismod nisl. Curabitur pharetra elementum erat. Nulla vel nibh vitae turpis cursus fermentum. Proin porta, nibh sed rhoncus ultricies, arcu justo feugiat lacus, non dictum tortor ante sed orci. Phasellus euismod, eros at iaculis consequat, sem justo ornare mi, et sagittis nisi risus id mi. Quisque sed felis. Praesent consectetuer. Pellentesque aliquet neque a augue. Etiam sem. Vivamus ac urna sollicitudin odio fringilla pellentesque. Morbi varius ante at nulla. Integer arcu mi, placerat sed, semper non, molestie ut, enim. Maecenas ullamcorper eleifend erat. Phasellus tellus nibh, malesuada quis, lacinia eget, scelerisque consectetuer, sapien. Donec vel arcu. Curabitur sit amet erat eu ante varius commodo. Morbi lorem urna, commodo eget, vehicula non, malesuada sit amet, purus. Nam hendrerit consequat urna. In quis risus. \n Aliquam nisl. Donec gravida. Nunc tincidunt. Suspendisse fermentum, orci vel vehicula sodales, tortor eros fermentum mauris, sit amet blandit quam dui at mauris. Etiam vel massa blandit orci aliquam posuere. Vivamus molestie, ligula quis convallis molestie, magna sapien aliquam massa, at suscipit risus neque laoreet nunc. Maecenas luctus dolor id urna. Suspendisse nonummy. Nulla auctor mi nec sapien. Aenean hendrerit sem sit amet magna. \n Curabitur augue nunc, hendrerit et, facilisis et, scelerisque id, massa. Pellentesque nonummy congue nibh. Nulla et augue. Nulla velit felis, hendrerit ut, suscipit eu, scelerisque at, justo. Nulla at velit. Suspendisse quis lorem ut augue sollicitudin ullamcorper. Etiam nulla quam, laoreet in, scelerisque id, laoreet eu, risus. Donec sodales egestas ante. Vestibulum ullamcorper. Nullam id dui. Integer imperdiet. Aenean metus nulla, accumsan id, luctus at, pharetra at, turpis. Proin diam velit, hendrerit ut, commodo quis, pulvinar non, elit. Vivamus egestas, nisl in euismod ultricies, nisl justo condimentum dolor, vitae posuere mi justo ut justo. Donec ullamcorper dui. Nullam et mauris. Aenean ac tellus. Nulla placerat dictum nibh. \n Phasellus enim leo, luctus vitae, molestie eu, interdum ut, pede. Sed in enim dapibus tellus molestie bibendum. Aliquam vitae leo. Quisque purus. Suspendisse potenti. Duis vehicula volutpat ligula. Donec nec diam. Etiam leo. Suspendisse nisi dui, cursus sed, fermentum et, mattis vitae, nisi. Sed blandit, tortor quis tristique elementum, urna urna sollicitudin metus, nec imperdiet velit libero sagittis turpis. Donec vehicula lacus ac enim pellentesque ultricies. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Proin sed dolor. Vestibulum. ");
		blogNode.setProperty("kronos:author", "The lorem ipsum generator");
		comments = blogNode.addNode("kronos:comments");
		
/* Start of ToDo plugin test */
		
//ToDo Plugin with all items		
		//put test parameters in todo Repository
		Node todoPlugin = plugins.addNode("kronos:plugininstance");
		todoPlugin.setProperty("kronos:name", "ToDo");
		todoPlugin.setProperty("kronos:position", 2);
		todoPlugin.setProperty("kronos:published", false);
		todoPlugin.setProperty("kronos:order", 2);
		todoPlugin.setProperty("kronos:pluginType",
				"wicket.kronos.plugins.ToDo.ToDoPlugin");
		
		Node todoItems = pluginContent.addNode("kronos:todoitems", "kronos:ToDoItems");
		Node todoItem = todoItems.addNode("kronos:todoitem");
		todoItem.setProperty("kronos:pluginname", "ToDo");
		todoItem.setProperty("kronos:title", "Threaded discussions");
		todoItem.setProperty("kronos:subject", "Blog comments can be threads");
		todoItem.setProperty("kronos:content", "Comments that are posted on a blog item, can be handled as threaded discussions");
		todoItem.setProperty("kronos:done", false);
		todoItem.setProperty("kronos:date", date);
		
		todoItem = todoItems.addNode("kronos:todoitem");
		todoItem.setProperty("kronos:pluginname", "ToDo");
		todoItem.setProperty("kronos:title", "Place Repeater in Form");
		todoItem.setProperty("kronos:subject", "Repeaters should be placed on a Form");
		todoItem.setProperty("kronos:content", "Repeaters are currently place on a Panel, which should be Forms, so they can be adjusted");
		todoItem.setProperty("kronos:done", true);
		todoItem.setProperty("kronos:date", date);
		
		todoItem = todoItems.addNode("kronos:todoitem");
		todoItem.setProperty("kronos:pluginname", "ToDo");
		todoItem.setProperty("kronos:title", "Authorization");
		todoItem.setProperty("kronos:subject", "Authorization must be implemented");
		todoItem.setProperty("kronos:content", "The authorization must be implemented for the entire CMS. This will include all the plugins");
		todoItem.setProperty("kronos:done", false);
		todoItem.setProperty("kronos:date", date);
		
		//add menu item
		Node menuItem4 = menu.addNode("kronos:menuitem");
		menuItem4.setProperty("kronos:menuitemname", "ToDo");
		menuItem4.setProperty("kronos:linkType", "intern");
		menuItem4.setProperty("kronos:isAdmin", false);
		menuItem4.setProperty("kronos:IDType", "plugin");
		menuItem4.setProperty("kronos:ID", todoPlugin.getUUID());
		
		//put test parameters in todo Repository
		todoPlugin = plugins.addNode("kronos:plugininstance", "kronos:UnfinishedToDoPlugin");
		todoPlugin.setProperty("kronos:name", "unfinishedToDo");
		todoPlugin.setProperty("kronos:position", 3);
		todoPlugin.setProperty("kronos:published", true);
		todoPlugin.setProperty("kronos:order", 2);
		todoPlugin.setProperty("kronos:plugincontentname", "ToDo");
		todoPlugin.setProperty("kronos:pluginType",
				"wicket.kronos.plugins.unfinishedtodo.UnfinishedToDoItemsPlugin");
		
		//TODO add admin tests for ToDo plugin
		
/* End of ToDo plugin test */
		
/* Start of admin menu test */
		
//		Creating menu structure
		Node adminmenus = cms.addNode("kronos:adminmenus");
		Node adminmenu = adminmenus.addNode("kronos:adminmenu"); 
		
	//Home menu item with submenu items
		Node adminmenuitem1 = adminmenu.addNode("kronos:adminmenuitem");
			adminmenuitem1.setProperty("kronos:name", "Home");
			adminmenuitem1.setProperty("kronos:IDType", "frontpage");
			adminmenuitem1.setProperty("kronos:ID", adminmenu.getUUID());
		Node adminsubmenuitem1 = adminmenuitem1.addNode("kronos:adminsubmenuitem");
			adminsubmenuitem1.setProperty("kronos:name", "Home");
			adminsubmenuitem1.setProperty("kronos:IDType", "adminpage");
			adminsubmenuitem1.setProperty("kronos:ID", adminmenu.getUUID());
		Node adminsubmenuitem2 = adminmenuitem1.addNode("kronos:adminsubmenuitem");
			adminsubmenuitem2.setProperty("kronos:name", "Frontpage");
			adminsubmenuitem2.setProperty("kronos:IDType", "frontpage");
			adminsubmenuitem2.setProperty("kronos:ID", adminmenu.getUUID());
		
	//User menu item with submenu items		
		Node adminmenuitem2 = adminmenu.addNode("kronos:adminmenuitem");
			adminmenuitem2.setProperty("kronos:name", "Users");
			adminmenuitem2.setProperty("kronos:ID", "#");
		Node adminsubmenuitem3 = adminmenuitem2.addNode("kronos:adminsubmenuitem");
			adminsubmenuitem3.setProperty("kronos:name", "Listing");
			adminsubmenuitem3.setProperty("kronos:IDType", "adminpage");
			adminsubmenuitem3.setProperty("kronos:ID", adminmenu.getUUID());
		Node adminsubmenuitem4 = adminmenuitem2.addNode("kronos:adminsubmenuitem");
			adminsubmenuitem4.setProperty("kronos:name", "Add User");
			adminsubmenuitem4.setProperty("kronos:IDType", "adminpage");
			adminsubmenuitem4.setProperty("kronos:ID", adminmenu.getUUID());
		Node adminsubmenuitem5 = adminmenuitem2.addNode("kronos:adminsubmenuitem");
			adminsubmenuitem5.setProperty("kronos:name", "Roles");
			adminsubmenuitem5.setProperty("kronos:IDType", "adminpage");
			adminsubmenuitem5.setProperty("kronos:ID", adminmenu.getUUID());
	
	//Menu menu item with submenu items		
		Node adminmenuitem3 = adminmenu.addNode("kronos:adminmenuitem");
			adminmenuitem3.setProperty("kronos:name", "Menu");
			adminmenuitem3.setProperty("kronos:ID", "#");
			
	//Plugins menu item with submenu items
		Node adminmenuitem4 = adminmenu.addNode("kronos:adminmenuitem");
			adminmenuitem4.setProperty("kronos:name", "Plugins");
			adminmenuitem4.setProperty("kronos:ID", "#");
			
		Node adminsubmenuitem6 = adminmenuitem4.addNode("kronos:adminsubmenuitem");
			adminsubmenuitem6.setProperty("kronos:name", "Upload JAR");
			adminsubmenuitem6.setProperty("kronos:IDType", "AdminPluginUpload");
			adminsubmenuitem6.setProperty("kronos:ID", "");
		Node adminsubmenuitem7 = adminmenuitem4.addNode("kronos:adminsubmenuitem");
			adminsubmenuitem7.setProperty("kronos:name", "Add Instance");
			adminsubmenuitem7.setProperty("kronos:IDType", "AdminNewPlugin");
			adminsubmenuitem7.setProperty("kronos:ID","");
			
	//Configuration menu item with submenu items
		Node adminmenuitem5 = adminmenu.addNode("kronos:adminmenuitem");
			adminmenuitem5.setProperty("kronos:name", "Configuration");
			adminmenuitem5.setProperty("kronos:ID", "#");
		
			Node adminsubmenuitem8 = adminmenuitem5.addNode("kronos:adminsubmenuitem");
			adminsubmenuitem8.setProperty("kronos:name", "MediaManager");
			adminsubmenuitem8.setProperty("kronos:IDType", "MediaManager");
			adminsubmenuitem8.setProperty("kronos:ID","");
			
	//Help menu item with submenu items
		Node adminmenuitem6 = adminmenu.addNode("kronos:adminmenuitem");
			adminmenuitem6.setProperty("kronos:name", "Help");
			adminmenuitem6.setProperty("kronos:ID", "#");
		
/* End of admin menu test */
			
		Node pluginsNode = cms.addNode("kronos:plugins");
		Node pluginNode = pluginsNode.addNode("kronos:plugin");
		pluginNode.setProperty("kronos:canonicalpluginname", "wicket.kronos.plugins.helloworld.HelloWorld");

		pluginNode = pluginsNode.addNode("kronos:plugin");
		pluginNode.setProperty("kronos:canonicalpluginname", "wicket.kronos.plugins.blog.BlogPlugin");

		pluginNode = pluginsNode.addNode("kronos:plugin");
		pluginNode.setProperty("kronos:canonicalpluginname", "wicket.kronos.plugins.banner.BannerPlugin");
		pluginNode.setProperty("kronos:nodetype", "kronos:BannerPlugin");

		pluginNode = pluginsNode.addNode("kronos:plugin");
		pluginNode.setProperty("kronos:canonicalpluginname", "wicket.kronos.plugins.login.LoginPlugin");

		pluginNode = pluginsNode.addNode("kronos:plugin");
		pluginNode.setProperty("kronos:canonicalpluginname", "wicket.kronos.plugins.menu.MenuPlugin");
		pluginNode.setProperty("kronos:nodetype", "kronos:MenuPlugin");

		pluginNode = pluginsNode.addNode("kronos:plugin");
		pluginNode.setProperty("kronos:canonicalpluginname", "wicket.kronos.plugins.titan.TitanPlugin");

		pluginNode = pluginsNode.addNode("kronos:plugin");
		pluginNode.setProperty("kronos:canonicalpluginname", "wicket.kronos.plugins.ToDo.ToDoPlugin");

		pluginNode = pluginsNode.addNode("kronos:plugin");
		pluginNode.setProperty("kronos:canonicalpluginname", "wicket.kronos.plugins.unfinishedtodo.UnfinishedToDoItemsPlugin");
		pluginNode.setProperty("kronos:nodetype", "kronos:UnfinishedToDoPlugin");
		
		pluginNode = pluginsNode.addNode("kronos:plugin");
		pluginNode.setProperty("kronos:canonicalpluginname", "wicket.kronos.plugins.version.VersionPlugin");
		
		session.save();

	}
}
