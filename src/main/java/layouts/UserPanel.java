package layouts;

import DAO.LocalDataBaseHelper;
import com.formdev.flatlaf.FlatClientProperties;
import layouts.user.UserAccountForm;
import layouts.user.UserDashboardForm;
import layouts.user.UserGoalsForm;
import layouts.user.UserWorkoutsForm;
import objects.Account;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;

public class UserPanel extends JFrame {

    private JPanel UserPanel;
    private JPanel userDashBoard;
    private JPanel sidebar;
    private JPanel panelToo;
    private JLabel usernameDisplay;
    private JLabel logoutButton;
    private JButton dashboardButton;
    private JButton workoutButton;
    private JButton goalsButton;
    private JButton accountsButton;
    private JPanel titleArea;
    private JLabel exitButton;

    private JButton exercisesButton;
    private JButton walkingButton;
    private JButton runningButton;
    private JButton cyclingButton;

    private JPanel calculatorArea;
    private JPanel BasicTB;

    private JPanel userPanelContainer;

    private CardLayout cardLayout;
    private CardLayout calcLayout;

    LocalDataBaseHelper dataBaseHelper;
    private Account accountInSession;
    Point initialClick;


    UserPanel(long UserSession) {

        ImageIcon icon = new ImageIcon("src/main/resources/images/logo.png");
        setIconImage(icon.getImage());

        dataBaseHelper = new LocalDataBaseHelper();
        accountInSession = dataBaseHelper.getAccount(UserSession);

        setContentPane(UserPanel);

        // IMPORTANT: USE THE EXISTING PANEL FROM GUI, DO NOT RECREATE IT
        cardLayout = (CardLayout) userPanelContainer.getLayout();

        UserAccountForm userAccountForm = new UserAccountForm();
        UserDashboardForm userDashboardForm = new UserDashboardForm();
        UserGoalsForm userGoalsForm = new UserGoalsForm();
        UserWorkoutsForm userWorkoutsForm =
                new UserWorkoutsForm(dataBaseHelper, accountInSession);


        userPanelContainer.add(userAccountForm.getUserAccountPanel(), "UserAccountPanel");
        userPanelContainer.add(userDashboardForm.getUserDashPanel(), "UserDashPanel");
        userPanelContainer.add(userGoalsForm.getUserGoalsPanel(), "UserGoalPanel");
        userPanelContainer.add(userWorkoutsForm.getUserWorkoutsPanel(), "UserWorkoutsPanel");

        cardLayout.show(userPanelContainer, "UserDashPanel");

        sidebar.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        panelToo.putClientProperty(FlatClientProperties.STYLE, "arc:20");

        usernameDisplay.setText(accountInSession.getUsername());

        setUndecorated(true);
        setSize(1300, 800);
        setLocationRelativeTo(null);
        setVisible(true);
        setBackground(Color.white);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));

        dashboardButton.addActionListener(e -> {
            cardLayout.show(userPanelContainer, "UserDashPanel");
            resetButton();
            dashboardButton.setBackground(new Color(31, 52, 62));
            dashboardButton.setForeground(new Color(220, 228, 55));
        });

        accountsButton.addActionListener(e -> {
            cardLayout.show(userPanelContainer, "UserAccountPanel");
            resetButton();
            accountsButton.setBackground(new Color(31, 52, 62));
            accountsButton.setForeground(new Color(220, 228, 55));
        });

        goalsButton.addActionListener(e -> {
            cardLayout.show(userPanelContainer, "UserGoalPanel");
            resetButton();
            goalsButton.setBackground(new Color(31, 52, 62));
            goalsButton.setForeground(new Color(220, 228, 55));
        });

        workoutButton.addActionListener(e -> {
            cardLayout.show(userPanelContainer, "UserWorkoutsPanel");
            resetButton();
            workoutButton.setBackground(new Color(31, 52, 62));
            workoutButton.setForeground(new Color(220, 228, 55));
        });

        exitButton.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                dispose();
            }
        });

        titleArea.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
            }
        });

        titleArea.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                int thisX = getLocation().x;
                int thisY = getLocation().y;
                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;
                setLocation(thisX + xMoved, thisY + yMoved);
            }
        });

        logoutButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                try {
                    LoginForm loginForm = new LoginForm();
                    loginForm.setVisible(true);
                    dispose();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }


    public void resetButton() {
        dashboardButton.setBackground(new Color(17, 37, 44));
        dashboardButton.setForeground(new Color(79, 96, 115));

        goalsButton.setBackground(new Color(17, 37, 44));
        goalsButton.setForeground(new Color(79, 96, 115));

        workoutButton.setBackground(new Color(17, 37, 44));
        workoutButton.setForeground(new Color(79, 96, 115));

        accountsButton.setBackground(new Color(17, 37, 44));
        accountsButton.setForeground(new Color(79, 96, 115));
    }
}
