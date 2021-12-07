/*
 * DrBookings
 *
 * Copyright (C) 2016 - 2018 Alexander Kerner
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 */

package com.github.drbookings.ui;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.drbookings.BookingPairBean;
import com.github.drbookings.DateEntryImpl;
import com.github.drbookings.Room;

import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Callback;

public class RoomBean2 extends DateEntryImpl<Room> {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(RoomBean2.class);

    public static Callback<RoomBean2, Observable[]> extractor() {
	return b -> new Observable[] { b.cleaningEntryProperty(), b.bookingProperty() };
    }

    @Deprecated
    private final ObjectProperty<CleaningBean> cleaningEntry;

    private final ObjectProperty<BookingPairBean> booking;

    public RoomBean2(final LocalDate date, final Room element) {
	super(date, element);
	cleaningEntry = new SimpleObjectProperty<>();
	booking = new SimpleObjectProperty<>();
    }

    public Room getRoom() {
	return getElement();
    }

    public ObjectProperty<CleaningBean> cleaningEntryProperty() {
	return cleaningEntry;
    }

    public CleaningBean getCleaningEntry() {
	return cleaningEntryProperty().get();
    }

    public void setCleaningEntry(final CleaningBean cleaningEntry) {
	cleaningEntryProperty().set(cleaningEntry);
    }

    public ObjectProperty<BookingPairBean> bookingProperty() {
	return booking;
    }

    public BookingPairBean getBooking() {
	return bookingProperty().get();
    }

    public void setBooking(final BookingPairBean booking) {
	bookingProperty().set(booking);
    }

}
