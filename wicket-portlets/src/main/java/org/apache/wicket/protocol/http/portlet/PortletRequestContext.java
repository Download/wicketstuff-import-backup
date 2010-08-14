/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.protocol.http.portlet;

import javax.portlet.MimeResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.ResourceURL;
import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.RequestContext;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebResponse;

/**
 * FIXME javadoc
 * 
 * <p>
 * Porlet strategy for url rewriting, and providing access to the portlet namespace for markup Ids
 * and isolated session state. Portlets need to have their URLs encoded with special portal
 * information, namespace etc.
 * 
 * <p>
 * For url rewriting, only three methods are needed to support creating Portlet ActionURLs, Portlet
 * RenderURLs and Resource/Ajax URLs.
 * 
 * @see RequestContext
 * @author Ate Douma
 */
public class PortletRequestContext extends RequestContext
{
	private final WicketFilterPortletContext filterContext;
	private final PortletConfig portletConfig;
	private final PortletRequest portletRequest;
	private final PortletResponse portletResponse;

	/**
	 * Needed for JSR-168 support which only allows PortletURLs to be created by RenderResponse with
	 * JSR-286 PortletResponse can do that too.
	 */
	private final MimeResponse mimeResponse;
	/**
	 * FIXME javadoc
	 */
	private final IHeaderResponse headerResponse;
	/**
	 * Stores the parameter name by which to store the Wicket URL as a request parameter.
	 */
	private final String wicketUrlPortletParameter;
	/**
	 * Is this an Ajax request?
	 */
	private final boolean ajax;
	/**
	 * Is this an embedded request?
	 */
	private final boolean embedded;
	/**
	 * Is this a resource request?
	 */
	private final boolean resourceRequest;
	/**
	 * Stores the last Wicket URL encoding as a key value pair.
	 * 
	 * FIXME javadoc - is usage explained enough?
	 * 
	 * @see #saveLastEncodedUrl(String, String)
	 */
	private final String[] lastEncodedUrl = new String[2];

	public PortletRequestContext(WicketFilterPortletContext filterContext,
		ServletWebRequest request, WebResponse response)
	{
		this.filterContext = filterContext;
		HttpServletRequest servletRequest = request.getHttpServletRequest();
		portletConfig = (PortletConfig)servletRequest.getAttribute("javax.portlet.config");
		portletRequest = (PortletRequest)servletRequest.getAttribute("javax.portlet.request");
		portletResponse = (PortletResponse)servletRequest.getAttribute("javax.portlet.response");
		wicketUrlPortletParameter = (String)portletRequest.getAttribute(WicketPortlet.WICKET_URL_PORTLET_PARAMETER_ATTR);
		ajax = request.isAjax();
		resourceRequest = portletRequest.getAttribute(PortletRequest.LIFECYCLE_PHASE).equals(
			PortletRequest.RESOURCE_PHASE);
		mimeResponse = resourceRequest ||
			portletRequest.getAttribute(PortletRequest.LIFECYCLE_PHASE).equals(
				PortletRequest.RENDER_PHASE) ? (MimeResponse)portletResponse : null;
		embedded = !(ajax || resourceRequest);
		headerResponse = embedded ? newPortletHeaderResponse(response) : null;
	}

	protected IHeaderResponse newPortletHeaderResponse(Response response)
	{
		return new EmbeddedPortletHeaderResponse(response);
	}


	/**
	 * Used to retrieve the path last encoded as a portlet URL, used for internal Wicket processing
	 * when internal methods require a target URL e.g.
	 * {@link org.apache.wicket.markup.html.form.Form#getJsForInterfaceUrl(CharSequence)}.
	 * 
	 * @return the original Wicket URL
	 */
	public String getLastEncodedPath()
	{
		if (lastEncodedUrl != null)
		{
			return lastEncodedUrl[1];
		}
		return null;
	}

	/**
	 * @see #getLastEncodedPath()
	 * @param url
	 *            the portal encoded URL
	 * @return the original Wicket URL
	 */
	public String getLastEncodedPath(String url)
	{
		if (url != null && lastEncodedUrl != null && url.equals(lastEncodedUrl[0]))
		{
			return lastEncodedUrl[1];
		}
		return null;
	}

