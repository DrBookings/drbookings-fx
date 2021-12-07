package com.github.drbookings.ui;

import java.util.Arrays;
import java.util.List;

import javax.money.MonetaryAmount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.drbookings.Payment;
import com.github.drbookings.PaymentImpl;
import com.github.drbookings.Payments2;
import com.github.drbookings.PaymentsProviderBean;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PaymentsBox extends VBox {

    private static final Logger logger = LoggerFactory.getLogger(PaymentsBox.class);

    private final Label totalPaymentExpectedLabel;

    private final Label totalPaymentExpectedAmount;

    private final Label totalPaymentCompletedLabel;

    private final Label totalPaymentCompletedAmount;

    private final Button addPayment;

    private final GridPane gridPane;

    private final TextField newAmountInput;

    private final ObjectProperty<PaymentsProviderBean> paymentsProvider;

    public static String format(final Number amount) {
	return format(Payments2.createMondary(amount));
    }

    public static String format(final MonetaryAmount amount) {
	return amount.toString();
    }

    public static PaymentsBox buildIncomeBox() {
	final PaymentsBox result = new PaymentsBox();
	return result;
    }

    public static Node buildExpenseBox() {
	final PaymentsBox result = new PaymentsBox();
	return result;
    }

    public PaymentsBox() {
	paymentsProvider = new SimpleObjectProperty<>();
	gridPane = new GridPane();
	addPayment = new Button("Add Payment");
	newAmountInput = new TextField();
	totalPaymentExpectedLabel = new Label("Expected");
	totalPaymentExpectedAmount = new Label(format(0));
	totalPaymentCompletedLabel = new Label("Completed");
	totalPaymentCompletedAmount = new Label(format(0));
	buildComponent();
	bindComponents();
    }

    private void buildComponent() {

	gridPane.getColumnConstraints().addAll(getConstrains());
	gridPane.add(totalPaymentExpectedLabel, 0, 0);
	gridPane.add(totalPaymentExpectedAmount, 1, 0);
	gridPane.add(totalPaymentCompletedLabel, 0, 1);
	gridPane.add(totalPaymentCompletedAmount, 1, 1);
	gridPane.add(newAmountInput, 0, 2);
	gridPane.add(addPayment, 1, 2);
	getChildren().add(gridPane);

	addPayment.setOnAction(e -> handleButtonClick());

    }

    private void handleButtonClick() {
	try {
	    addPayment(newAmountInput.getText());
	} catch (final Exception e) {
	    e.printStackTrace();
	}
    }

    private void bindComponents() {
	paymentsProvider.addListener((e, o, n) -> {
	    updateTotalPayments();
	});
    }

    private List<ColumnConstraints> getConstrains() {
	final ColumnConstraints c1 = new ColumnConstraints();
	c1.setHgrow(Priority.SOMETIMES);
	c1.setMinWidth(10);
	c1.setPercentWidth(30);
	c1.setHalignment(HPos.CENTER);
	final ColumnConstraints c2 = new ColumnConstraints();
	c2.setHgrow(Priority.ALWAYS);
	c2.setMinWidth(10);
	c2.setPercentWidth(40);
	c2.setHalignment(HPos.CENTER);
	// final ColumnConstraints c3 = new ColumnConstraints();
	// c3.setHgrow(Priority.SOMETIMES);
	// c3.setMinWidth(10);
	// c3.setPercentWidth(30);
	// c3.setHalignment(HPos.CENTER);
	return Arrays.asList(c1, c2/* , c3 */);
    }

    public void addPayment(final String amount) {
	if (getPaymentsProvider() == null)
	    throw new IllegalStateException("No payments provider set");
	final Payment p = new PaymentImpl(amount);
	getPaymentsProvider().getPayments().add(p);
	updateTotalPayments();
    }

    private void updateTotalPayments() {
	final MonetaryAmount total = Payments2.getSum(getPaymentsProvider().getPayments());
	totalPaymentCompletedAmount.setText(format(total));
    }

    // Getter / Setter //

    /**
     *
     * @param expectedLabel
     * @return {@code this}
     */
    public PaymentsBox setExpectedLabel(final String expectedLabel) {
	this.totalPaymentExpectedLabel.setText(expectedLabel);
	return this;
    }

    public PaymentsBox setCompletedLabel(final String completedLabel) {
	this.totalPaymentExpectedLabel.setText(completedLabel);
	return this;
    }

    public ObjectProperty<PaymentsProviderBean> paymentsProviderProperty() {
	return this.paymentsProvider;
    }

    public PaymentsProviderBean getPaymentsProvider() {
	return this.paymentsProviderProperty().get();
    }

    public void setPaymentsProvider(final PaymentsProviderBean paymentsProvider) {
	this.paymentsProviderProperty().set(paymentsProvider);

    }

}
