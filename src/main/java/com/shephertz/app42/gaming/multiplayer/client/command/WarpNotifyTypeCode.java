package com.shephertz.app42.gaming.multiplayer.client.command;

public interface WarpNotifyTypeCode {
	byte ROOM_CREATED = 1;
	byte ROOM_DELETED = 2;
	byte USER_JOINED_LOBBY = 3;
	byte USER_LEFT_LOBBY = 4;
	byte USER_JOINED_ROOM = 5;
	byte USER_LEFT_ROOM = 6;
	byte USER_ONLINE = 7;
	byte USER_OFFLINE = 8;
	byte CHAT = 9;
	byte UPDATE_PEERS = 10;
	byte ROOM_PROPERTY_CHANGE = 11;
	byte PRIVATE_CHAT = 12;
	byte MOVE_COMPLETED = 13;
	byte USER_PAUSED = 14;
	byte USER_RESUMED = 15;
	byte GAME_STARTED = 16;
	byte GAME_STOPPED = 17;
	byte PRIVATE_UPDATE = 18;
	byte NEXT_TURN_REQUESTED = 19;
}
