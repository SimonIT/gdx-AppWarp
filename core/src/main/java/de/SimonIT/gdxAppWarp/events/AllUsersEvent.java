package de.SimonIT.gdxAppWarp.events;

public class AllUsersEvent {
	private final byte result;
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

	public AllUsersEvent(byte result, String[] users, int userCount) {
		this.userNames = users;
		this.result = result;
		this.count = userCount;
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
