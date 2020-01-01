package com.shephertz.app42.gaming.multiplayer.client.events;

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
