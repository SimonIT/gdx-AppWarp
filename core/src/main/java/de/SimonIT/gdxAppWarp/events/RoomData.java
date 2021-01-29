package de.SimonIT.gdxAppWarp.events;

public class RoomData {
	private final String roomId;
	private final String owner;
	private final int maxUsers;
	private final String name;

	public RoomData(String id, String owner, String name, int max) {
		this.roomId = id;
		this.owner = owner;
		this.name = name;
		this.maxUsers = max;
	}

	public String getId() {
		return this.roomId;
	}

	public String getRoomOwner() {
		return this.owner;
	}

	public String getName() {
		return this.name;
	}

	public int getMaxUsers() {
		return this.maxUsers;
	}
}
