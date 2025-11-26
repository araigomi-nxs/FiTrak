package calculationModels.strength;

import calculationModels.metrics.MetricsCalculator;
import java.time.LocalDateTime;

public class PushWorkout extends StrengthWorkout {

    public PushWorkout(double durationMinutes, double bodyWeight,  LocalDateTime startDT, LocalDateTime endDT,
                       int sets, int reps, double weightLiftedKG,
                       String intensity, int restTimeSeconds, boolean useEquipment) {

        super(durationMinutes, bodyWeight, startDT, endDT, sets, reps, weightLiftedKG,
                intensity, restTimeSeconds, "Push", useEquipment);
        this.caloriesBurned = calculateCaloriesBurned();
    }

    @Override
    public double calculateCaloriesBurned() {
        double totalRestMinutes = (sets > 1) ? (restTimeSeconds / 60.0) * (sets - 1) : 0;
        double activeMinutes = durationMinutes - totalRestMinutes;
        if (activeMinutes < 0) activeMinutes = durationMinutes;

        double totalWeightMoved = isWeighted
                ? (weightLiftedKG > 0 ? sets * reps * weightLiftedKG : sets * reps * initialWeight * 0.5)
                : sets * reps * initialWeight * 0.3;

        /*
        if (isWeighted) {
            if (weightLiftedKG > 0) {
                totalWeightMoved = sets * reps * weightLiftedKG;
            } else {
                totalWeightMoved = sets * reps * weight * 0.5;
            }
        } else {
            totalWeightMoved = sets * reps * weight * 0.3;
        }
        */

        if (totalWeightMoved < 1000) metValue = 3.8;
        else if (totalWeightMoved < 5000) metValue = 5.2;
        else metValue = 6.8;

        if (intensity.equalsIgnoreCase("light")) metValue -= 0.2;
        else if (intensity.equalsIgnoreCase("moderate")) metValue += 0.3;
        else if (intensity.equalsIgnoreCase("vigorous")) metValue += 0.7;

        if (isWeighted) metValue += 0.4;

        return MetricsCalculator.calculateCalories(metValue, initialWeight, activeMinutes);
    }
}
