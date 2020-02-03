package com.shephertz.app42.gaming.multiplayer.client;

import com.badlogic.gdx.utils.Timer;
import com.shephertz.app42.gaming.multiplayer.client.message.WarpMessage;
import com.shephertz.app42.gaming.multiplayer.client.message.WarpRequestMessage;
import com.shephertz.app42.gaming.multiplayer.client.message.WarpResponseMessage;
import com.shephertz.app42.gaming.multiplayer.client.transformer.WarpMessageDecoder;
import com.shephertz.app42.gaming.multiplayer.client.transformer.WarpRequestEncoder;
import com.shephertz.app42.gaming.multiplayer.client.util.Util;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

public class UDPListener implements Runnable {
	private InetAddress warpServerIP;
	private DatagramSocket serverSocket;
	private List<DatagramPacket> pendingWriteOperations = new LinkedList<>();
	private Timer keepAliveTimer;
	private JavaWarpClient theGame;
	private boolean waitingForAck = false;
	private int waitCounter = 0;

	public void Stop() {
		if (this.serverSocket != null) {
			this.serverSocket.close();
			this.serverSocket = null;
		}

		this.stopTimer();
	}

	public void run() {
		try {
			byte[] recvBuff = new byte[1024];
			JavaWarpClient theClient = JavaWarpClient.getInstance();
			if (this.serverSocket != null) {
				this.serverSocket.setSoTimeout(1);

				while (true) {
					if (this.waitingForAck) {
						++this.waitCounter;
						if (this.waitCounter > 3000) {
							this.waitingForAck = false;
							this.waitCounter = 0;
							theClient.fireUDPEvent((byte) 4);
						}
					}

					DatagramPacket receivePacket = new DatagramPacket(recvBuff, recvBuff.length);

					try {
						this.serverSocket.receive(receivePacket);
						InetAddress sourceIP = receivePacket.getAddress();
						if (sourceIP.equals(this.warpServerIP)) {
							byte[] receiveData = receivePacket.getData();
							WarpMessage msg = WarpMessageDecoder.decode(ByteBuffer.wrap(receiveData));
							theClient.addMessageToQueue(msg);
							synchronized (theClient.dispatcher) {
								theClient.dispatcher.notify();
							}

							if (msg instanceof WarpResponseMessage) {
								WarpResponseMessage responseMsg = (WarpResponseMessage) msg;
								if (this.waitingForAck && responseMsg.getRequestType() == 64) {
									this.waitingForAck = false;
									this.startTimer();
								}
							}
						}
					} catch (SocketTimeoutException ignored) {
					}

					DatagramPacket packetToSend;
					while ((packetToSend = this.getFirstElement()) != null) {
						this.serverSocket.send(packetToSend);
					}
				}
			}

			Util.trace("Can't start listening UDP as socket is null");
		} catch (Exception var11) {
			Util.trace("UDP Listen loop " + var11.toString());
		}
	}

	synchronized DatagramPacket getFirstElement() {
		return this.pendingWriteOperations.size() > 0 ? this.pendingWriteOperations.remove(0) : null;
	}

	synchronized void SendRequest(WarpRequestMessage updateMsg) {
		try {
			if (updateMsg.getRequestType() == 64) {
				this.theGame = JavaWarpClient.getInstance();
				this.waitingForAck = true;
				this.waitCounter = 0;
				this.serverSocket = new DatagramSocket();
				this.warpServerIP = InetAddress.getByName(Util.WarpServerHost);
				Util.trace("new UDP Socket created with " + Util.WarpServerHost);
			}

			if (this.serverSocket == null) {
				Util.trace("Can't send request as UDP Socket is null");
				return;
			}

			ByteBuffer buf = WarpRequestEncoder.encode(updateMsg);
			buf.flip();
			DatagramPacket packet = new DatagramPacket(buf.array(), buf.array().length, this.warpServerIP, 12346);
			this.pendingWriteOperations.add(packet);
			this.stopTimer();
			this.startTimer();
		} catch (Exception var4) {
			Util.trace("UDP SendRequest " + var4.toString());
		}

	}

	private void startTimer() {
		this.keepAliveTimer = new Timer();
		this.keepAliveTimer.scheduleTask(new UDPListener.KeepAliveTask(this), 10000L);
	}

	private void stopTimer() {
		if (this.keepAliveTimer != null) {
			this.keepAliveTimer.clear();
			this.keepAliveTimer = null;
		}

	}

	private class KeepAliveTask extends Timer.Task {
		UDPListener owner;

		KeepAliveTask(UDPListener listener) {
			this.owner = listener;
		}

		public void run() {
			WarpRequestMessage msg = new WarpRequestMessage((byte) 63, UDPListener.this.theGame.sessionId, 0, (byte) 0,
					(byte) 0, (byte) 0, "".getBytes().length, "".getBytes());
			this.owner.SendRequest(msg);
		}
	}
}
