package com.paipeng.iot.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;

public class Font2ImageUtilTest {
    @Test
    void text2Image() throws IOException, FontFormatException {
        String text = "你好鹏龘";
        byte[] data = Font2ImageUtil.text2Image(text);
        Assertions.assertNotNull(data);
    }

    @Test
    void text2Pixel() throws IOException, FontFormatException {
        String text = "你好鹏龘";
        byte[] data = Font2ImageUtil.text2Pixel(text, 26);
        Assertions.assertNotNull(data);
    }
}
