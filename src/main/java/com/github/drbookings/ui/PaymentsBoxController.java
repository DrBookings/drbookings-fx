package com.github.drbookings.ui;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.drbookings.DrBookingsFX;
import com.github.drbookings.Payment;
import com.github.drbookings.PaymentImpl;
import com.github.drbookings.Payments2;
import com.github.drbookings.PaymentsProviderBean;
import com.github.drbookings.Scripting;

import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controller for the payments view. It is embedded for example in the cleanings
 * details view or the bookings details view to display payment information.
 *
 * @author Alexander Kerner
 *
 */
public class PaymentsBoxController implements Initializable {

    private final static Logger logger = LoggerFactory.getLogger(PaymentsBoxController.class);

    /**
     * A {@link Task} to calculate a new payment in a background thread. When done,
     * triggers a new {@link UpdateCompletedPaymentsTask}.
     *
     * @author Alexander Kerner
     *
     */
    private class AddPaymentTask extends Task<Payment> {

	private final String amount;

	public AddPaymentTask(final String amount) {
	    this.amount = amount;
	    setOnSucceeded(e -> {
		getData().getPayments().add(getValue());
	    });
	}

	@Override
	protected Payment call() throws Exception {
	    return new PaymentImpl(Scripting.evaluateExpression(amount));
	}
    }

    @FXML
    TextField expectedExpression;

    @FXML
    Label expected;

    @FXML
    TextField completedOutput;

    /**
     * Data for this view.
     */
    private PaymentsProviderBean data;

    public PaymentsBoxController() {
	this.data = null;

    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {

    }

    public void addPayment(final ActionEvent e) {
	final Optional<String> result = new NewPaymentDialog(getData().getExpectedExpression().get()).showAndWait();
	result.ifPresent(amount -> {
	    DrBookingsFX.exe.submit(new AddPaymentTask(amount));
	});

    }

    public void save(final ActionEvent e) {

	// saving is done in parent controller
    }

    /**
     * Sets a new {@link PaymentsProviderBean} that will be visualized by this
     * controller/ view and triggers an UI refresh.
     *
     *
     *
     * @param data
     *            the new {@link PaymentsProviderBean}
     */
    public void setData(final PaymentsProviderBean data) {
	expectedExpression.textProperty().unbind();
	completedOutput.textProperty().unbind();
	expected.textProperty().unbind();
	this.data = data;
	if (data != null) {
	    expectedExpression.textProperty().bindBidirectional(data.getExpectedExpression());
	    completedOutput.textProperty().bind(Bindings.createStringBinding(
		    () -> getData() == null ? "0" : Payments2.getSum(getData().getPayments()).getNumber().toString(),
		    getData().paymentsProperty()));
	    expected.textProperty().bind(Bindings.createStringBinding(
		    () -> String.format("%3.2f", Scripting.evaluateExpression(getData().getExpectedExpression().get())),
		    getData().getExpectedExpression()));
	}
	if (logger.isDebugEnabled()) {
	    logger.debug("Data now: " + data.hashCode());
	}
	updateUI();
    }

    /**
     * Starts tasks to update the UI.
     */
    void updateUI() {
	// DrBookingsFX.exe.submit(new UpdateCompletedPaymentsTask());
	// DrBookingsFX.exe.submit(new UpdateExpectedPaymentsTask());

    }

    // Getter / Setter //

    /**
     *
     */
    public PaymentsProviderBean getData() {
	return this.data;
    }

}
