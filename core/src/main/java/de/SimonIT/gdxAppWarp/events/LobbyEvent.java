package de.SimonIT.gdxAppWarp.events;

public class LobbyEvent {
	private final LobbyData info;
	private final byte result;

	public LobbyEvent(LobbyData info, byte result) {
		this.info = info;
		this.result = result;
	}

	public LobbyData getInfo() {
		return this.info;
	}

	public byte getResult() {
		return this.result;
	}
}
