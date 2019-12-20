package com.shephertz.app42.gaming.multiplayer.client.events;

import com.shephertz.app42.gaming.api.storage.Storage;
import com.shephertz.app42.gaming.multiplayer.client.util.Util;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class ChatEvent {
	private String chat;
	private String sender;
	private String locId;
	private boolean isLocationLobby;
	private long time;

	public ChatEvent(String chat, String sender, String id, boolean fromLobby) {
		this.chat = chat;
		this.sender = sender;
		this.locId = id;
		this.isLocationLobby = fromLobby;
	}

	public static ChatEvent buildEvent(JSONObject notifyData) throws JSONException {
		String chat = notifyData.optString("chat", "");
		String sender = notifyData.optString("sender", "");
		String locId = notifyData.optString("id", "");
		boolean isLobby = notifyData.optBoolean("isLobby", false);
		long time = notifyData.optLong("chatTime", (new Date()).getTime());
		ChatEvent chatEvent = new ChatEvent(chat, sender, locId, isLobby);
		chatEvent.setTime(time);
		return chatEvent;
	}

	public static ArrayList<ChatEvent> buildChatHistoryList(ArrayList<Storage.JSONDocument> jsonDocList) {
		ArrayList<ChatEvent> chatEvents = new ArrayList<>();

		try {
			for (Storage.JSONDocument jsonDocument : jsonDocList) {
				String chatMessage = jsonDocument.getJsonDoc();
				JSONObject messageJson = new JSONObject(chatMessage);
				chatEvents.add(buildEvent(messageJson));
			}
		} catch (Exception var5) {
			Util.trace("buildGetChatHistoryData: " + var5.toString());
		}

		return chatEvents;
	}

	public String getLocationId() {
		return this.locId;
	}

	public boolean isLocationLobby() {
		return this.isLocationLobby;
	}

	public String getSender() {
		return this.sender;
	}

	public String getMessage() {
		return this.chat;
	}

	public long getTime() {
		return this.time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
