package com.github.drbookings.ui;

import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import com.github.drbookings.Booking;
import com.github.drbookings.CleaningBeanSer2Factory2;
import com.github.drbookings.ExpenseBean;
import com.github.drbookings.ExpenseSerFactory;
import com.github.drbookings.io.ToXMLWriter;
import com.github.drbookings.ser.BookingBeanSer2Factory;
import com.github.drbookings.ser.DataStoreCoreSer2;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class SaveService extends Service<Void> {

    public static Collection<Booking> getBookings(final Collection<DateBean2> items) {
	return items.stream().filter(e -> e.getRooms() != null).flatMap(e -> e.getRooms().stream())
		.filter(e -> e.getBooking() != null).flatMap(e -> e.getBooking().toList().stream())
		.map(e -> e.getParent()).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public static Collection<CleaningBean> getCleanings(final Collection<DateBean2> items) {
	return items.stream().filter(e -> e.getRooms() != null).flatMap(e -> e.getRooms().stream())
		.filter(e -> e.getCleaningEntry() != null).map(e -> e.getCleaningEntry())
		.collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public static Collection<ExpenseBean> getExpenses(final Collection<DateBean2> items) {
	return items.stream().flatMap(e -> e.getExpenses().stream())
		.collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private final Collection<DateBean2> items;

    private final Path outPath;

    public SaveService(final Collection<DateBean2> items, final Path outPath) {
	this.items = items;
	this.outPath = outPath;
    }

    @Override
    protected Task<Void> createTask() {
	return new Task<Void>() {

	    @Override
	    protected Void call() throws Exception {

		final DataStoreCoreSer2 data = new DataStoreCoreSer2();
		data.setBookingSer(new BookingBeanSer2Factory().build(getBookings()));
		data.setCleaningSer(new CleaningBeanSer2Factory2().build(getCleanings(items)));
		data.setExpensesSer(ExpenseSerFactory.build(getExpenses(items)));
		new ToXMLWriter().write(data, outPath);
		return null;
	    }

	    private Collection<Booking> getBookings() {
		return SaveService.getBookings(items);
	    }
	};
    }
}
