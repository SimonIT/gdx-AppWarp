package com.shephertz.app42.gaming.multiplayer.client.events;

public class MatchedRoomsEvent {
	private byte result;
	private RoomData[] roomData;

	public MatchedRoomsEvent(byte result, RoomData[] roomData) {
		this.roomData = roomData;
		this.result = result;
	}

	public byte getResult() {
		return this.result;
	}

	public RoomData[] getRoomsData() {
		return this.roomData;
	}
}
