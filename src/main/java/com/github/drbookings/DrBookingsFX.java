
package com.github.drbookings;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.drbookings.io.EventsFiringReadFileTask;
import com.github.drbookings.persistence.InMemoryPersistenceProvider;
import com.github.drbookings.persistence.PersistenceService;
import com.github.events1000.api.Events;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The Dr.Bookings main {@link Application}.
 *
 * @author Alexander Kerner
 */
public class DrBookingsFX extends Application {

    private final static Logger logger = LoggerFactory.getLogger(DrBookingsFX.class);
    public static final ExecutorService exe = Executors.newSingleThreadExecutor();
    public static PersistenceService PERSISTENCE_SERVICE;

    public static void main(final String[] args) {

	PERSISTENCE_SERVICE = InMemoryPersistenceProvider.getInstance();
	launch(args);
    }

    public DrBookingsFX() {

    }

    @Override
    public void start(final Stage stage) throws Exception {

	if (logger.isInfoEnabled()) {
	    logger.info("Application version " + getClass().getPackage().getImplementationVersion());
	}
	readSettings();
	startGUI(stage);
	// TODO: move to InMemoryPersistenceProvider
	exe.submit(new EventsFiringReadFileTask(Paths.get("/home/alex/booking-data2.xml")));
    }

    private void readSettings() {

	SettingsManager.getInstance().setNumberOfRooms(4);
    }

    private void startGUI(final Stage stage) throws IOException {

	final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView2.fxml"));
	final Parent root = loader.load();
	final Scene scene = new Scene(root, 900, 800);
	String s = getClass().getPackage().getImplementationVersion();
	if (s == null) {
	    s = "dev version";
	}
	stage.setTitle("Dr.Bookings " + s);
	stage.setScene(scene);
	stage.setOnCloseRequest(new CloseRequestEventHandler());
	stage.show();
    }

    @Override
    public void stop() throws Exception {

	if (logger.isDebugEnabled()) {
	    logger.debug("Stopping application");
	}
	Events.getInstance().stop();
	exe.shutdown();
	super.stop();
    }
}
