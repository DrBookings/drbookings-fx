package com.github.drbookings.ui;

import java.util.List;

import com.github.drbookings.Selection;
import com.github.events1000.api.AbstractTransportingEvent;
import com.github.events1000.api.AsynchronousEvent;
import com.github.events1000.api.EventTopic;
import com.github.events1000.impl.SimpleEventTopic;

public class SelectionEvent extends AbstractTransportingEvent<List<Selection>> implements AsynchronousEvent {

    public static final EventTopic EVENT_TOPIC = new SimpleEventTopic("selection-event");

    public SelectionEvent(final List<Selection> selections) {
	super(EVENT_TOPIC, selections);
    }

}
