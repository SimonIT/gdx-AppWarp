package com.shephertz.app42.gaming.multiplayer.client;

import com.shephertz.app42.gaming.api.client.App42CallBack;
import com.shephertz.app42.gaming.api.storage.Query;
import com.shephertz.app42.gaming.api.storage.QueryBuilder;
import com.shephertz.app42.gaming.api.storage.Storage;
import com.shephertz.app42.gaming.api.storage.StorageService;
import com.shephertz.app42.gaming.multiplayer.client.events.*;
import com.shephertz.app42.gaming.multiplayer.client.listener.*;
import com.shephertz.app42.gaming.multiplayer.client.message.WarpMessage;
import com.shephertz.app42.gaming.multiplayer.client.message.WarpNotifyMessage;
import com.shephertz.app42.gaming.multiplayer.client.message.WarpRequestMessage;
import com.shephertz.app42.gaming.multiplayer.client.message.WarpResponseMessage;
import com.shephertz.app42.gaming.multiplayer.client.transformer.WarpMessageDecoder;
import com.shephertz.app42.gaming.multiplayer.client.transformer.WarpRequestEncoder;
import com.shephertz.app42.gaming.multiplayer.client.util.Util;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.*;

public class WarpClient {
	private static WarpClient _instance = null;
	private final int MAX_UPDATE_SIZE = 1000;
	private final int MAX_CHAT_SIZE = 500;
	protected MessageDispatchThread dispatcher = null;
	int sessionId = 0;
	private ClientChannel clientChannel;
	private String apiKey;
	private String privateKey;
	private Set<ConnectionRequestListener> ConnectionRequestListeners = Collections.synchronizedSet(new HashSet<>());
	private Set<ZoneRequestListener> zoneRequestListeners = Collections.synchronizedSet(new HashSet<>());
	private Set<RoomRequestListener> roomRequestListeners = Collections.synchronizedSet(new HashSet<>());
	private Set<LobbyRequestListener> lobbyRequestListeners = Collections.synchronizedSet(new HashSet<>());
	private Set<ChatRequestListener> chatRequestListeners = Collections.synchronizedSet(new HashSet<>());
	private Set<UpdateRequestListener> updateRequestListeners = Collections.synchronizedSet(new HashSet<>());
	private Set<NotifyListener> notifyListeners = Collections.synchronizedSet(new HashSet<>());
	private Set<TurnBasedRoomListener> turnBasedRoomListeners = Collections.synchronizedSet(new HashSet<>());
	private Thread udpListenerThread = null;
	private UDPListener udpListener = null;
	private WarpClient.ConnectionWatchTask connectTimeoutTask;
	private WarpClient.ConnectionAutoRecoveryTask connectAutoRecoveryTask;
	private Timer connectionRecoveryTimer;
	private Timer connectionTimer;
	private boolean autoRecover = false;
	private ArrayList<WarpMessage> messageQueue = new ArrayList<>();
	private String dbName;
	private boolean isChatHistory;
	private StorageService storageService;
	private int connectionState = 2;

	private WarpClient() {
	}

	public static void enableTrace(boolean enable) {
		Util.TRACE_ENABLED = enable;
	}

	/**
	* Sets the time allowed to the client to recover from an intermittent
	* connection loss. This must be set before the initial connect API is called as
	* that associates the value on the server for the given connection.
	*
	* @param maxRecoveryTime
	*            time - the time (in seconds) allowed to the client to recover from
	*            intermittent connection loss
	*/
	public static void setRecoveryAllowance(int maxRecoveryTime) {
		Util.RECOVERY_ALLOWANCE_TIME = maxRecoveryTime;
	}

	/**
	* It returns the singleton instance of WarpClient.This should be initialized
	* with a key pair before it is used.
	*
	* @return singleton instance of WarpClient.
	* @throws Exception
	*             WarpClient not initialized
	*/
	public static WarpClient getInstance() throws Exception {
		if (_instance == null) {
			throw new Exception("WarpClient not initialized!");
		} else {
			return _instance;
		}
	}

	/**
	* Initializes the singleton instance of WarpClient with the developer
	* credentials. This has to be called only once during the lifetime of the
	* application. It is required before you can call any other API.
	*
	* @param apiKey
	*            The Application key given when the application was created.
	* @param pvtKey
	*            The Application key given when the application was created.
	* @return WarpResponseResultCode
	*/
	public static byte initialize(String apiKey, String pvtKey) {
		if (_instance == null && apiKey != null && pvtKey != null) {
			_instance = new WarpClient();
			_instance.apiKey = apiKey;
			_instance.privateKey = pvtKey;
			_instance.dispatcher = new MessageDispatchThread(_instance);
			_instance.dispatcher.start();
			return 0;
		} else {
			return 4;
		}
	}

	/**
	* Initializes the singleton instance of WarpClient with the developer
	* credentials. This has to be called only once during the lifetime of the
	* application. It is required before you can call any other API.
	*
	* @param apiKey
	*            The Application key given when the application was created.
	* @param pvtKey
	*            The Application key given when the application was created.
	* @param server
	*            App Warp Server IP Or Name.
	* @return WarpResponseResultCode
	*/
	public static byte initialize(String apiKey, String pvtKey, String server) {
		if (_instance == null && apiKey != null && pvtKey != null && server != null) {
			_instance = new WarpClient();
			_instance.apiKey = apiKey;
			_instance.privateKey = pvtKey;
			Util.WarpServerHost = server;
			_instance.dispatcher = new MessageDispatchThread(_instance);
			_instance.dispatcher.start();
			return 0;
		} else {
			return 4;
		}
	}

	public static boolean adminCreateZone(String apiKey, String secretKey, String host) {
		boolean result = false;

		try {
			Socket socket = new Socket(host, 12346);
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			JSONObject zoneObj = buildAdminRequestJSON(apiKey, secretKey);
			WarpRequestMessage zoneMsg = new WarpRequestMessage((byte) 15, 0, 0, (byte) 0, (byte) 1, (byte) 2,
					zoneObj.toString().getBytes().length, zoneObj.toString().getBytes());
			ByteBuffer buf = WarpRequestEncoder.encode(zoneMsg);
			out.write(buf.array());
			byte[] respBuffer = new byte[1024];
			int readLen = in.read(respBuffer);
			ByteBuffer decodingBuf = ByteBuffer.wrap(respBuffer, 0, readLen);
			WarpResponseMessage msg = (WarpResponseMessage) WarpMessageDecoder.decode(decodingBuf);
			if (msg.getResultCode() == 0) {
				result = true;
			}

			if (socket != null) {
				socket.close();
			}
		} catch (Exception var14) {
			Util.trace(var14.getMessage());
		}

		return result;
	}

	public static boolean adminDeleteZone(String apiKey, String secretKey, String host) {
		boolean result = false;

		try {
			Socket socket = new Socket(host, 12346);
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			JSONObject zoneObj = buildAdminRequestJSON(apiKey, secretKey);
			WarpRequestMessage zoneMsg = new WarpRequestMessage((byte) 16, 0, 0, (byte) 0, (byte) 1, (byte) 2,
					zoneObj.toString().getBytes().length, zoneObj.toString().getBytes());
			ByteBuffer buf = WarpRequestEncoder.encode(zoneMsg);
			out.write(buf.array());
			byte[] respBuffer = new byte[1024];
			int readLen = in.read(respBuffer);
			ByteBuffer decodingBuf = ByteBuffer.wrap(respBuffer, 0, readLen);
			WarpResponseMessage msg = (WarpResponseMessage) WarpMessageDecoder.decode(decodingBuf);
			if (msg.getResultCode() == 0) {
				result = true;
			}

			if (socket != null) {
				socket.close();
			}
		} catch (Exception var14) {
			Util.trace(var14.getMessage());
		}

		return result;
	}

	public static byte adminCreateRoom(String name, String owner, int maxUsers, String apiKey, String secretKey,
			String host, HashMap<String, Object> tableProperties) {
		byte result = -1;

		try {
			JSONObject properties = new JSONObject();
			if (tableProperties != null && tableProperties.size() > 0) {
				properties = Util.getJsonObjectFromHashtable(tableProperties);
			}

			if (properties.toString().getBytes().length > 2048) {
				return 7;
			}

			Socket socket = new Socket(host, 12346);
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			JSONObject roomObj = buildAdminRequestJSON(apiKey, secretKey);
			roomObj.put("maxUsers", maxUsers);
			roomObj.put("owner", owner);
			roomObj.put("name", name);
			roomObj.put("address", host);
			roomObj.put("properties", properties);

			WarpRequestMessage roomCreateMsg = new WarpRequestMessage((byte) 6, 0, 0, (byte) 0, (byte) 1, (byte) 2,
					roomObj.toString().getBytes().length, roomObj.toString().getBytes());
			ByteBuffer buf = WarpRequestEncoder.encode(roomCreateMsg);
			out.write(buf.array());
			byte[] respBuffer = new byte[1024];
			int readLen = in.read(respBuffer);
			ByteBuffer decodingBuf = ByteBuffer.wrap(respBuffer, 0, readLen);
			WarpResponseMessage msg = (WarpResponseMessage) WarpMessageDecoder.decode(decodingBuf);
			if (msg.getResultCode() == 0) {
				result = 0;
			}

			socket.close();
		} catch (Exception var19) {
			Util.trace(var19.getMessage());
		}

		return result;
	}

	public static boolean adminDeleteRoom(String roomId, String apiKey, String secretKey, String host) {
		boolean result = false;

		try {
			Socket socket = new Socket(host, 12346);
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			JSONObject roomObj = buildAdminRequestJSON(apiKey, secretKey);
			roomObj.put("id", roomId);
			WarpRequestMessage roomDelMsg = new WarpRequestMessage((byte) 11, 0, 0, (byte) 0, (byte) 1, (byte) 2,
					roomObj.toString().getBytes().length, roomObj.toString().getBytes());
			ByteBuffer buf = WarpRequestEncoder.encode(roomDelMsg);
			out.write(buf.array());
			byte[] respBuffer = new byte[1024];
			int readLen = in.read(respBuffer);
			ByteBuffer decodingBuf = ByteBuffer.wrap(respBuffer, 0, readLen);
			WarpResponseMessage msg = (WarpResponseMessage) WarpMessageDecoder.decode(decodingBuf);
			if (msg.getResultCode() == 0) {
				result = true;
			}

			socket.close();
		} catch (Exception var15) {
			Util.trace(var15.getMessage());
		}

		return result;
	}

	private static JSONObject buildAdminRequestJSON(String appKey, String secKey) throws JSONException {
		JSONObject authObj = new JSONObject();
		String timeStamp = Util.getUTCFormattedTimestamp();
		authObj.put("version", "Android_1.0");
		authObj.put("timeStamp", timeStamp);
		authObj.put("apiKey", appKey);
		authObj.put("signature", Util.calculateSignature(appKey, "Android_1.0", timeStamp, secKey));
		return authObj;
	}

	synchronized void addMessageToQueue(WarpMessage msg) {
		this.messageQueue.add(msg);
	}

	synchronized WarpMessage getMessageFromQueue() {
		return this.messageQueue.size() > 0 ? this.messageQueue.remove(0) : null;
	}

	/**
	* It gives the Api Key of the current established connection,otherwise returns
	* null.
	*
	* @return Api key
	*/
	public String getAPIKey() {
		return this.apiKey;
	}

	/**
	* It gives the Private/Secret Key of the current established
	* connection,otherwise returns null.
	*
	* @return Private/Secret key
	*/
	public String getPrivateKey() {
		return this.privateKey;
	}

	public void setPrivateKey(String secretKey) {
		_instance.privateKey = secretKey;
	}

	public void setApiKey(String apiKey) {
		_instance.apiKey = apiKey;
	}

	public void setServer(String address) {
		Util.WarpServerHost = address;
	}

	/**
	* setGeo allows you to connect to our cloud servers in locations other than the
	* default location. This offers developers the choice to connect to the closest
	* server depending on the client’s device location.
	*
	* @param geo
	*            server location. For e.g. US, EU, JAPAN
	*/
	public void setGeo(String geo) {
		Util.WarpServerHost = null;
		Util.geo = geo;
	}

	/**
	* Adds (registers) the given listener object to the list of objects on which
	* callbacks will be invoked when a response from the server is received for
	* Connect and Disconnect APIs. The object must implement the
	* ConnectionRequestListener interface.
	*
	* @param listener
	*            method for listening to the request
	*/
	public void addConnectionRequestListener(ConnectionRequestListener listener) {
		this.ConnectionRequestListeners.add(listener);
	}

	/**
	* Removes the given listener object from the list of objects on which callbacks
	* will be invoked when a response from the server is received for Connect and
	* Disconnect APIs. The object must implement the ConnectionRequestListener
	* interface.
	*
	* @param listener
	*            listener object
	*/
	public void removeConnectionRequestListener(ConnectionRequestListener listener) {
		this.ConnectionRequestListeners.remove(listener);
	}

	/**
	* Adds (registers) the given listener object to the list of objects on which
	* callbacks will be invoked when a response from the server is received for
	* zone level requests such as create/delete room or live user info requests.
	* The object must implement the ZoneRequestListener interface.
	*
	* @param listener
	*            method for listening to the request
	*/
	public void addZoneRequestListener(ZoneRequestListener listener) {
		this.zoneRequestListeners.add(listener);
	}

	/**
	* Removes the given listener object from the list of objects on which callbacks
	* will be invoked when a response from the server is received for zone level
	* requests such as create/delete room or live user info requests. The object
	* must implement the ZoneRequestListener interface.
	*
	* @param listener
	*            listener object
	*/
	public void removeZoneRequestListener(ZoneRequestListener listener) {
		this.zoneRequestListeners.remove(listener);
	}

	/**
	* Adds (registers) the given listener object to the list of objects on which
	* callbacks will be invoked when a response from the server is received for
	* lobby level request. The object must implement the LobbyRequestListener
	* interface.
	*
	* @param listener
	*            method for listening to the request
	*/
	public void addLobbyRequestListener(LobbyRequestListener listener) {
		this.lobbyRequestListeners.add(listener);
	}

	/**
	* Removes the given listener object from the list of objects on which callbacks
	* will be invoked when a response from the server is received for lobby level
	* requests. The object must implement the Lobby Request Listener interface.
	*
	* @param listener
	*            listener object
	*/
	public void removeLobbyRequestListener(LobbyRequestListener listener) {
		this.lobbyRequestListeners.remove(listener);
	}

