package de.SimonIT.gdxAppWarp.events;

public class MatchedRoomsEvent {
	private final byte result;
	private final RoomData[] roomData;

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
