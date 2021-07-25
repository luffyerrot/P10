package fr.pierre.batch.entities;

import org.json.JSONObject;

public class InitCopy {

	public Copy toObject(JSONObject json) {
		Copy copy = new Copy();
		JSONObject jsonObj = (JSONObject)json.get("book");
		Long ibn = jsonObj.getLong("ibn");
		String title = jsonObj.getString("title");
		String author = jsonObj.getString("author");
		String publisher = jsonObj.getString("publisher");
		copy.setBook(new Book(ibn, title, author, publisher));
		copy.setBookings(null);
		copy.setId(json.getLong("id"));
		copy.setEtat(json.getString("etat"));
		return copy;
	}
	
	public JSONObject toJson(Copy copy) {
		JSONObject json = new JSONObject();
		json.put("book", "");
		json.put("booking", "");
		json.put("id", copy.getId());
		json.put("etat", copy.getEtat());
		return json;
	}
}
