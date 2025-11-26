package calculationModels.basic;

import calculationModels.metrics.MetricsCalculator;
import java.time.LocalDateTime;

public class WalkingWorkout extends BasicWorkout {

    private double distanceKM;
    private int steps;
    private String intensity;
    private String sex;
    private double height;
    private double calPerStep;

    private static final double MALE_STEP_LENGTH = 0.414;
    private static final double FEMALE_STEP_LENGTH =0.413;

    public WalkingWorkout(double durationMinutes, double weight, LocalDateTime startDT, LocalDateTime endDT,int steps, String intensity, String sex, double height) {

        super(durationMinutes, weight, startDT,endDT,0 , "Walking");
        this.steps = steps;
        this.height = (height*100);
        this.intensity = intensity;
        this.sex = sex;
        this.distanceKM = Math.round(calculateDistance(steps, sex, this.height) * 100.0) / 100.0;
        this.workoutType = "Walking";
        this.caloriesBurned = Math.round(calculateCaloriesBurned() * 100.0) / 100.0;
        this.calPerStep = MetricsCalculator.calculateCalPerStep(caloriesBurned, steps);

    }

    private static double calculateDistance(int steps, String gender,double height) {
        double heightMeters = height / 100.0;
        double strideLength = gender.equalsIgnoreCase("male") ? (heightMeters * MALE_STEP_LENGTH) : (heightMeters * FEMALE_STEP_LENGTH);
        return (steps * strideLength) / 1000.0; // km
    }


    private static double calculateMet(String intensity) {
        if (intensity == null) return 3.3; // default moderate walk

        switch (intensity.toLowerCase()) {
            case "brisk": return 3.9; // brisk pace ~4 MET
            case "calm":  return 2.8; // slow pace ~2.8 MET
            default:      return 3.3; // fallback moderate ~3.3 MET
        }
    }

    @Override
    public double calculateCaloriesBurned() {
        metValue= calculateMet(intensity);
        return MetricsCalculator.calculateCalories(metValue, initialWeight, durationMinutes);
    }

    // Getters
    public double getDistanceKM() { return distanceKM; }
    public int getSteps() { return steps; }
    public String getIntensity() { return intensity; }
    public String getGender() { return sex; }
    public double getCalPerStep() {
        return calPerStep;
    }
}
