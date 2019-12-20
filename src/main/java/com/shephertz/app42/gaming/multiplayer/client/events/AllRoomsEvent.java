package com.shephertz.app42.gaming.multiplayer.client.events;

import com.shephertz.app42.gaming.multiplayer.client.message.WarpResponseMessage;
import org.json.JSONException;
import org.json.JSONObject;

public class AllRoomsEvent {
	private byte result;
	private String[] roomIds;
	private int count;

	public AllRoomsEvent(byte result, String[] rooms) {
		this.roomIds = rooms;
		this.result = result;
	}

	public AllRoomsEvent(byte result, int roomCount) {
		this.count = roomCount;
		this.result = result;
	}

	public static AllRoomsEvent buildAllRoomsEvent(WarpResponseMessage msg) throws JSONException {
		String[] roomIds = null;
		if (msg.getResultCode() == 0) {
			JSONObject jsonRooms = new JSONObject(new String(msg.getPayLoad()));
			String jsonRoomIds = jsonRooms.getString("ids");
			if (jsonRoomIds.length() > 0) {
				roomIds = jsonRoomIds.split(";");
			}
		}

		return new AllRoomsEvent(msg.getResultCode(), roomIds);
	}

	public static AllRoomsEvent buildRoomsCountEvent(WarpResponseMessage msg) throws JSONException {
		int roomsCount = 0;
		if (msg.getResultCode() == 0) {
			JSONObject jsonRoomsCount = new JSONObject(new String(msg.getPayLoad()));
			roomsCount = jsonRoomsCount.getInt("count");
		}

		return new AllRoomsEvent(msg.getResultCode(), roomsCount);
	}

	public byte getResult() {
		return this.result;
	}

	public String[] getRoomIds() {
		return this.roomIds;
	}

	public int getRoomsCount() {
		return this.count;
	}
}
