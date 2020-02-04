package de.SimonIT.App42MultiPlayerGamingSDK.listener;

import de.SimonIT.App42MultiPlayerGamingSDK.events.LiveRoomInfoEvent;
import de.SimonIT.App42MultiPlayerGamingSDK.events.RoomEvent;

public interface RoomRequestListener {
	void onSubscribeRoomDone(RoomEvent var1);

	void onUnSubscribeRoomDone(RoomEvent var1);

	void onJoinRoomDone(RoomEvent var1);

	void onLeaveRoomDone(RoomEvent var1);

	void onGetLiveRoomInfoDone(LiveRoomInfoEvent var1);

	void onSetCustomRoomDataDone(LiveRoomInfoEvent var1);

	void onUpdatePropertyDone(LiveRoomInfoEvent var1);

	void onLockPropertiesDone(byte var1);

	void onUnlockPropertiesDone(byte var1);

	void onJoinAndSubscribeRoomDone(RoomEvent var1);

	void onLeaveAndUnsubscribeRoomDone(RoomEvent var1);
}
