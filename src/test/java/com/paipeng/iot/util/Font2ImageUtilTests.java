package com.paipeng.iot.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;

public class Font2ImageUtilTests {
    @Test
    void text2Image() throws IOException, FontFormatException {
        String text = "你好鹏龘";
        byte[] data = Font2ImageUtil.text2Image(text);
        Assertions.assertNotNull(data);
    }
}
