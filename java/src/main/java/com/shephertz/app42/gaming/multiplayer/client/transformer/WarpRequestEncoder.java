package com.shephertz.app42.gaming.multiplayer.client.transformer;

import com.shephertz.app42.gaming.multiplayer.client.message.WarpRequestMessage;

import java.nio.ByteBuffer;

public class WarpRequestEncoder {
	public static ByteBuffer encode(WarpRequestMessage msg) {
		ByteBuffer buf = ByteBuffer.allocate(16 + msg.getPayLoadSize());
		buf.put(msg.getType());
		buf.put(msg.getRequestType());
		buf.putInt(msg.getSessionId());
		buf.putInt(msg.getRequestId());
		buf.put(msg.getReserved());
		buf.put(msg.getPayLoadType());
		buf.putInt(msg.getPayLoadSize());
		if (msg.getPayLoadSize() > 0) {
			buf.put(msg.getPayLoad());
		}

		return buf;
	}
}
