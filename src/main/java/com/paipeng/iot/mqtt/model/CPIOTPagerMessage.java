package com.paipeng.iot.mqtt.model;

public class CPIOTPagerMessage extends CPIOMessageBoard {
    private String sender;
    private String uuid;
    private String receiver;

    private String textPixelBase64;

    private int textCount;

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

    public String getTextPixelBase64() {
        return textPixelBase64;
    }

    public void setTextPixelBase64(String textPixelBase64) {
        this.textPixelBase64 = textPixelBase64;
    }

    public int getTextCount() {
        return textCount;
    }

    public void setTextCount(int textCount) {
        this.textCount = textCount;
    }
}
