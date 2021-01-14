package de.SimonIT.gdxAppWarp.events;

public class RoomData {
	private String roomId;
	private String owner;
	private int maxUsers;
	private String name;

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
