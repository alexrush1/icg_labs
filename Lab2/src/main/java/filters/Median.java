package filters;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Median {
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

                ArrayList<Integer> rPixels = new ArrayList<>();
                ArrayList<Integer> gPixels = new ArrayList<>();
                ArrayList<Integer> bPixels = new ArrayList<>();

                rPixels.add(n1.getRed());
                rPixels.add(n2.getRed());
                rPixels.add(n3.getRed());
                rPixels.add(n4.getRed());
                rPixels.add(n5.getRed());
                rPixels.add(n6.getRed());
                rPixels.add(n7.getRed());
                rPixels.add(n8.getRed());
                rPixels.add(n9.getRed());
                rPixels.add(n10.getRed());
                rPixels.add(n11.getRed());
                rPixels.add(n12.getRed());
                rPixels.add(currentPixelColor.getRed());
                rPixels.add(n14.getRed());
                rPixels.add(n15.getRed());
                rPixels.add(n16.getRed());
                rPixels.add(n17.getRed());
                rPixels.add(n18.getRed());
                rPixels.add(n19.getRed());
                rPixels.add(n20.getRed());
                rPixels.add(n21.getRed());
                rPixels.add(n22.getRed());
                rPixels.add(n23.getRed());
                rPixels.add(n24.getRed());
                rPixels.add(n25.getRed());

                gPixels.add(n1.getGreen());
                gPixels.add(n2.getGreen());
                gPixels.add(n3.getGreen());
                gPixels.add(n4.getGreen());
                gPixels.add(n5.getGreen());
                gPixels.add(n6.getGreen());
                gPixels.add(n7.getGreen());
                gPixels.add(n8.getGreen());
                gPixels.add(n9.getGreen());
                gPixels.add(n10.getGreen());
                gPixels.add(n11.getGreen());
                gPixels.add(n12.getGreen());
                gPixels.add(currentPixelColor.getGreen());
                gPixels.add(n14.getGreen());
                gPixels.add(n15.getGreen());
                gPixels.add(n16.getGreen());
                gPixels.add(n17.getGreen());
                gPixels.add(n18.getGreen());
                gPixels.add(n19.getGreen());
                gPixels.add(n20.getGreen());
                gPixels.add(n21.getGreen());
                gPixels.add(n22.getGreen());
                gPixels.add(n23.getGreen());
                gPixels.add(n24.getGreen());
                gPixels.add(n25.getGreen());

                bPixels.add(n1.getBlue());
                bPixels.add(n2.getBlue());
                bPixels.add(n3.getBlue());
                bPixels.add(n4.getBlue());
                bPixels.add(n5.getBlue());
                bPixels.add(n6.getBlue());
                bPixels.add(n7.getBlue());
                bPixels.add(n8.getBlue());
                bPixels.add(n9.getBlue());
                bPixels.add(n10.getBlue());
                bPixels.add(n11.getBlue());
                bPixels.add(n12.getBlue());
                bPixels.add(currentPixelColor.getBlue());
                bPixels.add(n14.getBlue());
                bPixels.add(n15.getBlue());
                bPixels.add(n16.getBlue());
                bPixels.add(n17.getBlue());
                bPixels.add(n18.getBlue());
                bPixels.add(n19.getBlue());
                bPixels.add(n20.getBlue());
                bPixels.add(n21.getBlue());
                bPixels.add(n22.getBlue());
                bPixels.add(n23.getBlue());
                bPixels.add(n24.getBlue());
                bPixels.add(n25.getBlue());

                Collections.sort(rPixels);
                Collections.sort(gPixels);
                Collections.sort(bPixels);




                int newR = rPixels.get(13);
                int newG = gPixels.get(13);
                int newB = bPixels.get(13);

                Color newColor = new Color(newR, newG, newB);
                newImage.setRGB(j, i, newColor.getRGB());
            }
        }
        return newImage;
    }
}
