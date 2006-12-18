package wicket.kronos.adminpage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import wicket.kronos.DataProcessor;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.TextField;
import wicket.markup.html.form.upload.FileUpload;
import wicket.markup.html.form.upload.FileUploadField;
import wicket.markup.html.link.Link;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.markup.html.panel.Panel;
import wicket.model.Model;
import wicket.util.file.Files;
import wicket.util.file.Folder;
import wicket.util.lang.Bytes;

public class AdminPluginUpload extends Panel {
	
	//private String uploadDir = null;
	
	private Folder uploadFolder = null;
	
	/** List of files, model for file table. */
	private List files = new ArrayList();

	/** Reference to listview for easy access. */
	//private FileListView fileListView;
	
	private TextField pluginname = null;
	
	public AdminPluginUpload(String wicketId) 
	{
		super(wicketId);
		//"\\Kronos.src.main.java.wicket.kronos.plugins"
		this.uploadFolder = new Folder("./src/main/java/wicket/kronos/plugins");
		
		//ensuring folder exists
		this.uploadFolder.mkdirs();
		
		//Create feedback panels
		final FeedbackPanel uploadFeedback = new FeedbackPanel("uploadFeedback");

		// Add uploadFeedback to the page itself
		add(uploadFeedback);
		
		// Add simple upload form, which is hooked up to its feedback panel by
		// virtue of that panel being nested in the form.
		final FileUploadForm simpleUploadForm = new FileUploadForm("simpleUpload");
		add(simpleUploadForm);	
	}
	
	/**
	 * Refresh file list.
	 */
	private void refreshFiles()
	{
		//fileListView.modelChanging();
		files.clear();
		files.addAll(Arrays.asList(uploadFolder.listFiles()));
	}

	/**
	 * Check whether the file allready exists, and if so, try to delete it.
	 * 
	 * @param newFile
	 *            the file to check
	 */
	private void checkFileExists(File newFile)
	{
		if (newFile.exists())
		{
			// Try to delete the file
			if (!Files.remove(newFile))
			{
				throw new IllegalStateException("Unable to overwrite " + newFile.getAbsolutePath());
			}
		}
	}

	/**
	 * Form for uploads.
	 */
	private class FileUploadForm extends Form
	{
		private FileUploadField fileUploadField;

		/**
		 * Construct.
		 * 
		 * @param name
		 *            Component name
		 */
		public FileUploadForm(String name)
		{
			super(name);

			// set this form to multipart mode (allways needed for uploads!)
			setMultiPart(true);
			
			// Add one file input field
			add(fileUploadField = new FileUploadField("fileUpload"));
			
			// Add folder view
			add(new Label("dir", uploadFolder.getAbsolutePath()));
			
			// Set maximum size to 100K for demo purposes
			setMaxSize(Bytes.kilobytes(100));
		}

		/**
		 * @see wicket.markup.html.form.Form#onSubmit()
		 */
		protected void onSubmit()
		{
			final FileUpload upload = fileUploadField.getFileUpload();
			if (upload != null)
			{
				// Create a new file
				File newFile = new File(uploadFolder, upload.getClientFileName());

				// Check new file, delete if it allready existed
				checkFileExists(newFile);
				try
				{
					// Save to new file
					newFile.createNewFile();
					upload.writeTo(newFile);

					AdminPluginUpload.this.info("saved file: " + upload.getClientFileName());
					DataProcessor.savePlugin(newFile.getCanonicalPath());
				}
				catch (Exception e)
				{
					throw new IllegalStateException("Unable to write file.");
				}
			}
		}
	}
}
