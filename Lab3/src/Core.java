import java.awt.*;
import java.util.HashMap;

public class Core {

    Preferences preferences;
    HashMap<Pair<Integer, Integer>, Double> gridPoints = new HashMap<>();

    public Core(Preferences preferences) {
        this.preferences = preferences;
    }

    public void calcGridPoints(int xSize, int ySize, double xKoef, double yKoef) {
        var intervalX = xSize / (double) preferences.N;
        var intervalY = ySize / (double) preferences.M;

        for (int i = 0; i <= preferences.N; i++) {
            for (int j = 0; j <= preferences.M; j++) {
                gridPoints.put(new Pair(i * intervalX * xKoef + (preferences.a), j * intervalY * yKoef + preferences.c), (Math.sin(j) * Math.cos(i)));
            }
        }

        for (var e: gridPoints.entrySet()) {
            System.out.println("X: " + e.getKey().getElement0() + "     Y: " + e.getKey().getElement1() + "     F(X,Y): " + e.getValue());
        }
    }
}
