package layouts.admin;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;

public class AdminDashboard {
    private JPanel dashboardPanel;
    private JPanel statP2;
    private JLabel workCounter;
    private JPanel accountStat;
    private JLabel accCounter;
    private JPanel bastaPanel;
    private JPanel bastaPanel2;

    public AdminDashboard() {
       setupArc();


    }

    private void setupArc() {
        bastaPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        bastaPanel2.putClientProperty(FlatClientProperties.STYLE, "arc:20");

    }

    public JPanel getAdminDashboard() {
        return dashboardPanel;
    }
}
