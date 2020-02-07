package de.SimonIT.App42MultiPlayerGamingSDK;

import de.SimonIT.App42MultiPlayerGamingSDK.events.RoomData;
import org.jetbrains.annotations.Nullable;

public class Mapper {
	@Nullable
	static RoomData createRoomData(@Nullable com.shephertz.app42.gaming.multiplayer.client.events.RoomData roomData) {
		if (roomData == null) {
			return null;
		} else {
			return new RoomData(roomData.getId(), roomData.getRoomOwner(), roomData.getName(), roomData.getMaxUsers());
		}
	}
}
