package com.shephertz.app42.gaming.multiplayer.client.events;

public class LobbyEvent {
	private LobbyData info;
	private byte result;

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
