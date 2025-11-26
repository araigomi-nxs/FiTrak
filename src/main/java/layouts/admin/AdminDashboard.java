package layouts.admin;

import DAO.test.SyncAccManager;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;

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

    public AdminDashboard() {
       setupArc();
        SyncAccManager syncAccManager = new SyncAccManager();
        syncAccManager.getSyncLogsHttp(logsArea);

    }

    private void setupArc() {

        bastaPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        Tray.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        JP1.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        JP2.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        JP4.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        JP5.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        JP6.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        JP7.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        logPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20");





    }

    public JPanel getAdminDashboard() {
        return dashboardPanel;
    }
}
