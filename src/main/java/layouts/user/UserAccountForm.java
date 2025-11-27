package layouts.user;

import calculationModels.metrics.MetricsCalculator;
import DAO.LocalDataBaseHelper;
import com.formdev.flatlaf.FlatClientProperties;
import objects.Account;

import javax.swing.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

public class UserAccountForm {

    private JPanel userAccountPanel;

    private JPanel password;
    private JPasswordField passwordField;
    private JButton editPasswordButton;
    private JButton saveButtonPass;

    private JButton editEmailButton;
    private JButton saveButtonEmail;

    private JPanel userID;
    private JLabel userIDDisplay;

    private JLabel privilegeDisplay;

    private JFormattedTextField weightField;
    private JButton editWeightButton;
    private JButton saveButtonWeight;

    private JPanel height;
    private JFormattedTextField heightField;
    private JButton editHeightButton;
    private JButton saveButtonHeight;

    private JPanel bmi;
    private JLabel bmiDisplay;

    private JPanel sex;
    private JLabel sexDisplay;

    private JPanel systInfo;
    private JFormattedTextField creationDntFormattedField;
    private JFormattedTextField lastUpdateDnTFormattedField;

    private JTextField emailTextField;

    private JPanel username;
    private JTextField usernameTextField;
    private JButton editUsernameButton;
    private JButton saveButtonUsrN;
    private JPanel titleBar;
    private JPanel weight;


    private final LocalDataBaseHelper db;
    private Account account;

    DecimalFormat df = new DecimalFormat("#.##");

    public UserAccountForm(LocalDataBaseHelper db, Account account) {
        this.db = db;
        this.account = account;

        initializeUI();
        loadAccountData();
        setupEditSaveButtons();


    }


    private void initializeUI() {
        saveButtonPass.setVisible(false);
        saveButtonEmail.setVisible(false);
        saveButtonWeight.setVisible(false);
        saveButtonHeight.setVisible(false);
        saveButtonUsrN.setVisible(false);

        passwordField.setEnabled(false);
        emailTextField.setEnabled(false);
        weightField.setEnabled(false);
        heightField.setEnabled(false);
        usernameTextField.setEnabled(false);

        userAccountPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20");
    }


    private void loadAccountData() {
        if (account == null) return;



        userIDDisplay.setText(String.valueOf(account.getId()));
        privilegeDisplay.setText(String.valueOf(account.getPrivilege()));   // DISPLAY ONLY

        passwordField.setText(account.getPassword());
        emailTextField.setText(account.getEmail());
        usernameTextField.setText(account.getUsername());

        weightField.setText(String.valueOf(account.getWeight()));
        heightField.setText(String.valueOf(account.getHeight()));
        bmiDisplay.setText(String.valueOf(account.getBMI()));

        sexDisplay.setText(account.getSex());

        creationDntFormattedField.setText(account.getCreationDT());
        lastUpdateDnTFormattedField.setText(account.getLastUpdatedDT());
    }


    private void setupEditSaveButtons() {
        editUsernameButton.addActionListener(e -> toggle(usernameTextField, saveButtonUsrN));
        saveButtonUsrN.addActionListener(e ->
                handleSave("username", usernameTextField, saveButtonUsrN));

        editEmailButton.addActionListener(e -> toggle(emailTextField, saveButtonEmail));
        saveButtonEmail.addActionListener(e ->
                handleSave("email", emailTextField, saveButtonEmail));

        editPasswordButton.addActionListener(e -> toggle(passwordField, saveButtonPass));
        saveButtonPass.addActionListener(e ->
                handleSave("password", passwordField, saveButtonPass));

        editWeightButton.addActionListener(e -> toggle(weightField, saveButtonWeight));
        saveButtonWeight.addActionListener(e ->
                handleSave("weight", weightField, saveButtonWeight));

        editHeightButton.addActionListener(e -> toggle(heightField, saveButtonHeight));
        saveButtonHeight.addActionListener(e ->
                handleSave("height", heightField, saveButtonHeight));
    }


    private void toggle(JComponent field, JButton saveButton) {
        boolean enable = !field.isEnabled();
        field.setEnabled(enable);
        saveButton.setVisible(enable);

        if (enable) field.requestFocus();
    }


    private void updateValue(String type) {
        try {
            long id = Long.parseLong(userIDDisplay.getText());
            String email = emailTextField.getText();
            String password = new String(passwordField.getPassword());
            int privilege = Integer.parseInt(privilegeDisplay.getText());
            String username = usernameTextField.getText();
            String sex = sexDisplay.getText();
            int age = account.getAge(); // Not editable, so keep the existing one
            double weight = Double.parseDouble(weightField.getText());
            double height = Double.parseDouble(heightField.getText());
            String serverOrigin = account.getServerOrigin();
            int preference = 0;
            String creationDT = creationDntFormattedField.getText();
            String lastUpdated = LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            // Recalculate BMI ALWAYS based on UI values
            double heightMeters = MetricsCalculator.convertToMeters(height);
            double bmi = MetricsCalculator.calculateBMI(weight, heightMeters);
            bmiDisplay.setText(String.format("%.2f", bmi));

            // Update in-memory account object
            account = new Account(
                    id, email, password, privilege, username,
                    creationDT, lastUpdated, weight, height, bmi,
                    age, sex, serverOrigin, preference
            );

            // Save to DB
            db.updateAll(
                    id, email, password, privilege, username,
                    sex, age, weight, height, bmi,
                    serverOrigin, preference, creationDT, lastUpdated
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Invalid input.");
        }
    }



    private void handleSave(String type, JComponent field, JButton saveButton) {
        updateValue(type);
        toggle(field, saveButton);
        JOptionPane.showMessageDialog(null, "Update saved");
    }

    public JPanel getUserAccountPanel() {
        return userAccountPanel;
    }
}
