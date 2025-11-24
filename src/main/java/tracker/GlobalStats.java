package tracker;

import DAO.LocalActDBHelper;

public class GlobalStats {
    private  static LocalActDBHelper localActDBHelper = new LocalActDBHelper();

    public static double getGlobalCalLoss()
    {
        return localActDBHelper.getClobalCalBurn();
    }
}
