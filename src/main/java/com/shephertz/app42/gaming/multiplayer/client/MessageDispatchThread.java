package com.shephertz.app42.gaming.multiplayer.client;

import com.shephertz.app42.gaming.multiplayer.client.message.WarpMessage;
import com.shephertz.app42.gaming.multiplayer.client.message.WarpNotifyMessage;
import com.shephertz.app42.gaming.multiplayer.client.message.WarpResponseMessage;
import com.shephertz.app42.gaming.multiplayer.client.util.Util;

public class MessageDispatchThread extends Thread {
	boolean shouldStop = false;
	WarpClient theClient;

	MessageDispatchThread(WarpClient client) {
		super("MessageDispatchThread");
		this.theClient = client;
	}

	public void run() {
		while (!this.shouldStop) {
			WarpMessage msg = this.theClient.getMessageFromQueue();
			if (msg == null) {
				synchronized (this.theClient.dispatcher) {
					try {
						this.theClient.dispatcher.wait();
					} catch (InterruptedException var5) {
					}
				}
			} else if (msg.getType() == 1) {
				this.theClient.onResponse((WarpResponseMessage) msg);
			} else {
				this.theClient.onNotify((WarpNotifyMessage) msg);
			}
		}

		Util.trace("MessageDispatchThread exiting");
	}

	public void terminate() {
		this.shouldStop = true;
	}
}
