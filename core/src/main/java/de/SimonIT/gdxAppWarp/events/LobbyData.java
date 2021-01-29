package de.SimonIT.gdxAppWarp.events;

public class LobbyData extends RoomData {
	private final boolean isPrimary;

	public LobbyData(String id, String owner, String name, int max, boolean isPrimary) {
		super(id, owner, name, max);
		this.isPrimary = isPrimary;
	}

	public boolean isPrimary() {
		return this.isPrimary;
	}
}
