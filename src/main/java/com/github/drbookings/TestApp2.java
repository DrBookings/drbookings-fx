package com.github.drbookings;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestApp2 extends Application {

    public static void main(final String[] args) {
	launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws IOException {

	final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PaymentsBoxView.fxml"));
	final Parent root = loader.load();
	final Scene scene = new Scene(root, 900, 800);
	primaryStage.setScene(scene);
	primaryStage.setOnCloseRequest(new CloseRequestEventHandler());
	primaryStage.show();
    }
}