	/**
	* Adds (registers) the given listener object to the list of objects on which
	* callbacks will be invoked when a response from the server is received for
	* requests pertaining to a room. The object must implement the
	* RoomRequestListener interface.
	*
	* @param listener
	*            method for listening to the request
	*/
	public void addRoomRequestListener(RoomRequestListener listener) {
		this.roomRequestListeners.add(listener);
	}

	/**
	* Adds (registers) the given listener object to the list of objects on which
	* callbacks will be invoked when a response from the server is received for a
	* SendMove request. The object must implement the TurnBasedRoomListener
	* interface.
	*
	* @param listener
	*            listener object
	*/
	public void addTurnBasedRoomListener(TurnBasedRoomListener listener) {
		this.turnBasedRoomListeners.add(listener);
	}

	/**
	* Removes the given listener object from the list of objects on which callbacks
	* will be invoked when a response from the server is received for a turn based
	* game move request. The object must implement the Turn Based Room Listener
	* interface.
	*
	* @param listener
	*            listener object
	*/
	public void removeTurnBasedRoomListener(TurnBasedRoomListener listener) {
		this.turnBasedRoomListeners.remove(listener);
	}

	/**
	* Removes the given listener object from the list of objects on which callbacks
	* will be invoked when a response from the server is received for requests
	* pertaining to a room. The object must implement the RoomRequestListener
	* interface.
	*
	* @param listener
	*            listener object
	*/
	public void removeRoomRequestListener(RoomRequestListener listener) {
		this.roomRequestListeners.remove(listener);
	}

	/**
	* Adds (registers) the given listener object to the list of objects on which
	* callbacks will be invoked when a response from the server is received for a
	* SendChat or sendPrivateChat request. The object must implement the
	* ChatRequestListener interface.
	*
	* @param listener
	*            listener object
	*/
	public void addChatRequestListener(ChatRequestListener listener) {
		this.chatRequestListeners.add(listener);
	}

	/**
	* Removes the given listener object from the list of objects on which callbacks
	* will be invoked when a response from the server is received for a SendChat or
	* sendPrivateChat request. The object must implement the ChatRequestListener
	* interface.
	*
	* @param listener
	*            listener object
	*/
	public void removeChatRequestListener(ChatRequestListener listener) {
		this.chatRequestListeners.remove(listener);
	}

	/**
	* Adds (registers) the given listener object to the list of objects on which
	* callbacks will be invoked when a response from the server is received for a
	* SendUpdatePeers request. The object must implement the UpdateRequestListener
	* interface.
	*
	* @param listener
	*            method for listening to the request
	*/
	public void addUpdateRequestListener(UpdateRequestListener listener) {
		this.updateRequestListeners.add(listener);
	}

	/**
	* Removes the given listener object from the list of objects on which callbacks
	* will be invoked when a response from the server is received for a
	* SendUpdatePeers or SendPrivateUpdate request. The object must implement the
	* UpdateRequestListener interface.
	*
	* @param listener
	*            listener object
	*/
	public void removeUpdateRequestListener(UpdateRequestListener listener) {
		this.updateRequestListeners.remove(listener);
	}

	/**
	* Adds (registers) the given listener object to the list of objects on which
	* callbacks will be invoked when a notification is received from the server
	* from any subscribed location (room or lobby). The object must implement the
	* NotifyListener interface.
	*
	* @param listener
	*            method for listening to the request
	*/
	public void addNotificationListener(NotifyListener listener) {
		this.notifyListeners.add(listener);
	}

	/**
	* Removes the given listener object from the list of objects on which callbacks
	* will be invoked when a notification is received from the server from any
	* subscribed location (room or lobby). The object must implement the
	* NotifyListener interface.
	*
	* @param listener
	*            listener object
	*/
	public void removeNotificationListener(NotifyListener listener) {
		this.notifyListeners.remove(listener);
	}

	/**
	* Sets up your connection with the AppWarp cloud server. The username passed in
	* this method must be unique across all other concurrently connected users. If
	* two users with the same name try to connect at the same time, then the first
	* one will win and the second one will get an error. The username string
	* parameter length must be more than 0 and less than 25. Also it shouldn’t
	* contain the following characters ", ; / \". The result of the operation is
	* provided in the onConnectDone callback of the ConnectionRequestListener.
	*
	* @param userName
	*            Username of the player
	*/
	public void connectWithUserName(String userName) {
		if (!this.isNullOrEmpty(userName) && this.isUserNameValid(userName)) {
			if (this.connectionState != 2 && this.connectionState != 3) {
				this.fireConnectionEvent((byte) 4);
			} else {
				try {
					this.connectionState = 1;
					Util.userName = userName;
					this.sessionId = 0;
					if (this.isNullOrEmpty(Util.WarpServerHost)) {
						(new RestConnector()).fetchHostIp("https://control.appwarp.shephertz.com/lookup", this.apiKey,
								Util.geo);
					} else {
						this.onLookUpServer((byte) 0);
					}
				} catch (Exception var3) {
					this.fireConnectionEvent((byte) 5);
				}

			}
		} else {
			this.fireConnectionEvent((byte) 4);
		}
	}

	private boolean isUserNameValid(String userName) {
		return userName.length() <= 48 && userName.indexOf(59) == -1 && userName.indexOf(44) == -1
				&& userName.indexOf(47) == -1 && userName.indexOf(92) == -1;
	}

	/**
	* Disconnects the connection with the AppWarp server. The result for this
	* request will be provided in the onDisConnectDone callback of the
	* ConnectionRequestListener.
	*/
	public void disconnect() {
		if (this.connectionState != 2 && this.connectionState != 3) {
			Util.userName = "";
			this.sessionId = 0;
			if (this.connectionState == 0) {
				this.connectionState = 3;
				WarpRequestMessage signoutMsg = new WarpRequestMessage((byte) 14, this.sessionId, 0, (byte) 0, (byte) 0,
						(byte) 1, 0, null);
				this.clientChannel.SendRequest(signoutMsg);
			} else {
				this.fireConnectionEvent((byte) 4);
				this.connectionState = 2;
				this.fireDisconnectEvent((byte) 0);
				if (this.clientChannel != null) {
					this.clientChannel.disconnect();
				}

				if (this.connectionTimer != null) {
					this.connectionTimer.cancel();
					this.connectionTimer = null;
					this.connectTimeoutTask = null;
				}
			}

		} else {
			this.fireDisconnectEvent((byte) 4);
		}
	}

	/**
	* Returns the current connection state of the WarpClient instance.
	*
	* @return int
	*/
	public int getConnectionState() {
		return this.connectionState;
	}

	/**
	* Attempts to recover from an intermittent connection error. If successful, the
	* client will be placed in the same room as before the loss and all its
	* subscriptions will be maintained. The other subscribed users of the room,
	* will receive onUserResumed notification. This can only be called if an
	* established session was lost due to a connectivity error and the client got
	* onConnectDone with a recoverable connection error code. The connection must
	* be restored within the recovery allowance period, after which the server
	* considers the session to be over (non-recoverable).
	*/
	public void RecoverConnection() {
		if (this.sessionId != 0 && Util.RECOVERY_ALLOWANCE_TIME > 0 && this.connectionState == 2) {
			try {
				this.connectionState = 4;
				if (this.isNullOrEmpty(Util.WarpServerHost)) {
					(new RestConnector()).fetchHostIp("https://control.appwarp.shephertz.com/lookup", this.apiKey,
							Util.geo);
				} else {
					this.onLookUpServer((byte) 0);
				}
			} catch (Exception var2) {
				this.fireConnectionEvent((byte) 5);
			}

		} else {
			this.fireConnectionEvent((byte) 4);
		}
	}

	/**
	* It gives sessionId of the current established connection,otherwise returns
	* zero.
	*
	* @return sessionId
	*/
	public int getSessionID() {
		return this.sessionId;
	}

	/**
	* Attempts to recover from an intermittent connection error.Since this API
	* requires sessionId so it has to be saved by the game on the last successful
	* connection.The other subscribed users of the room, will receive onUserResumed
	* notification.The connection must be restored within the recovery allowance
	* period, after which the server considers the session to be over
	* (non-recoverable).
	*
	* @param session_id
	*            sessionId of the last successful session
	* @param user_name
	*            name of the player
	*/
	public void RecoverConnectionWithSessionID(int session_id, String user_name) {
		this.sessionId = session_id;
		Util.userName = user_name;
		this.RecoverConnection();
	}

