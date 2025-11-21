package calculationModels.basic;

import calculationModels.Workout;

import java.time.LocalDateTime; // ✅ added import


public abstract class BasicWorkout extends Workout {

    public BasicWorkout(double durationMinutes, double weight, LocalDateTime startDT,LocalDateTime endDT, double metValue, String basicType) {
        super(durationMinutes, weight, startDT,endDT, metValue);
        this.workoutType = "Basic:" + basicType ;
    }
}
