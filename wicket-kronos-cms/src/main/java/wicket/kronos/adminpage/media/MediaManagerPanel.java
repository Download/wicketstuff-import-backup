package wicket.kronos.adminpage.media;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
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
import wicket.model.Model;
import wicket.util.lang.Bytes;

public class MediaManagerPanel extends Panel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List images = null;

	public MediaManagerPanel(String wicketId)
	{
		super(wicketId);
		images = DataProcessor.getImages();
		add(new MediaManForm("mediamanform"));
		
		//Add upload form with ajax progress bar
		final UploadForm uploadForm = new UploadForm("uploadform");
		uploadForm.add(new UploadProgressBar("progress", uploadForm));
		add(uploadForm);
		
	}
	
	
	public class MediaManForm extends Form{

		CheckBox selected; 
		
		public MediaManForm(String wicketId)
		{
			super(wicketId);
			ListView imageList = new ListView("imagelist", images){

				@Override
				protected void populateItem(ListItem item)
				{
					final CMSImageResource image = (CMSImageResource)item.getModelObject();
					PopupSettings popupSettings = new PopupSettings(PageMap
							.forName("popuppagemap"), PopupSettings.SCROLLBARS);
					
					Link imageLink = new Link("imagelink"){
						@Override
						public void onClick()
						{
							setResponsePage(new ImagePopup(image));
						}
					};
					//TODO Possible to delete images
					selected = new CheckBox("selected", new Model());
					item.add(selected);
					imageLink.add(new Image("image", image));
					imageLink.setPopupSettings(popupSettings);
					item.add(imageLink);
					item.add(new Label("imagelabel", image.getImageName()));
				}
				
			};
			add(imageList);
			
		}
		
		public void onSubmit()
		{
			//System.out.println(selected.getModelObjectAsString());
		}
	}
	
	/**
	 * Form for uploads.
	 */
	private class UploadForm extends Form
	{
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
		protected void onSubmit()
		{
			final FileUpload upload = fileUploadField.getFileUpload();
			if (upload != null)
			{
				Session jcrSession = KronosSession.get().getJCRSession();
				try
				{
					String fileName = upload.getClientFileName();
					Node imageNode = jcrSession.getRootNode().getNode("kronos:cms").getNode("kronos:content").getNode("kronos:images").addNode(fileName, "nt:file");
					
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
