package calculationModels.basic;

import calculationModels.Workout;

import java.time.LocalDateTime; // ✅ added import


public abstract class BasicWorkout extends Workout {

    public BasicWorkout(double durationMinutes, double weight, LocalDateTime dateTime, double metValue, String basicType) {
        super(durationMinutes, weight, dateTime, metValue);
        this.workoutType = "Basic:" + basicType ;
    }

    public BasicWorkout(double durationMinutes, double weight, String date, double metValue) {
        super(durationMinutes, weight, date, metValue);
        this.workoutType = "Basic:";
    }


}
