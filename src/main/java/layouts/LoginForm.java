package layouts;

import DAO.DataBaseHelper;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;


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
    private JPanel spacer1;
    private JLabel signUpTextButton;
    private JPanel splashPanel;

    private int  accountLoginType = 0;

    public LoginForm() throws IOException {


        cardLayout = new CardLayout();
        setContentPane(panel1);


        loginPanel.setLayout(cardLayout);
        loginPanel.add(PanelInner, "LoginModule");

        SignUpForm signUpForm = new SignUpForm( loginPanel);

        loginPanel.add(signUpForm.getSignUpOuterPanel(), "SignUpModule");
        cardLayout.show(loginPanel, "LoginModule");



        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //setUndecorated(true);
        setSize(1080    , 720);
        setLocationRelativeTo(null);
        setVisible(true);
        pack();
        setBackground(Color.white);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);


        emailField.setMargin(new Insets(30,20,10,10));
        emailField.setToolTipText("Enter your email address");

        adminLoginButton.putClientProperty("JButton.buttonType", "roundRect");
        adminLoginButton.putClientProperty("Flatlaf.style", "arc:20");



        userLoginButton.putClientProperty("JButton.buttonType", "roundRect");
        userLoginButton.putClientProperty("Flatlaf.style", "arc: 20");



        signInButton.putClientProperty("JButton.buttonType", "roundRect");
        signInButton.putClientProperty("Flatlaf.style", "arc: 20");

        alertField.setForeground(Color.RED);


        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataBaseHelper dataBaseHelper = new DataBaseHelper();


              if(emailField.getText().equals("") || passwordField.getPassword().equals("")) {
                alertField.setForeground(Color.RED);
                alertField.setText("Please fill all the fields above");
              }
              else{
                  int status =dataBaseHelper.checkCredentials(emailField.getText(),new String(passwordField.getPassword()), accountLoginType);
                  if(status == 2 )
                  {
                      JOptionPane.showMessageDialog(null, "Login Successful");
                      emailField.setText("");
                      passwordField.setText("");
                      alertField.setText("");

                      if(accountLoginType == 1)
                      {
                          AdminPanel adminPanel = new AdminPanel();
                          setVisible(false);
                          setContentPane(adminPanel.getDashBoardPanel());
                          setVisible(true);
                      }
                      else
                      {
                          UserDashboard userDashboard = new UserDashboard();
                          setVisible(false);
                          setContentPane(userDashboard.getDashBoardPanel());
                          setVisible(true);

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




    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
