package org.wicketstuff.push.cometd;

import java.util.Map;

import org.wicketstuff.push.PushEvent;

/**
 * Default abstract event for wicket.<br/>
 * This event will ping all component on client side to make them do 
 * a XmlHttpRequest<br/>
 * 
 * This Event can be seen as a proxy, it will request the client side 
 * to make a request to make it update
 *  
 * @author Vincent Demay
 *
 */
public class CometdEvent extends PushEvent{
	
	
	public CometdEvent(String channel) {
		super(channel);
	}

	public final Map<String, String> getData() {
		addData("proxy", "true");
		return super.getData();
	}

}
