package com.paipeng.iot.mqtt.model;

public abstract class CPIOTBase {
    protected String udid;
    protected String serverName;

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public String toString() {
        return super.toString() + " deviceUuid: " + udid + " serverName: " + serverName;
    }
}
