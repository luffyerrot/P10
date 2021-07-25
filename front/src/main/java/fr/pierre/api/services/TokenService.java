package fr.pierre.api.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TokenService {

	@Autowired
	private Environment environment;
	
	private HashMap<Long, String> userToken = new HashMap<Long, String>();
	private RestTemplate restTemplate = new RestTemplate();

	public String getAuthToken(Long id , String password)
	{
		HttpHeaders headers = new HttpHeaders();
		headers.set("password", password);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		
		final String uri = environment.getRequiredProperty("token.url") + "/authentication/" + id;
		
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		return result.getBody();
	}
	
	public Map<Long, String> getUsersToken() {
		return userToken;
	}

	public void addUserToken(Long id, String token) {
		
		if (userToken.get(id) != null) {
			userToken.replace(id, token);
		}
		userToken.put(id, token);
	}
	
	public String getUserToken(Long id) {
		return userToken.get(id);
	}
}