	void onResponse(WarpResponseMessage msg) {
		RoomEvent roomEvent;
		LobbyEvent lobbyEvent;
		LiveRoomInfoEvent liveRoomInfoEvent;
		AllRoomsEvent allRoomsEvent;
		AllUsersEvent allUsersEvent;

		switch (msg.getRequestType()) {
			case 1 :
				this.handleAuthenticateResponse(msg);
				break;
			case 2 :
				try {
					lobbyEvent = LobbyEvent.buildLobbyEvent(msg);
				} catch (JSONException var22) {
					lobbyEvent = new LobbyEvent(null, (byte) 6);
				}

				for (LobbyRequestListener listener : this.lobbyRequestListeners) {
					listener.onJoinLobbyDone(lobbyEvent);
				}

				return;
			case 3 :
				try {
					lobbyEvent = LobbyEvent.buildLobbyEvent(msg);
				} catch (JSONException var24) {
					lobbyEvent = new LobbyEvent(null, (byte) 6);
				}

				for (LobbyRequestListener listener : this.lobbyRequestListeners) {
					listener.onSubscribeLobbyDone(lobbyEvent);
				}

				return;
			case 4 :
				try {
					lobbyEvent = LobbyEvent.buildLobbyEvent(msg);
				} catch (JSONException var23) {
					lobbyEvent = new LobbyEvent(null, (byte) 6);
				}

				for (LobbyRequestListener listener : this.lobbyRequestListeners) {
					listener.onUnSubscribeLobbyDone(lobbyEvent);
				}

				return;
			case 5 :
				try {
					lobbyEvent = LobbyEvent.buildLobbyEvent(msg);
				} catch (JSONException var21) {
					lobbyEvent = new LobbyEvent(null, (byte) 6);
				}

				for (LobbyRequestListener listener : this.lobbyRequestListeners) {
					listener.onLeaveLobbyDone(lobbyEvent);
				}

				return;
			case 6 :
				try {
					roomEvent = RoomEvent.buildRoomEvent(msg);
				} catch (JSONException var37) {
					roomEvent = new RoomEvent(null, (byte) 6);
				}

				for (ZoneRequestListener listener : this.zoneRequestListeners) {
					listener.onCreateRoomDone(roomEvent);
				}

				return;
			case 7 :
			case 24 :
			case 27 :
			case 37 :
				try {
					roomEvent = RoomEvent.buildRoomEvent(msg);
				} catch (JSONException var35) {
					roomEvent = new RoomEvent(null, (byte) 6);
				}

				for (RoomRequestListener listener : this.roomRequestListeners) {
					listener.onJoinRoomDone(roomEvent);
				}

				return;
			case 8 :
				try {
					roomEvent = RoomEvent.buildRoomEvent(msg);
				} catch (JSONException var32) {
					roomEvent = new RoomEvent(null, (byte) 6);
				}

				for (RoomRequestListener listener : this.roomRequestListeners) {
					listener.onSubscribeRoomDone(roomEvent);
				}

				return;
			case 9 :
				try {
					roomEvent = RoomEvent.buildRoomEvent(msg);
				} catch (JSONException var29) {
					roomEvent = new RoomEvent(null, (byte) 6);
				}

				for (RoomRequestListener listener : this.roomRequestListeners) {
					listener.onUnSubscribeRoomDone(roomEvent);
				}

				return;
			case 10 :
				try {
					roomEvent = RoomEvent.buildRoomEvent(msg);
				} catch (JSONException var33) {
					roomEvent = new RoomEvent(null, (byte) 6);
				}

				for (RoomRequestListener listener : this.roomRequestListeners) {
					listener.onLeaveRoomDone(roomEvent);
				}

				return;
			case 11 :
				try {
					roomEvent = RoomEvent.buildRoomEvent(msg);
				} catch (JSONException var36) {
					roomEvent = new RoomEvent(null, (byte) 6);
				}

				for (ZoneRequestListener listener : this.zoneRequestListeners) {
					listener.onDeleteRoomDone(roomEvent);
				}

				return;
			case 12 :
				for (ChatRequestListener listener : this.chatRequestListeners) {
					listener.onSendChatDone(msg.getResultCode());
				}

				return;
			case 13 :
				for (UpdateRequestListener listener : this.updateRequestListeners) {
					listener.onSendUpdateDone(msg.getResultCode());
				}
			case 14 :
			case 15 :
			case 16 :
			case 26 :
			case 33 :
			case 34 :
			case 39 :
			case 40 :
			case 41 :
			case 42 :
			case 43 :
			case 44 :
			case 45 :
			case 46 :
			case 47 :
			case 48 :
			case 49 :
			case 50 :
			case 51 :
			case 52 :
			case 53 :
			case 54 :
			case 55 :
			case 56 :
			case 57 :
			case 58 :
			case 59 :
			case 60 :
			case 61 :
			case 62 :
			case 63 :
			case 65 :
			case 72 :
			default :
				break;
			case 17 :
				try {
					allRoomsEvent = AllRoomsEvent.buildAllRoomsEvent(msg);
				} catch (JSONException var20) {
					allRoomsEvent = new AllRoomsEvent((byte) 6, null);
				}

				for (ZoneRequestListener listener : this.zoneRequestListeners) {
					listener.onGetAllRoomsDone(allRoomsEvent);
				}

				return;
			case 18 :
				try {
					allUsersEvent = AllUsersEvent.buildAllUsersEvent(msg);
				} catch (JSONException var19) {
					allUsersEvent = new AllUsersEvent((byte) 6, null);
				}

				for (ZoneRequestListener listener : this.zoneRequestListeners) {
					listener.onGetOnlineUsersDone(allUsersEvent);
				}

				return;
			case 19 :
				LiveUserInfoEvent userInfoEvent;
				try {
					userInfoEvent = LiveUserInfoEvent.buildLiveUserInfoEvent(msg);
				} catch (JSONException var15) {
					userInfoEvent = new LiveUserInfoEvent((byte) 6, null, null, null, false, false);
				}

				for (ZoneRequestListener listener : this.zoneRequestListeners) {
					listener.onGetLiveUserInfoDone(userInfoEvent);
				}

				return;
			case 20 :
				try {
					liveRoomInfoEvent = LiveRoomInfoEvent.buildLiveRoomEvent(msg);
				} catch (JSONException var28) {
					liveRoomInfoEvent = new LiveRoomInfoEvent(null, (byte) 6, null, null);
				}

				for (RoomRequestListener listener : this.roomRequestListeners) {
					listener.onGetLiveRoomInfoDone(liveRoomInfoEvent);
				}

				return;
			case 21 :
				try {
					liveRoomInfoEvent = LiveRoomInfoEvent.buildLiveRoomEvent(msg);
				} catch (JSONException var25) {
					liveRoomInfoEvent = new LiveRoomInfoEvent(null, (byte) 6, null, null);
				}

				for (RoomRequestListener listener : this.roomRequestListeners) {
					listener.onSetCustomRoomDataDone(liveRoomInfoEvent);
				}

				return;
			case 22 :
				LiveUserInfoEvent customUserEvent;
				try {
					customUserEvent = LiveUserInfoEvent.buildLiveUserInfoEvent(msg);
				} catch (JSONException var16) {
					customUserEvent = new LiveUserInfoEvent((byte) 6, null, null, null, false, false);
				}

				for (ZoneRequestListener listener : this.zoneRequestListeners) {
					listener.onSetCustomUserDataDone(customUserEvent);
				}

				return;
			case 23 :
				try {
					liveRoomInfoEvent = LiveRoomInfoEvent.buildLiveRoomEvent(msg);
				} catch (JSONException var26) {
					liveRoomInfoEvent = new LiveRoomInfoEvent(null, (byte) 6, null, null);
				}

				for (LobbyRequestListener listener : this.lobbyRequestListeners) {
					listener.onGetLiveLobbyInfoDone(liveRoomInfoEvent);
				}

				return;
			case 25 :
				try {
					liveRoomInfoEvent = LiveRoomInfoEvent.buildLiveRoomEvent(msg);
				} catch (JSONException var34) {
					liveRoomInfoEvent = new LiveRoomInfoEvent(null, (byte) 6, null, null);
				}

				for (RoomRequestListener listener : this.roomRequestListeners) {
					listener.onUpdatePropertyDone(liveRoomInfoEvent);
				}

				return;
			case 28 :
			case 29 :
			case 38 :
			case 73 :
				MatchedRoomsEvent matchedRoomsEvent;
				try {
					matchedRoomsEvent = MatchedRoomsEvent.buildMatchedRoomsEvent(msg);
				} catch (JSONException var27) {
					matchedRoomsEvent = new MatchedRoomsEvent((byte) 6, null);
				}

				for (ZoneRequestListener listener : this.zoneRequestListeners) {
					listener.onGetMatchedRoomsDone(matchedRoomsEvent);
				}

				return;
			case 30 :
				for (ChatRequestListener listener : this.chatRequestListeners) {
					listener.onSendPrivateChatDone(msg.getResultCode());
				}

				return;
			case 31 :
				for (TurnBasedRoomListener listener : this.turnBasedRoomListeners) {
					listener.onSendMoveDone(msg.getResultCode());
				}

				return;
			case 32 :
				for (UpdateRequestListener listener : this.updateRequestListeners) {
					listener.onSendPrivateUpdateDone(msg.getResultCode());
				}

				return;
			case 35 :
				for (RoomRequestListener listener : this.roomRequestListeners) {
					listener.onLockPropertiesDone(msg.getResultCode());
				}

				return;
			case 36 :
				for (RoomRequestListener listener : this.roomRequestListeners) {
					listener.onUnlockPropertiesDone(msg.getResultCode());
				}

				return;
			case 64 :
				if (msg.getResultCode() == 0) {
					this.fireUDPEvent((byte) 0);
					WarpRequestMessage updateMsg = new WarpRequestMessage((byte) 65, this.sessionId, 0, (byte) 0,
							(byte) 2, (byte) 1, 0, null);
					this.udpListener.SendRequest(updateMsg);
				} else {
					this.fireUDPEvent((byte) 4);
				}
				break;
			case 66 :
				for (TurnBasedRoomListener listener : this.turnBasedRoomListeners) {
					listener.onStartGameDone(msg.getResultCode());
				}

				return;
			case 67 :
				for (TurnBasedRoomListener listener : this.turnBasedRoomListeners) {
					listener.onStopGameDone(msg.getResultCode());
				}

				return;
			case 68 :
				MoveEvent[] moves = MoveEvent.buildMoveHistoryArray(msg);
				for (TurnBasedRoomListener listener : this.turnBasedRoomListeners) {
					listener.onGetMoveHistoryDone(msg.getResultCode(), moves);
				}

				return;
			case 69 :
				for (TurnBasedRoomListener listener : this.turnBasedRoomListeners) {
					listener.onSetNextTurnDone(msg.getResultCode());
				}

				return;
			case 70 :
				try {
					allRoomsEvent = AllRoomsEvent.buildRoomsCountEvent(msg);
				} catch (JSONException var18) {
					allRoomsEvent = new AllRoomsEvent((byte) 6, null);
				}

				for (ZoneRequestListener listener : this.zoneRequestListeners) {
					listener.onGetAllRoomsCountDone(allRoomsEvent);
				}

				return;
			case 71 :
				try {
					allUsersEvent = AllUsersEvent.buildUsersCountEvent(msg);
				} catch (JSONException var17) {
					allUsersEvent = new AllUsersEvent((byte) 6, null);
				}

				for (ZoneRequestListener listener : this.zoneRequestListeners) {
					listener.onGetOnlineUsersCountDone(allUsersEvent);
				}

				return;
			case 74 :
				LiveUserInfoEvent userInfoEvents;
				try {
					userInfoEvents = LiveUserInfoEvent.buildOnlineUserStatusEvent(msg);
				} catch (JSONException var14) {
					userInfoEvents = new LiveUserInfoEvent((byte) 6, null, null, null, false, false);
				}

				for (ZoneRequestListener listener : this.zoneRequestListeners) {
					listener.onGetUserStatusDone(userInfoEvents);
				}

				return;
			case 75 :
				try {
					roomEvent = RoomEvent.buildRoomEvent(msg);
				} catch (JSONException var31) {
					roomEvent = new RoomEvent(null, (byte) 6);
				}

				for (RoomRequestListener listener : this.roomRequestListeners) {
					listener.onJoinAndSubscribeRoomDone(roomEvent);
				}

				return;
			case 76 :
				try {
					roomEvent = RoomEvent.buildRoomEvent(msg);
				} catch (JSONException var30) {
					roomEvent = new RoomEvent(null, (byte) 6);
				}

				for (RoomRequestListener listener : this.roomRequestListeners) {
					listener.onLeaveAndUnsubscribeRoomDone(roomEvent);
				}

				return;
			case 77 :
				for (TurnBasedRoomListener listener : this.turnBasedRoomListeners) {
					listener.onGetGameStatusDone(msg.getResultCode(), MoveEvent.getGameStatus(msg));
				}
		}

	}

	private void handleAuthenticateResponse(WarpResponseMessage msg) {
		if (msg.getResultCode() == 0) {
			try {
				JSONObject jsonAuth = new JSONObject(new String(msg.getPayLoad()));
				this.sessionId = jsonAuth.getInt("sessionid");
				this.clientChannel.startKeepAlives();
				if (jsonAuth.getBoolean("autoRecover")) {
					this.autoRecover = jsonAuth.getBoolean("autoRecover");
				}

				if (jsonAuth.getInt("recoveryTime") > 0) {
					Util.RECOVERY_ALLOWANCE_TIME = jsonAuth.getInt("recoveryTime");
				}

				if (this.connectionState == 4) {
					this.connectionState = 0;
					this.fireConnectionEvent((byte) 8);
				} else {
					this.connectionState = 0;
					this.fireConnectionEvent((byte) 0);
				}
			} catch (Exception var5) {
				this.connectionState = 2;
				Util.trace("exception " + var5 + " in handleAuthenticationResponse " + var5.getMessage());
				this.fireConnectionEvent((byte) 6);
				this.clientChannel.disconnect();
			}
		} else if (msg.getResultCode() == 1) {
			int reasonCode = 0;
			if (msg.getPayLoadType() == 2) {
				try {
					JSONObject jsonAuth = new JSONObject(new String(msg.getPayLoad()));
					reasonCode = jsonAuth.getInt("reasonCode");
				} catch (Exception var4) {
				}
			}

			this.connectionState = 2;
			this.fireConnectionEvent((byte) 1, reasonCode);
			this.sessionId = 0;
			this.clientChannel.disconnect();
		} else {
			this.connectionState = 2;
			this.fireConnectionEvent((byte) 1);
			this.sessionId = 0;
			this.clientChannel.disconnect();
		}

	}

	void onNotify(WarpNotifyMessage msg) {
		try {
			JSONObject notifyData = null;
			RoomData roomData;
			LobbyData lobbyData;
			if (msg.getPayLoadType() == 2) {
				notifyData = new JSONObject(new String(msg.getPayLoad()));
			}

			switch (msg.getUpdateType()) {
				case 1 :
					roomData = this.buildRoomData(notifyData);
					for (NotifyListener listener : this.notifyListeners) {
						listener.onRoomCreated(roomData);
					}

					return;
				case 2 :
					roomData = this.buildRoomData(notifyData);
					for (NotifyListener listener : this.notifyListeners) {
						listener.onRoomDestroyed(roomData);
					}

					return;
				case 3 :
					lobbyData = this.buildLobbyData(notifyData);
					for (NotifyListener listener : this.notifyListeners) {
						listener.onUserJoinedLobby(lobbyData, notifyData.getString("user"));
					}

					return;
				case 4 :
					lobbyData = this.buildLobbyData(notifyData);
					for (NotifyListener listener : this.notifyListeners) {
						listener.onUserLeftLobby(lobbyData, notifyData.getString("user"));
					}

					return;
				case 5 :
					roomData = this.buildRoomData(notifyData);
					for (NotifyListener listener : this.notifyListeners) {
						listener.onUserJoinedRoom(roomData, notifyData.getString("user"));
					}

					return;
				case 6 :
					roomData = this.buildRoomData(notifyData);
					for (NotifyListener listener : this.notifyListeners) {
						listener.onUserLeftRoom(roomData, notifyData.getString("user"));
					}
				case 7 :
				case 8 :
				default :
					break;
				case 9 :
					ChatEvent evt = ChatEvent.buildEvent(notifyData);
					for (NotifyListener listener : this.notifyListeners) {
						listener.onChatReceived(evt);
					}

					return;
				case 10 :
					byte reserved = msg.getReserved();
					UpdateEvent event = new UpdateEvent(msg.getPayLoad(), (reserved & 2) > 0);
					for (NotifyListener listener : this.notifyListeners) {
						listener.onUpdatePeersReceived(event);
					}

					return;
				case 11 :
					roomData = this.buildRoomData(notifyData);
					HashMap<String, Object> properties = Util
							.getHashMapFromProperties(notifyData.getString("properties"));
					HashMap<String, Object> lockProperties = Util
							.getHashMapFromProperties(notifyData.getString("lockProperties"));
					for (NotifyListener listener : this.notifyListeners) {
						listener.onUserChangeRoomProperty(roomData, notifyData.getString("sender"), properties,
								lockProperties);
					}

					return;
				case 12 :
					for (NotifyListener listener : this.notifyListeners) {
						listener.onPrivateChatReceived(notifyData.optString("sender"), notifyData.optString("chat"));
					}

					return;
				case 13 :
					MoveEvent moveEvent = MoveEvent.buildEvent(notifyData);
					for (NotifyListener listener : this.notifyListeners) {
						listener.onMoveCompleted(moveEvent);
					}

					return;
				case 14 :
					for (NotifyListener listener : this.notifyListeners) {
						listener.onUserPaused(notifyData.getString("id"), notifyData.optBoolean("isLobby"),
								notifyData.getString("user"));
					}

					return;
				case 15 :
					for (NotifyListener listener : this.notifyListeners) {
						listener.onUserResumed(notifyData.getString("id"), notifyData.optBoolean("isLobby"),
								notifyData.getString("user"));
					}

					return;
				case 16 :
					for (NotifyListener listener : this.notifyListeners) {
						listener.onGameStarted(notifyData.optString("sender"), notifyData.optString("id"),
								notifyData.optString("nextTurn"));
					}

					return;
				case 17 :
					for (NotifyListener listener : this.notifyListeners) {
						listener.onGameStopped(notifyData.optString("sender"), notifyData.optString("id"));
					}

					return;
				case 18 :
					byte[] payload = msg.getPayLoad();
					byte senderUsernameLength = payload[0];
					String senderUsername = new String(payload, 1, senderUsernameLength);
					byte[] update = new byte[payload.length - 1 - senderUsernameLength];
					System.arraycopy(payload, 1 + senderUsernameLength, update, 0, update.length);
					for (NotifyListener listener : this.notifyListeners) {
						listener.onPrivateUpdateReceived(senderUsername, update, msg.getReserved() == 2);
					}

					return;
				case 19 :
					for (NotifyListener listener : this.notifyListeners) {
						listener.onNextTurnRequest(notifyData.optString("lastTurn"));
					}
			}
		} catch (JSONException var17) {
			Util.trace(var17.getMessage());
		}

	}

	void onConnect(boolean success) {
		long currentTime = System.currentTimeMillis() + 15000L;
		if (this.connectionTimer != null) {
			this.connectionTimer.cancel();
			this.connectionTimer = null;
			this.connectTimeoutTask = null;
		}

		if (this.connectionRecoveryTimer != null) {
			this.connectionRecoveryTimer.cancel();
			this.connectionRecoveryTimer = null;
			this.connectAutoRecoveryTask = null;
		}

		if (!success) {
			if (this.udpListener != null) {
				this.udpListener.Stop();
			}

			this.udpListener = null;
			this.udpListenerThread = null;
		}

		if (success) {
			this.sendAuthRequest(Util.userName, this.sessionId);
		} else if (this.connectionState == 3) {
			this.connectionState = 2;
			this.fireDisconnectEvent((byte) 0);
		} else if (this.connectionState != 2) {
			this.connectionState = 2;
			if (this.sessionId != 0 && Util.RECOVERY_ALLOWANCE_TIME > 0) {
				if (this.autoRecover) {
					this.fireConnectionEvent((byte) 11);
					this.startRecoveryConnectionTimer();
				} else {
					this.fireConnectionEvent((byte) 9);
				}
			} else {
				this.fireConnectionEvent((byte) 5);
			}
		}

	}

