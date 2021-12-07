package com.github.drbookings.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SerView {

    public SerView() {

    }

    public void show() {

	try {
	    final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ShowSerView.fxml"));
	    final Parent root = loader.load();
	    final Stage stage = new Stage();
	    final Scene scene = new Scene(root, 600, 400);
	    stage.setTitle("Serialization Data");
	    stage.setScene(scene);
	    final ShowSerController c = loader.getController();
	    stage.show();
	} catch (final Exception e) {
	    e.printStackTrace();
	}
    }
}
