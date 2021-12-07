package com.github.drbookings.ui.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.drbookings.CleaningBeanSer2;
import com.github.drbookings.SettingsManager;
import com.github.drbookings.event.cleaning.AddCleaningEntryEvent;
import com.github.events1000.api.Events;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddCleaningController2 implements Initializable {

	@SuppressWarnings("unused")
	private final static Logger logger = LoggerFactory.getLogger(AddCleaningController2.class);
	@FXML
	Button buttonOK;
	@FXML
	ComboBox<String> comboBoxRoom;
	@FXML
	DatePicker date;
	@FXML
	TextField name;
	@FXML
	CheckBox tax;

	public AddCleaningController2() {

	}

	/**
	 * Adds a new cleaning entry to the cleaning data.
	 *
	 * @see UICleaningData
	 */
	void addNewEntry() {

		final CleaningBeanSer2 data = buildCleaningBeanSer();
		Events.getInstance().emit(new AddCleaningEntryEvent(data));
	}

	public static String removePrefix(final String str) {

		return StringUtils.removeStart(str, SettingsManager.getInstance().getRoomNamePrefix());
	}

	private CleaningBeanSer2 buildCleaningBeanSer() {

		final CleaningBeanSer2 cb = new CleaningBeanSer2();
		cb.tax = tax.isSelected();
		cb.date = date.getValue();
		cb.name = name.getText();
		cb.room = removePrefix(comboBoxRoom.getSelectionModel().getSelectedItem());
		return cb;
	}

	/**
	 * Binds the disable property of {@code buttonOK} to be only enabled if input is
	 * valid.
	 */
	private void createBindings() {

		buttonOK.disableProperty().bind(Bindings.isNull(date.valueProperty()).or(Bindings.length(name.textProperty()).isEqualTo(0)).or(Bindings.isNull(comboBoxRoom.getSelectionModel().selectedItemProperty())));
	}

	/**
	 * Fills the combox with room names. Room names are taken from the
	 * {@link SettingsManager}.
	 */
	private void fillRoomComboBox() {

		final List<String> numbers = new ArrayList<>();
		final String roomPrefix = SettingsManager.getInstance().getRoomNamePrefix();
		for(int i = 1; i <= SettingsManager.getInstance().getNumberOfRooms(); i++) {
			numbers.add(roomPrefix + i);
		}
		comboBoxRoom.getItems().addAll(numbers);
	}

	public DatePicker getDate() {

		return date;
	}

	/**
	 * Adds a new entry and closes this stage.
	 */
	@FXML
	void handleButtonOK(final ActionEvent event) {

		addNewEntry();
		final Stage stage = (Stage)buttonOK.getScene().getWindow();
		stage.close();
	}

	/**
	 * Fills the combobox and creates bindings.
	 */
	@Override
	public void initialize(final URL location, final ResourceBundle resources) {

		fillRoomComboBox();
		createBindings();
	}

	public void setDate(final DatePicker date) {

		this.date = date;
	}
}
