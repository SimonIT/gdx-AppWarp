package com.shephertz.app42.gaming.multiplayer.client.listener;

import com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LobbyEvent;

public interface LobbyRequestListener {
	void onJoinLobbyDone(LobbyEvent var1);

	void onLeaveLobbyDone(LobbyEvent var1);

	void onSubscribeLobbyDone(LobbyEvent var1);

	void onUnSubscribeLobbyDone(LobbyEvent var1);

	void onGetLiveLobbyInfoDone(LiveRoomInfoEvent var1);
}