	private void startRecoveryConnectionTimer() {
		this.connectAutoRecoveryTask = new WarpClient.ConnectionAutoRecoveryTask();
		this.connectionRecoveryTimer = new Timer();
		this.connectionRecoveryTimer.schedule(this.connectAutoRecoveryTask, 3000L);
	}

	private void sendAuthRequest(String user, int sid) {
		try {
			JSONObject authObj = new JSONObject();
			String timeStamp = String.valueOf(System.currentTimeMillis());
			authObj.put("version", "Java_2.1.2");
			authObj.put("timeStamp", timeStamp);
			authObj.put("user", user);
			authObj.put("apiKey", this.apiKey);
			authObj.put("signature",
					Util.calculateSignature(this.apiKey, "Java_2.1.2", user, timeStamp, this.privateKey));
			authObj.put("keepalive", 6);
			authObj.put("recoverytime", Util.RECOVERY_ALLOWANCE_TIME);
			authObj.put("dbName", this.dbName);
			WarpRequestMessage authMsg = new WarpRequestMessage((byte) 1, sid, 0, (byte) 0, (byte) 0, (byte) 2,
					authObj.toString().getBytes().length, authObj.toString().getBytes());
			this.clientChannel.SendRequest(authMsg);
		} catch (Exception var6) {
			this.fireConnectionEvent((byte) 4);
		}

	}

	/**
	* Updates the custom data associated with the given room on the server. The
	* result is provided in the onSetCustomRoomDataDone callback of the registered
	* RoomRequestListener objects. It is recommended you use the room’s properties
	* where ever possible. Use this when you need to associate data with a room
	* which can not be represented as key value pairs.
	*
	* @param roomid
	*            Id of the room
	* @param data
	*            custom data that will be set for the room
	*/
	public void setCustomRoomData(String roomid, String data) {
		LiveRoomInfoEvent evt;
		if (this.isNotConnected()) {
			evt = new LiveRoomInfoEvent(null, (byte) 5, null, null);
			for (RoomRequestListener listener : this.roomRequestListeners) {
				listener.onSetCustomRoomDataDone(evt);
			}

		} else if (!this.isNullOrEmpty(roomid) && !this.isNullOrEmpty(data)) {
			this.sendCustomDataRequest((byte) 21, roomid, data);
		} else {
			evt = new LiveRoomInfoEvent(null, (byte) 4, null, null);
			for (RoomRequestListener listener : this.roomRequestListeners) {
				listener.onSetCustomRoomDataDone(evt);
			}

		}
	}

	/**
	* Updates the custom data associated with the given user on the server (if the
	* given user is online). Result is provided in the onSetCustomUserDataDone
	* callback of the registered ZoneRequestListener objects. It can be useful in
	* setting status messages or avatar url’s etc for online users.
	*
	* @param username
	*            user for whom custom data has to be update
	* @param data
	*            custom data that will be set for the user
	*/
	public void setCustomUserData(String username, String data) {
		LiveUserInfoEvent evt;
		if (this.isNotConnected()) {
			evt = new LiveUserInfoEvent((byte) 5, null, null, null, false, false);
			for (ZoneRequestListener listener : this.zoneRequestListeners) {
				listener.onSetCustomUserDataDone(evt);
			}

		} else if (!this.isNullOrEmpty(username) && !this.isNullOrEmpty(data)) {
			this.sendCustomDataRequest((byte) 22, username, data);
		} else {
			evt = new LiveUserInfoEvent((byte) 4, null, null, null, false, false);
			for (ZoneRequestListener listener : this.zoneRequestListeners) {
				listener.onSetCustomUserDataDone(evt);
			}

		}
	}

	/**
	* Retrieves usernames of all the users connected (online) to the server. Result
	* is provided in the onGetOnlineUsers callback of the registered
	* ZoneRequestListener objects.
	*/
	public void getOnlineUsers() {
		if (!this.isNotConnected()) {
			WarpRequestMessage msg = new WarpRequestMessage((byte) 18, this.sessionId, 0, (byte) 0, (byte) 0, (byte) 0,
					"".getBytes().length, "".getBytes());
			this.clientChannel.SendRequest(msg);
		} else {
			AllUsersEvent allUsersEvent = new AllUsersEvent((byte) 5, null);
			for (ZoneRequestListener listener : this.zoneRequestListeners) {
				listener.onGetOnlineUsersDone(allUsersEvent);
			}

		}
	}

	/**
	* Retrieves total number of rooms on the server. Result is provided in the
	* onGetAllRoomsCountDone callback of the registered ZoneRequestListener
	* objects.
	*/
	public void getAllRoomsCount() {
		if (!this.isNotConnected()) {
			WarpRequestMessage msg = new WarpRequestMessage((byte) 70, this.sessionId, 0, (byte) 0, (byte) 0, (byte) 0,
					"".getBytes().length, "".getBytes());
			this.clientChannel.SendRequest(msg);
		} else {
			AllRoomsEvent allRoomsEvent = new AllRoomsEvent((byte) 5, null);
			for (ZoneRequestListener listener : this.zoneRequestListeners) {
				listener.onGetAllRoomsCountDone(allRoomsEvent);
			}

		}
	}

	/**
	* Retrieves total number of users connected (online) to the server. Result is
	* provided in the onGetOnlineUsersCountDone callback of the registered
	* ZoneRequestListener objects.
	*/
	public void getOnlineUsersCount() {
		if (!this.isNotConnected()) {
			WarpRequestMessage msg = new WarpRequestMessage((byte) 71, this.sessionId, 0, (byte) 0, (byte) 0, (byte) 0,
					"".getBytes().length, "".getBytes());
			this.clientChannel.SendRequest(msg);
		} else {
			AllUsersEvent allUsersEvent = new AllUsersEvent((byte) 5, null);

			for (ZoneRequestListener listener : this.zoneRequestListeners) {
				listener.onGetOnlineUsersCountDone(allUsersEvent);
			}

		}
	}

	/**
	* Retrieves the room ids of all the rooms on the server. Result is provided in
	* the onGetAllRoomsDone callback of the registered ZoneRequestListener objects.
	* To get a filtered list of rooms, use the GetRoomWithProperties API.
	*/
	public void getAllRooms() {
		if (!this.isNotConnected()) {
			WarpRequestMessage msg = new WarpRequestMessage((byte) 17, this.sessionId, 0, (byte) 0, (byte) 0, (byte) 0,
					"".getBytes().length, "".getBytes());
			this.clientChannel.SendRequest(msg);
		} else {
			AllRoomsEvent allRoomsEvent = new AllRoomsEvent((byte) 5, null);
			for (ZoneRequestListener listener : this.zoneRequestListeners) {
				listener.onGetAllRoomsDone(allRoomsEvent);
			}

		}
	}

	/**
	* Retrieves the live information of the user from the server. Result is
	* provided in the onGetLiveUserInfo callback of the registered
	* ZoneRequestListener objects. The information (if user is online) includes the
	* current location of the user and any associated custom data. It is useful in
	* building scenarios where you want to find if a users friends are online or
	* not and then join their room if found online.
	*
	* @param username
	*            user whose information is requested
	*/
	public void getLiveUserInfo(String username) {
		LiveUserInfoEvent evt;
		if (this.isNotConnected()) {
			evt = new LiveUserInfoEvent((byte) 5, null, null, null, false, false);
			for (ZoneRequestListener listener : this.zoneRequestListeners) {
				listener.onGetLiveUserInfoDone(evt);
			}

		} else if (this.isNullOrEmpty(username)) {
			evt = new LiveUserInfoEvent((byte) 4, null, null, null, false, false);
			for (ZoneRequestListener listener : this.zoneRequestListeners) {
				listener.onGetLiveUserInfoDone(evt);
			}

		} else {
			try {
				JSONObject roomObj = new JSONObject();
				roomObj.put("name", username);
				WarpRequestMessage roomMsg = new WarpRequestMessage((byte) 19, this.sessionId, 0, (byte) 0, (byte) 0,
						(byte) 2, roomObj.toString().getBytes().length, roomObj.toString().getBytes());
				this.clientChannel.SendRequest(roomMsg);
			} catch (Exception var6) {
				evt = new LiveUserInfoEvent((byte) 4, null, null, null, false, false);
				for (ZoneRequestListener listener : this.zoneRequestListeners) {
					listener.onGetLiveUserInfoDone(evt);
				}
			}

		}
	}

	/**
	* Retrieves status of user via username on the server. Result is provided in
	* the onGetUserStatusDone callback of the registered ZoneRequestListener
	* objects. This is useful to find either user is connected or not.
	*
	* @param username
	*            Username of the player
	*/
	public void getUserStatus(String username) {
		LiveUserInfoEvent evt;
		if (this.isNotConnected()) {
			evt = new LiveUserInfoEvent((byte) 5, null, null, null, false, false);
			for (ZoneRequestListener listener : this.zoneRequestListeners) {
				listener.onGetUserStatusDone(evt);
			}

		} else if (this.isNullOrEmpty(username)) {
			evt = new LiveUserInfoEvent((byte) 4, null, null, null, false, false);
			for (ZoneRequestListener listener : this.zoneRequestListeners) {
				listener.onGetUserStatusDone(evt);
			}

		} else {
			try {
				JSONObject userObj = new JSONObject();
				userObj.put("name", username);
				WarpRequestMessage roomMsg = new WarpRequestMessage((byte) 74, this.sessionId, 0, (byte) 0, (byte) 0,
						(byte) 2, userObj.toString().getBytes().length, userObj.toString().getBytes());
				this.clientChannel.SendRequest(roomMsg);
			} catch (Exception var6) {
				evt = new LiveUserInfoEvent((byte) 4, null, null, null, false, false);
				for (ZoneRequestListener listener : this.zoneRequestListeners) {
					listener.onGetUserStatusDone(evt);
				}
			}

		}
	}

	/**
	* Retrieves the live information of the given room from the server. Result is
	* provided in the onGetLiveRoomInfoDone callback of the registered
	* RoomRequestListener objects. The information includes the names of the
	* currently joined users, the rooms properties and any associated customData.
	* This is useful in getting a snapshot of a rooms state.
	*
	* @param roomid
	*            Id of the room
	*/
	public void getLiveRoomInfo(String roomid) {
		LiveRoomInfoEvent event;
		if (this.isNotConnected()) {
			event = new LiveRoomInfoEvent(null, (byte) 5, null, null);
			for (RoomRequestListener listener : this.roomRequestListeners) {
				listener.onGetLiveRoomInfoDone(event);
			}

		} else if (!this.isNullOrEmpty(roomid)) {
			this.sendRoomRequest((byte) 20, roomid);
		} else {
			event = new LiveRoomInfoEvent(null, (byte) 4, null, null);
			for (RoomRequestListener listener : this.roomRequestListeners) {
				listener.onGetLiveRoomInfoDone(event);
			}

		}
	}

	/**
	* Retrieves live information of the lobby from the server. Result is provided
	* in the onGetLiveLobbyInfo callback of the registered LobbyRequestListener
	* objects. The information returned includes the names of the users who are
	* currently joined in the lobby.
	*/
	public void getLiveLobbyInfo() {
		if (!this.isNotConnected()) {
			this.sendLobbyRequestMessage((byte) 23);
		} else {
			LiveRoomInfoEvent liveRoomInfoEvent = new LiveRoomInfoEvent(null, (byte) 5, null, null);
			for (LobbyRequestListener listener : this.lobbyRequestListeners) {
				listener.onGetLiveLobbyInfoDone(liveRoomInfoEvent);
			}

		}
	}

	/**
	* Sends a join lobby request to the server. Result of the request is provided
	* in the onJoinLobbyDone callback of the registered LobbyRequestListener
	* objects.
	*/
	public void joinLobby() {
		if (!this.isNotConnected()) {
			this.sendLobbyRequestMessage((byte) 2);
		} else {
			LobbyEvent errEvent = new LobbyEvent(null, (byte) 5);
			for (LobbyRequestListener listener : this.lobbyRequestListeners) {
				listener.onJoinLobbyDone(errEvent);
			}

		}
	}

	/**
	* Sends a leave lobby request to the server. Result of the request is provided
	* in the onLeaveLobbyDone callback of the registered LobbyRequestListener
	* objects.
	*/
	public void leaveLobby() {
		if (!this.isNotConnected()) {
			this.sendLobbyRequestMessage((byte) 5);
		} else {
			LobbyEvent errEvent = new LobbyEvent(null, (byte) 5);
			for (LobbyRequestListener listener : this.lobbyRequestListeners) {
				listener.onLeaveLobbyDone(errEvent);
			}

		}
	}

	/**
	* Sends a subscribe lobby request to the server. Result of the request is
	* provided in the onSubscribeLobbyDone callback of the registered
	* LobbyRequestListener objects. Users which have subscribed to the lobby
	* receive chat events from other users in the lobby as well as join/leave
	* notifications about all players to and fro the lobby and all the rooms. In
	* addition, lobby subscribers get notifications when a new room is created or
	* deleted. All these notification events are given in the registered
	* NotifyListener objects.
	*/
	public void subscribeLobby() {
		if (!this.isNotConnected()) {
			this.sendLobbyRequestMessage((byte) 3);
		} else {
			LobbyEvent errEvent = new LobbyEvent(null, (byte) 5);
			for (LobbyRequestListener listener : this.lobbyRequestListeners) {
				listener.onSubscribeLobbyDone(errEvent);
			}

		}
	}

	/**
	* Sends a unsubscribe lobby request to the server. Result of the request is
	* provided in the onUnsubscribeLobbyDone callback of the LobbyRequestListener.
	*/
	public void unsubscribeLobby() {
		if (!this.isNotConnected()) {
			this.sendLobbyRequestMessage((byte) 4);
		} else {
			LobbyEvent errEvent = new LobbyEvent(null, (byte) 5);
			for (LobbyRequestListener listener : this.lobbyRequestListeners) {
				listener.onUnSubscribeLobbyDone(errEvent);
			}

		}
	}

