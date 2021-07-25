package fr.pierre.api.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import fr.pierre.api.entities.Book;
import fr.pierre.api.entities.InitBook;

@Service
public class BookService {
	
	@Autowired
	private Environment environment;
	@Autowired
	private UserService userService;
	@Autowired
	private HeaderInformationThrowerService headerInformationThrowerService;
	
	private InitBook init = new InitBook();
	private RestTemplate restTemplate = new RestTemplate();

	/**
	 * Get a book by his Ibn with a token
	 */
	public Book getBookByIbn(Long ibn)
	{
		Long id = userService.userAuth().getId();

		HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(id);

	    final String uri = environment.getRequiredProperty("book.url") + ibn;

		if (restTemplate.exchange(uri, HttpMethod.GET, entity, String.class) != null) {

		    ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		     
			JSONObject json = new JSONObject(result.getBody());
			Book book = null;
		    try {
				book = init.toObject(json);
			} catch (ParseException | JSONException e) {
				e.printStackTrace();
			}
		    return book;
		}
		return null;
	}
	
	public List<Book> getAllBook()
	{	     
	    final String uri = environment.getRequiredProperty("book.url");

		if (restTemplate.getForObject(uri, String.class) != null) {

		    String result = restTemplate.getForObject(uri, String.class);
		    JSONArray arrayJson = new JSONArray(result);
		    List<Book> books = new ArrayList<>();
		    for (int i = 0; i < arrayJson.length(); i++){
		    	JSONObject json = new JSONObject(arrayJson.get(i).toString());
		    	try {
			    	books.add(init.toObject(json));
				} catch (ParseException | JSONException e) {
					e.printStackTrace();
				}
		    }
		    return books;
		}
		return null;
	}
	
	public List<Book> getByAuthorOrTitle(String author, String title)
	{	    
		HttpEntity<String> entity = headerInformationThrowerService.SearchBookEntity(author, title);
		
	    final String uri = environment.getRequiredProperty("book.url") + "/search";

		if (restTemplate.exchange(uri, HttpMethod.GET, entity, String.class) != null) {

		    ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		    
		    JSONArray arrayJson = new JSONArray(result.getBody());
		    List<Book> books = new ArrayList<>();
		    for (int i = 0; i < arrayJson.length(); i++){
		    	JSONObject json = new JSONObject(arrayJson.get(i).toString());
		    	try {
			    	books.add(init.toObject(json));
				} catch (ParseException | JSONException e) {
					e.printStackTrace();
				}
		    }
		    return books;
		}
		return null;
	}
	
	public void create(Book book, String date)
	{
		Long userid = userService.userAuth().getId();

		HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(userid);

		final String uri = environment.getRequiredProperty("book.url") + "save";
	    
		try {
			Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
			book.setRelease_date(date1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		HttpEntity<Book> entityBook = new HttpEntity<Book>(book, entity.getHeaders());
		
	    restTemplate.exchange(uri, HttpMethod.PUT, entityBook, String.class);
	}
	
	public void update(Book book, Long ibn)
	{
		Long id = userService.userAuth().getId();

		HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(id);

		final String uri = environment.getRequiredProperty("book.url") + "update/" + ibn;

		HttpEntity<Book> entityBook = new HttpEntity<Book>(book, entity.getHeaders());
	    restTemplate.exchange(uri, HttpMethod.POST, entityBook, String.class);
	}
	
	public void delete(Long ibn)
	{
		Long id = userService.userAuth().getId();

		HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(id);

		final String uri = environment.getRequiredProperty("book.url") + "delete/" + ibn;
	    
	    restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);
	}
}
