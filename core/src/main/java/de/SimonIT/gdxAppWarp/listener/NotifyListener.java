package de.SimonIT.gdxAppWarp.listener;

import de.SimonIT.gdxAppWarp.events.*;

import java.util.Map;

public interface NotifyListener {
	void onRoomCreated(RoomData var1);

	void onRoomDestroyed(RoomData var1);

	void onUserLeftRoom(RoomData var1, String var2);

	void onUserJoinedRoom(RoomData var1, String var2);

	void onUserLeftLobby(LobbyData var1, String var2);

	void onUserJoinedLobby(LobbyData var1, String var2);

	void onChatReceived(ChatEvent var1);

	void onPrivateChatReceived(String var1, String var2);

	void onPrivateUpdateReceived(String var1, byte[] var2, boolean var3);

	void onUpdatePeersReceived(UpdateEvent var1);

	void onUserChangeRoomProperty(RoomData var1, String var2, Map<String, Object> var3, Map<String, String> var4);

	void onMoveCompleted(MoveEvent var1);

	void onGameStarted(String var1, String var2, String var3);

	void onGameStopped(String var1, String var2);

	void onUserPaused(String var1, boolean var2, String var3);

	void onUserResumed(String var1, boolean var2, String var3);

	void onNextTurnRequest(String var1);
}