	/**
	* Sends a create room request to the server with the given meta data. Result of
	* the request is provided in the onCreateRoomDone callback of the registered
	* ZoneRequestListener objects. If successful, this will create a dynamic room
	* at the server. These rooms lifetime is limited till the time users are inside
	* it. Read more about Rooms here.
	*
	* @param name
	*            name of the room
	* @param owner
	*            administrator of the room
	* @param maxUsers
	*            number of maximum users allowed in the room
	* @param tableProperties
	*            properties of room for matchmaking ( pass null if not required )
	*/
	public void createRoom(String name, String owner, int maxUsers, HashMap<String, Object> tableProperties) {
		JSONObject properties = new JSONObject();
		if (tableProperties != null && tableProperties.size() > 0) {
			properties = Util.getJsonObjectFromHashtable(tableProperties);
		}

		RoomEvent evt;
		if (properties.toString().getBytes().length > 2048) {
			evt = new RoomEvent(null, (byte) 7);
			for (ZoneRequestListener listener : this.zoneRequestListeners) {
				listener.onCreateRoomDone(evt);
			}

		} else if (this.isNotConnected()) {
			evt = new RoomEvent(null, (byte) 5);
			for (ZoneRequestListener listener : this.zoneRequestListeners) {
				listener.onCreateRoomDone(evt);
			}

		} else if (!this.isNullOrEmpty(name) && !this.isNullOrEmpty(owner) && !this.isNegativeOrZero(maxUsers)) {
			try {
				JSONObject roomObj = new JSONObject();
				roomObj.put("maxUsers", maxUsers);
				roomObj.put("owner", owner);
				roomObj.put("name", name);
				roomObj.put("chatHistory", this.isChatHistory);
				roomObj.put("properties", properties);

				WarpRequestMessage roomCreateMsg = new WarpRequestMessage((byte) 6, this.sessionId, 0, (byte) 0,
						(byte) 0, (byte) 2, roomObj.toString().getBytes().length, roomObj.toString().getBytes());
				this.clientChannel.SendRequest(roomCreateMsg);
			} catch (Exception var10) {
				evt = new RoomEvent(null, (byte) 4);
				for (ZoneRequestListener listener : this.zoneRequestListeners) {
					listener.onCreateRoomDone(evt);
				}
			}

		} else {
			evt = new RoomEvent(null, (byte) 4);
			for (ZoneRequestListener listener : this.zoneRequestListeners) {
				listener.onCreateRoomDone(evt);
			}

		}
	}

	/**
	* Sends a create turn based room request to the server with the given meta
	* data. Result of the request is provided in the onCreateRoomDone callback of
	* the registered ZoneRequestListener objects. If successful, this will create a
	* dynamic turn based room at the server. These rooms lifetime is limited till
	* the time users are inside it. Read more about Rooms here.
	*
	* @param name
	*            name of the room
	* @param owner
	*            owner of the room ( behavior and usage of this meta property is up
	*            to the developer )
	* @param maxUsers
	*            number of maximum users allowed in the room
	* @param tableProperties
	*            properties of room ( can be null )
	* @param time
	*            the time ( in seconds ) allowed for a user to complete its turn
	*            and send a move .
	*/
	public void createTurnRoom(String name, String owner, int maxUsers, HashMap<String, Object> tableProperties,
			int time) {
		JSONObject properties = new JSONObject();
		if (tableProperties != null && tableProperties.size() > 0) {
			properties = Util.getJsonObjectFromHashtable(tableProperties);
		}

		RoomEvent evt;
		if (properties.toString().getBytes().length > 2048) {
			evt = new RoomEvent(null, (byte) 7);
			for (ZoneRequestListener listener : this.zoneRequestListeners) {
				listener.onCreateRoomDone(evt);
			}

		} else if (this.isNotConnected()) {
			evt = new RoomEvent(null, (byte) 5);
			for (ZoneRequestListener listener : this.zoneRequestListeners) {
				listener.onCreateRoomDone(evt);
			}

		} else if (!this.isNullOrEmpty(name) && !this.isNullOrEmpty(owner) && !this.isNegativeOrZero(maxUsers)) {
			try {
				JSONObject roomObj = new JSONObject();
				roomObj.put("maxUsers", maxUsers);
				roomObj.put("owner", owner);
				roomObj.put("name", name);
				roomObj.put("turnTime", time);
				roomObj.put("inox", true);
				roomObj.put("chatHistory", this.isChatHistory);
				roomObj.put("properties", properties);

				WarpRequestMessage roomCreateMsg = new WarpRequestMessage((byte) 6, this.sessionId, 0, (byte) 0,
						(byte) 0, (byte) 2, roomObj.toString().getBytes().length, roomObj.toString().getBytes());
				this.clientChannel.SendRequest(roomCreateMsg);
			} catch (Exception var11) {
				evt = new RoomEvent(null, (byte) 4);
				for (ZoneRequestListener listener : this.zoneRequestListeners) {
					listener.onCreateRoomDone(evt);
				}
			}

		} else {
			evt = new RoomEvent(null, (byte) 4);
			for (ZoneRequestListener listener : this.zoneRequestListeners) {
				listener.onCreateRoomDone(evt);
			}

		}
	}

	/**
	* Sends a move to the server for the joined turn based room.Result of the
	* request is provided in the onSendMoveDone callback of the registered
	* TurnBasedRoomListener objects. If the joined user is not a turn based room or
	* if its not the users turn, this request will fail. If successful, this will
	* result in onMoveCompleted notification for all the subscribed users on the
	* registered NotifyListener objects.
	*
	* @param moveData
	*            any meta data associated with the move
	*/
	public void sendMove(String moveData) {
		this.sendMove(moveData, "");
	}

	public void sendMove(String moveData, String nextTurn) {
		if (this.isNotConnected()) {
			for (TurnBasedRoomListener listener : this.turnBasedRoomListeners) {
				listener.onSendMoveDone((byte) 5);
			}

		} else if (moveData != null && moveData.length() <= 500) {
			try {
				JSONObject chatObj = new JSONObject();
				chatObj.put("moveData", moveData);
				chatObj.put("nextTurn", nextTurn);
				WarpRequestMessage roomMsg = new WarpRequestMessage((byte) 31, this.sessionId, 0, (byte) 0, (byte) 0,
						(byte) 2, chatObj.toString().getBytes().length, chatObj.toString().getBytes());
				this.clientChannel.SendRequest(roomMsg);
			} catch (Exception var6) {
				for (TurnBasedRoomListener listener : this.turnBasedRoomListeners) {
					listener.onSendMoveDone((byte) 6);
				}
			}

		} else {
			for (TurnBasedRoomListener listener : this.turnBasedRoomListeners) {
				listener.onSendMoveDone((byte) 4);
			}

		}
	}

	/**
	* When the onNextTurnRequest is sent by the server, it sends the next turn to
	* the server for the joined turn based room.Result of the request is provided
	* in the onSetNextTurnDone callback of the registered Turn Based Room Listener
	* objects. If the joined user is not in turn based room or if it’s not the
	* user’s turn, this request will fail. If successful, this will result in
	* onMoveCompleted notification for all the subscribed users on the registered
	* NotifyListener objects.
	*
	* @param nextTurn
	*            the string value for the next turn
	*/
	public void setNextTurn(String nextTurn) {
		if (this.isNotConnected()) {
			for (TurnBasedRoomListener listener : this.turnBasedRoomListeners) {
				listener.onSetNextTurnDone((byte) 5);
			}

		} else {
			try {
				JSONObject chatObj = new JSONObject();
				chatObj.put("nextTurn", nextTurn);
				WarpRequestMessage roomMsg = new WarpRequestMessage((byte) 69, this.sessionId, 0, (byte) 0, (byte) 0,
						(byte) 2, chatObj.toString().getBytes().length, chatObj.toString().getBytes());
				this.clientChannel.SendRequest(roomMsg);
			} catch (Exception var5) {
				for (TurnBasedRoomListener listener : this.turnBasedRoomListeners) {
					listener.onSetNextTurnDone((byte) 6);
				}
			}

		}
	}

	/**
	* Sends a start game to server in TurnBasedRoom. Result of this callback is
	* provided in onStartGameDone of registered TurnBasedRoomListener interface. If
	* successful game will be started in TurnBasedRoom.
	*/
	public void startGame() {
		this.startGame(true, "");
	}

	/**
	* Sends a start game to server in TurnBasedRoom. Result of this callback is
	* provided in onStartGameDone of registered TurnBasedRoomListener interface. If
	* successful game will be started in TurnBasedRoom.
	*
	* @param isDefaultLogic
	*            a bool variable.
	*/
	public void startGame(boolean isDefaultLogic) {
		this.startGame(isDefaultLogic, "");
	}

	/**
	* Sends a start game to server in TurnBasedRoom. Result of this callback is
	* provided in onStartGameDone of registered TurnBasedRoomListener interface. If
	* successful game will be started in TurnBasedRoom.
	*
	* @param isDefaultLogic
	*            a bool variable.
	* @param nextTurn
	*            the next turn string value.
	*/
	public void startGame(boolean isDefaultLogic, String nextTurn) {
		if (this.isNotConnected()) {
			for (TurnBasedRoomListener listener : this.turnBasedRoomListeners) {
				listener.onStartGameDone((byte) 5);
			}

		} else {
			try {
				JSONObject chatObj = new JSONObject();
				chatObj.put("isDefaultLogic", isDefaultLogic);
				chatObj.put("nextTurn", nextTurn);
				WarpRequestMessage roomMsg = new WarpRequestMessage((byte) 66, this.sessionId, 0, (byte) 0, (byte) 0,
						(byte) 2, chatObj.toString().getBytes().length, chatObj.toString().getBytes());
				this.clientChannel.SendRequest(roomMsg);
			} catch (Exception var6) {
				for (TurnBasedRoomListener listener : this.turnBasedRoomListeners) {
					listener.onStartGameDone((byte) 6);
				}
			}

		}
	}

	/**
	* Sends a stop game to server in TurnBasedRoom. Result of this callback is
	* provided in onStopGameDone of registered TurnBasedRoomListener interface. If
	* successful game will be stopped in TurnBasedRoom.
	*/
	public void stopGame() {
		if (this.isNotConnected()) {
			for (TurnBasedRoomListener listener : this.turnBasedRoomListeners) {
				listener.onStopGameDone((byte) 5);
			}

		} else {
			try {
				WarpRequestMessage roomMsg = new WarpRequestMessage((byte) 67, this.sessionId, 0, (byte) 0, (byte) 0,
						(byte) 0, "".getBytes().length, "".getBytes());
				this.clientChannel.SendRequest(roomMsg);
			} catch (Exception var4) {
				for (TurnBasedRoomListener listener : this.turnBasedRoomListeners) {
					listener.onStopGameDone((byte) 6);
				}
			}

		}
	}

	/**
	* Sends get move history request to server in TurnBasedRoom. Result of this
	* callback is provided in onGetMoveHistoryDone of registered
	* TurnBasedRoomListener interface.
	*/
	public void getMoveHistory() {
		if (this.isNotConnected()) {
			for (TurnBasedRoomListener listener : this.turnBasedRoomListeners) {
				listener.onGetMoveHistoryDone((byte) 5, null);
			}

		} else {
			try {
				JSONObject chatObj = new JSONObject();
				chatObj.put("count", 5);
				WarpRequestMessage roomMsg = new WarpRequestMessage((byte) 68, this.sessionId, 0, (byte) 0, (byte) 0,
						(byte) 2, chatObj.toString().getBytes().length, chatObj.toString().getBytes());
				this.clientChannel.SendRequest(roomMsg);
			} catch (Exception var4) {
				for (TurnBasedRoomListener listener : this.turnBasedRoomListeners) {
					listener.onGetMoveHistoryDone((byte) 6, null);
				}
			}

		}
	}

	/**
	* Sends a delete room request to the server. Result of the request is provided
	* in the onDeleteRoomDone callback of the registered ZoneRequestListener
	* objects. Only dynamic rooms can be deleted through this API. Static rooms
	* (created from AppHQ) can not be deleted through this. Read more about Rooms
	* here.
	*
	* @param roomId
	*            Id of the room to be deleted
	*/
	public void deleteRoom(String roomId) {
		if (!this.isNotConnected()) {
			this.sendRoomRequest((byte) 11, roomId);
		} else {
			RoomEvent evt = new RoomEvent(null, (byte) 5);

			for (ZoneRequestListener listener : this.zoneRequestListeners) {
				listener.onDeleteRoomDone(evt);
			}

		}
	}

	/**
	* Sends a join room request to the server. Result of the request is provided in
	* the onJoinRoomDone callback of the registered RoomRequestListener objects. A
	* user can only be joined in one location (room or lobby) at a time. If a user
	* joins a room, it will automatically be removed from its current location.
	*
	* @param roomId
	*            Id of the room to be joined
	*/
	public void joinRoom(String roomId) {
		RoomEvent errEvent;
		if (this.isNotConnected()) {
			errEvent = new RoomEvent(null, (byte) 5);
			for (RoomRequestListener listener : this.roomRequestListeners) {
				listener.onJoinRoomDone(errEvent);
			}

		} else if (!this.isNullOrEmpty(roomId)) {
			this.sendRoomRequest((byte) 7, roomId);
		} else {
			errEvent = new RoomEvent(null, (byte) 4);
			for (RoomRequestListener listener : this.roomRequestListeners) {
				listener.onJoinRoomDone(errEvent);
			}

		}
	}

	/**
	* Sends a join and subscribe room request to the server. Result of the request
	* is provided in the onJoinAndSubscribeRoomDone callback of the registered
	* RoomRequestListener objects. A user can only be joined in one location (room
	* or lobby) at a time. If a user joins a room, it will automatically be removed
	* from its current location. Once subscribed, the client will receive all
	* notifications from the room such as chat, update and property change events.
	* In addition the client will also receive notifications when a user joins or
	* leaves the subscribed room. These notifications are given in the registered
	* NotifyListener objects.
	*
	* @param roomId
	*            Id of the room to be join and subscribed
	*/
	public void joinAndSubscribeRoom(String roomId) {
		RoomEvent errEvent;
		if (this.isNotConnected()) {
			errEvent = new RoomEvent(null, (byte) 5);
			for (RoomRequestListener listener : this.roomRequestListeners) {
				listener.onJoinAndSubscribeRoomDone(errEvent);
			}

		} else if (!this.isNullOrEmpty(roomId)) {
			this.sendRoomRequest((byte) 75, roomId);
		} else {
			errEvent = new RoomEvent(null, (byte) 4);
			for (RoomRequestListener listener : this.roomRequestListeners) {
				listener.onJoinAndSubscribeRoomDone(errEvent);
			}

		}
	}

