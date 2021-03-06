package filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Sharpness {

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public static BufferedImage process(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        var height = image.getHeight();
        var width = image.getWidth();
        for (int i = 0; i < height - 1; i++) {
            for (int j = 0; j < width - 1; j++) {
                Color currentPixelColor = new Color(image.getRGB(j,i));
                Color lowerPixel;
                Color upperPixel;
                Color leftPixel;
                Color rightPixel;

                if ((i + 1) >= height) {
                    lowerPixel = Color.BLACK;
                } else {lowerPixel = new Color(image.getRGB(j, i + 1));}

                if ((i - 1) < 0) {
                    upperPixel = Color.BLACK;
                } else {upperPixel = new Color(image.getRGB(j, i - 1));}

                if ((j - 1) < 0) {
                    leftPixel = Color.BLACK;
                } else {leftPixel = new Color(image.getRGB(j - 1, i));}

                if ((j + 1) >= width) {
                    rightPixel = Color.BLACK;
                } else {rightPixel = new Color(image.getRGB(j + 1, i));}

                int newR = currentPixelColor.getRed() * 5 - lowerPixel.getRed() - upperPixel.getRed()
                        - leftPixel.getRed() - rightPixel.getRed();

                int newG = currentPixelColor.getGreen() * 5 - lowerPixel.getGreen() - upperPixel.getGreen()
                        - leftPixel.getGreen() - rightPixel.getGreen();

                int newB = currentPixelColor.getBlue() * 5 - lowerPixel.getBlue() - upperPixel.getBlue()
                        - leftPixel.getBlue() - rightPixel.getBlue();

                //System.out.println("before R: " + newR + " G: " + newG + " B: " + newB);

                newR = clamp(newR, 0, 255);
                newG = clamp(newG, 0, 255);
                newB = clamp(newB, 0, 255);

                //System.out.println("R: " + newR + " G: " + newG + " B: " + newB);
                Color newColor = new Color(newR, newG, newB);
                newImage.setRGB(j, i, newColor.getRGB());
            }
        }
        return newImage;
    }
}
