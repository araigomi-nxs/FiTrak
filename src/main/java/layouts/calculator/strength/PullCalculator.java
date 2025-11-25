package layouts.calculator.strength;

import calculationModels.strength.PullWorkout;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import objects.Account;
import tracker.WorkoutTracker;

public class PullCalculator extends JFrame {

    private JPanel MainPanel;
    private JPanel JPanel2;
    private JLabel TitleLabel;
    private JLabel UseEquipmentLabel;
    private JCheckBox yesCheckBox;
    private JCheckBox noCheckBox;
    private JLabel DurationLabel;
    private JLabel RestTimeLabel;
    private JLabel IntensityLabel;
    private JButton calculateButton;
    private JTextArea OutputTextArea;
    private JLabel SetsLabel;
    private JLabel RepsLabel;
    private JComboBox<String> IntensityComboB;
    private JLabel DurationDisplay;
    private JTextField WeightField;
    private JTextField SetsField;
    private JTextField RepsField;
    private JTextField WeightLiftedField;
    private JTextField RestTimeField;
    private JLabel WeightLiftedLabel;
    private JButton saveButton;

    private LocalDateTime externalStartDT;
    private LocalDateTime externalEndDT;
    private double externalDurationMinutes;

    private boolean hasCalculated = false;

    public PullCalculator(Account account) {
        setContentPane(MainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 400);
        setLocationRelativeTo(null);

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
//                double duration = Double.parseDouble(DurationDisplay.getText());
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

                PullWorkout pull = new PullWorkout(duration, weight, startDT,endDT,
                        sets, reps, weightLifted, intensity, restTime, useEquipment);

                StringBuilder output = new StringBuilder();
                output.append("Workout: Pull\n");
                output.append("Date: ").append(startDT.toLocalDate()).append("\n");
                output.append("Start Time: ").append(startDT.toLocalTime().withSecond(0).withNano(0)).append("\n");
                output.append("End Time: ").append(endDT.toLocalTime().withSecond(0).withNano(0)).append("\n");
                output.append("Calories burned: ").append(String.format("%.2f", pull.calculateCaloriesBurned())).append("\n");
                output.append("Sets: ").append(pull.getSets()).append("\n");
                output.append("Reps per set: ").append(pull.getReps()).append("\n");
                output.append("Weight Lifted: ").append(pull.getWeightLiftedKG()).append(" kg\n");
                output.append("Rest Time: ").append(pull.getRestTimeSeconds()).append(" sec\n");
                output.append("Intensity: ").append(pull.getIntensity()).append("\n");
                output.append("Use Equipment: ").append(useEquipment ? "Yes" : "No").append("\n");

                OutputTextArea.setText(output.toString());
                OutputTextArea.setForeground(Color.BLACK);


            } catch (Exception ex) {
                JOptionPane.showMessageDialog(MainPanel,
                        "Error: Please fill all fields correctly.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

//        saveButton.addActionListener(e -> {
//            if (walk != null) {
//                WorkoutTracker.logWorkout(account.getId(), walk);
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
            SetsField.setText("");
            RepsField.setText("");
            WeightLiftedField.setText("");
            RestTimeField.setText("");
            IntensityComboB.setSelectedIndex(0);
            yesCheckBox.setSelected(false);
            noCheckBox.setSelected(true);
            WeightLiftedField.setEnabled(false);
        }
    public JPanel getPanel() {
        return MainPanel;
    }

}


