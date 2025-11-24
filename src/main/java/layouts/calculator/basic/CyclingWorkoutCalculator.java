package layouts.calculator.basic;

import calculationModels.basic.CyclingWorkout;
import com.formdev.flatlaf.FlatClientProperties;
import layouts.calculator.Stopwatch;
import objects.Account;
import raven.datetime.DatePicker;
import raven.datetime.TimePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class CyclingWorkoutCalculator  {

    private JPanel MainPanel;
    private JPanel JPanel2;
    private JLabel TitleLabel;
    private JLabel DurationLabel;
    private JLabel DurationDisplay;
    //private JTextField WeightField;
    private JLabel DistanceLabel;
    private JTextField DistanceField;
    private JLabel IntensityLabel;
    private JComboBox<String> IntensityComboB;
    private JButton CalculateButton;

    private JPanel stopWatchArea;
    private JTextArea outputTextArea;
    private TimePicker timePicker;
    private TimePicker timePicker2;
    private JFormattedTextField startTimeField;
    private JFormattedTextField endTimeField;
    private JFormattedTextField startDateField;
    private JFormattedTextField endDateField;

    JLabel stopwatchButton;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

    public CyclingWorkoutCalculator(Account account) {

        CardLayout cardLayout = new CardLayout();
        stopWatchArea.setLayout(cardLayout);
        Stopwatch stopwatch = new Stopwatch();
        stopwatchButton = stopwatch.getStartStopButton();

        stopWatchArea.add(stopwatch.getPanel(), "stopwatch" );
        cardLayout.show(stopWatchArea, "stopwatch");

        JPanel2.putClientProperty(FlatClientProperties.STYLE, "arc:20");

        DatePicker datePicker = new DatePicker();
        DatePicker datePicker2 = new DatePicker();
        datePicker.setEditor(startDateField);
        datePicker.setColor(new Color(220, 228, 55));
        datePicker.setBackground(new Color(255, 255, 255));
        datePicker.setForeground(new Color(17, 60, 67));

        datePicker2.setEditor(endDateField);
        datePicker2.setColor(new Color(220, 228, 55));
        datePicker2.setBackground(new Color(255, 255, 255));
        datePicker2.setForeground(new Color(17, 60, 67));

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


        timePicker = new TimePicker();
        timePicker.setColor(new  Color(220, 228, 55));
        timePicker.setEditor(startTimeField); // link popup to field
        timePicker2 = new TimePicker();
        timePicker2.setColor(new  Color(220, 228, 55));
        timePicker2.setEditor(endTimeField);

       // setContentPane(JPanel2);
        //  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // pack();
        //  setSize(300, 400);
        // setLocationRelativeTo(null);
        //  setVisible(true);

        CalculateButton.addActionListener(e -> {
            try {
                double duration = Double.parseDouble(DurationDisplay.getText());
                //double weight = Double.parseDouble(WeightField.getText());
                double weight = account.getWeight();
                double distance = Double.parseDouble(DistanceField.getText());
                String intensity = (String) IntensityComboB.getSelectedItem();

                LocalDateTime dateTime = LocalDateTime.now();

                CyclingWorkout cycle = new CyclingWorkout(duration, weight, dateTime, dateTime, distance, intensity);

                StringBuilder output = new StringBuilder();
                output.append("Workout: Cycling\n");
                output.append("Date: ").append(dateTime.toLocalDate()).append("\n");
                output.append("Time: ").append(dateTime.toLocalTime().withSecond(0).withNano(0)).append("\n");
                output.append("Calories burned: ").append(String.format("%.2f", cycle.calculateCaloriesBurned())).append("\n");
                output.append("Distance: ").append(String.format("%.2f", cycle.getDistanceKM())).append(" km\n");
                output.append("Average speed: ").append(String.format("%.2f", cycle.getSpeedKPH())).append(" km/h\n");
                output.append("Intensity: ").append(cycle.getIntensity()).append("\n");

                outputTextArea.setText(output.toString());
                outputTextArea.setForeground(Color.BLACK);

                clearFields();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(MainPanel,
                        "Error: Please fill all fields correctly.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        stopwatchButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                DurationDisplay.setText(stopwatch.getDurationMinutes());


                LocalDateTime startDT = stopwatch.getStartDT();
                timePicker.setSelectedTime(startDT.toLocalTime());
                startDateField.setValue(startDT.toLocalTime().format(formatter));

                LocalDateTime endDT = stopwatch.getEndDT();
                timePicker2.setSelectedTime(startDT.toLocalTime());
                endTimeField.setValue(endDT.toLocalTime().format(formatter));
            }
        });
    }

    private void updateDurationField(TimePicker startTimePicker, TimePicker endTimePicker, DatePicker startDatePicker, DatePicker endDatePicker) {
        if (startTimePicker.getSelectedTime() != null && endTimePicker.getSelectedTime() != null && startDatePicker.getSelectedDate() != null && endDatePicker.getSelectedDate() != null) {
            LocalDateTime start = LocalDateTime.of(startDatePicker.getSelectedDate(),
                    startTimePicker.getSelectedTime());

            LocalDateTime end = LocalDateTime.of(endDatePicker.getSelectedDate(),
                    endTimePicker.getSelectedTime());

            long diffSeconds = Duration.between(start, end).getSeconds();
            double diffMinutes = diffSeconds / 60.0;

            System.out.println("Duration: " + diffMinutes);

            if (diffMinutes < 0) {
                DurationDisplay.setText("0");
            } else {
                DurationDisplay.setText("" + diffMinutes);
            }
        } else {
            System.out.println("Incomplete StartEndTime");
        }
    }

    public void clearFields()
    {
        DurationDisplay.setText("");
       // WeightField.setText("");
        DistanceField.setText("");
        IntensityComboB.setSelectedIndex(0);

    }

    public JPanel getPanel() {
        return MainPanel;
    }
}