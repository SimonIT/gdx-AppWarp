package com.shephertz.app42.gaming.multiplayer.client.message;

public class WarpResponseMessage extends WarpMessage {
	private byte resultCode;
	private byte requestType;

	public WarpResponseMessage(byte type, byte reserved, byte payLoadType, int payLoadSize, byte[] payLoad) {
		super(type, reserved, payLoadType, payLoadSize, payLoad);
	}

	public WarpResponseMessage(byte type, byte resultCode, byte requestType, byte reserved, byte payLoadType,
			int payLoadSize, byte[] payLoad) {
		super(type, reserved, payLoadType, payLoadSize, payLoad);
		this.resultCode = resultCode;
		this.requestType = requestType;
	}

	public int getRequestType() {
		return this.requestType;
	}

	public void setRequestType(byte requestType) {
		this.requestType = requestType;
	}

	public byte getResultCode() {
		return this.resultCode;
	}

	public void setResultCode(byte resultCode) {
		this.resultCode = resultCode;
	}
}
