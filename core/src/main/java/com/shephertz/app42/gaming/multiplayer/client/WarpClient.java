package com.shephertz.app42.gaming.multiplayer.client;

import com.shephertz.app42.gaming.multiplayer.client.listener.*;

import java.util.Map;

public interface WarpClient {
	/**
	 * It gives the Api Key of the current established connection,otherwise returns
	 * null.
	 *
	 * @return Api key
	 */
	String getAPIKey();

	/**
	 * It gives the Private/Secret Key of the current established
	 * connection,otherwise returns null.
	 *
	 * @return Private/Secret key
	 */
	String getPrivateKey();

	void setPrivateKey(String secretKey);

	void setApiKey(String apiKey);

	void setServer(String address);

	/**
	 * setGeo allows you to connect to our cloud servers in locations other than the
	 * default location. This offers developers the choice to connect to the closest
	 * server depending on the client’s device location.
	 *
	 * @param geo server location. For e.g. US, EU, JAPAN
	 */
	void setGeo(String geo);

	/**
	 * Adds (registers) the given listener object to the list of objects on which
	 * callbacks will be invoked when a response from the server is received for
	 * Connect and Disconnect APIs. The object must implement the
	 * ConnectionRequestListener interface.
	 *
	 * @param listener method for listening to the request
	 */
	void addConnectionRequestListener(ConnectionRequestListener listener);

	/**
	 * Removes the given listener object from the list of objects on which callbacks
	 * will be invoked when a response from the server is received for Connect and
	 * Disconnect APIs. The object must implement the ConnectionRequestListener
	 * interface.
	 *
	 * @param listener listener object
	 */
	void removeConnectionRequestListener(ConnectionRequestListener listener);

	/**
	 * Adds (registers) the given listener object to the list of objects on which
	 * callbacks will be invoked when a response from the server is received for
	 * zone level requests such as create/delete room or live user info requests.
	 * The object must implement the ZoneRequestListener interface.
	 *
	 * @param listener method for listening to the request
	 */
	void addZoneRequestListener(ZoneRequestListener listener);

	/**
	 * Removes the given listener object from the list of objects on which callbacks
	 * will be invoked when a response from the server is received for zone level
	 * requests such as create/delete room or live user info requests. The object
	 * must implement the ZoneRequestListener interface.
	 *
	 * @param listener listener object
	 */
	void removeZoneRequestListener(ZoneRequestListener listener);

	/**
	 * Adds (registers) the given listener object to the list of objects on which
	 * callbacks will be invoked when a response from the server is received for
	 * lobby level request. The object must implement the LobbyRequestListener
	 * interface.
	 *
	 * @param listener method for listening to the request
	 */
	void addLobbyRequestListener(LobbyRequestListener listener);

	/**
	 * Removes the given listener object from the list of objects on which callbacks
	 * will be invoked when a response from the server is received for lobby level
	 * requests. The object must implement the Lobby Request Listener interface.
	 *
	 * @param listener listener object
	 */
	void removeLobbyRequestListener(LobbyRequestListener listener);

	/**
	 * Adds (registers) the given listener object to the list of objects on which
	 * callbacks will be invoked when a response from the server is received for
	 * requests pertaining to a room. The object must implement the
	 * RoomRequestListener interface.
	 *
	 * @param listener method for listening to the request
	 */
	void addRoomRequestListener(RoomRequestListener listener);

	/**
	 * Adds (registers) the given listener object to the list of objects on which
	 * callbacks will be invoked when a response from the server is received for a
	 * SendMove request. The object must implement the TurnBasedRoomListener
	 * interface.
	 *
	 * @param listener listener object
	 */
	void addTurnBasedRoomListener(TurnBasedRoomListener listener);

	/**
	 * Removes the given listener object from the list of objects on which callbacks
	 * will be invoked when a response from the server is received for a turn based
	 * game move request. The object must implement the Turn Based Room Listener
	 * interface.
	 *
	 * @param listener listener object
	 */
	void removeTurnBasedRoomListener(TurnBasedRoomListener listener);

	/**
	 * Removes the given listener object from the list of objects on which callbacks
	 * will be invoked when a response from the server is received for requests
	 * pertaining to a room. The object must implement the RoomRequestListener
	 * interface.
	 *
	 * @param listener listener object
	 */
	void removeRoomRequestListener(RoomRequestListener listener);

