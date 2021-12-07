package com.github.drbookings.ui;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.drbookings.BookingEntry2;
import com.github.drbookings.BookingEntryPair2;
import com.github.drbookings.CleaningBeanFactory;
import com.github.drbookings.CleaningBeanSer2;
import com.github.drbookings.ExpenseFactory;
import com.github.drbookings.Selection;
import com.github.drbookings.SelectionManagerBookings;
import com.github.drbookings.SelectionManagerCleanings;
import com.github.drbookings.SettingsManager;
import com.github.drbookings.persistence.InMemoryPersistenceProvider;
import com.github.drbookings.persistence.PersistenceService;
import com.github.events1000.api.Events;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

/**
 * The Main UI Controller. It renders a table view of data. Data inside the
 * table is of type {@link DateBean2}.
 *
 * @author Alexander Kerner
 *
 */
public class MainController2 implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(MainController2.class);
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("E\tdd.MM.yyyy");
    private static final PersistenceService persistenceService = InMemoryPersistenceProvider.getInstance();
    @FXML
    private TableView<DateBean2> tableView;
    private final TableColumn<DateBean2, LocalDate> firstColumn;
    private final CleaningBeanFactory cleaningBeanFactory = new CleaningBeanFactory();
    @SuppressWarnings("rawtypes")
    private final ListChangeListener<? super TablePosition> tableDataSelectionListener = (c) -> {
	Platform.runLater(() -> {
	    // if (logger.isDebugEnabled()) {
	    // logger.debug("Selection: " + c.getList());
	    // }
	    final List<Selection> selections = getSelectionFromTablePosition(c.getList());
	    Events.getInstance().emit(new SelectionEvent(selections));
	});
    };

    public MainController2() {

	firstColumn = new TableColumn<>("Date");
	expenseFactory = new ExpenseFactory();
	// initEventListeners();
    }

    @Deprecated
    @SuppressWarnings("rawtypes")
    private List<BookingEntry2> getBookingFromTablePosition(final Collection<? extends TablePosition> tablePositions) {
	final List<BookingEntry2> result = new ArrayList<>();
	for (final TablePosition r : tablePositions) {
	    if (r.getTableColumn().getCellData(r.getRow()) instanceof DateBean2) {
		final DateBean2 currentCell = (DateBean2) r.getTableColumn().getCellData(r.getRow());
		// SelectionManagerDateBean.getInstance().getSelection().remove(currentCell);
		final String currentRoomName = "" + r.getColumn();
		final BookingEntryPair2 bookingEntry = persistenceService.getBookingForRoomAndDate(currentRoomName,
			currentCell.getDate());
		if (bookingEntry != null) {
		    result.addAll(bookingEntry.toList());
		}
	    }
	}
	return result;
    }

    /**
     *
     * @param tablePositions
     *            all currently selected table positions
     * @return the current table selection transformed to instances of
     *         {@link Selection}
     */
    @SuppressWarnings("rawtypes")
    private List<Selection> getSelectionFromTablePosition(final Collection<? extends TablePosition> tablePositions) {
	final List<Selection> result = new ArrayList<>();
	for (final TablePosition r : tablePositions) {
	    final Object tableData = r.getTableColumn().getCellData(r.getRow());
	    if (tableData instanceof DateBean2) {
		final DateBean2 currentCell = (DateBean2) tableData;
		// TODO: dirty, room name equals column name
		final String currentRoomName = "" + r.getColumn();
		result.add(new Selection(currentRoomName, currentCell.getDate()));
	    }
	}
	return result;
    }

    @Deprecated
    @SuppressWarnings("rawtypes")
    private List<CleaningBeanSer2> getCleaningFromTablePosition(
	    final Collection<? extends TablePosition> tablePositions) {
	final List<CleaningBeanSer2> result = new ArrayList<>();
	for (final TablePosition r : tablePositions) {
	    if (r.getTableColumn().getCellData(r.getRow()) instanceof DateBean2) {
		final DateBean2 currentCell = (DateBean2) r.getTableColumn().getCellData(r.getRow());
		// SelectionManagerDateBean.getInstance().getSelection().remove(currentCell);
		final String currentRoomName = "" + r.getColumn();
		final CleaningBeanSer2 cleaningEntry = persistenceService.getCleaningForRoomAndDate(currentRoomName,
			currentCell.getDate());
		if (cleaningEntry != null) {
		    result.add(cleaningEntry);
		}
	    }
	}
	return result;
    }

    private void remove(final List<? extends TablePosition> removed) {
	final List<BookingEntry2> bookings = getBookingFromTablePosition(removed);
	final List<CleaningBeanSer2> cleanings = getCleaningFromTablePosition(removed);
	SelectionManagerBookings.getInstance().selectionProperty()
		.removeAll(bookings.stream().map(e -> e.getParent()).collect(Collectors.toSet()));
	SelectionManagerCleanings.getInstance().selectionProperty().removeAll(cleanings);

    }

    private void add(final List<? extends TablePosition> addedSubList) {
	final List<BookingEntry2> bookings = getBookingFromTablePosition(addedSubList);
	final List<CleaningBeanSer2> cleanings = getCleaningFromTablePosition(addedSubList);
	SelectionManagerBookings.getInstance().selectionProperty()
		.addAll(bookings.stream().map(e -> e.getParent()).collect(Collectors.toSet()));
	SelectionManagerCleanings.getInstance().selectionProperty().addAll(cleanings);
    }

    private void initEventListeners() {

    }

    private void initTableViewContextMenus() {

	final ContextMenu menu = buildTableContextMenu();
	tableView.setContextMenu(menu);
	tableView.addEventHandler(MouseEvent.MOUSE_CLICKED, t -> {
	    if (t.getButton() == MouseButton.SECONDARY) {
		menu.show(tableView, t.getScreenX(), t.getScreenY());
	    }
	});
    }

    private ContextMenu buildTableContextMenu() {

	final ContextMenu menu = new ContextMenu();
	final MenuItem deleteItem = new MenuItem("Delete");
	deleteItem.setAccelerator(KeyCombination.keyCombination("Shortcut+D"));
	final MenuItem addBookingEvent = new MenuItem("Add Booking");
	addBookingEvent.setAccelerator(KeyCombination.keyCombination("Shortcut+A"));
	final MenuItem addCleaningEvent = new MenuItem("Add Cleaning");
	addCleaningEvent.setAccelerator(KeyCombination.keyCombination("Shortcut+C"));
	addBookingEvent.setOnAction(event -> new NewBookingDialog().show());
	addCleaningEvent.setOnAction(event -> new NewCleaningDialog().show());
	menu.getItems().addAll(addBookingEvent, addCleaningEvent, deleteItem);
	return menu;
    }

    public void showSer() {

	new SerView().show();
    }

    public void save() {
	final Path p = Paths.get("/home/alex/test-out.xml");
	final SaveService s = new SaveService(tableView.getItems(), p);
	s.setOnFailed(e -> {
	    if (logger.isErrorEnabled()) {
		logger.error(s.getException().getLocalizedMessage(), s.getException());
	    }
	    final Alert alert = new Alert(AlertType.ERROR);
	    alert.setTitle("Error");
	    alert.setHeaderText("Saving failed.");
	    alert.setContentText(s.getException().getLocalizedMessage());
	    alert.showAndWait();
	});
	s.setOnSucceeded(e -> {
	    if (logger.isInfoEnabled()) {
		logger.info("Saved data to " + p);
	    }
	});
	s.start();
    }

    public void showBookingDetails() {

	Platform.runLater(() -> {
	    new BookingDetailsView().show();
	});

    }

    public void showCleaningDetails() {
	Platform.runLater(() -> {
	    new CleaningDetailsView().show();
	});
    }

    private final ExpenseFactory expenseFactory;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {

	initTableView();
    }

    private void initTableView() {

	setTableColumns();
	tableView.getSelectionModel().setCellSelectionEnabled(true);
	tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	initTableViewContextMenus();
	tableView.getSortOrder().add(firstColumn);
	tableView.setRowFactory(getRowFactory());

	tableView.getSelectionModel().getSelectedCells().addListener(tableDataSelectionListener);
	tableView.setOnMousePressed(event -> {
	    if (event.isPrimaryButtonDown() && (event.getClickCount() == 2)) {
		handleTableSelectEvent(event);
	    }
	});

	Platform.runLater(() -> {
	    tableView.getItems().add(new DateBean2(LocalDate.now()));
	    appendAtTop(LocalDate.now(), 5);
	    appendAtBottom(LocalDate.now(), 5);
	});

	tableView.setOnScroll(e -> {
	    if (e.getDeltaY() > 0) {
		addMoreDataTop(1);
	    } else if (e.getDeltaY() < 0) {
		addMoreDataBottom(1);
	    }
	});

    }

    private void appendAtTop(final LocalDate initialItemExclusive, final int numberOfElements) {
	for (int i = 1; i <= numberOfElements; i++) {
	    final int j = i;
	    tableView.getItems().add(0, new DateBean2(initialItemExclusive.minusDays(j)));
	}
    }

    private void appendAtBottom(final LocalDate initialItemExclusive, final int numberOfElements) {
	for (int i = 1; i <= numberOfElements; i++) {
	    final int j = i;
	    tableView.getItems().add(new DateBean2(initialItemExclusive.plusDays(j)));
	}
    }

    private void addMoreDataTop(final int numberOfElements) {

	if (tableView.getItems().isEmpty()) {

	} else {
	    final DateBean2 firstItem = tableView.getItems().get(0);
	    appendAtTop(firstItem.getDate(), numberOfElements);
	}

    }

    private void addMoreDataBottom(final int numberOfElements) {

	if (tableView.getItems().isEmpty()) {

	} else {
	    final DateBean2 lastItem = tableView.getItems().get(tableView.getItems().size() - 1);

	    appendAtBottom(lastItem.getDate(), numberOfElements);

	}

    }

    private void handleTableSelectEvent(final MouseEvent event) {
	Platform.runLater(() -> showBookingDetails());

    }

    private Callback<TableView<DateBean2>, TableRow<DateBean2>> getRowFactory() {

	return param -> {
	    final TableRow<DateBean2> row = new TableRow<DateBean2>() {

		@Override
		protected void updateItem(final DateBean2 item, final boolean empty) {

		    super.updateItem(item, empty);
		    getStyleClass().removeAll("now", "end-of-month");
		    if (empty || (item == null)) {
		    } else if (item.getDate().isEqual(LocalDate.now())) {
			getStyleClass().add("now");
		    } else if (item.getDate()
			    .equals(item.getDate().with(java.time.temporal.TemporalAdjusters.lastDayOfMonth()))) {
			getStyleClass().add("end-of-month");
		    }
		}
	    };
	    return row;
	};
    }

    private void setTableColumns() {

	addDateColumn();
	final int numberRooms = SettingsManager.getInstance().getNumberOfRooms();
	// final int numberRooms = 4;
	final String prefix = SettingsManager.getInstance().getRoomNamePrefix();
	for (int i = 1; i <= numberRooms; i++) {
	    final TableColumn<DateBean2, DateBean2> col1 = new TableColumn<>(prefix + i);
	    col1.setCellValueFactory(new PropertyValueFactory<>("self"));
	    col1.setCellFactory(new DateBean2CellFactory("" + i));
	    tableView.getColumns().add(col1);
	}
	// addOccupancyRateColumn();
	// addEarningsColumn();
    }

    private void addDateColumn() {

	firstColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
	firstColumn.setCellFactory(column -> {
	    return new TableCell<DateBean2, LocalDate>() {

		@Override
		protected void updateItem(final LocalDate item, final boolean empty) {

		    super.updateItem(item, empty);
		    if ((item == null) || empty) {
			setText(null);
			setStyle("");
		    } else {
			setText(DATE_FORMATTER.format(item));
		    }
		}
	    };
	});
	firstColumn.getStyleClass().addAll("center-left");
	tableView.getColumns().add(firstColumn);
    }
}
