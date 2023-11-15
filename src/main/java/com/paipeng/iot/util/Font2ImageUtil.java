package com.paipeng.iot.util;


import org.apache.commons.lang3.ArrayUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

public class Font2ImageUtil {
    private static final String fontName = "fonts/Alibaba-PuHuiTi-Regular.ttf";
    private static final String heiTiFontName = "fonts/FangZhengHeiTiJianTi.ttf";

    public static byte[] text2Image(String text) throws IOException, FontFormatException {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        //Font font = new Font("Arial", Font.PLAIN, 48);

        //String fontPath = Font2ImageUtil.class.getClassLoader().getResource(fontName).getPath();
        InputStream is = Font2ImageUtil.class.getClassLoader().getResourceAsStream(fontName);
        Font font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(24f);

        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(text);
        int height = fm.getHeight();
        g2d.dispose();

        img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setFont(font);
        fm = g2d.getFontMetrics();
        g2d.setColor(Color.WHITE);
        g2d.drawString(text, 0, fm.getAscent());
        g2d.dispose();
        try {
            ImageIO.write(img, "bmp", new File("Text.bmp"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    private static Size getTextImageSize(float fontSize) throws IOException, FontFormatException {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        Font font = new Font("Arial", Font.PLAIN, 48);

        //String fontPath = Font2ImageUtil.class.getClassLoader().getResource(fontName).getPath();
        InputStream is = Font2ImageUtil.class.getClassLoader().getResourceAsStream(fontName);
        font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(fontSize);

        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();

        Size s = new Size();
        s.width = fm.stringWidth("你");
        s.height = fm.getHeight();
        g2d.dispose();
        return s;
    }

    public static byte[] text2Pixel(String text, float fontSize) throws IOException, FontFormatException {
        // load font
        InputStream is = Font2ImageUtil.class.getClassLoader().getResourceAsStream(fontName);
        assert is != null;
        Font font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(fontSize);

        // calculate size
        Size fontPixelSize = getTextImageSize(fontSize);
        byte[] totalBytes = null;
        for (int i = 0; i < text.length(); i++) {
            BufferedImage bufferedImage = text2Image(text.substring(i, i + 1), font, fontPixelSize);
            try {
                // crop border
                Rectangle rectangle = new Rectangle(1, (fontPixelSize.height - fontPixelSize.width) / 2 + 1, fontPixelSize.width - 2, fontPixelSize.width - 2);
                bufferedImage = ImageUtil.crop(bufferedImage, rectangle);

                assert bufferedImage != null;
                ImageIO.write(bufferedImage, "bmp", new File("Text_" + i + ".bmp"));

                if (totalBytes == null) {
                    totalBytes = ImageUtil.convert1BitByteArray(bufferedImage);
                } else {
                    byte[] data = ImageUtil.convert1BitByteArray(bufferedImage);
                    totalBytes = ArrayUtils.addAll(totalBytes, data);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
        }
        return totalBytes;
    }



    public static byte[][] text2Parola(String text, float fontSize) throws IOException, FontFormatException {
        // load font
        InputStream is = Font2ImageUtil.class.getClassLoader().getResourceAsStream(fontName);
        assert is != null;
        Font font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(fontSize);

        // calculate size
        Size fontPixelSize = getTextImageSize(fontSize);
        byte[][] totalBytes = new byte[2][0];
        for (int i = 0; i < text.length(); i++) {
            BufferedImage bufferedImage = text2Image(text.substring(i, i + 1), font, fontPixelSize);
            try {
                // crop border
                Rectangle rectangle = new Rectangle(1, (fontPixelSize.height - fontPixelSize.width) / 2 + 1, fontPixelSize.width - 2, fontPixelSize.width - 2);
                bufferedImage = ImageUtil.crop(bufferedImage, rectangle);

                assert bufferedImage != null;
                ImageIO.write(bufferedImage, "bmp", new File("Text_" + i + ".bmp"));
                /*
                if (bufferedImage.getHeight() < 16) {
                    bufferedImage = ImageUtil.padding(bufferedImage, 2);
                }
                 */
                byte[][] data = ImageUtil.convertParolaDataFormat(bufferedImage, 16);
                totalBytes[0] = ArrayUtils.addAll(totalBytes[0], data[0]);
                totalBytes[1] = ArrayUtils.addAll(totalBytes[1], data[1]);
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
        }
        return totalBytes;
    }

    public static BufferedImage text2Image(String text, Font font, Size size) throws IOException, FontFormatException {
        BufferedImage img = new BufferedImage(size.width, size.height, BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        g2d.setColor(Color.WHITE);
        g2d.drawString(text, 0, fm.getAscent());
        g2d.dispose();

        return img;
    }
    public static byte[] compress(byte[] in) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DeflaterOutputStream defl = new DeflaterOutputStream(out);
            defl.write(in);
            defl.flush();
            defl.close();

            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(150);
            return null;
        }
    }

    public static byte[] decompress(byte[] in) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InflaterOutputStream infl = new InflaterOutputStream(out);
            infl.write(in);
            infl.flush();
            infl.close();

            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(150);
            return null;
        }
    }

}
