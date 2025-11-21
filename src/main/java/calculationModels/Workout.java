package calculationModels;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Workout {

    // 🔹 Common attributes for all workouts
    protected double durationMinutes;
    protected double caloriesBurned;
    protected LocalDateTime dateTime;   // updated to store both date and time
    protected double metValue;
    protected double weight;
    protected String workoutType;

    protected static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // 🔹 Default constructor
    public Workout() {
        this.durationMinutes = 0;
        this.caloriesBurned = 0;
        this.dateTime = LocalDateTime.now(); // default to current date & time
        this.metValue = 0;
        this.weight = 0;
        this.workoutType = "Generic";
        //this.userAge = 0;
    }

    // 🔹 Main constructor
    public Workout(double durationMinutes, double weight, LocalDateTime dateTime, double metValue) {
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Duration must be greater than 0 minutes.");
        }
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight must be greater than 0 kg.");
        }

        this.durationMinutes = durationMinutes;
        this.weight = weight;
        this.metValue = metValue;
        this.dateTime = (dateTime == null) ? LocalDateTime.now() : dateTime;
        this.caloriesBurned = 0; // will be handled by subclass
        this.workoutType = "Generic";
    }


    public Workout(double durationMinutes, double weight, String dateStr, double metValue) {
        this(durationMinutes, weight, parseDateTime(dateStr), metValue);
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
    public LocalDateTime getDateTime() { return dateTime; } // now returns LocalDateTime
    public String getFormattedDateTime() { return dateTime.format(formatter); } // for display
    public double getMetValue() { return metValue; }
    public double getWeight() { return weight; }
    public String getWorkoutType() { return workoutType; }
    //public double getUserAge() { return userAge; }


    // 🔹 Setters with validation
    public void setDurationMinutes(double durationMinutes) {
        if (durationMinutes <= 0)
            throw new IllegalArgumentException("Duration must be positive.");
        this.durationMinutes = durationMinutes;
    }

    public void setWeight(double weight) {
        if (weight <= 0)
            throw new IllegalArgumentException("Weight must be positive.");
        this.weight = weight;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = (dateTime == null) ? LocalDateTime.now() : dateTime;
    }

    public void setWorkoutType(String workoutType) {
        this.workoutType = (workoutType == null || workoutType.trim().isEmpty())
                ? "Generic"
                : workoutType;
    }

}
