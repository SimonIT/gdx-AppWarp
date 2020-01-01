package com.shephertz.app42.gaming.multiplayer.client;

import com.shephertz.app42.gaming.multiplayer.client.util.Util;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestConnector {
	private final String KEYADDRESS = "address";
	WarpClient theGame;

	public RestConnector() {
		try {
			this.theGame = JavaWarpClient.getInstance();
		} catch (Exception var2) {
			var2.printStackTrace(System.err);
		}

	}

	public void fetchHostIp(final String baseUrl, final String apiKey, final String geo) {
		(new Thread(() -> {
			try {
				byte lookUpStatus = RestConnector.this.getResultStatus(baseUrl, apiKey, geo);
				if (RestConnector.this.theGame != null) {
					RestConnector.this.theGame.onLookUpServer(lookUpStatus);
				}
			} catch (Exception var2) {
				var2.printStackTrace(System.err);
				RestConnector.this.theGame.onLookUpServer((byte) 5);
			}

		})).start();
	}

	private byte getResultStatus(String baseUrl, String apiKey, String geo) {
		try {
			String response = this.executeGet(this.buildUrl(baseUrl, apiKey, geo));
			JSONObject jsonResult = new JSONObject(response);
			if (jsonResult.get("address").toString().trim() != null
					&& !jsonResult.get("address").toString().trim().equals("")) {
				Util.WarpServerHost = jsonResult.get("address").toString().trim();
				return 0;
			} else {
				return 1;
			}
		} catch (FileNotFoundException var6) {
			Util.trace(var6.toString());
			return 1;
		} catch (Exception var7) {
			return 5;
		}
	}

	private String buildUrl(String basUrl, String apiKey, String geo) {
		return geo != null && geo.length() > 0 ? basUrl + "?api=" + apiKey + "&geo=" + geo : basUrl + "?api=" + apiKey;
	}

	private String executeGet(String urlStr) throws Exception {
		InputStream in = null;
		HttpURLConnection connection = null;

		try {
			URL url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Language", "en-US");
			connection.setUseCaches(false);
			connection.setDoInput(true);
			in = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			StringBuilder sb = new StringBuilder();

			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			return sb.toString();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}

			if (in != null) {
				in.close();
			}

		}
	}
}
