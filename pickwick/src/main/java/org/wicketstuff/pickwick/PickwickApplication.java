package org.wicketstuff.pickwick;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Application;
import org.apache.wicket.IRequestTarget;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.request.RequestParameters;
import org.apache.wicket.request.target.basic.EmptyRequestTarget;
import org.apache.wicket.request.target.basic.URIRequestTargetUrlCodingStrategy;
import org.apache.wicket.request.target.component.BookmarkablePageRequestTarget;
import org.apache.wicket.request.target.component.IBookmarkablePageRequestTarget;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.apache.wicket.settings.IApplicationSettings;
import org.apache.wicket.settings.ISessionSettings;
import org.apache.wicket.util.resource.AbstractResourceStream;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.resource.ZipResourceStream;
import org.wicketstuff.pickwick.auth.PickwickLoginPage;
import org.wicketstuff.pickwick.auth.PickwickSession;
import org.wicketstuff.pickwick.backend.ImageUtils;
import org.wicketstuff.pickwick.backend.Settings;
import org.wicketstuff.pickwick.backend.converter.ImageConverter;
import org.wicketstuff.pickwick.frontend.pages.ImagePage;
import org.wicketstuff.pickwick.frontend.pages.SequencePage;

import com.google.inject.Inject;

/**
 * Wicket application for PickWick
 * 
 * @author <a href="mailto:jbq@apache.org">Jean-Baptiste Quenot</a>
 */
public class PickwickApplication extends AuthenticatedWebApplication {

	@Inject
	ImageUtils imageUtils;

	@Inject
	FeedGenerator feedGenerator;

	@Inject
	Settings settings;
	
	@Inject
	ImageConverter imageConverter;

	public static final String SEQUENCE_PAGE_PATH = "sequence";

	public static final String SCALED_IMAGE_PATH = "scaled";

	public static final String THUMBNAIL_IMAGE_PATH = "thumbnail";

	public static final String IMAGE_PAGE_PATH = "image";

	public static final String FEED_PATH = "feed";

	private static final Log log = LogFactory.getLog(PickwickApplication.class);

	@Override
	public Class getHomePage() {
		return SequencePage.class;
	}

	public PickwickApplication() {
	}

	@Override
	protected void init() {
		if (getConfigurationType().equals(DEVELOPMENT) && new File("src/main/resources").exists()) {
			getResourceSettings().addResourceFolder("src/main/resources");
		}
		// FIXME how to specify url coding strategy for the home page?
		mount(new URIRequestTargetUrlCodingStrategy("/" + SEQUENCE_PAGE_PATH) {
			@Override
			public IRequestTarget decode(RequestParameters requestParameters) {
				// Get request URI
				String uri = getURI(requestParameters);
				
				// check that folder exists!
				File imageFolder = imageUtils.toFile(uri);
				log.debug("imageFile: " + imageFolder);
				
				if (! (imageFolder.exists() && imageFolder.isDirectory()))
					return null;

				// Point to the SequencePage
				PageParameters params = new PageParameters();
				params.add("uri", uri);
				return new BookmarkablePageRequestTarget(SequencePage.class, params);
			}

			@Override
			public boolean matches(IRequestTarget requestTarget) {
				if (requestTarget instanceof IBookmarkablePageRequestTarget) {
					return ((IBookmarkablePageRequestTarget) requestTarget).getPageClass().equals(SequencePage.class);
				}
				return false;
			}
		});
		mount(new URIRequestTargetUrlCodingStrategy("/" + IMAGE_PAGE_PATH) {
			@Override
			public IRequestTarget decode(RequestParameters requestParameters) {
				// Get request URI
				String uri = getURI(requestParameters);

				// check that file exists!
				File imageFile = imageUtils.toFile(uri);
				log.debug("imageFile: " + imageFile);
				
				if (! (imageFile.exists() && imageFile.isFile()))
					return null;

				// Point to the ImagePage
				PageParameters params = new PageParameters();
				params.add("uri", uri);
				return new BookmarkablePageRequestTarget(ImagePage.class, params);
			}

			@Override
			public boolean matches(IRequestTarget requestTarget) {
				if (requestTarget instanceof IBookmarkablePageRequestTarget) {
					return ((IBookmarkablePageRequestTarget) requestTarget).getPageClass().equals(ImagePage.class);
				}
				return false;
			}
		});
		mount(new URIRequestTargetUrlCodingStrategy("/" + THUMBNAIL_IMAGE_PATH) {
			@Override
			public IRequestTarget decode(RequestParameters requestParameters) {
				try {
					return serveImage(getURI(requestParameters), 64);
				} catch (Exception e) {
					throw new WicketRuntimeException(e);
				}
			}
		});
		mount(new URIRequestTargetUrlCodingStrategy("/" + SCALED_IMAGE_PATH) {
			@Override
			public IRequestTarget decode(RequestParameters requestParameters) {
				try {
					return serveImage(getURI(requestParameters), 640);
				} catch (Exception e) {
					throw new WicketRuntimeException(e);
				}
			}
		});
		mount(new URIRequestTargetUrlCodingStrategy("/" + "ZipFolder") {
			public IRequestTarget decode(RequestParameters requestParameters) {
				String uri = getURI(requestParameters);
				uri = uri.substring(0, uri.length() - ".zip".length());
				org.apache.wicket.util.file.File file = new org.apache.wicket.util.file.File(new File(settings.getImageDirectoryRoot(), uri));
				return new ResourceStreamRequestTarget(new ZipResourceStream(file));
			}
		});
		mount(new URIRequestTargetUrlCodingStrategy("/" + FEED_PATH) {
			@Override
			public IRequestTarget decode(RequestParameters requestParameters) {
				try {
					final ByteArrayOutputStream pout = new ByteArrayOutputStream();
					feedGenerator.generate(settings.getImageDirectoryRoot(), getURI(requestParameters), pout);

					IResourceStream resource = new AbstractResourceStream() {
						public InputStream getInputStream() throws ResourceStreamNotFoundException {
							return new ByteArrayInputStream(pout.toByteArray());
						}

						public void close() throws IOException {
							pout.close();
						}

						@Override
						public long length() {
							return pout.size();
						}
					};
					return new ResourceStreamRequestTarget(resource);
				} catch (Exception e) {
					throw new WicketRuntimeException(e);
				}
			}
		});
	}