	/**
	 * Adds (registers) the given listener object to the list of objects on which
	 * callbacks will be invoked when a response from the server is received for a
	 * SendChat or sendPrivateChat request. The object must implement the
	 * ChatRequestListener interface.
	 *
	 * @param listener listener object
	 */
	void addChatRequestListener(ChatRequestListener listener);

	/**
	 * Removes the given listener object from the list of objects on which callbacks
	 * will be invoked when a response from the server is received for a SendChat or
	 * sendPrivateChat request. The object must implement the ChatRequestListener
	 * interface.
	 *
	 * @param listener listener object
	 */
	void removeChatRequestListener(ChatRequestListener listener);

	/**
	 * Adds (registers) the given listener object to the list of objects on which
	 * callbacks will be invoked when a response from the server is received for a
	 * SendUpdatePeers request. The object must implement the UpdateRequestListener
	 * interface.
	 *
	 * @param listener method for listening to the request
	 */
	void addUpdateRequestListener(UpdateRequestListener listener);

	/**
	 * Removes the given listener object from the list of objects on which callbacks
	 * will be invoked when a response from the server is received for a
	 * SendUpdatePeers or SendPrivateUpdate request. The object must implement the
	 * UpdateRequestListener interface.
	 *
	 * @param listener listener object
	 */
	void removeUpdateRequestListener(UpdateRequestListener listener);

	/**
	 * Adds (registers) the given listener object to the list of objects on which
	 * callbacks will be invoked when a notification is received from the server
	 * from any subscribed location (room or lobby). The object must implement the
	 * NotifyListener interface.
	 *
	 * @param listener method for listening to the request
	 */
	void addNotificationListener(NotifyListener listener);

	/**
	 * Removes the given listener object from the list of objects on which callbacks
	 * will be invoked when a notification is received from the server from any
	 * subscribed location (room or lobby). The object must implement the
	 * NotifyListener interface.
	 *
	 * @param listener listener object
	 */
	void removeNotificationListener(NotifyListener listener);

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
	void connectWithUserName(String userName);

	/**
	 * Disconnects the connection with the AppWarp server. The result for this
	 * request will be provided in the onDisConnectDone callback of the
	 * ConnectionRequestListener.
	 */
	void disconnect();

	/**
	 * Returns the current connection state of the WarpClient instance.
	 *
	 * @return int
	 */
	int getConnectionState();

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
	void RecoverConnection();

	/**
	 * It gives sessionId of the current established connection,otherwise returns
	 * zero.
	 *
	 * @return sessionId
	 */
	int getSessionID();

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
	void RecoverConnectionWithSessionID(int session_id, String user_name);

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
	void setCustomRoomData(String roomid, String data);

	/**
	 * Updates the custom data associated with the given user on the server (if the
	 * given user is online). Result is provided in the onSetCustomUserDataDone
	 * callback of the registered ZoneRequestListener objects. It can be useful in
	 * setting status messages or avatar url’s etc for online users.
	 *
	 * @param username user for whom custom data has to be update
	 * @param data     custom data that will be set for the user
	 */
	void setCustomUserData(String username, String data);

	/**
	 * Retrieves usernames of all the users connected (online) to the server. Result
	 * is provided in the onGetOnlineUsers callback of the registered
	 * ZoneRequestListener objects.
	 */
	void getOnlineUsers();

	/**
	 * Retrieves total number of rooms on the server. Result is provided in the
	 * onGetAllRoomsCountDone callback of the registered ZoneRequestListener
	 * objects.
	 */
	void getAllRoomsCount();

	/**
	 * Retrieves total number of users connected (online) to the server. Result is
	 * provided in the onGetOnlineUsersCountDone callback of the registered
	 * ZoneRequestListener objects.
	 */
	void getOnlineUsersCount();

	/**
	 * Retrieves the room ids of all the rooms on the server. Result is provided in
	 * the onGetAllRoomsDone callback of the registered ZoneRequestListener objects.
	 * To get a filtered list of rooms, use the GetRoomWithProperties API.
	 */
	void getAllRooms();

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
	void getLiveUserInfo(String username);

