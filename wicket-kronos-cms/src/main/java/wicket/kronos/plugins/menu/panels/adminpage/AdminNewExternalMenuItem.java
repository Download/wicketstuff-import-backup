package wicket.kronos.plugins.menu.panels.adminpage;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import wicket.kronos.DataProcessor;
import wicket.kronos.adminpage.AdminPage;
import wicket.markup.html.form.Button;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.TextField;
import wicket.markup.html.panel.Panel;
import wicket.model.PropertyModel;

/**
 * Creation of an new external menu item
 * 
 * @author postma
 */
public class AdminNewExternalMenuItem extends Panel {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param wicketId
	 * @param menuName
	 */
	public AdminNewExternalMenuItem(String wicketId, String menuName)
	{
		super(wicketId);
		add(new ExternalMenuItemForm("externalMenuitemForm", menuName));
	}

	/**
	 * @author postma
	 */
	public class ExternalMenuItemForm extends Form {

		/**
		 * Default serialVersionUID
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor.
		 * 
		 * @param wicketId
		 * @param menuName
		 */
		public ExternalMenuItemForm(String wicketId, final String menuName)
		{
			super(wicketId);
			final NewExternalMenuItemModel nemim = new NewExternalMenuItemModel();
			add(new TextField("order", new PropertyModel(nemim, "order")));
			add(new TextField("linkname", new PropertyModel(nemim, "name")));
			add(new TextField("externallink", new PropertyModel(nemim, "link")));

			// TODO check to make sure supplied link is valid

			add(new Button("saveMenuItem") {
				@Override
				public void onSubmit()
				{
					String linkname = nemim.getName();
					String externallink = nemim.getLink();
					if(checkUrl(externallink))
					{
						int order = nemim.getOrder();
						DataProcessor.saveNewExternalMenuItem(menuName, linkname, order, externallink);
						setResponsePage(AdminPage.class);
					} else {
						info("The URL is not Valid");
					}
				}
			});
		}
		
		private boolean checkUrl(String urlToCheck)
		{
			boolean result = false;

	        if (!urlToCheck.substring(0, 7).equals("http://")) {
	            urlToCheck = "http://" + urlToCheck;
	        }

	        try {

	            URL url = new URL(urlToCheck);
	            URLConnection connection = url.openConnection();

	            if (connection instanceof HttpURLConnection) {

	                HttpURLConnection httpConnection = (HttpURLConnection) connection;
	                httpConnection.connect();

	                int response = httpConnection.getResponseCode();
	                if (response >= 200 && response < 400) {
	                    result = true;
	                }
	                InputStream is = httpConnection.getInputStream();
	                byte[] buffer = new byte[256];
	                while (is.read(buffer) != -1) {

	                }
	                is.close();
	            }
	        } catch (IOException e) {
	            result = false;
	        }
	        return result;
		}
	}

	/**
	 * Model for a new external menu item
	 * 
	 * @author postma
	 */
	public class NewExternalMenuItemModel implements Serializable {

		/**
		 * Default serialVErsionUID
		 */
		private static final long serialVersionUID = 1L;

		String name;

		String link;

		int order;

		/**
		 * Constructor.
		 */
		public NewExternalMenuItemModel()
		{
			name = null;
			link = "http://";
			order = 1;
		}

		/**
		 * @return the link
		 */
		public String getLink()
		{
			return link;
		}

		/**
		 * @param link
		 */
		public void setLink(String link)
		{
			this.link = link;
		}

		/**
		 * @return the name
		 */
		public String getName()
		{
			return name;
		}

		/**
		 * @param name
		 */
		public void setName(String name)
		{
			this.name = name;
		}

		/**
		 * @return order
		 */
		public int getOrder()
		{
			return order;
		}

		/**
		 * @param order
		 */
		public void setOrder(int order)
		{
			this.order = order;
		}
	}
}
