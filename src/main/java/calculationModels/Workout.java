package calculationModels;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Workout {

    // 🔹 Common attributes for all workouts
    protected double durationMinutes;
    protected double caloriesBurned;
    protected LocalDateTime startDT;
    protected LocalDateTime endDT;// updated to store both date and time
    protected double metValue;
    protected double initialWeight;
    protected String workoutType;

    protected static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // 🔹 Default constructor
    public Workout() {
        this.durationMinutes = 0;
        this.caloriesBurned = 0;
        this.startDT = LocalDateTime.now(); // default to current date & time
        this.metValue = 0;
        this.initialWeight = 0;
        this.workoutType = "Generic";
      
    }

    // 🔹 Main constructor
    public Workout(double durationMinutes, double weight, LocalDateTime startDT,LocalDateTime endDT ,double metValue) {
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Duration must be greater than 0 minutes.");
        }
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight must be greater than 0 kg.");
        }

        this.durationMinutes = durationMinutes;
        this.initialWeight = weight;
        this.metValue = metValue;
        this.startDT = startDT;
        this.endDT = endDT;
        this.caloriesBurned = 0; // will be handled by subclass
        this.workoutType = "Generic";
    }


    public Workout(double durationMinutes, double weight, String startDTStr, String endDTStr, double metValue) {
        this(durationMinutes, weight, parseDateTime(startDTStr),parseDateTime(endDTStr), metValue);
    }

    private static LocalDateTime parseDateTime(String dateStr) {
        try {
            if (dateStr == null || dateStr.trim().isEmpty()) {
                return LocalDateTime.now();
            }
            return LocalDateTime.parse(dateStr, formatter);
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }

    // 🔹 Abstract method to be implemented by subclasses
    public abstract double calculateCaloriesBurned();

    // 🔹 Getters
    public double getDurationMinutes() { return durationMinutes; }
    public double getCaloriesBurned() { return caloriesBurned; }
    public String getStartDT() { return startDT.format(formatter); } // now returns LocalDateTime
    public String getEndDT() { return endDT.format(formatter); }
    public double getMetValue() { return metValue; }
    public double getInitialWeight() { return initialWeight; }
    public String getWorkoutType() { return workoutType; }



    // 🔹 Setters with validation
    public void setDurationMinutes(double durationMinutes) {
        if (durationMinutes <= 0)
            throw new IllegalArgumentException("Duration must be positive.");
        this.durationMinutes = durationMinutes;
    }

    public void setWeight(double weight) {
        if (weight <= 0)
            throw new IllegalArgumentException("Weight must be positive.");
        this.initialWeight = weight;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.startDT = (dateTime == null) ? LocalDateTime.now() : dateTime;
    }

    public void setWorkoutType(String workoutType) {
        this.workoutType = (workoutType == null || workoutType.trim().isEmpty())
                ? "Generic"
                : workoutType;
    }

}
