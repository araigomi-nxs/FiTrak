package calculationModels.strength;

import calculationModels.Workout;
import calculationModels.metrics.MetricsCalculator;
import java.time.LocalDateTime;

public abstract class StrengthWorkout extends Workout {

    protected int sets;
    protected int reps;
    protected double weightLiftedKG;
    protected String intensity;
    protected int restTimeSeconds;
    protected String muscleGroup;
    protected boolean isWeighted;

    public StrengthWorkout(double durationMinutes, double weight, LocalDateTime dateTime,
                           int sets, int reps, double weightLiftedKG,
                           String intensity, int restTimeSeconds, String muscleGroup, boolean isWeighted) {

        super(durationMinutes, weight, dateTime, 0.0);
        this.sets = sets;
        this.reps = reps;
        this.weightLiftedKG = weightLiftedKG;
        this.intensity = intensity;
        this.restTimeSeconds = restTimeSeconds;
        this.muscleGroup = muscleGroup.toLowerCase();
        this.isWeighted = isWeighted;
        this.workoutType = "Strength:" + this.muscleGroup;
        this.caloriesBurned = calculateCaloriesBurned();
    }

    public int getSets() { return sets; }
    public int getReps() { return reps; }
    public double getWeightLiftedKG() { return weightLiftedKG; }
    public int getRestTimeSeconds() { return restTimeSeconds; }
    public String getIntensity() { return intensity; }
    public String getMuscleGroup() { return muscleGroup; }
    public boolean isWeighted() { return isWeighted; }
}
