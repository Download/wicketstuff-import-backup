package wicket.contrib.examples.gmap;

import java.util.ArrayList;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import wicket.contrib.examples.WicketExamplePage;
import wicket.contrib.gmap.GMap2Panel;
import wicket.contrib.gmap.api.GControl;
import wicket.contrib.gmap.api.GLatLng;
import wicket.contrib.gmap.api.GMarker;

/**
 * Example HomePage for the wicket-contrib-gmap2 project
 */
public class HomePage extends WicketExamplePage
{

	private static final long serialVersionUID = 1L;
	
	private Label markerLabel;
	private Label zoomLabel;
	private Label center;

	public HomePage()
	{
		final ArrayList<GMarker> markers = new ArrayList<GMarker>();
		markers.add(new GMarker(new GLatLng(49f, 49f), "Home"));
		
		final GMap2Panel topPanel = new GMap2Panel("topPanel",
				LOCALHOST_8080_WICKET_CONTRIB_GMAP2_EXAMPLES_KEY, markers) {
			
			@Override
			public void onMoveEnd(AjaxRequestTarget target) {
				target.addComponent(zoomLabel);
			}
			
			@Override
			public void onClick(GLatLng gLatLng, AjaxRequestTarget target) {
				GMarker marker = new GMarker(gLatLng);
				markers.add(marker);
				markerLabel.getModel().setObject(marker);
				target.addComponent(markerLabel);
			}
		};
		topPanel.addControl(GControl.GLargeMapControl);
		topPanel.addControl(GControl.GMapTypeControl);
		add(topPanel);

		zoomLabel = new Label("glabel", new PropertyModel(topPanel, "zoomLevel"));
		zoomLabel.setOutputMarkupId(true);
		add(zoomLabel);

		markerLabel = new Label("markerLabel", new Model(null));
		markerLabel.add(new AjaxEventBehavior("onclick")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(AjaxRequestTarget target)
			{
				GLatLng point = ((GMarker)markerLabel.getModelObject()).getLagLng();
				
				GMarker marker = new GMarker(new GLatLng(point.getLat() * (0.9995 + Math.random() / 1000), point.getLng()
						* (0.9995 + Math.random() / 1000)));

				markers.add(marker);
				topPanel.updateControlsAndOverlays(target);
			}
		});
		add(markerLabel);
 		 
 		final Label zoomIn = new Label("zoomInLabel", "ZoomIn");
		zoomIn.add(topPanel.new ZoomIn("onclick"));		
 		add(zoomIn);
 	
 		final Label zoomOut = new Label("zoomOutLabel", "ZoomOut");
		zoomOut.add(topPanel.new ZoomOut("onclick"));
 		add(zoomOut);
		
		final GMap2Panel bottomPanel = new GMap2Panel("bottomPanel",
				LOCALHOST_8080_WICKET_CONTRIB_GMAP2_EXAMPLES_KEY) {
			
			@Override
			public void onMoveEnd(AjaxRequestTarget target) {
				target.addComponent(center);
			}
			
			@Override
			public void onClick(GLatLng point, AjaxRequestTarget target) {
				openInfoWindow(new HelloPanel(), point, target);
			}
		};
		bottomPanel.addControl(GControl.GSmallMapControl);
		add(bottomPanel);
		
		center = new Label("center", new PropertyModel(bottomPanel, "center"));
		center.setOutputMarkupId(true);
		add(center);
 		 
 		final Label n = new Label("n", "N");
 		n.add(bottomPanel.new PanDirection("onclick", 0, 1));
 		add(n);
 
 		final Label ne = new Label("ne", "NE");
		ne.add(bottomPanel.new PanDirection("onclick", -1, 1));
 		add(ne);
 
 		final Label e = new Label("e", "E");
		e.add(bottomPanel.new PanDirection("onclick", -1, 0));
 		add(e);
 
 		final Label se = new Label("se", "SE");
		se.add(bottomPanel.new PanDirection("onclick", -1, -1));
 		add(se);
 
 		final Label s = new Label("s", "S");
		s.add(bottomPanel.new PanDirection("onclick", 0, -1));
 		add(s);
 
 		final Label sw = new Label("sw", "SW");
		sw.add(bottomPanel.new PanDirection("onclick", 1, -1));
 		add(sw);
 
 		final Label w = new Label("w", "W");
		w.add(bottomPanel.new PanDirection("onclick", 1, 0));
 		add(w);
 
 		final Label nw = new Label("nw", "NW");
		nw.add(bottomPanel.new PanDirection("onclick", 1, 1));
 		add(nw);
 
		final Label infoWindow = new Label("infoWindow", "openInfoWindow");
		infoWindow.add(bottomPanel.createOpenInfoWindowBehavior(new GLatLng(44.0f, 44.0f),
				new HelloPanel(), "onclick"));
		add(infoWindow);
	}

	// pay attention at webapp deploy context, we need a different key for each
	// deploy context
	// check <a href="http://www.google.com/apis/maps/signup.html">Google Maps
	// API - Sign Up</a> for more info

	// key for http://localhost:8080/wicket-contrib-gmap-examples/
	@SuppressWarnings("unused")
	private static final String LOCALHOST_8080_WICKET_CONTRIB_GMAP_EXAMPLES_KEY = "ABQIAAAAf5yszl-6vzOSQ0g_Sk9hsxQwbIpmX_ZriduCDVKZPANEQcosVRSYYl2q0zAQNI9wY7N10hRcPVFbLw";

	// http://localhost:8080/wicket-contrib-gmap2-examples/gmap/
	private static final String LOCALHOST_8080_WICKET_CONTRIB_GMAP2_EXAMPLES_KEY = "ABQIAAAAf5yszl-6vzOSQ0g_Sk9hsxSRJOeFm910afBJASoNgKJoF-fSURRPODotP7LZxsDKHpLi_jvawkMyrQ";

	// key for http://localhost:8080/wicket-contrib-gmap, deploy context is
	// wicket-contrib-gmap
	@SuppressWarnings("unused")
	private static final String LOCALHOST_8080_WICKET_CONTRIB_GMAP_KEY = "ABQIAAAALjfJpigGWq5XvKwy7McLIxTDxbH1TVfo7w-iwzG2OxhXSIjJdhQTwgha-mCK8wiVEq4rgi9qvz8HYw";

	// key for http://localhost:8080/gmap, deploy context is gmap
	@SuppressWarnings("unused")
	private static final String GMAP_8080_KEY = "ABQIAAAALjfJpigGWq5XvKwy7McLIxTh_sjBSLCHIDZfjzu1cFb3Pz7MrRQLOeA7BMLtPnXOjHn46gG11m_VFg";

	// key for http://localhost/gmap
	@SuppressWarnings("unused")
	private static final String GMAP_DEFAULT_KEY = "ABQIAAAALjfJpigGWq5XvKwy7McLIxTIqKwA3nrz2BTziwZcGRDeDRNmMxS-FtSv7KGpE1A21EJiYSIibc-oEA";

	// key for http://www.wicket-library.com/wicket-examples/
	@SuppressWarnings("unused")
	private static final String WICKET_LIBRARY_KEY = "ABQIAAAALjfJpigGWq5XvKwy7McLIxQTV35WN9IbLCS5__wznwqtm2prcBQxH8xw59T_NZJ3NCsDSwdTwHTrhg";
}
