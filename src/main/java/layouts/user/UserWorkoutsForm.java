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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import layouts.calculator.Stopwatch;
import raven.datetime.DatePicker;
import raven.datetime.TimePicker;
import tracker.WorkoutTracker;

import javax.swing.*;
import java.awt.*;

public class UserWorkoutsForm extends JFrame {
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

    private JPanel stopWatchArea;
    private JPanel DateTimeArea;
    private JPanel Recorder;
    private JPanel Cardio;
    private JPanel StrengthTB;
    private JButton pushButton;
    private JButton pullButton;
    private JButton legsButton;
    private JButton exercisesButton3;
    private JPanel Strength;

    TimePicker timePicker;
    TimePicker timePicker2;
    private LocalDateTime lastStartDT;
    private LocalDateTime lastEndDT;
    private double lastDurationMinutes;

    JLabel stopwatchButton;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

    private JFormattedTextField startTimeField;
    private JFormattedTextField endTimeField;
    private JFormattedTextField endDateField;
    private JFormattedTextField startDateField;
    private JPanel panel1;
    private JLabel timeLabel;
    private JLabel startPauseButton;
    private JLabel stopButton;

    // === Workout Calculators (CLASS FIELDS) ===
    private WalkingWorkoutCalculator walkingCalc;
    private RunningWorkoutCalculator runningCalc;
    private CyclingWorkoutCalculator cyclingCalc;

    private JumpingJacksCalculator jumpingJacksCalc;
    private JumpingRopeCalculator jumpingRopeCalc;
    private BurpeesCalculator burpeesCalc;

    private PushCalculator pushCalc;
    private PullCalculator pullCalc;
    private LegCalculator legsCalc;


    private CardLayout calcLayout;

    private final Account account;
    private final LocalDataBaseHelper db;

    public UserWorkoutsForm(LocalDataBaseHelper db, Account account) {
        this.db = db;
        this.account = account;

        CardLayout cardLayout = new CardLayout();
        stopWatchArea.setLayout(cardLayout);
        Stopwatch stopwatch = new Stopwatch();
        stopwatchButton = stopwatch.getStartStopButton();

        stopWatchArea.add(stopwatch.getPanel(), "stopwatch");
        cardLayout.show(stopWatchArea, "stopwatch");

        calculatorArea.setLayout(new CardLayout());
        calcLayout = (CardLayout) calculatorArea.getLayout();

        DatePicker datePicker = new DatePicker();
        DatePicker datePicker2 = new DatePicker();
        datePicker.setEditor(startDateField);
        datePicker.setColor(new Color(220, 228, 55));
        datePicker.setBackground(new Color(255, 255, 255));
        datePicker.setForeground(new Color(17, 60, 67));
        datePicker.setSelectedDate(LocalDate.now());

        datePicker2.setEditor(endDateField);
        datePicker2.setColor(new Color(220, 228, 55));
        datePicker2.setBackground(new Color(255, 255, 255));
        datePicker2.setForeground(new Color(17, 60, 67));
        datePicker2.setSelectedDate(LocalDate.now());

        timePicker = new TimePicker();
        timePicker.setColor(new Color(220, 228, 55));
        timePicker.setEditor(startTimeField); // link popup to field
        timePicker.setBackground(new Color(255, 255, 255));
        timePicker.setForeground(new Color(17, 60, 67));

        timePicker2 = new TimePicker();
        timePicker2.setBackground(new Color(255, 255, 255));
        timePicker2.setColor(new Color(220, 228, 55));
        timePicker2.setForeground(new Color(17, 60, 67));
        timePicker2.setEditor(endTimeField);

        BasicTB.setVisible(false);
        CardioTB.setVisible(false);
        StrengthTB.setVisible(false);
        calculatorArea.setVisible(false);

        exercisesButton1.setFocusable(false);
        exercisesButton2.setFocusable(false);
        exercisesButton3.setFocusable(false);

        startDateField.addPropertyChangeListener("value", evt -> {
            Object newValue = evt.getNewValue();
            System.out.println("Value changed: " + newValue);
            updateDurationField(timePicker, timePicker2, datePicker, datePicker2);
        });
        endDateField.addPropertyChangeListener("value", evt -> {
            Object newValue = evt.getNewValue();
            System.out.println("Value changed: " + newValue);
            updateDurationField(timePicker, timePicker2, datePicker, datePicker2);
        });
        endTimeField.addPropertyChangeListener("value", evt -> {
            Object newValue = evt.getNewValue();
            System.out.println("Value changed: " + newValue);
            updateDurationField(timePicker, timePicker2, datePicker, datePicker2);
        });
        startTimeField.addPropertyChangeListener("value", evt -> {
            Object newValue = evt.getNewValue();
            System.out.println("Value changed: " + newValue);
            updateDurationField(timePicker, timePicker2, datePicker, datePicker2);
        });

        walkingCalc = new WalkingWorkoutCalculator(account);
        runningCalc = new RunningWorkoutCalculator(account);
        cyclingCalc = new CyclingWorkoutCalculator(account);

        jumpingJacksCalc = new JumpingJacksCalculator(account);
        jumpingRopeCalc = new JumpingRopeCalculator(account);
        burpeesCalc = new BurpeesCalculator(account);

        pushCalc = new PushCalculator(account);
        pullCalc = new PullCalculator(account);
        legsCalc = new LegCalculator(account);

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

    private void updateDurationField(TimePicker startTP, TimePicker endTP,
                                     DatePicker startDP, DatePicker endDP) {

        if (startTP.getSelectedTime() == null ||
                endTP.getSelectedTime() == null ||
                startDP.getSelectedDate() == null ||
                endDP.getSelectedDate() == null) {
            return;
        }

        LocalDateTime start = LocalDateTime.of(startDP.getSelectedDate(),
                startTP.getSelectedTime());
        LocalDateTime end = LocalDateTime.of(endDP.getSelectedDate(),
                endTP.getSelectedTime());

        long sec = Duration.between(start, end).getSeconds();
        double minutes = sec / 60.0;

        if (minutes < 0) minutes = 0;

        double rounded = Math.round(minutes * 10.0) / 10.0;

        lastStartDT = start;
        lastEndDT = end;
        lastDurationMinutes = rounded;

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

        BasicTB.setVisible(false);
        CardioTB.setVisible(false);
        StrengthTB.setVisible(false);
    }

    public JPanel getUserWorkoutsPanel() {
        return userWorkoutsPanel;
    }

}
