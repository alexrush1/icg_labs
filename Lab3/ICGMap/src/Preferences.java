import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Preferences {

    //board size
    public int boardHeight = 400;
    public int boardWidth = 600;

    //function boundaries
    public float a = -5f;
    public float b = 5f;
    public float c = -5f;
    public float d = 5f;

    //grid size in X and Y
    public int N = 15;
    public int M = 8;

    //number of isolines
    public int K = 2;

    //colors array
    ArrayList<Color> colors = new ArrayList<>();

    ArrayList<Double> intervals = new ArrayList<Double>();

    //isolines color
    Color isolinesColor;

    Board board;

    double min;
    double max;

    public Preferences(Board board) {
        this.board = board;
        colors.add(Color.red);
        colors.add(Color.orange);
        colors.add(Color.yellow);
        isolinesColor = Color.BLACK;
        prepareValues();
        loadIntervals();
    }

    public void prepareValues() {
        min =  Double.MAX_VALUE;
        max = Double.MIN_VALUE;
        intervals.clear();
        for (float i = a; i < b; i+=0.01) {
            for (float y = c; y < d; y+=0.01) {
                var value = Math.cos(i) * Math.sin(y);
                if (value < min) {
                    min = value;
                }
                if (value > max) {
                    max = value;
                }
            }
        }
        intervals.add(min);
        System.out.println("MIN = " + min);
        var interval = (max - min) / K;
        for (int i = 1; i <= K; i++) {
            intervals.add((double) (min + (interval * i)));
        }
        intervals.add(max);

        for (var interva: intervals) {
            //System.out.println(interva);
        }
    }

    public void loadIntervals() {
        min =  Double.MAX_VALUE;
        max = Double.MIN_VALUE;

        for (double x = a; x < b; x+=0.001) {
            for (double y = c; y < d; y+=0.001) {
                var value = Math.cos(x) * Math.sin(y);
                if (value < min) {
                    min = value;
                }
                if (value > max) {
                    max = value;
                }
            }
        }
        intervals.clear();
        intervals.add(min);
        var interval = (max - min) / (K + 1);
        for (int i = 1; i <= K; i++) {
            intervals.add((double) (min + (interval * i)));
        }
        intervals.add(max);

        for (var interva: intervals) {
            System.out.println(interva);
        }
    }

    public void loadPreferences(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileInputStream(file));
        a = scanner.nextFloat();
        b = scanner.nextFloat();
        c = scanner.nextFloat();
        d = scanner.nextFloat();
        System.out.println("Boundaries: " + a + " " + b + " " + c + " " + d);

        N = scanner.nextInt();
        M = scanner.nextInt();
        System.out.println("Grid size: " + N + " x " + M);

        K = scanner.nextInt();
        System.out.println("Isolines: " + K);

        colors.clear();
        for (int i = 0; i <= K; i++) {
            colors.add(new Color(scanner.nextInt(), scanner.nextInt(), scanner.nextInt()));
        }

        System.out.println("Colors:");
        for (var c: colors) {
            System.out.println("[" + c.getRed() + ", " + c.getGreen() + ", " + c.getBlue() + "]");
        }

        isolinesColor = new Color(scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
        System.out.println("Isolines color: [" + isolinesColor.getRed() + ", " + isolinesColor.getGreen() + ", " + isolinesColor.getBlue() + "]");

        prepareValues();
        loadIntervals();
        board.workingPanel.reCoef();
        board.workingPanel.reGrid();
        board.workingPanel.paint();
        board.legendPanel.paint();
    }
}
