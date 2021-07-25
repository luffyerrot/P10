package fr.pierre.batch.entities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class InitUser {

	public User toObject(JSONObject json) {
		User user = new User();
		user.setBookings(null);
		List<Role> roles = new ArrayList<>();
		JSONArray jsonArray = (JSONArray)json.get("roles");
		for (Object o : jsonArray) {
			Long id = ((JSONObject)o).getLong("id");
			String name = ((JSONObject)o).getString("name");
			roles.add(new Role(id, name, null));
		}
		user.setRoles(roles);
		user.setId(json.getLong("id"));
		user.setUsername(json.getString("username"));
		user.setEmail(json.getString("email"));
		user.setPassword(json.getString("password"));
		return user;
	}
	
	public JSONObject toJson(User user) {
		JSONObject json = new JSONObject();
		json.put("role", "");
		json.put("booking", "");
		json.put("id", user.getId());
		json.put("username", user.getUsername());
		json.put("email", user.getEmail());
		json.put("password", user.getPassword());
		return json;
	}
}
