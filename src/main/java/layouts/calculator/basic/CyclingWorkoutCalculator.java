package layouts.calculator.basic;

import calculationModels.basic.CyclingWorkout;
import objects.Account;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

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
    private JTextArea outputTextArea;

    public CyclingWorkoutCalculator(Account account) {

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



    }
    //for implementation/interface overload


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