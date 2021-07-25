package fr.pierre.batch.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Token {

	private final String token;

	private RestTemplate restTemplate = new RestTemplate();
	
	public Token() {
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("password", "$2a$10$K1rDxIqpAISjDBtKHLR78.HEco2AqaC5Fw6Of7Zal8X39tqs8cHfK");
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		
		final String uri = "http://localhost:8080/token/authentication/7";
		ResponseEntity<String> token = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		
		this.token = token.getBody();
	}

	public String getToken() {
		return token;
	}
}