	/**
	* Sends a leave and unsubscribe room request to the server. Result of the
	* request is provided in the onLeaveAndUnsubscribeRoomDone callback of the
	* registered RoomRequestListener objects.
	*
	* @param roomId
	*            Id of the room to be subscribed
	*/
	public void leaveAndUnsubscribeRoom(String roomId) {
		RoomEvent errEvent;
		if (this.isNotConnected()) {
			errEvent = new RoomEvent(null, (byte) 5);
			for (RoomRequestListener listener : this.roomRequestListeners) {
				listener.onLeaveAndUnsubscribeRoomDone(errEvent);
			}

		} else if (!this.isNullOrEmpty(roomId)) {
			this.sendRoomRequest((byte) 76, roomId);
		} else {
			errEvent = new RoomEvent(null, (byte) 4);
			for (RoomRequestListener listener : this.roomRequestListeners) {
				listener.onLeaveAndUnsubscribeRoomDone(errEvent);
			}

		}
	}

	/**
	* Sends a join room request to the server with the condition that the room must
	* have at least minUsers and at Most maxUsers. Result of the request is
	* provided in the onJoinRoomDone callback of the registered
	* RoomRequestListener. This is useful is supporting quick play modes.
	*
	* @param minUser
	*            number of minimum users in room to be joined
	* @param maxUser
	*            number of maximum users in room to be joined
	* @param maxPreferred
	*            flag to specify search priority for room to be joined
	*/
	public void joinRoomInRange(int minUser, int maxUser, boolean maxPreferred) {
		byte errorCode = 0;
		boolean var14 = false;

		RoomEvent event;
		label155 : {
			try {
				var14 = true;
				if (this.isNotConnected()) {
					errorCode = 5;
				}

				if (minUser < 0 || maxUser < 0 || minUser > maxUser) {
					errorCode = 4;
				}

				if (errorCode == 0) {
					this.sendRangeMatchMakingRequest((byte) 37, minUser, maxUser, maxPreferred);
					var14 = false;
				} else {
					var14 = false;
				}
				break label155;
			} catch (Exception var15) {
				errorCode = 6;
				var14 = false;
			} finally {
				if (var14) {
					if (errorCode != 0) {
						event = new RoomEvent(null, errorCode);
						for (RoomRequestListener listener : this.roomRequestListeners) {
							listener.onJoinRoomDone(event);
						}
					}

				}
			}

			if (errorCode != 0) {
				event = new RoomEvent(null, errorCode);
				for (RoomRequestListener listener : this.roomRequestListeners) {
					listener.onJoinRoomDone(event);
				}
			}

			return;
		}

		if (errorCode != 0) {
			event = new RoomEvent(null, errorCode);
			for (RoomRequestListener listener : this.roomRequestListeners) {
				listener.onJoinRoomDone(event);
			}
		}

	}

	/**
	* Updates the properties associated with the given room on the server. Result
	* is provided in the onUpdatePropertyDone callback of the registered
	* RoomRequestListener objects. Properties which are not found on the server,
	* will be added while properties which are already present will simply be
	* updated with the new values. You can also specify the list of properties that
	* you want to remove from the remove. Update property will fail if any other
	* user has lock on same property that you are going to update or remove. This
	* request (if successful) will also result in an onUserChangeRoomProperty
	* notification on the registered NotifyListener objects to be triggered for all
	* subscribed users of the room.
	*
	* @param roomID
	*            Id of the room
	* @param tableProperties
	*            properties that will be set for the room
	* @param removeArray
	*            properties that will be removed for the room
	*/
	public void updateRoomProperties(String roomID, HashMap<String, Object> tableProperties, String[] removeArray) {
		if (this.isNotConnected()) {
			LiveRoomInfoEvent liveRoomInfoEvent = new LiveRoomInfoEvent(null, (byte) 5, null, null);
			for (RoomRequestListener listener : this.roomRequestListeners) {
				listener.onUpdatePropertyDone(liveRoomInfoEvent);
			}
		} else {
			JSONObject properties = null;
			String removeProperties = "";
			if (tableProperties != null && tableProperties.size() > 0) {
				properties = Util.getJsonObjectFromHashtable(tableProperties);
			}

			if (removeArray != null) {
				removeProperties = "";

				for (int i = 0; i < removeArray.length; ++i) {
					if (i < removeArray.length - 1) {
						removeProperties = removeProperties.concat(removeArray[i] + ";");
					} else {
						removeProperties = removeProperties.concat(removeArray[i]);
					}
				}
			}

			this.updateRoomPropertiesRequest((byte) 25, roomID, properties, removeProperties);
		}

	}

	/**
	* Lock the properties associated with the joined room on the server for
	* requested user. Result is provided in the onLockPropertyDone callback of the
	* registered RoomRequestListener objects. Lock properties will fail if any
	* other user has lock on same property, otherwise property will be added in
	* lockTable with owner name. This request (if successful) will also result in
	* an onUserChangeRoomProperty notification on the registered NotifyListener
	* objects to be triggered for all subscribed users of the room.
	*
	* @param tableProperties
	*            properties to be lock for the room
	*/
	public void lockProperties(HashMap<String, Object> tableProperties) {
		if (this.isNotConnected()) {
			for (RoomRequestListener listener : this.roomRequestListeners) {
				listener.onLockPropertiesDone((byte) 5);
			}
		} else {
			JSONObject properties = null;
			if (tableProperties != null && tableProperties.size() > 0) {
				properties = Util.getJsonObjectFromHashtable(tableProperties);
			}

			this.sendLockPropertiesRequest((byte) 35, properties);
		}

	}

	/**
	* Unlock the properties associated with the joined room on the server for
	* requested user. Result is provided in the onUnlockPropertyDone callback of
	* the registered RoomRequestListener objects. Unlock properties will fail if
	* any other user has lock on same property, otherwise property will be removed
	* from lock table. This request (if successful) will also result in an
	* onUserChangeRoomProperty notification on the registered NotifyListener
	* objects to be triggered for all subscribed users of the room.
	*
	* @param unlockProperties
	*            properties to be unlock for the room
	*/
	public void unlockProperties(String[] unlockProperties) {
		if (this.isNotConnected()) {
			for (RoomRequestListener listener : this.roomRequestListeners) {
				listener.onUnlockPropertiesDone((byte) 5);
			}
		} else {
			String unlock = "";
			if (unlockProperties != null) {
				unlock = "";

				for (int i = 0; i < unlockProperties.length; ++i) {
					if (i < unlockProperties.length - 1) {
						unlock = unlock.concat(unlockProperties[i] + ";");
					} else {
						unlock = unlock.concat(unlockProperties[i]);
					}
				}
			}

			this.sendUnlockPropertiesRequest((byte) 36, unlock);
		}

	}

	/**
	* Sends a join room request to the server with the condition that the room must
	* have a matching set of property value pairs associated with it. This is
	* useful in match making. Result of the request is provided in the
	* onJoinRoomDone callback of the registered RoomRequestListener.
	*
	* @param tableProperties
	*            properties of the room to be joined
	*/
	public void joinRoomWithProperties(HashMap<String, Object> tableProperties) {
		byte errorCode = 0;
		boolean var12 = false;

		RoomEvent event;
		label136 : {
			try {
				var12 = true;
				if (this.isNotConnected()) {
					errorCode = 5;
				}

				if (errorCode == 0) {
					this.sendPropertyMatchMakingRequest((byte) 27, tableProperties);
					var12 = false;
				} else {
					var12 = false;
				}
				break label136;
			} catch (Exception var13) {
				errorCode = 6;
				var12 = false;
			} finally {
				if (var12) {
					if (errorCode != 0) {
						event = new RoomEvent(null, errorCode);
						for (RoomRequestListener listener : this.roomRequestListeners) {
							listener.onJoinRoomDone(event);
						}
					}

				}
			}

			if (errorCode != 0) {
				event = new RoomEvent(null, errorCode);
				for (RoomRequestListener listener : this.roomRequestListeners) {
					listener.onJoinRoomDone(event);
				}
			}

			return;
		}

		if (errorCode != 0) {
			event = new RoomEvent(null, errorCode);
			for (RoomRequestListener listener : this.roomRequestListeners) {
				listener.onJoinRoomDone(event);
			}
		}

	}

	/**
	* Retrieves information of the rooms that contain at least minUsers and at most
	* maxUsers in them. Result is provided in the onGetMatchedRoomsDone callback of
	* the registered ZoneRequestListener objects. This is useful in building a
	* filtered list of rooms.
	*
	* @param minUser
	*            number of minimun users in room to be joined
	* @param maxUsers
	*            number of maximum users in room to be joined
	*/
	public void getRoomInRange(int minUser, int maxUsers) {
		byte errorCode = 0;
		boolean var13 = false;

		MatchedRoomsEvent event;
		label155 : {
			try {
				var13 = true;
				if (this.isNotConnected()) {
					errorCode = 5;
				}

				if (minUser < 0 || maxUsers < 0 || minUser > maxUsers) {
					errorCode = 4;
				}

				if (errorCode == 0) {
					this.sendRangeMatchMakingRequest((byte) 38, minUser, maxUsers, false);
				}
				var13 = false;
				break label155;
			} catch (Exception var14) {
				errorCode = 6;
				var13 = false;
			} finally {
				if (var13) {
					if (errorCode != 0) {
						event = new MatchedRoomsEvent(errorCode, null);
						for (ZoneRequestListener listener : this.zoneRequestListeners) {
							listener.onGetMatchedRoomsDone(event);
						}
					}

				}
			}

			if (errorCode != 0) {
				event = new MatchedRoomsEvent(errorCode, null);
				for (ZoneRequestListener listener : this.zoneRequestListeners) {
					listener.onGetMatchedRoomsDone(event);
				}
			}

			return;
		}

		if (errorCode != 0) {
			event = new MatchedRoomsEvent(errorCode, null);
			for (ZoneRequestListener listener : this.zoneRequestListeners) {
				listener.onGetMatchedRoomsDone(event);
			}
		}

	}

	/**
	* Retrieves information of the room that contain properties which match with
	* the given properties. Result is provided in the onGetMatchedRoomsDone
	* callback of the registered ZoneRequestListener objects. This is useful in
	* building a filtered list of rooms.
	*
	* @param properties
	*            properties of the room to be joined
	*/
	public void getRoomWithProperties(HashMap<String, Object> properties) {
		byte errorCode = 0;
		boolean var12 = false;

		MatchedRoomsEvent event;
		label136 : {
			try {
				var12 = true;
				if (this.isNotConnected()) {
					errorCode = 5;
				}

				if (errorCode == 0) {
					this.sendPropertyMatchMakingRequest((byte) 29, properties);
					var12 = false;
				} else {
					var12 = false;
				}
				break label136;
			} catch (Exception var13) {
				errorCode = 6;
				var12 = false;
			} finally {
				if (var12) {
					if (errorCode != 0) {
						event = new MatchedRoomsEvent(errorCode, null);
						for (ZoneRequestListener listener : this.zoneRequestListeners) {
							listener.onGetMatchedRoomsDone(event);
						}
					}

				}
			}

			if (errorCode != 0) {
				event = new MatchedRoomsEvent(errorCode, null);
				for (ZoneRequestListener listener : this.zoneRequestListeners) {
					listener.onGetMatchedRoomsDone(event);
				}
			}

			return;
		}

		if (errorCode != 0) {
			event = new MatchedRoomsEvent(errorCode, null);
			for (ZoneRequestListener listener : this.zoneRequestListeners) {
				listener.onGetMatchedRoomsDone(event);
			}
		}

	}

	/**
	* Retrieves information of the rooms that contain at least minUsers , at most
	* maxUsers and set of property value pairs in them. Result is provided in the
	* onGetMatchedRoomsDone callback of the registered ZoneRequestListener objects.
	* This is useful in building a filtered list of rooms.
	*
	* @param minUser
	*            number of minimun users in room to be joined
	* @param maxUsers
	*            number of maximum users in room to be joined
	* @param properties
	*            properties of the room to be joined
	*/
	public void getRoomInRangeWithProperties(int minUser, int maxUsers, HashMap<String, Object> properties) {
		byte errorCode = 0;
		boolean var14 = false;

		MatchedRoomsEvent event;
		label159 : {
			try {
				var14 = true;
				if (this.isNotConnected()) {
					errorCode = 5;
				}

				if (minUser < 0 || maxUsers < 0 || minUser > maxUsers || properties.isEmpty()) {
					errorCode = 4;
				}

				if (errorCode == 0) {
					this.sendRangePropertiesMatchMakingRequest((byte) 73, minUser, maxUsers, false, properties);
				}
				var14 = false;
				break label159;
			} catch (Exception var15) {
				errorCode = 6;
				var14 = false;
			} finally {
				if (var14) {
					if (errorCode != 0) {
						event = new MatchedRoomsEvent(errorCode, null);
						for (ZoneRequestListener listener : this.zoneRequestListeners) {
							listener.onGetMatchedRoomsDone(event);
						}
					}

				}
			}

			if (errorCode != 0) {
				event = new MatchedRoomsEvent(errorCode, null);
				for (ZoneRequestListener listener : this.zoneRequestListeners) {
					listener.onGetMatchedRoomsDone(event);
				}
			}
			return;
		}

		if (errorCode != 0) {
			event = new MatchedRoomsEvent(errorCode, null);
			for (ZoneRequestListener listener : this.zoneRequestListeners) {
				listener.onGetMatchedRoomsDone(event);
			}
		}

	}

	/**
	* Sends a leave room request to the server. Result of the request is provided
	* in the onLeaveRoomDone callback of the registered RoomRequestListener.
	*
	* @param roomId
	*            Id of the room to be left
	*/
	public void leaveRoom(String roomId) {
		RoomEvent errEvent;
		if (this.isNotConnected()) {
			errEvent = new RoomEvent(null, (byte) 5);
			for (RoomRequestListener listener : this.roomRequestListeners) {

				listener.onLeaveRoomDone(errEvent);
			}

		} else if (!this.isNullOrEmpty(roomId)) {
			this.sendRoomRequest((byte) 10, roomId);
		} else {
			errEvent = new RoomEvent(null, (byte) 4);
			for (RoomRequestListener listener : this.roomRequestListeners) {
				listener.onLeaveRoomDone(errEvent);
			}

		}
	}

