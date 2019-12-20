package com.shephertz.app42.gaming.multiplayer.client;

public interface ConnectionState {
	int CONNECTED = 0;
	int CONNECTING = 1;
	int DISCONNECTED = 2;
	int DISCONNECTING = 3;
	int RECOVERING = 4;
}
