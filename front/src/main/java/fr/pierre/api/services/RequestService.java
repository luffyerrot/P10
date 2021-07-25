package fr.pierre.api.services;

import java.text.ParseException;
import java.util.ArrayList;
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

import fr.pierre.api.entities.InitRequest;
import fr.pierre.api.entities.Request;

@Service
public class RequestService {
	
	@Autowired
	private Environment environment;
	@Autowired
	private UserService userService;
	@Autowired
	private HeaderInformationThrowerService headerInformationThrowerService;
	
	private RestTemplate restTemplate = new RestTemplate();
	private InitRequest init = new InitRequest();
	
	public List<Request> getRequestByUserId(Long id)
	{
		Long userid = userService.userAuth().getId();

		HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(userid);

	    final String uri = environment.getRequiredProperty("request.url") + "user/" + id;

		if (restTemplate.exchange(uri, HttpMethod.GET, entity, String.class) != null) {

		    ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		     
		    JSONArray arrayJson = new JSONArray(result.getBody());
		    List<Request> requests = new ArrayList<>();
		    for (int i = 0; i < arrayJson.length(); i++){
		    	JSONObject json = new JSONObject(arrayJson.get(i).toString());
		    	try {
		    		requests.add(init.toObject(json));
				} catch (ParseException | JSONException e) {
					e.printStackTrace();
				}
		    }
		    return requests;
		}
		return null;
	}
	
	public List<Request> getRequestByBookIbn(Long ibn)
	{
		Long id = userService.userAuth().getId();

		HttpEntity<String> entity = headerInformationThrowerService.UserTokenEntity(id);

	    final String uri = environment.getRequiredProperty("request.url") + "book/" + ibn;

		if (restTemplate.exchange(uri, HttpMethod.GET, entity, String.class) != null) {

		    ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		     
		    JSONArray arrayJson = new JSONArray(result.getBody());
		    List<Request> requests = new ArrayList<>();
		    for (int i = 0; i < arrayJson.length(); i++){
		    	JSONObject json = new JSONObject(arrayJson.get(i).toString());
		    	try {
		    		requests.add(init.toObject(json));
				} catch (ParseException | JSONException e) {
					e.printStackTrace();
				}
		    }
		    return requests;
		}
		return null;
	}
	
	public List<Long> getIdByRequestUser(Long ibn)
	{
		List<Request> requests = getRequestByBookIbn(ibn);
		List<Long> ids = new ArrayList<>();
		
		if (requests != null) {
			for (int i = 0; i < requests.size(); i++) {
				ids.add(requests.get(i).getUser().getId());
			}
			if (!ids.isEmpty()) {
			    return ids;
			}
		}
		return null;
	}
}
