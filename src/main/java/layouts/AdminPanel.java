package layouts;


import DAO.LocalDataBaseHelper;
import DAO.OnlineDataBaseHelper;
import DAO.SyncManager;
import layouts.admin.Activities;
import layouts.admin.AdminDashboard;
import layouts.admin.Calculations;
import objects.Account;
import objects.Admin;
import objects.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.formdev.flatlaf.FlatClientProperties;



public class AdminPanel extends JFrame {

    private JPanel dashBoardPanel;
    private JTable accountsTable;
    private JButton updateButton;
    private JButton removeButton;
    private JTextField userIDField;
    private JScrollPane scrollPane;

    private JPanel accountStat;
    private JTextField emailField;
    private JTextField passwordField;
    private JTextField heightField;
    private JTextField weightField;
    private JTextField bmiField;
    private JTextField usernameField;
    private JTextField sexField;
    private JTextField ageField;
    private JButton insertButton;
    private JLabel refreshTable;
    private JTextField servOriginField;
    private JTextField privilegeField;
    private JPanel sidebar;
    private JPanel calLossStat;
    private JPanel weightLossStat;
    private JPanel activityStat;
    private JLabel accCounter;
    private JLabel adminCounter;
    private JLabel userCounter;
    private JLabel localCount;
    private JLabel foreignCount;
    private JPanel statsPanel;
    private JPanel emptyStat;
    private JTable onlineAccountsTable;
    private JPanel accountsPanel;
    private JLabel logoutButton;
    private JPanel adminContainer;
    private JPanel panelToo;
    private JLabel usernameDisplay;
    private JButton generateButton;
    private JButton clearFieldButton;
    private JButton dashboardButton;
    private JButton accountsButton;
    private JButton activitiesButton;
    private JButton calculationButton;
    private JPanel titleArea;
    private JLabel exitButton;
    private JTextField searchField;
    private JLabel searchButton;
    private JTextField createDTField;
    private JTextField prefField;
    private JLabel matches;
    private JPanel fieldsPanel;
    private JPanel tablesPanel;
    private JPanel accountStats;
    private JPanel localDBPanel;
    private JPanel onlineDBPanel;
    private JTextField lastUpDTField;
    private JComboBox comboBox1;
    private JLabel limboCount;
    private JLabel refreshOnlineTable;
    private JButton syncButton;
    private JTextArea syncLogs;
    private JTextArea syncLogArea;
    private CardLayout cardLayout;

    protected static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private LocalDataBaseHelper dataBaseHelper ;
    private Account accountInSession;
    private Point initialClick;



