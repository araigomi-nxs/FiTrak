package layouts;

import DAO.DataBaseHelper;

import javax.swing.*;

public class AdminPanel extends JFrame {

    private JPanel dashBoardPanel;
    private JTabbedPane tabbedPane1;
    private JTable accountsTable;
    private JTabbedPane tabbedPane2;
    private JScrollPane scrollPane;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;


    public AdminPanel() {
        createTable();
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
        DataBaseHelper dataBaseHelper = new DataBaseHelper();
        accountsTable.setModel(new DataBaseHelper().getAccountsTableModel());


    }

    public JPanel getDashBoardPanel() {
        return dashBoardPanel;
    }



}
