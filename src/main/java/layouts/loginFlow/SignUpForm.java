
package layouts.loginFlow;

import objects.Admin;
import objects.User;
import DAO.LocalDataBaseHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    protected static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private LocalDataBaseHelper dataBaseHelper;
    private LocalDateTime dateTime;

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
                if( !emailSUField.getText().isEmpty() && !passwordSUField.getText().isEmpty()) {


                       if (emailSUField.getText().trim().matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                               "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$") ) {
                           LocalDataBaseHelper dataBaseHelper = new LocalDataBaseHelper();
                           dateTime =  LocalDateTime.now();

                           if(dataBaseHelper.checkEmailExists(emailSUField.getText().trim())!=1)
                           {

                               String password = new String(passwordSUField.getPassword());


                               if(password.matches("^(?=.*[A-Z])(?=.*\\d).+$")   )
                               {
                                   //add acount creation confirmed / stuff here about
                                   // add to database here
                                   //JOptionPane.showMessageDialog(null, "Account createed");

                                   dataBaseHelper = new LocalDataBaseHelper();
                                   long userIDtemp;
                                   if( accountCreationType == 1)
                                   {
                                       Admin admin = new Admin(emailSUField.getText().trim() , passwordSUField.getText().trim(), dateTime.format(formatter));
                                       dataBaseHelper.insertUser(admin.getId(),admin.getEmail(), admin.getPassword(), 1, admin.getCreationDT(), admin.getLastUpdatedDT());
                                       JOptionPane.showMessageDialog(null, "Admin account Created" + admin.getEmail() + "\n"+admin.getPassword() +"\n"+ admin.getPrivilege());
                                       userIDtemp = admin.getId();
                                   }
                                   else
                                   {
                                       User user = new User(emailSUField.getText().trim() , passwordSUField.getText().trim(), dateTime.format(formatter));
                                       dataBaseHelper.insertUser(user.getId(),user.getEmail(), user.getPassword(), 0, user.getCreationDT(), user.getLastUpdatedDT() );
                                       JOptionPane.showMessageDialog(null, "User account Created\n" +user.getId()+  user.getEmail() +"\n"+ user.getPassword() +"\n"+ user.getPrivilege());
                                       userIDtemp = user.getId();
                                   }

                                   alertFieldSU.setText("");
                                   emailSUField.setText("");
                                   passwordSUField.setText("");

                                   SignUpData signUpData = new SignUpData(userIDtemp, loginPanelRef);
                                   loginPanelRef.add(signUpData.getSignUpDataPanel(), "SignUpDataModule");
                                   cardLayout.show(loginPanelRef, "SignUpDataModule");

                               }
                               else {

                                   alertFieldSU.setText("Password must contain at least one digit and one upper case letter");
                               }
                           }
                           else
                           {
                               alertFieldSU.setText("Email already exists");
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
