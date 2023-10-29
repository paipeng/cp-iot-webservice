package com.paipeng.iot.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HexFormat;

public class ImageUtil {
    public static BufferedImage crop(BufferedImage bufferedImage, Rectangle rectangle) {
        try {
            return bufferedImage.getSubimage(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        } catch (Exception e) {
            return null;
        }
    }


    public static byte[] convert1BitByteArray(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        byte[] data = new byte[width * height / 8];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = bufferedImage.getRGB(j, i);
                int bit = (pixel & 0xFF)/255;
                System.out.print( bit+ " ");
                data[(i*width+j)/8] += (byte)((bit & 0x1) << ((i*width+j)%8));
            }
            System.out.println(" ");
        }

        System.out.println(HexFormat.of().formatHex(data));
        return data;
    }

    public static void print1Bit(byte[] data) {
        System.out.println(HexFormat.of().formatHex(data));
        int width = (int)Math.sqrt(data.length * 8);
        for (int i = 0; i < data.length; i++) {
            if (i % (width/8) == 0) {
                System.out.println(" ");
            }
            for (int j = 0; j < 8; j++) {
                System.out.print(((data[i] >> j) & 0x1) + " ");
            }
        }
        System.out.println(" ");
    }
}
