package tracker;

import DAO.LocalActDBHelper;
import DAO.LocalWorkoutDBHelper;
import calculationModels.Workout;
import calculationModels.basic.RunningWorkout;
import calculationModels.basic.WalkingWorkout;
import objects.Account;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WorkoutTracker {

    private final static String SERVER_ORIGIN = "Client-ARN-PC-002";
    protected static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static LocalActDBHelper localActDBHelper ;
    private static LocalWorkoutDBHelper localWorkoutDBHelper;
    private static LocalDateTime localDateTime;

    public static void logWorkout(long userID, WalkingWorkout walkingWorkout){

        localActDBHelper = new LocalActDBHelper();
        localWorkoutDBHelper = new LocalWorkoutDBHelper();
        localDateTime = LocalDateTime.now();

        long activityID = localActDBHelper.insertActivity(userID,walkingWorkout.getDurationMinutes(),
                walkingWorkout.getCaloriesBurned(), walkingWorkout.getStartDT(),walkingWorkout.getEndDT(),
                walkingWorkout.getMetValue(),walkingWorkout.getInitialWeight(),walkingWorkout.getWorkoutType(),SERVER_ORIGIN);

        localWorkoutDBHelper.insertWorkout( activityID, walkingWorkout.getSteps(), walkingWorkout.getDistanceKM(), walkingWorkout.getIntensity(),
                walkingWorkout.getCalPerStep(), 0.0,0, 0,0.0,0.0, SERVER_ORIGIN,
                localDateTime.format(formatter));
    }

    public static void logWorkout(long userID, RunningWorkout runningWorkout){


    }


    // group activities - summarize 20-11-25 1500 calburned today -dashboard
    // goal based - pre defined dates
}

//package tracker;
//
//import DAO.LocalActDBHelper;
//import DAO.LocalWorkoutDBHelper;
//import calculationModels.Workout;
//import calculationModels.basic.RunningWorkout;
//import calculationModels.basic.WalkingWorkout;
//import objects.Account;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//public class WorkoutTracker {
//
//
//    private final static String SERVER_ORIGIN = "Client-ARN-PC-002";
//    protected static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//
//
//    public static void logWorkout(long userID, WalkingWorkout walkingWorkout){
//
//        LocalActDBHelper localActDBHelper = new LocalActDBHelper();
//        LocalWorkoutDBHelper localWorkoutDBHelper = new LocalWorkoutDBHelper();
//        LocalDateTime localDateTime = LocalDateTime.now();
//
//        localWorkoutDBHelper.insertWorkout(localActDBHelper.insertActivity(userID,walkingWorkout.getDurationMinutes(),
//                        walkingWorkout.getCaloriesBurned(), walkingWorkout.getStartDT(),walkingWorkout.getEndDT(),
//                        walkingWorkout.getMetValue(),walkingWorkout.getInitialWeight(),walkingWorkout.getWorkoutType(),SERVER_ORIGIN),
//                walkingWorkout.getSteps(), walkingWorkout.getDistanceKM(), walkingWorkout.getIntensity(),
//                walkingWorkout.getCalPerStep(), 0.0,0, 0,0.0,0.0, SERVER_ORIGIN,
//                localDateTime.format(formatter));
//
//    }
//    public static void logWorkout(long userID, RunningWorkout runningWorkout){}
//
//
//    // group activities - summarize 20-11-25 1500 calburned today -dashboard
//    // goal based - pre defined dates
//}
