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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import fr.pierre.api.entities.InitUser;
import fr.pierre.api.entities.User;

@Service
public class UserService {

	@Autowired
	private Environment environment;
	@Autowired
	private HeaderInformationThrowerService headerInformationThrowerService;
	
	private RestTemplate restTemplate = new RestTemplate();
	private InitUser init = new InitUser();
	
	public User getUserId(Long id)
	{
		Long userId = userAuth().getId();

		HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(userId);

	    final String uri = environment.getRequiredProperty("user.url") + id;

		try {

		    ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		    
			JSONObject json = new JSONObject(result.getBody());
			User user = null;
			user = init.toObject(json);
		    return user;
		} catch (HttpClientErrorException e) {
			return null;
		}
	}
	
	public User getUserEmail(String mail)
	{
	    final String uri = environment.getRequiredProperty("user.url") + "find/" + mail;

	    try {

		    ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, null, String.class);
		    
			User user = init.toObject((JSONObject)new JSONObject(result.getBody()));
		    return user;
	    } catch (HttpClientErrorException e) {
			return null;
		}
	}
	
	public List<User> getAllUser()
	{
		Long userId = userAuth().getId();

		HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(userId);

	    final String uri = environment.getRequiredProperty("user.url");

		try {

		    ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		     
		    JSONArray arrayJson = new JSONArray(result.getBody());
		    List<User> users = new ArrayList<>();
		    for (int i = 0; i < arrayJson.length(); i++){
		    	JSONObject json = new JSONObject(arrayJson.get(i).toString());
		    	users.add(init.toObject(json));
		    }
		    return users;
		} catch (HttpClientErrorException e) {
			return null;
		}
	}
	
	public void create(User user)
	{
		if (user != null) {
			final String uri = environment.getRequiredProperty("user.url") + "save";

		    restTemplate.put(uri, user);
		}
	}
	
	public User update(User user, Long id)
	{
		Long userId = userAuth().getId();

		HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(userId);

		final String uri = environment.getRequiredProperty("user.url") + "update/" + id;

		HttpEntity<User> entityUser = new HttpEntity<User>(user, entity.getHeaders());
		
		try {

			HttpEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, entityUser, String.class);
			
			JSONObject json = new JSONObject(result.getBody());
			User user1 = null;
			user1 = init.toObject(json);
		    return user1;
		} catch (HttpClientErrorException e) {
			return null;
		}
	}
	
	public void delete(Long id)
	{
		Long userId = userAuth().getId();

		HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(userId);

		final String uri = environment.getRequiredProperty("user.url") + "delete/" + id;
	    
		restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);
	}
	
	public User userAuth() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		if (name == "anonymousUser") {
			return null;
		}
		
		try {

			final String uriUser = environment.getRequiredProperty("user.url") + "find/" + name;
			
		    String resultUser = restTemplate.getForObject(uriUser, String.class);

			JSONObject jsonU = new JSONObject(resultUser);
			User user = init.toObject(jsonU);
			
			return user;
		} catch (HttpClientErrorException e) {

			return null;
		}
	}
}
