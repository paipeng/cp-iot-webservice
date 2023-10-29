package com.paipeng.iot.util;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtil {
    public static BufferedImage crop(BufferedImage bufferedImage, Rectangle rectangle) {
        try {
            return bufferedImage.getSubimage(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        } catch (Exception e) {
            return null;
        }
    }
}
