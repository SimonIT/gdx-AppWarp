package de.SimonIT.App42MultiPlayerGamingSDK.listener;

import de.SimonIT.App42MultiPlayerGamingSDK.events.LiveRoomInfoEvent;
import de.SimonIT.App42MultiPlayerGamingSDK.events.LobbyEvent;

public interface LobbyRequestListener {
	void onJoinLobbyDone(LobbyEvent var1);

	void onLeaveLobbyDone(LobbyEvent var1);

	void onSubscribeLobbyDone(LobbyEvent var1);

	void onUnSubscribeLobbyDone(LobbyEvent var1);

	void onGetLiveLobbyInfoDone(LiveRoomInfoEvent var1);
}
