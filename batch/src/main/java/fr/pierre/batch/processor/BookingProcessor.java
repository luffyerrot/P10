package fr.pierre.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import fr.pierre.batch.entities.Booking;
import fr.pierre.batch.config.MailConfiguration;

public class BookingProcessor implements ItemProcessor<Booking, Booking> {

    JavaMailSender sender = MailConfiguration.getJavaMailSender();
	
	@Override
	public Booking process(final Booking bookingInput) throws Exception {
		
		if(bookingInput.getRecall() < 8) {
			Booking bookingOutput = null;
			bookingOutput = new Booking();
			
			bookingOutput.setId(bookingInput.getId());
			bookingOutput.setBooking_date(bookingInput.getBooking_date());
			bookingOutput.setCopy(bookingInput.getCopy());
			bookingOutput.setDelay(bookingInput.getDelay());
			bookingOutput.setUser(bookingInput.getUser());
			bookingOutput.setRendering(bookingInput.getRendering());
			bookingOutput.setRecall(bookingInput.getRecall() + 1);

			//sendEmailToUser(bookingInput.getUser().getEmail(), bookingInput.getCopy().getBook().getTitle());

			return bookingOutput;
		} else {
			return bookingInput;
		}
	}
	
	private void sendEmailToUser(String email, String title) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@projet7.com");
        message.setTo(email);
        message.setSubject("Rendering book named : " + title.toString());
        message.setText("Hello, You have received this automated email to warn you that the rental date has passed.");
        
        this.sender.send(message);
	}
}
