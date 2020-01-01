package com.shephertz.app42.gaming.multiplayer.client.events;

public class AllUsersEvent {
	private byte result;
	private String[] userNames;
	private int count;

	public AllUsersEvent(byte result, String[] users) {
		this.userNames = users;
		this.result = result;
	}

	AllUsersEvent(int userCount, byte resultCode) {
		this.count = userCount;
		this.result = resultCode;
	}

	public byte getResult() {
		return this.result;
	}

	public String[] getUserNames() {
		return this.userNames;
	}

	public int getUsersCount() {
		return this.count;
	}
}
