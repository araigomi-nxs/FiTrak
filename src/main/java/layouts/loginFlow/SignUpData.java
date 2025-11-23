package layouts.loginFlow;

import DAO.LocalDataBaseHelper;
import calculationModels.metrics.MetricsCalculator;
import checker.InputValidator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;

public class SignUpData {
    private JPanel signUpDataPanel;
    private JTextField usernameInputField;
    private JTextField weightInputField;
    private JTextField heightInputField;
    private JLabel alertLabel;
    private JButton finishButton;
    private JRadioButton maleRadioButton;
    private JRadioButton femaleRadioButton;
    private JTextField ageField;
    private CardLayout cardLayout;
    private String sexSelected ="none";

    public SignUpData(long userID, JPanel loginPanel){



        this.cardLayout = (CardLayout) loginPanel.getLayout();

        usernameInputField.setMargin(new Insets(30,20,10,10));
        weightInputField.setMargin(new Insets(30,20,10,10));
        heightInputField.setMargin(new Insets(30,20,10,10));
        maleRadioButton.setSelected(false);
        femaleRadioButton.setSelected(false);
        alertLabel.setForeground(Color.RED);

        finishButton.putClientProperty("JButton.buttonType", "roundRect");
        finishButton.putClientProperty("Flatlaf.style", "arc:20");

        maleRadioButton.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                femaleRadioButton.setEnabled(false);
                sexSelected = "male";
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                femaleRadioButton.setEnabled(true);
                sexSelected = "none";
            }
        });


        femaleRadioButton.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                maleRadioButton.setEnabled(false);
                sexSelected = "female";
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                maleRadioButton.setEnabled(true);
                sexSelected = "none";
            }
        });


        finishButton.addActionListener(new ActionListener( ) {
            @Override
            public void actionPerformed(ActionEvent e) {

             if(!usernameInputField.getText().isEmpty() && !weightInputField.getText().isEmpty() && !heightInputField.getText().isEmpty()){
                if(InputValidator.isValidDouble(weightInputField.getText())&&InputValidator.isValidDouble(heightInputField.getText())&& InputValidator.isValidInteger(ageField.getText()))
                {
                    if(!sexSelected.equals("none"))
                    {
                        if(Double.parseDouble(weightInputField.getText()) >= 30 )
                        {
                            String trimmed = usernameInputField.getText().replaceAll("\\s+", "");
                            LocalDataBaseHelper localDataBaseHelper = new LocalDataBaseHelper();
                            if(localDataBaseHelper.checkUniqueUsername(trimmed) != 1)
                            {
                                localDataBaseHelper.updateWH(userID, trimmed, Double.parseDouble(weightInputField.getText()), MetricsCalculator.convertToMeters(Double.parseDouble(heightInputField.getText()) ), sexSelected, Integer.parseInt(ageField.getText()));
                                JOptionPane.showMessageDialog(null, "Account Succesffully Created!");


                                SelectPreference selectPreference = new SelectPreference(userID, loginPanel);

                                loginPanel.add(selectPreference.getPrefPanel(), "PreferencePanel");
                                cardLayout.show(loginPanel, "PreferencePanel");


                            }
                            else
                            {
                                alertLabel.setText("Username is already in use");
                            }
                        }
                        else
                        {
                            alertLabel.setText("Enter a minimum weight");
                        }
                    }
                    else
                    {
                        alertLabel.setText("Please select your sex");
                    }
                }
                else
                {
                    alertLabel.setText("Please enter the correct data");
                }
             }
             else
             {
                 alertLabel.setText("Please fill all fields");
             }


            }
        });
    }

    public JPanel getSignUpDataPanel() {
        return signUpDataPanel;

    }

}




