package de.SimonIT.gdxAppWarp.listener;

import de.SimonIT.gdxAppWarp.events.MoveEvent;

public interface TurnBasedRoomListener {
	void onSendMoveDone(byte var1);

	void onStartGameDone(byte var1);

	void onStopGameDone(byte var1);

	void onGetMoveHistoryDone(byte var1, MoveEvent[] var2);

	void onSetNextTurnDone(byte var1);

	void onGetGameStatusDone(byte var1, boolean var2);
}