	/**
	 * Saves the key/value pairs so the original Wicket URL can be retrieved later if needed by
	 * Wicket, keyed by the encoded portal URL.
	 * 
	 * @see #getLastEncodedPath()
	 * @param url
	 *            the portal encoded URL
	 * @param path
	 *            the original Wicket URL
	 * @return
	 */
	protected String saveLastEncodedUrl(String url, String path)
	{
		lastEncodedUrl[0] = url;
		lastEncodedUrl[1] = path;
		return url;
	}

	/**
	 * FIXME javadoc
	 * 
	 * <p>
	 * Delegates to {@link #encodeActionURL(CharSequence, boolean)}, passing in forceRenderURL as
	 * false - FIXME why?
	 * 
	 * @param path
	 *            the URL to encode
	 * @see org.apache.wicket.RequestContext#encodeActionURL(java.lang.CharSequence)
	 * @see #encodeActionURL(CharSequence, boolean)
	 */
	@Override
	public CharSequence encodeActionURL(CharSequence path)
	{
		return encodeActionURL(path, false);
	}

	/**
	 * FIXME javadoc
	 * 
	 * <p>
	 * Encodes the given path into a portlet URL, saving the original Wicket URL against the
	 * {@link PortletURL} and in the class {@link #saveLastEncodedUrl(String, String)}.
	 * 
	 * @see #saveLastEncodedUrl(String, String)
	 * @param path
	 *            the path to encode
	 * @param forceRenderURL
	 *            FIXME param
	 * @return
	 */
	public CharSequence encodeActionURL(CharSequence path, boolean forceActionURL)
	{
		// TODO: NG - solve properly
		if ((!forceActionURL && resourceRequest) /* || RequestCycle.get().isUrlForNewWindowEncoding() */)
		{
			return encodeResourceURL(path);
		}
		if (path != null)
		{
			path = getQualifiedPath(path);
			if (mimeResponse != null)
			{
				PortletURL url = mimeResponse.createActionURL();
				url.setParameter(wicketUrlPortletParameter, path.toString());
				path = saveLastEncodedUrl(url.toString(), path.toString());
			}
		}
		return path;
	}

	/**
	 * @see org.apache.wicket.RequestContext#encodeMarkupId(java.lang.String)
	 * @return the markupId prefixed with the portlet's namespace.
	 */
	@Override
	public String encodeMarkupId(String markupId)
	{
		if (markupId != null)
		{
			markupId = getNamespace() + "_" + markupId;
		}
		return markupId;
	}

	/**
	 * FIXME javadoc
	 * 
	 * <p>
	 * Delegates to {@link #encodeRenderURL(CharSequence, boolean)}, passing in forceRenderURL as
	 * false - FIXME why?
	 * 
	 * @param path
	 *            the URL to encode
	 * @see org.apache.wicket.RequestContext#encodeRenderURL(java.lang.CharSequence)
	 * @see #encodeActionURL(CharSequence, boolean)
	 */
	@Override
	public CharSequence encodeRenderURL(CharSequence path)
	{
		return encodeRenderURL(path, false);
	}

	/**
	 * FIXME javadoc
	 * 
	 * <p>
	 * Encodes the given path into a portlet URL, saving the original Wicket URL against the
	 * {@link PortletURL} and in the class {@link #saveLastEncodedUrl(String, String)} and also
	 * includes the portlet view mode in the url link..
	 * 
	 * @see #saveLastEncodedUrl(String, String)
	 * @param path
	 *            the path to encode
	 * @param forceRenderURL
	 *            FIXME param
	 * @return
	 */
	public CharSequence encodeRenderURL(CharSequence path, boolean forceRenderURL)
	{
		// TODO (NG) Solve properly
		if ((!forceRenderURL && resourceRequest) /* || RequestCycle.get().isUrlForNewWindowEncoding() */)
		{
			return encodeResourceURL(path);
		}
		if (path != null)
		{
			path = getQualifiedPath(path);
			if (mimeResponse != null)
			{
				PortletURL url = mimeResponse.createRenderURL();
				url.setParameter(wicketUrlPortletParameter +
					portletRequest.getPortletMode().toString(), path.toString());
				path = saveLastEncodedUrl(url.toString(), path.toString());
			}
		}
		return path;
	}

