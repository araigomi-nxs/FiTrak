package layouts.loginFlow;

import DAO.LocalDataBaseHelper;
import DAO.test.SyncAccManager;
import DAO.test.SyncActManager;
import DAO.test.SyncWorkManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.InetAddress;

public class SelectPreference {
    private JButton finishButton;
    private JPanel basicWorkout;
    private JPanel selectPrefPanel;
    private JPanel strengthWorkout;
    private JPanel cardioWorkout;
    private int selectedPreference = 0;

    CardLayout cardLayout;


    SelectPreference(long userID, JPanel loginPanel) {

        basicWorkout.putClientProperty("FlatLaf.style", "arc:10");
        cardioWorkout.putClientProperty("FlatLaf.style", "arc:10");
        strengthWorkout.putClientProperty("FlatLaf.style", "arc:10");
        finishButton.putClientProperty("JButton.buttonType", "roundRect");
        finishButton.putClientProperty("FlatLaf.style", "arc:20");

        this.cardLayout = (CardLayout) loginPanel.getLayout();

        basicWorkout.addMouseListener(new  MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                selectedPreference = 1;
                clearBorder();
                basicWorkout.putClientProperty("JPanel.borderColor", new Color(220, 228, 55));
                basicWorkout.setBorder(BorderFactory.createLineBorder( new Color(220, 228, 55), 2));
                basicWorkout.putClientProperty("FlatLaf.style", "arc:10");

            }

        });
        cardioWorkout.addMouseListener(new  MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedPreference = 2;
                clearBorder();
                cardioWorkout.putClientProperty("JPanel.borderColor", new Color(220, 228, 55));
                cardioWorkout.setBorder(BorderFactory.createLineBorder( new Color(220, 228, 55), 2));
                basicWorkout.putClientProperty("FlatLaf.style", "arc:10");
            }
        });
        strengthWorkout.addMouseListener(new  MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedPreference = 3;
                clearBorder();
                strengthWorkout.putClientProperty("JPanel.borderColor", new Color(220, 228, 55));
                strengthWorkout.setBorder(BorderFactory.createLineBorder( new Color(220, 228, 55), 2));
                strengthWorkout.putClientProperty("FlatLaf.style", "arc:10");
            }
        });



        finishButton.addMouseListener(new  MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(selectedPreference>0){

                    LocalDataBaseHelper localDataBaseHelper = new LocalDataBaseHelper();
                    localDataBaseHelper.updatePref(userID, selectedPreference);
                    JOptionPane.showMessageDialog(null, "Account Succesfully Updated!");
                    clearBorder();
                    startSync();
                    cardLayout.show(loginPanel, "LoginModule");
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "No Preference Selected!");

                }
            }
        });
    }

    private void clearBorder()
    {
        basicWorkout.putClientProperty("JPanel.borderColor", new Color(255, 255, 255));
        basicWorkout.setBorder(BorderFactory.createLineBorder( new Color(250, 250, 250), 2));
        strengthWorkout.putClientProperty("JPanel.borderColor", new Color(255, 255, 255));
        strengthWorkout.setBorder(BorderFactory.createLineBorder( new Color(250, 250, 250), 2));
        cardioWorkout.putClientProperty("JPanel.borderColor", new Color(250, 250, 250));
        cardioWorkout.setBorder(BorderFactory.createLineBorder( new Color(250, 250, 250), 2));
        basicWorkout.putClientProperty("FlatLaf.style", "arc:10");
        cardioWorkout.putClientProperty("FlatLaf.style", "arc:10");
        strengthWorkout.putClientProperty("FlatLaf.style", "arc:10");
    }

    public  JPanel getPrefPanel() {
        return selectPrefPanel;
    }
    private boolean isNetworkAvailable() {
        try {
            // Try pinging a reliable host (e.g., Google DNS)
            InetAddress address = InetAddress.getByName("8.8.8.8");
            return address.isReachable(2000); // 2s timeout
        } catch (Exception e) {
            return false;
        }
    }

    public void startSync() {
        Thread syncThread = new Thread(() -> {
            if (isNetworkAvailable()) {
                System.out.println("Network available. Starting sync...");

                SyncAccManager syncAccManager = new SyncAccManager();
                syncAccManager.startSyncThread();

                SyncActManager syncActManager = new SyncActManager();
                try {
                    syncActManager.syncAllActivities();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                SyncWorkManager syncWorkManager = new SyncWorkManager();
                try {
                    syncWorkManager.syncAllWorkouts();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                System.out.println("Sync completed.");
            } else {
                System.out.println("No network. Loading local data...");
                System.out.println("Local data loaded.");
            }
        });

        syncThread.start(); // run in background
    }
}
