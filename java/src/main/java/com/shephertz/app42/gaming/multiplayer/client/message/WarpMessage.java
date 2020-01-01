package com.shephertz.app42.gaming.multiplayer.client.message;

public class WarpMessage {
	private byte type;
	private byte reserved;
	private byte payLoadType;
	private int payLoadSize;
	private byte[] payLoad;

	public WarpMessage(byte type, byte reserved, byte payLoadType, int payLoadSize, byte[] payLoad) {
		this.type = type;
		this.reserved = reserved;
		this.payLoadType = payLoadType;
		this.payLoadSize = payLoadSize;
		this.payLoad = payLoad;
	}

	public byte[] getPayLoad() {
		return this.payLoad;
	}

	public void setPayLoad(byte[] payLoad) {
		this.payLoad = payLoad;
	}

	public int getPayLoadSize() {
		return this.payLoadSize;
	}

	public void setPayLoadSize(int payLoadSize) {
		this.payLoadSize = payLoadSize;
	}

	public byte getPayLoadType() {
		return this.payLoadType;
	}

	public void setPayLoadType(byte payLoadType) {
		this.payLoadType = payLoadType;
	}

	public byte getReserved() {
		return this.reserved;
	}

	public void setReserved(byte reserved) {
		this.reserved = reserved;
	}

	public byte getType() {
		return this.type;
	}

	public void setType(byte type) {
		this.type = type;
	}
}
