package de.SimonIT.App42MultiPlayerGamingSDK;

import com.google.gwt.core.client.JavaScriptObject;
import de.SimonIT.App42MultiPlayerGamingSDK.listener.*;

import java.util.HashMap;

public class GwtWarpClient extends JavaScriptObject implements WarpClient {
	protected GwtWarpClient() {
	}

	/**
	 * Sets the time allowed to the client to recover from an intermittent
	 * connection loss. This must be set before the initial connect API is called as
	 * that associates the value on the server for the given connection.
	 *
	 * @param maxRecoveryTime time - the time (in seconds) allowed to the client to recover from
	 *                        intermittent connection loss
	 */
	public native static void setRecoveryAllowance(int maxRecoveryTime) /*-{
        $wnd.AppWarp.WarpClient.recoveryAllowance = maxRecoveryTime;
    }-*/;

	/**
	 * It returns the singleton instance of WarpClient.This should be initialized
	 * with a key pair before it is used.
	 *
	 * @return singleton instance of WarpClient.
	 * @throws Exception WarpClient not initialized
	 */
	public native static GwtWarpClient getInstance() throws Exception /*-{
        return $wnd.AppWarp.WarpClient.getInstance();
    }-*/;

	/**
	 * Initializes the singleton instance of WarpClient with the developer
	 * credentials. This has to be called only once during the lifetime of the
	 * application. It is required before you can call any other API.
	 *
	 * @param apiKey The Application key given when the application was created.
	 * @param pvtKey The Application key given when the application was created.
	 * @return WarpResponseResultCode
	 */
	public native static byte initialize(String apiKey, String pvtKey) /*-{
        return $wnd.AppWarp.WarpClient.initialize(apiKey, pvtKey);
    }-*/;

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
	public native static byte initialize(String apiKey, String pvtKey, String server) /*-{
        return $wnd.AppWarp.WarpClient.initialize(apiKey, pvtKey, server);
    }-*/;

	/**
	 * It gives the Api Key of the current established connection,otherwise returns
	 * null.
	 *
	 * @return Api key
	 */
	public final native String getAPIKey() /*-{
        return this.apiKey;
    }-*/;

	/**
	 * It gives the Private/Secret Key of the current established
	 * connection,otherwise returns null.
	 *
	 * @return Private/Secret key
	 */
	public final native String getPrivateKey() /*-{
        return this.secretKey;
    }-*/;

	public final native void setPrivateKey(String secretKey) /*-{
        this.secretKey = secretKey;
    }-*/;

	public final native void setApiKey(String apiKey) /*-{
        this.apiKey = apiKey;
    }-*/;

	public final native void setServer(String address) /*-{
        this.serverAddress = address;
    }-*/;

	/**
	 * setGeo allows you to connect to our cloud servers in locations other than the
	 * default location. This offers developers the choice to connect to the closest
	 * server depending on the client’s device location.
	 *
	 * @param geo server location. For e.g. US, EU, JAPAN
	 */
	public final native void setGeo(String geo) /*-{
        this.setGeoLocation(geo);
    }-*/;

	/**
	 * Adds (registers) the given listener object to the list of objects on which
	 * callbacks will be invoked when a response from the server is received for
	 * Connect and Disconnect APIs. The object must implement the
	 * ConnectionRequestListener interface.
	 *
	 * @param listener method for listening to the request
	 */
	public final native void addConnectionRequestListener(ConnectionRequestListener listener) /*-{
        this.setResponseListener($wnd.AppWarp.Events.onConnectDone, function (res, reasonCode) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.ConnectionRequestListener::onConnectDone(Lde/SimonIT/App42MultiPlayerGamingSDK/events/ConnectEvent;)(@de.SimonIT.App42MultiPlayerGamingSDK.events.ConnectEvent::new(BI)(res, reasonCode));
        });
        this.setResponseListener($wnd.AppWarp.Events.onDisconnectDone, function (res) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.ConnectionRequestListener::onDisconnectDone(Lde/SimonIT/App42MultiPlayerGamingSDK/events/ConnectEvent;)(@de.SimonIT.App42MultiPlayerGamingSDK.events.ConnectEvent::new(B)(res));
        });
        this.setResponseListener("onInitUDPDone", function (res) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.ConnectionRequestListener::onInitUDPDone(B)(res);
        });
    }-*/;

	/**
	 * Removes the given listener object from the list of objects on which callbacks
	 * will be invoked when a response from the server is received for Connect and
	 * Disconnect APIs. The object must implement the ConnectionRequestListener
	 * interface.
	 *
	 * @param listener listener object
	 */
	public final native void removeConnectionRequestListener(ConnectionRequestListener listener) /*-{
        this.setResponseListener($wnd.AppWarp.Events.onConnectDone, undefined);
        this.setResponseListener($wnd.AppWarp.Events.onDisconnectDone, undefined);
        this.setResponseListener("onInitUDPDone", undefined);
    }-*/;

