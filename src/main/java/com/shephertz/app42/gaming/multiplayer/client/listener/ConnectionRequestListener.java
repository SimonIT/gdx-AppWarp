package com.shephertz.app42.gaming.multiplayer.client.listener;

import com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent;

public interface ConnectionRequestListener {
	void onConnectDone(ConnectEvent var1);

	void onDisconnectDone(ConnectEvent var1);

	void onInitUDPDone(byte var1);
}
