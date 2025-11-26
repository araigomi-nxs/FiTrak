package tracker;

import DAO.Config;
import DAO.LocalActDBHelper;
import DAO.LocalWorkoutDBHelper;
import calculationModels.Workout;
import calculationModels.basic.CyclingWorkout;
import calculationModels.basic.RunningWorkout;
import calculationModels.basic.WalkingWorkout;
import calculationModels.cardio.Burpees;
import calculationModels.cardio.CardioWorkout;
import calculationModels.cardio.JumpingJacks;
import calculationModels.cardio.JumpingRope;
import calculationModels.strength.LegWorkout;
import calculationModels.strength.PullWorkout;
import calculationModels.strength.PushWorkout;
import calculationModels.strength.StrengthWorkout;
import objects.Account;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WorkoutTracker {

    private final static String SERVER_ORIGIN = Config.get("SERVER_ORIGIN");
    protected static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static LocalActDBHelper localActDBHelper;
    private static LocalWorkoutDBHelper localWorkoutDBHelper;
    private static LocalDateTime localDateTime;

    public static void logWorkout(long userID, WalkingWorkout workout) {

        localActDBHelper = new LocalActDBHelper();
        localWorkoutDBHelper = new LocalWorkoutDBHelper();
        localDateTime = LocalDateTime.now();

        localWorkoutDBHelper.insertWorkout(  logActivity(userID, workout)   , workout.getSteps(),rnD(workout.getDistanceKM()), workout.getIntensity(),
                rnD(workout.getCalPerStep()), 0.0, 0, 0, 0.0, 0.0, SERVER_ORIGIN,
                localDateTime.format(formatter));
    }


    public static void logWorkout(long userID, RunningWorkout workout) {

        localActDBHelper = new LocalActDBHelper();
        localWorkoutDBHelper = new LocalWorkoutDBHelper();
        localDateTime = LocalDateTime.now();

        localWorkoutDBHelper.insertWorkout(  logActivity(userID, workout)   , 0, rnD(workout.getDistanceKM()), workout.getIntensity(),
                0, rnD(workout.getSpeedKPH()), 0, 0, 0.0, 0.0, SERVER_ORIGIN,
                localDateTime.format(formatter));
    }


    ///


    public static  void logWorkout(long userID, CyclingWorkout workout) {
        localActDBHelper = new LocalActDBHelper();
        localWorkoutDBHelper = new LocalWorkoutDBHelper();
        localDateTime = LocalDateTime.now();

        localWorkoutDBHelper.insertWorkout(  logActivity(userID, workout)   , 0, rnD(workout.getDistanceKM()), workout.getIntensity(),
                0, rnD(workout.getSpeedKPH()), 0, 0, 0.0, 0.0, SERVER_ORIGIN,
                localDateTime.format(formatter));
    }

    public static  void logWorkout(long userID, Burpees workout) {
        localActDBHelper = new LocalActDBHelper();
        localWorkoutDBHelper = new LocalWorkoutDBHelper();
        localDateTime = LocalDateTime.now();

        localWorkoutDBHelper.insertWorkout(  logActivity(userID, workout)   , 0,0.0, workout.getIntensity(),
                0, 0.0, workout.getSets(), workout.getReps(), rnD(workout.getCurrentHeartRate()), 0.0, SERVER_ORIGIN,
                localDateTime.format(formatter));
    }
    public static  void logWorkout(long userID, JumpingJacks workout) {

        localActDBHelper = new LocalActDBHelper();
        localWorkoutDBHelper = new LocalWorkoutDBHelper();
        localDateTime = LocalDateTime.now();

        localWorkoutDBHelper.insertWorkout(  logActivity(userID, workout)   , 0,0.0, workout.getIntensity(),
                0, 0.0, workout.getSets(), workout.getReps(), rnD(workout.getCurrentHeartRate()), 0.0, SERVER_ORIGIN,
                localDateTime.format(formatter));
    }
    public static  void logWorkout(long userID, JumpingRope workout) {
        localActDBHelper = new LocalActDBHelper();
        localWorkoutDBHelper = new LocalWorkoutDBHelper();
        localDateTime = LocalDateTime.now();

        localWorkoutDBHelper.insertWorkout(  logActivity(userID, workout)   , 0,0.0, workout.getIntensity(),
                0, 0.0, workout.getSets(), workout.getReps(), rnD(workout.getCurrentHeartRate()), 0.0, SERVER_ORIGIN,
                localDateTime.format(formatter));
    }


    public static  void logWorkout(long userID, LegWorkout workout) {
        localActDBHelper = new LocalActDBHelper();
        localWorkoutDBHelper = new LocalWorkoutDBHelper();
        localDateTime = LocalDateTime.now();

        localWorkoutDBHelper.insertWorkout(  logActivity(userID, workout)   , 0,0.0, workout.getIntensity(),
                0, 0.0, workout.getSets(), workout.getReps(), 0.0, rnD(workout.getWeightLiftedKG()), SERVER_ORIGIN,
                localDateTime.format(formatter));

    }
    public static  void logWorkout(long userID, PullWorkout workout) {
        localActDBHelper = new LocalActDBHelper();
        localWorkoutDBHelper = new LocalWorkoutDBHelper();
        localDateTime = LocalDateTime.now();

        localWorkoutDBHelper.insertWorkout(  logActivity(userID, workout)   , 0,0.0, workout.getIntensity(),
                0, 0.0, workout.getSets(), workout.getReps(), 0.0, rnD(workout.getWeightLiftedKG()), SERVER_ORIGIN,
                localDateTime.format(formatter));

    }
    public static  void logWorkout(long userID, PushWorkout workout) {
        localActDBHelper = new LocalActDBHelper();
        localWorkoutDBHelper = new LocalWorkoutDBHelper();
        localDateTime = LocalDateTime.now();

        localWorkoutDBHelper.insertWorkout(  logActivity(userID, workout)   , 0,0.0, workout.getIntensity(),
                0, 0.0, workout.getSets(), workout.getReps(), 0.0, rnD(workout.getWeightLiftedKG()), SERVER_ORIGIN,
                localDateTime.format(formatter));
    }



    private static long logActivity(long userID, Workout workout)
    {
        //returns activity ID
        return localActDBHelper.insertActivity(userID, rnD(workout.getDurationMinutes()),
                rnD(workout.getCaloriesBurned()), workout.getStartDT(), workout.getEndDT(),
                rnD( workout.getMetValue()),rnD( workout.getInitialWeight()), workout.getWorkoutType(), SERVER_ORIGIN);

    }

    private static double rnD(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

}
// group activities - summarize 20-11-25 1500 calburned today -dashboard
// goal based - pre defined dates