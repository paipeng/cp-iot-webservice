package com.paipeng.iot.mqtt.model;

public abstract class CPIOTBase {
    protected String udid;
    protected String serverName;

    private int state;

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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return super.toString() + " udid: " + udid + " serverName: " + serverName  + " state: " + state;
    }
}
