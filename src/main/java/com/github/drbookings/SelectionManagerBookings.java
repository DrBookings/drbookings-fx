package com.github.drbookings;

import java.util.Collection;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SelectionManagerBookings {

    private static class InstanceHolder {

	private static final SelectionManagerBookings instance = new SelectionManagerBookings();
    }

    /**
     * Access from UI thread.
     *
     * @return the singleton instance
     */
    public static SelectionManagerBookings getInstance() {

	return InstanceHolder.instance;
    }

    private final ListProperty<Booking> bookings = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ListProperty<Booking> selection = new SimpleListProperty<>(FXCollections.observableArrayList());

    public ListProperty<Booking> bookingsProperty() {

	return bookings;
    }

    public ListProperty<Booking> selectionProperty() {

	return selection;
    }

    public ObservableList<Booking> getBookings() {

	return bookingsProperty().get();
    }

    public ObservableList<Booking> getSelection() {

	return selectionProperty().get();
    }

    public void setBookings(final Collection<Booking> bookings) {

	bookingsProperty().setAll(bookings);
    }

    public void setSelection(final Collection<Booking> selection) {

	selectionProperty().setAll(selection);
    }
}
