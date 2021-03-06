package de.SimonIT.gdxAppWarp;

import de.SimonIT.gdxAppWarp.events.*;
import de.SimonIT.gdxAppWarp.listener.*;

import java.util.ArrayList;
import java.util.HashMap;

public class JavaWarpClient implements WarpClient {

	private static JavaWarpClient _instance;

	private com.shephertz.app42.gaming.multiplayer.client.WarpClient clientInstance;

	private JavaWarpClient() throws Exception {
		clientInstance = com.shephertz.app42.gaming.multiplayer.client.WarpClient.getInstance();
	}

	public static void enableTrace(boolean enable) {
		com.shephertz.app42.gaming.multiplayer.client.WarpClient.enableTrace(enable);
	}

	/**
	 * Sets the time allowed to the client to recover from an intermittent
	 * connection loss. This must be set before the initial connect API is called as
	 * that associates the value on the server for the given connection.
	 *
	 * @param maxRecoveryTime time - the time (in seconds) allowed to the client to recover from
	 *                        intermittent connection loss
	 */
	public static void setRecoveryAllowance(int maxRecoveryTime) {
		com.shephertz.app42.gaming.multiplayer.client.WarpClient.setRecoveryAllowance(maxRecoveryTime);
	}

	/**
	 * It returns the singleton instance of WarpClient.This should be initialized
	 * with a key pair before it is used.
	 *
	 * @return singleton instance of WarpClient.
	 * @throws Exception WarpClient not initialized
	 */
	public static JavaWarpClient getInstance() throws Exception {
		if (_instance == null) {
			_instance = new JavaWarpClient();
		}
		return _instance;
	}

	/**
	 * Initializes the singleton instance of WarpClient with the developer
	 * credentials. This has to be called only once during the lifetime of the
	 * application. It is required before you can call any other API.
	 *
	 * @param apiKey The Application key given when the application was created.
	 * @param pvtKey The Application key given when the application was created.
	 * @return WarpResponseResultCode
	 */
	public static byte initialize(String apiKey, String pvtKey) {
		return com.shephertz.app42.gaming.multiplayer.client.WarpClient.initialize(apiKey, pvtKey);
	}

	/**
	 * Initializes the singleton instance of WarpClient with the developer
	 * credentials. This has to be called only once during the lifetime of the
	 * application. It is required before you can call any other API.
	 *
	 * @param apiKey The Application key given when the application was created.
	 * @param pvtKey The Application key given when the application was created.
	 * @param server App Warp Server IP Or Name.
	 * @return WarpResponseResultCode
	 */
	public static byte initialize(String apiKey, String pvtKey, String server) {
		return com.shephertz.app42.gaming.multiplayer.client.WarpClient.initialize(apiKey, pvtKey, server);
	}

	public static boolean adminCreateZone(String apiKey, String secretKey, String host) {
		return com.shephertz.app42.gaming.multiplayer.client.WarpClient.adminCreateZone(apiKey, secretKey, host);
	}

	public static boolean adminDeleteZone(String apiKey, String secretKey, String host) {
		return com.shephertz.app42.gaming.multiplayer.client.WarpClient.adminDeleteZone(apiKey, secretKey, host);
	}

	public static byte adminCreateRoom(String name, String owner, int maxUsers, String apiKey, String secretKey,
									   String host, HashMap<String, Object> tableProperties) {
		return com.shephertz.app42.gaming.multiplayer.client.WarpClient.adminCreateRoom(name, owner, maxUsers, apiKey, secretKey, host, tableProperties);
	}

	public static boolean adminDeleteRoom(String roomId, String apiKey, String secretKey, String host) {
		return com.shephertz.app42.gaming.multiplayer.client.WarpClient.adminDeleteRoom(roomId, apiKey, secretKey, host);
	}

	/**
	 * It gives the Api Key of the current established connection,otherwise returns
	 * null.
	 *
	 * @return Api key
	 */
	public String getAPIKey() {
		return clientInstance.getAPIKey();
	}

	/**
	 * It gives the Private/Secret Key of the current established
	 * connection,otherwise returns null.
	 *
	 * @return Private/Secret key
	 */
	public String getPrivateKey() {
		return clientInstance.getPrivateKey();
	}

	public void setPrivateKey(String secretKey) {
		clientInstance.setPrivateKey(secretKey);
	}

	public void setApiKey(String apiKey) {
		clientInstance.setApiKey(apiKey);
	}

	public void setServer(String address) {
		clientInstance.setServer(address);
	}

	/**
	 * setGeo allows you to connect to our cloud servers in locations other than the
	 * default location. This offers developers the choice to connect to the closest
	 * server depending on the client’s device location.
	 *
	 * @param geo server location. For e.g. US, EU, JAPAN
	 */
	public void setGeo(String geo) {
		clientInstance.setGeo(geo);
	}

	/**
	 * Adds (registers) the given listener object to the list of objects on which
	 * callbacks will be invoked when a response from the server is received for
	 * Connect and Disconnect APIs. The object must implement the
	 * ConnectionRequestListener interface.
	 *
	 * @param listener method for listening to the request
	 */
	public void addConnectionRequestListener(ConnectionRequestListener listener) {
		clientInstance.addConnectionRequestListener(new com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener() {
			@Override
			public void onConnectDone(com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent connectEvent) {
				listener.onConnectDone(new ConnectEvent(connectEvent.getResult(), connectEvent.getReasonCode()));
			}

			@Override
			public void onDisconnectDone(com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent connectEvent) {
				listener.onDisconnectDone(new ConnectEvent(connectEvent.getResult(), connectEvent.getReasonCode()));
			}

			@Override
			public void onInitUDPDone(byte b) {
				listener.onInitUDPDone(b);
			}
		});
	}

	/**
	 * Removes the given listener object from the list of objects on which callbacks
	 * will be invoked when a response from the server is received for Connect and
	 * Disconnect APIs. The object must implement the ConnectionRequestListener
	 * interface.
	 *
	 * @param listener listener object
	 */
	public void removeConnectionRequestListener(ConnectionRequestListener listener) {
		clientInstance.removeConnectionRequestListener(new com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener() {
			@Override
			public void onConnectDone(com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent connectEvent) {
				listener.onConnectDone(new ConnectEvent(connectEvent.getResult(), connectEvent.getReasonCode()));
			}

			@Override
			public void onDisconnectDone(com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent connectEvent) {
				listener.onDisconnectDone(new ConnectEvent(connectEvent.getResult(), connectEvent.getReasonCode()));
			}

			@Override
			public void onInitUDPDone(byte b) {
				listener.onInitUDPDone(b);
			}
		});
	}

