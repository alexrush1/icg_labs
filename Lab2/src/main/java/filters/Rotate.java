package filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Rotate {

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public static BufferedImage process(BufferedImage image, int angle) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        var height = image.getHeight();
        var width = image.getWidth();
        var x0 = width / 2;
        var y0 = height / 2;
        double r, sin, cos;
        double a = angle;
        for (int i = 0; i < height - 1; i++) {
            for (int j = 0; j < width - 1; j++) {
                r = Math.sqrt((j - x0) * (j - x0) + (i - y0) * (i - y0));
                sin = Math.sin(a + Math.atan2((i - y0), (j - x0)));
                cos = Math.cos(a + Math.atan2((i - y0), (j - x0)));
                try {
                    newImage.setRGB(j, i, image.getRGB((int) Math.round(x0 + r * cos), (int) Math.round(y0 + r * sin)));
                } catch (Exception e) {
                    newImage.setRGB(clamp(j, 0, width), clamp(i, 0, height), Color.WHITE.getRGB());
                }
            }
        }
        return newImage;
    }
}
