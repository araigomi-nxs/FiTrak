package calculationModels.cardio;

import calculationModels.metrics.MetricsCalculator;
import java.time.LocalDateTime;

public class JumpingRope extends CardioWorkout {

    private int sets;
    private int reps;
    private int restTimeSeconds;
    private boolean useReps;

    public JumpingRope(double durationMinutes, double weight,  LocalDateTime startDT, LocalDateTime endDT,
                       String intensity, int sets, int reps, int restTimeSeconds, boolean useReps,
                       double userAge, double currentHeartRate) {

        super(durationMinutes, weight, startDT,endDT, intensity, "JumpRope", userAge, currentHeartRate);
        this.sets = sets;
        this.reps = reps;
        this.restTimeSeconds = restTimeSeconds;
        this.useReps = useReps;
        this.caloriesBurned = Math.round(calculateCaloriesBurned() * 100.0) / 100.0;
    }

    @Override
    public double calculateCaloriesBurned() {
        double met;

        if (intensity.equalsIgnoreCase("vigorous")) {
            met = 12.0;
        } else if (intensity.equalsIgnoreCase("moderate")) {
            met = 9.0;
        } else if (intensity.equalsIgnoreCase("normal")) {
            met = 8.0;
        } else {
            met = 8.0;
        }

        if (currentHeartRate > 0 && userAge > 0) {
            double maxHR = 220 - userAge;
            double hrPercent = (currentHeartRate / maxHR) * 100;
            if (hrPercent > 85) met += 1.0;
            else if (hrPercent > 70) met += 0.5;
        }

        if (useReps && reps > 0) {
            double repsPerMin = reps / durationMinutes;
            if (repsPerMin > 60) met += 0.5;
        }

        double totalRestMinutes = (restTimeSeconds * (sets - 1)) / 60.0;
        double totalActiveMinutes = durationMinutes - totalRestMinutes;

        return MetricsCalculator.calculateCalories(met, initialWeight, Math.max(0, totalActiveMinutes));
    }

    public int getSets() { return sets; }
    public int getReps() { return reps; }
    public int getRestTimeSeconds() { return restTimeSeconds; }
    public boolean isUseReps() { return useReps; }

}