	/**
	 * Adds (registers) the given listener object to the list of objects on which
	 * callbacks will be invoked when a response from the server is received for
	 * zone level requests such as create/delete room or live user info requests.
	 * The object must implement the ZoneRequestListener interface.
	 *
	 * @param listener method for listening to the request
	 */
	public void addZoneRequestListener(ZoneRequestListener listener) {
		clientInstance.addZoneRequestListener(new com.shephertz.app42.gaming.multiplayer.client.listener.ZoneRequestListener() {
			@Override
			public void onDeleteRoomDone(com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent roomEvent) {
				listener.onDeleteRoomDone(new RoomEvent(Mapper.createRoomData(roomEvent.getData()), roomEvent.getResult()));
			}

			@Override
			public void onGetAllRoomsDone(com.shephertz.app42.gaming.multiplayer.client.events.AllRoomsEvent allRoomsEvent) {
				listener.onGetAllRoomsDone(new AllRoomsEvent(allRoomsEvent.getResult(), allRoomsEvent.getRoomIds(), allRoomsEvent.getRoomsCount()));
			}

			@Override
			public void onCreateRoomDone(com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent roomEvent) {
				listener.onCreateRoomDone(new RoomEvent(Mapper.createRoomData(roomEvent.getData()), roomEvent.getResult()));
			}

			@Override
			public void onGetOnlineUsersDone(com.shephertz.app42.gaming.multiplayer.client.events.AllUsersEvent allUsersEvent) {
				listener.onGetOnlineUsersDone(new AllUsersEvent(allUsersEvent.getResult(), allUsersEvent.getUserNames(), allUsersEvent.getUsersCount()));
			}

			@Override
			public void onGetLiveUserInfoDone(com.shephertz.app42.gaming.multiplayer.client.events.LiveUserInfoEvent liveUserInfoEvent) {
				listener.onGetLiveUserInfoDone(new LiveUserInfoEvent(liveUserInfoEvent.getResult(), liveUserInfoEvent.getLocationId(), liveUserInfoEvent.getName(), liveUserInfoEvent.getCustomData(), liveUserInfoEvent.isLocationLobby(), liveUserInfoEvent.isPaused(), liveUserInfoEvent.isActive()));
			}

			@Override
			public void onSetCustomUserDataDone(com.shephertz.app42.gaming.multiplayer.client.events.LiveUserInfoEvent liveUserInfoEvent) {
				listener.onSetCustomUserDataDone(new LiveUserInfoEvent(liveUserInfoEvent.getResult(), liveUserInfoEvent.getLocationId(), liveUserInfoEvent.getName(), liveUserInfoEvent.getCustomData(), liveUserInfoEvent.isLocationLobby(), liveUserInfoEvent.isPaused(), liveUserInfoEvent.isActive()));
			}

			@Override
			public void onGetMatchedRoomsDone(com.shephertz.app42.gaming.multiplayer.client.events.MatchedRoomsEvent matchedRoomsEvent) {
				RoomData[] roomData = new RoomData[matchedRoomsEvent.getRoomsData().length];
				for (int i = 0; i < matchedRoomsEvent.getRoomsData().length; i++) {
					roomData[i] = Mapper.createRoomData(matchedRoomsEvent.getRoomsData()[i]);
				}
				listener.onGetMatchedRoomsDone(new MatchedRoomsEvent(matchedRoomsEvent.getResult(), roomData));
			}

			@Override
			public void onGetAllRoomsCountDone(com.shephertz.app42.gaming.multiplayer.client.events.AllRoomsEvent allRoomsEvent) {
				listener.onGetAllRoomsCountDone(new AllRoomsEvent(allRoomsEvent.getResult(), allRoomsEvent.getRoomIds(), allRoomsEvent.getRoomsCount()));
			}

			@Override
			public void onGetOnlineUsersCountDone(com.shephertz.app42.gaming.multiplayer.client.events.AllUsersEvent allUsersEvent) {
				listener.onGetOnlineUsersCountDone(new AllUsersEvent(allUsersEvent.getResult(), allUsersEvent.getUserNames(), allUsersEvent.getUsersCount()));
			}

			@Override
			public void onGetUserStatusDone(com.shephertz.app42.gaming.multiplayer.client.events.LiveUserInfoEvent liveUserInfoEvent) {
				listener.onGetUserStatusDone(new LiveUserInfoEvent(liveUserInfoEvent.getResult(), liveUserInfoEvent.getLocationId(), liveUserInfoEvent.getName(), liveUserInfoEvent.getCustomData(), liveUserInfoEvent.isLocationLobby(), liveUserInfoEvent.isPaused(), liveUserInfoEvent.isActive()));
			}
		});
	}

	/**
	 * Removes the given listener object from the list of objects on which callbacks
	 * will be invoked when a response from the server is received for zone level
	 * requests such as create/delete room or live user info requests. The object
	 * must implement the ZoneRequestListener interface.
	 *
	 * @param listener listener object
	 */
	public void removeZoneRequestListener(ZoneRequestListener listener) {
		clientInstance.removeZoneRequestListener(new com.shephertz.app42.gaming.multiplayer.client.listener.ZoneRequestListener() {
			@Override
			public void onDeleteRoomDone(com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent roomEvent) {
				listener.onDeleteRoomDone(new RoomEvent(Mapper.createRoomData(roomEvent.getData()), roomEvent.getResult()));
			}

			@Override
			public void onGetAllRoomsDone(com.shephertz.app42.gaming.multiplayer.client.events.AllRoomsEvent allRoomsEvent) {
				listener.onGetAllRoomsDone(new AllRoomsEvent(allRoomsEvent.getResult(), allRoomsEvent.getRoomIds(), allRoomsEvent.getRoomsCount()));
			}

			@Override
			public void onCreateRoomDone(com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent roomEvent) {
				listener.onCreateRoomDone(new RoomEvent(Mapper.createRoomData(roomEvent.getData()), roomEvent.getResult()));
			}

			@Override
			public void onGetOnlineUsersDone(com.shephertz.app42.gaming.multiplayer.client.events.AllUsersEvent allUsersEvent) {
				listener.onGetOnlineUsersDone(new AllUsersEvent(allUsersEvent.getResult(), allUsersEvent.getUserNames(), allUsersEvent.getUsersCount()));
			}

			@Override
			public void onGetLiveUserInfoDone(com.shephertz.app42.gaming.multiplayer.client.events.LiveUserInfoEvent liveUserInfoEvent) {
				listener.onGetLiveUserInfoDone(new LiveUserInfoEvent(liveUserInfoEvent.getResult(), liveUserInfoEvent.getLocationId(), liveUserInfoEvent.getName(), liveUserInfoEvent.getCustomData(), liveUserInfoEvent.isLocationLobby(), liveUserInfoEvent.isPaused(), liveUserInfoEvent.isActive()));
			}

			@Override
			public void onSetCustomUserDataDone(com.shephertz.app42.gaming.multiplayer.client.events.LiveUserInfoEvent liveUserInfoEvent) {
				listener.onSetCustomUserDataDone(new LiveUserInfoEvent(liveUserInfoEvent.getResult(), liveUserInfoEvent.getLocationId(), liveUserInfoEvent.getName(), liveUserInfoEvent.getCustomData(), liveUserInfoEvent.isLocationLobby(), liveUserInfoEvent.isPaused(), liveUserInfoEvent.isActive()));
			}

			@Override
			public void onGetMatchedRoomsDone(com.shephertz.app42.gaming.multiplayer.client.events.MatchedRoomsEvent matchedRoomsEvent) {
				RoomData[] roomData = new RoomData[matchedRoomsEvent.getRoomsData().length];
				for (int i = 0; i < matchedRoomsEvent.getRoomsData().length; i++) {
					roomData[i] = Mapper.createRoomData(matchedRoomsEvent.getRoomsData()[i]);
				}
				listener.onGetMatchedRoomsDone(new MatchedRoomsEvent(matchedRoomsEvent.getResult(), roomData));
			}

			@Override
			public void onGetAllRoomsCountDone(com.shephertz.app42.gaming.multiplayer.client.events.AllRoomsEvent allRoomsEvent) {
				listener.onGetAllRoomsCountDone(new AllRoomsEvent(allRoomsEvent.getResult(), allRoomsEvent.getRoomIds(), allRoomsEvent.getRoomsCount()));
			}

			@Override
			public void onGetOnlineUsersCountDone(com.shephertz.app42.gaming.multiplayer.client.events.AllUsersEvent allUsersEvent) {
				listener.onGetOnlineUsersCountDone(new AllUsersEvent(allUsersEvent.getResult(), allUsersEvent.getUserNames(), allUsersEvent.getUsersCount()));
			}

			@Override
			public void onGetUserStatusDone(com.shephertz.app42.gaming.multiplayer.client.events.LiveUserInfoEvent liveUserInfoEvent) {
				listener.onGetUserStatusDone(new LiveUserInfoEvent(liveUserInfoEvent.getResult(), liveUserInfoEvent.getLocationId(), liveUserInfoEvent.getName(), liveUserInfoEvent.getCustomData(), liveUserInfoEvent.isLocationLobby(), liveUserInfoEvent.isPaused(), liveUserInfoEvent.isActive()));
			}
		});
	}

