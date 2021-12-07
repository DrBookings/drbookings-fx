package com.github.drbookings.ui;

import javafx.scene.control.TextInputDialog;
import javafx.stage.StageStyle;

class NewPaymentDialog extends TextInputDialog {

    public NewPaymentDialog(final String paymentProposal) {
	super(paymentProposal);
	setTitle(null);
	setHeaderText(null);
	setGraphic(null);
	initStyle(StageStyle.UTILITY);
	setContentText("Enter amount");
    }

}
