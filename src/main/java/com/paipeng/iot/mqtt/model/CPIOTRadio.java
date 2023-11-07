package com.paipeng.iot.mqtt.model;

import com.paipeng.iot.entity.Radio;

import java.util.List;

public class CPIOTRadio extends CPIOTBase{
    private List<Radio> radios;

    private int event;

    private long playRadioId;


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

    public long getPlayRadioId() {
        return playRadioId;
    }

    public void setPlayRadioId(long playRadioId) {
        this.playRadioId = playRadioId;
    }
}
