package com.github.drbookings.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NewBookingDialog {

    public NewBookingDialog() {

    }

    public void show() {

	try {
	    final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddBookingView2.fxml"));
	    final Parent root = loader.load();
	    final Stage stage = new Stage();
	    final Scene scene = new Scene(root, 400, 400);
	    stage.setTitle("Add new Booking");
	    stage.setScene(scene);
	    stage.show();
	} catch (final Exception e) {
	    e.printStackTrace();
	}

    }
}
