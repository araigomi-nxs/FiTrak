package calculationModels.cardio;

import calculationModels.metrics.MetricsCalculator;
import java.time.LocalDateTime;

public class JumpingJacks extends CardioWorkout {

    private int sets;
    private int reps;
    private int restTimeSeconds;
    private boolean useReps;

    public JumpingJacks(double durationMinutes, double weight,  LocalDateTime startDT, LocalDateTime endDT,
                        String intensity, int sets, int reps, int restTimeSeconds, boolean useReps,
                        double userAge, double currentHeartRate) {

        super(durationMinutes, weight, startDT,endDT, intensity, "JumpingJacks", userAge, currentHeartRate);
        this.sets = sets;
        this.reps = reps;
        this.restTimeSeconds = restTimeSeconds;
        this.useReps = useReps;
        this.caloriesBurned = calculateCaloriesBurned();
    }

    @Override
    public double calculateCaloriesBurned() {
        double met;

        if (intensity.equalsIgnoreCase("vigorous")) met = 10.0;
        else if (intensity.equalsIgnoreCase("moderate") || intensity.equalsIgnoreCase("normal")) met = 8.0;
        else met = 7.0;

        if (currentHeartRate > 0 && userAge > 0) {
            double maxHR = 220 - userAge;
            double hrPercent = (currentHeartRate / maxHR) * 100;

            if (hrPercent > 85) met += 1.0;
            else if (hrPercent > 70) met += 0.5;
        }

        if (useReps && reps > 0) {
            double totalReps = sets * reps;
            double repsPerMin = totalReps / (durationMinutes * sets);
            if (repsPerMin > 100) met += 0.3;
        }

        double totalRestMinutes = (restTimeSeconds * (sets - 1)) / 60.0;
        double totalActiveMinutes = (durationMinutes * sets) - totalRestMinutes;

        return MetricsCalculator.calculateCalories(met, weight, Math.max(0, totalActiveMinutes));
    }

    public int getSets() { return sets; }
    public int getReps() { return reps; }
    public int getRestTimeSeconds() { return restTimeSeconds; }
    public boolean isUseReps() { return useReps; }
}
