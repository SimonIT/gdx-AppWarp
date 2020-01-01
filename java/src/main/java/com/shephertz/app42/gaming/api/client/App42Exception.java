package com.shephertz.app42.gaming.api.client;

public class App42Exception extends RuntimeException {
	private int httpErrorCode;
	private int appErrorCode;

	public App42Exception() {
	}

	public App42Exception(String detailMessage) {
		super(detailMessage);
	}

	public App42Exception(Throwable throwable) {
		super(throwable);
	}

	public App42Exception(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public App42Exception(String detailMessage, int httpErrorCode, int appErrorCode) {
		super(detailMessage);
		this.httpErrorCode = httpErrorCode;
		this.appErrorCode = appErrorCode;
	}

	public App42Exception(int httpErrorCode, int appErrorCode) {
		this.httpErrorCode = httpErrorCode;
		this.appErrorCode = appErrorCode;
	}

	public void setHttpErrorCode(int httpErrorCode) {
		this.httpErrorCode = httpErrorCode;
	}

	public int getHttpErrorCode() {
		return this.httpErrorCode;
	}

	public void setAppErrorCode(int appErrorCode) {
		this.appErrorCode = appErrorCode;
	}

	public int getAppErrorCode() {
		return this.appErrorCode;
	}
}
