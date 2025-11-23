package layouts;

import DAO.LocalDataBaseHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;

import layouts.loginFlow.SignUpForm;


public class LoginForm extends JFrame {

    private JPanel panel1;
    private CardLayout cardLayout;


    private JButton userLoginButton;
    private JButton adminLoginButton;
    private JButton signInButton;

    private JPanel loginPanel;
    private JPanel PanelInner;
    private JLabel pageLabel;

    private JPanel LoginUpperPanel;
    private JPanel SignInMethodsPanel;
    private JPanel SignInMethodsButtonPanel;

    private JLabel SignUpFlowLabel;
    private JPasswordField passwordField;
    private JLabel greetLabel;
    private JTextField emailField;
    private JLabel SplashImage;
    private JLabel alertField;
    private JPanel spacer;
    private JLabel signUpTextButton;
    private JPanel splashPanel;
    private JPanel titleArea;
    private JLabel exitButton;


    private int  accountLoginType = 0;

    Point initialClick;

    public LoginForm() throws IOException {

        ImageIcon icon = new ImageIcon("src/main/resources/images/logo.png");
        setIconImage(icon.getImage());

        cardLayout = new CardLayout();
        setContentPane(panel1); //

        loginPanel.setLayout(cardLayout);
        loginPanel.add(PanelInner, "LoginModule");
        SignUpForm signUpForm = new SignUpForm( loginPanel);
        loginPanel.add(signUpForm.getSignUpOuterPanel(), "SignUpModule");
        cardLayout.show(loginPanel, "LoginModule");





        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setSize(1300    , 800);
        setLocationRelativeTo(null);
        setVisible(true);
        setBackground(Color.white);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setResizable(false);
        getRootPane().setDefaultButton(signInButton);

        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));

        emailField.setMargin(new Insets(30,20,10,10));
        emailField.setToolTipText("Enter your email address");




        adminLoginButton.putClientProperty("JButton.buttonType", "roundRect");
        adminLoginButton.putClientProperty("Flatlaf.style", "arc:20");



        userLoginButton.putClientProperty("JButton.buttonType", "roundRect");
        userLoginButton.putClientProperty("Flatlaf.style", "arc: 20");



        signInButton.putClientProperty("JButton.buttonType", "roundRect");
        signInButton.putClientProperty("Flatlaf.style", "arc: 20");

        alertField.setForeground(Color.RED);




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

        exitButton.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                dispose();
            }
        });

        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDataBaseHelper dataBaseHelper = new LocalDataBaseHelper();


              if(emailField.getText().isEmpty() || passwordField.getPassword().equals("")) {
                alertField.setForeground(Color.RED);
                alertField.setText("Please fill all the fields above");
              }
              else{
                  int status =dataBaseHelper.checkCredentials(emailField.getText(),new String(passwordField.getPassword()), accountLoginType);
                  if(status == 2 )
                  {
                      JOptionPane.showMessageDialog(null, "Login Successful");
                      if(accountLoginType == 1)
                      {
                          AdminPanel adminPanel = new AdminPanel(dataBaseHelper.getID(emailField.getText()));
                          dispose();
                      }
                      else
                      {
                          UserPanel userPanel = new UserPanel(dataBaseHelper.getID(emailField.getText()));
                          dispose();

                      }


                  }
                  else if( status == 1)
                  {
                      JOptionPane.showMessageDialog(null, "No such account exists");
                      alertField.setForeground(Color.RED);
                      alertField.setText("No such account exists");
                  }
                  else
                  {
                      JOptionPane.showMessageDialog(null, "Username or password incorrect");
                      alertField.setForeground(Color.RED);
                      alertField.setText("Incorrect Username or Password");
                  }
              }
            }
        });




        userLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                userLoginButton.setBackground(new Color(0x013A50));
                adminLoginButton.setBackground(Color.white);
                greetLabel.setText("Welcome Back User!");
                accountLoginType = 0;

            }
        });

        adminLoginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                adminLoginButton.setBackground(new Color(0x013A50) );
                userLoginButton.setBackground(Color.white);
                greetLabel.setText("Welcome Back Admin!");
                accountLoginType = 1;


            }
        });

        signUpTextButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signUpTextButton.addMouseListener( new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                cardLayout.show(loginPanel, "SignUpModule");
            }
        });

    }

    public  JPanel getLoginPanel() {
        return panel1;
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
