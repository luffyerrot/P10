package fr.pierre.batch.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import fr.pierre.batch.entities.Booking;
import fr.pierre.batch.util.Token;

public class BookingWriter implements ItemWriter<Booking>{

	Token token = new Token();
	
	private RestTemplate restTemplate = new RestTemplate();
		
	@Override
	public void write(List<? extends Booking> items) throws Exception {
		
		for (int i = 0; i < items.size(); i++) {

			Booking booking = items.get(i);
			final String uri = "http://localhost:8080/booking/updateRecall/" + booking.getId();
			
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + token.getToken());
			HttpEntity<Booking> entity = new HttpEntity<Booking>(booking, headers);
			
			restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
		}
	}
}
