package de.SimonIT.gdxAppWarp.listener;

import de.SimonIT.gdxAppWarp.events.ChatEvent;

import java.util.List;

public interface ChatRequestListener {
	void onSendChatDone(byte var1);

	void onSendPrivateChatDone(byte var1);

	void onGetChatHistoryDone(byte var1, List<ChatEvent> var2);
}
