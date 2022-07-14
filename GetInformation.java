package com.srccodes.examples;

public class GetInformation {
	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	private String primaryKey;
	private String currency;
	
	public GetInformation(String primaryKey, String currency) {
		this.primaryKey = primaryKey;
		this.currency = currency;
	}

	public GetInformation() {
		// TODO Auto-generated constructor stub
	}

}
