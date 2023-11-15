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

    public static BufferedImage padding(BufferedImage bufferedImage, int p) {
        BufferedImage newImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight() + p, bufferedImage.getType());

        Graphics g = newImage.getGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, newImage.getWidth(), newImage.getHeight());
        g.drawImage(bufferedImage, 0, p, null);
        g.dispose();
        return newImage;
    }


    public static byte[] convert1BitByteArray(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        byte[] data = new byte[width * height / 8];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = bufferedImage.getRGB(j, i);
                int bit = (pixel & 0xFF) / 255;
                System.out.print(bit + " ");
                data[(i * width + j) / 8] += (byte) ((bit & 0x1) << ((i * width + j) % 8));
            }
            System.out.println(" ");
        }

        System.out.println(HexFormat.of().formatHex(data));
        return data;
    }

    public static byte[][] convertParolaDataFormat(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        // check col size
        int[] col_pixel = new int[width];
        for (int j = 0; j < width; j++) {
            int col_bit = 0;
            for (int i = 0; i < height; i++) {
                int pixel = bufferedImage.getRGB(j, i);
                int bit = (pixel & 0xFF) / 255;
                if (bit == 1) {
                    col_bit = 1;
                    break;
                }
            }
            if (col_bit == 1) {
                col_pixel[j] = 1;
            }
        }
        for (int i = 0; i < width; i++) {
            System.out.print(col_pixel[i] + ",");
        }
        System.out.println();

        int col_size = width;
        for (int j = 0; j < width; j++) {
            if (col_pixel[j] == 0) {
                col_size--;
            } else {
                break;
            }
        }
        System.out.println("col size -> " + col_size);


        for (int j = width - 1; j >= 0; j--) {
            if (col_pixel[j] == 0) {
                col_size--;
            } else {
                break;
            }
        }
        System.out.println("col size <- " + col_size);

        if (col_size < 0) {
            col_size = 0;
        }

        byte[][] data = new byte[2][col_size + 1];
        data[0][0] = (byte) col_size;
        data[1][0] = (byte) col_size;

        for (int i = 0, k = 1; i < width && k < col_size + 1; i++) {
            if (col_pixel[i] != 0) {
                // upper
                for (int j = 0; j < height / 2; j++) {
                    int pixel = bufferedImage.getRGB(i, j);
                    int bit = (pixel & 0xFF) / 255;
                    System.out.print(bit + " ");
                    data[0][k] += (byte) ((bit & 0x1) << j);
                }
                // lower
                for (int j = height / 2; j < height; j++) {
                    int pixel = bufferedImage.getRGB(i, j);
                    int bit = (pixel & 0xFF) / 255;
                    System.out.print(bit + " ");
                    data[1][k] += (byte) ((bit & 0x1) << (j - height/2));
                }
                k++;
            } else {
                System.out.println("skip: " + i);
            }
        }
        System.out.println();


        System.out.println("data 0 hex: " + HexFormat.of().formatHex(data[0]));
        printParolaDataFormat(data[0]);

        System.out.println("data 1 hex: " + HexFormat.of().formatHex(data[1]));
        printParolaDataFormat(data[1]);

        return data;
    }

    public static void printParolaDataFormat(byte[] data) {
        System.out.println("data parola data format: ");
        for (byte d : data) {
            System.out.print(d & 0xFF);
            System.out.print(",");
        }
        System.out.println();
    }

    public static void print1Bit(byte[] data) {
        System.out.println(HexFormat.of().formatHex(data));
        int width = (int) Math.sqrt(data.length * 8);
        for (int i = 0; i < data.length; i++) {
            if (i % (width / 8) == 0) {
                System.out.println(" ");
            }
            for (int j = 0; j < 8; j++) {
                System.out.print(((data[i] >> j) & 0x1) + " ");
            }
        }
        System.out.println(" ");
    }

    private static int[] calculateParolaWordPosition(byte[] data) {
        int word = 0;
        int column = 0;
        int columnTotal = 0;
        for (int i = 0; i < data.length; i++) {
            if (i == 0) {
                column = data[i];
                if (column > 0) {
                    word++;
                }
                columnTotal += (column + 1);
            } else if (i == columnTotal) {
                column = data[i];
                if (column > 0) {
                    word++;
                }
                columnTotal += (column + 1);
            }
        }

        int[] wordPositions = new int[word];
        int k = 0;
        columnTotal = 0;
        for (int i = 0; i < data.length; i++) {
            if (i == 0) {
                wordPositions[k++] = i;
                columnTotal += ( data[i] + 1);
            } else if (i == columnTotal) {
                wordPositions[k++] = i;
                columnTotal += (data[i] + 1);
            }
        }
        return wordPositions;
    }

    public static void printParolaMatrix(byte[][] data, int font_size) {
        System.out.println(HexFormat.of().formatHex(data[0]));
        System.out.println(HexFormat.of().formatHex(data[1]));

        // word count
        int[] wordPositions = calculateParolaWordPosition(data[0]);

        System.out.println("word count: " + wordPositions.length);

        for (int k = 0; k < wordPositions.length; k++) {
            int column =data[0][wordPositions[k]];
            System.out.println("column: " + column);
            int[][] pixels = new int[font_size][column];
            // data[0]:  upper 8x16
            // data[1]:  lower 8x16

            for (int i =1; i <= column; i++) {
                // upper
                if (i < data[0].length) {
                    for (int j = 0; j < font_size / 2; j++) {
                        //System.out.println("i: " + i + " j: " + j);
                        pixels[j][i - 1] = ((data[0][i +  wordPositions[k]] >> j) & 0x1);
                    }
                }
                // lower
                if (i < data[1].length) {
                    for (int j = 0; j < font_size / 2; j++) {
                        pixels[j + font_size / 2][i - 1] = (((data[1][i +  wordPositions[k]] & 0xFF - 0) >> j) & 0x1);
                    }
                }
            }

            for (int i = 0; i < font_size; i++) {
                for (int j = 0; j < column; j++) {
                    System.out.print((pixels[i][j]==1?"X":" ") + " ");
                }
                System.out.println();
            }
            System.out.println(" ");
        }
    }
}