	/**
	 * Adds (registers) the given listener object to the list of objects on which
	 * callbacks will be invoked when a response from the server is received for
	 * lobby level request. The object must implement the LobbyRequestListener
	 * interface.
	 *
	 * @param listener method for listening to the request
	 */
	public void addLobbyRequestListener(LobbyRequestListener listener) {
		clientInstance.addLobbyRequestListener(new com.shephertz.app42.gaming.multiplayer.client.listener.LobbyRequestListener() {
			@Override
			public void onJoinLobbyDone(com.shephertz.app42.gaming.multiplayer.client.events.LobbyEvent lobbyEvent) {
				listener.onJoinLobbyDone(new LobbyEvent(new LobbyData(lobbyEvent.getInfo().getId(), lobbyEvent.getInfo().getRoomOwner(), lobbyEvent.getInfo().getName(), lobbyEvent.getInfo().getMaxUsers(), lobbyEvent.getInfo().isPrimary()), lobbyEvent.getResult()));
			}

			@Override
			public void onLeaveLobbyDone(com.shephertz.app42.gaming.multiplayer.client.events.LobbyEvent lobbyEvent) {
				listener.onLeaveLobbyDone(new LobbyEvent(new LobbyData(lobbyEvent.getInfo().getId(), lobbyEvent.getInfo().getRoomOwner(), lobbyEvent.getInfo().getName(), lobbyEvent.getInfo().getMaxUsers(), lobbyEvent.getInfo().isPrimary()), lobbyEvent.getResult()));
			}

			@Override
			public void onSubscribeLobbyDone(com.shephertz.app42.gaming.multiplayer.client.events.LobbyEvent lobbyEvent) {
				listener.onSubscribeLobbyDone(new LobbyEvent(new LobbyData(lobbyEvent.getInfo().getId(), lobbyEvent.getInfo().getRoomOwner(), lobbyEvent.getInfo().getName(), lobbyEvent.getInfo().getMaxUsers(), lobbyEvent.getInfo().isPrimary()), lobbyEvent.getResult()));
			}

			@Override
			public void onUnSubscribeLobbyDone(com.shephertz.app42.gaming.multiplayer.client.events.LobbyEvent lobbyEvent) {
				listener.onUnSubscribeLobbyDone(new LobbyEvent(new LobbyData(lobbyEvent.getInfo().getId(), lobbyEvent.getInfo().getRoomOwner(), lobbyEvent.getInfo().getName(), lobbyEvent.getInfo().getMaxUsers(), lobbyEvent.getInfo().isPrimary()), lobbyEvent.getResult()));
			}

			@Override
			public void onGetLiveLobbyInfoDone(com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent liveRoomInfoEvent) {
				listener.onGetLiveLobbyInfoDone(new LiveRoomInfoEvent(Mapper.createRoomData(liveRoomInfoEvent.getData()), liveRoomInfoEvent.getResult(), liveRoomInfoEvent.getJoinedUsers(), liveRoomInfoEvent.getCustomData(), liveRoomInfoEvent.getProperties(), liveRoomInfoEvent.getLockProperties()));
			}
		});
	}

	/**
	 * Removes the given listener object from the list of objects on which callbacks
	 * will be invoked when a response from the server is received for lobby level
	 * requests. The object must implement the Lobby Request Listener interface.
	 *
	 * @param listener listener object
	 */
	public void removeLobbyRequestListener(LobbyRequestListener listener) {
		clientInstance.removeLobbyRequestListener(new com.shephertz.app42.gaming.multiplayer.client.listener.LobbyRequestListener() {
			@Override
			public void onJoinLobbyDone(com.shephertz.app42.gaming.multiplayer.client.events.LobbyEvent lobbyEvent) {
				listener.onJoinLobbyDone(new LobbyEvent(new LobbyData(lobbyEvent.getInfo().getId(), lobbyEvent.getInfo().getRoomOwner(), lobbyEvent.getInfo().getName(), lobbyEvent.getInfo().getMaxUsers(), lobbyEvent.getInfo().isPrimary()), lobbyEvent.getResult()));
			}

			@Override
			public void onLeaveLobbyDone(com.shephertz.app42.gaming.multiplayer.client.events.LobbyEvent lobbyEvent) {
				listener.onLeaveLobbyDone(new LobbyEvent(new LobbyData(lobbyEvent.getInfo().getId(), lobbyEvent.getInfo().getRoomOwner(), lobbyEvent.getInfo().getName(), lobbyEvent.getInfo().getMaxUsers(), lobbyEvent.getInfo().isPrimary()), lobbyEvent.getResult()));
			}

			@Override
			public void onSubscribeLobbyDone(com.shephertz.app42.gaming.multiplayer.client.events.LobbyEvent lobbyEvent) {
				listener.onSubscribeLobbyDone(new LobbyEvent(new LobbyData(lobbyEvent.getInfo().getId(), lobbyEvent.getInfo().getRoomOwner(), lobbyEvent.getInfo().getName(), lobbyEvent.getInfo().getMaxUsers(), lobbyEvent.getInfo().isPrimary()), lobbyEvent.getResult()));
			}

			@Override
			public void onUnSubscribeLobbyDone(com.shephertz.app42.gaming.multiplayer.client.events.LobbyEvent lobbyEvent) {
				listener.onUnSubscribeLobbyDone(new LobbyEvent(new LobbyData(lobbyEvent.getInfo().getId(), lobbyEvent.getInfo().getRoomOwner(), lobbyEvent.getInfo().getName(), lobbyEvent.getInfo().getMaxUsers(), lobbyEvent.getInfo().isPrimary()), lobbyEvent.getResult()));
			}

			@Override
			public void onGetLiveLobbyInfoDone(com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent liveRoomInfoEvent) {
				listener.onGetLiveLobbyInfoDone(new LiveRoomInfoEvent(Mapper.createRoomData(liveRoomInfoEvent.getData()), liveRoomInfoEvent.getResult(), liveRoomInfoEvent.getJoinedUsers(), liveRoomInfoEvent.getCustomData(), liveRoomInfoEvent.getProperties(), liveRoomInfoEvent.getLockProperties()));
			}
		});
	}

	/**
	 * Adds (registers) the given listener object to the list of objects on which
	 * callbacks will be invoked when a response from the server is received for
	 * requests pertaining to a room. The object must implement the
	 * RoomRequestListener interface.
	 *
	 * @param listener method for listening to the request
	 */
	public void addRoomRequestListener(RoomRequestListener listener) {
		clientInstance.addRoomRequestListener(new com.shephertz.app42.gaming.multiplayer.client.listener.RoomRequestListener() {
			@Override
			public void onSubscribeRoomDone(com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent roomEvent) {
				listener.onSubscribeRoomDone(new RoomEvent(Mapper.createRoomData(roomEvent.getData()), roomEvent.getResult()));
			}

			@Override
			public void onUnSubscribeRoomDone(com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent roomEvent) {
				listener.onUnSubscribeRoomDone(new RoomEvent(Mapper.createRoomData(roomEvent.getData()), roomEvent.getResult()));
			}

			@Override
			public void onJoinRoomDone(com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent roomEvent) {
				listener.onJoinRoomDone(new RoomEvent(Mapper.createRoomData(roomEvent.getData()), roomEvent.getResult()));
			}

			@Override
			public void onLeaveRoomDone(com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent roomEvent) {
				listener.onLeaveRoomDone(new RoomEvent(Mapper.createRoomData(roomEvent.getData()), roomEvent.getResult()));
			}

			@Override
			public void onGetLiveRoomInfoDone(com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent liveRoomInfoEvent) {
				listener.onGetLiveRoomInfoDone(new LiveRoomInfoEvent(Mapper.createRoomData(liveRoomInfoEvent.getData()), liveRoomInfoEvent.getResult(), liveRoomInfoEvent.getJoinedUsers(), liveRoomInfoEvent.getCustomData(), liveRoomInfoEvent.getProperties(), liveRoomInfoEvent.getLockProperties()));
			}

			@Override
			public void onSetCustomRoomDataDone(com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent liveRoomInfoEvent) {
				listener.onSetCustomRoomDataDone(new LiveRoomInfoEvent(Mapper.createRoomData(liveRoomInfoEvent.getData()), liveRoomInfoEvent.getResult(), liveRoomInfoEvent.getJoinedUsers(), liveRoomInfoEvent.getCustomData(), liveRoomInfoEvent.getProperties(), liveRoomInfoEvent.getLockProperties()));
			}

			@Override
			public void onUpdatePropertyDone(com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent liveRoomInfoEvent) {
				listener.onUpdatePropertyDone(new LiveRoomInfoEvent(Mapper.createRoomData(liveRoomInfoEvent.getData()), liveRoomInfoEvent.getResult(), liveRoomInfoEvent.getJoinedUsers(), liveRoomInfoEvent.getCustomData(), liveRoomInfoEvent.getProperties(), liveRoomInfoEvent.getLockProperties()));
			}

			@Override
			public void onLockPropertiesDone(byte b) {
				listener.onLockPropertiesDone(b);
			}

			@Override
			public void onUnlockPropertiesDone(byte b) {
				listener.onUnlockPropertiesDone(b);
			}

			@Override
			public void onJoinAndSubscribeRoomDone(com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent roomEvent) {
				listener.onJoinAndSubscribeRoomDone(new RoomEvent(Mapper.createRoomData(roomEvent.getData()), roomEvent.getResult()));
			}

			@Override
			public void onLeaveAndUnsubscribeRoomDone(com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent roomEvent) {
				listener.onLeaveAndUnsubscribeRoomDone(new RoomEvent(Mapper.createRoomData(roomEvent.getData()), roomEvent.getResult()));
			}
		});
	}

