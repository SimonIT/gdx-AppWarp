package com.shephertz.app42.gaming.api.storage;

import com.shephertz.app42.gaming.api.client.App42Exception;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Query {
	private JSONObject jsonObject;
	private JSONArray jsonArray;

	public Query(JSONObject jsonQuery) {
		this.jsonObject = jsonQuery;
	}

	public Query(JSONArray jsonQuery) {
		this.jsonArray = jsonQuery;
	}

	public String get() {
		try {
			return this.jsonObject != null
					? (new JSONArray(this.jsonObject.toString())).toString()
					: (new JSONArray(this.jsonArray.toString())).toString();
		} catch (JSONException var2) {
			throw new App42Exception(var2);
		}
	}

	public String getStr() {
		return this.jsonObject != null ? "[" + this.jsonObject.toString() + "]" : this.jsonArray.toString();
	}

	public Object getType() {
		return this.jsonObject != null ? this.jsonObject : this.jsonArray;
	}
}
