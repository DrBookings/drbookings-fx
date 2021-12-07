package com.github.drbookings.ui.concurrent;

import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.LinkedHashSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.github.drbookings.Booking;
import com.github.drbookings.BookingEntry2;
import com.github.drbookings.CleaningBeanSer2Factory2;
import com.github.drbookings.ExpenseSerFactory;
import com.github.drbookings.ser.BookingBeanSer2Factory;
import com.github.drbookings.ser.DataStoreCoreSer2;
import com.github.drbookings.ser.MarshallListener;
import com.github.drbookings.ui.DateBean2;
import com.github.drbookings.ui.RoomBean2;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class UpdateSerInfoService extends Service<String> {

    private final Collection<? extends DateBean2> selectedData;

    public UpdateSerInfoService(final Collection<? extends DateBean2> selectedData) {
	super();
	this.selectedData = selectedData;
    }

    @Override
    protected Task<String> createTask() {
	return new Task<String>() {

	    @Override
	    protected String call() throws Exception {
		return getString(selectedData);
	    }
	};
    }

    private static String getString(Collection<? extends DateBean2> list) {
	list = new LinkedHashSet<>(list);
	final ByteArrayOutputStream bao = new ByteArrayOutputStream();
	final BookingBeanSer2Factory bf = new BookingBeanSer2Factory();
	final CleaningBeanSer2Factory2 cf = new CleaningBeanSer2Factory2();
	@SuppressWarnings("unused")
	final ExpenseSerFactory ef = new ExpenseSerFactory();
	try {
	    final JAXBContext jc = JAXBContext.newInstance(DataStoreCoreSer2.class);
	    final Marshaller jaxbMarshaller = jc.createMarshaller();
	    jaxbMarshaller.setListener(new MarshallListener());
	    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	    final DataStoreCoreSer2 d = new DataStoreCoreSer2();
	    final Collection<Booking> bookings = new LinkedHashSet<>();
	    for (final DateBean2 o : list) {
		d.getExpenses().addAll(ExpenseSerFactory.build(o.getExpenses()));
		for (final RoomBean2 r : o.getRooms()) {
		    if (r.getCleaningEntry() != null) {
			d.getCleaningsSer().add(cf.build(r.getCleaningEntry()));
		    }
		    if (r.getBooking() != null) {
			for (final BookingEntry2 e : r.getBooking().toList()) {
			    bookings.add(e.getParent());
			}
		    }
		}
	    }
	    d.getBookingsSer().addAll(bf.build(bookings));
	    jaxbMarshaller.marshal(d, bao);
	} catch (final JAXBException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return bao.toString();
    }

}
