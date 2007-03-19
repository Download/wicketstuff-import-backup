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
package wicket.contrib.push.timer;

import java.util.ArrayList;
import java.util.Iterator;

import wicket.contrib.push.PushEvent;

/**
 * Here we are simulating a bus with this event store
 * It is an Internal class (volontary package)
 * 
 * 
 * @author Vincent Demay
 */
class EventStore
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private transient ArrayList<EventStoreListener> listenerList = new ArrayList<EventStoreListener>();
	
	private final static EventStore eventStore = new EventStore();
	

	public void add(Object value)
	{
		//triggered listener
		Iterator<EventStoreListener> ite = listenerList.iterator(); 
		while(ite.hasNext()){
			EventStoreListener listener = (EventStoreListener) ite.next();
			listener.EventTriggered(((PushEvent)value).getChannel(), ((PushEvent)value).getData());
		}
	}
	
	public static EventStore get(){
		return eventStore;
	}
	
	/**
	 * Adds a listener to this list which will be notified whenever the list is modified
	 * @param listener the listener to add
	 */
	public void addEventStoreListener(EventStoreListener listener)
	{
		listenerList.add(listener);
	}
}
