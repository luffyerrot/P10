package fr.pierre.batch.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import fr.pierre.batch.util.Token;

public class RequestWriter implements ItemWriter<Long>{
	
	Token token = new Token();
	
	private RestTemplate restTemplate = new RestTemplate();
		
	@Override
	public void write(List<? extends Long> items) throws Exception {
		if (!items.equals(null) || !items.isEmpty()) {

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + token.getToken());
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			
			for (int i = 0; i < items.size(); i++) {
				
				Long id = items.get(i);
				final String uriDelete = "http://localhost:8080/request/delete/" + id;
				restTemplate.exchange(uriDelete, HttpMethod.DELETE, entity, String.class);
			}
		}
	}
}
