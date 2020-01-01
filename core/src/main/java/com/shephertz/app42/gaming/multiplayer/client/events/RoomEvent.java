package com.shephertz.app42.gaming.multiplayer.client.events;

public class RoomEvent {
	private RoomData info;
	private byte result;

	public RoomEvent(RoomData info, byte result) {
		this.info = info;
		this.result = result;
	}

	public RoomData getData() {
		return this.info;
	}

	public byte getResult() {
		return this.result;
	}
}
