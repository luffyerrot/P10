package fr.pierre.apirest.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InitBook {
	
	public Book toObject(JSONObject json) throws ParseException, JSONException {
		Book book = new Book();
		JSONArray jsonA = json.getJSONArray("copies");
		List<Copy> copies = new ArrayList<>();
		for (Object o : jsonA) {
			Long id = ((JSONObject)o).getLong("id");
			String etat = ((JSONObject)o).getString("etat");
			copies.add(new Copy(id, etat));
		}
		book.setCopies(copies);
		book.setIbn(json.getLong("ibn"));
		book.setAuthor(json.getString("author"));
		book.setPublisher(json.getString("publisher"));
		String date = json.getString("release_date").substring(0, 10);
		Date date1;
		date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
		book.setRelease_date(date1);
		book.setTitle(json.getString("title"));
		return book;
	}
	
	public Book toObjectWithRequest(JSONObject json) throws ParseException, JSONException {
		Book book = new Book();
		JSONArray jsonA = json.getJSONArray("copies");
		List<Copy> copies = new ArrayList<>();
		for (Object o : jsonA) {
			Long id = ((JSONObject)o).getLong("id");
			String etat = ((JSONObject)o).getString("etat");
			copies.add(new Copy(id, etat));
		}
		book.setCopies(copies);
		JSONArray jsonB = json.getJSONArray("requests");
		List<Request> requests = new ArrayList<>();
		for (Object f : jsonB) {
			Long id = ((JSONObject)f).getLong("id");
			String date2 = json.getString("release_date").substring(0, 10);
			Date release_dateR;
			release_dateR = new SimpleDateFormat("yyyy-MM-dd").parse(date2);
			JSONObject jsonU = ((JSONObject)f).getJSONObject("user");
			Long userId = jsonU.getLong("id");
			String userEmail = jsonU.getString("email");
			String userUsername = jsonU.getString("username");
			User user = new User(userId, userEmail, userUsername);
			Request request = new Request(id, release_dateR);
			request.setUser(user);
			requests.add(request);
		}
		book.setRequests(requests);
		book.setIbn(json.getLong("ibn"));
		book.setAuthor(json.getString("author"));
		book.setPublisher(json.getString("publisher"));
		String date = json.getString("release_date").substring(0, 10);
		Date date1;
		date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
		book.setRelease_date(date1);
		book.setTitle(json.getString("title"));
		return book;
	}
	
	public JSONObject toJson(Book book) {
		JSONObject json = new JSONObject();
		json.put("copy", "");
		json.put("ibn", book.getIbn());
		json.put("author", book.getAuthor());
		json.put("publisher", book.getPublisher());
		json.put("release_date", book.getRelease_date());
		json.put("title", book.getTitle());
		return json;
	}
}