    public AdminPanel(long SessionAdmin) {
        dataBaseHelper = new LocalDataBaseHelper();
        accountInSession = dataBaseHelper.getAccount( SessionAdmin);
        usernameDisplay.setText(accountInSession.getUsername());

        ImageIcon icon = new ImageIcon("src/main/resources/images/logo.png");
        setIconImage(icon.getImage());

        setContentPane(dashBoardPanel);
        cardLayout = new CardLayout();
        adminContainer.setLayout(cardLayout);
        adminContainer.add(accountsPanel, "accounts");

        Activities activities = new Activities();
        AdminDashboard adminDashboard = new AdminDashboard();
        Calculations calculations = new Calculations();

        adminContainer.add(activities.getActivitiesPanel(), "activities");
        adminContainer.add(adminDashboard.getAdminDashPanel(), "adminDashboard");
        adminContainer.add(calculations.getCalculationsPanel(), "calculations");
        cardLayout.show(adminContainer, "accounts");

        setBackground(new Color(255, 255, 255));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(1300    , 800);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("Edit");
        JMenuItem removeItem = new JMenuItem("Remove");
        popupMenu.add(editItem);
        popupMenu.add(removeItem);
        createTable();
        arcSetup();
        setStats();


        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
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


        activitiesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(adminContainer, "activities");
                resetButton();
                activitiesButton.setBackground(new Color(31, 52, 62));
                activitiesButton.setForeground(new Color(220, 228, 55));

            }
        });

        accountsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(adminContainer, "accounts");
                resetButton();
                accountsButton.setBackground(new Color(31, 52, 62));
                accountsButton.setForeground(new Color(220, 228, 55));
            }
        });

        dashboardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(adminContainer, "adminDashboard");
                resetButton();
                dashboardButton.setBackground(new Color(31, 52, 62));
                dashboardButton.setForeground(new Color(220, 228, 55));

            }
        });

        calculationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(adminContainer, "calculations");
                resetButton();
                calculationButton.setBackground(new Color(31, 52, 62));
                calculationButton.setForeground(new Color(220, 228, 55));
            }
        });
        clearFieldButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearTextFields();
            }
        });


        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    LoginForm loginForm = new LoginForm();
                    loginForm.setVisible(true);
                    AdminPanel.this.dispose();


                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }


            }
        });

        accountsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.isPopupTrigger()) showMenu(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.isPopupTrigger()) showMenu(e);
            }

            private void showMenu(MouseEvent e) {
                int row = accountsTable.rowAtPoint(e.getPoint());
                if(row >= 0 && row < accountsTable.getRowCount()) {
                    accountsTable.setRowSelectionInterval(row, row);
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        editItem.addActionListener(e -> {
            int selectedRow = accountsTable.getSelectedRow();
            if (selectedRow != -1) {
                userIDField.setText(accountsTable.getValueAt(selectedRow, 1).toString());
                emailField.setText(accountsTable.getValueAt(selectedRow, 2).toString());
                passwordField.setText(accountsTable.getValueAt(selectedRow, 3).toString());
                privilegeField.setText(accountsTable.getValueAt(selectedRow, 4).toString());
                usernameField.setText(accountsTable.getValueAt(selectedRow, 5).toString());
                sexField.setText(accountsTable.getValueAt(selectedRow, 6).toString());
                ageField.setText(accountsTable.getValueAt(selectedRow, 7).toString());
                weightField.setText(accountsTable.getValueAt(selectedRow, 8).toString());
                heightField.setText(accountsTable.getValueAt(selectedRow, 9).toString());
                bmiField.setText(accountsTable.getValueAt(selectedRow, 10).toString());
                servOriginField.setText(accountsTable.getValueAt(selectedRow, 11).toString());
                prefField.setText(accountsTable.getValueAt(selectedRow, 12).toString());
                createDTField.setText(accountsTable.getValueAt(selectedRow, 13).toString());
                lastUpDTField.setText(accountsTable.getValueAt(selectedRow, 14).toString());
            }
        });

        removeItem.addActionListener(e -> {
            int selectedRow = accountsTable.getSelectedRow();
            if (selectedRow != -1) {
                Object userID = accountsTable.getValueAt(selectedRow, 1);
                // Confirm and remove user from DB
            }
        });
        syncButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SyncManager  syncManager = new SyncManager();
                try {
                    syncManager.compareAccountsODB();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                syncManager.getSyncLogs(syncLogs);
            }

        });


        updateButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(userIDField.getText().isEmpty() || emailField.getText().isEmpty() || passwordField.getText().isEmpty() || privilegeField.getText().isEmpty() || usernameField.getText().isEmpty()|| sexField.getText().isEmpty() || ageField.getText().isEmpty() || heightField.getText().isEmpty() || weightField.getText().isEmpty() || bmiField.getText().isEmpty() || servOriginField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(AdminPanel.this, "Please enter all the fields correctly.");
                }
                else {
                    LocalDataBaseHelper localDataBaseHelper = new LocalDataBaseHelper();
                    localDataBaseHelper.updateAll(Long.parseLong(userIDField.getText().trim()), emailField.getText(), passwordField.getText(),
                            Integer.parseInt(privilegeField.getText()),usernameField.getText() , sexField.getText(),
                            Integer.parseInt(ageField.getText()), Double.parseDouble(weightField.getText()),
                            Double.parseDouble(heightField.getText()), Double.parseDouble(bmiField.getText()),
                            servOriginField.getText(), Integer.parseInt(prefField.getText()),createDTField.getText(),
                            LocalDateTime.now().format(formatter));
                    createTable();
                    clearTextFields();

                }
            }
        });


        insertButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dataBaseHelper = new LocalDataBaseHelper();
                if( !userIDField.getText().isEmpty() && !emailField.getText().isEmpty() && !passwordField.getText().isEmpty() && !privilegeField.getText().isEmpty() &&  !usernameField.getText().isEmpty() && !sexField.getText().isEmpty() && !ageField.getText().isEmpty() && !heightField.getText().isEmpty() && !weightField.getText().isEmpty() && !bmiField.getText().isEmpty() && !servOriginField.getText().isEmpty()) {
                    if( dataBaseHelper.checkUserExists(Long.parseLong(userIDField.getText())) != 1)
                    {
                        LocalDataBaseHelper localDataBaseHelper = new LocalDataBaseHelper();
                        LocalDateTime localDateTime = LocalDateTime.now();


                        if(Integer.parseInt(privilegeField.getText()) == 1 )
                        {

                           Admin admin = new Admin(Long.parseLong(userIDField.getText()), emailField.getText(), passwordField.getText(), localDateTime.format(formatter));
                           admin.setupAccount(usernameField.getText(), sexField.getText(), Integer.parseInt(ageField.getText()), Double.parseDouble(weightField.getText()), Double.parseDouble(heightField.getText()), Double.parseDouble(bmiField.getText()), servOriginField.getText(), 1);
                           localDataBaseHelper.insertUser(admin.getId(), admin.getEmail(), admin.getPassword(), admin.getPrivilege(), admin.getCreationDT(),admin.getLastUpdatedDT());
                           localDataBaseHelper.updateWH(admin.getId(), admin.getUsername(), admin.getWeight(), admin.getHeight(), admin.getSex(), admin.getAge());
                           createTable();
                           clearTextFields();

                        }
                        else if (Integer.parseInt(privilegeField.getText()) == 0) {
                            User user = new User(Long.parseLong(userIDField.getText()), emailField.getText(), passwordField.getText(), localDateTime.format(formatter));
                            user.setupAccount(usernameField.getText(), sexField.getText(), Integer.parseInt(ageField.getText()), Double.parseDouble(weightField.getText()), Double.parseDouble(heightField.getText()), Double.parseDouble(bmiField.getText()), servOriginField.getText(), 1);
                            localDataBaseHelper.insertUser(user.getId(), user.getEmail(), user.getPassword(), user.getPrivilege(), user.getCreationDT(), user.getLastUpdatedDT());
                            localDataBaseHelper.updateWH(user.getId(), user.getUsername(), user.getWeight(), user.getHeight(), user.getSex(), user.getAge());
                            createTable();
                            clearTextFields();

                        }
                        setStats();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(AdminPanel.this, "Account already exists.");
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(AdminPanel.this, "Please fill all fields.");
                }
            }
        });


        removeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dataBaseHelper = new LocalDataBaseHelper();
                if(dataBaseHelper.checkUserExists(Long.parseLong(userIDField.getText())) == 1)
                {
                    LocalDateTime localDateTime = LocalDateTime.now();
                    dataBaseHelper.removeUser(Long.parseLong(userIDField.getText()), localDateTime.format(formatter));
                    createTable();
                    clearTextFields();
                    setStats();
                }
                else
                {
                    JOptionPane.showMessageDialog(AdminPanel.this, "User does not exist.");
                }
            }
        });
        refreshTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setStats();
                createTable();
            }

        });
        refreshOnlineTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                OnlineDataBaseHelper onlineDBHelper = new OnlineDataBaseHelper();
                onlineAccountsTable.setModel(onlineDBHelper.getAccountsTableModelOnline());
            }
        });

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userIDField.setText(String.valueOf(generateAccountID()));
            }
        });



        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dataBaseHelper  = new LocalDataBaseHelper();
                accountsTable.setModel(dataBaseHelper.searchAccounts(searchField.getText(), comboBox1.getSelectedItem().toString().toLowerCase()));
                matches.setText(String.valueOf(accountsTable.getRowCount()) + " matches");


            }
        });
    }

    private void arcSetup() {
        updateButton.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        removeButton.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        clearFieldButton.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        refreshTable.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        insertButton.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        emptyStat.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        accountStat.putClientProperty(FlatClientProperties.STYLE,  "arc:20");
        calLossStat.putClientProperty(FlatClientProperties.STYLE,  "arc:20");
        weightLossStat.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        accountStat.putClientProperty(FlatClientProperties.STYLE,  "arc:20");
        activityStat.putClientProperty(FlatClientProperties.STYLE,  "arc:20");
        sidebar.putClientProperty(FlatClientProperties.STYLE,  "arc:20");
        panelToo.putClientProperty(FlatClientProperties.STYLE,  "arc:20");
        fieldsPanel.putClientProperty(FlatClientProperties.STYLE,  "arc:20");
        statsPanel.putClientProperty(FlatClientProperties.STYLE,  "arc:20");
        tablesPanel.putClientProperty(FlatClientProperties.STYLE,  "arc:20");
        accountStats.putClientProperty(FlatClientProperties.STYLE,  "arc:20");
        localDBPanel.putClientProperty(FlatClientProperties.STYLE,  "arc:20");
        onlineDBPanel.putClientProperty(FlatClientProperties.STYLE,  "arc:20");
    }

    private void setStats() {
        accCounter.setText( String.valueOf(dataBaseHelper.getRowCount(0)));
        adminCounter.setText("Admins: "+ String.valueOf(dataBaseHelper.getRowCount(1)));
        userCounter.setText("Users: "+ String.valueOf(dataBaseHelper.getRowCount(2)));
        localCount.setText("Local: "+ String.valueOf(dataBaseHelper.getRowCount(3)));
        foreignCount.setText("Foreign: "+ String.valueOf(dataBaseHelper.getRowCount(0)- dataBaseHelper.getRowCount(3)));
        limboCount.setText("Accounts in Limbo: "+ String.valueOf(dataBaseHelper.getRowCount(4)));
    }

    private void clearTextFields() {
        userIDField.setText("");
        emailField.setText("");
        passwordField.setText("");
        privilegeField.setText("");
        usernameField.setText("");
        sexField.setText("");
        ageField.setText("");
        heightField.setText("");
        weightField.setText("");
        bmiField.setText("");
        servOriginField.setText("");
        createDTField.setText("");
        lastUpDTField.setText("");
        prefField.setText("");

    }

    /*

    public Dashboard(){
    setContentPane(dashBoardPanel);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("Dashboard");
    setSize(1080    , 720);
    setLocationRelativeTo(null);
    setVisible(true);


    }
    */
    public void createTable() {
       dataBaseHelper = new LocalDataBaseHelper();
       accountsTable.setModel(new LocalDataBaseHelper().getAccountsTableModel());

       OnlineDataBaseHelper onlineDBHelper = new OnlineDataBaseHelper();

       onlineAccountsTable.setModel( onlineDBHelper.getAccountsTableModelOnline() );
       SyncManager syncManager = new SyncManager();
       syncManager.getSyncLogs(syncLogs);


    }
    public void resetButton()
    {
        dashboardButton.setBackground(new  Color(17, 37, 44));
        dashboardButton.setForeground(new Color(79, 96, 115));
        activitiesButton.setBackground(new  Color(17, 37, 44));
        activitiesButton.setForeground(new Color(79, 96, 115));
        accountsButton.setBackground(new  Color(17, 37, 44));
        accountsButton.setForeground(new Color(79, 96, 115));
        calculationButton.setBackground(new  Color(17, 37, 44));
        calculationButton.setForeground(new Color(79, 96, 115));
    }

    public JPanel getAccountsPanel() {
        return accountsPanel;
    }

    public JPanel getDashBoardPanel() {
        return dashBoardPanel;
    }


    public static long generateAccountID() {
        long timestamp = System.currentTimeMillis(); // 13-digit value
        return timestamp;
    }
}
