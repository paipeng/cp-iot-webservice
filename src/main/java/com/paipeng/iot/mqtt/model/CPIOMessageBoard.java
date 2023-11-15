package com.paipeng.iot.mqtt.model;

import lombok.Getter;

public class CPIOMessageBoard extends CPIOTBase {

    private String message;
    private String textPixelBase64;
    private String textPixel2Base64;
    private int textCount;

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getTextPixelBase64() {
        return textPixelBase64;
    }

    public void setTextPixelBase64(String textPixelBase64) {
        this.textPixelBase64 = textPixelBase64;
    }

    public String getTextPixel2Base64() {
        return textPixel2Base64;
    }

    public void setTextPixel2Base64(String textPixel2Base64) {
        this.textPixel2Base64 = textPixel2Base64;
    }


    public int getTextCount() {
        return textCount;
    }

    public void setTextCount(int textCount) {
        this.textCount = textCount;
    }
    @Override
    public String toString() {
        return super.toString() + " message: " + message;
    }
}
