package filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Floyd {

    private static int[] createDots(int dots) {
        int[] dotsArray = new int[dots];
        dotsArray[0] = 0;
        var interval = 255 / (dots - 1);
        for (int i = 1; i < dots; i++) {
            dotsArray[i] = interval + interval * (i - 1);
        }
        return dotsArray;
    }

    private static int trunc(int[] dots, int value) {
        int min = 256;
        for (int i = 0; i < dots.length; i++) {
            if ((value - dots[i]) < min) {min = dots[i];}
        }
        return min;
    }

    public static BufferedImage process(BufferedImage image, int dots) {
        int[] P = new int[image.getHeight() * image.getWidth()];
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        var dotsArray = Floyd.createDots(dots);
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                var color = new Color(image.getRGB(i, j));
                var oldRed = color.getRed();
                var newRed = Floyd.trunc(dotsArray, oldRed);
                var error = Math.abs(newRed - oldRed);
            }
        }
        return newImage;
    }
}