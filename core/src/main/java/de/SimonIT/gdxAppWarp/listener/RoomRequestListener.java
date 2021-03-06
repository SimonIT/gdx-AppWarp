package de.SimonIT.gdxAppWarp.listener;

import de.SimonIT.gdxAppWarp.events.LiveRoomInfoEvent;
import de.SimonIT.gdxAppWarp.events.RoomEvent;

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
