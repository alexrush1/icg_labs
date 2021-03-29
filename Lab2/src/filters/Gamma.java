package filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Gamma {

    public static BufferedImage process(BufferedImage image, double gamma) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        var height = image.getHeight();
        var width = image.getWidth();
        for (int i = 0; i < height - 1; i++) {
            for (int j = 0; j < width - 1; j++) {
                Color currentColor = new Color(image.getRGB(j,i));
                int rGamma = (int)(255 * Math.pow(((double) currentColor.getRed() / 255), gamma));
                int gGamma = (int)(255 * Math.pow(((double) currentColor.getGreen() / 255), gamma));
                int bGamma = (int)(255 * Math.pow(((double) currentColor.getBlue() / 255), gamma));
                Color newColor = new Color(rGamma, gGamma, bGamma);
                newImage.setRGB(j, i, newColor.getRGB());
            }
        }
        return newImage;
    }
}
