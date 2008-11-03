package org.wicketstuff.calendarviews;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.wicketstuff.calendarviews.logic.DateMidnightIterator;
import org.wicketstuff.calendarviews.model.IEvent;
import org.wicketstuff.calendarviews.model.IEventProvider;
import org.wicketstuff.calendarviews.util.Comparators;

public abstract class BaseCalendarView extends Panel {

	private static final long serialVersionUID = 1L;
//	private static final Logger LOGGER = LoggerFactory.getLogger(BaseCalendarView.class);

	private final Date mStartDate;
	private final Date mEndDate;
	private IEventProvider mEventProvider;
	

	public BaseCalendarView(String id, Date startDate, Date endDate, IEventProvider eventProvider) {
		super(id);
		add(HeaderContributor.forCss(new ResourceReference(LargeView.class, "calendars.css")));
		if (startDate == null || endDate == null || eventProvider == null) {
			throw new IllegalArgumentException("no null parameters are allowed in this constructor");
		}
		mStartDate = startDate;
		mEndDate = endDate;
		mEventProvider = eventProvider;
	}

	/* Helper methods for subclasses */
	protected final Map<DateMidnight, List<IEvent>> convertToMapByDay(Collection<? extends IEvent> allEvents) {
		// TODO: this could probably use a much more efficient algorithm
		Map<DateMidnight, List<IEvent>> map = new HashMap<DateMidnight, List<IEvent>>();
		boolean includeEventInEachDayOfMap = includeEventInEachDayOfMap();
		for (IEvent event : allEvents) {
			DateMidnight start = new DateMidnight(event.getStartTime());
			DateMidnight end = start;
			if (event.getEndTime() != null && event.getEndTime().equals(event.getStartTime()) == false) {
				end = new DateMidnight(event.getEndTime());
			}
			if (end.isAfter(start) && includeEventInEachDayOfMap) {
				for (Iterator<DateMidnight> it = new DateMidnightIterator(start.toDateTime(), end.toDateTime()); it.hasNext(); ) {
					addEventToDate(map, it.next(), event);
				}
			} else {
				addEventToDate(map, start, event);
			}
		}
		// now sort
		for (List<IEvent> list : map.values()) {
			Collections.sort(list, Comparators.EVENT_START_DATE_ASC_COMPARATOR);
		}
		return map;
	}

	protected boolean includeEventInEachDayOfMap() {
		return true;
	}

	protected final void addEventToDate(Map<DateMidnight, List<IEvent>> map, DateMidnight date, IEvent event) {
		List<IEvent> events = map.get(date);
		if (events == null) {
			events = new ArrayList<IEvent>();
			map.put(date, events);
		}
		events.add(event);
	}

	protected IDataProvider<DateMidnight> createDaysDataProvider(final DateTime start, final DateTime end, final Period period) {
		return new IDataProvider<DateMidnight>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Iterator<? extends DateMidnight> iterator(final int first, int count) {
				return createDateMidnightIterator(start, end, first, count);
			}

			@Override
			public IModel<DateMidnight> model(DateMidnight object) {
				return new Model<DateMidnight>(object);
			}

			@Override
			public int size() {
				return period.getDays() + 1;
			}

			@Override
			public void detach() {
				// no-op
			}
			
			@Override
			public String toString() {
				return "BaseCalendarView#DaysDataProvider [size: " + size() + "]";
			}
		};
	}

	protected Iterator<? extends DateMidnight> createDateMidnightIterator(DateTime start, DateTime end, int first, int count) {
		return new DateMidnightIterator(start, end, first, count);
	}

	protected final int getNumberOfColumns() {
		return 7;
	}

	protected final int getLastDayOfWeek() {
		return 6;
	}

	protected final int getFirstDayOfWeek() {
		return 7;
	}
	

	/* Getters / Setters */
	public IEventProvider getEventProvider() {
		return mEventProvider;
	}
	public Date getStartDate() {
		return mStartDate;
	}
	public Date getEndDate() {
		return mEndDate;
	}
}
