package layouts.user;

import DAO.LocalDataBaseHelper;
import objects.Account;

import layouts.calculator.basic.WalkingWorkoutCalculator;
import layouts.calculator.basic.RunningWorkoutCalculator;
import layouts.calculator.basic.CyclingWorkoutCalculator;

import layouts.calculator.cardio.JumpingJacksCalculator;
import layouts.calculator.cardio.JumpingRopeCalculator;
import layouts.calculator.cardio.BurpeesCalculator;

import layouts.calculator.strength.PushCalculator;
import layouts.calculator.strength.PullCalculator;
import layouts.calculator.strength.LegCalculator;

import javax.swing.*;
import java.awt.*;

public class UserWorkoutsForm extends JPanel {
    private JPanel userWorkoutsPanel;

    private JButton exercisesButton1;
    private JButton exercisesButton2;

    private JPanel Basic;

    private JPanel BasicTB;
    private JPanel CardioTB;

    private JButton walkingButton;
    private JButton runningButton;
    private JButton cyclingButton;

    private JButton jumpingJacksButton;
    private JButton jumpingRopeButton;
    private JButton burpeesButton;

    private JPanel calculatorArea;

    private JPanel stopwatchArea;
    private JPanel DateTimeArea;
    private JPanel Recorder;
    private JPanel Cardio;
    private JPanel StrengthTB;
    private JButton pushButton;
    private JButton pullButton;
    private JButton legsButton;
    private JButton exercisesButton3;

    private CardLayout calcLayout;

    private final Account account;
    private final LocalDataBaseHelper db;

    public UserWorkoutsForm(LocalDataBaseHelper db, Account account) {
        this.db = db;
        this.account = account;

        //$$$setupUI$$$();
        setLayout(new BorderLayout());
        add(userWorkoutsPanel, BorderLayout.CENTER);

        calculatorArea.setLayout(new CardLayout());
        calcLayout = (CardLayout) calculatorArea.getLayout();

        BasicTB.setVisible(false);
        CardioTB.setVisible(false);
        StrengthTB.setVisible(false);
        calculatorArea.setVisible(false);

        exercisesButton1.setFocusable(false);
        exercisesButton2.setFocusable(false);
        exercisesButton3.setFocusable(false);

        WalkingWorkoutCalculator walkingCalc = new WalkingWorkoutCalculator(account);
        RunningWorkoutCalculator runningCalc = new RunningWorkoutCalculator(account);
        CyclingWorkoutCalculator cyclingCalc = new CyclingWorkoutCalculator(account);

        JumpingJacksCalculator jumpingJacksCalc = new JumpingJacksCalculator(account);
        JumpingRopeCalculator jumpingRopeCalc = new JumpingRopeCalculator(account);
        BurpeesCalculator burpeesCalc = new BurpeesCalculator(account);

        PushCalculator pushCalc = new PushCalculator(account);
        PullCalculator pullCalc = new PullCalculator(account);
        LegCalculator legsCalc = new LegCalculator(account);

        calculatorArea.add(walkingCalc.getPanel(), "WALK");
        calculatorArea.add(runningCalc.getPanel(), "RUN");
        calculatorArea.add(cyclingCalc.getPanel(), "CYCLE");

        calculatorArea.add(jumpingJacksCalc.getPanel(), "JUMP");
        calculatorArea.add(jumpingRopeCalc.getPanel(), "ROPE");
        calculatorArea.add(burpeesCalc.getPanel(), "BURPEE");

        calculatorArea.add(pushCalc.getPanel(), "PUSH");
        calculatorArea.add(pullCalc.getPanel(), "PULL");
        calculatorArea.add(legsCalc.getPanel(), "LEGS");

        exercisesButton1.addActionListener(e -> toggleDropdown(BasicTB));
        exercisesButton2.addActionListener(e -> toggleDropdown(CardioTB));
        exercisesButton3.addActionListener(e -> toggleDropdown(StrengthTB));

        walkingButton.addActionListener(e -> showCalc("WALK"));
        runningButton.addActionListener(e -> showCalc("RUN"));
        cyclingButton.addActionListener(e -> showCalc("CYCLE"));

        jumpingJacksButton.addActionListener(e -> showCalc("JUMP"));
        jumpingRopeButton.addActionListener(e -> showCalc("ROPE"));
        burpeesButton.addActionListener(e -> showCalc("BURPEE"));

        pushButton.addActionListener(e -> showCalc("PUSH"));
        pullButton.addActionListener(e -> showCalc("PULL"));
        legsButton.addActionListener(e -> showCalc("LEGS"));
    }

    private void toggleDropdown(JPanel panel) {
        panel.setVisible(!panel.isVisible());
        calculatorArea.setVisible(false);
        panel.getParent().revalidate();
        panel.getParent().repaint();
    }

    private void showCalc(String name) {
        calculatorArea.setVisible(true);
        calcLayout.show(calculatorArea, name);
        calculatorArea.revalidate();
        calculatorArea.repaint();

        BasicTB.setVisible(false);
        CardioTB.setVisible(false);
        StrengthTB.setVisible(false);
    }

    public JPanel getUserWorkoutsPanel() {
        return userWorkoutsPanel;
    }
}
