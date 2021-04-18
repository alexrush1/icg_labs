import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class Core {

    Preferences preferences;
    Graphics2D graphics2D;
    double[] grid;
    int xSize;
    int ySize;
    double xKoef;
    double yKoef;

    public Core(Preferences preferences, int xSize, int ySize, double xKoef, double yKoef, Graphics2D graphics2D) {
        this.preferences = preferences;
        this.xSize = xSize;
        this.ySize = ySize;
        this.xKoef = xKoef;
        this.yKoef = yKoef;
        this.graphics2D = graphics2D;
    }

    public double[] calcGridPoints() {
        var intervalX = xSize / (double) preferences.N;
        var intervalY = ySize / (double) preferences.M;

        grid = new double[preferences.N * preferences.M * 2];

        int count = 0;
        for (int x = 0; x <= preferences.N; x++) {
            for (int y = 0; y <= preferences.M; y++) {
                grid[y * preferences.N + x] = Math.sin((y * intervalY * yKoef + preferences.c)) * Math.cos((x * intervalX * xKoef + preferences.a));
                System.out.printf("%s", String.format("%.5f  ", Math.sin((y * intervalY * yKoef + preferences.c)) * Math.cos((x * intervalX * xKoef + preferences.a))));
            }
            System.out.println();
        }
        return grid;
    }

    public boolean checkKnots(double value) {
        for (int x = 0; x <= preferences.N; x++) {
            for (int y = 0; y <= preferences.M; y++) {
                if (grid[y * preferences.N + x] == value) {
                    return false;
                }
            }
        }
        return true;
    }

    public void lines(double value, Color color) {
        graphics2D.setColor(color);
        while (!checkKnots(value)) {
            value = value + 0.0000001;
        }

        ArrayList<Pair<Double, Double>> dots = new ArrayList<>();
        DotsFinder.findDots(grid, value, preferences, dots, graphics2D, xKoef, yKoef);

    }

    public void lines(double value, Color color, Graphics graphics2D) {
        graphics2D.setColor(color);
        while (!checkKnots(value)) {
            value = value + 0.0000001;
        }

        ArrayList<Pair<Double, Double>> dots = new ArrayList<>();
        DotsFinder.findDots(grid, value, preferences, dots, graphics2D, xKoef, yKoef);
    }


    public void span(double min, double max, Color newColor, BufferedImage image) {
        Span span = new Span(image, newColor, preferences.boardWidth, preferences.boardHeight);

        for (int x = 0; x < preferences.N; x++) {
            for (int y = 0; y < preferences.M; y++) {
                if (grid[y * preferences.N + x] > min && grid[y * preferences.N + x] <= max) {
                    span.spanFill((int)(x * (preferences.boardWidth/ preferences.N)), (int)(y * (preferences.boardHeight / preferences.M)));
                }
            }
        }
    }

}
