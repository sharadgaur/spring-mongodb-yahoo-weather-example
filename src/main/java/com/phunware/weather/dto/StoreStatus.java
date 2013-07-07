package com.phunware.weather.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class StoreStatus extends Message {
	private static final long serialVersionUID = 1L;
	private long rows;
	private boolean progress;
	private String statusMsg;
}
