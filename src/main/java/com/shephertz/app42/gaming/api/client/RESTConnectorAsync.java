package com.shephertz.app42.gaming.api.client;

import org.json.JSONObject;

import javax.naming.ConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;
import java.util.Set;

public class RESTConnectorAsync {
	private static final RESTConnectorAsync mInstance = null;
	public int TimeOut = 60000;
	private String baseURL;

	private RESTConnectorAsync() {
		this.baseURL = "https://api.shephertz.com/cloud/";
	}

	public static RESTConnectorAsync getInstance() throws ConfigurationException {
		return mInstance == null ? new RESTConnectorAsync() : mInstance;
	}

	public String executeDelete(String url, Hashtable<String, String> params, Hashtable<String, String> headerParams)
			throws Exception {
		String queryString = this.buildQueryString(params);
		String encodedUrl = URLEncoder.encode(url, "UTF-8");
		String uri = this.baseURL + encodedUrl.replace("+", "%20").replace("%2F", "/") + queryString;
		return this.getDeleteResponse(uri, headerParams);
	}

	public String executeGet(String url, Hashtable<String, String> params, Hashtable<String, String> headerParams)
			throws Exception {
		String response;
		String queryString = this.buildQueryString(params);
		String encodedUrl = URLEncoder.encode(url, "UTF-8");
		String uri = this.baseURL + encodedUrl.replace("+", "%20").replace("%2F", "/") + queryString;
		response = this.getResponse(uri, headerParams);
		return response;
	}

	public String executePost(String url, Hashtable<String, String> params, String bodyPayload,
			Hashtable<String, String> headerParams) throws Exception {
		String queryString = this.buildQueryString(params);
		String encodedUrl = URLEncoder.encode(url, "UTF-8");
		String uri = this.baseURL + encodedUrl.replace("+", "%20").replace("%2F", "/") + queryString;
		return this.getPostResponse(uri, headerParams, bodyPayload);
	}

	public String getPostResponse(String uri, Hashtable<String, String> headerParams, String bodyPayLoad)
			throws Exception {
		HttpURLConnection mUrlConnection = this.getHttpsUrlConnection(uri, headerParams, "POST");
		mUrlConnection.setDoOutput(true);
		if (bodyPayLoad != null) {
			DataOutputStream wr = new DataOutputStream(mUrlConnection.getOutputStream());
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, StandardCharsets.UTF_8));
			writer.write(bodyPayLoad);
			writer.close();
			wr.close();
		}

		return this.getRequestResponse(mUrlConnection);
	}

	public String getResponse(String uri, Hashtable<String, String> headerParams) throws Exception {
		HttpURLConnection mUrlConnection = this.getHttpsUrlConnection(uri, headerParams, "GET");
		return this.getRequestResponse(mUrlConnection);
	}

	public String buildQueryString(Hashtable<String, String> queryParams) throws UnsupportedEncodingException {
		StringBuilder queryString = new StringBuilder("?");
		if (queryParams != null) {
			Set<String> keys = queryParams.keySet();

			for (String key : keys) {
				String value = queryParams.get(key);
				queryString.append(URLEncoder.encode(key, "UTF-8")).append("=")
						.append(URLEncoder.encode(value, "UTF-8")).append("&");
			}
		}

		return queryString.toString();
	}

	public String getDeleteResponse(String uri, Hashtable<String, String> headerParams) throws Exception {
		HttpURLConnection mUrlConnection = this.getHttpsUrlConnection(uri, headerParams, "DELETE");
		mUrlConnection.setDoOutput(true);
		return this.getRequestResponse(mUrlConnection);
	}

	private HttpURLConnection getHttpsUrlConnection(String uri, Hashtable<String, String> headerParams,
			String requestType) throws IOException {
		URL obj = new URL(uri);
		HttpURLConnection mUrlConnection = (HttpURLConnection) obj.openConnection();
		mUrlConnection.setRequestMethod(requestType);
		mUrlConnection.setConnectTimeout(this.TimeOut);
		this.populateHeaders(mUrlConnection, headerParams);
		return mUrlConnection;
	}

	private void populateHeaders(HttpURLConnection request, Hashtable<String, String> headerParams) {
		request.setRequestProperty("Content-Type", "application/json");
		request.setRequestProperty("Accept", "application/json");
		Set<String> keys = headerParams.keySet();

		for (String key : keys) {
			String value = headerParams.get(key);
			request.setRequestProperty(key, value);
		}

	}

	private String getRequestResponse(HttpURLConnection mUrlConnection) throws Exception {
		InputStream inputStream = null;

		String var4;
		try {
			String responseStr;
			if (mUrlConnection.getResponseCode() != 200) {
				inputStream = mUrlConnection.getErrorStream();
				responseStr = this.buildResponseFromEntity(inputStream);
				this.handleException(mUrlConnection.getResponseCode(), responseStr);
			} else {
				inputStream = mUrlConnection.getInputStream();
				responseStr = this.buildResponseFromEntity(mUrlConnection.getInputStream());
			}

			var4 = responseStr;
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}

		}

		return var4;
	}

	private String buildResponseFromEntity(InputStream inputStaream) throws IllegalStateException, IOException {
		BufferedReader r = new BufferedReader(new InputStreamReader(inputStaream));
		StringBuilder total = new StringBuilder();

		String line;
		while ((line = r.readLine()) != null) {
			total.append(line);
		}

		return total.toString();
	}

	private void handleException(int responseCode, String responseStr) {
		JSONObject jsonObj;

		try {
			jsonObj = new JSONObject(responseStr);
		} catch (Exception var5) {
			throw new App42Exception("Can not parse String : " + responseCode + " Method Not Allowed");
		}

		if (responseCode != 404 && responseCode != 400 && responseCode != 401 && responseCode != 500) {
			if (responseCode == 413) {
				throw new App42Exception(responseStr, responseCode, 1413);
			} else if (responseCode == 403) {
				throw new App42Exception(responseStr, responseCode, 1403);
			} else {
				throw new App42Exception(responseStr, 500, 1500);
			}
		} else {
			throw new App42Exception(responseStr, responseCode,
					Integer.parseInt(jsonObj.getJSONObject("app42Fault").getString("appErrorCode")));
		}
	}
}
