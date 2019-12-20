package com.shephertz.app42.gaming.api.client;

public class App42Response {
	private boolean isResponseSuccess;
	private String strResponse;
	private int totalRecords = -1;

	public int getTotalRecords() {
		return this.totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public String getStrResponse() {
		return this.strResponse;
	}

	public void setStrResponse(String strResponse) {
		this.strResponse = strResponse;
	}

	public boolean isResponseSuccess() {
		return this.isResponseSuccess;
	}

	public void setResponseSuccess(boolean isResponseSuccess) {
		this.isResponseSuccess = isResponseSuccess;
	}

	public String toString() {
		return this.strResponse;
	}
}
