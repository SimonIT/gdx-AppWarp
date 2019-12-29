package com.shephertz.app42.gaming.multiplayer.client.events;

import com.shephertz.app42.gaming.multiplayer.client.message.WarpResponseMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class MatchedRoomsEvent {
	private byte result;
	private RoomData[] roomData;

	public MatchedRoomsEvent(byte result, RoomData[] roomData) {
		this.roomData = roomData;
		this.result = result;
	}

	public static MatchedRoomsEvent buildMatchedRoomsEvent(WarpResponseMessage msg) throws JSONException {
		List<RoomData> roomsDataList = new ArrayList<>();
		if (msg.getResultCode() == 0 || msg.getResultCode() == 7) {
			JSONObject roomData = new JSONObject(new String(msg.getPayLoad()));
			Iterator<String> it = roomData.keys();

			while (it.hasNext()) {
				String key = it.next();
				JSONObject jsonRoom = (JSONObject) roomData.get(key);
				RoomData roomInfo = new RoomData(key, jsonRoom.getString("owner"), jsonRoom.getString("name"),
						jsonRoom.getInt("maxUsers"));
				roomsDataList.add(roomInfo);
			}
		}

		RoomData[] roomData = new RoomData[roomsDataList.size()];

		for (int i = 0; i < roomsDataList.size(); ++i) {
			roomData[i] = roomsDataList.get(i);
		}

		return new MatchedRoomsEvent(msg.getResultCode(), roomData);
	}

	public byte getResult() {
		return this.result;
	}

	public RoomData[] getRoomsData() {
		return this.roomData;
	}
}
