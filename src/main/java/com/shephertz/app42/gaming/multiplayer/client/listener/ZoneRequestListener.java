package com.shephertz.app42.gaming.multiplayer.client.listener;

import com.shephertz.app42.gaming.multiplayer.client.events.*;

public interface ZoneRequestListener {
	void onDeleteRoomDone(RoomEvent var1);

	void onGetAllRoomsDone(AllRoomsEvent var1);

	void onCreateRoomDone(RoomEvent var1);

	void onGetOnlineUsersDone(AllUsersEvent var1);

	void onGetLiveUserInfoDone(LiveUserInfoEvent var1);

	void onSetCustomUserDataDone(LiveUserInfoEvent var1);

	void onGetMatchedRoomsDone(MatchedRoomsEvent var1);

	void onGetAllRoomsCountDone(AllRoomsEvent var1);

	void onGetOnlineUsersCountDone(AllUsersEvent var1);

	void onGetUserStatusDone(LiveUserInfoEvent var1);
}
