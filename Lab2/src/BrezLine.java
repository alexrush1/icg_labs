import java.awt.*;
import java.awt.image.BufferedImage;

public class BrezLine {

    private static int maxX;
    private static int maxY;

    private static void drawDot(BufferedImage image, int x, int y, int color) {
        if (x > maxX - 1 || x < 0 || y > maxY - 1 || y < 0) return;

        image.setRGB(x, y, color);
    }

    public BrezLine(int xSize, int ySize) {
        maxX = xSize;
        maxY = ySize;
    }

    public static void line(BufferedImage image, Color color, int x0, int y0, int x1, int y1) {

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);

        int derr;

        if (dx > dy) {
            derr = dy + 1;
        } else {
            derr = dx + 1;
        }


        int err = 0;

        if (dx > dy) {
            int y = y0;
            int diry = y1 - y0;
            if (diry > 0) {
                diry = 1;
            }
            if (diry < 0) {
                diry = -1;
            }

            if (x0 < x1) {
                for (int x = x0; x < x1; x++) {
                    drawDot(image, x, y, color.getRGB());
                    err = err + derr;
                    if (err >= dx + 1) {
                        y = y + diry;
                        err = err - (dx + 1);
                    }
                }
            } else {
                for (int x = x0; x > x1; x--) {
                    drawDot(image, x, y, color.getRGB());
                    err = err + derr;
                    if (err >= dx + 1) {
                        y = y + diry;
                        err = err - (dx + 1);
                    }
                }
            }
        } else {
            int x = x0;
            int dirx = x1 - x0;
            if (dirx > 0) {
                dirx = 1;
            }
            if (dirx < 0) {
                dirx = -1;
            }

            if (y0 < y1) {
                for (int y = y0; y < y1; y++) {
                    drawDot(image, x, y, color.getRGB());
                    err = err + derr;
                    if (err >= dx + 1) {
                        x = x + dirx;
                        err = err - (dy + 1);
                    }
                }
            } else {
                for (int y = y0; y > y1; y--) {
                    drawDot(image, x, y, color.getRGB());
                    err = err + derr;
                    if (err >= dx + 1) {
                        x = x + dirx;
                        err = err - (dy + 1);
                    }
                }
            }
        }
    }

}
