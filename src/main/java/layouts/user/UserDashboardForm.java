package layouts.user;

import DAO.LocalActDBHelper;
import DAO.UserData;
import calculationModels.metrics.MetricsCalculator;
import com.formdev.flatlaf.FlatClientProperties;
import layouts.widgets.ContributionGridPanel;
import objects.Account;

import javax.swing.*;
import java.time.LocalTime;

public class UserDashboardForm {
    private JPanel userDashPanel;
    private JPanel dashboardPanel;
    private JPanel bastaPanel;
    private JPanel bastaPanel2;
    private JPanel JP1;
    private JLabel greetLabel;
    private JPanel localDBPanel;
    private JTable userActivitiesTable;
    private JPanel contributionPanel;
    private JPanel JP2;
    private JPanel JP3;
    private JPanel JP4;
    private JPanel JP5;
    private JPanel JP6;
    private JLabel totalCalBurnStat;
    private JLabel totalWeightLoss;
    private JLabel dailyCalBurnStat;
    private JCheckBox fullDataCheckBox;
    private Account accountInSession;

    UserDashboardForm(Account accountInSession) {
        this.accountInSession = accountInSession;

        setArc();
        updateGreeting();
        loadTable();
        setStats();

        contributionPanel.add(new ContributionGridPanel(accountInSession.getId(), 20));
    }

    private void setArc()
    {
            bastaPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20");
            bastaPanel2.putClientProperty(FlatClientProperties.STYLE, "arc:20");
            JP1.putClientProperty(FlatClientProperties.STYLE, "arc:20");
            localDBPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20");
            JP2.putClientProperty(FlatClientProperties.STYLE, "arc:20");
            JP3.putClientProperty(FlatClientProperties.STYLE, "arc:20");
            JP4.putClientProperty(FlatClientProperties.STYLE, "arc:20");
            JP5.putClientProperty(FlatClientProperties.STYLE, "arc:20");
            JP6.putClientProperty(FlatClientProperties.STYLE, "arc:20");


    }

    public JPanel getUserDashPanel() {
        return userDashPanel;
    }

    private  void updateGreeting() {
        LocalTime now = LocalTime.now();
        int hour = now.getHour();

        if (hour >= 5 && hour < 12) {
            greetLabel.setText("Good Morning  ⠶"  + accountInSession.getUsername()  + "!🌞");
        } else if (hour >= 12 && hour < 18) {
            greetLabel.setText("Good Afternoon  ⠶" + accountInSession.getUsername()  + "!🌅");
        } else {
            greetLabel.setText("Good Evening   ⠶" + accountInSession.getUsername()  + "! 🌜");
        }
    }

    private void loadTable()
    {
        LocalActDBHelper localActDBHelper = new LocalActDBHelper();
        userActivitiesTable.setModel( localActDBHelper.getActivitiesTableModel(accountInSession.getId()));




    }


    private void setStats()
    {
        totalCalBurnStat.setText(""+ UserData.getTotalCaloriesBurned(accountInSession.getId()));
        dailyCalBurnStat.setText(""+ UserData.getTodayCaloriesBurned(accountInSession.getId()));
        totalWeightLoss.setText("" + MetricsCalculator.computeFatLoss(UserData.getTotalCaloriesBurned(accountInSession.getId())));



    }

}
