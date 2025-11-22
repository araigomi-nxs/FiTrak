package layouts.calculator.basic;

import calculationModels.basic.CyclingWorkout;
import layouts.calculator.Stopwatch;
import objects.Account;
import raven.datetime.TimePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class CyclingWorkoutCalculator  {

    private JPanel MainPanel;
    private JPanel JPanel2;
    private JLabel TitleLabel;
    private JLabel DurationLabel;
    private JTextField DurationField;
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
    private JFormattedTextField StartTimeField;
    private JFormattedTextField EndTimeField;

    JLabel stopwatchButton;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

    public CyclingWorkoutCalculator(Account account) {

        CardLayout cardLayout = new CardLayout();
        stopWatchArea.setLayout(cardLayout);
        Stopwatch stopwatch = new Stopwatch();
        stopwatchButton = stopwatch.getStartStopButton();

        stopWatchArea.add(stopwatch.getPanel(), "stopwatch" );
        cardLayout.show(stopWatchArea, "stopwatch");

        timePicker = new TimePicker();
        timePicker.setColor(new  Color(220, 228, 55));
        timePicker.setEditor(StartTimeField); // link popup to field
        timePicker2 = new TimePicker();
        timePicker2.setColor(new  Color(220, 228, 55));
        timePicker2.setEditor(EndTimeField);

       // setContentPane(JPanel2);
        //  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // pack();
        //  setSize(300, 400);
        // setLocationRelativeTo(null);
        //  setVisible(true);


        CalculateButton.addActionListener(e -> {
            try {
                double duration = Double.parseDouble(DurationField.getText());
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

                DurationField.setText(stopwatch.getDurationMinutes());

    }
    //for implementation/interface overload

                LocalDateTime startDT = stopwatch.getStartDT();
                timePicker.setSelectedTime(startDT.toLocalTime());
                StartTimeField.setValue(startDT.toLocalTime().format(formatter));

                LocalDateTime endDT = stopwatch.getEndDT();
                timePicker2.setSelectedTime(startDT.toLocalTime());
                EndTimeField.setValue(endDT.toLocalTime().format(formatter));
            }
        });
    }

    public void clearFields()
    {
        DurationField.setText("");
       // WeightField.setText("");
        DistanceField.setText("");
        IntensityComboB.setSelectedIndex(0);

    }

    public JPanel getPanel() {
        return MainPanel;
    }
}