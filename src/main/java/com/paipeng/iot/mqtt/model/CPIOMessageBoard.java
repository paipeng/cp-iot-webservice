package com.paipeng.iot.mqtt.model;

public class CPIOMessageBoard extends CPIOTBase {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    @Override
    public String toString() {
        return super.toString() + " message: " + message;
    }
}
