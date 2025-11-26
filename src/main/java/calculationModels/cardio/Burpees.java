package calculationModels.cardio;

import calculationModels.metrics.MetricsCalculator;
import java.time.LocalDateTime;

public class Burpees extends CardioWorkout {

    private int sets;
    private int reps;
    private int restTimeSeconds;

    public Burpees(double durationMinutes, double weight, LocalDateTime startDT, LocalDateTime endDT,
                   String intensity, int sets, int reps, int restTimeSeconds,
                   double userAge, double currentHeartRate) {

        super(durationMinutes, weight, startDT, endDT, intensity, "Burpees", userAge, currentHeartRate);

        this.sets = sets;
        this.reps = reps;
        this.restTimeSeconds = restTimeSeconds;
        this.caloriesBurned = Math.round(calculateCaloriesBurned() * 100.0) / 100.0;
    }

    @Override
    public double calculateCaloriesBurned() {
        double met;

        if (intensity.equalsIgnoreCase("vigorous")) met = 9.5;
        else if (intensity.equalsIgnoreCase("moderate")) met = 8.0;
        else if (intensity.equalsIgnoreCase("normal")) met = 7.5;
        else met = 7.5;

        if (currentHeartRate > 0 && userAge > 0) {
            double maxHR = 220 - userAge;
            double hrPercent = (currentHeartRate / maxHR) * 100;

            if (hrPercent > 85) met += 1.0;
            else if (hrPercent > 70) met += 0.5;
        }
        double totalRestMinutes = (restTimeSeconds * (sets - 1)) / 60.0;
        double totalActiveMinutes = durationMinutes - totalRestMinutes;

        return MetricsCalculator.calculateCalories(met, initialWeight, Math.max(0, totalActiveMinutes));
    }

    public int getSets() { return sets; }
    public int getReps() { return reps; }
    public int getRestTimeSeconds() { return restTimeSeconds; }


}
