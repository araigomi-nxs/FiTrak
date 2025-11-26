package layouts.calculator.cardio;

import calculationModels.cardio.Burpees;
import javax.swing.*;
import java.awt.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import objects.Account;
import tracker.WorkoutTracker;

public class BurpeesCalculator extends JFrame {

    private JPanel MainPanel;
    private JPanel JPanel2;
    private JLabel TitleLabel;
    private JLabel DurationDisplay;
    private JTextField WeightField;
    private JComboBox<String> IntensityComboB;
    private JTextField SetsField;
    private JTextField RepsField;
    private JTextField RestTimeField;
//    private JTextField AgeField;
    private JTextField HeartRateField;
    private JButton calculateButton;
    private JTextArea outputTextArea;
    private JLabel DurationLabel;
    private JLabel SetsLabel;
    private JLabel RepsLabel;
    private JLabel RestTimeLabel;
    private JLabel HeartRateLabel;
    private JLabel IntensityLabel;
    private JButton saveButton;

    private LocalDateTime externalStartDT;
    private LocalDateTime externalEndDT;
    private double externalDurationMinutes;

    private static final DateTimeFormatter timeFormatter12hr = DateTimeFormatter.ofPattern("hh:mm a");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    private Burpees burpees;

    private boolean hasCalculated = false;

    public BurpeesCalculator(Account account) {
        setContentPane(MainPanel);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(300, 400);
//        setLocationRelativeTo(null);

        saveButton.setEnabled(false);
        saveButton.setVisible(false);

        if (outputTextArea != null) {
            outputTextArea.setLayout(new BorderLayout());
            outputTextArea.setLineWrap(true);
            outputTextArea.setWrapStyleWord(true);
        }

        calculateButton.addActionListener(e -> {
            if (hasCalculated) {
                JOptionPane.showMessageDialog(MainPanel,
                        "Please press 'Calculate Again' before doing another calculation!",
                        "Notice", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
//                double weight = Double.parseDouble(WeightField.getText());
                double weight = account.getWeight();
                int sets = Integer.parseInt(SetsField.getText());
                int reps = Integer.parseInt(RepsField.getText());
                int restTime = Integer.parseInt(RestTimeField.getText());
                double age = account.getAge();
                double heartRate = HeartRateField.getText().isEmpty() ? 0 : Double.parseDouble(HeartRateField.getText());
                String intensity = (String) IntensityComboB.getSelectedItem();

                double duration = externalDurationMinutes;
                if (duration <= 0) {
                    duration = Double.parseDouble(DurationDisplay.getText());
                }
                duration = Math.round(duration * 10) / 10.0;

                LocalDateTime startDT = (externalStartDT != null) ? externalStartDT : LocalDateTime.now();
                LocalDateTime endDT = (externalEndDT != null) ? externalEndDT : LocalDateTime.now();

                LocalDateTime dateTime = LocalDateTime.now();

                burpees = new Burpees(duration, weight, startDT,dateTime, intensity,sets, reps, restTime, age, heartRate);

                StringBuilder output = new StringBuilder();
                output.append("Workout: Burpees\n");
                output.append("Date: ").append(startDT.toLocalDate().format(dateFormatter)).append("\n");
                output.append("Start Time: ").append(startDT.toLocalTime().format(timeFormatter12hr)).append("\n");
                output.append("End Time: ").append(endDT.toLocalTime().format(timeFormatter12hr)).append("\n");
                output.append("Calories burned: ").append(String.format("%.2f", burpees.calculateCaloriesBurned())).append("\n");
                output.append("Sets: ").append(burpees.getSets()).append("\n");
                output.append("Reps per set: ").append(burpees.getReps()).append("\n");
                output.append("Rest time: ").append(burpees.getRestTimeSeconds()).append(" sec\n");
                output.append("Intensity: ").append(intensity).append("\n");
                if (heartRate > 0) output.append("Heart Rate: ").append(heartRate).append(" bpm\n");
                output.append("Age: ").append(age).append("\n");

                outputTextArea.setText(output.toString());
                outputTextArea.setForeground(Color.BLACK);
                outputTextArea.revalidate();
                outputTextArea.repaint();

                saveButton.setEnabled(true);
                saveButton.setVisible(true);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(MainPanel,
                        "Error: Please fill all fields correctly.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        saveButton.addActionListener(e -> {
            if (burpees != null) {
                WorkoutTracker.logWorkout(account.getId(), burpees);

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
            SetsField.setText("");
            RepsField.setText("");
            RestTimeField.setText("");
            HeartRateField.setText("");
            IntensityComboB.setSelectedIndex(0);

            saveButton.setEnabled(false);
            saveButton.setVisible(false);

        }
        public JPanel getPanel() {
            return MainPanel;
        }

    }

