package layouts.admin;

import DAO.LocalActDBHelper;
import com.formdev.flatlaf.FlatClientProperties;

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
    private JPanel act2statsPanel;
    private JPanel activityStat;
    private JPanel calLossStat;
    private JPanel weightLossStat;
    private JPanel statsPanel;
    private JPanel activityStatsPanel;
    private JPanel tablesPanel;
    private JPanel fieldsPanel;
    private JPanel localDBPanel;
    private JPanel onlineDBPanel;


    public Activities(){
     createTable();

     activitiesPanel.putClientProperty(FlatClientProperties.STYLE,"arc:20");
     statsPanel.putClientProperty(FlatClientProperties.STYLE,"arc:20");
     act2statsPanel.putClientProperty(FlatClientProperties.STYLE,"arc:20");
     tablesPanel.putClientProperty(FlatClientProperties.STYLE,"arc:20");
     fieldsPanel.putClientProperty(FlatClientProperties.STYLE,"arc:20");
     localDBPanel.putClientProperty(FlatClientProperties.STYLE,"arc:20");
     onlineDBPanel.putClientProperty(FlatClientProperties.STYLE,"arc:20");


    }
    public void createTable() {
        LocalActDBHelper localActDBHelper = new LocalActDBHelper();
        activitiesTable.setModel(localActDBHelper.getActivitiesTable());


    }
    public JPanel getActivitiesPanel() {
        return activitiesPanel;
    }
}
