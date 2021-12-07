package com.github.drbookings.ui;

import java.time.LocalDate;
import java.util.concurrent.Callable;

import com.github.drbookings.ExpenseBean;
import com.github.drbookings.RoomFactory;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * The main data bean. For every day one. It holds a list of {@link RoomBean2
 * rooms} and {@link ExpenseBean expenses}.
 *
 * @author Alexander Kerner
 *
 */
public class DateBean2 {

    /**
     * A reference to {@code this}. Used for change-event listening.
     */
    private final ObjectProperty<DateBean2> self = new SimpleObjectProperty<>();

    /**
     * The room manifestations that exist on this date.
     */
    private final ListProperty<RoomBean2> rooms;

    private final ListProperty<ExpenseBean> expenses;

    private final LocalDate date;

    public DateBean2(final LocalDate date) {
	this.date = date;
	rooms = new SimpleListProperty<>(FXCollections.observableArrayList(RoomBean2.extractor()));
	rooms.addListener((ListChangeListener<? super RoomBean2>) (c) -> {
	    while (c.next()) {

	    }
	});
	expenses = new SimpleListProperty<>(FXCollections.observableArrayList(ExpenseBean.extractor()));
	bindProperties();
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = (prime * result) + ((date == null) ? 0 : date.hashCode());
	return result;
    }

    @Override
    public boolean equals(final Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (!(obj instanceof DateBean2))
	    return false;
	final DateBean2 other = (DateBean2) obj;
	if (date == null) {
	    if (other.date != null)
		return false;
	} else if (!date.equals(other.date))
	    return false;
	return true;
    }

    private void bindProperties() {
	selfProperty().bind(Bindings.createObjectBinding(update(), roomsProperty()));
	selfProperty().bind(Bindings.createObjectBinding(update(), expensesProperty()));
    }

    private Callable<DateBean2> update() {
	return () -> DateBean2.this;
    }

    private static final RoomFactory roomFactory = RoomFactory.getInstance();

    public RoomBean2 getRoom(final String roomName) {
	if ((roomName == null) || (roomName.length() < 1))
	    throw new IllegalArgumentException("No name given");
	for (final RoomBean2 rb : getRooms())
	    if (rb.getRoom().getName().equals(roomName))
		return rb;
	// auto-creation of room
	final RoomBean2 rb = new RoomBean2(getDate(), roomFactory.getOrCreateElement(roomName));
	roomsProperty().add(rb);
	return rb;
    }

    @Override
    public String toString() {
	return getDate().toString();
    }

    // Getter / Setter //

    public ObjectProperty<DateBean2> selfProperty() {
	return self;
    }

    public LocalDate getDate() {
	return date;
    }

    public DateBean2 getSelf() {
	return selfProperty().get();
    }

    public void setSelf(final DateBean2 self) {
	selfProperty().set(self);
    }

    public ListProperty<RoomBean2> roomsProperty() {
	return rooms;
    }

    public ObservableList<RoomBean2> getRooms() {
	return roomsProperty().get();
    }

    public void setRooms(final ObservableList<RoomBean2> rooms) {
	roomsProperty().set(rooms);
    }

    public ListProperty<ExpenseBean> expensesProperty() {
	return expenses;
    }

    public ObservableList<ExpenseBean> getExpenses() {
	return expensesProperty().get();
    }

    public void setExpenses(final ObservableList<ExpenseBean> expenses) {
	expensesProperty().set(expenses);
    }

}
