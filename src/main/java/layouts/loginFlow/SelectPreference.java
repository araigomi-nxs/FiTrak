package layouts.loginFlow;

import DAO.LocalDataBaseHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SelectPreference {
    private JButton finishButton;
    private JPanel basicWorkout;
    private JPanel selectPrefPanel;
    private JPanel strengthWorkout;
    private JPanel cardioWorkout;
    private int selectedPreference = 0;

    CardLayout cardLayout;


    SelectPreference(long userID, JPanel loginPanel) {

        basicWorkout.putClientProperty("FlatLaf.style", "arc:10");
        cardioWorkout.putClientProperty("FlatLaf.style", "arc:10");
        strengthWorkout.putClientProperty("FlatLaf.style", "arc:10");
        finishButton.putClientProperty("JButton.buttonType", "roundRect");
        finishButton.putClientProperty("FlatLaf.style", "arc:20");

        this.cardLayout = (CardLayout) loginPanel.getLayout();

        basicWorkout.addMouseListener(new  MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                selectedPreference = 1;
                clearBorder();
                basicWorkout.putClientProperty("JPanel.borderColor", new Color(220, 228, 55));
                basicWorkout.setBorder(BorderFactory.createLineBorder( new Color(220, 228, 55), 2));
                basicWorkout.putClientProperty("FlatLaf.style", "arc:10");

            }

        });
        cardioWorkout.addMouseListener(new  MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedPreference = 2;
                clearBorder();
                cardioWorkout.putClientProperty("JPanel.borderColor", new Color(220, 228, 55));
                cardioWorkout.setBorder(BorderFactory.createLineBorder( new Color(220, 228, 55), 2));
                basicWorkout.putClientProperty("FlatLaf.style", "arc:10");
            }
        });
        strengthWorkout.addMouseListener(new  MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedPreference = 3;
                clearBorder();
                strengthWorkout.putClientProperty("JPanel.borderColor", new Color(220, 228, 55));
                strengthWorkout.setBorder(BorderFactory.createLineBorder( new Color(220, 228, 55), 2));
                strengthWorkout.putClientProperty("FlatLaf.style", "arc:10");
            }
        });



        finishButton.addMouseListener(new  MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(selectedPreference>0){

                    LocalDataBaseHelper localDataBaseHelper = new LocalDataBaseHelper();
                    localDataBaseHelper.updatePref(userID, selectedPreference);
                    JOptionPane.showMessageDialog(null, "Account Succesfully Updated!");
                    clearBorder();

                    cardLayout.show(loginPanel, "LoginModule");
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "No Preference Selected!");

                }
            }
        });



    }

    private void clearBorder()
    {
        basicWorkout.putClientProperty("JPanel.borderColor", new Color(255, 255, 255));
        basicWorkout.setBorder(BorderFactory.createLineBorder( new Color(250, 250, 250), 2));
        strengthWorkout.putClientProperty("JPanel.borderColor", new Color(255, 255, 255));
        strengthWorkout.setBorder(BorderFactory.createLineBorder( new Color(250, 250, 250), 2));
        cardioWorkout.putClientProperty("JPanel.borderColor", new Color(250, 250, 250));
        cardioWorkout.setBorder(BorderFactory.createLineBorder( new Color(250, 250, 250), 2));
        basicWorkout.putClientProperty("FlatLaf.style", "arc:10");
        cardioWorkout.putClientProperty("FlatLaf.style", "arc:10");
        strengthWorkout.putClientProperty("FlatLaf.style", "arc:10");
    }

    public  JPanel getPrefPanel() {
        return selectPrefPanel;
    }

}
