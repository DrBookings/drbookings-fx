package com.github.drbookings.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NewCleaningDialog {

    public NewCleaningDialog() {

    }

    public void show() {

	try {
	    final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddCleaningView2.fxml"));
	    final Parent root = loader.load();
	    final Stage stage = new Stage();
	    final Scene scene = new Scene(root, 330, 180);
	    stage.setTitle("Add new Cleaning");
	    stage.setScene(scene);
	    stage.show();
	} catch (final Exception e) {
	    e.printStackTrace();
	}

    }
}