	/**
	 * Adds (registers) the given listener object to the list of objects on which
	 * callbacks will be invoked when a response from the server is received for
	 * zone level requests such as create/delete room or live user info requests.
	 * The object must implement the ZoneRequestListener interface.
	 *
	 * @param listener method for listening to the request
	 */
	public final native void addZoneRequestListener(ZoneRequestListener listener) /*-{
        this.setResponseListener($wnd.AppWarp.Events.onDeleteRoomDone, function (room) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.ZoneRequestListener::onDeleteRoomDone(Lde/SimonIT/App42MultiPlayerGamingSDK/events/RoomEvent;)(@de.SimonIT.App42MultiPlayerGamingSDK.events.RoomEvent::new(Lde/SimonIT/App42MultiPlayerGamingSDK/events/RoomData;B)(@de.SimonIT.App42MultiPlayerGamingSDK.events.RoomData::new(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)(room.getRoomId(), room.getOwner(), room.getName(), room.getMaxUsers()), room.getResult()));
        });
        this.setResponseListener($wnd.AppWarp.Events.onGetAllRoomsDone, function (rooms) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.ZoneRequestListener::onGetAllRoomsDone(Lde/SimonIT/App42MultiPlayerGamingSDK/events/AllRoomsEvent;)(@de.SimonIT.App42MultiPlayerGamingSDK.events.AllRoomsEvent::new(B[Ljava/lang/String;)(rooms.getResult(), @de.SimonIT.App42MultiPlayerGamingSDK.util.JsUtil::toJavaByteArray(*)(rooms.getRoomIds())));
        });
        this.setResponseListener($wnd.AppWarp.Events.onCreateRoomDone, function (room) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.ZoneRequestListener::onCreateRoomDone(Lde/SimonIT/App42MultiPlayerGamingSDK/events/RoomEvent;)(@de.SimonIT.App42MultiPlayerGamingSDK.events.RoomEvent::new(Lde/SimonIT/App42MultiPlayerGamingSDK/events/RoomData;B)(@de.SimonIT.App42MultiPlayerGamingSDK.events.RoomData::new(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)(room.getRoomId(), room.getOwner(), room.getName(), room.getMaxUsers()), room.getResult()));
        });
        this.setResponseListener($wnd.AppWarp.Events.onGetOnlineUsersDone, function (users) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.ZoneRequestListener::onGetOnlineUsersDone(Lde/SimonIT/App42MultiPlayerGamingSDK/events/AllUsersEvent;)(@de.SimonIT.App42MultiPlayerGamingSDK.events.AllUsersEvent::new(B[Ljava/lang/String;)(users.getResult(), @de.SimonIT.App42MultiPlayerGamingSDK.util.JsUtil::toJavaByteArray(*)(users.getUsernames())));
        });
        this.setResponseListener($wnd.AppWarp.Events.onGetLiveUserInfoDone, function (user) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.ZoneRequestListener::onGetLiveUserInfoDone(Lde/SimonIT/App42MultiPlayerGamingSDK/events/LiveUserInfoEvent;)(@de.SimonIT.App42MultiPlayerGamingSDK.events.LiveUserInfoEvent::new(BLjava/lang/String;Ljava/lang/String;Ljava/lang/String;ZZ)(user.getResult(), user.getLocationId(), user.getName(), user.getCustomData(), user.isLobby(), user.isPaused()));
        });
        this.setResponseListener($wnd.AppWarp.Events.onSetCustomUserDataDone, function (user) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.ZoneRequestListener::onSetCustomUserDataDone(Lde/SimonIT/App42MultiPlayerGamingSDK/events/LiveUserInfoEvent;)(@de.SimonIT.App42MultiPlayerGamingSDK.events.LiveUserInfoEvent::new(BLjava/lang/String;Ljava/lang/String;Ljava/lang/String;ZZ)(user.getResult(), user.getLocationId(), user.getName(), user.getCustomData(), user.isLobby(), user.isPaused()));
        });
        this.setResponseListener($wnd.AppWarp.Events.onGetMatchedRoomsDone, function (event) {
            var roomDatas = [];
            var rooms = event.getRooms();
            for (var i = 0; i < rooms.length; i++) {
                roomDatas[i] = @de.SimonIT.App42MultiPlayerGamingSDK.events.RoomData::new(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)(rooms[i].getRoomId(), rooms[i].getOwner(), rooms[i].getName(), rooms[i].getMaxUsers());
            }
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.ZoneRequestListener::onGetMatchedRoomsDone(Lde/SimonIT/App42MultiPlayerGamingSDK/events/MatchedRoomsEvent;)(@de.SimonIT.App42MultiPlayerGamingSDK.events.MatchedRoomsEvent::new(B[Lde/SimonIT/App42MultiPlayerGamingSDK/events/RoomData;)(event.getResult(), @de.SimonIT.App42MultiPlayerGamingSDK.util.JsUtil::toJavaArray(*)(roomDatas)));
        });
        this.setResponseListener($wnd.AppWarp.Events.onGetAllRoomsCountDone, function (result, count) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.ZoneRequestListener::onGetAllRoomsCountDone(Lde/SimonIT/App42MultiPlayerGamingSDK/events/AllRoomsEvent;)(@de.SimonIT.App42MultiPlayerGamingSDK.events.AllRoomsEvent::new(BI)(result, count));
        });
        this.setResponseListener($wnd.AppWarp.Events.onGetOnlineUsersCountDone, function (result, count) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.ZoneRequestListener::onGetOnlineUsersCountDone(Lde/SimonIT/App42MultiPlayerGamingSDK/events/AllUsersEvent;)(@de.SimonIT.App42MultiPlayerGamingSDK.events.AllUsersEvent::new(IB)(count, result));
        });
        this.setResponseListener($wnd.AppWarp.Events.onUserStatusDone, function (user) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.ZoneRequestListener::onGetLiveUserInfoDone(Lde/SimonIT/App42MultiPlayerGamingSDK/events/LiveUserInfoEvent;)(@de.SimonIT.App42MultiPlayerGamingSDK.events.LiveUserInfoEvent::new(BLjava/lang/String;Ljava/lang/String;Ljava/lang/String;ZZ)(user.getResult(), user.getLocationId(), user.getName(), user.getCustomData(), user.isLobby(), user.isPaused()));
        });
    }-*/;

	/**
	 * Removes the given listener object from the list of objects on which callbacks
	 * will be invoked when a response from the server is received for zone level
	 * requests such as create/delete room or live user info requests. The object
	 * must implement the ZoneRequestListener interface.
	 *
	 * @param listener listener object
	 */
	public final native void removeZoneRequestListener(ZoneRequestListener listener) /*-{
        this.setResponseListener($wnd.AppWarp.Events.onDeleteRoomDone, undefined);
        this.setResponseListener($wnd.AppWarp.Events.onGetAllRoomsDone, undefined);
        this.setResponseListener($wnd.AppWarp.Events.onCreateRoomDone, undefined);
        this.setResponseListener($wnd.AppWarp.Events.onGetOnlineUsersDone, undefined);
        this.setResponseListener($wnd.AppWarp.Events.onGetLiveUserInfoDone, undefined);
        this.setResponseListener($wnd.AppWarp.Events.onSetCustomUserDataDone, undefined);
        this.setResponseListener($wnd.AppWarp.Events.onGetMatchedRoomsDone, undefined);
        this.setResponseListener($wnd.AppWarp.Events.onGetAllRoomsCountDone, undefined);
        this.setResponseListener($wnd.AppWarp.Events.onGetOnlineUsersCountDone, undefined);
        this.setResponseListener($wnd.AppWarp.Events.onUserStatusDone, undefined);
    }-*/;