	/**
	* Sends a subscribe room request to the server. Result of the request is
	* provided in the onSubscribeRoomDone callback of the registered
	* RoomRequestListener objects. Once subscribed, the client will receive all
	* notifications from the room such as chat, update and property change events.
	* In addition the client will also receive notifications when a user joins or
	* leaves the subscribed room. These notifications are given in the registered
	* NotifyListener objects.
	*
	* @param roomId
	*            Id of the room to be subscribed
	*/
	public void subscribeRoom(String roomId) {
		if (!this.isNotConnected()) {
			this.sendRoomRequest((byte) 8, roomId);
		} else {
			RoomEvent errEvent = new RoomEvent(null, (byte) 5);
			for (RoomRequestListener listener : this.roomRequestListeners) {
				listener.onSubscribeRoomDone(errEvent);
			}

		}
	}

	/**
	* Sends a unsubscribe room request to the server. Result of the request is
	* provided in the onUnSubscribeRoomDone callback of the registered
	* RoomRequestListener objects.
	*
	* @param roomId
	*            Id of the room to be subscribed
	*/
	public void unsubscribeRoom(String roomId) {
		if (!this.isNotConnected()) {
			this.sendRoomRequest((byte) 9, roomId);
		} else {
			RoomEvent errEvent = new RoomEvent(null, (byte) 5);
			for (RoomRequestListener listener : this.roomRequestListeners) {
				listener.onUnSubscribeRoomDone(errEvent);
			}

		}
	}

	/**
	* Sends a chat message to the room (or lobby) in which the user is currently
	* joined. Result of the request is provided in the onSendChatDone callback of
	* the registered ChatRequestListener objects. All users who are subscribed to
	* the location in which the message is sent will get a onChatReceived event on
	* their registered NotifyListener objects.
	*
	* @param message
	*            message to be sent
	*/
	public void sendChat(String message) {
		if (this.isNotConnected()) {
			for (ChatRequestListener listener : this.chatRequestListeners) {
				listener.onSendChatDone((byte) 5);
			}

		} else if (message != null && message.length() <= 500) {
			try {
				JSONObject chatObj = new JSONObject();
				chatObj.put("chat", message);
				WarpRequestMessage roomMsg = new WarpRequestMessage((byte) 12, this.sessionId, 0, (byte) 0, (byte) 0,
						(byte) 2, chatObj.toString().getBytes().length, chatObj.toString().getBytes());
				this.clientChannel.SendRequest(roomMsg);
			} catch (Exception var5) {
				for (ChatRequestListener listener : this.chatRequestListeners) {
					listener.onSendChatDone((byte) 4);
				}
			}

		} else {
			for (ChatRequestListener listener : this.chatRequestListeners) {
				listener.onSendChatDone((byte) 4);
			}

		}
	}

	/**
	* Sends a byte array update message to the recipient user. This is useful if
	* developers want to send private data between the users. It is unreliable and
	* may not work over cellular data connections – hence no result callback should
	* be expected from it. The corresponding flavor of this API is
	* sendPrivateUpdate which shows a similar behavior.
	*
	* @param toUsername
	*            recipient username
	* @param update
	*            byte array data to be sent
	*/
	public void sendUDPPrivateUpdate(String toUsername, byte[] update) {
		if (!this.isNotConnected() && this.udpListener != null && update.length <= 1000
				&& !this.isNullOrEmpty(toUsername)) {
			try {
				int namelen = toUsername.getBytes().length;
				byte[] payload = new byte[update.length + namelen + 1];
				payload[0] = (byte) namelen;
				System.arraycopy(toUsername.getBytes(), 0, payload, 1, namelen);
				System.arraycopy(update, 0, payload, 1 + namelen, update.length);
				WarpRequestMessage pvtUpdateMsg = new WarpRequestMessage((byte) 32, this.sessionId, 0, (byte) 0,
						(byte) 2, (byte) 1, payload.length, payload);
				this.udpListener.SendRequest(pvtUpdateMsg);
			} catch (Exception var6) {
				Util.trace(var6.getMessage());
			}

		}
	}

	/**
	* Sends a byte array update message to recipient user. Result of the request is
	* provided in the onSendPrivateUpdateDone callback of the registered
	* UpdateRequestListener objects. Remote user will get a onPrivateUpdateReceived
	* event on their registered NotifyListener objects. This is useful if
	* developers want to send private data between the users. The corresponding UDP
	* flavor of this API is sendUDPPrivateUpdate, which is unreliable and may not
	* work over cellular data connections – hence no result callback should be
	* expected from it. The behavior is otherwise similar.
	*
	* @param toUsername
	*            recipient username
	* @param update
	*            byte array data to be sent
	*/
	public void sendPrivateUpdate(String toUsername, byte[] update) {
		if (this.isNotConnected()) {
			for (UpdateRequestListener listener : this.updateRequestListeners) {
				listener.onSendPrivateUpdateDone((byte) 5);
			}

		} else if (update != null && update.length <= 1000 && !this.isNullOrEmpty(toUsername)) {
			int namelen = toUsername.getBytes().length;
			byte[] payload = new byte[update.length + namelen + 1];
			payload[0] = (byte) namelen;
			System.arraycopy(toUsername.getBytes(), 0, payload, 1, namelen);
			System.arraycopy(update, 0, payload, 1 + namelen, update.length);
			WarpRequestMessage pvtUpdateMsg = new WarpRequestMessage((byte) 32, this.sessionId, 0, (byte) 0, (byte) 0,
					(byte) 1, payload.length, payload);
			this.clientChannel.SendRequest(pvtUpdateMsg);
		} else {
			for (UpdateRequestListener listener : this.updateRequestListeners) {
				listener.onSendPrivateUpdateDone((byte) 4);
			}

		}
	}

	/**
	* Sends a private message to the given user if its online. Result of the
	* request is provided in the onSendPrivateChatDone callback of the registered
	* ChatRequestListener objects. The sender and receiver don’t need to be in the
	* same room or lobby for the private message to be delivered. This is useful in
	* building invite/challenge friend like scenarios. If successful, the receiver
	* will get a onPrivateChatReceived event on its registered NotifyListener
	* objects.
	*
	* @param userName
	*            recipient of the message
	* @param message
	*            message to be sent
	*/
	public void sendPrivateChat(String userName, String message) {
		if (this.isNotConnected()) {
			for (ChatRequestListener listener : this.chatRequestListeners) {
				listener.onSendPrivateChatDone((byte) 5);
			}

		} else if (message != null && message.length() <= 500) {
			try {
				JSONObject chatObj = new JSONObject();
				chatObj.put("to", userName);
				chatObj.put("chat", message);
				WarpRequestMessage roomMsg = new WarpRequestMessage((byte) 30, this.sessionId, 0, (byte) 0, (byte) 0,
						(byte) 2, chatObj.toString().getBytes().length, chatObj.toString().getBytes());
				this.clientChannel.SendRequest(roomMsg);
			} catch (Exception var6) {
				for (ChatRequestListener listener : this.chatRequestListeners) {
					listener.onSendPrivateChatDone((byte) 4);
				}
			}

		} else {
			for (ChatRequestListener listener : this.chatRequestListeners) {
				listener.onSendPrivateChatDone((byte) 4);
			}

		}
	}

	/**
	* Sends a byte array update message to the room to which the user is currently
	* joined. This is useful if developers want to send their own binary encoded
	* data across and is useful in minimizing the payload exchanged between the
	* clients and AppWarp cloud server. The frequency at which these messages can
	* be processed is not restricted by the server. However, the latency involved
	* is largely determined by the client’s bandwidth. The size of the byte array
	* must not exceed 1000. It is unreliable and may not work over cellular data
	* connections – hence no result callback should be expected from it.The
	* corresponding UDP flavor of this API is sendUpdatePeers which shows a similar
	* behavior.
	*
	* @param update
	*            byte array data to be sent
	*/
	public void sendUDPUpdatePeers(byte[] update) {
		if (!this.isNotConnected() && this.udpListener != null && update.length <= 1000) {
			try {
				WarpRequestMessage updateMsg = new WarpRequestMessage((byte) 13, this.sessionId, 0, (byte) 0, (byte) 2,
						(byte) 1, update.length, update);
				this.udpListener.SendRequest(updateMsg);
			} catch (Exception var3) {
				Util.trace(var3.getMessage());
			}

		}
	}

	/**
	* This API checks if device has a full duplex UDP connection or not with the
	* AppWarp server. Incoming UDP Traffic may be blocked if the client is behind
	* certain types of NATs(Network address translation). It determines the
	* connectivity by performing a 3-way handshake with the server over UDP and
	* provides the result in ConnectionListener.onInitUDPDone. In case of lack of
	* connectivity, the server will fall back to sending updates over TCP for the
	* client. Sending can continue over UDP irrespective. ResultCode: 1.
	* WarpResponseResultCode.SUCCESS : If the client has full duplex UDP
	* connectivity. 2. WarpResponseResultCode.BAD_REQUEST : If the client is unable
	* to receive UDP traffic from the server.
	*/
	public void initUDP() {
		if (this.isNotConnected()) {
			Util.trace("Can't initUDP till connected successfully");
		} else {
			this.udpListener = new UDPListener();
			this.udpListenerThread = new Thread(this.udpListener);

			try {
				WarpRequestMessage updateMsg = new WarpRequestMessage((byte) 64, this.sessionId, 0, (byte) 0, (byte) 2,
						(byte) 1, 0, null);
				this.udpListener.SendRequest(updateMsg);
				this.udpListenerThread.start();
			} catch (Exception var2) {
				Util.trace(var2.getMessage());
			}

		}
	}

	/**
	* Sends a byte array update message to room in which the user is currently
	* joined. Result of the request is provided in the onSendUpdateDone callback of
	* the registered UpdateRequestListener objects. All users who are subscribed to
	* the room in which the update is sent will get a onUpdatePeersReceived event
	* on their registered NotifyListener objects. This is useful if developers want
	* to send their own binary encoded data across and is useful in minimizing the
	* payload exchanged between the clients and AppWarp cloud server. The size of
	* each message should be limited to 1000 bytes. The frequency at which these
	* messages can be processed is not restricted by the server. However, the
	* latency involved is largely determined by the client’s bandwidth. The
	* corresponding UDP flavor of this API is sendUdpUpdatePeers, which is
	* unreliable and may not work over cellular data connections – hence no result
	* callback should be expected from it. The behavior is otherwise similar.
	*
	* @param update
	*            binary data to be sent
	*/
	public void sendUpdatePeers(byte[] update) {
		if (this.isNotConnected()) {
			for (UpdateRequestListener listener : this.updateRequestListeners) {
				listener.onSendUpdateDone((byte) 5);
			}

		} else if (update.length > 1000) {
			for (UpdateRequestListener listener : this.updateRequestListeners) {
				listener.onSendUpdateDone((byte) 4);
			}

		} else {
			try {
				WarpRequestMessage updateMsg = new WarpRequestMessage((byte) 13, this.sessionId, 0, (byte) 0, (byte) 0,
						(byte) 1, update.length, update);
				this.clientChannel.SendRequest(updateMsg);
			} catch (Exception var5) {
				for (UpdateRequestListener listener : this.updateRequestListeners) {
					listener.onSendUpdateDone((byte) 6);
				}
			}

		}
	}

	private void sendLobbyRequestMessage(byte code) {
		try {
			JSONObject lobbyObj = new JSONObject();
			lobbyObj.put("isPrimary", true);
			WarpRequestMessage roomCreateMsg = new WarpRequestMessage(code, this.sessionId, 0, (byte) 0, (byte) 0,
					(byte) 2, lobbyObj.toString().getBytes().length, lobbyObj.toString().getBytes());
			this.clientChannel.SendRequest(roomCreateMsg);
		} catch (Exception var6) {
			LobbyEvent errEvent = new LobbyEvent(null, (byte) 4);
			if (code == 2) {
				for (LobbyRequestListener listener : this.lobbyRequestListeners) {
					listener.onJoinLobbyDone(errEvent);
				}
			}

			if (code == 5) {
				for (LobbyRequestListener listener : this.lobbyRequestListeners) {
					listener.onLeaveLobbyDone(errEvent);
				}
			}

			if (code == 3) {
				for (LobbyRequestListener listener : this.lobbyRequestListeners) {
					listener.onSubscribeLobbyDone(errEvent);
				}
			}

			if (code == 4) {
				for (LobbyRequestListener listener : this.lobbyRequestListeners) {
					listener.onUnSubscribeLobbyDone(errEvent);
				}
			}
		}

	}

	private void sendCustomDataRequest(byte code, String key, String data) {
		try {
			JSONObject reqObj = new JSONObject();
			if (code == 21) {
				reqObj.put("id", key);
			} else {
				reqObj.put("name", key);
			}

			reqObj.put("data", data);
			WarpRequestMessage roomMsg = new WarpRequestMessage(code, this.sessionId, 0, (byte) 0, (byte) 0, (byte) 2,
					reqObj.toString().getBytes().length, reqObj.toString().getBytes());
			this.clientChannel.SendRequest(roomMsg);
		} catch (Exception var8) {
			if (code == 21) {
				LiveRoomInfoEvent evt = new LiveRoomInfoEvent(null, (byte) 4, null, null);
				for (RoomRequestListener listener : this.roomRequestListeners) {
					listener.onSetCustomRoomDataDone(evt);
				}
			} else if (code == 22) {
				LiveUserInfoEvent evt = new LiveUserInfoEvent((byte) 4, null, null, null, false, false);
				for (ZoneRequestListener listener : this.zoneRequestListeners) {
					listener.onSetCustomUserDataDone(evt);
				}
			}
		}

	}

