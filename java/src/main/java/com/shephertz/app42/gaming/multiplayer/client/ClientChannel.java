package com.shephertz.app42.gaming.multiplayer.client;

import com.shephertz.app42.gaming.multiplayer.client.message.WarpMessage;
import com.shephertz.app42.gaming.multiplayer.client.message.WarpRequestMessage;
import com.shephertz.app42.gaming.multiplayer.client.message.WarpResponseMessage;
import com.shephertz.app42.gaming.multiplayer.client.transformer.WarpMessageDecoder;
import com.shephertz.app42.gaming.multiplayer.client.transformer.WarpRequestEncoder;
import com.shephertz.app42.gaming.multiplayer.client.util.Util;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.*;

class ClientChannel implements Runnable {
	private static boolean waitForMore = false;
	private String host;
	private int port;
	private SocketChannel theChannel;
	private List<WarpRequestMessage> pendingWriteOperations = new LinkedList<>();
	private Selector selector;
	private ByteBuffer socketBuffer;
	private JavaWarpClient theGame;
	private ClientChannel.KeepAliveTask keepAliveTask;
	private Timer keepAliveTimer;
	private int pendingKeepAliveCount = 0;
	private boolean shouldStop = false;

	ClientChannel(String host, int port) {
		this.host = host;
		this.port = port;

		try {
			this.theGame = JavaWarpClient.getInstance();
		} catch (Exception var4) {
			Util.trace("Exception in ClientChannel " + var4.getMessage());
		}

	}

	private void startConnect() throws Exception {
		this.selector = SelectorProvider.provider().openSelector();
		this.theChannel = SocketChannel.open();
		this.theChannel.configureBlocking(false);
		this.theChannel.connect(new InetSocketAddress(InetAddress.getByName(this.host), this.port));
		this.theChannel.register(this.selector, SelectionKey.OP_CONNECT);
	}

	private void channelRead(SelectionKey key) throws Exception {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		ByteBuffer singleReadBuffer = ByteBuffer.allocate(4096);
		int numRead = socketChannel.read(singleReadBuffer);
		ByteBuffer decodingBuf;
		if (!waitForMore) {
			decodingBuf = ByteBuffer.wrap(singleReadBuffer.array(), 0, numRead);
		} else {
			Util.trace("appending " + numRead + " newly read bytes to previous " + this.socketBuffer.position()
					+ " buffer");
			this.socketBuffer.put(singleReadBuffer.array(), 0, numRead);
			decodingBuf = ByteBuffer.wrap(this.socketBuffer.array(), 0, this.socketBuffer.position());
		}

		int numToRead = decodingBuf.limit();
		int numDecoded = 0;

		while (numDecoded < numToRead) {
			waitForMore = WarpMessageDecoder.needsMoreData(decodingBuf.array(), numDecoded, decodingBuf.limit());
			if (waitForMore) {
				this.socketBuffer = ByteBuffer.allocate(8192);
				this.socketBuffer.put(decodingBuf.array(), decodingBuf.position(),
						decodingBuf.limit() - decodingBuf.position());
				return;
			}

			WarpMessage msg = WarpMessageDecoder.decode(decodingBuf);
			if (msg.getType() == 1) {
				WarpResponseMessage response = (WarpResponseMessage) msg;
				if (response.getRequestType() == 63) {
					this.pendingKeepAliveCount = 0;
				}
			}

			this.theGame.addMessageToQueue(msg);
			synchronized (this.theGame.dispatcher) {
				this.theGame.dispatcher.notify();
			}

			if (msg.getType() == 1) {
				numDecoded += 9 + msg.getPayLoadSize();
			} else {
				numDecoded += 8 + msg.getPayLoadSize();
			}
		}

	}

	void startKeepAlives() {
		this.keepAliveTimer = new Timer();
		this.keepAliveTask = new ClientChannel.KeepAliveTask(this);
		this.keepAliveTimer.schedule(this.keepAliveTask, 2000L);
	}

	private synchronized void channelWrite(SelectionKey key) throws Exception {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		WarpRequestMessage msg = this.pendingWriteOperations.remove(0);
		if (msg != null) {
			ByteBuffer buf = WarpRequestEncoder.encode(msg);
			buf.flip();
			socketChannel.write(buf);
			if (this.pendingWriteOperations.size() <= 0) {
				key.interestOps(SelectionKey.OP_READ);
			}

		}
	}

	private void channelConnect(SelectionKey key) throws Exception {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		socketChannel.finishConnect();
		key.interestOps(SelectionKey.OP_READ);
		this.theGame.onConnect(true);
	}

	public void run() {
		try {
			this.startConnect();

			while (true) {
				this.selector.select();
				if (this.shouldStop) {
					Util.trace("shouldStop... returning");
					return;
				}

				Iterator<SelectionKey> selectedKeys = this.selector.selectedKeys().iterator();

				while (selectedKeys.hasNext()) {
					SelectionKey key = selectedKeys.next();
					selectedKeys.remove();
					if (key.isValid()) {
						if (key.isConnectable()) {
							this.channelConnect(key);
						} else if (key.isReadable()) {
							this.channelRead(key);
						} else if (key.isWritable()) {
							this.channelWrite(key);
						}
					}
				}
			}
		} catch (Exception var3) {
			Util.trace("Exception " + var3.getClass() + " in thread run " + var3.getMessage());
			this.disconnect();
			this.theGame.onConnect(false);
		}
	}

	synchronized void SendRequest(WarpRequestMessage authMsg) {
		try {
			this.pendingWriteOperations.add(authMsg);
			SelectionKey key = this.theChannel.keyFor(this.selector);
			if (key == null || !key.isValid()) {
				Util.trace("key " + key + " is invalid.");
				this.disconnect();
				this.theGame.onConnect(false);
				return;
			}

			key.interestOps(SelectionKey.OP_WRITE);
			this.selector.wakeup();
			if (this.keepAliveTimer != null) {
				this.keepAliveTimer.cancel();
			}

			if (this.theGame.getConnectionState() == 0) {
				this.keepAliveTimer = new Timer();
				this.keepAliveTask = new ClientChannel.KeepAliveTask(this);
				this.keepAliveTimer.schedule(this.keepAliveTask, 2000L);
			}
		} catch (Exception var3) {
			Util.trace("Exception in sending Request " + var3);
		}

	}

	void disconnect() {
		this.shouldStop = true;

		try {
			this.theChannel.close();
			this.selector.wakeup();
		} catch (Exception var2) {
			Util.trace("Exception in disconnect closing the channel " + var2);
		}

		if (this.keepAliveTimer != null) {
			this.keepAliveTimer.cancel();
			this.keepAliveTimer = null;
		}

	}

	private class KeepAliveTask extends TimerTask {
		ClientChannel owner;

		KeepAliveTask(ClientChannel channel) {
			this.owner = channel;
		}

		public void run() {
			if (this.owner.pendingKeepAliveCount > 3) {
				Util.trace("Missed 3 heart beats. connection error");
				this.owner.disconnect();
				ClientChannel.this.theGame.onConnect(false);
			} else {
				this.owner.pendingKeepAliveCount++;
				WarpRequestMessage msg = new WarpRequestMessage((byte) 63, ClientChannel.this.theGame.sessionId, 0,
						(byte) 0, (byte) 0, (byte) 0, "".getBytes().length, "".getBytes());
				this.owner.SendRequest(msg);
			}
		}
	}
}
