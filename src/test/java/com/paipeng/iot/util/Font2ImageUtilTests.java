package com.paipeng.iot.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Font2ImageUtilTests {
    @Test
    void text2Image() {
        String text = "你好";
        byte[] data = Font2ImageUtil.text2Image(text);
        Assertions.assertNotNull(data);
    }
}
