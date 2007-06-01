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

	private GMapComponentUpdate gMapComponentUpdate;
	private Loop gMarkerLoop;
	private WebMarkupContainer listContainer;

	/**
	 * Construct.
	 * 
	 * @param gmap
	 */
	public GMapContainer(final GMap gmap)
	{
		super(ID);
		setOutputMarkupId(true);

		final List overlays = gmap.getOverlays();
		gMapComponentUpdate = new GMapComponentUpdate(gmap);
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
		listContainer = new WebMarkupContainer("loopContainer");
		listContainer.setOutputMarkupId(true);
		listContainer.add(gMarkerLoop);
		add(listContainer);
	}

	/**
	 * @param target
	 */
	public void refresh(AjaxRequestTarget target)
	{
		target.addComponent(this);
		target.appendJavascript(GMapComponentUpdate.REFRESH_FUNCTION + ";");
	}

	public static final String ID = "gmapContainer";
}
