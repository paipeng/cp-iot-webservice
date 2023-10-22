package com.paipeng.iot.mqtt.model;

public abstract class CPIOTBase {
    protected String deviceUuid;
    protected String serverName;

    public String getDeviceUuid() {
        return deviceUuid;
    }

    public void setDeviceUuid(String deviceUuid) {
        this.deviceUuid = deviceUuid;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public String toString() {
        return super.toString() + " deviceUuid: " + deviceUuid + " serverName: " + serverName;
    }
}
