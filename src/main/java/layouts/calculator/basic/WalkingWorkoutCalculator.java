package layouts.calculator.basic;

import calculationModels.basic.WalkingWorkout;
import layouts.calculator.Stopwatch;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class WalkingWorkoutCalculator{

    private JPanel MainPanel;
    private JPanel JPanel2;
    private JLabel TitleLabel;
    private JTextField DurationField;
    private JLabel DurationLabel;
    private JTextField WeightField;
    private JLabel WeightLabel;
    private JTextField StepsField;
    private JLabel StepsLabel;
    private JTextField HeightField;
    private JLabel HeightLabel;
    private JComboBox IntensityComboB;
    private JLabel IntensityLabel;
    private JComboBox SexComboB;
    private JLabel SexLabel;
    private JButton calculateButton;

    private JTextArea outputTextArea;
    private JPanel stopWatchArea;

    //JButton button = stopwatch.getButton


    public WalkingWorkoutCalculator() {

       // setContentPane(JPanel2);
       // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      //  pack();
       // setSize(300, 400);
       // setLocationRelativeTo(null);
       // setVisible(true);

        CardLayout cardLayout = new CardLayout();
        stopWatchArea.setLayout(cardLayout);
        Stopwatch stopwatch = new Stopwatch();

        stopWatchArea.add(stopwatch.getPanel(), "stopwatch" );
        cardLayout.show(stopWatchArea, "stopwatch");




        calculateButton.addActionListener(e -> {
            try {
                double duration = Double.parseDouble(DurationField.getText());
                double weight = Double.parseDouble(WeightField.getText());
                int steps = Integer.parseInt(StepsField.getText());
                double height = Double.parseDouble(HeightField.getText());
                String intensity = (String) IntensityComboB.getSelectedItem();
                String sex = (String) SexComboB.getSelectedItem();

                LocalDateTime dateTime = LocalDateTime.now();

                WalkingWorkout walk = new WalkingWorkout(duration, weight, dateTime, steps, intensity, sex, height);

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

    }


    public void clearFields()
    {

        DurationField.setText("");
        WeightField.setText("");
        StepsField.setText("");
        HeightField.setText("");
        IntensityComboB.setSelectedIndex(0);
        SexComboB.setSelectedIndex(0);

    }

    public JPanel getPanel() {
        return MainPanel;
    }
}
