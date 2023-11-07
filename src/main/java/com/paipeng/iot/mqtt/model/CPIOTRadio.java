package com.paipeng.iot.mqtt.model;

import com.paipeng.iot.entity.Radio;

import java.util.List;

public class CPIOTRadio extends CPIOTBase{
    private List<Radio> radios;

    private int event;


    public List<Radio> getRadios() {
        return radios;
    }

    public void setRadios(List<Radio> radios) {
        this.radios = radios;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }
}
