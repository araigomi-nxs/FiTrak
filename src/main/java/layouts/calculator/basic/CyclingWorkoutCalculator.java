package layouts.calculator.basic;

import calculationModels.basic.CyclingWorkout;
import com.formdev.flatlaf.FlatClientProperties;
import objects.Account;
import tracker.WorkoutTracker;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    private static final DateTimeFormatter timeFormatter12hr = DateTimeFormatter.ofPattern("hh:mm a");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    private CyclingWorkout cycl;

    public CyclingWorkoutCalculator(Account account) {

        JPanel2.putClientProperty(FlatClientProperties.STYLE, "arc:20");

        saveButton.setEnabled(false);
        saveButton.setVisible(false);

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
                output.append("Date: ").append(startDT.toLocalDate().format(dateFormatter)).append("\n");
                output.append("Start Time: ").append(startDT.toLocalTime().format(timeFormatter12hr)).append("\n");
                output.append("End Time: ").append(endDT.toLocalTime().format(timeFormatter12hr)).append("\n");
                output.append("Calories burned: ").append(String.format("%.2f", cycl.calculateCaloriesBurned())).append("\n");
                output.append("Distance: ").append(String.format("%.2f", cycl.getDistanceKM())).append(" km\n");
                output.append("Average speed: ").append(String.format("%.2f", cycl.getSpeedKPH())).append(" km/h\n");
                output.append("Intensity: ").append(cycl.getIntensity()).append("\n");

                outputTextArea.setText(output.toString());
                outputTextArea.setForeground(Color.BLACK);
                outputTextArea.revalidate();
                outputTextArea.repaint();

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
            if (cycl != null) {
                WorkoutTracker.logWorkout(account.getId(), cycl);

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


    public void clearFields() {
        DurationDisplay.setText("");
        DistanceField.setText("");
        IntensityComboB.setSelectedIndex(0);

        saveButton.setEnabled(false);
        saveButton.setVisible(false);
    }

    public JPanel getPanel() {
        return MainPanel;
    }
}