	IRequestTarget serveImage(String uri, int size) throws IOException, FileNotFoundException,
			ImageConversionException {
		File baseDirectory = new File(settings.getGeneratedImageDirectoryPath() + "/" + size);
		File file = new File(baseDirectory, uri);
		if (!file.getCanonicalPath().startsWith(baseDirectory.getCanonicalPath())) {
			// For security matters, we don't serve a file above the base
			// directory, can happen if user forges the URL
			throw new FileNotFoundException(file.toString());
		}
		log.debug("Serving " + file);
		File srcfile = new File(settings.getImageDirectoryRoot(), uri);
		if (!srcfile.exists()) {
			// Don't throw an exception, it means the URL is not correct
			// FIXME return a PageNotFoundRequestTarget
			return EmptyRequestTarget.getInstance();
		}
		if (!file.exists() || srcfile.lastModified() > file.lastModified()) {
			imageConverter.scaleImage(srcfile, file, size, null);
		}
		IResourceStream fileResource = new FileResourceStream(new org.apache.wicket.util.file.File(file)){
			@Override
			public String getContentType() {
				return "image/jpeg";
			}
		};
		return new ResourceStreamRequestTarget(fileResource);
	}

	public static PickwickApplication get() {
		System.out.println(Application.get().getClass().getName());
		return (PickwickApplication) Application.get();
	}
	
	/**
	 * return the current pickwickSession
	 * @return the current pickwickSession
	 */
	public PickwickSession getPickwickSession(){
		return ((PickwickSession)((WebRequestCycle)RequestCycle.get()).getSession());
	}
	
	@Override
	public String getConfigurationType() {
		return settings.getConfigurationType();
	}

	@Override
	protected Class<? extends WebPage> getSignInPageClass() {
		return PickwickLoginPage.class;
	}

	@Override
	protected Class<? extends AuthenticatedWebSession> getWebSessionClass() {
		return PickwickSession.class;
	}

	public Settings getSettings() {
		return settings;
	}
	
	@Override
	public IApplicationSettings getApplicationSettings() {
		IApplicationSettings appSettings = super.getApplicationSettings();
		appSettings.setPageExpiredErrorPage(SequencePage.class);
		return appSettings;
	}
}
