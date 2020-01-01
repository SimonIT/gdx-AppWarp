package com.shephertz.app42.gaming.multiplayer.client.message;

public class WarpRequestMessage extends WarpMessage {
	private byte requestType;
	private int sessionId;
	private int requestId;

	public WarpRequestMessage(byte requestType, int sessionId, int requestId, byte type, byte reserved,
			byte payLoadType, int payLoadSize, byte[] payLoad) {
		super(type, reserved, payLoadType, payLoadSize, payLoad);
		this.requestType = requestType;
		this.requestId = requestId;
		this.sessionId = sessionId;
	}

	public byte getRequestType() {
		return this.requestType;
	}

	public int getSessionId() {
		return this.sessionId;
	}

	public int getRequestId() {
		return this.requestId;
	}
}
