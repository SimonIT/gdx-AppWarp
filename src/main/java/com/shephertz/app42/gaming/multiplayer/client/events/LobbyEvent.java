package com.shephertz.app42.gaming.multiplayer.client.events;

import com.shephertz.app42.gaming.multiplayer.client.message.WarpResponseMessage;
import org.json.JSONException;
import org.json.JSONObject;

public class LobbyEvent {
	private LobbyData info;
	private byte result;

	public LobbyEvent(LobbyData info, byte result) {
		this.info = info;
		this.result = result;
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

	public LobbyData getInfo() {
		return this.info;
	}

	public byte getResult() {
		return this.result;
	}
}
