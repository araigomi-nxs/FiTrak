package layouts.admin;

import DAO.test.SupabaseHttpClient;
import DAO.test.SyncAccManager;
import com.formdev.flatlaf.FlatClientProperties;
import objects.Account;
import tracker.Stats;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

public class AdminDashboard {
    private JPanel dashboardPanel;
    private JPanel JP2;

    private JPanel bastaPanel;
    private JPanel Tray;
    private JPanel JP1;
    private JPanel JP4;
    private JPanel JP5;
    private JPanel JP6;
    private JPanel JP7;

    private JTextArea logsArea;
    private JPanel logPanel;
    private JPanel JP3;
    private JPanel JP8;
    private JLabel accCnt;
    private JLabel actCnt;
    private JLabel wrkCnt;
    private JLabel globCalBurn;
    private JLabel globWtLss;
    private JLabel clockLabel;
    private JLabel greetLabel;
    private JLabel oLAccounts;
    private JLabel inLimbo;
    private JLabel oLActivities;
    private JLabel oLWorkout;
    private JLabel connectionStatus;
    private static Account accountInSession;

    public AdminDashboard(Account accountInSession) {
        AdminDashboard.accountInSession = accountInSession;

       setupArc();
       setStats();
       updateGreeting();

        SyncAccManager syncAccManager = new SyncAccManager();
        syncAccManager.getSyncLogsHttp(logsArea);

        Timer timer = new Timer(30000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SyncAccManager syncAccManager = new SyncAccManager();
                syncAccManager.getSyncLogsHttp(logsArea);
                setStats();

            }
        });
        timer.start();

        Timer clock = new Timer(1000, e -> {
            // 12-hour format with AM/PM
            String time = new SimpleDateFormat("hh:mm:ss a").format(new Date());
            clockLabel.setText(time);
        });
        clock.start();


    }

    private  void updateGreeting() {
        LocalTime now = LocalTime.now();
        int hour = now.getHour();

        if (hour >= 5 && hour < 12) {
            greetLabel.setText("Good Morning " + accountInSession.getUsername()  + "!🌞");
        } else if (hour >= 12 && hour < 18) {
            greetLabel.setText("Good Afternoon " + accountInSession.getUsername()  + "!🌅");
        } else {
            greetLabel.setText("Good Evening  " + accountInSession.getUsername()  + "!🌜");
        }
    }

    private void setStats() {
            accCnt.setText(Stats.getEntryCount("accounts") +"");
            actCnt.setText(Stats.getEntryCount("activities") +"");
            wrkCnt.setText(Stats.getEntryCount("workouts") +"");
            globCalBurn.setText(Stats.getGlobalCalLoss() +"\nkCal");
            globWtLss.setText(Stats.getGlobalWtLoss() +"\nKg");

            inLimbo.setText("Total Entities in Limbo: " + (0 + Stats.getLimboCount("accounts")  + Stats.getLimboCount("workouts") +  Stats.getLimboCount("activities")  ));
          if(Stats.getOnlineTableCount("accounts") > 0){
              connectionStatus.setText("Connected");
              connectionStatus.setIcon(new ImageIcon("src/main/resources/images/greenOrb.png"));
              oLAccounts.setText("Online Accounts: " + Stats.getOnlineTableCount("accounts"));
              oLActivities.setText("Online Activities: " + Stats.getOnlineTableCount("activities"));
              oLWorkout.setText("Online Workout: " + Stats.getOnlineTableCount("workouts"));
          }
          else
          {
              connectionStatus.setText("Offline");
              connectionStatus.setIcon(new ImageIcon("src/main/resources/images/redOrb.png"));
          }


    }


    private void setupArc() {

        bastaPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        Tray.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        JP1.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        JP2.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        JP4.putClientProperty(FlatClientProperties.STYLE, "arc:20");
       // JP5.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        JP6.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        JP7.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        JP8.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        JP3.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        logPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20");

    }

    public JPanel getAdminDashboard() {
        return dashboardPanel;

    }
}
