package filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class OrderedDithering {

    private static int[] createDots(int dots) {
        int[] dotsArray = new int[dots + 1];
        dotsArray[0] = 0;
        var interval = 255 / (dots - 1);
        for (int i = 1; i <= dots; i++) {
            dotsArray[i] = i * 255 / dots;
        }
        return dotsArray;
    }

    private static int trunc(int[] dots, int value) {
        int halfInterval = dots[1] / 2;
        for (int i = 0; i < dots.length; i++) {
            if ((value - dots[i]) <= halfInterval) {return dots[i];}
        }
        return 0;
    }

    public static BufferedImage process(BufferedImage image, int dots) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        int[] dotsArray = createDots(dots);
        int[] matrix = new int[] {
                0, 32, 8, 40, 2, 34, 10, 42,
                48, 16, 56, 24, 50, 18, 58, 26,
                12, 44, 4, 36, 14, 46, 6, 38,
                60, 28, 52, 20, 62, 30, 54, 22,
                3, 35, 11, 43, 1, 33, 9, 41,
                51, 19, 59, 27, 49, 17, 57, 25,
                15, 47, 7, 39, 13, 45, 5, 37,
                63, 31, 55, 23, 61, 29, 53, 21
        };

        for (int j = 0; j < image.getHeight() - 1; j++) {
            for (int i = 0; i < image.getWidth() - 1; i++) {
                var color = new Color(image.getRGB(i, j));
                var oldColor = color.getRed();
                double preColor = oldColor + (((double)255 / dots) * ((double) matrix[(i % 8) * 8 + (j % 8)] / 64) - (double) 1 / 2);
                var newColor = trunc(dotsArray, (int) preColor);

                newImage.setRGB(i, j, (new Color(newColor, newColor, newColor).getRGB()));
            }
        }
        return newImage;
    }
}
