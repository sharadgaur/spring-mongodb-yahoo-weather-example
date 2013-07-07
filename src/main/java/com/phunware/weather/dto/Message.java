package com.phunware.weather.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	private int code;
	private String message;
}