	/**
	 * Adds (registers) the given listener object to the list of objects on which
	 * callbacks will be invoked when a response from the server is received for
	 * lobby level request. The object must implement the LobbyRequestListener
	 * interface.
	 *
	 * @param listener method for listening to the request
	 */
	public final native void addLobbyRequestListener(LobbyRequestListener listener) /*-{
        this.setResponseListener($wnd.AppWarp.Events.onJoinLobbyDone, function (lobby) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.LobbyRequestListener::onJoinLobbyDone(Lde/SimonIT/App42MultiPlayerGamingSDK/events/LobbyEvent;)(@de.SimonIT.App42MultiPlayerGamingSDK.events.LobbyEvent::new(Lde/SimonIT/App42MultiPlayerGamingSDK/events/LobbyData;B)(@de.SimonIT.App42MultiPlayerGamingSDK.events.LobbyData::new(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;IZ)(lobby.getLobbyId(), lobby.getOwner(), lobby.getName(), lobby.getMaxUsers(), lobby.getIsPrimary()), lobby.getResult()));
        });
        this.setResponseListener($wnd.AppWarp.Events.onLeaveLobbyDone, function (lobby) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.LobbyRequestListener::onLeaveLobbyDone(Lde/SimonIT/App42MultiPlayerGamingSDK/events/LobbyEvent;)(@de.SimonIT.App42MultiPlayerGamingSDK.events.LobbyEvent::new(Lde/SimonIT/App42MultiPlayerGamingSDK/events/LobbyData;B)(@de.SimonIT.App42MultiPlayerGamingSDK.events.LobbyData::new(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;IZ)(lobby.getLobbyId(), lobby.getOwner(), lobby.getName(), lobby.getMaxUsers(), lobby.getIsPrimary()), lobby.getResult()));
        });
        this.setResponseListener($wnd.AppWarp.Events.onSubscribeLobbyDone, function (lobby) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.LobbyRequestListener::onSubscribeLobbyDone(Lde/SimonIT/App42MultiPlayerGamingSDK/events/LobbyEvent;)(@de.SimonIT.App42MultiPlayerGamingSDK.events.LobbyEvent::new(Lde/SimonIT/App42MultiPlayerGamingSDK/events/LobbyData;B)(@de.SimonIT.App42MultiPlayerGamingSDK.events.LobbyData::new(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;IZ)(lobby.getLobbyId(), lobby.getOwner(), lobby.getName(), lobby.getMaxUsers(), lobby.getIsPrimary()), lobby.getResult()));
        });
        this.setResponseListener($wnd.AppWarp.Events.onUnsubscribeLobbyDone, function (lobby) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.LobbyRequestListener::onUnSubscribeLobbyDone(Lde/SimonIT/App42MultiPlayerGamingSDK/events/LobbyEvent;)(@de.SimonIT.App42MultiPlayerGamingSDK.events.LobbyEvent::new(Lde/SimonIT/App42MultiPlayerGamingSDK/events/LobbyData;B)(@de.SimonIT.App42MultiPlayerGamingSDK.events.LobbyData::new(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;IZ)(lobby.getLobbyId(), lobby.getOwner(), lobby.getName(), lobby.getMaxUsers(), lobby.getIsPrimary()), lobby.getResult()));
        });
        this.setResponseListener($wnd.AppWarp.Events.onGetLiveLobbyInfoDone, function (liveRoom) {
            var room = liveRoom.getRoom();
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.LobbyRequestListener::onGetLiveLobbyInfoDone(Lde/SimonIT/App42MultiPlayerGamingSDK/events/LiveRoomInfoEvent;)(@de.SimonIT.App42MultiPlayerGamingSDK.events.LiveRoomInfoEvent::new(Lde/SimonIT/App42MultiPlayerGamingSDK/events/RoomData;B[Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;Ljava/util/Map;)(@de.SimonIT.App42MultiPlayerGamingSDK.events.RoomData::new(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)(room.getRoomId(), room.getOwner(), room.getName(), room.getMaxUsers()), liveRoom.getResult(), @de.SimonIT.App42MultiPlayerGamingSDK.util.JsUtil::toJavaByteArray(*)(liveRoom.getUsers()), liveRoom.getCustomData(), @de.SimonIT.App42MultiPlayerGamingSDK.util.JsUtil::toJavaMap(*)(liveRoom.getProperties()), null));
        });
    }-*/;

	/**
	 * Removes the given listener object from the list of objects on which callbacks
	 * will be invoked when a response from the server is received for lobby level
	 * requests. The object must implement the Lobby Request Listener interface.
	 *
	 * @param listener listener object
	 */
	public final native void removeLobbyRequestListener(LobbyRequestListener listener) /*-{
        this.setResponseListener($wnd.AppWarp.Events.onJoinLobbyDone, undefined);
        this.setResponseListener($wnd.AppWarp.Events.onLeaveLobbyDone, undefined);
        this.setResponseListener($wnd.AppWarp.Events.onSubscribeLobbyDone, undefined);
        this.setResponseListener($wnd.AppWarp.Events.onUnsubscribeLobbyDone, undefined);
        this.setResponseListener($wnd.AppWarp.Events.onGetLiveLobbyInfoDone, undefined);
    }-*/;

	/**
	 * Adds (registers) the given listener object to the list of objects on which
	 * callbacks will be invoked when a response from the server is received for
	 * requests pertaining to a room. The object must implement the
	 * RoomRequestListener interface.
	 *
	 * @param listener method for listening to the request
	 */
	public final native void addRoomRequestListener(RoomRequestListener listener) /*-{
        this.setResponseListener($wnd.AppWarp.Events.onSubscribeRoomDone, function (room) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.RoomRequestListener::onSubscribeRoomDone(Lde/SimonIT/App42MultiPlayerGamingSDK/events/RoomEvent;)(@de.SimonIT.App42MultiPlayerGamingSDK.events.RoomEvent::new(Lde/SimonIT/App42MultiPlayerGamingSDK/events/RoomData;B)(@de.SimonIT.App42MultiPlayerGamingSDK.events.RoomData::new(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)(room.getRoomId(), room.getOwner(), room.getName(), room.getMaxUsers()), room.getResult()));
        });
        this.setResponseListener($wnd.AppWarp.Events.onUnsubscribeRoomDone, function (room) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.RoomRequestListener::onUnSubscribeRoomDone(Lde/SimonIT/App42MultiPlayerGamingSDK/events/RoomEvent;)(@de.SimonIT.App42MultiPlayerGamingSDK.events.RoomEvent::new(Lde/SimonIT/App42MultiPlayerGamingSDK/events/RoomData;B)(@de.SimonIT.App42MultiPlayerGamingSDK.events.RoomData::new(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)(room.getRoomId(), room.getOwner(), room.getName(), room.getMaxUsers()), room.getResult()));
        });
        this.setResponseListener($wnd.AppWarp.Events.onJoinRoomDone, function (room) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.RoomRequestListener::onJoinRoomDone(Lde/SimonIT/App42MultiPlayerGamingSDK/events/RoomEvent;)(@de.SimonIT.App42MultiPlayerGamingSDK.events.RoomEvent::new(Lde/SimonIT/App42MultiPlayerGamingSDK/events/RoomData;B)(@de.SimonIT.App42MultiPlayerGamingSDK.events.RoomData::new(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)(room.getRoomId(), room.getOwner(), room.getName(), room.getMaxUsers()), room.getResult()));
        });
        this.setResponseListener($wnd.AppWarp.Events.onLeaveRoomDone, function (room) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.RoomRequestListener::onLeaveRoomDone(Lde/SimonIT/App42MultiPlayerGamingSDK/events/RoomEvent;)(@de.SimonIT.App42MultiPlayerGamingSDK.events.RoomEvent::new(Lde/SimonIT/App42MultiPlayerGamingSDK/events/RoomData;B)(@de.SimonIT.App42MultiPlayerGamingSDK.events.RoomData::new(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)(room.getRoomId(), room.getOwner(), room.getName(), room.getMaxUsers()), room.getResult()));
        });
        this.setResponseListener($wnd.AppWarp.Events.onGetLiveRoomInfoDone, function (liveRoom) {
            var room = liveRoom.getRoom();
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.RoomRequestListener::onGetLiveRoomInfoDone(Lde/SimonIT/App42MultiPlayerGamingSDK/events/LiveRoomInfoEvent;)(@de.SimonIT.App42MultiPlayerGamingSDK.events.LiveRoomInfoEvent::new(Lde/SimonIT/App42MultiPlayerGamingSDK/events/RoomData;B[Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;Ljava/util/Map;)(@de.SimonIT.App42MultiPlayerGamingSDK.events.RoomData::new(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)(room.getRoomId(), room.getOwner(), room.getName(), room.getMaxUsers()), liveRoom.getResult(), @de.SimonIT.App42MultiPlayerGamingSDK.util.JsUtil::toJavaByteArray(*)(liveRoom.getUsers()), liveRoom.getCustomData(), @de.SimonIT.App42MultiPlayerGamingSDK.util.JsUtil::toJavaMap(*)(liveRoom.getProperties()), null));
        });
        this.setResponseListener($wnd.AppWarp.Events.onSetCustomRoomDataDone, function (liveRoom) {
            var room = liveRoom.getRoom();
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.RoomRequestListener::onSetCustomRoomDataDone(Lde/SimonIT/App42MultiPlayerGamingSDK/events/LiveRoomInfoEvent;)(@de.SimonIT.App42MultiPlayerGamingSDK.events.LiveRoomInfoEvent::new(Lde/SimonIT/App42MultiPlayerGamingSDK/events/RoomData;B[Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;Ljava/util/Map;)(@de.SimonIT.App42MultiPlayerGamingSDK.events.RoomData::new(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)(room.getRoomId(), room.getOwner(), room.getName(), room.getMaxUsers()), liveRoom.getResult(), @de.SimonIT.App42MultiPlayerGamingSDK.util.JsUtil::toJavaByteArray(*)(liveRoom.getUsers()), liveRoom.getCustomData(), @de.SimonIT.App42MultiPlayerGamingSDK.util.JsUtil::toJavaMap(*)(liveRoom.getProperties()), null));
        });
        this.setResponseListener($wnd.AppWarp.Events.onUpdatePropertyDone, function (liveRoom) {
            var room = liveRoom.getRoom();
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.RoomRequestListener::onUpdatePropertyDone(Lde/SimonIT/App42MultiPlayerGamingSDK/events/LiveRoomInfoEvent;)(@de.SimonIT.App42MultiPlayerGamingSDK.events.LiveRoomInfoEvent::new(Lde/SimonIT/App42MultiPlayerGamingSDK/events/RoomData;B[Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;Ljava/util/Map;)(@de.SimonIT.App42MultiPlayerGamingSDK.events.RoomData::new(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)(room.getRoomId(), room.getOwner(), room.getName(), room.getMaxUsers()), liveRoom.getResult(), @de.SimonIT.App42MultiPlayerGamingSDK.util.JsUtil::toJavaByteArray(*)(liveRoom.getUsers()), liveRoom.getCustomData(), @de.SimonIT.App42MultiPlayerGamingSDK.util.JsUtil::toJavaMap(*)(liveRoom.getProperties()), null));
        });
        this.setResponseListener($wnd.AppWarp.Events.onLockPropertiesDone, function (res) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.RoomRequestListener::onLockPropertiesDone(B)(res);
        });
        this.setResponseListener($wnd.AppWarp.Events.onUnlockPropertiesDone, function (res) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.RoomRequestListener::onUnlockPropertiesDone(B)(res);
        });
        this.setResponseListener($wnd.AppWarp.Events.onJoinAndSubscribeRoomDone, function (room) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.RoomRequestListener::onJoinAndSubscribeRoomDone(Lde/SimonIT/App42MultiPlayerGamingSDK/events/RoomEvent;)(@de.SimonIT.App42MultiPlayerGamingSDK.events.RoomEvent::new(Lde/SimonIT/App42MultiPlayerGamingSDK/events/RoomData;B)(@de.SimonIT.App42MultiPlayerGamingSDK.events.RoomData::new(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)(room.getRoomId(), room.getOwner(), room.getName(), room.getMaxUsers()), room.getResult()));
        });
        this.setResponseListener($wnd.AppWarp.Events.onLeaveAndUnsubscribeRoomDone, function (room) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.RoomRequestListener::onLeaveAndUnsubscribeRoomDone(Lde/SimonIT/App42MultiPlayerGamingSDK/events/RoomEvent;)(@de.SimonIT.App42MultiPlayerGamingSDK.events.RoomEvent::new(Lde/SimonIT/App42MultiPlayerGamingSDK/events/RoomData;B)(@de.SimonIT.App42MultiPlayerGamingSDK.events.RoomData::new(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)(room.getRoomId(), room.getOwner(), room.getName(), room.getMaxUsers()), room.getResult()));
        });
    }-*/;

