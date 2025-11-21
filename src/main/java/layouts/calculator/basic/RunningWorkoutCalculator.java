package layouts.calculator.basic;

import calculationModels.basic.RunningWorkout;
import objects.Account;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

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

    private JPanel OutputPanel;
    private JTextArea outputTextArea;


    public RunningWorkoutCalculator(Account  account) {

        //setContentPane(JPanel2);
       //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //pack();
       // setSize(300, 400);
       // setLocationRelativeTo(null);
       // setVisible(true);







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
