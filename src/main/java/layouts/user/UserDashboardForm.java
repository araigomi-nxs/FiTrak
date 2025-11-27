package layouts.user;

import DAO.LocalActDBHelper;
import DAO.UserData;
import calculationModels.metrics.MetricsCalculator;
import com.formdev.flatlaf.FlatClientProperties;
import layouts.widgets.ContributionGridPanel;
import objects.Account;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private JLabel refreshButton;
    private JLabel workoutsTable;
    private JScrollPane workTable;
    private JTable workoutstabtable;
    private JLabel weightField;
    private JLabel heighField;
    private JLabel classification;
    private JLabel bmiField;
    private JLabel bmrField;
    private JLabel tdeeField;
    private JPanel JP9;
    private JPanel JP10;
    private Account accountInSession;

    UserDashboardForm(Account accountInSession) {
        this.accountInSession = accountInSession;

        setArc();
        updateGreeting();
        loadTable();


        contributionPanel.add(new ContributionGridPanel(accountInSession.getId(), 20));

        refreshButton.addMouseListener(new  MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                loadTable();
                setStats();
            }
        });


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
            JP9.putClientProperty(FlatClientProperties.STYLE, "arc:20");
            JP10.putClientProperty(FlatClientProperties.STYLE, "arc:20");



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

        if(fullDataCheckBox.isSelected())
        {
           userActivitiesTable.setModel(localActDBHelper.searchByUserID(accountInSession.getId()));
        }
        else
        {
            userActivitiesTable.setModel( localActDBHelper.getActivitiesTableModel(accountInSession.getId()));
        }
        setStats();

        workoutstabtable.setModel (UserData.getWorkoutsTableModelFromActivities(localActDBHelper.searchByUserID(accountInSession.getId())));
    }


    private void setStats()
    {
        totalCalBurnStat.setText(String.format("%.2f",  UserData.getTotalCaloriesBurned(accountInSession.getId() )));

       double dailyCalBurn = UserData.getTodayCaloriesBurned(accountInSession.getId());
        dailyCalBurnStat.setText(""+ dailyCalBurn);

        totalWeightLoss.setText("" + MetricsCalculator.computeFatLoss(UserData.getTotalCaloriesBurned(accountInSession.getId())));
        weightField.setText("WEIGHT: "  +accountInSession.getWeight() + "KG");
        heighField.setText("HEIGHT: "  +accountInSession.getHeight() + "M");
        bmiField.setText( "BMI: "  +accountInSession.getBMI() );
        double BMR =MetricsCalculator.computeBMR(accountInSession.getWeight(), accountInSession.getHeight(), accountInSession.getAge(), accountInSession.getSex());
        bmrField.setText( "BMR: "  + BMR);
        classification.setText("Classification:" +  MetricsCalculator.suggestDifficulty(accountInSession.getBMI()));
        tdeeField.setText(MetricsCalculator.computeTDEE(BMR,  dailyCalBurn , accountInSession.getPreference() )+"");







    }

}
