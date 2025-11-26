
package tracker;

import DAO.LocalActDBHelper;
import DAO.LocalDataBaseHelper;
import DAO.LocalWorkoutDBHelper;
import DAO.OnlineDataBaseHelper;
import calculationModels.metrics.MetricsCalculator;

public class Stats {
    private  static LocalActDBHelper localActDBHelper = new LocalActDBHelper();
    private static LocalDataBaseHelper localDataBaseHelper = new LocalDataBaseHelper();
    private static LocalWorkoutDBHelper localWorkoutDBHelper = new LocalWorkoutDBHelper();
    private static OnlineDataBaseHelper onlineDataBaseHelper = new OnlineDataBaseHelper();

    public static double getGlobalCalLoss()
    {
        return localActDBHelper.getClobalCalBurn();
    }


    //online table rows
    public static int getOnlineTableCount(String tableName)
    {

        return switch (tableName) {
            case "workouts" -> onlineDataBaseHelper.getWorkoutsRowCountOnline();
            case "activities" -> onlineDataBaseHelper.getActivitiesRowCountOnline();
            case "accounts" -> onlineDataBaseHelper.getAccountsRowCountOnline();
            default -> 0;
        };
    }

    public static int getEntryCount(String tableName)
    {
        return  switch (tableName)
        {
            case "workouts" -> localWorkoutDBHelper.getWorkoutCount();
            case "activities" -> localActDBHelper.getActivityCount();
            case "accounts" -> localDataBaseHelper.getRowCount(0);
            default -> 0;
        };
    }

    public static double getAccountAverage(String column)
    {
        return switch (column){
            case "weight" -> localDataBaseHelper.getAverages("weight");
            case "height" -> localDataBaseHelper.getAverages("height");
            case "bmi" -> localDataBaseHelper.getAverages("bmi");
            default -> 0;
        };
    }



    public static int getLocalCount(String tableName)
    {
        return  switch (tableName)
                {
                    case "workouts" -> localWorkoutDBHelper.getLocalCount();
                    case "activities" -> localActDBHelper.getLocalActivityCount();
                    case "accounts" -> localDataBaseHelper.getRowCount(3);
                    default -> 0;
                };
    }


    public  static int getLimboCount(String tableName)
    {

        return switch (tableName) {
            case "workouts" -> localWorkoutDBHelper.getDeletedLinkedWorkoutCount();
            case "activities" -> localActDBHelper.getDeletedActivitiesCount();
            case "accounts" -> localDataBaseHelper.getRowCount(4);
            default -> 0;
        };
    }

    public static double getGlobalWtLoss() {
        return MetricsCalculator.computeFatLoss( Stats.getGlobalCalLoss() );
    }
}
