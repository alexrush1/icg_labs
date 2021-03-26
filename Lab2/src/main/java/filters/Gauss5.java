package filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Gauss5 {
    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public static BufferedImage process(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        var height = image.getHeight();
        var width = image.getWidth();
        for (int i = 0; i < height - 1; i++) {
            for (int j = 0; j < width - 1; j++) {
                Color currentPixelColor = new Color(image.getRGB(j,i));
                //System.out.println("i: " + i + " j: " + j);
                //System.out.println("maxX: "+ height + "maxY: " + width);
                Color n1 = new Color(image.getRGB(clamp(j - 2, 0, width - 1), clamp(i - 2, 0, height - 1)));
                Color n2 = new Color(image.getRGB(clamp(j - 2, 0, width - 1), clamp(i - 1, 0, height - 1)));
                Color n3 = new Color(image.getRGB(clamp(j - 2, 0, width - 1), clamp(i, 0, height - 1)));
                Color n4 = new Color(image.getRGB(clamp(j - 2, 0, width - 1), clamp(i + 1, 0, height - 1)));
                Color n5 = new Color(image.getRGB(clamp(j - 2, 0, width - 1), clamp(i + 2, 0, height - 1)));

                Color n6 = new Color(image.getRGB(clamp(j - 1, 0, width - 1), clamp(i - 2, 0, height - 1)));
                Color n7 = new Color(image.getRGB(clamp(j - 1, 0, width - 1), clamp(i - 2, 0, height - 1)));
                Color n8 = new Color(image.getRGB(clamp(j - 1, 0, width - 1), clamp(i - 2, 0, height - 1)));
                Color n9 = new Color(image.getRGB(clamp(j - 1, 0, width - 1), clamp(i - 2, 0, height - 1)));
                Color n10 = new Color(image.getRGB(clamp(j - 1, 0, width - 1), clamp(i - 2, 0, height - 1)));

                Color n11 = new Color(image.getRGB(clamp(j, 0, width - 1), clamp(i - 2, 0, height - 1)));
                Color n12 = new Color(image.getRGB(clamp(j, 0, width - 1), clamp(i - 1, 0, height - 1)));
                Color n14 = new Color(image.getRGB(clamp(j, 0, width - 1), clamp(i + 1, 0, height - 1)));
                Color n15 = new Color(image.getRGB(clamp(j, 0, width - 1), clamp(i + 2, 0, height - 1)));

                Color n16 = new Color(image.getRGB(clamp(j + 1, 0, width - 1), clamp(i - 2, 0, height - 1)));
                Color n17 = new Color(image.getRGB(clamp(j + 1, 0, width - 1), clamp(i - 1, 0, height - 1)));
                Color n18 = new Color(image.getRGB(clamp(j + 1, 0, width - 1), clamp(i, 0, height - 1)));
                Color n19 = new Color(image.getRGB(clamp(j + 1, 0, width - 1), clamp(i + 1, 0, height - 1)));
                Color n20 = new Color(image.getRGB(clamp(j + 1, 0, width - 1), clamp(i + 2, 0, height - 1)));

                Color n21 = new Color(image.getRGB(clamp(j + 2, 0, width - 1), clamp(i - 2, 0, height - 1)));
                Color n22 = new Color(image.getRGB(clamp(j + 2, 0, width - 1), clamp(i - 1, 0, height - 1)));
                Color n23 = new Color(image.getRGB(clamp(j + 2, 0, width - 1), clamp(i, 0, height - 1)));
                Color n24 = new Color(image.getRGB(clamp(j + 2, 0, width - 1), clamp(i + 1, 0, height - 1)));
                Color n25 = new Color(image.getRGB(clamp(j + 2, 0, width - 1), clamp(i + 2, 0, height - 1)));


                int newR = (n1.getRed() + 2 * n2.getRed() + 3 * n3.getRed() + 2 * n4.getRed() + n5.getRed() +
                        2 * n6.getRed() + 4 * n7.getRed() + 5 * n8.getRed() + 4 * n9.getRed() + 2 * n10.getRed() +
                        3 * n11.getRed() + 5 * n12.getRed() + 6 * currentPixelColor.getRed() + 5 * n14.getRed() + 3 * n15.getRed() +
                        2 * n16.getRed() + 4 * n17.getRed() + 5 * n18.getRed() + 4 * n19.getRed() + 2 * n20.getRed() +
                        n21.getRed() + 2 * n22.getRed() + 3 * n23.getRed() + 2 * n24.getRed() + n25.getRed()) / 74;

                int newG = (n1.getGreen() + 2 * n2.getGreen() + 3 * n3.getGreen() + 2 * n4.getGreen() + n5.getGreen() +
                        2 * n6.getGreen() + 4 * n7.getGreen() + 5 * n8.getGreen() + 4 * n9.getGreen() + 2 * n10.getGreen() +
                        3 * n11.getGreen() + 5 * n12.getGreen() + 6 * currentPixelColor.getGreen() + 5 * n14.getGreen() + 3 * n15.getGreen() +
                        2 * n16.getGreen() + 4 * n17.getGreen() + 5 * n18.getGreen() + 4 * n19.getGreen() + 2 * n20.getGreen() +
                        n21.getGreen() + 2 * n22.getGreen() + 3 * n23.getGreen() + 2 * n24.getGreen() + n25.getGreen()) / 74;

                int newB = (n1.getBlue() + 2 * n2.getBlue() + 3 * n3.getBlue() + 2 * n4.getBlue() + n5.getBlue() +
                        2 * n6.getBlue() + 4 * n7.getBlue() + 5 * n8.getBlue() + 4 * n9.getBlue() + 2 * n10.getBlue() +
                        3 * n11.getBlue() + 5 * n12.getBlue() + 6 * currentPixelColor.getBlue() + 5 * n14.getBlue() + 3 * n15.getBlue() +
                        2 * n16.getBlue() + 4 * n17.getBlue() + 5 * n18.getBlue() + 4 * n19.getBlue() + 2 * n20.getBlue() +
                        n21.getBlue() + 2 * n22.getBlue() + 3 * n23.getBlue() + 2 * n24.getBlue() + n25.getBlue()) / 74;

//                newR = newR + 128;
//                newG = newG + 128;
//                newB = newB + 128;

                newR = clamp(newR, 0, 255);
                newG = clamp(newG, 0, 255);
                newB = clamp(newB, 0, 255);

                //System.out.println("R: " + newR + " G: " + newG + " B: " + newB);
                Color newColor = new Color(newR, newG, newB);
                newImage.setRGB(j, i, newColor.getRGB());
            }
        }
        return newImage;
    }
}
