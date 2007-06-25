/*
 * $Id$
 * $Revision$ $Date$
 * 
 * ==================================================================== Licensed
 * under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the
 * License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.contrib.gmap;

import java.util.Locale;

import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.markup.html.form.AjaxSubmitLink;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.HiddenField;
import wicket.markup.html.panel.Panel;
import wicket.model.PropertyModel;
import wicket.util.convert.IConverter;

/**
 * A reusable wicket component for <a href="http://maps.google.com">Google Maps</a>.
 * Becasue Maps API requires a different key for each deployment context you
 * have to either generate a new key (check <a
 * href="http://www.google.com/apis/maps/signup.html">Google Maps API - Sign Up</a>
 * for more info) or use already generated ones:
 * {@link GMapPanel.GMAP_DEFAULT_KEY} or {@link GMapPanel.GMAP_8080_KEY}
 * 
 * @see GMap
 * 
 * @author Iulian-Corneliu Costan, Nino Martinez Wael
 */
public class GMapPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	/**
	 * should listeners be attached to gmap instead, inorder to be able to ad event in the gmapcomponet based on wheter needed or not?
	 */
	private GMapClickListener clickListener;
	private GMapListener moveEndListener;
	private GMapContainer mapContainer;

	private AjaxSubmitLink ajaxClickNotifierSubmitLink;

	private AjaxSubmitLink ajaxSubmitLink;

	/**
	 * Creates a GMapPanel with width=400, height=300 and using default
	 * {GMapPanel.GMAP_DEFAULT_KEY} key. Make sure that deployment context of
	 * your application is <a
	 * href="http://localhost/gmap/">http://localhost/gmap/</a>
	 * 
	 * @param id
	 *            wicket component id
	 * @param gmap
	 *            a GMap instance
	 */
	public GMapPanel(String id, GMap gmap)
	{
		this(id, gmap, 400, 300, GMAP_DEFAULT_KEY);
	}

	/**
	 * Creates GMapPanel component using default
	 * {@link GMapPanel.GMAP_DEFAULT_KEY} key. Make sure that deployment context
	 * of your application is <a
	 * href="http://localhost/gmap">http://localhost/gmap/</a>
	 * 
	 * @param id
	 *            wicket component id
	 * @param gmap
	 *            a GMap instance
	 * @param width
	 *            map width in px
	 * @param height
	 *            map height in px
	 */
	public GMapPanel(String id, GMap gmap, int width, int height)
	{
		this(id, gmap, width, height, GMAP_DEFAULT_KEY);
	}

	/**
	 * Create GMapPanel.
	 * 
	 * @param id
	 *            wicket component id
	 * @param gMap
	 *            a GMap object
	 * @param width
	 *            map width in px
	 * @param height
	 *            map height in px
	 * @param gmapKey
	 *            key generated for your site, you can get it from <a
	 *            href="http://www.google.com/apis/maps/signup.html">here</a>
	 */
	public GMapPanel(String id, final GMap gMap, int width, int height, String gmapKey)
	{
		super(id);
		setOutputMarkupId(true);

		add(new GMapScript("script", GMAP_URL + gmapKey));
		add(new GMapInitializer(gMap));
		add(new GMapAjaxBehavior());
		mapContainer = new GMapContainer(gMap);
		add(mapContainer);
		add(new Map("map", width, height));

		// add form that contains center and zoomlevel
		Form gMapUpdatingForm = new Form("gmapUpdatingForm");
		gMapUpdatingForm.setOutputMarkupId(true);
		gMapUpdatingForm.add(new HiddenField("latitudeCenter", new PropertyModel(gMap.getCenter(),
				"latitude"))
		{
			public IConverter getConverter()
			{
				return getUSConverter();
			}
		});
		gMapUpdatingForm.add(new HiddenField("longitudeCenter", new PropertyModel(gMap.getCenter(),
				"longitude"))
		{
			public IConverter getConverter()
			{
				return getUSConverter();
			}
		});
		gMapUpdatingForm.add(new HiddenField("latitudeSW", new PropertyModel(gMap.getBounds()
				.getSouthWest(), "latitude"))
		{
			public IConverter getConverter()
			{
				return getUSConverter();
			}
		});
		gMapUpdatingForm.add(new HiddenField("longitudeSW", new PropertyModel(gMap.getBounds()
				.getSouthWest(), "longitude"))
		{
			public IConverter getConverter()
			{
				return getUSConverter();
			}
		});
		gMapUpdatingForm.add(new HiddenField("latitudeNE", new PropertyModel(gMap.getBounds()
				.getNorthEast(), "latitude"))
		{
			public IConverter getConverter()
			{
				return getUSConverter();
			}
		});
		gMapUpdatingForm.add(new HiddenField("longitudeNE", new PropertyModel(gMap.getBounds()
				.getNorthEast(), "longitude"))
		{
			public IConverter getConverter()
			{
				return getUSConverter();
			}
		});
		gMapUpdatingForm.add(new HiddenField("zoomLevel", new PropertyModel(gMap, "zoomLevel")));
		gMapUpdatingForm.add(new HiddenField("openMarkerInfoWindow", new PropertyModel(gMap, "openMarkerInfoWindow")));
		add(gMapUpdatingForm);


		ajaxSubmitLink = new AjaxSubmitLink("ajaxGMapUpdatingFormSubmit",
				gMapUpdatingForm)
		{
			private static final long serialVersionUID = 1L;

			protected void onSubmit(AjaxRequestTarget target, Form arg1)
			{
				// only notify dragEnd in dragEnd mode
				if (moveEndListener != null)
				{
					// notify dragEnd listener
					moveEndListener.onClick(target, gMap);

					// do refresh gmap panel
					refresh(target);
				}
			}
		};
		
		add(ajaxSubmitLink);

		// add click notifier form that contains center
		Form gmapClickNotifierForm = new Form("gMapClickNotifierForm");

		gmapClickNotifierForm.setOutputMarkupId(true);
		final GLatLng clickLatLng = new GLatLng(0f, 0f);
		gmapClickNotifierForm.add(new HiddenField("latitudeCenter", new PropertyModel(clickLatLng,
				"latitude"))
		{
			public IConverter getConverter()
			{
				return getUSConverter();
			}
		});
		gmapClickNotifierForm.add(new HiddenField("longitudeCenter", new PropertyModel(clickLatLng,
				"longitude"))
		{
			public IConverter getConverter()
			{
				return getUSConverter();
			}
		});
		add(gmapClickNotifierForm);
		ajaxClickNotifierSubmitLink = new AjaxSubmitLink(
				"ajaxGMapClickNotifierFormSubmit", gmapClickNotifierForm)
		{
			private static final long serialVersionUID = 1L;

			protected void onSubmit(AjaxRequestTarget target, Form arg1)
			{
				// fire up click event
				if (clickListener != null)
				{
					clickListener.onClick(target, new GLatLng(clickLatLng));
				}

				// do refresh gmap panel
				refresh(target);
			}
		};
		add(ajaxClickNotifierSubmitLink);
	}

	/**
	 * refresh the gmap panel on ajax call
	 * 
	 * @param target
	 *            the ajax request target instance
	 */
	public void refresh(AjaxRequestTarget target)
	{
		mapContainer.refresh(target);
	}

	/**
	 * @param clickListener
	 *            the listener for click eventF
	 */
	public void setGMapClickListener(GMapClickListener clickListener)
	{
		this.clickListener = clickListener;
	}

	private IConverter getUSConverter()
	{
		IConverter converter = getApplication().getApplicationSettings().getConverterFactory()
				.newConverter(Locale.US);
		return converter;
	}

	/** gmap url **/
	private static final String GMAP_URL = "http://maps.google.com/maps?file=api&v=2.x&key=";


	/**
	 * ALL localhost key could be removed, as of gmap version 2.81 keys are
	 * nolonger required for localhost.
	 */

	/**
	 * GMap key for root context <a href="http://localhost/">http://localhost</a>
	 */
	public static final String LOCALHOST_KEY = "ABQIAAAALjfJpigGWq5XvKwy7McLIxT2yXp_ZAY8_ufC3CFXhHIE1NvwkxRTqfd1PEwgWtnBwhFCBpkPDmu-nA";

	/**
	 * GMap key for <a
	 * href="http://localhost:8080/gmap">http://localhost:8080/gmap</a>
	 */
	public static final String GMAP_8080_KEY = "ABQIAAAALjfJpigGWq5XvKwy7McLIxTh_sjBSLCHIDZfjzu1cFb3Pz7MrRQLOeA7BMLtPnXOjHn46gG11m_VFg";

	/**
	 * Defaul GMap key for <a href="http://localhost/gmap">http://localhost/gmap</a>
	 */
	public static final String GMAP_DEFAULT_KEY = "ABQIAAAALjfJpigGWq5XvKwy7McLIxTIqKwA3nrz2BTziwZcGRDeDRNmMxS-FtSv7KGpE1A21EJiYSIibc-oEA";


	public GMapListener getMoveEndListener()
	{
		return moveEndListener;
	}

	public void setMoveEndListener(GMapListener dragEndListener)
	{
		this.moveEndListener = dragEndListener;
	}

	public GMapClickListener getClickListener()
	{
		return clickListener;
	}

	public void setClickListener(GMapClickListener clickListener)
	{
		this.clickListener = clickListener;
	}

	public AjaxSubmitLink getAjaxClickNotifierSubmitLink()
	{
		return ajaxClickNotifierSubmitLink;
	}

	public AjaxSubmitLink getAjaxSubmitLink()
	{
		return ajaxSubmitLink;
	}

}
