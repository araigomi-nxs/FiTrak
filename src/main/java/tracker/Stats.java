
package tracker;

import DAO.LocalActDBHelper;
import DAO.LocalDataBaseHelper;
import DAO.LocalWorkoutDBHelper;
import DAO.OnlineDataBaseHelper;

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


}
