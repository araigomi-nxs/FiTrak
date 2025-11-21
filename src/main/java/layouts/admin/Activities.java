package layouts.admin;

import DAO.LocalActDBHelper;

import javax.swing.*;

public class Activities {
    private JPanel activitiesPanel;
    private JTable activitiesTable;
    private JTextField actIDField;
    private JTextField userIDField;
    private JTextField durationFIeld;
    private JTextField calBurnField;
    private JTextField startDTField;
    private JTextField endDTField;
    private JTextField metValField;
    private JTextField serverOriginFIeld;
    private JTextField initialWeightField;
    private JTextField workoutTypeField;
    private JTextField weightLossField;
    private JButton updateButton;
    private JButton insertButton;
    private JButton removeButton;
    private JButton clearFields;
    private JTextField searchField;
    private JLabel searchButton;
    private JLabel refreshTable;
    private JPanel statsPanel;
    private JPanel calLossStat;
    private JPanel weightLossStat;
    private JPanel activityStat;
    private JPanel accountStat;
    private JLabel accCounter;
    private JLabel adminCounter;
    private JLabel userCounter;
    private JLabel localCount;
    private JLabel foreignCount;


    public Activities(){
     createTable();


    }
    public void createTable() {
        LocalActDBHelper localActDBHelper = new LocalActDBHelper();
        activitiesTable.setModel(localActDBHelper.getActivitiesTable());


    }
    public JPanel getActivitiesPanel() {
        return activitiesPanel;
    }
}
