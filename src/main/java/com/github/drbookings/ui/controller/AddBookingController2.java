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

package com.github.drbookings.ui.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.drbookings.SettingsManager;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddBookingController2 implements Initializable {

    private final static Logger logger = LoggerFactory.getLogger(AddBookingController2.class);

    @FXML
    private Button buttonOK;

    @FXML
    private DatePicker datePickerCheckIn;

    @FXML
    private DatePicker datePickerCheckOut;

    @FXML
    private TextField textFieldSource;

    @FXML
    private TextField textFieldGrossEarnings;

    @FXML
    private ComboBox<String> comboBoxRoom;

    @FXML
    private TextField textFieldGuestName;

    @FXML
    private TextField textFieldServiceFees;

    @FXML
    private TextField textFieldServiceFeesPercent;

    @FXML
    private Label infoLabel;

    @FXML
    void handleButtonOK(final ActionEvent event) {

	final Stage stage = (Stage) buttonOK.getScene().getWindow();
	stage.close();

    }

    @FXML
    void handleButtonSetCheckInDate(final ActionEvent event) {
	final LocalDate date = datePickerCheckIn.getValue();
	// if (logger.isDebugEnabled()) {
	// logger.debug("Selected Check-in " + date);
	// }
	if (datePickerCheckOut.getValue() == null) {
	    datePickerCheckOut.setValue(date.plusDays(3));
	}
	updateInfoLabel();
    }

    @FXML
    void handleButtonSetCheckOutDate(final ActionEvent event) {
	final LocalDate date = datePickerCheckOut.getValue();
	if (datePickerCheckIn.getValue() == null) {
	    datePickerCheckIn.setValue(date.minusDays(3));
	}
	updateInfoLabel();
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
	final List<String> numbers = new ArrayList<>();
	for (int i = 1; i <= SettingsManager.getInstance().getNumberOfRooms(); i++) {
	    numbers.add("" + i);
	}
	comboBoxRoom.getItems().addAll(numbers);
	textFieldGrossEarnings.textProperty().addListener((observable, oldValue, newValue) -> updateInfoLabel());
	textFieldServiceFees.setText(String.format("%4.2f", SettingsManager.getInstance().getServiceFees()));
	textFieldServiceFeesPercent
		.setText(String.format("%4.2f", SettingsManager.getInstance().getServiceFeesPercent()));
	textFieldSource.textProperty().addListener(new ChangeListener<String>() {

	    @Override
	    public void changed(final ObservableValue<? extends String> observable, final String oldValue,
		    final String newValue) {
		if ("booking".equalsIgnoreCase(newValue)) {
		    textFieldServiceFeesPercent.setText("12");
		} else if ("airbnb".equalsIgnoreCase(newValue)) {
		    textFieldServiceFeesPercent.setText("0");
		}
	    }
	});
    }

    private void updateInfoLabel() {
	if (datePickerCheckIn.getValue() == null)
	    return;
	if (datePickerCheckOut.getValue() == null)
	    return;

    }

    private boolean validateInput() {

	return true;
    }

}
