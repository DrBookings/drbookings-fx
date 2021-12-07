package com.github.drbookings.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CleaningDetailsView {

    public CleaningDetailsView() {

    }

    public void show() {

	try {
	    final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CleaningDetailsView.fxml"));
	    final Parent root = loader.load();
	    final Stage stage = new Stage();
	    final Scene scene = new Scene(root, 400, 400);
	    stage.setTitle("Cleaning Details");
	    stage.setScene(scene);
	    final CleaningDetailsController c = loader.getController();
	    stage.show();
	} catch (final Exception e) {
	    e.printStackTrace();
	}
    }
}
