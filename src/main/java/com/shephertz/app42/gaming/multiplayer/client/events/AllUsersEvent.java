package com.shephertz.app42.gaming.multiplayer.client.events;

import com.shephertz.app42.gaming.multiplayer.client.message.WarpResponseMessage;
import org.json.JSONException;
import org.json.JSONObject;

public class AllUsersEvent {
	private byte result;
	private String[] userNames;
	private int count;

	public AllUsersEvent(byte result, String[] users) {
		this.userNames = users;
		this.result = result;
	}

	private AllUsersEvent(int userCount, byte resultCode) {
		this.count = userCount;
		this.result = resultCode;
	}

	public static AllUsersEvent buildAllUsersEvent(WarpResponseMessage msg) throws JSONException {
		String[] usernames = null;
		if (msg.getResultCode() == 0) {
			JSONObject jsonUsers = new JSONObject(new String(msg.getPayLoad()));
			usernames = jsonUsers.getString("names").split(";");
		}

		return new AllUsersEvent(msg.getResultCode(), usernames);
	}

	public static AllUsersEvent buildUsersCountEvent(WarpResponseMessage msg) throws JSONException {
		int userCount = 0;
		if (msg.getResultCode() == 0) {
			JSONObject jsonUsersCount = new JSONObject(new String(msg.getPayLoad()));
			userCount = jsonUsersCount.getInt("count");
		}

		return new AllUsersEvent(userCount, msg.getResultCode());
	}

	public byte getResult() {
		return this.result;
	}

	public String[] getUserNames() {
		return this.userNames;
	}

	public int getUsersCount() {
		return this.count;
	}
}
