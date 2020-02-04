package de.SimonIT.App42MultiPlayerGamingSDK.events;

public class MoveEvent {
	private String sender;
	private String moveData;
	private String nextTurn;
	private String roomId;

	public MoveEvent() {
	}

	public MoveEvent(String sender, String moveData, String nextTurn, String roomId) {
		this.sender = sender;
		this.moveData = moveData;
		this.nextTurn = nextTurn;
		this.roomId = roomId;
	}

	public String getSender() {
		return this.sender;
	}

	public String getMoveData() {
		return this.moveData;
	}

	public String getNextTurn() {
		return this.nextTurn;
	}

	public String getRoomId() {
		return this.roomId;
	}
}
