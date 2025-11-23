package layouts.calculator.basic;

import calculationModels.basic.WalkingWorkout;
import com.formdev.flatlaf.FlatClientProperties;
import layouts.calculator.Stopwatch;
import objects.Account;
import raven.datetime.DatePicker;
import raven.datetime.TimePicker;
import tracker.WorkoutTracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class WalkingWorkoutCalculator{

    private JPanel MainPanel;
    private JPanel JPanel2;
    private JLabel TitleLabel;
    private JTextField DurationField;
    //private JTextField WeightField;
    private JTextField StepsField;
    private JLabel StepsLabel;
    //private JTextField HeightField;
    private JComboBox IntensityComboB;
    private JLabel IntensityLabel;

    //private JComboBox SexComboB;

    private JButton calculateButton;

    private JTextArea outputTextArea;
    private JPanel stopWatchArea;
      private JFormattedTextField startTimeField;
    private JFormattedTextField endTimeField;
    private JFormattedTextField endDateField;
    private JFormattedTextField startDateField;
    private JLabel DurationLabel;
    TimePicker timePicker;
    TimePicker timePicker2;

    private JButton saveButton;

    JLabel stopwatchButton;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

    WalkingWorkout walk;
    public WalkingWorkoutCalculator(Account account) {

       // setContentPane(JPanel2);
       // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      //  pack();
       // setSize(300, 400);
       // setLocationRelativeTo(null);
       // setVisible(true);

        CardLayout cardLayout = new CardLayout();
        stopWatchArea.setLayout(cardLayout);
        Stopwatch stopwatch = new Stopwatch();
        stopwatchButton = stopwatch.getStartStopButton();



        stopWatchArea.add(stopwatch.getPanel(), "stopwatch" );
        cardLayout.show(stopWatchArea, "stopwatch");

        JPanel2.putClientProperty(FlatClientProperties.STYLE,  "arc:20");



        DatePicker datePicker = new DatePicker();
        DatePicker datePicker2 = new DatePicker();
        datePicker.setEditor(startDateField);
        datePicker.setColor(new  Color(220, 228, 55));
        datePicker.setBackground(new  Color(255, 255, 255));
        datePicker.setForeground(new  Color(17, 60, 67));
        datePicker.setSelectedDate(LocalDate.now());

        datePicker2.setEditor(endDateField);
        datePicker2.setColor(new  Color(220, 228, 55));
        datePicker2.setBackground(new  Color(255, 255, 255));
        datePicker2.setForeground(new  Color(17, 60, 67));
        datePicker2.setSelectedDate(LocalDate.now());

        timePicker = new TimePicker();
        timePicker.setColor(new  Color(220, 228, 55));
        timePicker.setEditor(startTimeField); // link popup to field
        timePicker.setBackground(new  Color(255, 255, 255));
        timePicker.setForeground(new  Color(17, 60, 67));

       timePicker2 = new TimePicker();
        timePicker2.setBackground(new  Color(255, 255, 255));
        timePicker2.setColor(new  Color(220, 228, 55));
        timePicker2.setForeground(new  Color(17, 60, 67));
        timePicker2.setEditor(endTimeField);


        startDateField.addPropertyChangeListener("value", evt -> {
            Object newValue = evt.getNewValue();
            System.out.println("Value changed: " + newValue);
            updateDurationField(timePicker, timePicker2,datePicker, datePicker2);
        });
        endDateField.addPropertyChangeListener("value", evt -> {
            Object newValue = evt.getNewValue();
            System.out.println("Value changed: " + newValue);
            updateDurationField(timePicker, timePicker2,datePicker, datePicker2);
        });
        endTimeField.addPropertyChangeListener("value", evt -> {
            Object newValue = evt.getNewValue();
            System.out.println("Value changed: " + newValue);
            updateDurationField(timePicker, timePicker2,datePicker, datePicker2);
        });
        startTimeField.addPropertyChangeListener("value", evt -> {
            Object newValue = evt.getNewValue();
            System.out.println("Value changed: " + newValue);
            updateDurationField(timePicker, timePicker2,datePicker, datePicker2);
        });




        calculateButton.addActionListener(e -> {
            try {
                double duration = Double.parseDouble(DurationField.getText());
                //double weight = Double.parseDouble(WeightField.getText());
                double weight = account.getWeight();
                int steps = Integer.parseInt(StepsField.getText());
                //double height = Double.parseDouble(HeightField.getText());
                double height = account.getHeight();


                String intensity = (String) IntensityComboB.getSelectedItem();
                //String sex = (String) SexComboB.getSelectedItem();
                String sex = account.getSex();

                LocalDateTime dateTime = LocalDateTime.now();

                walk = new WalkingWorkout(duration, weight, dateTime,dateTime, steps, intensity, sex, height);

                StringBuilder output = new StringBuilder();

                output.append("Workout: Walking\n");
                output.append("Date: ").append(dateTime.toLocalDate()).append("\n");
                output.append("Time: ").append(dateTime.toLocalTime().withSecond(0).withNano(0)).append("\n");
                output.append("Calories burned: ").append(String.format("%.2f", walk.calculateCaloriesBurned())).append("\n");
                output.append("Distance walked: ").append(String.format("%.2f", walk.getDistanceKM())).append(" km\n");
                output.append("Steps: ").append(walk.getSteps()).append("\n");
                output.append("Intensity: ").append(walk.getIntensity()).append("\n");
                output.append("Gender: ").append(walk.getGender()).append("\n");

                outputTextArea.setText(output.toString());
                outputTextArea.setForeground(Color.BLACK);
                outputTextArea.revalidate();
                outputTextArea.repaint();

                clearFields();


            } catch (Exception ex) {
                JOptionPane.showMessageDialog(MainPanel,
                        "Error: Please fill all fields correctly.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        saveButton.addActionListener(e -> {

            if(walk!= null)
            {
                WorkoutTracker.logWorkout(account.getId(),walk);
            }

        });


        stopwatchButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                DurationField.setText(stopwatch.getDurationMinutes());


                LocalDateTime startDT = stopwatch.getStartDT();
                timePicker.setSelectedTime(startDT.toLocalTime());
                datePicker.setSelectedDate(startDT.toLocalDate());
                startTimeField.setValue(startDT.toLocalTime().format(formatter));

                LocalDateTime endDT = stopwatch.getEndDT();

                timePicker2.setSelectedTime(startDT.toLocalTime());
                datePicker2.setSelectedDate(startDT.toLocalDate());
                endTimeField.setValue(endDT.toLocalTime().format(formatter));

            }
        });
    }

    private  void updateDurationField(TimePicker startTimePicker, TimePicker endTimePicker, DatePicker startDatePicker, DatePicker endDatePicker)
    {
       if(startTimePicker.getSelectedTime() != null && endTimePicker.getSelectedTime() != null && startDatePicker.getSelectedDate() != null && endDatePicker.getSelectedDate() != null )
       {
           LocalDateTime start = LocalDateTime.of(startDatePicker.getSelectedDate(),
                   startTimePicker.getSelectedTime());

           LocalDateTime end = LocalDateTime.of(endDatePicker.getSelectedDate(),
                   endTimePicker.getSelectedTime());

           long diffSeconds = Duration.between(start, end).getSeconds();
           double diffMinutes = diffSeconds / 60.0;

           System.out.println("Duration: " + diffMinutes);

           if(diffMinutes <0 )
           {
               DurationField.setText("0");
           }
           else
           {
               DurationField.setText("" + diffMinutes);
           }
       }
       else
       {
           System.out.println("Incomplete StartEndTime");
       }
    }


    private void clearFields()
    {

        DurationField.setText("");
        //WeightField.setText("");
        StepsField.setText("");
        //HeightField.setText("");
        IntensityComboB.setSelectedIndex(0);
        //SexComboB.setSelectedIndex(0);

    }

    public JPanel getPanel() {
        return MainPanel;
    }
}
