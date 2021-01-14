package de.SimonIT.gdxAppWarp.listener;

import de.SimonIT.gdxAppWarp.events.LiveRoomInfoEvent;
import de.SimonIT.gdxAppWarp.events.LobbyEvent;

public interface LobbyRequestListener {
	void onJoinLobbyDone(LobbyEvent var1);

	void onLeaveLobbyDone(LobbyEvent var1);

	void onSubscribeLobbyDone(LobbyEvent var1);

	void onUnSubscribeLobbyDone(LobbyEvent var1);

	void onGetLiveLobbyInfoDone(LiveRoomInfoEvent var1);
}