	/**
	 * Adds (registers) the given listener object to the list of objects on which
	 * callbacks will be invoked when a response from the server is received for a
	 * SendMove request. The object must implement the TurnBasedRoomListener
	 * interface.
	 *
	 * @param listener listener object
	 */
	public void addTurnBasedRoomListener(TurnBasedRoomListener listener) {
		clientInstance.addTurnBasedRoomListener(new com.shephertz.app42.gaming.multiplayer.client.listener.TurnBasedRoomListener() {
			@Override
			public void onSendMoveDone(byte b) {
				listener.onSendMoveDone(b);
			}

			@Override
			public void onStartGameDone(byte b) {
				listener.onStartGameDone(b);
			}

			@Override
			public void onStopGameDone(byte b) {
				listener.onStopGameDone(b);
			}

			@Override
			public void onGetMoveHistoryDone(byte b, com.shephertz.app42.gaming.multiplayer.client.events.MoveEvent[] moveEvents) {
				MoveEvent[] roomData = new MoveEvent[moveEvents.length];
				for (int i = 0; i < moveEvents.length; i++) {
					roomData[i] = new MoveEvent(moveEvents[i].getSender(), moveEvents[i].getMoveData(), moveEvents[i].getNextTurn(), moveEvents[i].getRoomId());
				}
				listener.onGetMoveHistoryDone(b, roomData);
			}

			@Override
			public void onSetNextTurnDone(byte b) {
				listener.onSetNextTurnDone(b);
			}

			@Override
			public void onGetGameStatusDone(byte b, boolean b1) {
				listener.onGetGameStatusDone(b, b1);
			}
		});
	}

	/**
	 * Removes the given listener object from the list of objects on which callbacks
	 * will be invoked when a response from the server is received for a turn based
	 * game move request. The object must implement the Turn Based Room Listener
	 * interface.
	 *
	 * @param listener listener object
	 */
	public void removeTurnBasedRoomListener(TurnBasedRoomListener listener) {
		clientInstance.removeTurnBasedRoomListener(new com.shephertz.app42.gaming.multiplayer.client.listener.TurnBasedRoomListener() {
			@Override
			public void onSendMoveDone(byte b) {
				listener.onSendMoveDone(b);
			}

			@Override
			public void onStartGameDone(byte b) {
				listener.onStartGameDone(b);
			}

			@Override
			public void onStopGameDone(byte b) {
				listener.onStopGameDone(b);
			}

			@Override
			public void onGetMoveHistoryDone(byte b, com.shephertz.app42.gaming.multiplayer.client.events.MoveEvent[] moveEvents) {
				MoveEvent[] roomData = new MoveEvent[moveEvents.length];
				for (int i = 0; i < moveEvents.length; i++) {
					roomData[i] = new MoveEvent(moveEvents[i].getSender(), moveEvents[i].getMoveData(), moveEvents[i].getNextTurn(), moveEvents[i].getRoomId());
				}
				listener.onGetMoveHistoryDone(b, roomData);
			}

			@Override
			public void onSetNextTurnDone(byte b) {
				listener.onSetNextTurnDone(b);
			}

			@Override
			public void onGetGameStatusDone(byte b, boolean b1) {
				listener.onGetGameStatusDone(b, b1);
			}
		});
	}

	/**
	 * Removes the given listener object from the list of objects on which callbacks
	 * will be invoked when a response from the server is received for requests
	 * pertaining to a room. The object must implement the RoomRequestListener
	 * interface.
	 *
	 * @param listener listener object
	 */
	public void removeRoomRequestListener(RoomRequestListener listener) {
		clientInstance.removeRoomRequestListener(new com.shephertz.app42.gaming.multiplayer.client.listener.RoomRequestListener() {
			@Override
			public void onSubscribeRoomDone(com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent roomEvent) {
				listener.onSubscribeRoomDone(new RoomEvent(Mapper.createRoomData(roomEvent.getData()), roomEvent.getResult()));
			}

			@Override
			public void onUnSubscribeRoomDone(com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent roomEvent) {
				listener.onUnSubscribeRoomDone(new RoomEvent(Mapper.createRoomData(roomEvent.getData()), roomEvent.getResult()));
			}

			@Override
			public void onJoinRoomDone(com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent roomEvent) {
				listener.onJoinRoomDone(new RoomEvent(Mapper.createRoomData(roomEvent.getData()), roomEvent.getResult()));
			}

			@Override
			public void onLeaveRoomDone(com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent roomEvent) {
				listener.onLeaveRoomDone(new RoomEvent(Mapper.createRoomData(roomEvent.getData()), roomEvent.getResult()));
			}

			@Override
			public void onGetLiveRoomInfoDone(com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent liveRoomInfoEvent) {
				listener.onGetLiveRoomInfoDone(new LiveRoomInfoEvent(Mapper.createRoomData(liveRoomInfoEvent.getData()), liveRoomInfoEvent.getResult(), liveRoomInfoEvent.getJoinedUsers(), liveRoomInfoEvent.getCustomData(), liveRoomInfoEvent.getProperties(), liveRoomInfoEvent.getLockProperties()));
			}

			@Override
			public void onSetCustomRoomDataDone(com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent liveRoomInfoEvent) {
				listener.onSetCustomRoomDataDone(new LiveRoomInfoEvent(Mapper.createRoomData(liveRoomInfoEvent.getData()), liveRoomInfoEvent.getResult(), liveRoomInfoEvent.getJoinedUsers(), liveRoomInfoEvent.getCustomData(), liveRoomInfoEvent.getProperties(), liveRoomInfoEvent.getLockProperties()));
			}

			@Override
			public void onUpdatePropertyDone(com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent liveRoomInfoEvent) {
				listener.onUpdatePropertyDone(new LiveRoomInfoEvent(Mapper.createRoomData(liveRoomInfoEvent.getData()), liveRoomInfoEvent.getResult(), liveRoomInfoEvent.getJoinedUsers(), liveRoomInfoEvent.getCustomData(), liveRoomInfoEvent.getProperties(), liveRoomInfoEvent.getLockProperties()));
			}

			@Override
			public void onLockPropertiesDone(byte b) {
				listener.onLockPropertiesDone(b);
			}

			@Override
			public void onUnlockPropertiesDone(byte b) {
				listener.onUnlockPropertiesDone(b);
			}

			@Override
			public void onJoinAndSubscribeRoomDone(com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent roomEvent) {
				listener.onJoinAndSubscribeRoomDone(new RoomEvent(Mapper.createRoomData(roomEvent.getData()), roomEvent.getResult()));
			}

			@Override
			public void onLeaveAndUnsubscribeRoomDone(com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent roomEvent) {
				listener.onLeaveAndUnsubscribeRoomDone(new RoomEvent(Mapper.createRoomData(roomEvent.getData()), roomEvent.getResult()));
			}
		});
	}

