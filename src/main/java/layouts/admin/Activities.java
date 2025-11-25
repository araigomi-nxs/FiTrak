package layouts.admin;

import DAO.LocalActDBHelper;
import DAO.OnlineDataBaseHelper;
import DAO.test.SyncAccManager;
import DAO.test.SyncActManager;
import com.formdev.flatlaf.FlatClientProperties;
import tracker.GlobalStats;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private JButton insertButton;
    private JButton removeButton;
    private JTextField searchField;
    private JLabel searchButton;
    private JLabel refreshOnlineTbale;
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
    private JLabel refreshButton;
    private JLabel matches;
    private JTextField workoutIDField;
    private JTextField data1field;
    private JLabel data1Label;
    private JLabel data2Label;
    private JLabel data3Label;
    private JLabel data4Label;
    private JTextField data2field;
    private JTextField data3field;
    private JTextField data4field;
    private JTextField logDTField;
    private JLabel globCalCount;
    private JLabel globWtLosCount;
    private JLabel activityCounter;
    private JLabel basicCounter;
    private JLabel cardioCounter;
    private JLabel strengthCounter;
    private JTable onlineActivitiesTable;
    private JButton syncButton;
    private LocalActDBHelper localActDBHelper;
    protected static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Activities(){
         createTable();

         JPopupMenu popupMenu = new JPopupMenu();
         JMenuItem editItem = new JMenuItem("Edit");
         popupMenu.add(editItem);
         activitiesTable.setComponentPopupMenu(popupMenu);


         arcSetup();
         setStats();

         editItem.addActionListener(e -> {
             int selectedRow = activitiesTable.getSelectedRow();
             if (selectedRow != -1) {

                 actIDField.setText(activitiesTable.getValueAt(selectedRow, 0).toString());
                 userIDField.setText(activitiesTable.getValueAt(selectedRow, 1).toString());
                 workoutTypeField.setText(activitiesTable.getValueAt(selectedRow, 2).toString());
                 durationFIeld.setText(activitiesTable.getValueAt(selectedRow, 3).toString());
                 calBurnField.setText(activitiesTable.getValueAt(selectedRow, 4).toString());
                 startDTField.setText(activitiesTable.getValueAt(selectedRow, 5).toString());
                 endDTField.setText(activitiesTable.getValueAt(selectedRow, 6).toString());
                 metValField.setText(activitiesTable.getValueAt(selectedRow, 7).toString());
                 serverOriginFIeld.setText(activitiesTable.getValueAt(selectedRow, 8).toString());
                 initialWeightField.setText(activitiesTable.getValueAt(selectedRow, 9).toString());

                 LocalActDBHelper localActDBHelper = new LocalActDBHelper();

                 List<Object[]> workout = new ArrayList<>();

                 workout = localActDBHelper.getWorkoutData(workoutTypeField.getText(), Integer.parseInt(actIDField.getText().trim()));

                 setWorkoutField(workoutTypeField.getText());
                 for (Object[] row : workout) {

                         data1field.setText(row[0].toString());
                         data2field.setText(row[1].toString());
                         data3field.setText(row[2].toString());
                         data4field.setText(row[3].toString());
                         logDTField.setText(row[4].toString());

                 }



             }
         });




         searchButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    localActDBHelper = new LocalActDBHelper();
                    activitiesTable.setModel(localActDBHelper.searchByUserID(parseOrZero(searchField.getText().trim())));
                    matches.setText(activitiesTable.getModel().getRowCount() + " matches found.");

                }
            });

         refreshButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    createTable();
                }
         });
         removeButton.addMouseListener(new MouseAdapter() {
             @Override
                public void mouseClicked(MouseEvent e) {
                 localActDBHelper = new LocalActDBHelper();
                LocalDateTime localDateTime = LocalDateTime.now();
                localActDBHelper.deleteActivity(Integer.parseInt(actIDField.getText().trim()),localDateTime.format(formatter) );
                createTable();
             }
         });

         refreshOnlineTbale.addMouseListener(new MouseAdapter() {

             @Override
             public void mouseClicked(MouseEvent e) {

                 createTable();
             }
         });
         syncButton.addMouseListener(new MouseAdapter() {
             @Override
             public void mouseClicked(MouseEvent e) {
                SyncActManager syncActManager = new SyncActManager();
                syncActManager.startActSyncThread();
                createTable();
             }
         });

    }

    private void setStats() {
        Map<String, Integer> categoryCount = new HashMap<>();
        globCalCount.setText( String.valueOf(GlobalStats.getGlobalCalLoss()));
        localActDBHelper = new LocalActDBHelper();
        activityCounter.setText(String.valueOf(localActDBHelper.getActivityCount()));

        categoryCount = localActDBHelper.countActivitiesByCategory();
        basicCounter.setText(String.valueOf(categoryCount.getOrDefault("Basic", 0)));

        cardioCounter.setText(String.valueOf(categoryCount.getOrDefault("Cardio",0)));
        strengthCounter.setText(String.valueOf(categoryCount.getOrDefault("Strength",0)));


    }

    private void setWorkoutField(String wrokoutType) {
        switch (wrokoutType) {
            case "Walking":
                data1Label.setText("Steps");
                data2Label.setText("DistanceKM");
                data3Label.setText("Intensity");
                data4Label.setText("CalPerStep");
                break;
            case "Running":
                data1Label.setText("SpeedKPH");
                data2Label.setText("DistanceKM");
                data3Label.setText("Intensity");
                data4Label.setText("Terrain");
                break;
            case "Cycling":
                data1Label.setText("SpeedKPH");
                data2Label.setText("DistanceKM");
                data3Label.setText("Intensity");
                data4Label.setText("");
                break;
            case "Cardio:Burpees", "Cardio:JumpingJacks", "Cardio:JumpRope":
                data1Label.setText("Sets");
                data2Label.setText("Reps");
                data3Label.setText("Intensity");
                data4Label.setText("CurrentHeartRate");
                break;

            case "Strength:Leg", "Strength:Pull", "Strength:Push":
                data1Label.setText("Sets");
                data2Label.setText("Reps");
                data3Label.setText("Intensity");
                data4Label.setText("WeightLifted");
                break;
            default:
                break;
        }
    }

    //design related setups
    private void arcSetup() {
        activitiesPanel.putClientProperty(FlatClientProperties.STYLE,"arc:20");
        statsPanel.putClientProperty(FlatClientProperties.STYLE,"arc:20");
        act2statsPanel.putClientProperty(FlatClientProperties.STYLE,"arc:20");
        tablesPanel.putClientProperty(FlatClientProperties.STYLE,"arc:20");
        fieldsPanel.putClientProperty(FlatClientProperties.STYLE,"arc:20");
        localDBPanel.putClientProperty(FlatClientProperties.STYLE,"arc:20");
        onlineDBPanel.putClientProperty(FlatClientProperties.STYLE,"arc:20");
        activityStatsPanel.putClientProperty(FlatClientProperties.STYLE,"arc:20");
        activityStat.putClientProperty(FlatClientProperties.STYLE,"arc:20");
        calLossStat.putClientProperty(FlatClientProperties.STYLE,"arc:20");
        weightLossStat.putClientProperty(FlatClientProperties.STYLE,"arc:20");
    }
    //activities table setup
    public void createTable(){
        LocalActDBHelper localActDBHelper = new LocalActDBHelper();
        activitiesTable.setModel(localActDBHelper.getActivitiesTable());

        OnlineDataBaseHelper onlineDataBaseHelper = new OnlineDataBaseHelper();
        onlineActivitiesTable.setModel(onlineDataBaseHelper.getActivitiesTableModelOnline());


    }
    //converting UID empty inputs to 0 or valid UID
    private long parseOrZero(String text) {
        try {
            return Long.parseLong(text.trim());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }
    public JPanel getActivitiesPanel() {
        return activitiesPanel;
    }
}
