package filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Sobel {
    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public static BufferedImage process(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        var height = image.getHeight();
        var width = image.getWidth();
        for (int i = 0; i < height - 1; i++) {
            for (int j = 0; j < width - 1; j++) {
                Color n1 = new Color(image.getRGB(clamp(j - 1, 0, width), clamp(i - 1, 0, height)));
                Color n2 = new Color(image.getRGB(clamp(j, 0, width), clamp(i - 1, 0, height)));
                Color n3 = new Color(image.getRGB(clamp(j + 1, 0, width), clamp(i - 1, 0, height)));
                Color n4 = new Color(image.getRGB(clamp(j - 1, 0, width), clamp(i, 0, height)));
                Color n6 = new Color(image.getRGB(clamp(j + 1, 0, width), clamp(i, 0, height)));
                Color n7 = new Color(image.getRGB(clamp(j - 1, 0, width), clamp(i + 1, 0, height)));
                Color n8 = new Color(image.getRGB(clamp(j, 0, width), clamp(i + 1, 0, height)));
                Color n9 = new Color(image.getRGB(clamp(j + 1, 0, width), clamp(i + 1, 0, height)));

                var newR = Math.abs(n3.getRed() + 2 * n6.getRed() + n9.getRed() - n1.getRed() - 2 * n4.getRed() - n7.getRed())
                        + Math.abs(n7.getRed() + 2 * n8.getRed() + n9.getRed() - n1.getRed() - 2 * n2.getRed() - n3.getRed());

                var newG = Math.abs(n3.getGreen() + 2 * n6.getGreen() + n9.getGreen() - n1.getGreen() - 2 * n4.getGreen() - n7.getGreen())
                        + Math.abs(n7.getGreen() + 2 * n8.getGreen() + n9.getGreen() - n1.getGreen() - 2 * n2.getGreen() - n3.getGreen());

                var newB = Math.abs(n3.getBlue() + 2 * n6.getBlue() + n9.getBlue() - n1.getBlue() - 2 * n4.getBlue() - n7.getBlue())
                        + Math.abs(n7.getBlue() + 2 * n8.getBlue() + n9.getBlue() - n1.getBlue() - 2 * n2.getBlue() - n3.getBlue());

                newR = clamp(newR, 0, 255);
                newG = clamp(newG, 0, 255);
                newB = clamp(newB, 0, 255);

                newImage.setRGB(j, i, (new Color(newR, newG, newB)).getRGB());
            }
        }
        return newImage;
    }
}
