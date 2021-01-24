package de.SimonIT.gdxAppWarp;

import com.badlogic.gdx.utils.Null;
import de.SimonIT.gdxAppWarp.events.RoomData;

public class Mapper {
	@Null
	static RoomData createRoomData(@Null com.shephertz.app42.gaming.multiplayer.client.events.RoomData roomData) {
		if (roomData == null) {
			return null;
		} else {
			return new RoomData(roomData.getId(), roomData.getRoomOwner(), roomData.getName(), roomData.getMaxUsers());
		}
	}
}
