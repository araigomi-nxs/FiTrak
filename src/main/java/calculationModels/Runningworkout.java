package calculationModels;

public class Runningworkout extends Workout {

    private double distanceKM;
    private String intensity;
    private String terrain;
    private String speedLevel; 

    public Runningworkout(double duration, double weight, String date,
                          double distanceKM, String intensity, String terrain, String speedLevel) {

        super(duration, weight, date, calculateMet(intensity, terrain, speedLevel));

        this.distanceKM = distanceKM;
        this.intensity = intensity;
        this.terrain = terrain;
        this.speedLevel = speedLevel;
        this.caloriesBurned = calculateCaloriesBurned();
    }

    private static double calculateMet(String intensity, String terrain, String speedLevel) {
        double met;

        switch (speedLevel.toLowerCase()) {
            case "fast":
                met = 11.5;
                break;
            case "moderate":
                met = 9.8;
                break;
            case "slow":
            default:
                met = 8.3;
                break;
        }

        if (intensity.equalsIgnoreCase("vigorous")) met += 0.5;
        else if (intensity.equalsIgnoreCase("light")) met -= 0.3;

        if (terrain.equalsIgnoreCase("uphill")) met += 1.0;
        else if (terrain.equalsIgnoreCase("trail")) met += 0.5;

        return met;
    }

    @Override
    public double calculateCaloriesBurned() {
        double hours = durationMinutes / 60.0;
        return metValue * weight * hours;
    }
}
