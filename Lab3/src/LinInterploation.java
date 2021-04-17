public class LinInterploation {

    public static double linInterpolation(double lValue, double rValue, double goal) {

        double min = Math.min(lValue, rValue);
        double max = Math.max(lValue, rValue);

        if (lValue > rValue) return 1 - ((goal - min) / (double) (max - min));

        return ((goal - min) / (double) (max - min));
    }

}