	/**
	 * Adds (registers) the given listener object to the list of objects on which
	 * callbacks will be invoked when a response from the server is received for a
	 * SendChat or sendPrivateChat request. The object must implement the
	 * ChatRequestListener interface.
	 *
	 * @param listener listener object
	 */
	public void addChatRequestListener(ChatRequestListener listener) {
		clientInstance.addChatRequestListener(new com.shephertz.app42.gaming.multiplayer.client.listener.ChatRequestListener() {
			@Override
			public void onSendChatDone(byte b) {
				listener.onSendChatDone(b);
			}

			@Override
			public void onSendPrivateChatDone(byte b) {
				listener.onSendPrivateChatDone(b);
			}

			@Override
			public void onGetChatHistoryDone(byte b, ArrayList<com.shephertz.app42.gaming.multiplayer.client.events.ChatEvent> arrayList) {
				ArrayList<ChatEvent> chatEvents = new ArrayList<>(arrayList.size());
				for (com.shephertz.app42.gaming.multiplayer.client.events.ChatEvent chatEvent : arrayList) {
					chatEvents.add(new ChatEvent(chatEvent.getMessage(), chatEvent.getSender(), chatEvent.getLocationId(), chatEvent.isLocationLobby()));
				}
				listener.onGetChatHistoryDone(b, chatEvents);
			}
		});
	}

	/**
	 * Removes the given listener object from the list of objects on which callbacks
	 * will be invoked when a response from the server is received for a SendChat or
	 * sendPrivateChat request. The object must implement the ChatRequestListener
	 * interface.
	 *
	 * @param listener listener object
	 */
	public void removeChatRequestListener(ChatRequestListener listener) {
		clientInstance.removeChatRequestListener(new com.shephertz.app42.gaming.multiplayer.client.listener.ChatRequestListener() {
			@Override
			public void onSendChatDone(byte b) {
				listener.onSendChatDone(b);
			}

			@Override
			public void onSendPrivateChatDone(byte b) {
				listener.onSendPrivateChatDone(b);
			}

			@Override
			public void onGetChatHistoryDone(byte b, ArrayList<com.shephertz.app42.gaming.multiplayer.client.events.ChatEvent> arrayList) {
				ArrayList<ChatEvent> chatEvents = new ArrayList<>(arrayList.size());
				for (com.shephertz.app42.gaming.multiplayer.client.events.ChatEvent chatEvent : arrayList) {
					chatEvents.add(new ChatEvent(chatEvent.getMessage(), chatEvent.getSender(), chatEvent.getLocationId(), chatEvent.isLocationLobby()));
				}
				listener.onGetChatHistoryDone(b, chatEvents);
			}
		});
	}

	/**
	 * Adds (registers) the given listener object to the list of objects on which
	 * callbacks will be invoked when a response from the server is received for a
	 * SendUpdatePeers request. The object must implement the UpdateRequestListener
	 * interface.
	 *
	 * @param listener method for listening to the request
	 */
	public void addUpdateRequestListener(UpdateRequestListener listener) {
		clientInstance.addUpdateRequestListener(new com.shephertz.app42.gaming.multiplayer.client.listener.UpdateRequestListener() {
			@Override
			public void onSendUpdateDone(byte b) {
				listener.onSendUpdateDone(b);
			}

			@Override
			public void onSendPrivateUpdateDone(byte b) {
				listener.onSendPrivateUpdateDone(b);
			}
		});
	}

	/**
	 * Removes the given listener object from the list of objects on which callbacks
	 * will be invoked when a response from the server is received for a
	 * SendUpdatePeers or SendPrivateUpdate request. The object must implement the
	 * UpdateRequestListener interface.
	 *
	 * @param listener listener object
	 */
	public void removeUpdateRequestListener(UpdateRequestListener listener) {
		clientInstance.removeUpdateRequestListener(new com.shephertz.app42.gaming.multiplayer.client.listener.UpdateRequestListener() {
			@Override
			public void onSendUpdateDone(byte b) {
				listener.onSendUpdateDone(b);
			}

			@Override
			public void onSendPrivateUpdateDone(byte b) {
				listener.onSendPrivateUpdateDone(b);
			}
		});
	}

	/**
	 * Adds (registers) the given listener object to the list of objects on which
	 * callbacks will be invoked when a notification is received from the server
	 * from any subscribed location (room or lobby). The object must implement the
	 * NotifyListener interface.
	 *
	 * @param listener method for listening to the request
	 */
	public void addNotificationListener(NotifyListener listener) {
		clientInstance.addNotificationListener(new com.shephertz.app42.gaming.multiplayer.client.listener.NotifyListener() {
			@Override
			public void onRoomCreated(com.shephertz.app42.gaming.multiplayer.client.events.RoomData roomData) {
				listener.onRoomCreated(Mapper.createRoomData(roomData));
			}

			@Override
			public void onRoomDestroyed(com.shephertz.app42.gaming.multiplayer.client.events.RoomData roomData) {
				listener.onRoomDestroyed(Mapper.createRoomData(roomData));
			}

			@Override
			public void onUserLeftRoom(com.shephertz.app42.gaming.multiplayer.client.events.RoomData roomData, String s) {
				listener.onUserLeftRoom(Mapper.createRoomData(roomData), s);
			}

			@Override
			public void onUserJoinedRoom(com.shephertz.app42.gaming.multiplayer.client.events.RoomData roomData, String s) {
				listener.onUserJoinedRoom(Mapper.createRoomData(roomData), s);
			}

			@Override
			public void onUserLeftLobby(com.shephertz.app42.gaming.multiplayer.client.events.LobbyData lobbyData, String s) {
				listener.onUserLeftLobby(new LobbyData(lobbyData.getId(), lobbyData.getRoomOwner(), lobbyData.getName(), lobbyData.getMaxUsers(), lobbyData.isPrimary()), s);
			}

			@Override
			public void onUserJoinedLobby(com.shephertz.app42.gaming.multiplayer.client.events.LobbyData lobbyData, String s) {
				listener.onUserJoinedLobby(new LobbyData(lobbyData.getId(), lobbyData.getRoomOwner(), lobbyData.getName(), lobbyData.getMaxUsers(), lobbyData.isPrimary()), s);
			}

			@Override
			public void onChatReceived(com.shephertz.app42.gaming.multiplayer.client.events.ChatEvent chatEvent) {
				listener.onChatReceived(new ChatEvent(chatEvent.getMessage(), chatEvent.getSender(), chatEvent.getLocationId(), chatEvent.isLocationLobby()));
			}

			@Override
			public void onPrivateChatReceived(String s, String s1) {
				listener.onPrivateChatReceived(s, s1);
			}

			@Override
			public void onPrivateUpdateReceived(String s, byte[] bytes, boolean b) {
				listener.onPrivateUpdateReceived(s, bytes, b);
			}

			@Override
			public void onUpdatePeersReceived(com.shephertz.app42.gaming.multiplayer.client.events.UpdateEvent updateEvent) {
				listener.onUpdatePeersReceived(new UpdateEvent(updateEvent.getUpdate(), updateEvent.isUDP()));
			}

			@Override
			public void onUserChangeRoomProperty(com.shephertz.app42.gaming.multiplayer.client.events.RoomData roomData, String s, HashMap<String, Object> hashMap, HashMap<String, String> hashMap1) {
				listener.onUserChangeRoomProperty(Mapper.createRoomData(roomData), s, hashMap, hashMap1);
			}

			@Override
			public void onMoveCompleted(com.shephertz.app42.gaming.multiplayer.client.events.MoveEvent moveEvent) {
				listener.onMoveCompleted(new MoveEvent(moveEvent.getSender(), moveEvent.getMoveData(), moveEvent.getNextTurn(), moveEvent.getRoomId()));
			}

			@Override
			public void onGameStarted(String s, String s1, String s2) {
				listener.onGameStarted(s, s1, s2);
			}

			@Override
			public void onGameStopped(String s, String s1) {
				listener.onGameStopped(s, s1);
			}

			@Override
			public void onUserPaused(String s, boolean b, String s1) {
				listener.onUserPaused(s, b, s1);
			}

			@Override
			public void onUserResumed(String s, boolean b, String s1) {
				listener.onUserResumed(s, b, s1);
			}

			@Override
			public void onNextTurnRequest(String s) {
				listener.onNextTurnRequest(s);
			}
		});
	}

