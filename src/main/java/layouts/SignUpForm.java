
package layouts;

import Objects.Admin;
import Objects.User;
import DAO.DataBaseHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;

public class SignUpForm {
    private JPanel signUpMainPanel;
    private JTextField emailSUField;
    private JPasswordField passwordSUField;
    private JLabel alertFieldSU;
    private JButton signUpButton;
    private JButton userButton;
    private JButton adminButton;
    private JPanel spacer1;
    private JLabel signInTextButton;
    private JLabel greetLabelSU;
    private JPanel signUpOuterPanel;


    private JPanel loginPanelRef;
    private CardLayout cardLayout;
    private int  accountCreationType = 0;

    DataBaseHelper dataBaseHelper;

    public SignUpForm(JPanel loginPanel) {


        this.loginPanelRef = loginPanel;
        this.cardLayout = (CardLayout) loginPanel.getLayout();




        emailSUField.setMargin(new Insets(30,20,10,10));

        userButton.putClientProperty("JButton.buttonType", "roundRect");
        userButton.putClientProperty("Flatlaf.style", "arc:20");

        adminButton.putClientProperty("JButton.buttonType", "roundRect");
        adminButton.putClientProperty("Flatlaf.style", "arc:20");

        signUpButton.putClientProperty("JButton.buttonType", "roundRect");
        signUpButton.putClientProperty("Flatlaf.style", "arc:20");


        signInTextButton.putClientProperty("JButton.buttonType", "roundRect");
        signInTextButton.putClientProperty("Flatlaf.style", "arc:20");


        userButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userButton.setBackground(new Color(0x013A50));
                adminButton.setBackground(Color.WHITE);
                greetLabelSU.setText("Create a User Account");
                accountCreationType = 0;

            }
        });

        adminButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userButton.setBackground(Color.WHITE);
                adminButton.setBackground(new Color(0x013A50));
                greetLabelSU.setText("Create an Admin Account");
                accountCreationType = 1;
            }
        });


        signInTextButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                cardLayout.show(loginPanelRef, "LoginModule");

            }
        });


        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                alertFieldSU.setForeground(Color.RED);
                if( !emailSUField.getText().equals("") && !passwordSUField.getText().equals("") ) {

                    if (emailSUField.getText().trim().matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                            "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$") ) {

                        String password = new String(passwordSUField.getPassword());


                        if(password.matches("^(?=.*[A-Z])(?=.*\\d).+$")   )
                        {
                            //add acount creation confirmed / stuff here about
                            // add to database here
                            //JOptionPane.showMessageDialog(null, "Account createed");

                            dataBaseHelper = new DataBaseHelper();

                            if( accountCreationType == 1)
                            {
                                Admin admin = new Admin(emailSUField.getText().trim() , passwordSUField.getText().trim());
                                dataBaseHelper.insertUser(admin.getId(),admin.getEmail(), admin.getPassword(), 1);
                                JOptionPane.showMessageDialog(null, "Admin account Created" + admin.getEmail() + "\n"+admin.getPassword() +"\n"+ admin.getPrivelege());
                            }
                            else
                            {
                                User user = new User(emailSUField.getText().trim() , passwordSUField.getText().trim());
                                dataBaseHelper.insertUser(user.getId(),user.getEmail(), user.getPassword(), 0);
                                JOptionPane.showMessageDialog(null, "User account Created\n" +user.getId()+  user.getEmail() +"\n"+ user.getPassword() +"\n"+ user.getPrivelege());
                            }
                            alertFieldSU.setText("");
                            emailSUField.setText("");
                            passwordSUField.setText("");
                            cardLayout.show(loginPanelRef, "LoginModule");

                        }
                        else {

                            alertFieldSU.setText("Password must contain at least one digit and one upper case letter");
                        }
                    }
                    else {
                        //JOptionPane.showMessageDialog(null, "Please enter a valid email address");
                        alertFieldSU.setText("Please enter a valid email address");
                    }
                }
                else{
                    //JOptionPane.showMessageDialog(null, "Please fill all input fields");
                    alertFieldSU.setText("Please fill all input fields");
                }


            }
        });
    }

    public JPanel getSignUpOuterPanel(){
        return signUpMainPanel;
    }


}
