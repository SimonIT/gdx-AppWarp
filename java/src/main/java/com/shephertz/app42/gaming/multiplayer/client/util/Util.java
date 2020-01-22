package com.shephertz.app42.gaming.multiplayer.client.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.TimeUtils;
import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

public class Util {
	public static String WarpServerHost;
	public static String userName = "";
	public static String geo = "";
	public static boolean TRACE_ENABLED = false;
	public static int RECOVERY_ALLOWANCE_TIME = 0;

	static String sortAndConvertTableToString(Hashtable<String, String> table) {
		Vector<String> v = new Vector<>(table.keySet());
		Collections.sort(v);
		StringBuilder requestString = new StringBuilder();
		Enumeration<String> e = v.elements();

		while (e.hasMoreElements()) {
			String key = e.nextElement();
			String val = table.get(key);
			requestString.append(key);
			requestString.append(val);
		}

		return requestString.toString();
	}

	public static String calculateSignature(String apiKey, String version, String user, String timeStamp,
											String secretKey) {
		Hashtable<String, String> params = new Hashtable<>();
		params.put("apiKey", apiKey);
		params.put("version", version);
		params.put("timeStamp", timeStamp);
		if (user != null) params.put("user", user);
		return sign(secretKey, params);
	}

	public static String calculateSignature(String apiKey, String version, String timeStamp, String secretKey) {
		return calculateSignature(apiKey, version, null, timeStamp, secretKey);
	}

	public static String sign(String secretKey, Hashtable<String, String> params) {
		try {
			String sortedParams = sortAndConvertTableToString(params);
			String signature = computeHmac(sortedParams, secretKey);
			return URLEncoder.encode(signature);
		} catch (Exception var4) {
			trace(var4.getMessage());
			return null;
		}
	}

	public static String computeHmac(String baseString, String key)
			throws NoSuchAlgorithmException, InvalidKeyException, IllegalStateException {
		Mac mac = Mac.getInstance("HmacSHA1");
		SecretKeySpec secret = new SecretKeySpec(key.getBytes(), mac.getAlgorithm());
		mac.init(secret);
		byte[] digest = mac.doFinal(baseString.getBytes());
		return new String(Base64Coder.encode(digest));
	}

	public static String getUTCFormattedTimestamp() {
		return getUTCFormattedTimestamp(new Date());
	}

	public static String getUTCFormattedTimestamp(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		return df.format(date);
	}

	public static int generateLocalUdpPort() {
		Random randomGenerator = new Random();
		return randomGenerator.nextInt(100) + 12345;
	}

	public static JSONObject getJsonObjectFromHashtable(Map<String, Object> properties) {
		JSONObject jsonObject = new JSONObject();

		try {

			for (Entry<String, Object> entry : properties.entrySet()) {
				jsonObject.put(entry.getKey(), entry.getValue());
			}
		} catch (Exception var4) {
			trace(var4.getMessage());
		}

		return jsonObject;
	}

	public static Map<String, Object> getHashMapFromProperties(String input) {
		Map<String, Object> properties = new HashMap<>();

		try {
			JSONObject jSONObject = new JSONObject(input);
			Iterator<String> it = jSONObject.keys();

			while (it.hasNext()) {
				String key = it.next();
				properties.put(key, jSONObject.get(key));
			}

			return properties;
		} catch (Exception var5) {
			trace(var5.getMessage());
			return null;
		}
	}

	public static void trace(String message) {
		if (TRACE_ENABLED) {
			Gdx.app.error("AppWarpTrace", TimeUtils.millis() + ":" + message);
		}
	}
}
