package com.shephertz.app42.gaming.multiplayer.client.listener;

import com.shephertz.app42.gaming.multiplayer.client.events.ChatEvent;

import java.util.ArrayList;

public interface ChatRequestListener {
	void onSendChatDone(byte var1);

	void onSendPrivateChatDone(byte var1);

	void onGetChatHistoryDone(byte var1, ArrayList<ChatEvent> var2);
}
