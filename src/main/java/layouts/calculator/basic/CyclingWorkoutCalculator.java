package layouts.calculator.basic;

import calculationModels.basic.CyclingWorkout;
import com.formdev.flatlaf.FlatClientProperties;
import objects.Account;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class CyclingWorkoutCalculator {

    private JPanel MainPanel;
    private JPanel JPanel2;
    private JLabel TitleLabel;
    private JLabel DurationLabel;
    private JLabel DurationDisplay;
    private JLabel DistanceLabel;
    private JTextField DistanceField;
    private JLabel IntensityLabel;
    private JComboBox<String> IntensityComboB;
    private JButton CalculateButton;

    private JTextArea outputTextArea;
    private JButton saveButton;

    private LocalDateTime externalStartDT;
    private LocalDateTime externalEndDT;
    private double externalDurationMinutes;

    private CyclingWorkout cycl;

    public CyclingWorkoutCalculator(Account account) {

        JPanel2.putClientProperty(FlatClientProperties.STYLE, "arc:20");

        CalculateButton.addActionListener(e -> {
            try {
                double weight = account.getWeight();
                double distance = Double.parseDouble(DistanceField.getText());
                String intensity = (String) IntensityComboB.getSelectedItem();

                double duration = externalDurationMinutes;
                if (duration <= 0) {
                    duration = Double.parseDouble(DurationDisplay.getText());
                }

                duration = Math.round(duration * 10) / 10.0;

                LocalDateTime startDT = (externalStartDT != null) ? externalStartDT : LocalDateTime.now();
                LocalDateTime endDT   = (externalEndDT != null)   ? externalEndDT   : LocalDateTime.now();

                LocalDateTime dateTime = LocalDateTime.now();

                cycl = new CyclingWorkout(duration, weight, startDT, endDT, distance, intensity);

                StringBuilder output = new StringBuilder();
                output.append("Workout: Cycling\n");
                output.append("Date: ").append(startDT.toLocalDate()).append("\n");
                output.append("Start Time: ").append(startDT.toLocalTime().withSecond(0).withNano(0)).append("\n");
                output.append("End Time: ").append(endDT.toLocalTime().withSecond(0).withNano(0)).append("\n");
                output.append("Calories burned: ").append(String.format("%.2f", cycl.calculateCaloriesBurned())).append("\n");
                output.append("Distance: ").append(String.format("%.2f", cycl.getDistanceKM())).append(" km\n");
                output.append("Average speed: ").append(String.format("%.2f", cycl.getSpeedKPH())).append(" km/h\n");
                output.append("Intensity: ").append(cycl.getIntensity()).append("\n");

                outputTextArea.setText(output.toString());
                outputTextArea.setForeground(Color.BLACK);
                outputTextArea.revalidate();
                outputTextArea.repaint();

                clearFields();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        MainPanel,
                        "Error: Please fill all fields correctly.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

//        saveButton.addActionListener(e -> {
//            if (cycl != null) {
//                // Replace with your saving logic if necessary
//            }
//        });
    }

    public void setExternalWorkoutData(LocalDateTime start, LocalDateTime end, double durationMinutes) {
        double rounded = Math.round(durationMinutes * 10.0) / 10.0;
        DurationDisplay.setText(String.format("%.1f", rounded));

        this.externalStartDT = start;
        this.externalEndDT = end;
        this.externalDurationMinutes = durationMinutes;
    }


    public void clearFields() {
        DurationDisplay.setText("");
        DistanceField.setText("");
        IntensityComboB.setSelectedIndex(0);
    }

    public JPanel getPanel() {
        return MainPanel;
    }
}
