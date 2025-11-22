package layouts.calculator.basic;

import calculationModels.basic.RunningWorkout;
import layouts.calculator.Stopwatch;
import objects.Account;
import raven.datetime.TimePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class RunningWorkoutCalculator {

    private JPanel MainPanel;
    private JPanel JPanel2;
    private JTextField DurationField;
    //private JTextField WeightField;
    private JTextField DistanceField;
    private JComboBox IntensityComboB;
    private JComboBox TerrainComboB;
    private JLabel DistanceLabel;
    private JLabel IntensityLabel;
    private JLabel TerrainLabel;
    private JLabel DurationLabel;
    private JButton CalculateButton;

    private JTextArea outputTextArea;
    private JPanel stopWatchArea;
    private TimePicker timePicker;
    private TimePicker timePicker2;
    private JFormattedTextField StartTimeField;
    private JFormattedTextField EndTimeField;

    JLabel stopwatchButton;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

    public RunningWorkoutCalculator(Account  account) {

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

        CalculateButton.addActionListener(e -> {
            try {
                double duration = Double.parseDouble(DurationField.getText());
               // double weight = Double.parseDouble(WeightField.getText());
                double weight = account.getWeight();
                double distance = Double.parseDouble(DistanceField.getText());
                String intensity = (String) IntensityComboB.getSelectedItem();
                String terrain = (String) TerrainComboB.getSelectedItem();

                LocalDateTime dateTime = LocalDateTime.now();

                RunningWorkout run = new RunningWorkout(duration, weight, dateTime,dateTime, distance, intensity, terrain);

                StringBuilder output = new StringBuilder();
                output.append("Workout: Running\n");
                output.append("Date: ").append(dateTime.toLocalDate()).append("\n");
                output.append("Time: ").append(dateTime.toLocalTime().withSecond(0).withNano(0)).append("\n");
                output.append("Calories burned: ").append(String.format("%.2f", run.calculateCaloriesBurned())).append("\n");
                output.append("Distance: ").append(String.format("%.2f", run.getDistanceKM())).append(" km\n");
                output.append("Average Speed: ").append(String.format("%.2f", run.getSpeedKPH())).append(" km/h\n");
                output.append("Intensity: ").append(run.getIntensity()).append("\n");
                output.append("Terrain: ").append(run.getTerrain()).append("\n");

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

        stopwatchButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                DurationField.setText(stopwatch.getDurationMinutes());


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
        TerrainComboB.setSelectedIndex(0);

    }
    public JPanel getPanel() {
        return MainPanel;
    }
}
