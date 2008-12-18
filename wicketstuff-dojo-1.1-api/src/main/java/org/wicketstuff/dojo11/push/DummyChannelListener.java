package org.wicketstuff.dojo11.push;

import java.util.Map;

/**
 * Convenience class for use with {@link JavaScriptChannelEvent}s.
 * Use this channellistener when you're creating javascript-only pushes to the client
 * using the events of {@link JavaScriptChannelEvent}. As no roundtrip will be made,
 * this class's onEvent method never gets called.
 *
 * @author Michael Sparer (msparer)
 */
public class DummyChannelListener implements IChannelListener {

	private static final long serialVersionUID = 1L;

	/**
	 * @see org.wicketstuff.dojo11.push.IChannelListener#onEvent(java.lang.String, java.util.Map, org.wicketstuff.dojo11.push.IChannelTarget)
	 */
	public void onEvent(final String channel, final Map<String, String> data, final IChannelTarget target) {
		// do nothing at all
	}

}