	/**
	 * Removes the given listener object from the list of objects on which callbacks
	 * will be invoked when a notification is received from the server from any
	 * subscribed location (room or lobby). The object must implement the
	 * NotifyListener interface.
	 *
	 * @param listener listener object
	 */
	public void removeNotificationListener(NotifyListener listener) {
		clientInstance.removeNotificationListener(new com.shephertz.app42.gaming.multiplayer.client.listener.NotifyListener() {
			@Override
			public void onRoomCreated(com.shephertz.app42.gaming.multiplayer.client.events.RoomData roomData) {
				listener.onRoomCreated(Mapper.createRoomData(roomData));
			}

			@Override
			public void onRoomDestroyed(com.shephertz.app42.gaming.multiplayer.client.events.RoomData roomData) {
				listener.onRoomDestroyed(Mapper.createRoomData(roomData));
			}

			@Override
			public void onUserLeftRoom(com.shephertz.app42.gaming.multiplayer.client.events.RoomData roomData, String s) {
				listener.onUserLeftRoom(Mapper.createRoomData(roomData), s);
			}

			@Override
			public void onUserJoinedRoom(com.shephertz.app42.gaming.multiplayer.client.events.RoomData roomData, String s) {
				listener.onUserJoinedRoom(Mapper.createRoomData(roomData), s);
			}

			@Override
			public void onUserLeftLobby(com.shephertz.app42.gaming.multiplayer.client.events.LobbyData lobbyData, String s) {
				listener.onUserLeftLobby(new LobbyData(lobbyData.getId(), lobbyData.getRoomOwner(), lobbyData.getName(), lobbyData.getMaxUsers(), lobbyData.isPrimary()), s);
			}

			@Override
			public void onUserJoinedLobby(com.shephertz.app42.gaming.multiplayer.client.events.LobbyData lobbyData, String s) {
				listener.onUserJoinedLobby(new LobbyData(lobbyData.getId(), lobbyData.getRoomOwner(), lobbyData.getName(), lobbyData.getMaxUsers(), lobbyData.isPrimary()), s);
			}

			@Override
			public void onChatReceived(com.shephertz.app42.gaming.multiplayer.client.events.ChatEvent chatEvent) {
				listener.onChatReceived(new ChatEvent(chatEvent.getMessage(), chatEvent.getSender(), chatEvent.getLocationId(), chatEvent.isLocationLobby()));
			}

			@Override
			public void onPrivateChatReceived(String s, String s1) {
				listener.onPrivateChatReceived(s, s1);
			}

			@Override
			public void onPrivateUpdateReceived(String s, byte[] bytes, boolean b) {
				listener.onPrivateUpdateReceived(s, bytes, b);
			}

			@Override
			public void onUpdatePeersReceived(com.shephertz.app42.gaming.multiplayer.client.events.UpdateEvent updateEvent) {
				listener.onUpdatePeersReceived(new UpdateEvent(updateEvent.getUpdate(), updateEvent.isUDP()));
			}

			@Override
			public void onUserChangeRoomProperty(com.shephertz.app42.gaming.multiplayer.client.events.RoomData roomData, String s, HashMap<String, Object> hashMap, HashMap<String, String> hashMap1) {
				listener.onUserChangeRoomProperty(Mapper.createRoomData(roomData), s, hashMap, hashMap1);
			}

			@Override
			public void onMoveCompleted(com.shephertz.app42.gaming.multiplayer.client.events.MoveEvent moveEvent) {
				listener.onMoveCompleted(new MoveEvent(moveEvent.getSender(), moveEvent.getMoveData(), moveEvent.getNextTurn(), moveEvent.getRoomId()));
			}

			@Override
			public void onGameStarted(String s, String s1, String s2) {
				listener.onGameStarted(s, s1, s2);
			}

			@Override
			public void onGameStopped(String s, String s1) {
				listener.onGameStopped(s, s1);
			}

			@Override
			public void onUserPaused(String s, boolean b, String s1) {
				listener.onUserPaused(s, b, s1);
			}

			@Override
			public void onUserResumed(String s, boolean b, String s1) {
				listener.onUserResumed(s, b, s1);
			}

			@Override
			public void onNextTurnRequest(String s) {
				listener.onNextTurnRequest(s);
			}
		});
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
	 * @param userName Username of the player
	 */
	public void connectWithUserName(String userName) {
		clientInstance.connectWithUserName(userName);
	}

	/**
	 * Disconnects the connection with the AppWarp server. The result for this
	 * request will be provided in the onDisConnectDone callback of the
	 * ConnectionRequestListener.
	 */
	public void disconnect() {
		clientInstance.disconnect();
	}

	/**
	 * Returns the current connection state of the WarpClient instance.
	 *
	 * @return int
	 */
	public int getConnectionState() {
		return clientInstance.getConnectionState();
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
		clientInstance.RecoverConnection();
	}

	/**
	 * It gives sessionId of the current established connection,otherwise returns
	 * zero.
	 *
	 * @return sessionId
	 */
	public int getSessionID() {
		return clientInstance.getSessionID();
	}

	/**
	 * Attempts to recover from an intermittent connection error.Since this API
	 * requires sessionId so it has to be saved by the game on the last successful
	 * connection.The other subscribed users of the room, will receive onUserResumed
	 * notification.The connection must be restored within the recovery allowance
	 * period, after which the server considers the session to be over
	 * (non-recoverable).
	 *
	 * @param session_id sessionId of the last successful session
	 * @param user_name  name of the player
	 */
	public void RecoverConnectionWithSessionID(int session_id, String user_name) {
		clientInstance.RecoverConnectionWithSessionID(session_id, user_name);
	}

	/**
	 * Updates the custom data associated with the given room on the server. The
	 * result is provided in the onSetCustomRoomDataDone callback of the registered
	 * RoomRequestListener objects. It is recommended you use the room’s properties
	 * where ever possible. Use this when you need to associate data with a room
	 * which can not be represented as key value pairs.
	 *
	 * @param roomid Id of the room
	 * @param data   custom data that will be set for the room
	 */
	public void setCustomRoomData(String roomid, String data) {
		clientInstance.setCustomRoomData(roomid, data);
	}

	/**
	 * Updates the custom data associated with the given user on the server (if the
	 * given user is online). Result is provided in the onSetCustomUserDataDone
	 * callback of the registered ZoneRequestListener objects. It can be useful in
	 * setting status messages or avatar url’s etc for online users.
	 *
	 * @param username user for whom custom data has to be update
	 * @param data     custom data that will be set for the user
	 */
	public void setCustomUserData(String username, String data) {
		clientInstance.setCustomUserData(username, data);
	}

	/**
	 * Retrieves usernames of all the users connected (online) to the server. Result
	 * is provided in the onGetOnlineUsers callback of the registered
	 * ZoneRequestListener objects.
	 */
	public void getOnlineUsers() {
		clientInstance.getOnlineUsers();
	}

	/**
	 * Retrieves total number of rooms on the server. Result is provided in the
	 * onGetAllRoomsCountDone callback of the registered ZoneRequestListener
	 * objects.
	 */
	public void getAllRoomsCount() {
		clientInstance.getAllRoomsCount();
	}

	/**
	 * Retrieves total number of users connected (online) to the server. Result is
	 * provided in the onGetOnlineUsersCountDone callback of the registered
	 * ZoneRequestListener objects.
	 */
	public void getOnlineUsersCount() {
		clientInstance.getOnlineUsers();
	}

	/**
	 * Retrieves the room ids of all the rooms on the server. Result is provided in
	 * the onGetAllRoomsDone callback of the registered ZoneRequestListener objects.
	 * To get a filtered list of rooms, use the GetRoomWithProperties API.
	 */
	public void getAllRooms() {
		clientInstance.getAllRooms();
	}

	/**
	 * Retrieves the live information of the user from the server. Result is
	 * provided in the onGetLiveUserInfo callback of the registered
	 * ZoneRequestListener objects. The information (if user is online) includes the
	 * current location of the user and any associated custom data. It is useful in
	 * building scenarios where you want to find if a users friends are online or
	 * not and then join their room if found online.
	 *
	 * @param username user whose information is requested
	 */
	public void getLiveUserInfo(String username) {
		clientInstance.getLiveUserInfo(username);
	}

	/**
	 * Retrieves status of user via username on the server. Result is provided in
	 * the onGetUserStatusDone callback of the registered ZoneRequestListener
	 * objects. This is useful to find either user is connected or not.
	 *
	 * @param username Username of the player
	 */
	public void getUserStatus(String username) {
		clientInstance.getUserStatus(username);
	}

	/**
	 * Retrieves the live information of the given room from the server. Result is
	 * provided in the onGetLiveRoomInfoDone callback of the registered
	 * RoomRequestListener objects. The information includes the names of the
	 * currently joined users, the rooms properties and any associated customData.
	 * This is useful in getting a snapshot of a rooms state.
	 *
	 * @param roomid Id of the room
	 */
	public void getLiveRoomInfo(String roomid) {
		clientInstance.getLiveRoomInfo(roomid);
	}

	/**
	 * Retrieves live information of the lobby from the server. Result is provided
	 * in the onGetLiveLobbyInfo callback of the registered LobbyRequestListener
	 * objects. The information returned includes the names of the users who are
	 * currently joined in the lobby.
	 */
	public void getLiveLobbyInfo() {
		clientInstance.getLiveLobbyInfo();
	}

	/**
	 * Sends a join lobby request to the server. Result of the request is provided
	 * in the onJoinLobbyDone callback of the registered LobbyRequestListener
	 * objects.
	 */
	public void joinLobby() {
		clientInstance.joinLobby();
	}

	/**
	 * Sends a leave lobby request to the server. Result of the request is provided
	 * in the onLeaveLobbyDone callback of the registered LobbyRequestListener
	 * objects.
	 */
	public void leaveLobby() {
		clientInstance.leaveLobby();
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
		clientInstance.subscribeLobby();
	}

	/**
	 * Sends a unsubscribe lobby request to the server. Result of the request is
	 * provided in the onUnsubscribeLobbyDone callback of the LobbyRequestListener.
	 */
	public void unsubscribeLobby() {
		clientInstance.unsubscribeLobby();
	}

	/**
	 * Sends a create room request to the server with the given meta data. Result of
	 * the request is provided in the onCreateRoomDone callback of the registered
	 * ZoneRequestListener objects. If successful, this will create a dynamic room
	 * at the server. These rooms lifetime is limited till the time users are inside
	 * it. Read more about Rooms here.
	 *
	 * @param name            name of the room
	 * @param owner           administrator of the room
	 * @param maxUsers        number of maximum users allowed in the room
	 * @param tableProperties properties of room for matchmaking ( pass null if not required )
	 */
	public void createRoom(String name, String owner, int maxUsers, HashMap<String, Object> tableProperties) {
		clientInstance.createRoom(name, owner, maxUsers, tableProperties);
	}

	/**
	 * Sends a create turn based room request to the server with the given meta
	 * data. Result of the request is provided in the onCreateRoomDone callback of
	 * the registered ZoneRequestListener objects. If successful, this will create a
	 * dynamic turn based room at the server. These rooms lifetime is limited till
	 * the time users are inside it. Read more about Rooms here.
	 *
	 * @param name            name of the room
	 * @param owner           owner of the room ( behavior and usage of this meta property is up
	 *                        to the developer )
	 * @param maxUsers        number of maximum users allowed in the room
	 * @param tableProperties properties of room ( can be null )
	 * @param time            the time ( in seconds ) allowed for a user to complete its turn
	 *                        and send a move .
	 */
	public void createTurnRoom(String name, String owner, int maxUsers, HashMap<String, Object> tableProperties, int time) {
		clientInstance.createTurnRoom(name, owner, maxUsers, tableProperties, time);
	}

	/**
	 * Sends a move to the server for the joined turn based room.Result of the
	 * request is provided in the onSendMoveDone callback of the registered
	 * TurnBasedRoomListener objects. If the joined user is not a turn based room or
	 * if its not the users turn, this request will fail. If successful, this will
	 * result in onMoveCompleted notification for all the subscribed users on the
	 * registered NotifyListener objects.
	 *
	 * @param moveData any meta data associated with the move
	 */
	public void sendMove(String moveData) {
		clientInstance.sendMove(moveData);
	}

	public void sendMove(String moveData, String nextTurn) {
		clientInstance.sendMove(moveData, nextTurn);
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
	 * @param nextTurn the string value for the next turn
	 */
	public void setNextTurn(String nextTurn) {
		clientInstance.setNextTurn(nextTurn);
	}

	/**
	 * Sends a start game to server in TurnBasedRoom. Result of this callback is
	 * provided in onStartGameDone of registered TurnBasedRoomListener interface. If
	 * successful game will be started in TurnBasedRoom.
	 */
	public void startGame() {
		clientInstance.startGame();
	}

	/**
	 * Sends a start game to server in TurnBasedRoom. Result of this callback is
	 * provided in onStartGameDone of registered TurnBasedRoomListener interface. If
	 * successful game will be started in TurnBasedRoom.
	 *
	 * @param isDefaultLogic a bool variable.
	 */
	public void startGame(boolean isDefaultLogic) {
		clientInstance.startGame(isDefaultLogic);
	}

	/**
	 * Sends a start game to server in TurnBasedRoom. Result of this callback is
	 * provided in onStartGameDone of registered TurnBasedRoomListener interface. If
	 * successful game will be started in TurnBasedRoom.
	 *
	 * @param isDefaultLogic a bool variable.
	 * @param nextTurn       the next turn string value.
	 */
	public void startGame(boolean isDefaultLogic, String nextTurn) {
		clientInstance.startGame(isDefaultLogic, nextTurn);
	}

	/**
	 * Sends a stop game to server in TurnBasedRoom. Result of this callback is
	 * provided in onStopGameDone of registered TurnBasedRoomListener interface. If
	 * successful game will be stopped in TurnBasedRoom.
	 */
	public void stopGame() {
		clientInstance.stopGame();
	}

	/**
	 * Sends get move history request to server in TurnBasedRoom. Result of this
	 * callback is provided in onGetMoveHistoryDone of registered
	 * TurnBasedRoomListener interface.
	 */
	public void getMoveHistory() {
		clientInstance.getMoveHistory();
	}

	/**
	 * Sends a delete room request to the server. Result of the request is provided
	 * in the onDeleteRoomDone callback of the registered ZoneRequestListener
	 * objects. Only dynamic rooms can be deleted through this API. Static rooms
	 * (created from AppHQ) can not be deleted through this. Read more about Rooms
	 * here.
	 *
	 * @param roomId Id of the room to be deleted
	 */
	public void deleteRoom(String roomId) {
		clientInstance.deleteRoom(roomId);
	}

	/**
	 * Sends a join room request to the server. Result of the request is provided in
	 * the onJoinRoomDone callback of the registered RoomRequestListener objects. A
	 * user can only be joined in one location (room or lobby) at a time. If a user
	 * joins a room, it will automatically be removed from its current location.
	 *
	 * @param roomId Id of the room to be joined
	 */
	public void joinRoom(String roomId) {
		clientInstance.joinRoom(roomId);
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
	 * @param roomId Id of the room to be join and subscribed
	 */
	public void joinAndSubscribeRoom(String roomId) {
		clientInstance.joinAndSubscribeRoom(roomId);
	}

	/**
	 * Sends a leave and unsubscribe room request to the server. Result of the
	 * request is provided in the onLeaveAndUnsubscribeRoomDone callback of the
	 * registered RoomRequestListener objects.
	 *
	 * @param roomId Id of the room to be subscribed
	 */
	public void leaveAndUnsubscribeRoom(String roomId) {
		clientInstance.leaveAndUnsubscribeRoom(roomId);
	}

	/**
	 * Sends a join room request to the server with the condition that the room must
	 * have at least minUsers and at Most maxUsers. Result of the request is
	 * provided in the onJoinRoomDone callback of the registered
	 * RoomRequestListener. This is useful is supporting quick play modes.
	 *
	 * @param minUser      number of minimum users in room to be joined
	 * @param maxUser      number of maximum users in room to be joined
	 * @param maxPreferred flag to specify search priority for room to be joined
	 */
	public void joinRoomInRange(int minUser, int maxUser, boolean maxPreferred) {
		clientInstance.joinRoomInRange(minUser, maxUser, maxPreferred);
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
	 * @param roomID          Id of the room
	 * @param tableProperties properties that will be set for the room
	 * @param removeArray     properties that will be removed for the room
	 */
	public void updateRoomProperties(String roomID, HashMap<String, Object> tableProperties, String[] removeArray) {
		clientInstance.updateRoomProperties(roomID, tableProperties, removeArray);
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
	 * @param tableProperties properties to be lock for the room
	 */
	public void lockProperties(HashMap<String, Object> tableProperties) {
		clientInstance.lockProperties(tableProperties);
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
	 * @param unlockProperties properties to be unlock for the room
	 */
	public void unlockProperties(String[] unlockProperties) {
		clientInstance.unlockProperties(unlockProperties);
	}

	/**
	 * Sends a join room request to the server with the condition that the room must
	 * have a matching set of property value pairs associated with it. This is
	 * useful in match making. Result of the request is provided in the
	 * onJoinRoomDone callback of the registered RoomRequestListener.
	 *
	 * @param tableProperties properties of the room to be joined
	 */
	public void joinRoomWithProperties(HashMap<String, Object> tableProperties) {
		clientInstance.joinRoomWithProperties(tableProperties);
	}

	/**
	 * Retrieves information of the rooms that contain at least minUsers and at most
	 * maxUsers in them. Result is provided in the onGetMatchedRoomsDone callback of
	 * the registered ZoneRequestListener objects. This is useful in building a
	 * filtered list of rooms.
	 *
	 * @param minUser  number of minimum users in room to be joined
	 * @param maxUsers number of maximum users in room to be joined
	 */
	public void getRoomsInRange(int minUser, int maxUsers) {
		clientInstance.getRoomInRange(minUser, maxUsers);
	}

	/**
	 * Retrieves information of the room that contain properties which match with
	 * the given properties. Result is provided in the onGetMatchedRoomsDone
	 * callback of the registered ZoneRequestListener objects. This is useful in
	 * building a filtered list of rooms.
	 *
	 * @param properties properties of the room to be joined
	 */
	public void getRoomsWithProperties(HashMap<String, Object> properties) {
		clientInstance.getRoomWithProperties(properties);
	}

	/**
	 * Retrieves information of the rooms that contain at least minUsers , at most
	 * maxUsers and set of property value pairs in them. Result is provided in the
	 * onGetMatchedRoomsDone callback of the registered ZoneRequestListener objects.
	 * This is useful in building a filtered list of rooms.
	 *
	 * @param minUser    number of minimum users in room to be joined
	 * @param maxUsers   number of maximum users in room to be joined
	 * @param properties properties of the room to be joined
	 */
	public void getRoomInRangeWithProperties(int minUser, int maxUsers, HashMap<String, Object> properties) {
		clientInstance.getRoomInRangeWithProperties(minUser, maxUsers, properties);
	}

	/**
	 * Sends a leave room request to the server. Result of the request is provided
	 * in the onLeaveRoomDone callback of the registered RoomRequestListener.
	 *
	 * @param roomId Id of the room to be left
	 */
	public void leaveRoom(String roomId) {
		clientInstance.leaveRoom(roomId);
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
	 * @param roomId Id of the room to be subscribed
	 */
	public void subscribeRoom(String roomId) {
		clientInstance.subscribeRoom(roomId);
	}

	/**
	 * Sends a unsubscribe room request to the server. Result of the request is
	 * provided in the onUnSubscribeRoomDone callback of the registered
	 * RoomRequestListener objects.
	 *
	 * @param roomId Id of the room to be subscribed
	 */
	public void unsubscribeRoom(String roomId) {
		clientInstance.unsubscribeRoom(roomId);
	}

	/**
	 * Sends a chat message to the room (or lobby) in which the user is currently
	 * joined. Result of the request is provided in the onSendChatDone callback of
	 * the registered ChatRequestListener objects. All users who are subscribed to
	 * the location in which the message is sent will get a onChatReceived event on
	 * their registered NotifyListener objects.
	 *
	 * @param message message to be sent
	 */
	public void sendChat(String message) {
		clientInstance.sendChat(message);
	}

	/**
	 * Sends a byte array update message to the recipient user. This is useful if
	 * developers want to send private data between the users. It is unreliable and
	 * may not work over cellular data connections – hence no result callback should
	 * be expected from it. The corresponding flavor of this API is
	 * sendPrivateUpdate which shows a similar behavior.
	 *
	 * @param toUsername recipient username
	 * @param update     byte array data to be sent
	 */
	public void sendUDPPrivateUpdate(String toUsername, byte[] update) {
		clientInstance.sendUDPPrivateUpdate(toUsername, update);
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
	 * @param toUsername recipient username
	 * @param update     byte array data to be sent
	 */
	public void sendPrivateUpdate(String toUsername, byte[] update) {
		clientInstance.sendPrivateUpdate(toUsername, update);
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
	 * @param userName recipient of the message
	 * @param message  message to be sent
	 */
	public void sendPrivateChat(String userName, String message) {
		clientInstance.sendPrivateChat(userName, message);
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
	 * @param update byte array data to be sent
	 */
	public void sendUDPUpdatePeers(byte[] update) {
		clientInstance.sendUDPUpdatePeers(update);
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
		clientInstance.initUDP();
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
	 * @param update binary data to be sent
	 */
	public void sendUpdatePeers(byte[] update) {
		clientInstance.sendUpdatePeers(update);
	}

	public void onLookUpServer(byte lookUpaStatus) {
		clientInstance.onLookUpServer(lookUpaStatus);
	}

	public void getGameStatus() {
		clientInstance.getGameStatus();
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
	 * @param message     message to be sent
	 * @param saveHistory Boolean variable indicating sent message needs to be saved or not.
	 * @param roomId      Id of the room
	 */
	public void sendChat(String message, boolean saveHistory, String roomId) {
		clientInstance.sendChat(message, saveHistory, roomId);
	}

	/**
	 * Set a db name where chat history need to be saved.
	 *
	 * @param dbName Database name that will be set for the chat history.
	 */
	public void setDbName(String dbName) {
		clientInstance.setDbName(dbName);
	}

	/**
	 * Enables client to save the chat sent using sendChat API.
	 *
	 * @param status If true, the client can save chat using sendChat API.
	 */
	public void enableChatHistory(boolean status) {
		clientInstance.enableChatHistory(status);
	}

	/**
	 * Retrieves chat history of a room of specified roomId from the server. Result
	 * is provided in the onGetChatHistoryDone callback of the registered
	 * ChatRequestListener objects.
	 *
	 * @param roomId Id of the room whose history is requested.
	 * @param max    Number of messages to be fetched from the server.
	 * @param offset index from where the messages need to be fetched.
	 */
	public void getChatHistory(String roomId, int max, int offset) {
		clientInstance.getChatHistory(roomId, max, offset);
	}
}
