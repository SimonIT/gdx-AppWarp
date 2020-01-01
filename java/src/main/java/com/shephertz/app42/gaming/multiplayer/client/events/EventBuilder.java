package com.shephertz.app42.gaming.multiplayer.client.events;

import com.shephertz.app42.gaming.api.storage.Storage;
import com.shephertz.app42.gaming.multiplayer.client.message.WarpResponseMessage;
import com.shephertz.app42.gaming.multiplayer.client.util.Util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class EventBuilder {
	private EventBuilder() {
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

	public static AllUsersEvent buildAllUsersEvent(WarpResponseMessage msg) throws JSONException {
		String[] usernames = null;
		if (msg.getResultCode() == 0) {
			JSONObject jsonUsers = new JSONObject(new String(msg.getPayLoad()));
			usernames = jsonUsers.getString("names").split(";");
		}

		return new AllUsersEvent(msg.getResultCode(), usernames);
	}

	public static AllUsersEvent buildUsersCountEvent(WarpResponseMessage msg) throws JSONException {
		int userCount = 0;
		if (msg.getResultCode() == 0) {
			JSONObject jsonUsersCount = new JSONObject(new String(msg.getPayLoad()));
			userCount = jsonUsersCount.getInt("count");
		}

		return new AllUsersEvent(userCount, msg.getResultCode());
	}

	public static ChatEvent buildChatEvent(JSONObject notifyData) throws JSONException {
		String chat = notifyData.optString("chat", "");
		String sender = notifyData.optString("sender", "");
		String locId = notifyData.optString("id", "");
		boolean isLobby = notifyData.optBoolean("isLobby", false);
		long time = notifyData.optLong("chatTime", (new Date()).getTime());
		ChatEvent chatEvent = new ChatEvent(chat, sender, locId, isLobby);
		chatEvent.setTime(time);
		return chatEvent;
	}

	public static List<ChatEvent> buildChatHistoryList(List<Storage.JSONDocument> jsonDocList) {
		List<ChatEvent> chatEvents = new ArrayList<>();

		try {
			for (Storage.JSONDocument jsonDocument : jsonDocList) {
				String chatMessage = jsonDocument.getJsonDoc();
				JSONObject messageJson = new JSONObject(chatMessage);
				chatEvents.add(buildChatEvent(messageJson));
			}
		} catch (Exception var5) {
			Util.trace("buildGetChatHistoryData: " + var5.toString());
		}

		return chatEvents;
	}

	public static LiveRoomInfoEvent buildLiveRoomEvent(WarpResponseMessage msg) throws JSONException {
		RoomData roomInfo = null;
		String customData = "";
		String properties = null;
		String lockProperties = null;
		String[] usernames = null;
		if (msg.getResultCode() == 0) {
			JSONObject jsonRoom = new JSONObject(new String(msg.getPayLoad()));
			roomInfo = new RoomData(jsonRoom.getString("id"), jsonRoom.getString("owner"), jsonRoom.getString("name"),
					jsonRoom.getInt("maxUsers"));
			String jsonUsernames = jsonRoom.getString("usernames");
			if (jsonUsernames.length() > 0) {
				usernames = jsonUsernames.split(";");
			}

			customData = jsonRoom.getString("data");
			properties = jsonRoom.getString("properties");
			lockProperties = jsonRoom.getString("lockProperties");
		}

		return new LiveRoomInfoEvent(roomInfo, msg.getResultCode(), usernames, customData, buildProperties(properties), buildLockProperties(lockProperties));
	}

	public static LiveUserInfoEvent buildLiveUserInfoEvent(WarpResponseMessage msg) throws JSONException {
		String name = "";
		String locationId = "";
		String customData = "";
		boolean isPaused = false;
		boolean isLobby = false;
		if (msg.getResultCode() == 0) {
			JSONObject jsonUser = new JSONObject(new String(msg.getPayLoad()));
			name = jsonUser.getString("name");
			locationId = jsonUser.optString("locationId", "");
			customData = jsonUser.getString("custom");
			isLobby = jsonUser.has("isLobby");
			isPaused = jsonUser.has("isPaused");
		}

		return new LiveUserInfoEvent(msg.getResultCode(), locationId, name, customData, isLobby, isPaused);
	}

	public static LiveUserInfoEvent buildOnlineUserStatusEvent(WarpResponseMessage msg) throws JSONException {
		boolean isActive = false;
		String name = "";
		if (msg.getResultCode() == 0) {
			JSONObject jsonUser = new JSONObject(new String(msg.getPayLoad()));
			isActive = jsonUser.getBoolean("status");
			name = jsonUser.optString("username", "");
		}

		return new LiveUserInfoEvent(msg.getResultCode(), name, isActive);
	}

	public static LobbyEvent buildLobbyEvent(WarpResponseMessage msg) throws JSONException {
		LobbyData lobbyInfo = null;
		if (msg.getResultCode() == 0) {
			JSONObject jsonLobby = new JSONObject(new String(msg.getPayLoad()));
			lobbyInfo = new LobbyData(jsonLobby.getString("id"), jsonLobby.getString("owner"),
					jsonLobby.getString("name"), jsonLobby.getInt("maxUsers"), jsonLobby.getBoolean("isPrimary"));
		}

		return new LobbyEvent(lobbyInfo, msg.getResultCode());
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

	public static MoveEvent buildMoveEvent(JSONObject notifyData) {
		return new MoveEvent(notifyData.optString("sender"), notifyData.optString("moveData"), notifyData.optString("nextTurn"), notifyData.optString("id"));
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
					eventArray[i] = buildMoveEvent(moveArray.getJSONObject(i));
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

	public static RoomEvent buildRoomEvent(WarpResponseMessage msg) throws JSONException {
		RoomData roomInfo = null;
		if (msg.getResultCode() == 0) {
			JSONObject jsonRoom = new JSONObject(new String(msg.getPayLoad()));
			roomInfo = new RoomData(jsonRoom.getString("id"), jsonRoom.getString("owner"), jsonRoom.getString("name"),
					jsonRoom.getInt("maxUsers"));
		}

		return new RoomEvent(roomInfo, msg.getResultCode());
	}

	private static Map<String, Object> buildProperties(String input) {
		Map<String, Object> properties = new HashMap<>();

		try {
			JSONObject jSONObject = new JSONObject(input);
			Iterator<String> it = jSONObject.keys();

			while (it.hasNext()) {
				String key = it.next();
				properties.put(key, jSONObject.get(key));
			}
		} catch (Exception var5) {
			Util.trace("buildProperties: " + var5);
		}
		return properties;
	}

	private static Map<String, String> buildLockProperties(String input) {
		Map<String, String> lockProperties = new HashMap<>();

		try {
			JSONObject jSONObject = new JSONObject(input);
			Iterator<String> it = jSONObject.keys();

			while (it.hasNext()) {
				String key = it.next();
				lockProperties.put(key, jSONObject.get(key).toString());
			}
		} catch (Exception var5) {
			Util.trace("buildLockProperties: " + var5);
		}
		return lockProperties;
	}
}
