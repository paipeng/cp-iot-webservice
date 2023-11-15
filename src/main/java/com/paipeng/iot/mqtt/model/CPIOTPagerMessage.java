package com.paipeng.iot.mqtt.model;

public class CPIOTPagerMessage extends CPIOMessageBoard {
    private String sender;
    private String uuid;
    private String receiver;



    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
