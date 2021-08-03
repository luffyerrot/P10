package fr.pierre.apirest.services;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.pierre.apirest.entities.Request;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class RequestServiceTest {

	@Autowired
	RequestService requestService;
	
	@Test
	@Transactional
	@Rollback
	public void saveUpdateDeleteProcessRequest() {

		Date actualDate = new Date();
		
		Request request = new Request(actualDate);
	
		Request requestReturn = requestService.add(request);

		String parseDate = new SimpleDateFormat("yyyy-MM-dd").format(requestReturn.getRelease_date());
		
		Assert.assertEquals(new SimpleDateFormat("yyyy-MM-dd").format(actualDate), parseDate);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(requestReturn.getRelease_date());
		calendar.add(Calendar.DATE, 2);
		Date newDate = calendar.getTime();
		
		requestReturn.setRelease_date(newDate);
		
		Request requestUpdate = requestService.update(requestReturn);

		String parseDate2 = new SimpleDateFormat("yyyy-MM-dd").format(requestUpdate.getRelease_date());
		
		Assert.assertEquals(new SimpleDateFormat("yyyy-MM-dd").format(newDate), parseDate2);
			
		requestService.delete(requestUpdate.getId());
	}
}
