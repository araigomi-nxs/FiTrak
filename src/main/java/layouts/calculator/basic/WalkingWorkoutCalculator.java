package layouts.calculator.basic;

import calculationModels.basic.WalkingWorkout;
import com.formdev.flatlaf.FlatClientProperties;
import objects.Account;
import tracker.WorkoutTracker;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class WalkingWorkoutCalculator extends JFrame  {

    private JPanel MainPanel;
    private JPanel JPanel2;
    private JLabel TitleLabel;
    private JLabel DurationLabel;
    private JLabel DurationDisplay;
    private JTextField StepsField;
    private JLabel StepsLabel;
    private JComboBox IntensityComboB;
    private JLabel IntensityLabel;
    private JButton calculateButton;
    private JTextArea outputTextArea;
    private JButton saveButton;

    private LocalDateTime externalStartDT;
    private LocalDateTime externalEndDT;
    private double externalDurationMinutes;

    private WalkingWorkout walk;

    public WalkingWorkoutCalculator(Account account) {
        JPanel2.putClientProperty(FlatClientProperties.STYLE, "arc:20");

        // 🔹 Save button starts disabled/hidden
        saveButton.setEnabled(false);
        saveButton.setVisible(false);

        calculateButton.addActionListener(e -> {
            try {
                int steps = Integer.parseInt(StepsField.getText());
                double weight = account.getWeight();
                double height = account.getHeight();
                String intensity = (String) IntensityComboB.getSelectedItem();
                String sex = account.getSex();

                double duration = externalDurationMinutes;
                if (duration <= 0) {
                    duration = Double.parseDouble(DurationDisplay.getText());
                }
                duration = Math.round(duration * 10) / 10.0;

                LocalDateTime startDT = (externalStartDT != null) ? externalStartDT : LocalDateTime.now();
                LocalDateTime endDT = (externalEndDT != null) ? externalEndDT : LocalDateTime.now();

                walk = new WalkingWorkout(duration, weight, startDT, endDT, steps, intensity, sex, height);

                StringBuilder output = new StringBuilder();
                output.append("Workout: Walking\n");
                output.append("Date: ").append(startDT.toLocalDate()).append("\n");
                output.append("Start Time: ").append(startDT.toLocalTime().withSecond(0).withNano(0)).append("\n");
                output.append("End Time: ").append(endDT.toLocalTime().withSecond(0).withNano(0)).append("\n");
                output.append("Duration (min): ").append(String.format("%.2f", duration)).append("\n");
                output.append("Calories burned: ").append(String.format("%.2f", walk.calculateCaloriesBurned())).append("\n");
                output.append("Distance walked: ").append(String.format("%.2f", walk.getDistanceKM())).append(" km\n");
                output.append("Steps: ").append(walk.getSteps()).append("\n");
                output.append("Intensity: ").append(walk.getIntensity()).append("\n");
                output.append("Gender: ").append(walk.getGender()).append("\n");

                outputTextArea.setText(output.toString());
                outputTextArea.setForeground(Color.BLACK);

                // 🔹 Enable Save button after successful calculation
                saveButton.setEnabled(true);
                saveButton.setVisible(true);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        MainPanel,
                        "Error: Please fill all fields correctly.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        saveButton.addActionListener(e -> {
            if (walk != null) {
                WorkoutTracker.logWorkout(account.getId(), walk);

                // ✅ Show success message
                JOptionPane.showMessageDialog(
                        MainPanel,
                        "Exercise saved!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                // Disable Save button after one use
                saveButton.setEnabled(false);
                saveButton.setVisible(false);
                outputTextArea.setText("");

                clearFields();
            }
        });
    }

    public void setExternalWorkoutData(LocalDateTime start, LocalDateTime end, double durationMinutes) {
        double rounded = Math.round(durationMinutes * 10.0) / 10.0;
        DurationDisplay.setText(String.format("%.1f", rounded));

        this.externalStartDT = start;
        this.externalEndDT = end;
        this.externalDurationMinutes = durationMinutes;
    }

    private void clearFields() {
        DurationDisplay.setText("");
        StepsField.setText("");
        IntensityComboB.setSelectedIndex(0);

        // 🔹 Reset Save button when fields are cleared
        saveButton.setEnabled(false);
        saveButton.setVisible(false);
    }

    public JPanel getPanel() {
        return MainPanel;
    }
}