	private void sendRoomRequest(byte code, String id) {
		try {
			JSONObject roomObj = new JSONObject();
			roomObj.put("id", id);
			WarpRequestMessage roomMsg = new WarpRequestMessage(code, this.sessionId, 0, (byte) 0, (byte) 0, (byte) 2,
					roomObj.toString().getBytes().length, roomObj.toString().getBytes());
			this.clientChannel.SendRequest(roomMsg);
		} catch (Exception var8) {
			RoomEvent errEvent = new RoomEvent(null, (byte) 4);
			if (code == 7) {
				for (RoomRequestListener listener : this.roomRequestListeners) {
					listener.onJoinRoomDone(errEvent);
				}
			}

			if (code == 10) {
				for (RoomRequestListener listener : this.roomRequestListeners) {
					listener.onLeaveRoomDone(errEvent);
				}
			}

			if (code == 8) {
				for (RoomRequestListener listener : this.roomRequestListeners) {
					listener.onSubscribeRoomDone(errEvent);
				}
			}

			if (code == 9) {
				for (RoomRequestListener listener : this.roomRequestListeners) {
					listener.onUnSubscribeRoomDone(errEvent);
				}
			}

			if (code == 20) {
				LiveRoomInfoEvent event = new LiveRoomInfoEvent(null, (byte) 4, null, null);
				for (RoomRequestListener listener : this.roomRequestListeners) {
					listener.onGetLiveRoomInfoDone(event);
				}
			}
		}

	}

	private void sendRangeMatchMakingRequest(byte code, int minUser, int maxUser, boolean maxPreferred)
			throws JSONException {
		JSONObject roomObj = new JSONObject();
		roomObj.put("minUsers", minUser);
		roomObj.put("maxUsers", maxUser);
		roomObj.put("maxPreferred", maxPreferred);
		WarpRequestMessage roomMsg = new WarpRequestMessage(code, this.sessionId, 0, (byte) 0, (byte) 0, (byte) 2,
				roomObj.toString().getBytes().length, roomObj.toString().getBytes());
		this.clientChannel.SendRequest(roomMsg);
	}

	private void sendPropertyMatchMakingRequest(byte code, HashMap<String, Object> tableProperties)
			throws JSONException {
		JSONObject properties = null;
		if (tableProperties != null && tableProperties.size() > 0) {
			properties = Util.getJsonObjectFromHashtable(tableProperties);
		}

		JSONObject roomObj = new JSONObject();
		roomObj.put("properties", properties);
		WarpRequestMessage roomMsg = new WarpRequestMessage(code, this.sessionId, 0, (byte) 0, (byte) 0, (byte) 2,
				roomObj.toString().getBytes().length, roomObj.toString().getBytes());
		this.clientChannel.SendRequest(roomMsg);
	}

	private void sendRangePropertiesMatchMakingRequest(byte code, int minUser, int maxUser, boolean maxPreferred,
			HashMap<String, Object> tableProperties) throws JSONException {
		JSONObject properties = null;
		if (tableProperties != null && tableProperties.size() > 0) {
			properties = Util.getJsonObjectFromHashtable(tableProperties);
		}

		JSONObject roomObj = new JSONObject();
		roomObj.put("minUsers", minUser);
		roomObj.put("maxUsers", maxUser);
		roomObj.put("maxPreferred", maxPreferred);
		roomObj.put("properties", properties);
		WarpRequestMessage roomMsg = new WarpRequestMessage(code, this.sessionId, 0, (byte) 0, (byte) 0, (byte) 2,
				roomObj.toString().getBytes().length, roomObj.toString().getBytes());
		this.clientChannel.SendRequest(roomMsg);
	}

	private void updateRoomPropertiesRequest(byte code, String id, JSONObject property, String removeProperties) {
		try {
			JSONObject roomObj = new JSONObject();
			roomObj.put("id", id);
			roomObj.put("addOrUpdate", property);
			roomObj.put("remove", removeProperties);
			WarpRequestMessage roomMsg = new WarpRequestMessage(code, this.sessionId, 0, (byte) 0, (byte) 0, (byte) 2,
					roomObj.toString().getBytes().length, roomObj.toString().getBytes());
			this.clientChannel.SendRequest(roomMsg);
		} catch (Exception var9) {
			if (code == 25) {
				LiveRoomInfoEvent liveRoomInfoEvent = new LiveRoomInfoEvent(null, (byte) 4, null, null);
				for (RoomRequestListener listener : this.roomRequestListeners) {
					listener.onUpdatePropertyDone(liveRoomInfoEvent);
				}
			}
		}

	}

	private void sendLockPropertiesRequest(byte code, JSONObject property) {
		try {
			JSONObject roomObj = new JSONObject();
			roomObj.put("lockProperties", property);
			WarpRequestMessage roomMsg = new WarpRequestMessage(code, this.sessionId, 0, (byte) 0, (byte) 0, (byte) 2,
					roomObj.toString().getBytes().length, roomObj.toString().getBytes());
			this.clientChannel.SendRequest(roomMsg);
		} catch (Exception var6) {
			if (code == 35) {
				for (RoomRequestListener listener : this.roomRequestListeners) {
					listener.onLockPropertiesDone((byte) 4);
				}
			}
		}

	}

	private void sendUnlockPropertiesRequest(byte code, String unlockProperties) {
		try {
			JSONObject roomObj = new JSONObject();
			roomObj.put("unlockProperties", unlockProperties);
			WarpRequestMessage roomMsg = new WarpRequestMessage(code, this.sessionId, 0, (byte) 0, (byte) 0, (byte) 2,
					roomObj.toString().getBytes().length, roomObj.toString().getBytes());
			this.clientChannel.SendRequest(roomMsg);
		} catch (Exception var6) {
			if (code == 36) {
				for (RoomRequestListener listener : this.roomRequestListeners) {
					listener.onUnlockPropertiesDone((byte) 4);
				}
			}
		}

	}

	private RoomData buildRoomData(JSONObject objData) throws JSONException {
		String id = objData.getString("id");
		String name = objData.getString("name");
		String owner = objData.getString("owner");
		int max = objData.getInt("maxUsers");
		return new RoomData(id, owner, name, max);
	}

	private LobbyData buildLobbyData(JSONObject objData) throws JSONException {
		String id = objData.getString("id");
		String name = objData.getString("name");
		String owner = objData.getString("owner");
		int max = objData.getInt("maxUsers");
		return new LobbyData(id, owner, name, max, true);
	}

	private void fireConnectionEvent(byte resultCode) {
		ConnectEvent event = new ConnectEvent(resultCode);
		Object[] listenersCopy = this.ConnectionRequestListeners.toArray();

		for (Object o : listenersCopy) {
			ConnectionRequestListener listener = (ConnectionRequestListener) o;
			if (this.ConnectionRequestListeners.contains(listener)) {
				listener.onConnectDone(event);
			}
		}

	}

	private void fireConnectionEvent(byte resultCode, int reasonCode) {
		ConnectEvent event = new ConnectEvent(resultCode, reasonCode);
		Object[] listenersCopy = this.ConnectionRequestListeners.toArray();

		for (Object o : listenersCopy) {
			ConnectionRequestListener listener = (ConnectionRequestListener) o;
			if (this.ConnectionRequestListeners.contains(listener)) {
				listener.onConnectDone(event);
			}
		}

	}

	protected void fireUDPEvent(byte resultCode) {
		Object[] listenersCopy = this.ConnectionRequestListeners.toArray();

		for (Object o : listenersCopy) {
			ConnectionRequestListener listener = (ConnectionRequestListener) o;
			if (this.ConnectionRequestListeners.contains(listener)) {
				listener.onInitUDPDone(resultCode);
			}
		}

	}

	private void fireDisconnectEvent(byte resultCode) {
		ConnectEvent event = new ConnectEvent(resultCode);
		Object[] listenersCopy = this.ConnectionRequestListeners.toArray();

		for (Object o : listenersCopy) {
			ConnectionRequestListener listener = (ConnectionRequestListener) o;
			if (this.ConnectionRequestListeners.contains(listener)) {
				listener.onDisconnectDone(event);
			}
		}

	}

	private boolean isNullOrEmpty(String check) {
		return check == null || check.length() <= 0;
	}

	private boolean isNotConnected() {
		return this.getConnectionState() != 0;
	}

	private boolean isNegativeOrZero(int value) {
		return value <= 0;
	}

	private void startChannelConnectTimer() {
		this.connectTimeoutTask = new WarpClient.ConnectionWatchTask(this);
		this.connectionTimer = new Timer();
		this.connectionTimer.schedule(this.connectTimeoutTask, 6000L);
	}

	public void onLookUpServer(byte lookUpaStatus) {
		if (this.connectionState == 1 || this.connectionState == 4) {
			if (lookUpaStatus == 0) {
				this.startChannelConnectTimer();
				this.clientChannel = new ClientChannel(Util.WarpServerHost, 12346);
				Thread t = new Thread(this.clientChannel);
				t.setDaemon(false);
				t.start();
			} else {
				this.connectionState = 2;
				if (lookUpaStatus == 1) {
					this.fireConnectionEvent(lookUpaStatus, 22);
				} else {
					this.fireConnectionEvent(lookUpaStatus);
				}
			}

		}
	}

	public void getGameStatus() {
		if (this.isNotConnected()) {
			for (TurnBasedRoomListener listener : this.turnBasedRoomListeners) {
				listener.onGetGameStatusDone((byte) 5, false);
			}

		} else {
			try {
				WarpRequestMessage roomMsg = new WarpRequestMessage((byte) 77, this.sessionId, 0, (byte) 0, (byte) 0,
						(byte) 0, "".getBytes().length, "".getBytes());
				this.clientChannel.SendRequest(roomMsg);
			} catch (Exception var4) {
				for (TurnBasedRoomListener listener : this.turnBasedRoomListeners) {
					listener.onGetGameStatusDone((byte) 6, false);
				}
			}

		}
	}

	/**
	* Sends a chat message with two more parameters specially used if chat history
	* is enabled. Here user need to provide boolean variable(i.e true/false) for
	* either save this chat or not and roomId of joined room. Result of the request
	* is provided in the onSendChatDone callback of the registered
	* ChatRequestListener objects. All users who are subscribed to the location in
	* which the message is sent will get a onChatReceived event on their registered
	* NotifyListener objects.
	*
	* @param message
	*            message to be sent
	* @param saveHistory
	*            Boolean variable indicating sent message needs to be saved or not.
	* @param roomId
	*            Id of the room
	*/
	public void sendChat(String message, boolean saveHistory, String roomId) {
		if (this.isNotConnected()) {
			for (ChatRequestListener listener : this.chatRequestListeners) {
				listener.onSendChatDone((byte) 5);
			}

		} else if (message != null && message.length() <= 500) {
			if (roomId != null && roomId.length() != 0) {
				try {
					JSONObject chatObj = new JSONObject();
					chatObj.put("chat", message);
					WarpRequestMessage roomMsg = new WarpRequestMessage((byte) 12, this.sessionId, 0, (byte) 0,
							(byte) 0, (byte) 2, chatObj.toString().getBytes().length, chatObj.toString().getBytes());
					this.clientChannel.SendRequest(roomMsg);
					if (saveHistory && this.storageService != null && !this.isNullOrEmpty(this.dbName)) {
						chatObj.put("chatTime", (new Date()).getTime());
						chatObj.put("roomId", roomId);
						chatObj.put("sender", Util.userName);
						this.storageService.insertJSONDocument(this.dbName, "ChatHistory", chatObj, null);
					}
				} catch (Exception var7) {
					for (ChatRequestListener listener : this.chatRequestListeners) {
						listener.onSendChatDone((byte) 4);
					}
				}

			} else {
				for (ChatRequestListener listener : this.chatRequestListeners) {
					listener.onSendChatDone((byte) 4);
				}

			}
		} else {
			for (ChatRequestListener listener : this.chatRequestListeners) {
				listener.onSendChatDone((byte) 4);
			}

		}
	}

	/**
	* Set a db name where chat history need to be saved.
	*
	* @param dbName
	*            Database name that will be set for the chat history.
	*/
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	/**
	* Enables client to save the chat sent using sendChat API.
	*
	* @param status
	*            If true, the client can save chat using sendChat API.
	*/
	public void enableChatHistory(boolean status) {
		this.isChatHistory = status;
		if (status) {
			this.storageService = new StorageService();
		}

	}

	/**
	* Retrieves chat history of a room of specified roomId from the server. Result
	* is provided in the onGetChatHistoryDone callback of the registered
	* ChatRequestListener objects.
	*
	* @param roomId
	*            Id of the room whose history is requested.
	* @param max
	*            Number of messages to be fetched from the server.
	* @param offset
	*            index from where the messages need to be fetched.
	*/
	public void getChatHistory(String roomId, int max, int offset) {
		if (this.isNotConnected()) {
			for (ChatRequestListener listener : this.chatRequestListeners) {
				listener.onGetChatHistoryDone((byte) 5, null);
			}

		} else if (this.storageService != null && !this.isNullOrEmpty(roomId)) {
			HashMap<String, String> otherMetaHeaders = new HashMap<>();
			Query q1 = QueryBuilder.build("roomId", roomId, QueryBuilder.Operator.EQUALS);
			otherMetaHeaders.put("orderByDescending", "chatTime");
			this.storageService.setOtherMetaHeaders(otherMetaHeaders);
			this.storageService.findDocumentsByQueryWithPaging(this.dbName, "ChatHistory", q1, max, offset,
					new App42CallBack() {
						public void onSuccess(Object response) {
							WarpClient.this.onApp42Response(1, response);
						}

						public void onException(Exception ex) {
							WarpClient.this.onApp42Exception(1);
						}
					});
		} else {
			for (ChatRequestListener listener : this.chatRequestListeners) {
				listener.onGetChatHistoryDone((byte) 4, null);
			}

		}
	}

	void onApp42Response(int app42RequestCode, Object response) {
		if (app42RequestCode == 1) {
			Storage storage = (Storage) response;
			ArrayList<ChatEvent> chats = ChatEvent.buildChatHistoryList(storage.getJsonDocList());
			for (ChatRequestListener listener : this.chatRequestListeners) {
				listener.onGetChatHistoryDone((byte) 0, chats);
			}
		}
	}

	void onApp42Exception(int app42RequestCode) {
		if (app42RequestCode == 1) {
			for (ChatRequestListener listener : this.chatRequestListeners) {
				listener.onGetChatHistoryDone((byte) 2, null);
			}
		}
	}

	private class ConnectionWatchTask extends TimerTask {
		WarpClient owner;

		ConnectionWatchTask(WarpClient client) {
			this.owner = client;
		}

		public void run() {
			if (this.owner.connectionState != 0) {
				byte result = 5;
				if (WarpClient.this.connectionState == 4) {
					result = 9;
				}

				this.owner.connectionState = 2;
				this.owner.fireConnectionEvent(result);
				this.owner.clientChannel.disconnect();
			}

		}
	}

	class ConnectionAutoRecoveryTask extends TimerTask {
		public void run() {
			WarpClient.this.RecoverConnection();
		}
	}
}
