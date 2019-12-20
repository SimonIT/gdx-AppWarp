package com.shephertz.app42.gaming.api.client;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class App42ResponseBuilder {
	public void buildObjectFromJSONTree(Object obj, JSONObject jsonObj) throws Exception {
		String[] names = JSONObject.getNames(jsonObj);

		for (String name : names) {
			Field field;

			try {
				field = obj.getClass().getDeclaredField(name);
				Object value = jsonObj.get(name);
				if (value.getClass().getCanonicalName() != null
						&& !value.getClass().getCanonicalName().equals("org.json.JSONObject.Null")) {
					if (field.getType().isInstance(new Date())) {
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
						Date dateObj = df.parse("" + value);
						field.set(obj, dateObj);
					} else if (field.getType().isInstance("")) {
						field.set(obj, "" + value);
					} else if (field.getType().isInstance(new BigDecimal("1.0"))) {
						field.set(obj, new BigDecimal("" + value));
					} else if (field.getType().isInstance(Integer.valueOf("1"))) {
						field.set(obj, Integer.valueOf("" + value));
					} else if (field.getType().isInstance(Boolean.TRUE)) {
						field.set(obj, Boolean.valueOf("" + value));
					}
				}
			} catch (NoSuchFieldException var12) {
			}
		}

	}

	public JSONObject getServiceJSONObject(String serviceName, String json) {
		JSONObject jsonObj = new JSONObject(json);
		JSONObject jsonObjApp42 = jsonObj.getJSONObject("app42");
		JSONObject jsonObjResponse = jsonObjApp42.getJSONObject("response");
		return jsonObjResponse.getJSONObject(serviceName);
	}

	public boolean isResponseSuccess(String json) {
		JSONObject jsonObj = new JSONObject(json);
		JSONObject jsonObjApp42 = jsonObj.getJSONObject("app42");
		JSONObject jsonObjResponse = jsonObjApp42.getJSONObject("response");
		return jsonObjResponse.getBoolean("success");
	}

	public int getTotalRecords(String json) {
		int totalRecords = -1;
		JSONObject jsonObj = new JSONObject(json);
		JSONObject jsonObjApp42 = jsonObj.getJSONObject("app42");
		JSONObject jsonObjResponse = jsonObjApp42.getJSONObject("response");
		if (jsonObjResponse != null && jsonObjResponse.has("totalRecords")) {
			totalRecords = jsonObjResponse.getInt("totalRecords");
		}

		return totalRecords;
	}

	public String[] getNames(JSONObject jo) {
		int length = jo.length();
		if (length == 0) {
			return null;
		} else {
			return jo.keySet().toArray(new String[0]);
		}
	}
}