	/**
	 * Adds (registers) the given listener object to the list of objects on which
	 * callbacks will be invoked when a response from the server is received for a
	 * SendMove request. The object must implement the TurnBasedRoomListener
	 * interface.
	 *
	 * @param listener listener object
	 */
	public final native void addTurnBasedRoomListener(TurnBasedRoomListener listener) /*-{
        this.setResponseListener($wnd.AppWarp.Events.onSendMoveDone, function (res) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.TurnBasedRoomListener::onSendMoveDone(B)(res);
        });
        this.setResponseListener($wnd.AppWarp.Events.onStartGameDone, function (res) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.TurnBasedRoomListener::onStartGameDone(B)(res);
        });
        this.setResponseListener($wnd.AppWarp.Events.onStopGameDone, function (res) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.TurnBasedRoomListener::onStopGameDone(B)(res);
        });
        this.setResponseListener($wnd.AppWarp.Events.onStopGameDone, function (res, moves) {
            var jmoves = [];
            for (var i = 0; i < moves.length; i++) {
                jmoves[i] = @de.SimonIT.App42MultiPlayerGamingSDK.events.MoveEvent::new(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(moves[i].getSender(), moves[i].getMoveData(), moves[i].getNextTurn(), moves[i].getRoomId());
            }
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.TurnBasedRoomListener::onGetMoveHistoryDone(B[Lde/SimonIT/App42MultiPlayerGamingSDK/events/MoveEvent;)(res, @de.SimonIT.App42MultiPlayerGamingSDK.util.JsUtil::toJavaArray(*)(jmoves));
        });
        this.setResponseListener($wnd.AppWarp.Events.onSetNextTurnDone, function (res) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.TurnBasedRoomListener::onSetNextTurnDone(B)(res);
        });
    }-*/;

	/**
	 * Removes the given listener object from the list of objects on which callbacks
	 * will be invoked when a response from the server is received for a turn based
	 * game move request. The object must implement the Turn Based Room Listener
	 * interface.
	 *
	 * @param listener listener object
	 */
	public final native void removeTurnBasedRoomListener(TurnBasedRoomListener listener) /*-{
        this.setResponseListener($wnd.AppWarp.Events.onSendMoveDone, undefined);
        this.setResponseListener($wnd.AppWarp.Events.onStartGameDone, undefined);
        this.setResponseListener($wnd.AppWarp.Events.onStopGameDone, undefined);
        this.setResponseListener($wnd.AppWarp.Events.onStopGameDone, undefined);
        this.setResponseListener($wnd.AppWarp.Events.onSetNextTurnDone, undefined);
    }-*/;

	/**
	 * Removes the given listener object from the list of objects on which callbacks
	 * will be invoked when a response from the server is received for requests
	 * pertaining to a room. The object must implement the RoomRequestListener
	 * interface.
	 *
	 * @param listener listener object
	 */
	public final native void removeRoomRequestListener(RoomRequestListener listener) /*-{
        this.setResponseListener($wnd.AppWarp.Events.onSubscribeRoomDone, undefined);
        this.setResponseListener($wnd.AppWarp.Events.onUnsubscribeRoomDone, undefined);
        this.setResponseListener($wnd.AppWarp.Events.onJoinRoomDone, undefined);
        this.setResponseListener($wnd.AppWarp.Events.onLeaveRoomDone, undefined);
        this.setResponseListener($wnd.AppWarp.Events.onGetLiveRoomInfoDone, undefined);
        this.setResponseListener($wnd.AppWarp.Events.onSetCustomRoomDataDone, undefined);
        this.setResponseListener($wnd.AppWarp.Events.onUpdatePropertyDone, undefined);
        this.setResponseListener($wnd.AppWarp.Events.onLockPropertiesDone, undefined);
        this.setResponseListener($wnd.AppWarp.Events.onUnlockPropertiesDone, undefined);
        this.setResponseListener($wnd.AppWarp.Events.onJoinAndSubscribeRoomDone, undefined);
        this.setResponseListener($wnd.AppWarp.Events.onLeaveAndUnsubscribeRoomDone, undefined);
    }-*/;

