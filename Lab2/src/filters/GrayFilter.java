package filters;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class GrayFilter {

    public static BufferedImage process(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < image.getHeight() - 1; i++) {
            for (int j = 0; j < image.getWidth() - 1; j++) {
                Color currentColor = new Color(image.getRGB(j,i));
                int avgColor = (int) ((currentColor.getRed() * 0.299) + (currentColor.getGreen() * 0.587) + (currentColor.getBlue() * 0.114));
                Color newColor = new Color(avgColor, avgColor, avgColor);
                newImage.setRGB(j, i, newColor.getRGB());
            }
        }
        return newImage;
    }
}
