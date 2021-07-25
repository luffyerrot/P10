package fr.pierre.api.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import fr.pierre.api.entities.Book;
import fr.pierre.api.entities.Booking;
import fr.pierre.api.entities.Copy;
import fr.pierre.api.entities.InitBook;
import fr.pierre.api.entities.InitBooking;
import fr.pierre.api.entities.InitCopy;
import fr.pierre.api.entities.InitRequest;
import fr.pierre.api.entities.Request;
import fr.pierre.api.entities.User;

@Service
public class BookingService {

	@Autowired
	private Environment environment;
	@Autowired
	private UserService userService;
	@Autowired
	private BookService bookService;
	@Autowired
	private HeaderInformationThrowerService headerInformationThrowerService;
	
	private InitBooking init = new InitBooking();
	private InitBook initBook = new InitBook();
	private InitCopy initCopy = new InitCopy();
	private InitRequest initRequest = new InitRequest();
	private RestTemplate restTemplate = new RestTemplate();

	public Booking getBookingId(Long id)
	{
		Long userId = userService.userAuth().getId();

		HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(userId);
		
		final String uri = environment.getRequiredProperty("booking.url") + id;

		if (restTemplate.exchange(uri, HttpMethod.GET, entity, String.class) != null) {

		    ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		     
			JSONObject json = new JSONObject(result.getBody());
			Booking booking = null;
			try {
				booking = init.toObject(json);
			} catch (ParseException | JSONException e) {
				e.printStackTrace();
			}
		    return booking;
		}
		return null;
	}
	
