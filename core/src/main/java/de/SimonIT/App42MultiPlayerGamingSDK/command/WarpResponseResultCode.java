package de.SimonIT.App42MultiPlayerGamingSDK.command;

public interface WarpResponseResultCode {
	byte SUCCESS = 0;
	byte AUTH_ERROR = 1;
	byte RESOURCE_NOT_FOUND = 2;
	byte RESOURCE_MOVED = 3;
	byte BAD_REQUEST = 4;
	byte CONNECTION_ERROR = 5;
	byte UNKNOWN_ERROR = 6;
	byte RESULT_SIZE_ERROR = 7;
	byte SUCCESS_RECOVERED = 8;
	byte CONNECTION_ERROR_RECOVERABLE = 9;
	byte USER_PAUSED_ERROR = 10;
	byte AUTO_RECOVERING = 11;
}
