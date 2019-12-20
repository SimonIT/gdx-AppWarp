package com.shephertz.app42.gaming.multiplayer.client.events;

import com.shephertz.app42.gaming.multiplayer.client.message.WarpResponseMessage;
import org.json.JSONException;
import org.json.JSONObject;

public class RoomEvent {
	private RoomData info;
	private byte result;

	public RoomEvent(RoomData info, byte result) {
		this.info = info;
		this.result = result;
	}

	public static RoomEvent buildRoomEvent(WarpResponseMessage msg) throws JSONException {
		RoomData roomInfo = null;
		if (msg.getResultCode() == 0) {
			JSONObject jsonRoom = new JSONObject(new String(msg.getPayLoad()));
			roomInfo = new RoomData(jsonRoom.getString("id"), jsonRoom.getString("owner"), jsonRoom.getString("name"),
					jsonRoom.getInt("maxUsers"));
		}

		return new RoomEvent(roomInfo, msg.getResultCode());
	}

	public RoomData getData() {
		return this.info;
	}

	public byte getResult() {
		return this.result;
	}
}
