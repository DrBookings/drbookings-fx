package com.github.drbookings;

import java.util.concurrent.atomic.AtomicLong;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TestApp extends Application {

    public static void main(final String[] args) {
	launch(args);
    }

    private final AtomicLong counter = new AtomicLong();

    @Override
    public void start(final Stage primaryStage) {

	final VBox root = new VBox(5);
	root.setPadding(new Insets(10));
	root.setAlignment(Pos.CENTER);

	final TableView<String> tableView = new TableView<>();
	final TableColumn<String, String> column = new TableColumn<>("Text");
	column.setCellValueFactory(f -> new SimpleStringProperty(f.getValue()));

	tableView.getColumns().add(column);

	// Add some sample items to our TableView
	for (int i = 0; i < 100; i++) {
	    tableView.getItems().add("Item #" + counter.incrementAndGet());
	}

	final Button button = new Button("Add items");

	final TextArea t1 = new TextArea();

	button.setOnAction(e -> {
	    final long oldElement = counter.get();
	    // Add more elements
	    for (int i = 0; i < 10; i++) {
		tableView.getItems().add("Item #" + counter.incrementAndGet());
	    }
	    tableView.scrollTo("Item #" + oldElement);
	});

	root.getChildren().add(button);
	root.getChildren().add(t1);
	root.getChildren().add(tableView);

	// Show the Stage
	primaryStage.setWidth(300);
	primaryStage.setHeight(300);
	primaryStage.setScene(new Scene(root));
	primaryStage.show();
    }
}
