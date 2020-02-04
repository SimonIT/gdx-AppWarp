package de.SimonIT.App42MultiPlayerGamingSDK.events;

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
