package wicket.contrib.gmap;

import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.list.Loop;

import java.util.List;

/**
 * @author Iulian-Corneliu Costan
 */
class GMapContainer extends WebMarkupContainer {

    public GMapContainer(final GMap gmap) {
        super(ID);
        final List<Overlay> overlays = gmap.getOverlays();

        add(new GMapComponent(gmap));
        add(new Loop("gmarkersLoop", overlays.size()) {
            protected void populateItem(LoopItem item) {
                Overlay gmarker = overlays.get(item.getIteration());
                item.add(new GMarkerContainer((GMarker) gmarker));
            }
        });
    }

    public static final String ID = "gmapContainer";
}
