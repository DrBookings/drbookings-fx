package com.github.drbookings.ui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.drbookings.Cleaning;
import com.github.drbookings.CleaningBeanFactory;
import com.github.drbookings.CleaningBeanSer2;
import com.github.drbookings.CleaningBeanSer2Factory2;
import com.github.drbookings.DrBookingsFX;
import com.github.drbookings.Selection;
import com.github.drbookings.persistence.InMemoryPersistenceProvider;
import com.github.drbookings.persistence.PersistenceService;
import com.github.events1000.api.Event;
import com.github.events1000.api.EventTopic;
import com.github.events1000.api.Events;
import com.github.events1000.listener.api.AsynchronousEventListener;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 * Controller for the CleaningDetailsView.
 * 
 * @author Alexander Kerner
 *
 */
public class CleaningDetailsController implements Initializable {

    private final static Logger logger = LoggerFactory.getLogger(CleaningDetailsController.class);

    private class SetFieldsTask extends Task<Void> {

	public SetFieldsTask(final String name, final String date) {
	    setOnSucceeded(e -> {
		nameOutput.setText(name);
		dateOutput.setText(date);
	    });
	}

	@Override
	protected Void call() throws Exception {
	    return null;
	}
    }

    private final AsynchronousEventListener selectionChangedListener = new AsynchronousEventListener() {

	@Override
	public void visit(final Event e) {
	    final SelectionEvent se = (SelectionEvent) e;
	    final List<Selection> selections = se.getData();
	    if (!selections.isEmpty()) {
		final Selection firstSelection = selections.get(0);
		final CleaningBeanSer2 cbs = persistenceService.getCleaningForRoomAndDate(firstSelection.getRoomName(),
			firstSelection.getDate());
		if (cbs != null) {
		    Platform.runLater(() -> setData(new CleaningBeanFactory().build(cbs)));
		} else {
		    setData(null);
		}
	    }
	}

	@Override
	public EventTopic getTopic() {
	    return SelectionEvent.EVENT_TOPIC;
	}
    };

    /**
     * Data for this view.
     */
    private CleaningBean data;

    // FXML //

    @FXML
    Pane root;

    @FXML
    Pane name;

    @FXML
    Pane bookingBefore;

    @FXML
    Pane bookingAfter;

    @FXML
    Pane date;

    private TextField nameOutput;

    private Label dateOutput;

    @FXML
    PaymentsBoxController paymentsBoxController;

    /**
     * The {@link PersistenceService} to translate an UI selection into an instance
     * of {@link Cleaning}
     */
    private final PersistenceService persistenceService;

    public CleaningDetailsController() {
	this.data = null;
	persistenceService = InMemoryPersistenceProvider.getInstance();
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {

	this.nameOutput = new TextField();
	nameOutput.setPromptText("Set new cleaning");
	this.dateOutput = new Label();
	this.name.getChildren().add(nameOutput);
	this.date.getChildren().add(dateOutput);

	Events.getInstance().registerListener(selectionChangedListener);

	// 'unfocus' all fields
	Platform.runLater(() -> root.requestFocus());

    }

    public void save(final ActionEvent e) {
	if (logger.isDebugEnabled()) {
	    logger.debug("saving all data");
	}
	// TODO: use events here
	persistenceService.addCleaning(new CleaningBeanSer2Factory2().build(getData()));
	updateUI();

    }

    public void setData(final CleaningBean data) {
	this.data = data;
	if (logger.isDebugEnabled()) {
	    logger.debug("Data now: " + data.hashCode());
	}

	paymentsBoxController.setData(data);
	updateUI();
    }

    private void updateUI() {
	if (getData() != null) {
	    DrBookingsFX.exe
		    .submit(new SetFieldsTask(getData().getElement().getName(), getData().getDate().toString()));
	} else {
	    DrBookingsFX.exe.submit(new SetFieldsTask(null, null));
	}
	paymentsBoxController.updateUI();
    }

    // Getter / Setter //

    /**
     *
     * @return
     */
    public CleaningBean getData() {
	return this.data;
    }

}
