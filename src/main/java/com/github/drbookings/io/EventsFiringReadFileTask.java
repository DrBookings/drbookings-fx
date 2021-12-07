package com.github.drbookings.io;

import java.nio.file.Path;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.drbookings.Booking;
import com.github.drbookings.CleaningBeanSer2;
import com.github.drbookings.event.booking.AddBookingEvent;
import com.github.drbookings.event.booking.AddExpenseEvent;
import com.github.drbookings.event.cleaning.AddCleaningEntryEvent;
import com.github.drbookings.event.ui.ScrollEvent;
import com.github.drbookings.ser.ExpenseSer;
import com.github.events1000.api.Events;

public class EventsFiringReadFileTask extends AbstractReadFileTask2 {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(EventsFiringReadFileTask.class);

    public EventsFiringReadFileTask(final Path file) {

	super(file);
    }

    @Override
    public void run() {

	super.run();
//	Events.getInstance().emit(new ScrollEvent(LocalDate.now()));
    }

    @Override
    protected void handleNewCleaning(final CleaningBeanSer2 c) {

	Events.getInstance().emit(new AddCleaningEntryEvent(c));
    }

    @Override
    protected void handleNewBooking(final Booking newBooking) {

	Events.getInstance().emit(new AddBookingEvent(newBooking));
    }

    @Override
    protected int getFileVersion() {

	return 2;
    }

    @Override
    protected void handleNewExpense(final ExpenseSer c) {
	Events.getInstance().emit(new AddExpenseEvent(c));

    }
}
