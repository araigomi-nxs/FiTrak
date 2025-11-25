package layouts.calculator.cardio;

import calculationModels.cardio.JumpingJacks;
import objects.Account;
import tracker.WorkoutTracker;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class JumpingJacksCalculator extends JFrame {

    private JPanel MainPanel;
    private JPanel Jpanel2;
    private JLabel DurationDisplay;
    private JLabel DurationLabel;
    private JComboBox<String> IntensityComboB;
    private JLabel IntensityLabel;
    private JTextField SetsField;
    private JLabel SetsLabel;
    private JTextField RepsField;
    private JLabel RepsLabel;
    private JTextField RestTimeField;
    private JLabel RestTimeLabel;
//    private JTextField AgeField;
    private JTextField HeartRateField;
    private JCheckBox yesCheckBox;
    private JCheckBox noCheckBox;
    private JButton calculateButton;
    private JTextArea OutputTextArea;
    private JLabel TitleLabel;
    private JLabel HeartRateLabel;
    private JLabel UseRepsLabel;
    private JButton saveButton;

    private LocalDateTime externalStartDT;
    private LocalDateTime externalEndDT;
    private double externalDurationMinutes;

    private boolean hasCalculated = false;

    public JumpingJacksCalculator(Account account) {
        setContentPane(MainPanel);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        pack();
//        setSize(300, 400);
//        setLocationRelativeTo(null);

        RepsField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void updateCheckbox() {
                String text = RepsField.getText().trim();
                try {
                    int val = Integer.parseInt(text);
                    yesCheckBox.setEnabled(true);
                } catch (NumberFormatException e) {
                    yesCheckBox.setSelected(false);
                    yesCheckBox.setEnabled(false);
                }
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateCheckbox();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateCheckbox();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateCheckbox();
            }
        });

        yesCheckBox.setEnabled(!RepsField.getText().trim().isEmpty());

        if (OutputTextArea != null) {
            OutputTextArea.setLayout(new BorderLayout());
            OutputTextArea.setLineWrap(true);
            OutputTextArea.setWrapStyleWord(true);
        }

        yesCheckBox.addActionListener(e -> {
            if (yesCheckBox.isSelected()) noCheckBox.setSelected(false);
        });
        noCheckBox.addActionListener(e -> {
            if (noCheckBox.isSelected()) yesCheckBox.setSelected(false);
        });

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
                int restTime = Integer.parseInt(RestTimeField.getText());
//                double age = Double.parseDouble(AgeField.getText());
                double age = account.getAge();
                double heartRate = HeartRateField.getText().isEmpty()
                        ? 0
                        : Double.parseDouble(HeartRateField.getText());
                String intensity = (String) IntensityComboB.getSelectedItem();
                boolean useReps = yesCheckBox.isSelected();

                int reps = 0;
                if (useReps && !RepsField.getText().isEmpty()) {
                    reps = Integer.parseInt(RepsField.getText());
                }

                double duration = externalDurationMinutes;
                if (duration <= 0) {
                    duration = Double.parseDouble(DurationDisplay.getText());
                }
                duration = Math.round(duration * 10) / 10.0;

                LocalDateTime startDT = (externalStartDT != null) ? externalStartDT : LocalDateTime.now();
                LocalDateTime endDT = (externalEndDT != null) ? externalEndDT : LocalDateTime.now();

                JumpingJacks jj = new JumpingJacks(
                        duration, weight, startDT, endDT, intensity,
                        sets, reps, restTime, useReps, age, heartRate
                );

                StringBuilder output = new StringBuilder();
                output.append("Workout: Jumping Jacks\n");
                output.append("Date: ").append(startDT.toLocalDate()).append("\n");
                output.append("Start Time: ").append(startDT.toLocalTime().withSecond(0).withNano(0)).append("\n");
                output.append("End Time: ").append(endDT.toLocalTime().withSecond(0).withNano(0)).append("\n");
                output.append("Calories burned: ").append(String.format("%.2f", jj.calculateCaloriesBurned())).append("\n");
                output.append("Sets: ").append(jj.getSets()).append("\n");
                output.append("Reps per set: ").append(jj.getReps()).append("\n");
                output.append("Rest time: ").append(jj.getRestTimeSeconds()).append(" sec\n");
                output.append("Use reps to calculate: ").append(jj.isUseReps() ? "Yes" : "No").append("\n");
                output.append("Intensity: ").append(intensity).append("\n");
                if (heartRate > 0) output.append("Heart Rate: ").append(heartRate).append(" bpm\n");
                output.append("Age: ").append(age).append("\n");

                OutputTextArea.setText(output.toString());
                OutputTextArea.revalidate();
                OutputTextArea.repaint();

                clearFields();

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
        RestTimeField.setText("");
        HeartRateField.setText("");
        yesCheckBox.setSelected(false);
        noCheckBox.setSelected(false);
        IntensityComboB.setSelectedIndex(0);
    }

    public JPanel getPanel() {
        return MainPanel;
    }

}

