package com.paipeng.iot.entity;


import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.NUMBER)
public enum RecordType {
    NONE,
    TEMPERATURE,
    PHOTOSENSITIVE,
    MESSAGE_BOARD,
    VOICE,
    DEVICE_STATE,
    PING,
    LED

}
