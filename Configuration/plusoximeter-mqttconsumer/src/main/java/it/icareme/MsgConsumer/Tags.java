package it.icareme.MsgConsumer;

import com.google.gson.annotations.SerializedName;

public class Tags {
	@SerializedName("deviceId")
	private String deviceId;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
}