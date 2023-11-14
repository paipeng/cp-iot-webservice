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
        String text = "你好-鹏龘,";
        text = "你";
        byte[] data = Font2ImageUtil.text2Pixel(text, 26, 0);
        Assertions.assertNotNull(data);
        System.out.println("byte data size: " + data.length);

        //data = Font2ImageUtil.decompress(data);

        System.out.println("byte data size: " + data.length);
        System.out.println("font count: " + data.length/(72));
        ImageUtil.print1Bit(data);
    }

    @Test
    void text2PixelParolaDataFormat() throws IOException, FontFormatException {
        String text = "你好-鹏龘,";
        text = "吕";
        byte[] data = Font2ImageUtil.text2Pixel(text, 16+2, 1);
        Assertions.assertNotNull(data);
        System.out.println("byte data size: " + data.length);

        //data = Font2ImageUtil.decompress(data);

        System.out.println("byte data size: " + data.length);
        System.out.println("font count: " + data.length/(72));
        ImageUtil.print1Bit(data);
    }
}
