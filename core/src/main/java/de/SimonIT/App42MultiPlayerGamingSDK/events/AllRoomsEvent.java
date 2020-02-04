package de.SimonIT.App42MultiPlayerGamingSDK.events;

public class AllRoomsEvent {
	private byte result;
	private String[] roomIds;
	private int count;

	public AllRoomsEvent(byte result, String[] rooms) {
		this.roomIds = rooms;
		this.result = result;
	}

	public AllRoomsEvent(byte result, int roomCount) {
		this.count = roomCount;
		this.result = result;
	}

	public AllRoomsEvent(byte result, String[] rooms, int roomCount) {
		this.count = roomCount;
		this.roomIds = rooms;
		this.result = result;
	}

	public byte getResult() {
		return this.result;
	}

	public String[] getRoomIds() {
		return this.roomIds;
	}

	public int getRoomsCount() {
		return this.count;
	}
}
