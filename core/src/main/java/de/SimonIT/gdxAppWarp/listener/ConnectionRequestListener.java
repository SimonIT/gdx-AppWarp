package de.SimonIT.gdxAppWarp.listener;

import de.SimonIT.gdxAppWarp.events.ConnectEvent;

public interface ConnectionRequestListener {
	void onConnectDone(ConnectEvent var1);

	void onDisconnectDone(ConnectEvent var1);

	void onInitUDPDone(byte var1);
}
