package com.github.drbookings.ui;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.drbookings.Booking;
import com.github.drbookings.LocalDates;
import com.github.drbookings.Payment;
import com.github.drbookings.PaymentImpl;
import com.github.drbookings.Payments2;
import com.github.drbookings.Scripting;
import com.github.drbookings.SelectionManagerBookings;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.StageStyle;

public class BookingDetailsController implements Initializable {

    @SuppressWarnings("unused")
    private final static Logger logger = LoggerFactory.getLogger(BookingDetailsController.class);

    private static void clearBackgroundStyle(final Pane pane) {
	pane.getStyleClass().removeAll(Styles2.getBackgroundStyleSource("airbnb"));
	pane.getStyleClass().removeAll(Styles2.getBackgroundStyleSource("booking"));
	pane.getStyleClass().removeAll(Styles2.getBackgroundStyleSource(""));

    }

    private static void setBackgroundStyle(final Pane pane, final Booking b) {
	clearBackgroundStyle(pane);
	// pane.getStyleClass().add(Styles2.getBackgroundStyleSource(b.getBookingOrigin().getName()));
    }

    @FXML
    Label amountReceived;

    @FXML
    Pane dates;

    @FXML
    Label grossPayments;

    @FXML
    TextField grossPaymentsExpression;

    @FXML
    Pane name;

    @FXML
    Pane nights;

    @FXML
    Pane root;

    @FXML
    CheckBox welcomeMailSent;

    private final ListChangeListener<Booking> listener = c -> {

	List<? extends Booking> list = null;
	while (c.next()) {
	    list = c.getList();
	}
	updateView(list);
    };

    final ChangeListener<? super String> grossPaymentsInputListener = (e, o, n) -> updateGrossPayments2(n);

    final ChangeListener<? super Boolean> welcomeMailSentListener = (e, o, n) -> updateWelcomeMailSent(n);

    private Booking selectedBooking;

    public BookingDetailsController() {

    }

    public void addPayment() {
	final TextInputDialog dialog = new TextInputDialog();
	dialog.setTitle(null);
	dialog.setHeaderText(null);
	dialog.setContentText("Amount received:");
	dialog.initStyle(StageStyle.UTILITY);
	dialog.showAndWait().ifPresent(payment -> addPayment(payment));
    }

    private void addPayment(final String payment) {
	final Payment p = new PaymentImpl(LocalDate.now(), payment);
	selectedBooking.addPayment(p);
	updateAmountReceived();
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
	SelectionManagerBookings.getInstance().selectionProperty().addListener(listener);
	updateView(SelectionManagerBookings.getInstance().getSelection());
	// grossPaymentsExpression.setStyle("-fx-text-box-border: transparent;");
	Platform.runLater(() -> root.requestFocus());

    }

    private void setDates() {
	final TextFlow checkIn = LocalDates.getDateText(selectedBooking.getCheckInDate());
	// checkIn.getStyleClass().add("first-line");
	// dates.getStyleClass().add("first-line");
	final TextFlow checkOut = LocalDates.getDateText(selectedBooking.getCheckOutDate());
	final TextFlow tf = new TextFlow();
	tf.getChildren().addAll(checkIn, new Text("\n"), checkOut);
	dates.getChildren().setAll(tf);
	setBackgroundStyle(dates, selectedBooking);

    }

    private void setName() {
	final TextFlow tf = new TextFlow();
	final Text t0 = new Text(selectedBooking.getGuest().getName());
	final Text t1 = new Text(selectedBooking.getBookingOrigin().getName());
	tf.getChildren().addAll(t0, new Text("\n"), t1);
	name.getChildren().setAll(tf);
	setBackgroundStyle(name, selectedBooking);

    }

    private void setNights() {
	final TextFlow tf = new TextFlow();
	tf.getChildren().addAll(new Text(selectedBooking.getNumberOfNights() + " nights"));
	nights.getChildren().setAll(tf);
	setBackgroundStyle(nights, selectedBooking);
    }

    private void updateView(final List<? extends Booking> list) {
	if ((list == null) || list.isEmpty()) {
	    name.getChildren().clear();
	    dates.getChildren().clear();
	    nights.getChildren().clear();
	    clearBackgroundStyle(name);
	    clearBackgroundStyle(dates);
	    clearBackgroundStyle(nights);
	    grossPaymentsExpression.setEditable(false);
	    grossPaymentsExpression.setText(null);
	    grossPayments.setText(null);
	    amountReceived.setText(null);
	    grossPaymentsExpression.textProperty().removeListener(grossPaymentsInputListener);
	    welcomeMailSent.selectedProperty().removeListener(welcomeMailSentListener);
	    welcomeMailSent.setSelected(false);
	    selectedBooking = null;
	} else {
	    selectedBooking = list.get(list.size() - 1);
	    grossPaymentsExpression.textProperty().addListener(grossPaymentsInputListener);
	    welcomeMailSent.selectedProperty().addListener(welcomeMailSentListener);
	    setDates();
	    setName();
	    setNights();
	    grossPaymentsExpression.setEditable(true);
	    grossPaymentsExpression.setText(selectedBooking.getGrossPaymentExpression());
	    updateGrossPayments();
	    updateAmountReceived();
	    welcomeMailSent.setSelected(selectedBooking.isWelcomeMailSend());
	}
    }

    private double parseGrossPayments() {

	return Scripting.evaluateExpression(grossPaymentsExpression.getText());

    }

    private void updateGrossPayments() {
	grossPayments.setText(String.format("%6.2f €", parseGrossPayments()));
    }

    private void updateGrossPayments2(final String newValue) {
	if (newValue != null) {
	    selectedBooking.setGrossPaymentExpression(newValue.trim());
	}
	updateGrossPayments();
    }

    private void updateAmountReceived() {
	amountReceived.setText(
		String.format("%6.2f €", Payments2.getSum(selectedBooking.getPayments()).getNumber().doubleValue()));
    }

    private void updateWelcomeMailSent(final Boolean sent) {
	if (sent != null) {
	    selectedBooking.setWelcomeMailSend(sent);
	}
    }
}
