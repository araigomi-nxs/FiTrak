package layouts.admin;

import DAO.LocalWorkoutDBHelper;
import DAO.OnlineDataBaseHelper;
import DAO.test.SyncWorkManager;
import com.formdev.flatlaf.FlatClientProperties;
import tracker.Stats;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdminWorkout {


    private JPanel adminWorkoutPanel;
    private JPanel statsPanel;
    private JPanel statP1;
    private JPanel statP2;
    private JPanel statP3;
    private JPanel localDBPanel;
    private JTable workoutTable;
    private JScrollPane scroll;
    private JLabel limboCount;
    private JLabel workCounter;
    private JLabel localCount;
    private JLabel foreignCount;
    private JLabel matches;
    private JTextField searchField;
    private JLabel searchButton;
    private JLabel refreshButton;
    private JScrollPane scrolltable;
    private JTable onlineWorkoutTable;
    private JButton syncButton;
    private JLabel refreshOnline;
    private JLabel onlineEntryCount;
    private JLabel offlineEntityCount;
    private JPanel emptyStat;


    public AdminWorkout() {
        statP1.putClientProperty(FlatClientProperties.STYLE,"arc:20");
        statP2.putClientProperty(FlatClientProperties.STYLE,"arc:20");
        statP3.putClientProperty(FlatClientProperties.STYLE,"arc:20");
        statsPanel.putClientProperty(FlatClientProperties.STYLE,"arc:20");

        localDBPanel.putClientProperty(FlatClientProperties.STYLE,"arc:20");

        loadLocalTable();
        loadOnlineTable();

        setupStats();

        refreshButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                loadOnlineTable();
            }
        });

        refreshOnline.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                loadOnlineTable();
            }
        });

        syncButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                SyncWorkManager syncWorkManager = new SyncWorkManager();
                try {
                    syncWorkManager.startWorkSyncThread();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });




    }


    public void loadLocalTable()
    {
        LocalWorkoutDBHelper localWorkoutDBHelper = new LocalWorkoutDBHelper();
        workoutTable.setModel(localWorkoutDBHelper.getWorkoutsTableModelLocal());
        setupStats();

       }
    public void loadOnlineTable()
    {
        OnlineDataBaseHelper onlineDataBaseHelper = new OnlineDataBaseHelper();
        onlineWorkoutTable.setModel(onlineDataBaseHelper.getWorkoutsTableModelOnline());
        setupStats();

    }


    private void setupStats() {
        workCounter.setText(workoutTable.getRowCount()+"");
        limboCount.setText("Entities in Limbo: " + Stats.getLimboCount("workouts"));
        offlineEntityCount.setText("Offline Entities: " + workoutTable.getRowCount());
        onlineEntryCount.setText("Online Entities: " + Stats.getOnlineTableCount("workouts") );

        localCount.setText("Local: "+ Stats.getLocalCount("workouts"));
        int foreignIntCount = workoutTable.getRowCount() - Stats.getLocalCount("workouts");
        foreignCount.setText("Foreign: "+ foreignIntCount);
    }
    public JPanel getAdminWorkoutPanel() {
        return adminWorkoutPanel;
    }




}
