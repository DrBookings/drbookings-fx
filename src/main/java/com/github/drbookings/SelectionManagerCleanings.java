package com.github.drbookings;

import java.util.Collection;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SelectionManagerCleanings {

    private static class InstanceHolder {

	private static final SelectionManagerCleanings instance = new SelectionManagerCleanings();
    }

    /**
     * Access from UI thread.
     *
     * @return the singleton instance
     */
    public static SelectionManagerCleanings getInstance() {

	return InstanceHolder.instance;
    }

    private final ListProperty<CleaningBeanSer2> cleanings = new SimpleListProperty<>(
	    FXCollections.observableArrayList());
    private final ListProperty<CleaningBeanSer2> selection = new SimpleListProperty<>(
	    FXCollections.observableArrayList());

    public ListProperty<CleaningBeanSer2> cleaningsProperty() {

	return cleanings;
    }

    public ListProperty<CleaningBeanSer2> selectionProperty() {

	return selection;
    }

    public ObservableList<CleaningBeanSer2> getCleanings() {

	return cleaningsProperty().get();
    }

    public ObservableList<CleaningBeanSer2> getSelection() {

	return selectionProperty().get();
    }

    public void setCleanings(final Collection<CleaningBeanSer2> bookings) {

	cleaningsProperty().setAll(bookings);
    }

    public void setSelection(final Collection<CleaningBeanSer2> selection) {

	selectionProperty().setAll(selection);
    }
}