	/**
	 * Adds (registers) the given listener object to the list of objects on which
	 * callbacks will be invoked when a response from the server is received for a
	 * SendChat or sendPrivateChat request. The object must implement the
	 * ChatRequestListener interface.
	 *
	 * @param listener listener object
	 */
	public final native void addChatRequestListener(ChatRequestListener listener) /*-{
        this.setResponseListener($wnd.AppWarp.Events.onSendChatDone, function (res) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.ChatRequestListener::onSendChatDone(B)(res);
        });
        this.setResponseListener($wnd.AppWarp.Events.onSendPrivateChatDone, function (res) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.ChatRequestListener::onSendPrivateChatDone(B)(res);
        });
        this.setResponseListener($wnd.AppWarp.Events.onGetChatHistoryDone, function (res, chat) {
            var jchat = [];
            for (var i = 0; i < chat.length; i++) {
                jchat[i] = @de.SimonIT.App42MultiPlayerGamingSDK.events.ChatEvent::new(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)(chat[i].getChat(), chat[i].getSender(), chat[i].getLocId(), chat[i].getIsLocationLobby());
            }
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.ChatRequestListener::onGetChatHistoryDone(BLjava/util/List;)(res, @de.SimonIT.App42MultiPlayerGamingSDK.util.JsUtil::toJavaList(*)(jchat));
        });
    }-*/;

	/**
	 * Removes the given listener object from the list of objects on which callbacks
	 * will be invoked when a response from the server is received for a SendChat or
	 * sendPrivateChat request. The object must implement the ChatRequestListener
	 * interface.
	 *
	 * @param listener listener object
	 */
	public final native void removeChatRequestListener(ChatRequestListener listener) /*-{
        this.setResponseListener($wnd.AppWarp.Events.onSendChatDone, undefined);
        this.setResponseListener($wnd.AppWarp.Events.onSendPrivateChatDone, undefined);
        this.setResponseListener($wnd.AppWarp.Events.onGetChatHistoryDone, undefined);
    }-*/;

	/**
	 * Adds (registers) the given listener object to the list of objects on which
	 * callbacks will be invoked when a response from the server is received for a
	 * SendUpdatePeers request. The object must implement the UpdateRequestListener
	 * interface.
	 *
	 * @param listener method for listening to the request
	 */
	public final native void addUpdateRequestListener(UpdateRequestListener listener) /*-{
        this.setResponseListener($wnd.AppWarp.Events.onSendUpdateDone, function (res) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.UpdateRequestListener::onSendUpdateDone(B)(res);
        });
        this.setResponseListener($wnd.AppWarp.Events.onSendPrivateUpdateDone, function (res) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.UpdateRequestListener::onSendPrivateUpdateDone(B)(res);
        });
    }-*/;

	/**
	 * Removes the given listener object from the list of objects on which callbacks
	 * will be invoked when a response from the server is received for a
	 * SendUpdatePeers or SendPrivateUpdate request. The object must implement the
	 * UpdateRequestListener interface.
	 *
	 * @param listener listener object
	 */
	public final native void removeUpdateRequestListener(UpdateRequestListener listener) /*-{
        this.setResponseListener($wnd.AppWarp.Events.onSendUpdateDone, undefined);
        this.setResponseListener($wnd.AppWarp.Events.onSendPrivateUpdateDone, undefined);
    }-*/;

