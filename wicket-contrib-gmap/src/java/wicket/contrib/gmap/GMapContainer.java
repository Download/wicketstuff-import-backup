package wicket.contrib.gmap;

import java.util.List;

import wicket.Component;
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

	/**
	 * Construct.
	 * 
	 * @param gmap
	 */
	public GMapContainer(final GMap gmap)
	{
		super(ID);
		final List overlays = gmap.getOverlays();

		add(new GMapComponent(gmap));
		add(new GMapComponentUpdate(gmap));
		IModel model = new AbstractReadOnlyModel()
		{
			public Object getObject(Component component)
			{

				return new Integer(overlays.size());
			}
		};
		add(new Loop("gmarkersLoop", model)
		{
			protected void populateItem(LoopItem item)
			{
				Overlay gmarker = (Overlay)overlays.get(item.getIteration());
				item.add(new GMarkerContainer((GMarker)gmarker));
			}
		});
	}


	public static final String ID = "gmapContainer";
}
