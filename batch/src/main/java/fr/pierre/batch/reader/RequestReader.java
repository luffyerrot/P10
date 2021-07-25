package fr.pierre.batch.reader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import fr.pierre.batch.entities.Book;
import fr.pierre.batch.entities.Booking;
import fr.pierre.batch.entities.Copy;
import fr.pierre.batch.entities.InitBook;
import fr.pierre.batch.entities.InitBooking;
import fr.pierre.batch.entities.Request;
import fr.pierre.batch.entities.RequestAndCopy;
import fr.pierre.batch.util.Token;

public class RequestReader implements ItemReader<RequestAndCopy>{
	
	Token token = new Token();
	RestTemplate restTemplate = new RestTemplate();
	
	private InitBook initBook = new InitBook();
	private InitBooking initBooking = new InitBooking();
	
	private int nextRequestIndex = 0;
	private List<RequestAndCopy> requestData = null;

	@Override
	public RequestAndCopy read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {			
		 	
		checkBookingValidation();
		
		if (requestDataIsNotInitialized()) {
			this.requestData = fetchRequestDataFromAPI();
	    }
		
    	RequestAndCopy nextRequest = null;
	 
        if (requestData != null && nextRequestIndex < requestData.size()) {
        	nextRequest = requestData.get(nextRequestIndex);
        	nextRequestIndex++;
        } else {
        	nextRequestIndex = 0;
        	requestData = null;
        }

        return nextRequest;
	}

    private boolean requestDataIsNotInitialized() {
        return this.requestData == null;
    }
    
    private HttpEntity<String> getEntity() {
    	HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token.getToken());
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		return entity;
    }
    
    private void checkBookingValidation() {

		String uriBooking = "http://localhost:8080/booking/";
		
		HttpEntity<String> entity = getEntity();

    	ResponseEntity<String> resultBooking = restTemplate.exchange(uriBooking, HttpMethod.GET, entity, String.class);

	    JSONArray arrayJsonBg = new JSONArray(resultBooking.getBody());
	    List<Booking> bookings = new ArrayList<>();
	    for (int i = 0; i < arrayJsonBg.length(); i++){
	    	JSONObject json = new JSONObject(arrayJsonBg.get(i).toString());
			try {
				bookings.add(initBooking.toObjectCheckUserAccept(json));
			} catch (JSONException | java.text.ParseException e) {
				e.printStackTrace();
			}
	    }
	    for (int i = 0; i < bookings.size(); i++) {
	    	if (bookings.get(i).getUserClaim() == null) {
	    		double hours = ((new Date().getTime() - bookings.get(i).getBooking_date().getTime()) / (1000 * 60 * 60));
	    		if (hours > 48) {
	    			deleteBooking(bookings.get(i).getId());
	    		}
	    	} else if (bookings.get(i).getUserClaim().equals(false)) {
    			deleteBooking(bookings.get(i).getId());
	    	}
	    }
    }
    
    private void deleteBooking(Long id) {

    	String uriBooking = "http://localhost:8080/booking/delete/" + id;
		
		HttpEntity<String> entity = getEntity();

    	restTemplate.exchange(uriBooking, HttpMethod.DELETE, entity, String.class);
    }
 
	private List<RequestAndCopy> fetchRequestDataFromAPI() {
    	
		String uriBook = "http://localhost:8080/book/requestnotnull";
		String uriBooking = "http://localhost:8080/booking/";

		HttpEntity<String> entity = getEntity();
	    List<Booking> bookings = new ArrayList<>();
	    
		if (restTemplate.exchange(uriBooking, HttpMethod.GET, entity, String.class) != null) {

	    	ResponseEntity<String> resultBooking = restTemplate.exchange(uriBooking, HttpMethod.GET, entity, String.class);
		    
		    JSONArray arrayJsonBg = new JSONArray(resultBooking.getBody());
		    for (int i = 0; i < arrayJsonBg.length(); i++){
		    	JSONObject json = new JSONObject(arrayJsonBg.get(i).toString());
				try {
					bookings.add(initBooking.toObject(json));
				} catch (JSONException | java.text.ParseException e) {
					e.printStackTrace();
				}
		    }
		}

		if (restTemplate.exchange(uriBook, HttpMethod.GET, entity, String.class) != null) {

	    	ResponseEntity<String> resultBook = restTemplate.exchange(uriBook, HttpMethod.GET, entity, String.class);
	    	JSONArray arrayJsonB = new JSONArray(resultBook.getBody());
		    List<Book> books = new ArrayList<>();
		    for (int i = 0; i < arrayJsonB.length(); i++){
		    	JSONObject json = new JSONObject(arrayJsonB.get(i).toString());
				try {
					books.add(initBook.toObjectWithRequest(json));
				} catch (JSONException | java.text.ParseException e) {
					e.printStackTrace();
				}
		    }

			List<Long> idsBookingCopy = new ArrayList<>();
			List<Long> idsBookingUser = new ArrayList<>();
			if (bookings != null && !bookings.isEmpty()) {
	    		for (int i = 0; i < bookings.size(); i++) {
	    			idsBookingCopy.add(bookings.get(i).getCopy().getId());
	    			idsBookingUser.add(bookings.get(i).getUser().getId());
	    		}
			}
		    
		    List<RequestAndCopy> requestAndCopys = new ArrayList<>();
		    for (int i = 0; i < books.size(); i++) {
			    List<Copy> copies = new ArrayList<>();
		    	for (int j = 0; j < books.get(i).getCopies().size(); j++) {
		    		if (!idsBookingCopy.isEmpty()) {
		    			if (!idsBookingCopy.contains(books.get(i).getCopies().get(j).getId())) {
		    				copies.add(books.get(i).getCopies().get(j));
		    			}
		    		} else {
		    			copies.add(books.get(i).getCopies().get(j));
		    		}
		    	}
		    	if (!copies.isEmpty()) {
	    			List<Long> alreadyRequest = new ArrayList<>();
	    			for (int j = 0; j < copies.size(); j++) {
	    				Request oldRequest = null;
	    				if (books.get(i).getRequests() != null) {
	    					for (int k = 0; k < books.get(i).getRequests().size(); k++) {
								if (alreadyRequest.isEmpty() || !alreadyRequest.contains(books.get(i).getRequests().get(k).getId())) {
									if (oldRequest == null || !oldRequest.getRelease_date().before(books.get(i).getRequests().get(k).getRelease_date()) || !idsBookingUser.contains(books.get(i).getRequests().get(k).getUser().getId())) {
										oldRequest = books.get(i).getRequests().get(k);
									}
								}
							}
	    				}
	    				if (oldRequest != null) {
				    		alreadyRequest.add(oldRequest.getId());
				    		requestAndCopys.add(new RequestAndCopy(oldRequest.getId(), oldRequest.getUser().getId(), oldRequest.getUser().getEmail(), oldRequest.getUser().getUsername(), 
				    				copies.get(j).getId(), copies.get(j).getEtat()));
	    				}
	    			}
	    		} else {
	    			return requestData = null;
	    		}
		    }

		    if (!requestAndCopys.isEmpty()) {
			    return requestData = requestAndCopys;
		    }
		}
	    return requestData = null;
    }
}
