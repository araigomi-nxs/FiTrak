package layouts.calculator.strength;

import calculationModels.strength.PushWorkout;
import javax.swing.*;
import java.awt.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import objects.Account;
import tracker.WorkoutTracker;

public class PushCalculator extends JFrame {

    private JPanel MainPanel;
    private JPanel JPanel2;
    private JLabel TitleLabel;
    private JCheckBox yesCheckBox;
    private JCheckBox noCheckBox;
    private JLabel DurationLabel;
    private JLabel DurationDisplay;
    private JTextField WeightField;
    private JLabel SetsLabel;
    private JLabel RepsLabel;
    private JTextField SetsField;
    private JTextField RepsField;
    private JTextField WeightLiftedField;
    private JLabel WeightLiftedLabel;
    private JLabel IntensityLabel;
    private JComboBox<String> IntensityComboB;
    private JTextField RestTimeField;
    private JLabel RestTimeLabel;
    private JButton calculateButton;
    private JTextArea outputTextArea;
    private JLabel UseEquipmentLabel;
    private JButton saveButton;

    private LocalDateTime externalStartDT;
    private LocalDateTime externalEndDT;
    private double externalDurationMinutes;

    private static final DateTimeFormatter timeFormatter12hr = DateTimeFormatter.ofPattern("hh:mm a");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    private PushWorkout push;

    private boolean hasCalculated = false;

    public PushCalculator(Account account) {
        setContentPane(MainPanel);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(300, 400);
//        setLocationRelativeTo(null);

        saveButton.setEnabled(false);
        saveButton.setVisible(false);

        WeightLiftedField.setEnabled(false);
        noCheckBox.setSelected(true);

        yesCheckBox.addActionListener(e -> {
            if (yesCheckBox.isSelected()) {
                noCheckBox.setSelected(false);
                WeightLiftedField.setEnabled(true);
            } else {
                WeightLiftedField.setEnabled(false);
            }
        });

        noCheckBox.addActionListener(e -> {
            if (noCheckBox.isSelected()) {
                yesCheckBox.setSelected(false);
                WeightLiftedField.setText("");
                WeightLiftedField.setEnabled(false);
            }
        });

        calculateButton.addActionListener(e -> {
            if (hasCalculated) {
                JOptionPane.showMessageDialog(MainPanel,
                        "Please press 'Calculate Again' before doing another calculation!",
                        "Notice", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                //double duration = Double.parseDouble(DurationDisplay.getText());
//                double bodyWeight = Double.parseDouble(WeightField.getText());
                double weight = account.getWeight();
                int sets = Integer.parseInt(SetsField.getText());
                int reps = Integer.parseInt(RepsField.getText());
                int restTime = Integer.parseInt(RestTimeField.getText());
                double weightLifted = WeightLiftedField.getText().isEmpty() ? 0 : Double.parseDouble(WeightLiftedField.getText());
                String intensity = (String) IntensityComboB.getSelectedItem();
                boolean useEquipment = yesCheckBox.isSelected();

                double duration = externalDurationMinutes;
                if (duration <= 0) {
                    duration = Double.parseDouble(DurationDisplay.getText());
                }
                duration = Math.round(duration * 10) / 10.0;

                LocalDateTime startDT = (externalStartDT != null) ? externalStartDT : LocalDateTime.now();
                LocalDateTime endDT = (externalEndDT != null) ? externalEndDT : LocalDateTime.now();

                push = new PushWorkout(duration, weight, startDT,endDT,
                        sets, reps, weightLifted, intensity, restTime, useEquipment);

                StringBuilder output = new StringBuilder();
                output.append("Workout: Push\n");
                output.append("Date: ").append(startDT.toLocalDate().format(dateFormatter)).append("\n");
                output.append("Start Time: ").append(startDT.toLocalTime().format(timeFormatter12hr)).append("\n");
                output.append("End Time: ").append(endDT.toLocalTime().format(timeFormatter12hr)).append("\n");
                output.append("Calories burned: ").append(String.format("%.2f", push.calculateCaloriesBurned())).append("\n");
                output.append("Sets: ").append(push.getSets()).append("\n");
                output.append("Reps per set: ").append(push.getReps()).append("\n");
                output.append("Weight Lifted: ").append(push.getWeightLiftedKG()).append(" kg\n");
                output.append("Rest Time: ").append(push.getRestTimeSeconds()).append(" sec\n");
                output.append("Intensity: ").append(push.getIntensity()).append("\n");
                output.append("Use Equipment: ").append(useEquipment ? "Yes" : "No").append("\n");

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
            if (push != null) {
                WorkoutTracker.logWorkout(account.getId(), push);

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

        public void clearFields () {
            DurationDisplay.setText("");
            SetsField.setText("");
            RepsField.setText("");
            WeightLiftedField.setText("");
            RestTimeField.setText("");
            IntensityComboB.setSelectedIndex(0);
            yesCheckBox.setSelected(false);
            noCheckBox.setSelected(true);
            WeightLiftedField.setEnabled(false);

            saveButton.setEnabled(false);
            saveButton.setVisible(false);

        }

        public JPanel getPanel() {
        return MainPanel;
        }

    }

