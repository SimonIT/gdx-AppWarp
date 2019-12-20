package com.shephertz.app42.gaming.multiplayer.client.events;

import com.shephertz.app42.gaming.multiplayer.client.message.WarpResponseMessage;
import com.shephertz.app42.gaming.multiplayer.client.util.Util;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class LiveRoomInfoEvent extends RoomEvent {
	private String[] joinedUsers;
	private String customData;
	private HashMap<String, Object> properties;
	private HashMap<String, String> lockProperties;

	public LiveRoomInfoEvent(RoomData data, byte result, String[] users, String customData) {
		super(data, result);
		this.joinedUsers = users;
		this.customData = customData;
	}

	public LiveRoomInfoEvent(RoomData data, byte result, String[] users, String customData, String properties,
			String lockProperties) {
		super(data, result);
		this.joinedUsers = users;
		this.customData = customData;
		if (properties != null) {
			this.buildProperties(properties);
		}

		if (lockProperties != null) {
			this.buildLockProperties(lockProperties);
		}

	}

	public static LiveRoomInfoEvent buildLiveRoomEvent(WarpResponseMessage msg) throws JSONException {
		RoomData roomInfo = null;
		String customData = "";
		String properties = null;
		String lockProperties = null;
		String[] usernames = null;
		if (msg.getResultCode() == 0) {
			JSONObject jsonRoom = new JSONObject(new String(msg.getPayLoad()));
			roomInfo = new RoomData(jsonRoom.getString("id"), jsonRoom.getString("owner"), jsonRoom.getString("name"),
					jsonRoom.getInt("maxUsers"));
			String jsonUsernames = jsonRoom.getString("usernames");
			if (jsonUsernames.length() > 0) {
				usernames = jsonUsernames.split(";");
			}

			customData = jsonRoom.getString("data");
			properties = jsonRoom.getString("properties");
			lockProperties = jsonRoom.getString("lockProperties");
		}

		return new LiveRoomInfoEvent(roomInfo, msg.getResultCode(), usernames, customData, properties, lockProperties);
	}

	private void buildProperties(String input) {
		if (this.properties == null) {
			this.properties = new HashMap<>();
		}

		this.properties.clear();

		try {
			JSONObject jSONObject = new JSONObject(input);
			Iterator<String> it = jSONObject.keys();

			while (it.hasNext()) {
				String key = it.next();
				this.properties.put(key, jSONObject.get(key));
			}
		} catch (Exception var5) {
			Util.trace("buildProperties: " + var5);
		}

	}

	private void buildLockProperties(String input) {
		if (this.lockProperties == null) {
			this.lockProperties = new HashMap<>();
		}

		this.lockProperties.clear();

		try {
			JSONObject jSONObject = new JSONObject(input);
			Iterator<String> it = jSONObject.keys();

			while (it.hasNext()) {
				String key = it.next();
				this.lockProperties.put(key, jSONObject.get(key).toString());
			}
		} catch (Exception var5) {
			Util.trace("buildLockProperties: " + var5);
		}

	}

	public String[] getJoinedUsers() {
		return this.joinedUsers;
	}

	public String getCustomData() {
		return this.customData;
	}

	public HashMap<String, Object> getProperties() {
		return this.properties;
	}

	public HashMap<String, String> getLockProperties() {
		return this.lockProperties;
	}
}
