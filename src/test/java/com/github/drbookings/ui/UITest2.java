package com.github.drbookings.ui;

import java.io.File;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.testfx.framework.junit.ApplicationTest;

import com.github.drbookings.DrBookingsFX;

import javafx.stage.Stage;

public class UITest2 extends ApplicationTest {

    public static final File USER_HOME = new File("src/test/resources/tmp/user-home");

    @BeforeClass
    public static void setUpClass() throws Exception {

	Files.createDirectories(USER_HOME.toPath());
	System.setProperty("user.home", USER_HOME.getAbsolutePath());
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
	FileUtils.deleteDirectory(USER_HOME);
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    protected DrBookingsFX instance;

    @Override
    public void start(final Stage stage) throws Exception {
	instance = new DrBookingsFX();
	instance.start(stage);
    }

}
