package com.paipeng.iot.mqtt.model;

public class CPIOTTemperature extends CPIOTBase {
    private float value;
    private float humidity;

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }


    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    @Override
    public String toString() {
        return super.toString() + " value: " + value + " humidity: " + humidity;
    }
}
