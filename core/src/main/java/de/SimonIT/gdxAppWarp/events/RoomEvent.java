package de.SimonIT.gdxAppWarp.events;

import com.badlogic.gdx.utils.Null;

public class RoomEvent {
	@Null
	private RoomData info;
	private byte result;

	public RoomEvent(@Null RoomData info, byte result) {
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
