package filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Floyd {

    private static int[] createDots(int dots) {
        int[] dotsArray = new int[dots + 1];
        dotsArray[0] = 0;
        for (int i = 1; i <= dots; i++) {
            dotsArray[i] = i * 255 / dots;
        }
        return dotsArray;
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private static int trunc(int[] dots, int value) {
        int halfInterval = dots[1] / 2;
        for (int i = 0; i < dots.length; i++) {
            if ((value - dots[i]) <= halfInterval) {return dots[i];}
        }
        return 0;
    }

    private static int xCoordinateFix(int coord, BufferedImage image) {
        if (coord < 0) { return 0; }
        if (coord > image.getWidth()) {return image.getWidth() - 1;}
        return coord;
    }

    private static int yCoordinateFix(int coord, BufferedImage image) {
        if (coord < 0) { return 0; }
        if (coord > image.getHeight()) {return image.getHeight() - 1;}
        return coord;
    }

    public static void process(BufferedImage image, int dots) {

        var dotsArray = createDots(dots - 1);
        for (int j = 0; j < image.getHeight() - 1; j++) {
            for (int i = 0; i < image.getWidth() - 1; i++) {

                var color = new Color(image.getRGB(i, j));
                var oldColor = color.getRed();
                var newColorRed = trunc(dotsArray, oldColor);
                var error = oldColor - newColorRed;

                var firstNeighbour = new Color(image.getRGB(clamp(i + 1, 0, image.getWidth()), clamp(j, 0, image.getHeight())));
                var secondNeighbour = new Color(image.getRGB(clamp(i - 1, 0, image.getWidth()), clamp(j + 1, 0, image.getHeight())));
                var thirdNeighbour = new Color(image.getRGB(clamp(i, 0, image.getWidth()), clamp(j + 1, 0, image.getHeight())));
                var fourthNeighbour = new Color(image.getRGB(clamp(i + 1, 0, image.getWidth()), clamp(j + 1, 0, image.getHeight())));

                var firstNeighbourRed = clamp(firstNeighbour.getRed() + (error * 7) / 16, 0, 255);
                var secondNeighbourRed = clamp(secondNeighbour.getRed() + (3 * error) / 16, 0, 255);
                var thirdNeighbourRed = clamp(thirdNeighbour.getRed() + (5 * error) / 16, 0, 255);
                var fourthNeighbourRed = clamp(fourthNeighbour.getRed() + error / 16, 0, 255);

                oldColor = color.getGreen();
                var newColorGreen = trunc(dotsArray, oldColor);
                error = oldColor - newColorGreen;

                var firstNeighbourGreen = clamp(firstNeighbour.getGreen() + (error * 7) / 16, 0, 255);
                var secondNeighbourGreen = clamp(secondNeighbour.getGreen() + (error * 3) / 16, 0, 255);
                var thirdNeighbourGreen = clamp(thirdNeighbour.getGreen() + (error * 5) / 16, 0, 255);
                var fourthNeighbourGreen = clamp(fourthNeighbour.getGreen() + error / 16, 0, 255);

                oldColor = color.getBlue();
                var newColorBlue = trunc(dotsArray, oldColor);
                error = oldColor - newColorBlue;

                var firstNeighbourBlue = clamp(firstNeighbour.getBlue() + (error * 7) / 16, 0, 255);
                var secondNeighbourBlue = clamp(secondNeighbour.getBlue() + (error * 3) / 16, 0, 255);
                var thirdNeighbourBlue = clamp(thirdNeighbour.getBlue() + (error * 5) / 16, 0, 255);
                var fourthNeighbourBlue = clamp(fourthNeighbour.getBlue() + error / 16, 0, 255);

                image.setRGB(clamp(i + 1, 0, image.getWidth()), clamp(j, 0, image.getHeight()), (new Color(firstNeighbourRed, firstNeighbourGreen, firstNeighbourBlue)).getRGB());
                image.setRGB(clamp(i - 1, 0, image.getWidth()), clamp(j + 1, 0, image.getHeight()), (new Color(secondNeighbourRed, secondNeighbourGreen, secondNeighbourBlue)).getRGB());
                image.setRGB(clamp(i, 0, image.getWidth()), clamp(j + 1, 0, image.getHeight()), (new Color(thirdNeighbourRed, thirdNeighbourGreen, thirdNeighbourBlue)).getRGB());
                image.setRGB(clamp(i + 1, 0, image.getWidth()), clamp(j + 1, 0, image.getHeight()), (new Color(fourthNeighbourRed, fourthNeighbourGreen, fourthNeighbourBlue)).getRGB());
                image.setRGB(i, j, (new Color(newColorRed, newColorGreen, newColorBlue).getRGB()));
            }
        }
    }
}