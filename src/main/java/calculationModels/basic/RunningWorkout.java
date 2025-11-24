package calculationModels.basic;

import calculationModels.metrics.MetricsCalculator;
import java.time.LocalDateTime;

public class RunningWorkout extends BasicWorkout {

    private double distanceKM;
    private double speedKPH;
    private String intensity;
    private String terrain;

    public RunningWorkout(double durationMinutes, double weight, LocalDateTime startDT,LocalDateTime endDT,
                          double distanceKM, String intensity, String terrain) {

        super(durationMinutes, weight, startDT, endDT, 0, "Running" );

        this.distanceKM = distanceKM;
        this.speedKPH = calculateSpeed(distanceKM, durationMinutes);
        this.intensity = intensity;
        this.terrain = terrain;
        this.caloriesBurned = calculateCaloriesBurned();


    }
    private static double calculateMet(String intensity, String terrain, double speed) {
        double met = speed * 1.5 ; // base MET for running

        if ("vigorous".equalsIgnoreCase(intensity)) met += 0.5;
        if ("uphill".equalsIgnoreCase(terrain)) met += 1.0;
        else if ("trail".equalsIgnoreCase(terrain)) met += 0.3;

        return met;
    }
    private static double calculateSpeed(double distanceKM, double durationMinutes) {
        double hours = durationMinutes / 60.0;
        if (hours <= 0) return 0;
        return distanceKM / hours;
    }

    @Override
    public double calculateCaloriesBurned() {
        metValue= calculateMet(intensity, terrain, speedKPH);
        return MetricsCalculator.calculateCalories(metValue, initialWeight, durationMinutes);
    }

    // Getters
    public double getDistanceKM() { return distanceKM; }
    public double getSpeedKPH() { return speedKPH; }
    public String getIntensity() { return intensity; }
    public String getTerrain() { return terrain; }
}