	/**
	 * Adds (registers) the given listener object to the list of objects on which
	 * callbacks will be invoked when a notification is received from the server
	 * from any subscribed location (room or lobby). The object must implement the
	 * NotifyListener interface.
	 *
	 * @param listener method for listening to the request
	 */
	public final native void addNotificationListener(NotifyListener listener) /*-{
        this.setNotifyListener($wnd.AppWarp.Events.onRoomCreated, function (room) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.NotifyListener::onRoomCreated(Lde/SimonIT/App42MultiPlayerGamingSDK/events/RoomData;)((@de.SimonIT.App42MultiPlayerGamingSDK.events.RoomData::new(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)(room.getRoomId(), room.getOwner(), room.getName(), room.getMaxUsers())));
        });
        this.setNotifyListener($wnd.AppWarp.Events.onRoomDestroyed, function (room) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.NotifyListener::onRoomDestroyed(Lde/SimonIT/App42MultiPlayerGamingSDK/events/RoomData;)((@de.SimonIT.App42MultiPlayerGamingSDK.events.RoomData::new(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)(room.getRoomId(), room.getOwner(), room.getName(), room.getMaxUsers())));
        });
        this.setNotifyListener($wnd.AppWarp.Events.onUserLeftRoom, function (room, user) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.NotifyListener::onUserLeftRoom(Lde/SimonIT/App42MultiPlayerGamingSDK/events/RoomData;Ljava/lang/String;)((@de.SimonIT.App42MultiPlayerGamingSDK.events.RoomData::new(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)(room.getRoomId(), room.getOwner(), room.getName(), room.getMaxUsers())), user);
        });
        this.setNotifyListener($wnd.AppWarp.Events.onUserJoinedRoom, function (room, user) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.NotifyListener::onUserJoinedRoom(Lde/SimonIT/App42MultiPlayerGamingSDK/events/RoomData;Ljava/lang/String;)((@de.SimonIT.App42MultiPlayerGamingSDK.events.RoomData::new(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)(room.getRoomId(), room.getOwner(), room.getName(), room.getMaxUsers())), user);
        });
        this.setNotifyListener($wnd.AppWarp.Events.onUserLeftLobby, function (lobby, user) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.NotifyListener::onUserLeftLobby(Lde/SimonIT/App42MultiPlayerGamingSDK/events/LobbyData;Ljava/lang/String;)((@de.SimonIT.App42MultiPlayerGamingSDK.events.LobbyData::new(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;IZ)(lobby.getRoomId(), lobby.getOwner(), lobby.getName(), lobby.getMaxUsers(), lobby.getIsPrimary())), user);
        });
        this.setNotifyListener($wnd.AppWarp.Events.onUserJoinedLobby, function (lobby, user) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.NotifyListener::onUserJoinedLobby(Lde/SimonIT/App42MultiPlayerGamingSDK/events/LobbyData;Ljava/lang/String;)((@de.SimonIT.App42MultiPlayerGamingSDK.events.LobbyData::new(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;IZ)(lobby.getRoomId(), lobby.getOwner(), lobby.getName(), lobby.getMaxUsers(), lobby.getIsPrimary())), user);
        });
        this.setNotifyListener($wnd.AppWarp.Events.onChatReceived, function (chat) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.NotifyListener::onChatReceived(Lde/SimonIT/App42MultiPlayerGamingSDK/events/ChatEvent;)(@de.SimonIT.App42MultiPlayerGamingSDK.events.ChatEvent::new(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)(chat.getChat(), chat.getSender(), chat.getLocId(), chat.getIsLocationLobby()));
        });
        this.setNotifyListener($wnd.AppWarp.Events.onPrivateChatReceived, function (sender, chat) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.NotifyListener::onPrivateChatReceived(Ljava/lang/String;Ljava/lang/String;)(sender, chat);
        });
        this.setNotifyListener($wnd.AppWarp.Events.onPrivateUpdateReceived, function (userName, msg) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.NotifyListener::onPrivateUpdateReceived(Ljava/lang/String;[BZ)(userName, msg, true); // I don't know whether true is a good idea
        });
        this.setNotifyListener($wnd.AppWarp.Events.onUpdatePeersReceived, function (payload) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.NotifyListener::onUpdatePeersReceived(Lde/SimonIT/App42MultiPlayerGamingSDK/events/UpdateEvent;)(@de.SimonIT.App42MultiPlayerGamingSDK.events.UpdateEvent::new([BZ)(@de.SimonIT.App42MultiPlayerGamingSDK.util.JsUtil::toJavaByteArray(Lcom/google/gwt/typedarrays/client/Uint8ArrayNative;)(payload), true)); // I don't know whether true is a good idea
        });
        this.setNotifyListener($wnd.AppWarp.Events.onUserChangeRoomProperty, function (sender, properties, lockProperties) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.NotifyListener::onUserChangeRoomProperty(Lde/SimonIT/App42MultiPlayerGamingSDK/events/RoomData;Ljava/lang/String;Ljava/util/Map;Ljava/util/Map;)(null, sender, @de.SimonIT.App42MultiPlayerGamingSDK.util.JsUtil::toJavaMap(*)(properties), @de.SimonIT.App42MultiPlayerGamingSDK.util.JsUtil::toJavaMap(*)(lockProperties));  // It seems that no room data is provided
        });
        this.setNotifyListener($wnd.AppWarp.Events.onMoveCompleted, function (move) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.NotifyListener::onMoveCompleted(Lde/SimonIT/App42MultiPlayerGamingSDK/events/MoveEvent;)(@de.SimonIT.App42MultiPlayerGamingSDK.events.MoveEvent::new(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(move.getSender(), move.getMoveData(), move.getNextTurn(), move.getRoomId()));
        });
        this.setNotifyListener($wnd.AppWarp.Events.onGameStarted, function (sender, id, nextTurn) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.NotifyListener::onGameStarted(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(sender, id, nextTurn);
        });
        this.setNotifyListener($wnd.AppWarp.Events.onGameStopped, function (sender, id) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.NotifyListener::onGameStopped(Ljava/lang/String;Ljava/lang/String;)(sender, id);
        });
        this.setNotifyListener($wnd.AppWarp.Events.onUserPaused, function (id, isLobby, user) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.NotifyListener::onUserPaused(Ljava/lang/String;ZLjava/lang/String;)(id, isLobby, user);
        });
        this.setNotifyListener($wnd.AppWarp.Events.onUserResumed, function (id, isLobby, user) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.NotifyListener::onUserResumed(Ljava/lang/String;ZLjava/lang/String;)(id, isLobby, user);
        });
        this.setNotifyListener($wnd.AppWarp.Events.onNextTurnRequested, function (lastTurn) {
            listener.@de.SimonIT.App42MultiPlayerGamingSDK.listener.NotifyListener::onNextTurnRequest(Ljava/lang/String;)(lastTurn);
        });
    }-*/;

	/**
	 * Removes the given listener object from the list of objects on which callbacks
	 * will be invoked when a notification is received from the server from any
	 * subscribed location (room or lobby). The object must implement the
	 * NotifyListener interface.
	 *
	 * @param listener listener object
	 */
	public final native void removeNotificationListener(NotifyListener listener) /*-{
        this.setNotifyListener($wnd.AppWarp.Events.onRoomCreated, undefined);
        this.setNotifyListener($wnd.AppWarp.Events.onRoomDestroyed, undefined);
        this.setNotifyListener($wnd.AppWarp.Events.onUserLeftRoom, undefined);
        this.setNotifyListener($wnd.AppWarp.Events.onUserJoinedRoom, undefined);
        this.setNotifyListener($wnd.AppWarp.Events.onUserLeftLobby, undefined);
        this.setNotifyListener($wnd.AppWarp.Events.onUserJoinedLobby, undefined);
        this.setNotifyListener($wnd.AppWarp.Events.onChatReceived, undefined);
        this.setNotifyListener($wnd.AppWarp.Events.onPrivateChatReceived, undefined);
        this.setNotifyListener($wnd.AppWarp.Events.onPrivateUpdateReceived, undefined);
        this.setNotifyListener($wnd.AppWarp.Events.onUpdatePeersReceived, undefined);
        this.setNotifyListener($wnd.AppWarp.Events.onUserChangeRoomProperty, undefined);
        this.setNotifyListener($wnd.AppWarp.Events.onMoveCompleted, undefined);
        this.setNotifyListener($wnd.AppWarp.Events.onGameStarted, undefined);
        this.setNotifyListener($wnd.AppWarp.Events.onGameStopped, undefined);
        this.setNotifyListener($wnd.AppWarp.Events.onUserPaused, undefined);
        this.setNotifyListener($wnd.AppWarp.Events.onUserResumed, undefined);
        this.setNotifyListener($wnd.AppWarp.Events.onNextTurnRequested, undefined);
    }-*/;

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
	public final native void connectWithUserName(String userName) /*-{
        this.connect(userName);
    }-*/;

	/**
	 * Disconnects the connection with the AppWarp server. The result for this
	 * request will be provided in the onDisConnectDone callback of the
	 * ConnectionRequestListener.
	 */
	public final native void disconnect() /*-{
        this.disconnect();
    }-*/;

	/**
	 * Returns the current connection state of the WarpClient instance.
	 *
	 * @return int
	 */
	public final native int getConnectionState() /*-{
        return this.getConnectionState();
    }-*/;

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
	public final native void RecoverConnection() /*-{
        this.recoverConnection();
    }-*/;

	/**
	 * It gives sessionId of the current established connection,otherwise returns
	 * zero.
	 *
	 * @return sessionId
	 */
	public final native int getSessionID() /*-{
        return this.getSessionID();
    }-*/;

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
	public final native void RecoverConnectionWithSessionID(int session_id, String user_name) /*-{
        this.recoverConnectionWithSessionID(session_id, user_name);
    }-*/;

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
	public final native void setCustomRoomData(String roomid, String data) /*-{
        this.setCustomRoomData(roomid, data);
    }-*/;

	/**
	 * Updates the custom data associated with the given user on the server (if the
	 * given user is online). Result is provided in the onSetCustomUserDataDone
	 * callback of the registered ZoneRequestListener objects. It can be useful in
	 * setting status messages or avatar url’s etc for online users.
	 *
	 * @param username user for whom custom data has to be update
	 * @param data     custom data that will be set for the user
	 */
	public final native void setCustomUserData(String username, String data) /*-{
        this.setCustomUserData(username, data);
    }-*/;

