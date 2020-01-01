package com.shephertz.app42.gaming.api.client;

import com.shephertz.app42.gaming.multiplayer.client.util.Util;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public class App42Service {
	protected Map<String, String> otherMetaHeaders = new HashMap<>();

	protected Hashtable<String, String> populateSignParams(String apiKey) throws JSONException {
		Hashtable<String, String> params = new Hashtable<>();
		params.put("apiKey", apiKey);
		params.put("version", "1.0");
		params.put("timeStamp", Util.getUTCFormattedTimestamp());
		return params;
	}

	public void setOtherMetaHeaders(Map<String, String> otherMetaHeaders) {
		this.otherMetaHeaders = otherMetaHeaders;
	}

	protected Hashtable<String, String> populateMetaHeaderParams() {
		Hashtable<String, String> params = new Hashtable<>();
		params.put("SDKName", "Java");
		if (this.otherMetaHeaders.size() > 0) {
			Set<String> keySet = this.otherMetaHeaders.keySet();

			for (String key : keySet) {
				String value = this.otherMetaHeaders.get(key);
				if (key != null && !key.equals("") && value != null && !value.equals("")) {
					params.put(key, value);
				}
			}
		}

		return params;
	}
}
