package com.github.drbookings.ui;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.github.drbookings.UITests;
import com.github.drbookings.event.cleaning.AddCleaningEntryEvent;
import com.github.events1000.api.Events;

import javafx.scene.input.KeyCode;

@Category(UITests.class)
public class AddCleaningEntryUITest extends UITest2 {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

	}

	@Override
	@Before
	public void setUp() throws Exception {

	}

	@Override
	@After
	public void tearDown() throws Exception {

	}

	@Ignore
	@Test
	public void test() throws Exception {

		// wait for reading to complete
		Thread.sleep(2000);
		rightClickOn("#tableView");
		sleep(500);
		push(KeyCode.CONTROL, KeyCode.C);
		sleep(500);
		clickOn("#date");
		write("8/19/2018");
		sleep(500);
		clickOn("#comboBoxRoom");
		sleep(500);
		clickOn("F2");
		sleep(500);
		clickOn("#name");
		sleep(500);
		write("Alona");
		sleep(500);
		push(KeyCode.ENTER);
		assertThat(Events.getInstance().getHistory().size(), is(1));
		final AddCleaningEntryEvent e = (AddCleaningEntryEvent)Events.getInstance().getHistory().get(0);
		// assertThat(e.getData().room, is("2"));
		// assertThat(e.getData().name, is("Alona"));
		// assertThat(e.getData().date, is(LocalDate.of(2018, 8, 19)));
	}
}
