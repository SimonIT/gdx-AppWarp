package com.shephertz.app42.gaming.multiplayer.client.events;

public class ConnectEvent {
	private byte result;
	private int reasonCode;

	public ConnectEvent(byte result) {
		this.result = result;
		this.reasonCode = 0;
	}

	public ConnectEvent(byte result, int reasonCode) {
		this.result = result;
		this.reasonCode = reasonCode;
	}

	public byte getResult() {
		return this.result;
	}

	public int getReasonCode() {
		return this.reasonCode;
	}
}
