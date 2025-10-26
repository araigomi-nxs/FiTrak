package calculationModels;

public class Walkingworkout extends Workout {

    private double distanceKM;
    private int steps;
    private String intensity;

    public Walkingworkout(double durationMinutes, double weight, String date,
                          double distanceKM, int steps, String intensity) {

        super(durationMinutes, weight, date, calculateMet(intensity));

        this.distanceKM = distanceKM;
        this.steps = steps;
        this.intensity = intensity;
        this.caloriesBurned = calculateCaloriesBurned();
    }

    private static double calculateMet(String intensity) {
        switch (intensity.toLowerCase()) {
            case "brisk":
                return 3.9;  // brisk walk
            case "calm":
                return 2.8;  // slow, relaxed walk
            default:
                return 3.3;  // default average
        }
    }

    @Override
    public double calculateCaloriesBurned() {
        double hours = durationMinutes / 60.0;
        return metValue * weight * hours;
    }
}
