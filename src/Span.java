import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;

public class Span {

    private BufferedImage image;
    private Stack<Pair<Integer, Integer>> queue;
    private Color newColor;
    private int newColorRGB;

    private int maxX;
    private int maxY;
    int startPixel;

    public Span(BufferedImage image, Color newColor, int maxX, int maxY) {
        this.image = image;
        this.newColor = newColor;
        this.maxX = maxX;
        this.maxY = maxY;
        newColorRGB = newColor.getRGB();
    }

    public void spanFill(int x, int y) {

        queue = new Stack<>();
        startPixel = image.getRGB(x, y);
        queue.push(new Pair<>(x, y));

        while (!queue.empty()) {
            var current = queue.pop();
            var lx = current.getElement0();
            var currentY = current.getElement1();
            var currentX = current.getElement0();

            while ((lx > 0) && (image.getRGB(lx - 1, currentY) != newColorRGB && (image.getRGB(lx - 1, currentY) == startPixel))) {
                image.setRGB(lx - 1, currentY, newColor.getRGB());
                lx = lx - 1;
            }
            while (currentX < maxX && currentX >= 0 && currentY <= maxY && currentY >= 0 && image.getRGB(currentX, currentY) != newColorRGB && (image.getRGB(currentX, currentY) == startPixel)) {
                image.setRGB(currentX, currentY, newColorRGB);
                currentX = currentX + 1;
                //System.out.println(currentX);
            }

            if (currentX - 1 >= 0 && currentY + 1 < maxY) {
                scan(lx, currentX - 1, currentY + 1);
            }
            if (currentX - 1 >= 0 && currentY - 1 >= 0) {
                scan(lx, currentX - 1, currentY - 1);
            }
        }
    }

    public void scan(int lx, int rx, int y) {
        var added = false;
        for (int i = lx; i < rx; i++) {
            if (image.getRGB(i, y) != newColorRGB && (image.getRGB(i, y) != startPixel)) {
                added = false;
            } else if (!added) {
                queue.push(new Pair<>(i, y));
                added = true;
            }
        }
    }

}