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

import javax.swing.*;
import java.awt.*;

public class UserWorkoutsForm extends JFrame {
    private JPanel userWorkoutsPanel;

    private JButton exercisesButton1;
    private JButton exercisesButton2;
    private JButton exercisesButton3;

    private JPanel Basic;
    private JPanel BasicTB;
    private JPanel CardioTB;
    private JPanel StrengthTB;
    private JPanel Cardio;
    private JPanel Strength;

    private JButton walkingButton;
    private JButton runningButton;
    private JButton cyclingButton;

    private JButton jumpingJacksButton;
    private JButton jumpingRopeButton;
    private JButton burpeesButton;

    private JButton pushButton;
    private JButton pullButton;
    private JButton legsButton;

    private JPanel calculatorArea;
    private JPanel stopWatchArea;
    private JPanel DateTimeArea;
    private JPanel Recorder;

    private JFormattedTextField startTimeField;
    private JFormattedTextField endTimeField;
    private JFormattedTextField endDateField;
    private JFormattedTextField startDateField;

    // Pickers must be class fields so listeners can access them
    private DatePicker datePicker;
    private DatePicker datePicker2;
    private TimePicker timePicker;
    private TimePicker timePicker2;

    private LocalDateTime lastStartDT;
    private LocalDateTime lastEndDT;
    private double lastDurationMinutes;

    private Stopwatch stopwatch;
    private CardLayout calcLayout;

    private final Account account;
    private final LocalDataBaseHelper db;

    // Calculators
    private WalkingWorkoutCalculator walkingCalc;
    private RunningWorkoutCalculator runningCalc;
    private CyclingWorkoutCalculator cyclingCalc;
    private JumpingJacksCalculator jumpingJacksCalc;
    private JumpingRopeCalculator jumpingRopeCalc;
    private BurpeesCalculator burpeesCalc;
    private PushCalculator pushCalc;
    private PullCalculator pullCalc;
    private LegCalculator legsCalc;

    // Track currently active calculator (no interface)
    private Object currentCalc;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

