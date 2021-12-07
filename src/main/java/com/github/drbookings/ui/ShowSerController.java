package com.github.drbookings.ui;

import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.drbookings.SelectionManagerDateBean;
import com.github.drbookings.ui.concurrent.UpdateSerInfoService;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

public class ShowSerController implements Initializable {

    @SuppressWarnings("unused")
    private final static Logger logger = LoggerFactory.getLogger(ShowSerController.class);
    @FXML
    TextArea text;

    private final ListChangeListener<DateBean2> listener = c -> {

	ObservableList<? extends DateBean2> list2 = null;
	while (c.next()) {
	    list2 = c.getList();
	}
	if ((list2 == null) || list2.isEmpty()) {
	    text.setText(null);
	} else {
	    final UpdateSerInfoService s = new UpdateSerInfoService(list2);
	    s.setOnSucceeded(e -> {
		text.setText((String) e.getSource().getValue());
	    });
	    s.setOnFailed(e -> {
		if (logger.isErrorEnabled()) {
		    logger.error(s.getException().getLocalizedMessage(), s.getException());
		}
	    });
	    s.start();
	}

    };

    public ShowSerController() {

    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
	text.setWrapText(true);
	text.setEditable(false);
	SelectionManagerDateBean.getInstance().selectionProperty().addListener(listener);
    }
}
