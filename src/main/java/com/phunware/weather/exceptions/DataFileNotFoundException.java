package com.phunware.weather.exceptions;

public class DataFileNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public DataFileNotFoundException() {

	}

	public DataFileNotFoundException(String msg) {
		super(msg);
	}

}
