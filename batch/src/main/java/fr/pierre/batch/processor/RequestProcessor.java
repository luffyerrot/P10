package fr.pierre.batch.processor;

import java.util.Date;

import org.json.JSONObject;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.RestTemplate;

import fr.pierre.batch.entities.Booking;
import fr.pierre.batch.entities.Copy;
import fr.pierre.batch.entities.InitCopy;
import fr.pierre.batch.entities.RequestAndCopy;
import fr.pierre.batch.entities.User;
import fr.pierre.batch.util.Token;
import fr.pierre.batch.config.MailConfiguration;

public class RequestProcessor implements ItemProcessor<RequestAndCopy, Long> {

    JavaMailSender sender = MailConfiguration.getJavaMailSender();
	
	Token token = new Token();
	
	private RestTemplate restTemplate = new RestTemplate();
	private InitCopy init = new InitCopy();
	
	@Override
	public Long process(RequestAndCopy item) throws Exception {

		if (!item.equals(null)) {
			Booking booking = new Booking(new Date(), new Copy(item.getCopyId(), item.getEtat()), new User(item.getUserId(), item.getEmail(), item.getUsername()));
			booking.setRendering(false);
			booking.setAccepted(true);
			final String uriSave = "http://localhost:8080/booking/save";

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + token.getToken());
			HttpEntity<Booking> entityBooking = new HttpEntity<Booking>(booking, headers);
			
			restTemplate.exchange(uriSave, HttpMethod.PUT, entityBooking, String.class);

			Copy copy = null;
			
			final String uriCopy = "http://localhost:8080/copy/" + item.getCopyId();
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			ResponseEntity<String> copyString = restTemplate.exchange(uriCopy, HttpMethod.GET, entity, String.class);
			JSONObject json = new JSONObject(copyString.getBody());
			copy = init.toObject(json);
			
			if (copy != null) {
				
				sendEmailToUser(item.getEmail(), copy);
			}
			return item.getRequestId();
		}
		return null;
	}
	
	private void sendEmailToUser(String email, Copy copy) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@projet10.com");
        message.setTo(email);
        message.setSubject("Claim your book" + copy.getBook().getTitle().toString());
        message.setText("Hello, You have received this automated email to warn you that you had 48H to claim the book " + copy.getBook().getTitle().toString() + ". The reference are '" + 
        		copy.getId() + "' please keep it to claim your book.");
        
        this.sender.send(message);
	}
}
