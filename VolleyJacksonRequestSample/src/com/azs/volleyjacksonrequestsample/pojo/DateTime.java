package com.azs.volleyjacksonrequestsample.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DateTime {
	private String time;
	private long millis_since_epoch;
	private String date;

	@JsonProperty("milliseconds_since_epoch")
	public void setMillisSinceEpoch(long millis_since_epoch) {
		this.millis_since_epoch = millis_since_epoch;
	}

	@JsonProperty("time")
	public void setTime(String time) {
		this.time = time;
	}

	@JsonProperty("date")
	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}
	
	public long getMillisSinceEpoch() {
		return millis_since_epoch;
	}
	
	public String getDate() {
		return date;
	}
}
