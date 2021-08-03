package fr.pierre.apirest.services;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.pierre.apirest.entities.Booking;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class BookingServiceTest {

	@Autowired
	BookingService bookingService;
	
	@Test
	@Transactional
	@Rollback
	public void saveUpdateDeleteProcessBooking() {
		Booking booking = new Booking(new Date(), null, null);
				
		Booking bookingReturn = bookingService.save(booking);
		String parseDate = new SimpleDateFormat("yyyy-MM-dd").format(bookingReturn.getBooking_date());
		
		Assert.assertEquals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), parseDate);
		Assert.assertEquals(0, bookingReturn.getRecall());
		Assert.assertEquals(false, bookingReturn.getRendering());
		
		bookingReturn.setRecall(4);
		bookingReturn.setRendering(true);
		
		Booking bookingUpdate = bookingService.update(bookingReturn);
		
		Assert.assertEquals(4, bookingUpdate.getRecall());
		Assert.assertEquals(true, bookingUpdate.getRendering());
				
		bookingService.deleteById(bookingUpdate.getId());
	}
}
