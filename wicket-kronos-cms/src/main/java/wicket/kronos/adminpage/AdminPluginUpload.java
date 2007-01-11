package wicket.kronos.adminpage;

import java.io.File;

import wicket.kronos.DataProcessor;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.upload.FileUpload;
import wicket.markup.html.form.upload.FileUploadField;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.markup.html.panel.Panel;
import wicket.util.file.Files;
import wicket.util.file.Folder;
import wicket.util.lang.Bytes;

/**
 * @author postma
 */
public class AdminPluginUpload extends Panel {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private Folder uploadFolder = null;

	/**
	 * Constructor.
	 * 
	 * @param wicketId
	 */
	public AdminPluginUpload(String wicketId)
	{
		super(wicketId);

		this.uploadFolder = new Folder("./src/main/java/wicket/kronos/plugins");

		// ensuring folder exists
		this.uploadFolder.mkdirs();

		// Create feedback panels
		final FeedbackPanel uploadFeedback = new FeedbackPanel("uploadFeedback");

		// Add uploadFeedback to the page itself
		add(uploadFeedback);

		// Add simple upload form, which is hooked up to its feedback panel by
		// virtue of that panel being nested in the form.
		final FileUploadForm simpleUploadForm = new FileUploadForm("simpleUpload");
		add(simpleUploadForm);
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
	private class FileUploadForm extends Form {
		/**
		 * Default serialVersionUID
		 */
		private static final long serialVersionUID = 1L;

		private FileUploadField fileUploadField;

		/**
		 * Constructor.
		 * 
		 * @param wicketId
		 */
		public FileUploadForm(String wicketId)
		{
			super(wicketId);

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
		@Override
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
