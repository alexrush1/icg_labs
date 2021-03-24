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

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private static int trunc(int[] dots, int value) {
        int min = 256;
        for (int i = 0; i < dots.length; i++) {
            if ((value - dots[i]) < min) {min = dots[i];}
        }
        return min;
    }

    private static int xCoordinateFix(int coord, BufferedImage image) {
        if (coord < 0) { return 0; }
        if (coord > image.getHeight()) {return image.getHeight() - 1;}
        return coord;
    }

    private static int yCoordinateFix(int coord, BufferedImage image) {
        if (coord < 0) { return 0; }
        if (coord > image.getWidth()) {return image.getWidth() - 1;}
        return coord;
    }

    public static BufferedImage process(BufferedImage image, int dots) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        var dotsArray = createDots(dots);
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                //System.out.println("x:" + i + "y:" + j);
                //System.out.println("height:" + image.getHeight() + " width" + image.getWidth());
                var color = new Color(image.getRGB(i, j));
                var oldColor = color.getRed();
                var newColor = trunc(dotsArray, oldColor);
                var error = Math.abs(newColor - oldColor);

                var firstNeighbour = new Color(image.getRGB(xCoordinateFix(i + 1, image), yCoordinateFix(j, image)));
                var secondNeighbour = new Color(image.getRGB(xCoordinateFix(i - 1, image), yCoordinateFix(j + 1, image)));
                var thirdNeighbour = new Color(image.getRGB(xCoordinateFix(i, image), yCoordinateFix(j + 1, image)));
                var fourthNeighbour = new Color(image.getRGB(xCoordinateFix(i + 1, image), yCoordinateFix(j + 1, image)));

                var firstNeighbourRed = clamp(firstNeighbour.getRed() + (error * 7) / 16, 0, 255);
                var secondNeighbourRed = clamp(secondNeighbour.getRed() + (3 * error) / 16, 0, 255);
                var thirdNeighbourRed = clamp(thirdNeighbour.getRed() + (5 * error) / 16, 0, 255);
                var fourthNeighbourRed = clamp(fourthNeighbour.getRed() + error / 16, 0, 255);

                oldColor = color.getGreen();
                newColor = trunc(dotsArray, oldColor);
                error = Math.abs(newColor - oldColor);

                var firstNeighbourGreen = clamp(firstNeighbour.getGreen() + (error * 7) / 16, 0, 255);
                var secondNeighbourGreen = clamp(secondNeighbour.getGreen() + (error * 3) / 16, 0, 255);
                var thirdNeighbourGreen = clamp(thirdNeighbour.getGreen() + (error * 5) / 16, 0, 255);
                var fourthNeighbourGreen = clamp(fourthNeighbour.getGreen() + error / 16, 0, 255);

                oldColor = color.getBlue();
                newColor = trunc(dotsArray, oldColor);
                error = Math.abs(newColor - oldColor);

                var firstNeighbourBlue = clamp(firstNeighbour.getBlue() + (error * 7) / 16, 0, 255);
                var secondNeighbourBlue = clamp(secondNeighbour.getBlue() + (error * 3) / 16, 0, 255);
                var thirdNeighbourBlue = clamp(thirdNeighbour.getBlue() + (error * 5) / 16, 0, 255);
                var fourthNeighbourBlue = clamp(fourthNeighbour.getBlue() + error / 16, 0, 255);

                image.setRGB(Floyd.xCoordinateFix(i + 1, image), Floyd.yCoordinateFix(j, image), (new Color(firstNeighbourRed, firstNeighbourGreen, firstNeighbourBlue)).getRGB());
                image.setRGB(Floyd.xCoordinateFix(i - 1, image), Floyd.yCoordinateFix(j + 1, image), (new Color(secondNeighbourRed, secondNeighbourGreen, secondNeighbourBlue)).getRGB());
                image.setRGB(Floyd.xCoordinateFix(i, image), Floyd.yCoordinateFix(j + 1, image), (new Color(thirdNeighbourRed, thirdNeighbourGreen, thirdNeighbourBlue)).getRGB());
                image.setRGB(Floyd.xCoordinateFix(i + 1, image), Floyd.yCoordinateFix(j + 1, image), (new Color(fourthNeighbourRed, fourthNeighbourGreen, fourthNeighbourBlue)).getRGB());
            }
        }
        return newImage;
    }
}