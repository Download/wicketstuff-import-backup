package wicket.contrib.gmap;

import java.util.List;

import wicket.Component;
import wicket.ajax.AjaxRequestTarget;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.list.Loop;
import wicket.model.AbstractReadOnlyModel;
import wicket.model.IModel;

/**
 * @author Iulian-Corneliu Costan
 */
class GMapContainer extends WebMarkupContainer
{
	private static final long serialVersionUID = 1L;
	private GMapComponent gMapComponent;
	private GMapComponentUpdate gMapComponentUpdate;
	private Loop gMarkerLoop;

	/**
	 * Construct.
	 * 
	 * @param gmap
	 */
	public GMapContainer(final GMap gmap)
	{
		super(ID);
		final List overlays = gmap.getOverlays();
		gMapComponent = new GMapComponent(gmap);
		gMapComponent.setOutputMarkupId(true);
		add(gMapComponent);
		gMapComponentUpdate = new GMapComponentUpdate(gmap);
		gMapComponentUpdate.setOutputMarkupId(true);
		add(gMapComponentUpdate);

		IModel model = new AbstractReadOnlyModel()
		{
			public Object getObject(Component component)
			{

				return new Integer(overlays.size());
			}
		};
		gMarkerLoop = new Loop("gmarkersLoop", model)
		{
			protected void populateItem(LoopItem item)
			{
				Overlay gmarker = (Overlay)overlays.get(item.getIteration());
				item.add(new GMarkerContainer((GMarker)gmarker));
			}
		};
		gMarkerLoop.setOutputMarkupId(true);
		add(gMarkerLoop);
	}


	public static final String ID = "gmapContainer";

	public GMapComponent getGMapComponent()
	{
		return gMapComponent;
	}


	public GMapComponentUpdate getGMapComponentUpdate()
	{
		return gMapComponentUpdate;
	}


	public Loop getGMarkerLoop()
	{
		return gMarkerLoop;
	}
	public void refresh(AjaxRequestTarget target)
	{
		target.addComponent(getGMarkerLoop(), "gMarkersLoop");
		target.addComponent(getGMapComponentUpdate(), "gmapComponentUpdate");
		// TOD split the init function
		// TODO call both initGMap and updateGMap
		target.appendJavascript(getGMapComponentUpdate().getFunctionName() + ";");
	}

}