	public List<Booking> getByUserId()
	{
		User user = userService.userAuth();
		if(user != null) {
			Long id = user.getId();

			HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(id);
			
		    final String uri = environment.getRequiredProperty("booking.url") + "userid/" + id;

			if (restTemplate.exchange(uri, HttpMethod.GET, entity, String.class) != null) {
				
				ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
			    
				JSONArray jsonArray = new JSONArray(result.getBody());
				List<Booking> bookings = new ArrayList<>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject json = jsonArray.getJSONObject(i);
					Booking booking = null;
					try {
						booking = init.toObject(json);
						bookings.add(booking);
					} catch (JSONException | ParseException e) {
						e.printStackTrace();
					}
				}
			    return bookings;
			}
			return null;
		} else {
			return null;
		}
	}

	public List<Booking> getByUserIdAndAcceptedAndUserclaim(String userclaim)
	{
		User user = userService.userAuth();
		if(user != null) {
			Long id = user.getId();

			HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(id);
			HttpEntity<String> entityBoolean = new HttpEntity<String>(userclaim, entity.getHeaders());
			
		    final String uri = environment.getRequiredProperty("booking.url") + "useridandaccepted/" + id;

			if (restTemplate.exchange(uri, HttpMethod.POST, entityBoolean, String.class) != null) {
				
				ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, entityBoolean, String.class);
			    
				JSONArray jsonArray = new JSONArray(result.getBody());
				List<Booking> bookings = new ArrayList<>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject json = jsonArray.getJSONObject(i);
					Booking booking = null;
					try {
						booking = init.toObject(json);
						bookings.add(booking);
					} catch (JSONException | ParseException e) {
						e.printStackTrace();
					}
				}
			    return bookings;
			}
			return null;
		} else {
			return null;
		}
	}
	
	public List<Booking> getAllBookingNotRenderedAndAcceptedNull()
	{
		Long userId = userService.userAuth().getId();

		HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(userId);
		
	    final String uri = environment.getRequiredProperty("booking.url") + "acceptednull";

		if (restTemplate.exchange(uri, HttpMethod.GET, entity, String.class) != null) {
			
		    ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		     
		    JSONArray arrayJson = new JSONArray(result.getBody());
		    List<Booking> bookings = new ArrayList<>();
		    for (int i = 0; i < arrayJson.length(); i++){
		    	JSONObject json = new JSONObject(arrayJson.get(i).toString());
		    	try {
					bookings.add(init.toObject(json));
				} catch (ParseException | JSONException e) {
					e.printStackTrace();
				}
		    }
		    return bookings;
		}
		return null;
	}

	public List<Booking> getAllBookingNotRenderedAndAccepted(Boolean accepted)
	{
		Long userId = userService.userAuth().getId();

		HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(userId);
		
	    final String uri = environment.getRequiredProperty("booking.url") + "acceptednotrendered";

		HttpEntity<Boolean> entityBoolean = new HttpEntity<Boolean>(accepted, entity.getHeaders());
		if (restTemplate.exchange(uri, HttpMethod.POST, entityBoolean, String.class) != null) {
			
		    ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, entityBoolean, String.class);
		     
		    JSONArray arrayJson = new JSONArray(result.getBody());
		    List<Booking> bookings = new ArrayList<>();
		    for (int i = 0; i < arrayJson.length(); i++){
		    	JSONObject json = new JSONObject(arrayJson.get(i).toString());
		    	try {
					bookings.add(init.toObject(json));
				} catch (ParseException | JSONException e) {
					e.printStackTrace();
				}
		    }
		    return bookings;
		}
		return null;
	}
	
	public List<Booking> getAllBookingNotRendered()
	{
		Long userId = userService.userAuth().getId();

		HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(userId);
		
	    final String uri = environment.getRequiredProperty("booking.url");

		if (restTemplate.exchange(uri, HttpMethod.GET, entity, String.class) != null) {
			
		    ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		     
		    JSONArray arrayJson = new JSONArray(result.getBody());
		    List<Booking> bookings = new ArrayList<>();
		    for (int i = 0; i < arrayJson.length(); i++){
		    	JSONObject json = new JSONObject(arrayJson.get(i).toString());
		    	try {
					bookings.add(init.toObject(json));
				} catch (ParseException | JSONException e) {
					e.printStackTrace();
				}
		    }
		    return bookings;
		}
		return null;
	}
	
	public List<Booking> getAllBookingRendered()
	{
		Long userId = userService.userAuth().getId();

		HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(userId);
	     
	    final String uri = environment.getRequiredProperty("booking.url") + "rendered";

		if (restTemplate.exchange(uri, HttpMethod.GET, entity, String.class) != null) {
			
		    ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		     
		    JSONArray arrayJson = new JSONArray(result.getBody());
		    List<Booking> bookings = new ArrayList<>();
		    for (int i = 0; i < arrayJson.length(); i++){
		    	JSONObject json = new JSONObject(arrayJson.get(i).toString());
		    	try {
					bookings.add(init.toObject(json));
				} catch (ParseException | JSONException e) {
					e.printStackTrace();
				}
		    }
		    return bookings;
		}
		return null;
	}
	
	public Date getFirstDate(Long ibn)
	{
		Long userId = userService.userAuth().getId();

		HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(userId);
	     
	    final String uri = environment.getRequiredProperty("booking.url") + "firstdate/" + ibn;

		try {
			
		    ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		    
			String dateString = result.getBody().toString().substring(1, 11);
		    Date date = null;
		    try {
				date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return date;
		} catch (HttpClientErrorException e) {
			return null;
		}
	}
	
	public List<Long> userBookingCopyId() {

		Long id = userService.userAuth().getId();

		HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(id);

		final String uri = environment.getRequiredProperty("booking.url");

		if (restTemplate.exchange(uri, HttpMethod.GET, entity, String.class) != null) {
			
		    ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
			
			JSONArray jsonBooking = new JSONArray(result.getBody());
			List<Long> userCopyId = new ArrayList<>();
			for (Object o: jsonBooking) {
				Long idCopy = ((JSONObject)o).getJSONObject("copy").getLong("id");
				userCopyId.add(idCopy);
			}
			return userCopyId;
		}
		return null;
	}
	
	public ResponseEntity<Void> create(Long ibn, String email)
	{
		Long userid = userService.userAuth().getId();

		HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(userid);

		final String uri = environment.getRequiredProperty("booking.url") + "save";

		Copy copy = null;
		try {
			copy = getCopyNotBooking(ibn);
		} catch (ParseException e) {
			e.printStackTrace();
		}
			
		if (copy != null) {

			Booking booking = new Booking();
			booking.setCopy(copy);
			booking.setUser(userService.getUserEmail(email));
			
			Date date = new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			//c.add(Calendar.DATE, -45);
			date = c.getTime();
			
			booking.setBooking_date(date);
			booking.setDelay(false);
			booking.setRendering(false);
			booking.setRecall(0);
			booking.setUserClaim(true);
			
			HttpEntity<Booking> entityBooking = new HttpEntity<Booking>(booking, entity.getHeaders());
			
		    restTemplate.exchange(uri, HttpMethod.PUT, entityBooking, String.class);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	public Copy getCopyNotBooking(Long ibn) throws ParseException {
		Long userid = userService.userAuth().getId();

		HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(userid);
		
		final String uriBooking = environment.getRequiredProperty("booking.url");
		final String uriCopy = environment.getRequiredProperty("copy.url") + "/bookibn/" + ibn;

		List<Booking> bookings = new ArrayList<>();
		if (restTemplate.exchange(uriBooking, HttpMethod.GET, entity, String.class) != null) {
		    ResponseEntity<String> resultBooking = restTemplate.exchange(uriBooking, HttpMethod.GET, entity, String.class);
			
			JSONArray jsonA = new JSONArray(resultBooking.getBody());
		    for (int i = 0; i < jsonA.length(); i++){
		    	JSONObject json = new JSONObject(jsonA.get(i).toString());
		    	try {
		    		bookings.add(init.toObject(json));
				} catch (JSONException e) {
					e.printStackTrace();
				}
		    }
		}

		List<Copy> copies = new ArrayList<>();
		if (restTemplate.exchange(uriBooking, HttpMethod.GET, entity, String.class) != null) {
		    ResponseEntity<String> resultCopy = restTemplate.exchange(uriCopy, HttpMethod.GET, entity, String.class);
			
			JSONArray jsonB = new JSONArray(resultCopy.getBody());
		    for (int i = 0; i < jsonB.length(); i++){
		    	JSONObject json = new JSONObject(jsonB.get(i).toString());
		    	try {
		    		copies.add(initCopy.toObject(json));
				} catch (JSONException e) {
					e.printStackTrace();
				}
		    }
		}
		
		if (copies != null && !copies.isEmpty()) {
			if (bookings != null) {
				if (!bookings.isEmpty()) {
					for (int i = 0; i < copies.size(); i++) {
						List<Long> idsBookingCopy = new ArrayList<>();
						for (int j = 0; j < bookings.size(); j++) {
							idsBookingCopy.add(bookings.get(j).getCopy().getId());
						}
						
						if (!idsBookingCopy.isEmpty()) {
							if (!idsBookingCopy.contains(copies.get(i).getId())) {
								return copies.get(i);
							}
						}
					}
				} else {
					return copies.get(0);
				}
			}
		}
		return null;
	}
	
	public ResponseEntity<Void> requestCreate(Long ibn, String email)
	{
		if (userService.getUserEmail(email) == null) {
			return ResponseEntity.notFound().build();
		}
		
		Long userid = userService.userAuth().getId();

		HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(userid);

		final String uri = environment.getRequiredProperty("request.url") + "add";
		final String uriGet = environment.getRequiredProperty("request.url");
		final String uriBook = environment.getRequiredProperty("book.url") + ibn;
		final String uriBooking = environment.getRequiredProperty("booking.url") + "userid/" + userService.getUserEmail(email).getId();
		
		List<Booking> bookings = new ArrayList<>();
		if (restTemplate.exchange(uriBooking, HttpMethod.GET, entity, String.class) != null) {
			ResponseEntity<String> result = restTemplate.exchange(uriBooking, HttpMethod.GET, entity, String.class);
			
			JSONArray jsonBooking = new JSONArray(result.getBody());
			for (Object o: jsonBooking) {
				try {
					bookings.add(init.toObject((JSONObject) o));
				} catch (JSONException | ParseException e) {
					e.printStackTrace();
				}
			}
		}
		
		for (int i = 0; i < bookings.size(); i ++) {
			if (bookings.get(i).getUser().getId().equals(userService.getUserEmail(email).getId())) {
				return ResponseEntity.notFound().build();
			}
		}
		
		Book book = null;
		if (restTemplate.exchange(uriBook, HttpMethod.GET, entity, String.class) != null) {
			ResponseEntity<String> result = restTemplate.exchange(uriBook, HttpMethod.GET, entity, String.class);
			
			JSONObject jsonBook = new JSONObject(result.getBody());
			try {
				book = initBook.toObject(jsonBook);
			} catch (JSONException | ParseException e) {
				e.printStackTrace();
			}
		}
		
		if (restTemplate.exchange(uriGet, HttpMethod.GET, entity, String.class) != null) {
			ResponseEntity<String> result = restTemplate.exchange(uriGet, HttpMethod.GET, entity, String.class);
			
			JSONArray jsonRequest = new JSONArray(result.getBody());
			List<Long> idUserRequest = new ArrayList<>();
			List<Request> requests = new ArrayList<>();
			for (Object o: jsonRequest) {
				idUserRequest.add(((JSONObject)o).getJSONObject("user").getLong("id"));
				if (book.getIbn().equals(((JSONObject) o).getJSONObject("book").getLong("ibn"))) {
					try {
						requests.add(initRequest.toObject((JSONObject) o));
					} catch (JSONException | ParseException e) {
						e.printStackTrace();
					}
				}
			}
			
			if (!idUserRequest.contains(userService.getUserEmail(email).getId()) && book.getCopies() != null && requests.size() < (book.getCopies().size() * 2)) {
				Request request = new Request();
				request.setUser(userService.getUserEmail(email));
				request.setBook(bookService.getBookByIbn(ibn));
				
				Date date = new Date();
				Calendar c = Calendar.getInstance();
				c.setTime(date);
				date = c.getTime();
				
				request.setRelease_date(date);
				request.setAccepted(null);
				request.setAccepted_time(null);
				
				HttpEntity<Request> entityRequest = new HttpEntity<Request>(request, entity.getHeaders());
				
			    restTemplate.exchange(uri, HttpMethod.PUT, entityRequest, String.class);
				return ResponseEntity.ok().build();
			}
		}
		return ResponseEntity.notFound().build();
	}
	
	public ResponseEntity<Void> requestDelete(Long ibn, String email)
	{
		if (userService.getUserEmail(email) == null) {
			return ResponseEntity.notFound().build();
		}
		
		Long userid = userService.userAuth().getId();

		HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(userid);

		final String uriGet = environment.getRequiredProperty("request.url");
		Long idRequest = null;
		
		if (restTemplate.exchange(uriGet, HttpMethod.GET, entity, String.class) != null) {
			ResponseEntity<String> result = restTemplate.exchange(uriGet, HttpMethod.GET, entity, String.class);
			
			User user = userService.getUserEmail(email);
			JSONArray jsonRequest = new JSONArray(result.getBody());
			Request request = null;
			
			for (Object o : jsonRequest) {
				if (ibn.equals(((JSONObject)o).getJSONObject("book").getLong("ibn")) && user.getId().equals(((JSONObject)o).getJSONObject("user").getLong("id"))) {
					try {
						request = initRequest.toObject((JSONObject)o);
					} catch (JSONException | ParseException e) {
						e.printStackTrace();
					}
				}
			}
			
			if (request != null) {
				idRequest = request.getId();
			}
			
			if (idRequest != null) {

				final String uri = environment.getRequiredProperty("request.url") + "delete/" + idRequest;
				restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);
				return ResponseEntity.ok().build();
			}
		}
		return ResponseEntity.notFound().build();
	}
	
	public Booking update(Booking booking, Long id)
	{
		Long userid = userService.userAuth().getId();

		HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(userid);

		final String uri = environment.getRequiredProperty("booking.url") + "update/" + id;

		HttpEntity<Booking> entityBooking = new HttpEntity<Booking>(booking, entity.getHeaders());
		
		if (restTemplate.exchange(uri, HttpMethod.POST, entity, String.class, entityBooking) != null) {

		    ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class, entityBooking);
			
			JSONObject json = new JSONObject(result.getBody());
			Booking booking1 = null;
			try {
				booking1 = init.toObject(json);
			} catch (ParseException | JSONException e) {
				e.printStackTrace();
			}
		    return booking1;
		}
		return null;
	}
	
	public void delete(Long id)
	{
		Long userid = userService.userAuth().getId();

		HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(userid);

		final String uri = environment.getRequiredProperty("booking.url") + "delete/" + id;
	    
	    restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);
	}
	
	public void extend(Long id)
	{
		Long userid = userService.userAuth().getId();

		HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(userid);

		final String uri = environment.getRequiredProperty("booking.url") + "extend/" + id;
	    
	    restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);
	}
	
	public void userClaim(Long id, Boolean userclaim)
	{
		Long userid = userService.userAuth().getId();

		HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(userid);
		HttpEntity<Boolean> entityBoolean = new HttpEntity<Boolean>(userclaim, entity.getHeaders());

		final String uri = environment.getRequiredProperty("booking.url") + "userclaim/" + id;
	    
	    restTemplate.exchange(uri, HttpMethod.PUT, entityBoolean, String.class);
	}
	
	public void changeRendering(Long id)
	{
		Long userid = userService.userAuth().getId();

		HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(userid);

		final String uri = environment.getRequiredProperty("booking.url") + "rendering/" + id;
	    
	    restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);
	}
	
	public void changeAccepted(Long id, Boolean accepted)
	{
		Long userid = userService.userAuth().getId();

		HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(userid);

		final String uri = environment.getRequiredProperty("booking.url") + "accepted/" + id;
	    
		HttpEntity<Boolean> entityBoolean = new HttpEntity<Boolean>(accepted, entity.getHeaders());
	    restTemplate.exchange(uri, HttpMethod.PUT, entityBoolean, String.class);
	}
}