	/**
	 * Override to encode the path to the resource with the portal specific URL (e.g. adds portlet
	 * window id etc...) and includes the actual Wicket URL as a URL parameter.
	 * 
	 * @see org.apache.wicket.RequestContext#encodeResourceURL(java.lang.CharSequence)
	 */
	@Override
	public CharSequence encodeResourceURL(CharSequence path)
	{
		if (path != null)
		{
			path = getQualifiedPath(path);
			if (mimeResponse != null)
			{
				try
				{
					ResourceURL url = mimeResponse.createResourceURL();
					String resourceID = path.toString();
					url.setResourceID(resourceID);
					path = saveLastEncodedUrl(url.toString(), resourceID);
				}
				catch (Exception e)
				{
					throw new RuntimeException(e);
				}
			}
		}
		return path;
	}

	/**
	 * Override to encode the path to the resource with the portal specific url (e.g. adds portlet
	 * window id etc...).
	 * 
	 * @see WicketFilterPortletContext#encodeWindowIdInPath(String, CharSequence)
	 * @see org.apache.wicket.RequestContext#encodeSharedResourceURL(java.lang.CharSequence)
	 */
	@Override
	public CharSequence encodeSharedResourceURL(CharSequence path)
	{
		if (path != null)
		{
			String url = filterContext.encodeWindowIdInPath(getPortletWindowId(), path);
			return saveLastEncodedUrl(url, url);
		}
		return null;
	}

	/**
	 * Override to return the special {@link EmbeddedPortletHeaderResponse}.
	 * 
	 * @see EmbeddedPortletHeaderResponse
	 * @see #newPortletHeaderResponse(Response)
	 * @see org.apache.wicket.RequestContext#getHeaderResponse()
	 */
	@Override
	public IHeaderResponse getHeaderResponse()
	{
		return headerResponse;
	}

	/**
	 * Should be prefixed or appended to elements, such as JavaScript variables or function names,
	 * to ensure they are unique in the context of the portal page.
	 * 
	 * @see javax.portlet.PortletResponse#getNamespace
	 * @see org.apache.wicket.RequestContext#getNamespace()
	 * @return the portlet's namespace, typically the portlet window id.
	 */
	@Override
	public CharSequence getNamespace()
	{
		return portletResponse != null ? portletResponse.getNamespace() : "";
	}

	/**
	 * @see org.apache.wicket.RequestContext#isPortletRequest()
	 */
	@Override
	public boolean isPortletRequest()
	{
		return true;
	}

	/**
	 * @return true if this is an embedded request.
	 */
	public boolean isEmbedded()
	{
		return embedded;
	}

	/**
	 * @param path
	 *            the relative path
	 * @return the fully qualified path which begins with the servlet context.
	 */
	protected String getQualifiedPath(CharSequence path)
	{
		HttpServletRequest request = ((ServletWebRequest)RequestCycle.get().getRequest()).getHttpServletRequest();
		return request.getServletPath() + "/" + path;
	}

	/**
	 * @see PortletWindowUtils#getPortletWindowId(javax.portlet.PortletSession)
	 * @return the portlet window id as assigned by the portlet container.
	 */
	protected String getPortletWindowId()
	{
		return portletRequest.getWindowID();
	}

	/**
	 * @see PortletConfig
	 * @return the portlet config
	 */
	public PortletConfig getPortletConfig()
	{
		return portletConfig;
	}

	/**
	 * @see PortletRequest
	 * @return the portlet request
	 */
	public PortletRequest getPortletRequest()
	{
		return portletRequest;
	}

	/**
	 * @see PortletResponse
	 * @return the portlet response
	 */
	public PortletResponse getPortletResponse()
	{
		return portletResponse;
	}

	/**
	 * @return is the current request an Ajax request?
	 */
	public boolean isAjax()
	{
		return ajax;
	}
}