	/**
	 * Retrieves usernames of all the users connected (online) to the server. Result
	 * is provided in the onGetOnlineUsers callback of the registered
	 * ZoneRequestListener objects.
	 */
	public final native void getOnlineUsers() /*-{
        this.getOnlineUsers();
    }-*/;

	/**
	 * Retrieves total number of rooms on the server. Result is provided in the
	 * onGetAllRoomsCountDone callback of the registered ZoneRequestListener
	 * objects.
	 */
	public final native void getAllRoomsCount() /*-{
        this.getAllRoomsCount();
    }-*/;

	/**
	 * Retrieves total number of users connected (online) to the server. Result is
	 * provided in the onGetOnlineUsersCountDone callback of the registered
	 * ZoneRequestListener objects.
	 */
	public final native void getOnlineUsersCount() /*-{
        this.getOnlineUsersCount();
    }-*/;

	/**
	 * Retrieves the room ids of all the rooms on the server. Result is provided in
	 * the onGetAllRoomsDone callback of the registered ZoneRequestListener objects.
	 * To get a filtered list of rooms, use the GetRoomWithProperties API.
	 */
	public final native void getAllRooms() /*-{
        this.getAllRooms();
    }-*/;

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
	public final native void getLiveUserInfo(String username) /*-{
        this.getLiveUserInfo(username);
    }-*/;

	/**
	 * Retrieves status of user via username on the server. Result is provided in
	 * the onGetUserStatusDone callback of the registered ZoneRequestListener
	 * objects. This is useful to find either user is connected or not.
	 *
	 * @param username Username of the player
	 */
	public final native void getUserStatus(String username) /*-{
        this.getUserStatus(username);
    }-*/;

	/**
	 * Retrieves the live information of the given room from the server. Result is
	 * provided in the onGetLiveRoomInfoDone callback of the registered
	 * RoomRequestListener objects. The information includes the names of the
	 * currently joined users, the rooms properties and any associated customData.
	 * This is useful in getting a snapshot of a rooms state.
	 *
	 * @param roomid Id of the room
	 */
	public final native void getLiveRoomInfo(String roomid) /*-{
        this.getLiveRoomInfo(roomid);
    }-*/;

	/**
	 * Retrieves live information of the lobby from the server. Result is provided
	 * in the onGetLiveLobbyInfo callback of the registered LobbyRequestListener
	 * objects. The information returned includes the names of the users who are
	 * currently joined in the lobby.
	 */
	public final native void getLiveLobbyInfo() /*-{
        this.getLiveLobbyInfo();
    }-*/;

	/**
	 * Sends a join lobby request to the server. Result of the request is provided
	 * in the onJoinLobbyDone callback of the registered LobbyRequestListener
	 * objects.
	 */
	public final native void joinLobby() /*-{
        this.joinLobby();
    }-*/;

	/**
	 * Sends a leave lobby request to the server. Result of the request is provided
	 * in the onLeaveLobbyDone callback of the registered LobbyRequestListener
	 * objects.
	 */
	public final native void leaveLobby() /*-{
        this.leaveLobby();
    }-*/;

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
	public final native void subscribeLobby() /*-{
        this.subscribeLobby();
    }-*/;

	/**
	 * Sends a unsubscribe lobby request to the server. Result of the request is
	 * provided in the onUnsubscribeLobbyDone callback of the LobbyRequestListener.
	 */
	public final native void unsubscribeLobby() /*-{
        this.unsubscribeLobby();
    }-*/;

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
	public final native void createRoom(String name, String owner, int maxUsers, HashMap<String, Object> tableProperties) /*-{
        this.createRoom(name, owner, maxUsers, @de.SimonIT.App42MultiPlayerGamingSDK.util.JsUtil::toJsMap(*)(tableProperties));
    }-*/;

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
	public final native void createTurnRoom(String name, String owner, int maxUsers, HashMap<String, Object> tableProperties, int time) /*-{
        this.createTurnRoom(name, owner, maxUsers, @de.SimonIT.App42MultiPlayerGamingSDK.util.JsUtil::toJsMap(*)(tableProperties), time);
    }-*/;

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
	public final native void sendMove(String moveData) /*-{
        this.sendMove(moveData);
    }-*/;

	public final native void sendMove(String moveData, String nextTurn) /*-{
        this.sendMove(moveData, nextTurn);
    }-*/;

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
	public final native void setNextTurn(String nextTurn) /*-{
        this.setNextTurn(nextTurn);
    }-*/;

	/**
	 * Sends a start game to server in TurnBasedRoom. Result of this callback is
	 * provided in onStartGameDone of registered TurnBasedRoomListener interface. If
	 * successful game will be started in TurnBasedRoom.
	 */
	public final native void startGame() /*-{
        this.startGame();
    }-*/;

	/**
	 * Sends a start game to server in TurnBasedRoom. Result of this callback is
	 * provided in onStartGameDone of registered TurnBasedRoomListener interface. If
	 * successful game will be started in TurnBasedRoom.
	 *
	 * @param isDefaultLogic a bool variable.
	 */
	public final native void startGame(boolean isDefaultLogic) /*-{
        this.startGame(isDefaultLogic);
    }-*/;

	/**
	 * Sends a start game to server in TurnBasedRoom. Result of this callback is
	 * provided in onStartGameDone of registered TurnBasedRoomListener interface. If
	 * successful game will be started in TurnBasedRoom.
	 *
	 * @param isDefaultLogic a bool variable.
	 * @param nextTurn       the next turn string value.
	 */
	public final native void startGame(boolean isDefaultLogic, String nextTurn) /*-{
        this.startGame(isDefaultLogic, nextTurn);
    }-*/;

	/**
	 * Sends a stop game to server in TurnBasedRoom. Result of this callback is
	 * provided in onStopGameDone of registered TurnBasedRoomListener interface. If
	 * successful game will be stopped in TurnBasedRoom.
	 */
	public final native void stopGame() /*-{
        this.stopGame();
    }-*/;

	/**
	 * Sends get move history request to server in TurnBasedRoom. Result of this
	 * callback is provided in onGetMoveHistoryDone of registered
	 * TurnBasedRoomListener interface.
	 */
	public final native void getMoveHistory() /*-{
        this.getMoveHistory();
    }-*/;

	/**
	 * Sends a delete room request to the server. Result of the request is provided
	 * in the onDeleteRoomDone callback of the registered ZoneRequestListener
	 * objects. Only dynamic rooms can be deleted through this API. Static rooms
	 * (created from AppHQ) can not be deleted through this. Read more about Rooms
	 * here.
	 *
	 * @param roomId Id of the room to be deleted
	 */
	public final native void deleteRoom(String roomId) /*-{
        this.deleteRoom(roomId);
    }-*/;

	/**
	 * Sends a join room request to the server. Result of the request is provided in
	 * the onJoinRoomDone callback of the registered RoomRequestListener objects. A
	 * user can only be joined in one location (room or lobby) at a time. If a user
	 * joins a room, it will automatically be removed from its current location.
	 *
	 * @param roomId Id of the room to be joined
	 */
	public final native void joinRoom(String roomId) /*-{
        this.joinRoom(roomId);
    }-*/;

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
	public final native void joinAndSubscribeRoom(String roomId) /*-{
        this.joinAndSubscribeRoom(roomId);
    }-*/;

