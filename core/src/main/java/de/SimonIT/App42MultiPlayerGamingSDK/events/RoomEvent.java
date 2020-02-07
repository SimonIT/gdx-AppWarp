package de.SimonIT.App42MultiPlayerGamingSDK.events;

import org.jetbrains.annotations.Nullable;

public class RoomEvent {
	@Nullable
	private RoomData info;
	private byte result;

	public RoomEvent(@Nullable RoomData info, byte result) {
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
