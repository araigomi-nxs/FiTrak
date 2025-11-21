package calculationModels.basic;

import calculationModels.metrics.MetricsCalculator;
import java.time.LocalDateTime;

public class CyclingWorkout extends BasicWorkout {

    private double distanceKM;
    private double speedKPH;
    private String intensity;

    public CyclingWorkout(double durationMinutes, double weight, LocalDateTime dateTime,
                          double distanceKM, String intensity) {

        super(durationMinutes, weight, dateTime, 0.0 , "Cycling"  );

        this.distanceKM = distanceKM;
        this.speedKPH = calculateSpeed(distanceKM, durationMinutes);
        this.intensity = intensity;
        this.caloriesBurned = MetricsCalculator.calculateCalories(metValue, weight, durationMinutes);
    }

    private static double calculateSpeed(double distanceKM, double durationMinutes) {
        double hours = durationMinutes / 60.0;
        if (hours <= 0) return 0;
        return distanceKM / hours;
    }

    private static double calculateMet(double distanceKM, double durationMinutes, String intensity) {
        double avgSpeed = calculateSpeed(distanceKM, durationMinutes);
        double met;

        if (avgSpeed < 16) met = 4.0;
        else if (avgSpeed <= 20) met = 6.8;
        else if (avgSpeed <= 25) met = 8.0;
        else met = 10.0;

        if ("vigorous".equalsIgnoreCase(intensity)) {
            met += 1.0;
        } else if ("normal".equalsIgnoreCase(intensity) || "moderate".equalsIgnoreCase(intensity)) {
        met += 0.0;
        }

        return met;
    }

    @Override
    public double calculateCaloriesBurned() {
        metValue= calculateMet(distanceKM, durationMinutes, intensity);
        return MetricsCalculator.calculateCalories(metValue, weight, durationMinutes);
    }

    // Getters
    public double getDistanceKM() {
        return distanceKM;
    }

    public double getSpeedKPH() {
        return speedKPH;
    }

    public String getIntensity() {
        return intensity;
    }
}
