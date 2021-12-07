package com.github.drbookings;

import com.github.drbookings.ui.DateBean2;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SelectionManagerDateBean {

    private static class InstanceHolder {

	private static final SelectionManagerDateBean instance = new SelectionManagerDateBean();
    }

    /**
     * Access from UI thread.
     *
     * @return the singleton instance
     */
    public static SelectionManagerDateBean getInstance() {

	return InstanceHolder.instance;
    }

    private final ListProperty<DateBean2> elements = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ListProperty<DateBean2> selection = new SimpleListProperty<>(FXCollections.observableArrayList());

    public ListProperty<DateBean2> elementsProperty() {
	return elements;
    }

    public ObservableList<DateBean2> getElements() {
	return elementsProperty().get();
    }

    public void setElements(final ObservableList<DateBean2> elements) {
	elementsProperty().set(elements);
    }

    public ListProperty<DateBean2> selectionProperty() {
	return selection;
    }

    public ObservableList<DateBean2> getSelection() {
	return selectionProperty().get();
    }

    public void setSelection(final ObservableList<DateBean2> selection) {
	selectionProperty().set(selection);
    }

}
