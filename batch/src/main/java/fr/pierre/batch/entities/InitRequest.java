package fr.pierre.batch.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class InitRequest {
	
	public Request toObject(JSONObject json) throws ParseException, JSONException {
		Request request = new Request();
		request.setId(json.getLong("id"));
		String date = json.getString("release_date").substring(0, 10);
		Date date1;
		date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
		request.setRelease_date(date1);
		JSONObject jsonObjB = (JSONObject)json.get("book");
		Long ibn = jsonObjB.getLong("ibn");
		String title = jsonObjB.getString("title");
		String author = jsonObjB.getString("author");
		String publisher = jsonObjB.getString("publisher");
		request.setBook(new Book(ibn, title, author, publisher));
		JSONObject jsonObjU = (JSONObject)json.get("user");
		Long id = jsonObjU.getLong("id");
		String email = jsonObjU.getString("email");
		String username = jsonObjU.getString("username");
		request.setUser(new User(id, email, username));
		return request;
	}
	
	public JSONObject toJson(Request request) {
		JSONObject json = new JSONObject();
		json.put("book", "");
		json.put("user", "");
		json.put("id", request.getId());
		json.put("release_date", request.getRelease_date());
		return json;
	}
}
