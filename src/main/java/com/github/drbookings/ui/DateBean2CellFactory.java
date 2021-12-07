/*
 * DrBookings
 * Copyright (C) 2016 - 2018 Alexander Kerner
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public
 * License along with this program. If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 */
package com.github.drbookings.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.drbookings.BookingEntry2;
import com.github.drbookings.BookingEntryPair2;
import com.github.drbookings.CleaningBeanFactory;
import com.github.drbookings.CleaningBeanSer2;
import com.github.drbookings.persistence.PersistenceService;
import com.github.drbookings.persistence.InMemoryPersistenceProvider;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class DateBean2CellFactory
	implements Callback<TableColumn<DateBean2, DateBean2>, TableCell<DateBean2, DateBean2>> {

    private static final Logger logger = LoggerFactory.getLogger(DateBean2CellFactory.class);
    private static final PersistenceService persistenceService = InMemoryPersistenceProvider.getInstance();
    private static final CleaningBeanFactory cleaningBeanFactory = new CleaningBeanFactory();
    private final String id;

    public DateBean2CellFactory(final String id) {

	this.id = id;
    }

    private Node buildCellContent(final DateBean2 dateBean) {

	final VBox parent = new VBox();
	parent.setPadding(new Insets(0, 0, 0, 0));

	final BookingEntryPair2 booking = persistenceService.getBookingForRoomAndDate(id, dateBean.getDate());
	final CleaningBeanSer2 cleaning = persistenceService.getCleaningForRoomAndDate(id, dateBean.getDate());

	parent.getChildren().add(buildCheckOutNode(booking == null ? null : booking.getCheckOut()));
	parent.getChildren().add(buildCleaningOrStayOverNode(booking == null ? null : booking.getStay(), cleaning));
	parent.getChildren().add(buildCheckInNode(booking == null ? null : booking.getCheckIn()));

	if ((booking != null)) {
	    if (booking.hasStay()) {
		parent.getStyleClass()
			.add(Styles2.getBackgroundStyleSource(booking.getStay().getBookingOrigin().getName()));
	    }
	}
	return parent;
    }

    private static Label getNewLabel(final String text) {

	final Label l = new Label(text);
	l.setMaxWidth(Double.POSITIVE_INFINITY);
	// l.setPadding(new Insets(2));
	l.setAlignment(Pos.CENTER);
	return l;
    }

    private Node buildCheckOutNode(final BookingEntry2 booking) {

	if ((booking != null) && booking.isCheckOut()) {
	    final Label l = getNewLabel(booking.getGuest().getName());
	    l.getStyleClass().add("check-out");
	    return l;
	}
	return getEmptyNode();
    }

    private Node getEmptyNode() {

	return new Label();
    }

    private Node buildCleaningOrStayOverNode(final BookingEntry2 booking, final CleaningBeanSer2 cleaningSer) {

	if ((booking != null) && booking.isStayOver()) {
	    if (cleaningSer != null) {
		if (logger.isWarnEnabled()) {
		    logger.warn("Cleaning and stayover at the same time, cleaning not rendered");
		}
	    }
	    final Label l = getNewLabel(booking.getGuest().getName());
	    l.getStyleClass().add("entry-stay");
	    return l;
	}
	if (cleaningSer != null) {
	    final Label l = getNewLabel(cleaningSer.name);
	    l.getStyleClass().add("cleaning");
	    return l;
	}
	return getEmptyNode();
    }

    private Node buildCheckInNode(final BookingEntry2 booking) {

	if ((booking != null) && booking.isCheckIn()) {
	    final Label l = getNewLabel(booking.getGuest().getName());
	    l.getStyleClass().add("check-in");
	    return l;
	}
	return getEmptyNode();
    }

    @Override
    public TableCell<DateBean2, DateBean2> call(final TableColumn<DateBean2, DateBean2> param) {

	return new TableCell<DateBean2, DateBean2>() {

	    @Override
	    protected void updateItem(final DateBean2 item, final boolean empty) {

		super.updateItem(item, empty);
		if (empty || (item == null)) {
		    setText("");
		    setStyle("");
		} else {
		    graphicProperty().bind(
			    Bindings.createObjectBinding(() -> buildCellContent(empty, item), item.selfProperty()));
		    // setGraphic(buildCellContentFast(item.getRoom(id)));
		    setStyle("-fx-padding: 0 0 0 0;");
		}
	    }
	};
    }

    protected Node buildCellContent(final boolean empty, final DateBean2 item) {
	if (empty || (item == null))
	    return null;
	return buildCellContent(item);
    }
}
