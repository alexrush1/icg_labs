import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DotsFinder {

    public static void findHorizontalDots(double[] grid, double value, Preferences preferences, ArrayList<Pair<Double, Double>> dots) {
        for (int i = 0; i <= preferences.M; i++) {
            for (int j = 0; j < preferences.N; j++) {
                var lValue = grid[i * preferences.N + j];
                var rValue = grid[i * preferences.N + j + 1];
                if (lValue < value && rValue > value) {
                    var polValue = LinInterploation.linInterpolation(lValue, rValue, value);
                    dots.add(new Pair<>(lValue + polValue, rValue));
                }
            }
        }
    }

    public static void findVerticalDots(double[] grid, double value, Preferences preferences, ArrayList<Pair<Double, Double>> dots) {
        for (int i = 0; i < preferences.M; i++) {
            for (int j = 0; j <= preferences.N; j++) {
                var upValue = grid[i * preferences.N + j];
                var downValue = grid[(i + 1) * preferences.N];
                if (upValue < value && downValue > value) {
                    var polValue = LinInterploation.linInterpolation(upValue, downValue, value);
                    dots.add(new Pair<>(upValue + polValue, downValue));
                }
            }
        }
    }

    public static void fourWay(Preferences preferences, double xKoef, double yKoef, int y, int x, ArrayList<Pair<Double, Double>> dots, double upLeftValue, double upRightValue, double downLeftValue, double downRightValue, double value, Graphics graphics2D) {
        var intervalX = preferences.boardWidth / (double) preferences.N;
        var intervalY = preferences.boardHeight / (double) preferences.M;

        var centerY = (0.5 + y);
        var centerX = (0.5 + x);

        var size = dots.size();
        var firstDot = dots.get(size - 4);
        var secondDot = dots.get(size - 3);
        var thirdDot = dots.get(size - 2);
        var fourthDot = dots.get(size - 1);
        dots.remove(size - 1);
        dots.remove(size - 2);
        dots.remove(size - 3);
        dots.remove(size - 4);

        var centerPoint = Math.sin((centerY * intervalY * yKoef + preferences.c)) * Math.cos((centerX * intervalX * xKoef + preferences.a));

        //System.out.println("center : " + centerPoint);

        if ((upLeftValue < value && centerPoint > value) || (upLeftValue > value && centerPoint < value)) {
            var polValue = LinInterploation.linInterpolation(upLeftValue, centerPoint, value);
            dots.add(firstDot);
            dots.add(new Pair<Double, Double>((double) x + polValue/2, y + polValue/2));
            dots.add(new Pair<Double, Double>((double) x + polValue/2, y + polValue/2));
            dots.add(secondDot);
            //System.out.println("1");
        }

        if ((centerPoint < value && upRightValue > value) || (centerPoint > value && upRightValue < value)) {
            var polValue = LinInterploation.linInterpolation(centerPoint, upRightValue, value);
            dots.add(secondDot);
            dots.add(new Pair<Double, Double>(x + polValue/2 + 0.5, (double) y + polValue/2));
            dots.add(new Pair<Double, Double>(x + polValue/2 + 0.5, (double) y + polValue/2));
            dots.add(fourthDot);
            //System.out.println("2");
        }

        if ((centerPoint < value && downRightValue > value) || (centerPoint > value && downRightValue < value)) {
            var polValue = LinInterploation.linInterpolation(centerPoint, downRightValue, value);
            dots.add(thirdDot);
            dots.add(new Pair<Double, Double>(x + polValue/2 + 0.5, (double) (y + polValue/2 + 0.5)));
            dots.add(new Pair<Double, Double>(x + polValue/2 + 0.5, (double) (y + polValue/2 + 0.5)));
            dots.add(fourthDot);
            //System.out.println("3");
        }

        if ((centerPoint < value && downLeftValue > value) || (centerPoint > value && downLeftValue < value)) {
            var polValue = LinInterploation.linInterpolation(downLeftValue, centerPoint, value);
            dots.add(firstDot);
            dots.add(new Pair<Double, Double>((double) (x + polValue/2), y + polValue/2 + 0.5));
            dots.add(new Pair<Double, Double>((double) (x + polValue/2), y + polValue/2 + 0.5));
            dots.add(thirdDot);
            //System.out.println("4");
        }

        var newSize = dots.size();

        //System.out.println("oldSize = " + size + " newSize = " + newSize);

        for (int k = size - 4; k < newSize - 1; k = k + 2) {
            //System.out.println(k);
            graphics2D.drawLine((int) (dots.get(k).getElement0() * intervalX), (int) (dots.get(k).getElement1() * intervalY), (int) (dots.get(k + 1).getElement0() * intervalX), (int) (dots.get(k + 1).getElement1() * intervalY));
        }


    }

    public static void findDots(double[] grid, double value, Preferences preferences, ArrayList<Pair<Double, Double>> dots, Graphics graphics2D, double xKoef, double yKoef) {
        var intervalX = preferences.boardWidth / (double) preferences.N;
        var intervalY = preferences.boardHeight / (double) preferences.M;

        int count = 0;
        for (int i = 0; i < preferences.M; i++) {
            for (int j = 0; j < preferences.N; j++) {
                var upLeftValue = grid[i * preferences.N + j];
                var downLeftValue = grid[(i + 1) * preferences.N + j];
                var upRightValue = grid[i * preferences.N + j + 1];
                var downRightValue = grid[(i + 1) * preferences.N + 1 + j];

                var previousCount = count;

                if ((upLeftValue < value && downLeftValue > value) || (upLeftValue > value && downLeftValue < value)) {
                    var polValue = LinInterploation.linInterpolation(upLeftValue, downLeftValue, value);
                    dots.add(new Pair<Double, Double>((double) j, i + polValue));
                    count++;
                }

                if ((upLeftValue < value && upRightValue > value) || (upLeftValue > value && upRightValue < value)) {
                    var polValue = LinInterploation.linInterpolation(upLeftValue, upRightValue, value);
                    dots.add(new Pair<Double, Double>(j + polValue, (double) i));
                    count++;
                }

                if ((downLeftValue < value && downRightValue > value) || (downLeftValue > value && downRightValue < value)) {
                    var polValue = LinInterploation.linInterpolation(downLeftValue, downRightValue, value);
                    dots.add(new Pair<Double, Double>(j + polValue, (double) (i + 1)));
                    count++;
                }

                if ((upRightValue < value && downRightValue > value) || (upRightValue > value && downRightValue < value)) {
                    var polValue = LinInterploation.linInterpolation(upRightValue, downRightValue, value);
                    dots.add(new Pair<Double, Double>((double) (j + 1), i + polValue));
                    count++;
                }

                if (count - previousCount == 4) {
                    fourWay(preferences, xKoef, yKoef, i, j, dots, upLeftValue, upRightValue, downLeftValue, downRightValue, value, graphics2D);
                    count += 4;
                } else {
                    for (int k = previousCount; k < count - 1; k++) {
                        graphics2D.drawLine((int) (dots.get(k).getElement0() * intervalX), (int) (dots.get(k).getElement1() * intervalY), (int) (dots.get(k + 1).getElement0() * intervalX), (int) (dots.get(k + 1).getElement1() * intervalY));
                    }
                }

            }
        }
//        graphics2D.setColor(Color.BLACK);
//        for (var e: dots) {
//            graphics2D.drawOval((int) (e.getElement0() * intervalX) - 10, (int) (e.getElement1() * intervalY) - 10, 20, 20);
//        }
    }

}
