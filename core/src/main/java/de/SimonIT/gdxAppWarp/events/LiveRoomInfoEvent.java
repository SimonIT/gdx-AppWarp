package de.SimonIT.gdxAppWarp.events;

import java.util.Map;

public class LiveRoomInfoEvent extends RoomEvent {
	private final String[] joinedUsers;
	private final String customData;
	private Map<String, Object> properties;
	private Map<String, String> lockProperties;

	public LiveRoomInfoEvent(RoomData data, byte result, String[] users, String customData) {
		super(data, result);
		this.joinedUsers = users;
		this.customData = customData;
	}

	public LiveRoomInfoEvent(RoomData data, byte result, String[] users, String customData,
							 Map<String, Object> properties, Map<String, String> lockProperties) {
		super(data, result);
		this.joinedUsers = users;
		this.customData = customData;
		this.properties = properties;
		this.lockProperties = lockProperties;
	}

	public String[] getJoinedUsers() {
		return this.joinedUsers;
	}

	public String getCustomData() {
		return this.customData;
	}

	public Map<String, Object> getProperties() {
		return this.properties;
	}

	public Map<String, String> getLockProperties() {
		return this.lockProperties;
	}
}
