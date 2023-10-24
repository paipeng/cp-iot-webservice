package com.paipeng.iot.mqtt.model;

public class CPIOTTemperature extends CPIOTBase {
    private float value;

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return super.toString() + " value: " + value;
    }
}
