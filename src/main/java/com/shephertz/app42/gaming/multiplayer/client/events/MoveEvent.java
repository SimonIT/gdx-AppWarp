package com.shephertz.app42.gaming.multiplayer.client.events;

import com.shephertz.app42.gaming.multiplayer.client.message.WarpResponseMessage;
import com.shephertz.app42.gaming.multiplayer.client.util.Util;
import org.json.JSONArray;
import org.json.JSONObject;

public class MoveEvent {
	private String sender;
	private String moveData;
	private String nextTurn;
	private String roomId;

	public MoveEvent() {
	}

	public MoveEvent(String sender, String moveData, String nextTurn, String roomId) {
		this.sender = sender;
		this.moveData = moveData;
		this.nextTurn = nextTurn;
		this.roomId = roomId;
	}

	public static MoveEvent buildEvent(JSONObject notifyData) {
		MoveEvent event = new MoveEvent();
		event.roomId = notifyData.optString("id");
		event.moveData = notifyData.optString("moveData");
		event.nextTurn = notifyData.optString("nextTurn");
		event.sender = notifyData.optString("sender");
		return event;
	}

	public static MoveEvent[] buildMoveHistoryArray(WarpResponseMessage msg) {
		MoveEvent[] eventArray = null;

		try {
			String message = new String(msg.getPayLoad());
			JSONObject responseJson = new JSONObject(message);
			JSONArray moveArray = responseJson.getJSONArray("history");
			if (moveArray != null) {
				eventArray = new MoveEvent[moveArray.length()];

				for (int i = 0; i < moveArray.length(); ++i) {
					eventArray[i] = buildEvent(moveArray.getJSONObject(i));
				}
			}
		} catch (Exception var6) {
			Util.trace("buildGetMoveHistoryData: " + var6.toString());
		}

		return eventArray;
	}

	public static boolean getGameStatus(WarpResponseMessage msg) {
		boolean gameStatus = false;

		try {
			String message = new String(msg.getPayLoad());
			JSONObject responseJson = new JSONObject(message);
			gameStatus = responseJson.optBoolean("gameStatus", false);
		} catch (Exception var4) {
			Util.trace("getGameStatus: " + var4.toString());
		}

		return gameStatus;
	}

	public String getSender() {
		return this.sender;
	}

	public String getMoveData() {
		return this.moveData;
	}

	public String getNextTurn() {
		return this.nextTurn;
	}

	public String getRoomId() {
		return this.roomId;
	}
}
