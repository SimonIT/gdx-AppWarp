package com.shephertz.app42.gaming.multiplayer.client.message;

public class WarpNotifyMessage extends WarpMessage {
	private byte updateType;

	public WarpNotifyMessage(byte type, byte reserved, byte payLoadType, int payLoadSize, byte[] payLoad) {
		super(type, reserved, payLoadType, payLoadSize, payLoad);
	}

	public WarpNotifyMessage(byte type, byte updateType, byte reserved, byte payLoadType, int payLoadSize,
			byte[] payLoad) {
		super(type, reserved, payLoadType, payLoadSize, payLoad);
		this.updateType = updateType;
	}

	public byte getUpdateType() {
		return this.updateType;
	}

	public void setUpdateType(byte updateType) {
		this.updateType = updateType;
	}
}
