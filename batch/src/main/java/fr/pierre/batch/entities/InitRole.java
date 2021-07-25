package fr.pierre.batch.entities;

import org.json.JSONObject;

public class InitRole {

	public Role toObject(JSONObject json) {
		Role role = new Role(json.getLong("id"), json.getString("name"), null);
		return role;
	}
	
	public JSONObject toJson(Role role) {
		JSONObject json = new JSONObject();
		json.put("user", "");
		json.put("id", role.getId());
		json.put("name", role.getName());
		return json;
	}
}
