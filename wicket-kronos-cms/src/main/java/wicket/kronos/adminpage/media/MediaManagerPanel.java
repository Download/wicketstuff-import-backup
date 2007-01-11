package wicket.kronos.adminpage.media;

import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.version.VersionException;

import sun.net.www.MimeTable;
import wicket.PageMap;
import wicket.PageParameters;
import wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar;
import wicket.kronos.CMSImageResource;
import wicket.kronos.DataProcessor;
import wicket.kronos.KronosSession;
import wicket.kronos.adminpage.AdminPage;
import wicket.kronos.adminpage.media.popup.ImagePopup;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.CheckBox;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.upload.FileUpload;
import wicket.markup.html.form.upload.FileUploadField;
import wicket.markup.html.image.Image;
import wicket.markup.html.link.Link;
import wicket.markup.html.link.PopupSettings;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.markup.html.panel.Panel;
import wicket.model.CompoundPropertyModel;
import wicket.model.PropertyModel;
import wicket.util.lang.Bytes;

/**
 * @author postma
 */
public class MediaManagerPanel extends Panel {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private List<CMSImageResource> images = null;

	/**
	 * Constructor.
	 * 
	 * @param wicketId
	 */
	public MediaManagerPanel(String wicketId)
	{
		super(wicketId);
		images = DataProcessor.getImages();
		add(new MediaManForm("mediamanform"));

		// Add upload form with ajax progress bar
		final UploadForm uploadForm = new UploadForm("uploadform");
		uploadForm.add(new UploadProgressBar("progress", uploadForm));
		add(uploadForm);

	}

	/**
	 * @author postma
	 */
	public class MediaManForm extends Form {

		/**
		 * Default serialVersionUID
		 */
		private static final long serialVersionUID = 1L;

		private CheckBox selected;

		private ListView imageList;

		/**
		 * Constructor.
		 * 
		 * @param wicketId
		 */
		public MediaManForm(String wicketId)
		{
			super(wicketId, new CompoundPropertyModel(new MediaModel(images)));
			imageList = new ListView("imagelist", images) {

				@Override
				protected void populateItem(ListItem item)
				{
					final CMSImageResource image = (CMSImageResource) item.getModelObject();
					PopupSettings popupSettings = new PopupSettings(
							PageMap.forName("popuppagemap"), PopupSettings.SCROLLBARS);

					Link imageLink = new Link("imagelink") {
						@Override
						public void onClick()
						{
							setResponsePage(new ImagePopup(image));
						}
					};
					// TODO Possible to delete images
					selected = new CheckBox("selected", new PropertyModel(image, "selected"));
					item.add(selected);
					imageLink.add(new Image("image", image));
					imageLink.setPopupSettings(popupSettings);
					item.add(imageLink);
					item.add(new Label("imagelabel", image.getImageName()));
				}

			};
			add(imageList);

		}

		@Override
		public void onSubmit()
		{
			MediaModel model = (MediaModel) this.getModelObject();
			List modelImagelist = model.getImagelist();
			Iterator i = modelImagelist.iterator();
			while (i.hasNext())
			{
				CMSImageResource image = (CMSImageResource) i.next();
				if (image.isSelected())
				{
					String imageName = image.getImageName();
					Session jcrSession = KronosSession.get().getJCRSession();

					try
					{
						Workspace ws = jcrSession.getWorkspace();
						QueryManager qm = ws.getQueryManager();
						Query q = qm.createQuery(
								"//kronos:content/kronos:images/" + imageName + "", Query.XPATH);

						QueryResult result = q.execute();
						NodeIterator it = result.getNodes();

						if (it.hasNext())
						{
							Node n = it.nextNode();
							n.remove();
							jcrSession.save();
						}
					}
					catch (RepositoryException e)
					{
						e.printStackTrace();
					}
				}
			}
			PageParameters param = new PageParameters();
			param.add("IDType", "mediamanager");
			param.add("ID", "");
			setResponsePage(AdminPage.class, param);
		}
	}

	private class MediaModel implements Serializable {
		/**
		 * Default serialVersionUID
		 */
		private static final long serialVersionUID = 1L;

		private List<CMSImageResource> imagelist;

		/**
		 * Constructor.
		 * 
		 * @param imagelist
		 */
		public MediaModel(List<CMSImageResource> imagelist)
		{
			this.imagelist = imagelist;
		}

		/**
		 * @return List
		 */
		public List<CMSImageResource> getImagelist()
		{
			return imagelist;
		}

		/**
		 * @param imagelist
		 */
		public void setImagelist(List<CMSImageResource> imagelist)
		{
			this.imagelist = imagelist;
		}
	}

	/**
	 * Form for uploads.
	 */
	private class UploadForm extends Form {
		/**
		 * Default serialVersionUID
		 */
		private static final long serialVersionUID = 1L;

		private FileUploadField fileUploadField;

		/**
		 * Construct.
		 * 
		 * @param name
		 *            Component name
		 */
		public UploadForm(String name)
		{
			super(name);

			// set this form to multipart mode (allways needed for uploads!)
			setMultiPart(true);

			// Add one file input field
			add(fileUploadField = new FileUploadField("fileInput"));

			// Set maximum size to 100K for demo purposes
			setMaxSize(Bytes.kilobytes(1024));
		}

		/**
		 * @see wicket.markup.html.form.Form#onSubmit()
		 */
		@Override
		protected void onSubmit()
		{
			final FileUpload upload = fileUploadField.getFileUpload();
			if (upload != null)
			{
				Session jcrSession = KronosSession.get().getJCRSession();
				try
				{
					String fileName = upload.getClientFileName();
					Node imageNode = jcrSession.getRootNode().getNode("kronos:cms").getNode(
							"kronos:content").getNode("kronos:images").addNode(fileName, "nt:file");

					MimeTable mt = MimeTable.getDefaultTable();
					String mimeType = mt.getContentTypeFor(fileName);
					if (mimeType == null) mimeType = "application/octet-stream";

					imageNode.addMixin("mix:referenceable");
					Node resNode = imageNode.addNode("jcr:content", "nt:resource");
					resNode.setProperty("jcr:mimeType", mimeType);
					resNode.setProperty("jcr:encoding", "");
					resNode.setProperty("jcr:data", upload.getInputStream());
					Calendar lastModified = new GregorianCalendar();
					resNode.setProperty("jcr:lastModified", lastModified);

					jcrSession.save();
				}
				catch (ItemExistsException e)
				{
					e.printStackTrace();
				}
				catch (PathNotFoundException e)
				{
					e.printStackTrace();
				}
				catch (VersionException e)
				{
					e.printStackTrace();
				}
				catch (ConstraintViolationException e)
				{
					e.printStackTrace();
				}
				catch (LockException e)
				{
					e.printStackTrace();
				}
				catch (RepositoryException e)
				{
					e.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			PageParameters param = new PageParameters();
			param.add("IDType", "mediamanager");
			param.add("ID", "");
			setResponsePage(AdminPage.class, param);
		}
	}
}