	/**
	 * Retrieves status of user via username on the server. Result is provided in
	 * the onGetUserStatusDone callback of the registered ZoneRequestListener
	 * objects. This is useful to find either user is connected or not.
	 *
	 * @param username Username of the player
	 */
	void getUserStatus(String username);

	/**
	 * Retrieves the live information of the given room from the server. Result is
	 * provided in the onGetLiveRoomInfoDone callback of the registered
	 * RoomRequestListener objects. The information includes the names of the
	 * currently joined users, the rooms properties and any associated customData.
	 * This is useful in getting a snapshot of a rooms state.
	 *
	 * @param roomid Id of the room
	 */
	void getLiveRoomInfo(String roomid);

	/**
	 * Retrieves live information of the lobby from the server. Result is provided
	 * in the onGetLiveLobbyInfo callback of the registered LobbyRequestListener
	 * objects. The information returned includes the names of the users who are
	 * currently joined in the lobby.
	 */
	void getLiveLobbyInfo();

	/**
	 * Sends a join lobby request to the server. Result of the request is provided
	 * in the onJoinLobbyDone callback of the registered LobbyRequestListener
	 * objects.
	 */
	void joinLobby();

	/**
	 * Sends a leave lobby request to the server. Result of the request is provided
	 * in the onLeaveLobbyDone callback of the registered LobbyRequestListener
	 * objects.
	 */
	void leaveLobby();

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
	void subscribeLobby();

	/**
	 * Sends a unsubscribe lobby request to the server. Result of the request is
	 * provided in the onUnsubscribeLobbyDone callback of the LobbyRequestListener.
	 */
	void unsubscribeLobby();

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
	void createRoom(String name, String owner, int maxUsers, Map<String, Object> tableProperties);

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
	void createTurnRoom(String name, String owner, int maxUsers, Map<String, Object> tableProperties, int time);

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
	void sendMove(String moveData);

	void sendMove(String moveData, String nextTurn);

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
	void setNextTurn(String nextTurn);

	/**
	 * Sends a start game to server in TurnBasedRoom. Result of this callback is
	 * provided in onStartGameDone of registered TurnBasedRoomListener interface. If
	 * successful game will be started in TurnBasedRoom.
	 */
	void startGame();

	/**
	 * Sends a start game to server in TurnBasedRoom. Result of this callback is
	 * provided in onStartGameDone of registered TurnBasedRoomListener interface. If
	 * successful game will be started in TurnBasedRoom.
	 *
	 * @param isDefaultLogic a bool variable.
	 */
	void startGame(boolean isDefaultLogic);

	/**
	 * Sends a start game to server in TurnBasedRoom. Result of this callback is
	 * provided in onStartGameDone of registered TurnBasedRoomListener interface. If
	 * successful game will be started in TurnBasedRoom.
	 *
	 * @param isDefaultLogic a bool variable.
	 * @param nextTurn       the next turn string value.
	 */
	void startGame(boolean isDefaultLogic, String nextTurn);

	/**
	 * Sends a stop game to server in TurnBasedRoom. Result of this callback is
	 * provided in onStopGameDone of registered TurnBasedRoomListener interface. If
	 * successful game will be stopped in TurnBasedRoom.
	 */
	void stopGame();

	/**
	 * Sends get move history request to server in TurnBasedRoom. Result of this
	 * callback is provided in onGetMoveHistoryDone of registered
	 * TurnBasedRoomListener interface.
	 */
	void getMoveHistory();

	/**
	 * Sends a delete room request to the server. Result of the request is provided
	 * in the onDeleteRoomDone callback of the registered ZoneRequestListener
	 * objects. Only dynamic rooms can be deleted through this API. Static rooms
	 * (created from AppHQ) can not be deleted through this. Read more about Rooms
	 * here.
	 *
	 * @param roomId Id of the room to be deleted
	 */
	void deleteRoom(String roomId);

	/**
	 * Sends a join room request to the server. Result of the request is provided in
	 * the onJoinRoomDone callback of the registered RoomRequestListener objects. A
	 * user can only be joined in one location (room or lobby) at a time. If a user
	 * joins a room, it will automatically be removed from its current location.
	 *
	 * @param roomId Id of the room to be joined
	 */
	void joinRoom(String roomId);

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
	void joinAndSubscribeRoom(String roomId);

