package fr.pierre.api.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import fr.pierre.api.entities.Copy;
import fr.pierre.api.entities.InitCopy;
import fr.pierre.api.entities.Book;

@Service
public class CopyService {
	
	@Autowired
	private Environment environment;
	@Autowired
	private UserService userService;
	@Autowired
	private HeaderInformationThrowerService headerInformationThrowerService;
	
	private InitCopy init = new InitCopy();
	private RestTemplate restTemplate = new RestTemplate();

	public Copy getCopyId(Long id)
	{
		Long userId = userService.userAuth().getId();

		HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(userId);

	    final String uri = environment.getRequiredProperty("copy.url") + id;

		if (restTemplate.exchange(uri, HttpMethod.GET, entity, String.class) != null) {

		    ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		     
			JSONObject json = new JSONObject(result.getBody());
			Copy copy = null;
			copy = init.toObject(json);
		    return copy;
		}
		return null;
	}
	
	public List<Copy> getAllCopy()
	{
		Long userId = userService.userAuth().getId();

		HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(userId);

	    final String uri = environment.getRequiredProperty("copy.url");

		if (restTemplate.exchange(uri, HttpMethod.GET, entity, String.class) != null) {

		    ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		     
		    JSONArray arrayJson = new JSONArray(result.getBody());
		    List<Copy> copies = new ArrayList<>();
		    for (int i = 0; i < arrayJson.length(); i++){
		    	JSONObject json = new JSONObject(arrayJson.get(i).toString());
		    	copies.add(init.toObject(json));
		    }
		    return copies;
		}
		return null;
	}
	
	public void create(Copy copy, Book book)
	{
		Long userId = userService.userAuth().getId();

		HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(userId);

		final String uri = environment.getRequiredProperty("copy.url") + "save";
	     
		copy.setBook(book);
		HttpEntity<Copy> entityCopy = new HttpEntity<Copy>(copy, entity.getHeaders());
	    restTemplate.exchange(uri, HttpMethod.PUT, entityCopy, String.class);
	}
	
	public void update(Copy copy, Long id)
	{
		Long userId = userService.userAuth().getId();

		HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(userId);

		final String uri = environment.getRequiredProperty("copy.url") + "update/" + id;

		HttpEntity<Copy> entityCopy = new HttpEntity<Copy>(copy, entity.getHeaders());
	    restTemplate.exchange(uri, HttpMethod.POST, entityCopy, String.class);
	}
	
	public void delete(Long id)
	{
		Long userId = userService.userAuth().getId();

		HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(userId);

		final String uri = environment.getRequiredProperty("copy.url") + "delete/" + id;
	    
	    restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);
	}
	
	public boolean freeForBooking(Long id) {

		Long userId = userService.userAuth().getId();

		HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(userId);

		final String uri = environment.getRequiredProperty("copy.url") + "isfull/" + id ;

		if (restTemplate.exchange(uri, HttpMethod.GET, entity, String.class) != null) {
			
			ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
			if (result.getBody().contains("true")) return true;
		}
		return false;
	}
}
