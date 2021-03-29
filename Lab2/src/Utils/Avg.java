package Utils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Avg {

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public static BufferedImage process(BufferedImage image, int size) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        var height = image.getHeight();
        var width = image.getWidth();
        double newR = 0;
        double newG = 0;
        double newB = 0;
        for (int i = 0; i < height - 1; i++) {
            for (int j = 0; j < width - 1; j++) {

                for (int y = i - (size / 2); y <= i + (size / 2); y++) {
                    for (int x = j - (size / 2); x <= j + (size / 2); x++) {

                        newR += (new Color(image.getRGB(clamp(x, 0, width - 1), clamp(y, 0, height - 1)))).getRed();
                        newG += (new Color(image.getRGB(clamp(x, 0, width - 1), clamp(y, 0, height - 1)))).getGreen();
                        newB += (new Color(image.getRGB(clamp(x, 0, width - 1), clamp(y, 0, height - 1)))).getBlue();
                    }
                }

                newR /= (size * size);
                newG /= (size * size);
                newB /= (size * size);

                newR = clamp((int)newR, 0, 255);
                newG = clamp((int)newG, 0, 255);
                newB = clamp((int)newB, 0, 255);

                newImage.setRGB(j, i, (new Color((int)newR, (int)newG, (int)newB)).getRGB());
            }
        }
        return newImage;
    }
}
