package filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Roberts {

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public static BufferedImage process(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        var height = image.getHeight();
        var width = image.getWidth();
        for (int i = 0; i < height - 1; i++) {
            for (int j = 0; j < width - 1; j++) {
                Color currentPixelColor = new Color(image.getRGB(clamp(j, 0, width), clamp(i, 0, height)));
                Color rightPixel = new Color(image.getRGB(clamp(j + 1, 0, width), clamp(i, 0, height)));
                Color lowerPixel = new Color(image.getRGB(clamp(j, 0, width), clamp(i + 1, 0, height)));
                Color rightLowerPixel = new Color(image.getRGB(clamp(j + 1, 0, width), clamp(i + 1, 0, height)));

                var newR = Math.abs(currentPixelColor.getRed() - rightLowerPixel.getRed()) + Math.abs(lowerPixel.getRed() - rightPixel.getRed());
                var newG = Math.abs(currentPixelColor.getGreen() - rightLowerPixel.getGreen()) + Math.abs(lowerPixel.getGreen() - rightPixel.getGreen());
                var newB = Math.abs(currentPixelColor.getBlue() - rightLowerPixel.getBlue()) + Math.abs(lowerPixel.getBlue() - rightPixel.getBlue());

                newR = clamp(newR, 0, 255);
                newG = clamp(newG, 0, 255);
                newB = clamp(newB, 0, 255);

                newImage.setRGB(j, i, (new Color(newR, newG, newB)).getRGB());
            }
        }
        return newImage;
    }
}
