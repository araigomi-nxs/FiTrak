package layouts;

import DAO.LocalDataBaseHelper;
import com.formdev.flatlaf.FlatClientProperties;
import layouts.calculator.basic.CyclingWorkoutCalculator;
import layouts.calculator.basic.RunningWorkoutCalculator;
import layouts.calculator.basic.WalkingWorkoutCalculator;
import layouts.user.UserAccountForm;
import layouts.user.UserDashboardForm;
import layouts.user.UserGoalsForm;
import objects.Account;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;

public class UserPanel extends JFrame {

    private JPanel UserPanel;
    private JPanel userDashBoard;
    private JPanel walkPos;
    private JPanel runPos;
    private JPanel cycPos;
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
    CardLayout cardLayout;

    private JPanel row1;
    private JPanel basicRow;
    private JPanel userPanelContainer;
    private JPanel workoutPanel;

    LocalDataBaseHelper dataBaseHelper ;
    private  Account accountInSession;
    Point initialClick;

    private JButton jButtonHolder;


    UserPanel(long UserSession)  {

        ImageIcon icon = new ImageIcon("src/main/resources/images/logo.png");
        setIconImage(icon.getImage());

        dataBaseHelper = new LocalDataBaseHelper();
        dataBaseHelper.getAccount(UserSession);
        accountInSession = dataBaseHelper.getAccount(UserSession);

        setContentPane(UserPanel);


        cardLayout = new CardLayout();

        userPanelContainer.setLayout(cardLayout);

        UserAccountForm  userAccountForm = new UserAccountForm();
        UserDashboardForm userDashboardForm = new UserDashboardForm();
        UserGoalsForm userGoalsForm = new UserGoalsForm();

        userPanelContainer.add(workoutPanel, "WorkoutPanel");
        userPanelContainer.add(userAccountForm.getUserAccountPanel(), "UserAccountPanel");
        userPanelContainer.add(userDashboardForm.getUserDashPanel(), "UserDashPanel");
        userPanelContainer.add(userGoalsForm.getUserGoalsPanel(), "UserGoalPanel");

        cardLayout.show(userPanelContainer, "WorkoutPanel");

        UserPanel.setLayout(cardLayout);
        walkPos.setLayout(cardLayout);
        runPos.setLayout(cardLayout);
        cycPos.setLayout(cardLayout);

        WalkingWorkoutCalculator walkingWorkoutCalculator = new WalkingWorkoutCalculator(accountInSession);
        CyclingWorkoutCalculator cyclingWorkoutCalculator = new CyclingWorkoutCalculator(accountInSession);
        RunningWorkoutCalculator runningWorkoutCalculator = new RunningWorkoutCalculator(accountInSession);


        walkPos.add(walkingWorkoutCalculator.getPanel(),"walkingCalculator");
        cycPos.add(cyclingWorkoutCalculator.getPanel(), "cyclingWorkoutCalculator");
        runPos.add(runningWorkoutCalculator.getPanel(), "runningWorkoutCalculator");


        cardLayout.show(walkPos, "walkingCalculator");
        cardLayout.show(cycPos, "cyclingWorkoutCalculator");
        cardLayout.show(runPos, "runningWorkoutCalculator");

        sidebar.putClientProperty(FlatClientProperties.STYLE,  "arc:20");
        panelToo.putClientProperty(FlatClientProperties.STYLE,  "arc:20");

        usernameDisplay.setText(accountInSession.getUsername());



        setUndecorated(true);
        setSize(1300    , 800);
        setLocationRelativeTo(null);
         setVisible(true);
        setBackground(Color.white);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));

        dashboardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(userPanelContainer, "UserDashPanel");
                resetButton();
                dashboardButton.setBackground(new Color(31, 52, 62));
                dashboardButton.setForeground(new Color(220, 228, 55));
            }
        });
        accountsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(userPanelContainer, "UserAccountPanel");
                resetButton();
                accountsButton.setBackground(new Color(31, 52, 62));
                accountsButton.setForeground(new Color(220, 228, 55));
            }
        });

        goalsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(userPanelContainer, "UserGoalPanel");
                resetButton();
                goalsButton.setBackground(new Color(31, 52, 62));
                goalsButton.setForeground(new Color(220, 228, 55));
            }
        });
        workoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(userPanelContainer, "WorkoutPanel");
                resetButton();
                workoutButton.setBackground(new Color(31, 52, 62));
                workoutButton.setForeground(new Color(220, 228, 55));
            }
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
                // get current location
                int thisX = getLocation().x;
                int thisY = getLocation().y;

                // calculate movement
                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;

                // move frame
                int X = thisX + xMoved;
                int Y = thisY + yMoved;
                setLocation(X, Y);
            }
        });

        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
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

    public void resetButton()
    {
        dashboardButton.setBackground(new  Color(17, 37, 44));
        dashboardButton.setForeground(new Color(79, 96, 115));
        goalsButton.setBackground(new  Color(17, 37, 44));
        goalsButton.setForeground(new Color(79, 96, 115));
        workoutButton.setBackground(new  Color(17, 37, 44));
        workoutButton.setForeground(new Color(79, 96, 115));
        accountsButton.setBackground(new  Color(17, 37, 44));
        accountsButton.setForeground(new Color(79, 96, 115));
    }



    public JPanel getDashBoardPanel() {
        return UserPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