	/**
	 * Sends a leave and unsubscribe room request to the server. Result of the
	 * request is provided in the onLeaveAndUnsubscribeRoomDone callback of the
	 * registered RoomRequestListener objects.
	 *
	 * @param roomId Id of the room to be subscribed
	 */
	public final native void leaveAndUnsubscribeRoom(String roomId) /*-{
        this.leaveAndUnsubscribeRoom(roomId);
    }-*/;

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
	public final native void joinRoomInRange(int minUser, int maxUser, boolean maxPreferred) /*-{
        this.joinRoomInRange(minUser, maxUser, maxPreferred);
    }-*/;

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
	public final native void updateRoomProperties(String roomID, HashMap<String, Object> tableProperties, String[] removeArray) /*-{
        this.updateRoomProperties(roomID, @de.SimonIT.App42MultiPlayerGamingSDK.util.JsUtil::toJsMap(*)(tableProperties), @com.google.gwt.core.client.JsArrayUtils::readOnlyJsArray([Lcom/google/gwt/core/client/JavaScriptObject;)(removeArray));
    }-*/;

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
	public final native void lockProperties(HashMap<String, Object> tableProperties) /*-{
        this.lockProperties(@de.SimonIT.App42MultiPlayerGamingSDK.util.JsUtil::toJsMap(*)(tableProperties));
    }-*/;

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
	public final native void unlockProperties(String[] unlockProperties) /*-{
        this.unlockProperties(@com.google.gwt.core.client.JsArrayUtils::readOnlyJsArray([Lcom/google/gwt/core/client/JavaScriptObject;)(unlockProperties));
    }-*/;

	/**
	 * Sends a join room request to the server with the condition that the room must
	 * have a matching set of property value pairs associated with it. This is
	 * useful in match making. Result of the request is provided in the
	 * onJoinRoomDone callback of the registered RoomRequestListener.
	 *
	 * @param tableProperties properties of the room to be joined
	 */
	public final native void joinRoomWithProperties(HashMap<String, Object> tableProperties) /*-{
        this.joinRoomWithProperties(@de.SimonIT.App42MultiPlayerGamingSDK.util.JsUtil::toJsMap(*)(tableProperties));
    }-*/;

	/**
	 * Retrieves information of the rooms that contain at least minUsers and at most
	 * maxUsers in them. Result is provided in the onGetMatchedRoomsDone callback of
	 * the registered ZoneRequestListener objects. This is useful in building a
	 * filtered list of rooms.
	 *
	 * @param minUser  number of minimum users in room to be joined
	 * @param maxUsers number of maximum users in room to be joined
	 */
	public final native void getRoomsInRange(int minUser, int maxUsers) /*-{
        this.getRoomsInRange(minUser, maxUsers);
    }-*/;

	/**
	 * Retrieves information of the room that contain properties which match with
	 * the given properties. Result is provided in the onGetMatchedRoomsDone
	 * callback of the registered ZoneRequestListener objects. This is useful in
	 * building a filtered list of rooms.
	 *
	 * @param properties properties of the room to be joined
	 */
	public final native void getRoomsWithProperties(HashMap<String, Object> properties) /*-{
        this.getRoomsWithProperties(@de.SimonIT.App42MultiPlayerGamingSDK.util.JsUtil::toJsMap(*)(properties));
    }-*/;

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
	public final native void getRoomInRangeWithProperties(int minUser, int maxUsers, HashMap<String, Object> properties) /*-{
        this.getRoomInRangeWithProp(minUser, maxUsers, @de.SimonIT.App42MultiPlayerGamingSDK.util.JsUtil::toJsMap(*)(properties));
    }-*/;

	/**
	 * Sends a leave room request to the server. Result of the request is provided
	 * in the onLeaveRoomDone callback of the registered RoomRequestListener.
	 *
	 * @param roomId Id of the room to be left
	 */
	public final native void leaveRoom(String roomId) /*-{
        this.leaveRoom(roomId);
    }-*/;

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
	public final native void subscribeRoom(String roomId) /*-{
        this.subscribeRoom(roomId);
    }-*/;

	/**
	 * Sends a unsubscribe room request to the server. Result of the request is
	 * provided in the onUnSubscribeRoomDone callback of the registered
	 * RoomRequestListener objects.
	 *
	 * @param roomId Id of the room to be subscribed
	 */
	public final native void unsubscribeRoom(String roomId) /*-{
        this.unsubscribeRoom(roomId);
    }-*/;

	/**
	 * Sends a chat message to the room (or lobby) in which the user is currently
	 * joined. Result of the request is provided in the onSendChatDone callback of
	 * the registered ChatRequestListener objects. All users who are subscribed to
	 * the location in which the message is sent will get a onChatReceived event on
	 * their registered NotifyListener objects.
	 *
	 * @param message message to be sent
	 */
	public final native void sendChat(String message) /*-{
        this.sendChat(message);
    }-*/;

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
	public final void sendUDPPrivateUpdate(String toUsername, byte[] update) {
		sendPrivateUpdate(toUsername, update);
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
	public final native void sendPrivateUpdate(String toUsername, byte[] update) /*-{
        this.sendPrivateUpdatePeers(toUsername, @com.google.gwt.core.client.JsArrayUtils::readOnlyJsArray([B)(update));
    }-*/;

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
	public final native void sendPrivateChat(String userName, String message) /*-{
        this.sendPrivateChat(userName, message);
    }-*/;

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
	 * @see GwtWarpClient#sendUpdatePeers(byte[])
	 */
	public final void sendUDPUpdatePeers(byte[] update) {
		sendUpdatePeers(update);
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
	public final native void initUDP() /*-{
        if (this.responseCallbacks["onInitUDPDone"]) {
            this.responseCallbacks["onInitUDPDone"](4);
        }
    }-*/;

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
	public final native void sendUpdatePeers(byte[] update) /*-{
        this.sendUpdatePeers(@com.google.gwt.core.client.JsArrayUtils::readOnlyJsArray([B)(update));
    }-*/;

	public final void onLookUpServer(byte lookUpaStatus) {
	}

	public final void getGameStatus() {
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
	public final native void sendChat(String message, boolean saveHistory, String roomId) /*-{
        this.sendChat(message, saveHistory, roomId);
    }-*/;

	/**
	 * Set a db name where chat history need to be saved.
	 *
	 * @param dbName Database name that will be set for the chat history.
	 */
	public final native void setDbName(String dbName) /*-{
        this.setDbName(dbName);
    }-*/;

	/**
	 * Enables client to save the chat sent using sendChat API.
	 *
	 * @param status If true, the client can save chat using sendChat API.
	 */
	public final native void enableChatHistory(boolean status) /*-{
        this.enableChatHistory(status);
    }-*/;

	/**
	 * Retrieves chat history of a room of specified roomId from the server. Result
	 * is provided in the onGetChatHistoryDone callback of the registered
	 * ChatRequestListener objects.
	 *
	 * @param roomId Id of the room whose history is requested.
	 * @param max    Number of messages to be fetched from the server.
	 * @param offset index from where the messages need to be fetched.
	 */
	public final native void getChatHistory(String roomId, int max, int offset) /*-{
        this.getChatHistory(roomId, max, offset);
    }-*/;
}
