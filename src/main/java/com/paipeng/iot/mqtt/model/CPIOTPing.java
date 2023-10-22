package com.paipeng.iot.mqtt.model;

public class CPIOTPing extends CPIOTBase {
    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return super.toString() + " state: " + state;
    }
}