	/**
	 * Sends a leave and unsubscribe room request to the server. Result of the
	 * request is provided in the onLeaveAndUnsubscribeRoomDone callback of the
	 * registered RoomRequestListener objects.
	 *
	 * @param roomId Id of the room to be subscribed
	 */
	void leaveAndUnsubscribeRoom(String roomId);

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
	void joinRoomInRange(int minUser, int maxUser, boolean maxPreferred);

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
	void updateRoomProperties(String roomID, Map<String, Object> tableProperties, String[] removeArray);

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
	void lockProperties(Map<String, Object> tableProperties);

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
	void unlockProperties(String[] unlockProperties);

	/**
	 * Sends a join room request to the server with the condition that the room must
	 * have a matching set of property value pairs associated with it. This is
	 * useful in match making. Result of the request is provided in the
	 * onJoinRoomDone callback of the registered RoomRequestListener.
	 *
	 * @param tableProperties properties of the room to be joined
	 */
	void joinRoomWithProperties(Map<String, Object> tableProperties);

	/**
	 * Retrieves information of the rooms that contain at least minUsers and at most
	 * maxUsers in them. Result is provided in the onGetMatchedRoomsDone callback of
	 * the registered ZoneRequestListener objects. This is useful in building a
	 * filtered list of rooms.
	 *
	 * @param minUser  number of minimum users in room to be joined
	 * @param maxUsers number of maximum users in room to be joined
	 */
	void getRoomsInRange(int minUser, int maxUsers);

	/**
	 * Retrieves information of the room that contain properties which match with
	 * the given properties. Result is provided in the onGetMatchedRoomsDone
	 * callback of the registered ZoneRequestListener objects. This is useful in
	 * building a filtered list of rooms.
	 *
	 * @param properties properties of the room to be joined
	 */
	void getRoomsWithProperties(Map<String, Object> properties);

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
	void getRoomInRangeWithProperties(int minUser, int maxUsers, Map<String, Object> properties);

	/**
	 * Sends a leave room request to the server. Result of the request is provided
	 * in the onLeaveRoomDone callback of the registered RoomRequestListener.
	 *
	 * @param roomId Id of the room to be left
	 */
	void leaveRoom(String roomId);

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
	void subscribeRoom(String roomId);

	/**
	 * Sends a unsubscribe room request to the server. Result of the request is
	 * provided in the onUnSubscribeRoomDone callback of the registered
	 * RoomRequestListener objects.
	 *
	 * @param roomId Id of the room to be subscribed
	 */
	void unsubscribeRoom(String roomId);

	/**
	 * Sends a chat message to the room (or lobby) in which the user is currently
	 * joined. Result of the request is provided in the onSendChatDone callback of
	 * the registered ChatRequestListener objects. All users who are subscribed to
	 * the location in which the message is sent will get a onChatReceived event on
	 * their registered NotifyListener objects.
	 *
	 * @param message message to be sent
	 */
	void sendChat(String message);

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
	void sendUDPPrivateUpdate(String toUsername, byte[] update);

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
	void sendPrivateUpdate(String toUsername, byte[] update);

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
	void sendPrivateChat(String userName, String message);

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
	void sendUDPUpdatePeers(byte[] update);

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
	void initUDP();

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
	void sendUpdatePeers(byte[] update);

	void onLookUpServer(byte lookUpaStatus);

	void getGameStatus();

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
	void sendChat(String message, boolean saveHistory, String roomId);

	/**
	 * Set a db name where chat history need to be saved.
	 *
	 * @param dbName Database name that will be set for the chat history.
	 */
	void setDbName(String dbName);

	/**
	 * Enables client to save the chat sent using sendChat API.
	 *
	 * @param status If true, the client can save chat using sendChat API.
	 */
	void enableChatHistory(boolean status);

	/**
	 * Retrieves chat history of a room of specified roomId from the server. Result
	 * is provided in the onGetChatHistoryDone callback of the registered
	 * ChatRequestListener objects.
	 *
	 * @param roomId Id of the room whose history is requested.
	 * @param max    Number of messages to be fetched from the server.
	 * @param offset index from where the messages need to be fetched.
	 */
	void getChatHistory(String roomId, int max, int offset);
}
