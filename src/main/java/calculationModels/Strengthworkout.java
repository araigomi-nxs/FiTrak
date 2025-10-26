package calculationModels;

public class Strengthworkout extends Workout {

    private int sets;
    private int reps;
    private double weightLiftedKG;
    private String intensity;
    private int restTimeSeconds;
    private String muscleGroup;

    public Strengthworkout(double duration, double weight, String date,
                           int sets, int reps, double weightLiftedKG,
                           String intensity, int restTimeSeconds, String muscleGroup, int withWeights) {

        super(duration, weight, date, 0.0);

        this.sets = sets;
        this.reps = reps;
        this.weightLiftedKG = weightLiftedKG;
        this.intensity = intensity;
        this.restTimeSeconds = restTimeSeconds;
        this.muscleGroup = muscleGroup;

        double initiaTotallWeight = sets * reps;

        if( withWeights == 1)
        {
            double totalWeightMoved = initiaTotallWeight * weightLiftedKG;
            if (totalWeightMoved < 1000) {
                metValue = 3.5;
            } else if (totalWeightMoved < 5000) {
                metValue = 5.0;
            } else {
                metValue = 6.0;

            }
        }
        else


        switch (muscleGroup.toLowerCase()) {
            case "push":  
                metValue += 0.3;
                break;
            case "pull": 
                metValue += 0.5;
                break;
            case "legs": 
                metValue += 1.0;
                break;
            default:
                break;
        }

        if (intensity.equalsIgnoreCase("vigorous")) {
            metValue += 0.5;
        }

        this.caloriesBurned = calculateCaloriesBurned();
    }

    @Override
    public double calculateCaloriesBurned() {
        double totalRestMinutes = (restTimeSeconds / 60.0) * (sets - 1);
        double activeMinutes = durationMinutes - totalRestMinutes;
        if (activeMinutes < 0) activeMinutes = durationMinutes; 

        double hours = activeMinutes / 60.0;
        return metValue * weight * hours;
    }

    public int getSets() {
        return sets;
    }

    public int getReps() {
        return reps;
    }

    public double getWeightLiftedKG() {
        return weightLiftedKG;
    }

    public String getIntensity() {
        return intensity;
    }

    public int getRestTimeSeconds() {
        return restTimeSeconds;
    }

    public String getMuscleGroup() {
        return muscleGroup;
    }
}
