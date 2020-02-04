package de.SimonIT.App42MultiPlayerGamingSDK.listener;

import de.SimonIT.App42MultiPlayerGamingSDK.events.ChatEvent;

import java.util.List;

public interface ChatRequestListener {
	void onSendChatDone(byte var1);

	void onSendPrivateChatDone(byte var1);

	void onGetChatHistoryDone(byte var1, List<ChatEvent> var2);
}
