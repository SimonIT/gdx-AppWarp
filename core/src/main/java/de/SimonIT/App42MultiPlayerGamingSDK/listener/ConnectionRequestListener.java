package de.SimonIT.App42MultiPlayerGamingSDK.listener;

import de.SimonIT.App42MultiPlayerGamingSDK.events.ConnectEvent;

public interface ConnectionRequestListener {
	void onConnectDone(ConnectEvent var1);

	void onDisconnectDone(ConnectEvent var1);

	void onInitUDPDone(byte var1);
}
