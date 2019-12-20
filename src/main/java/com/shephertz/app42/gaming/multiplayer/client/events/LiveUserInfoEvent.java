package com.shephertz.app42.gaming.multiplayer.client.events;

import com.shephertz.app42.gaming.multiplayer.client.message.WarpResponseMessage;
import org.json.JSONException;
import org.json.JSONObject;

public class LiveUserInfoEvent {
	private byte result;
	private String locationId;
	private String name;
	private String customData;
	private boolean isLocationLobby;
	private boolean isPaused;
	private boolean isActive;

	public LiveUserInfoEvent(byte result, String locId, String name, String custom, boolean fromLobby,
			boolean isPaused) {
		this.result = result;
		this.locationId = locId;
		this.name = name;
		this.customData = custom;
		this.isLocationLobby = fromLobby;
		this.isPaused = isPaused;
	}

	public LiveUserInfoEvent(byte result, String name, boolean isActive) {
		this.result = result;
		this.name = name;
		this.isActive = isActive;
	}

	public static LiveUserInfoEvent buildLiveUserInfoEvent(WarpResponseMessage msg) throws JSONException {
		String name = "";
		String locationId = "";
		String customData = "";
		boolean isPaused = false;
		boolean isLobby = false;
		if (msg.getResultCode() == 0) {
			JSONObject jsonUser = new JSONObject(new String(msg.getPayLoad()));
			name = jsonUser.getString("name");
			locationId = jsonUser.optString("locationId", "");
			customData = jsonUser.getString("custom");
			isLobby = jsonUser.has("isLobby");
			isPaused = jsonUser.has("isPaused");
		}

		return new LiveUserInfoEvent(msg.getResultCode(), locationId, name, customData, isLobby, isPaused);
	}

	public static LiveUserInfoEvent buildOnlineUserStatusEvent(WarpResponseMessage msg) throws JSONException {
		boolean isActive = false;
		String name = "";
		if (msg.getResultCode() == 0) {
			JSONObject jsonUser = new JSONObject(new String(msg.getPayLoad()));
			isActive = jsonUser.getBoolean("status");
			name = jsonUser.optString("username", "");
		}

		return new LiveUserInfoEvent(msg.getResultCode(), name, isActive);
	}

	public byte getResult() {
		return this.result;
	}

	public boolean isLocationLobby() {
		return this.isLocationLobby;
	}

	public String getLocationId() {
		return this.locationId;
	}

	public String getName() {
		return this.name;
	}

	public String getCustomData() {
		return this.customData;
	}

	public boolean isPaused() {
		return this.isPaused;
	}

	public boolean isActive() {
		return this.isActive;
	}
}
