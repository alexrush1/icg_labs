import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Preferences {

    //function boundaries
    public float a = -5f;
    public float b = 5f;
    public float c = -5f;
    public float d = 5f;

    //grid size in X and Y
    public int N = 15;
    public int M = 12;

    //number of isolines
    public int K = 3;

    //colors array
    ArrayList<Color> colors = new ArrayList<>();

    //isolines color
    Color isolinesColor;

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

        for (int i = 0; i < K; i++) {
            colors.add(new Color(scanner.nextInt(), scanner.nextInt(), scanner.nextInt()));
        }

        System.out.println("Colors:");
        for (var c: colors) {
            System.out.println("[" + c.getRed() + ", " + c.getGreen() + ", " + c.getBlue() + "]");
        }

        isolinesColor = new Color(scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
        System.out.println("Isolines color: [" + isolinesColor.getRed() + ", " + isolinesColor.getGreen() + ", " + isolinesColor.getBlue() + "]");

    }
}
