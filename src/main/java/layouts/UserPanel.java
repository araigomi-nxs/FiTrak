package layouts;

import DAO.LocalDataBaseHelper;
import com.formdev.flatlaf.FlatClientProperties;
import layouts.calculator.basic.CyclingWorkoutCalculator;
import layouts.calculator.basic.RunningWorkoutCalculator;
import layouts.calculator.basic.WalkingWorkoutCalculator;
import objects.Account;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
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
    private JButton accountsButton;
    private JButton activitiesButton;
    private JButton calculationButton;
    private JPanel titleArea;
    private JLabel exitButton;
    CardLayout cardLayout;

    private JPanel row1;
    private JPanel basicRow;

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



    public JPanel getDashBoardPanel() {
        return UserPanel;
    }

}
