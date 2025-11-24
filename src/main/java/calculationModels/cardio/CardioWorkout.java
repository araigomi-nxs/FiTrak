package calculationModels.cardio;

import calculationModels.Workout;
import calculationModels.metrics.MetricsCalculator;
import java.time.LocalDateTime;

public abstract class CardioWorkout extends Workout {

    protected String cardioType;
    protected String intensity;
    protected double userAge;
    protected double currentHeartRate;

    public CardioWorkout(double durationMinutes, double weight, LocalDateTime startDT, LocalDateTime endDT,
                         String intensity, String cardioType,
                         double userAge, double currentHeartRate) {

        super(durationMinutes, weight, startDT,endDT, 0.0);
        this.intensity = (intensity == null || intensity.trim().isEmpty()) ? "moderate" : intensity;
        this.cardioType = (cardioType == null || cardioType.trim().isEmpty()) ? "Cardio:Generic" : "Cardio:"+ cardioType;
        this.userAge = userAge;
        this.currentHeartRate = currentHeartRate;
        this.workoutType = "Cardio (" + this.cardioType + ")";
    }


//    public String getHeartRateZone() {
//        return MetricsCalculator.getHeartRateZone(userAge, currentHeartRate);
//    }
//
//    public String getCardioType() { return cardioType; }
    public String getIntensity() { return intensity; }

}
