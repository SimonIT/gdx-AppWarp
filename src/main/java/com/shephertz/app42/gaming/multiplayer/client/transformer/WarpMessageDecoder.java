package com.shephertz.app42.gaming.multiplayer.client.transformer;

import com.shephertz.app42.gaming.multiplayer.client.message.WarpMessage;
import com.shephertz.app42.gaming.multiplayer.client.message.WarpNotifyMessage;
import com.shephertz.app42.gaming.multiplayer.client.message.WarpResponseMessage;
import com.shephertz.app42.gaming.multiplayer.client.util.Util;

import java.nio.ByteBuffer;

public class WarpMessageDecoder {
	public static boolean needsMoreData(byte[] bytesToCheck, int startPos, int limit) {
		if (limit - startPos <= 7) {
			Util.trace("Header Missing : Bytes avail are " + (limit - startPos));
			return true;
		} else if (bytesToCheck[startPos] == 1 && limit - startPos <= 8) {
			Util.trace("Header Missing : Bytes avail are " + (limit - startPos));
			return true;
		} else {
			int payLoadSize;
			if (bytesToCheck[startPos] == 1) {
				payLoadSize = bytesToInteger(bytesToCheck, startPos + 5);
				if (limit - startPos < 9 + payLoadSize) {
					Util.trace(
							"Payload Missing: Bytes avail are " + (limit - startPos) + " payload size " + payLoadSize);
					return true;
				}
			} else {
				payLoadSize = bytesToInteger(bytesToCheck, startPos + 4);
				if (limit - startPos < 8 + payLoadSize) {
					Util.trace(
							"Payload Missing: Bytes avail are " + (limit - startPos) + " payload size " + payLoadSize);
					return true;
				}
			}

			return false;
		}
	}

	private static int bytesToInteger(byte[] bytes, int offset) {
		int value = 0;

		for (int i = 0; i < 4; ++i) {
			value = (value << 8) + (bytes[offset + i] & 255);
		}

		return value;
	}

	public static WarpMessage decode(ByteBuffer buf) {
		byte type = buf.get();
		byte updateType;
		byte reserved;
		byte payLoadType;
		if (type == 1) {
			updateType = buf.get();
			reserved = buf.get();
			payLoadType = buf.get();
			int payLoadSize = buf.getInt();
			byte[] payLoadBytes = new byte[payLoadSize];
			buf.get(payLoadBytes);
			return new WarpResponseMessage(type, reserved, updateType, payLoadType, payLoadType, payLoadSize,
					payLoadBytes);
		} else {
			updateType = buf.get();
			reserved = buf.get();
			payLoadType = buf.get();
			int payLoadSize = buf.getInt();
			byte[] payLoadBytes = new byte[payLoadSize];
			buf.get(payLoadBytes);
			return new WarpNotifyMessage(type, updateType, reserved, payLoadType, payLoadSize, payLoadBytes);
		}
	}
}
