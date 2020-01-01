package com.shephertz.app42.gaming.multiplayer.client.events;

public class LiveUserInfoEvent {
	private byte result;
	private String locationId;
	private String name;
	private String customData;
	private boolean isLocationLobby;
	private boolean isPaused;
	private boolean isActive;

	public LiveUserInfoEvent(byte result, String locId, String name, String custom, boolean fromLobby,
							 boolean isPaused) {
		this.result = result;
		this.locationId = locId;
		this.name = name;
		this.customData = custom;
		this.isLocationLobby = fromLobby;
		this.isPaused = isPaused;
	}

	public LiveUserInfoEvent(byte result, String name, boolean isActive) {
		this.result = result;
		this.name = name;
		this.isActive = isActive;
	}

	public byte getResult() {
		return this.result;
	}

	public boolean isLocationLobby() {
		return this.isLocationLobby;
	}

	public String getLocationId() {
		return this.locationId;
	}

	public String getName() {
		return this.name;
	}

	public String getCustomData() {
		return this.customData;
	}

	public boolean isPaused() {
		return this.isPaused;
	}

	public boolean isActive() {
		return this.isActive;
	}
}
