package fr.pierre.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class HeaderInformationThrowerService {
	
	@Autowired
	public TokenService tokenService;
	
	public HttpEntity<String> UserTokenEntity(Long id) {
		
		String token = tokenService.getUserToken(id);
		String keyToken = "Bearer " + token;

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", keyToken);
		
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		
		return entity;
	}
	
	public HttpEntity<String> SearchBookEntity(String author, String title) {

		HttpHeaders headers = new HttpHeaders();
		headers.set("author", author);
		headers.set("title", title);
		
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		
		return entity;
	}
}
