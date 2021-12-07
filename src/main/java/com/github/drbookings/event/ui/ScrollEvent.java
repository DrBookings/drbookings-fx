package com.github.drbookings.event.ui;

import java.time.LocalDate;

import com.github.events1000.api.AbstractTransportingEvent;
import com.github.events1000.api.AsynchronousEvent;
import com.github.events1000.api.EventTopic;
import com.github.events1000.impl.SimpleEventTopic;

public class ScrollEvent extends AbstractTransportingEvent<LocalDate> implements AsynchronousEvent {

    public static final EventTopic EVENT_TOPIC = new SimpleEventTopic("scroll-event");

    public ScrollEvent(final LocalDate data) {
	super(EVENT_TOPIC, data);
    }
}
