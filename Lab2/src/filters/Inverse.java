package filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Inverse {

    public static BufferedImage process(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < image.getHeight() - 1; i++) {
            for (int j = 0; j < image.getWidth() - 1; j++) {
                Color currentColor = new Color(image.getRGB(j,i));
                Color newColor = new Color(255 - currentColor.getRed(), 255 - currentColor.getGreen(), 255 - currentColor.getBlue());
                newImage.setRGB(j, i, newColor.getRGB());
            }
        }
        return newImage;
    }
}
