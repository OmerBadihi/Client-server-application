package com.srccodes.examples;

public class PrimaryKey {
	private int primaryKey;
	
	public PrimaryKey(String primaryKey) {
		this.primaryKey = Integer.valueOf(primaryKey);
	}

	public PrimaryKey() {
		// TODO Auto-generated constructor stub
	}

	public int getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
	}
}