    public UserWorkoutsForm(LocalDataBaseHelper db, Account account) {
        this.db = db;
        this.account = account;

        // Stopwatch setup
        CardLayout cardLayout = new CardLayout();
        stopWatchArea.setLayout(cardLayout);
        stopwatch = new Stopwatch();
        stopWatchArea.add(stopwatch.getPanel(), "STOPWATCH");
        cardLayout.show(stopWatchArea, "STOPWATCH");

        // Date/Time pickers setup (class fields)
        datePicker = new DatePicker();
        datePicker2 = new DatePicker();
        datePicker.setEditor(startDateField);
        datePicker.setColor(new Color(220, 228, 55));
        datePicker.setBackground(Color.WHITE);
        datePicker.setForeground(new Color(17, 60, 67));
        datePicker.setSelectedDate(LocalDate.now());

        datePicker2.setEditor(endDateField);
        datePicker2.setColor(new Color(220, 228, 55));
        datePicker2.setBackground(Color.WHITE);
        datePicker2.setForeground(new Color(17, 60, 67));
        datePicker2.setSelectedDate(LocalDate.now());

        timePicker = new TimePicker();
        timePicker.setEditor(startTimeField);
        timePicker.setColor(new Color(220, 228, 55));
        timePicker.setBackground(Color.WHITE);
        timePicker.setForeground(new Color(17, 60, 67));

        timePicker2 = new TimePicker();
        timePicker2.setEditor(endTimeField);
        timePicker2.setColor(new Color(220, 228, 55));
        timePicker2.setBackground(Color.WHITE);
        timePicker2.setForeground(new Color(17, 60, 67));

        // Initial visibility
        BasicTB.setVisible(false);
        CardioTB.setVisible(false);
        StrengthTB.setVisible(false);
        calculatorArea.setVisible(false);

        exercisesButton1.setFocusable(false);
        exercisesButton2.setFocusable(false);
        exercisesButton3.setFocusable(false);

        // Property change listeners: manual time/date entry should update duration and active calculator
        startDateField.addPropertyChangeListener("value", evt ->
                updateDurationField(timePicker, timePicker2, datePicker, datePicker2)
        );
        endDateField.addPropertyChangeListener("value", evt ->
                updateDurationField(timePicker, timePicker2, datePicker, datePicker2)
        );
        startTimeField.addPropertyChangeListener("value", evt ->
                updateDurationField(timePicker, timePicker2, datePicker, datePicker2)
        );
        endTimeField.addPropertyChangeListener("value", evt ->
                updateDurationField(timePicker, timePicker2, datePicker, datePicker2)
        );

        // Calculators setup
        calculatorArea.setLayout(new CardLayout());
        calcLayout = (CardLayout) calculatorArea.getLayout();

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

        // Dropdown toggles
        exercisesButton1.addActionListener(e -> toggleDropdown(BasicTB));
        exercisesButton2.addActionListener(e -> toggleDropdown(CardioTB));
        exercisesButton3.addActionListener(e -> toggleDropdown(StrengthTB));

        // Calculator button actions
        walkingButton.addActionListener(e -> showCalc("WALK"));
        runningButton.addActionListener(e -> showCalc("RUN"));
        cyclingButton.addActionListener(e -> showCalc("CYCLE"));

        jumpingJacksButton.addActionListener(e -> showCalc("JUMP"));
        jumpingRopeButton.addActionListener(e -> showCalc("ROPE"));
        burpeesButton.addActionListener(e -> showCalc("BURPEE"));

        pushButton.addActionListener(e -> showCalc("PUSH"));
        pullButton.addActionListener(e -> showCalc("PULL"));
        legsButton.addActionListener(e -> showCalc("LEGS"));

        // Wire stopwatch stop to update pickers and active calculator
        stopwatch.getStopButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                LocalDateTime startDT = stopwatch.getStartDT();
                LocalDateTime endDT = stopwatch.getEndDT();
                double duration = stopwatch.getDuration();

                // Sync pickers
                if (startDT != null) {
                    datePicker.setSelectedDate(startDT.toLocalDate());
                    timePicker.setSelectedTime(startDT.toLocalTime());
                    startTimeField.setValue(startDT.toLocalTime().format(formatter));
                }
                if (endDT != null) {
                    datePicker2.setSelectedDate(endDT.toLocalDate());
                    timePicker2.setSelectedTime(endDT.toLocalTime());
                    endTimeField.setValue(endDT.toLocalTime().format(formatter));
                }

                // Store values
                lastStartDT = startDT;
                lastEndDT = endDT;
                lastDurationMinutes = Math.round(duration * 10.0) / 10.0;

                // 🔥 Push stopwatch duration directly into the active calculator
                pushDurationToActiveCalculator(lastStartDT, lastEndDT, lastDurationMinutes);
            }
        });
    }

    private void updateDurationField(TimePicker startTP, TimePicker endTP,
                                     DatePicker startDP, DatePicker endDP) {
        if (startTP.getSelectedTime() == null ||
                endTP.getSelectedTime() == null ||
                startDP.getSelectedDate() == null ||
                endDP.getSelectedDate() == null) {
            return;
        }

        LocalDateTime start = LocalDateTime.of(startDP.getSelectedDate(), startTP.getSelectedTime());
        LocalDateTime end = LocalDateTime.of(endDP.getSelectedDate(), endTP.getSelectedTime());

        long sec = Duration.between(start, end).getSeconds();
        double minutes = sec / 60.0;
        if (minutes < 0) minutes = 0;

        double rounded = Math.round(minutes * 10.0) / 10.0;

        lastStartDT = start;
        lastEndDT = end;
        lastDurationMinutes = rounded;

        // Push into currently active calculator
        pushDurationToActiveCalculator(lastStartDT, lastEndDT, lastDurationMinutes);
    }

    private void pushDurationToActiveCalculator(LocalDateTime start, LocalDateTime end, double durationMinutes) {
        if (currentCalc instanceof WalkingWorkoutCalculator) {
            ((WalkingWorkoutCalculator) currentCalc).setExternalWorkoutData(start, end, durationMinutes);
        } else if (currentCalc instanceof RunningWorkoutCalculator) {
            ((RunningWorkoutCalculator) currentCalc).setExternalWorkoutData(start, end, durationMinutes);
        } else if (currentCalc instanceof CyclingWorkoutCalculator) {
            ((CyclingWorkoutCalculator) currentCalc).setExternalWorkoutData(start, end, durationMinutes);
        } else if (currentCalc instanceof JumpingJacksCalculator) {
            ((JumpingJacksCalculator) currentCalc).setExternalWorkoutData(start, end, durationMinutes);
        } else if (currentCalc instanceof JumpingRopeCalculator) {
            ((JumpingRopeCalculator) currentCalc).setExternalWorkoutData(start, end, durationMinutes);
        } else if (currentCalc instanceof BurpeesCalculator) {
            ((BurpeesCalculator) currentCalc).setExternalWorkoutData(start, end, durationMinutes);
        } else if (currentCalc instanceof PushCalculator) {
            ((PushCalculator) currentCalc).setExternalWorkoutData(start, end, durationMinutes);
        } else if (currentCalc instanceof PullCalculator) {
            ((PullCalculator) currentCalc).setExternalWorkoutData(start, end, durationMinutes);
        } else if (currentCalc instanceof LegCalculator) {
            ((LegCalculator) currentCalc).setExternalWorkoutData(start, end, durationMinutes);
        }
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

        // Track the active calculator for duration pushes
        switch (name) {
            case "WALK": currentCalc = walkingCalc; break;
            case "RUN": currentCalc = runningCalc; break;
            case "CYCLE": currentCalc = cyclingCalc; break;
            case "JUMP": currentCalc = jumpingJacksCalc; break;
            case "ROPE": currentCalc = jumpingRopeCalc; break;
            case "BURPEE": currentCalc = burpeesCalc; break;
            case "PUSH": currentCalc = pushCalc; break;
            case "PULL": currentCalc = pullCalc; break;
            case "LEGS": currentCalc = legsCalc; break;
        }

        // Immediately push current duration to the newly active calculator (if we already have it)
        if (lastStartDT != null && lastEndDT != null) {
            pushDurationToActiveCalculator(lastStartDT, lastEndDT, lastDurationMinutes);
        }
    }

    public JPanel getUserWorkoutsPanel() {
        return userWorkoutsPanel;
    }
}