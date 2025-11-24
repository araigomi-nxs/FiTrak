package layouts.calculator.cardio;

import calculationModels.cardio.JumpingRope;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import objects.Account;

public class JumpingRopeCalculator extends JFrame {

    private JPanel MainPanel;
    private JPanel JPanel2;
    private JLabel TitleLabel;
    private JLabel DurationDisplay;
//    private JTextField WeightField;
    private JComboBox<String> IntensityComboB;
    private JTextField SetsField;
    private JTextField RepsField;
    private JTextField RestTimeField;
    private JCheckBox yesCheckBox;
    private JCheckBox noCheckBox;
//    private JTextField AgeField;
    private JTextField HeartRateField;
    private JButton calculateButton;
    private JTextArea OutputTextArea;
    private JLabel DurationLabel;
    private JLabel IntensityLabel;
    private JLabel SetsLabel;
    private JLabel RepsLabel;
    private JLabel RestTimeLabel;
    private JLabel HeartRateLabel;
    private JLabel UseRepsLabel;

    private boolean hasCalculated = false;

    public JumpingRopeCalculator(Account account) {
        setContentPane(MainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setSize(300, 400);
        setLocationRelativeTo(null);

        RepsField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void updateCheckbox() {
                String text = RepsField.getText().trim();
                try {
                    Integer.parseInt(text);
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
                double duration = Double.parseDouble(DurationDisplay.getText());
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

                LocalDateTime dateTime = LocalDateTime.now();

                JumpingRope rope = new JumpingRope(
                        duration, weight, dateTime,dateTime, intensity,
                        sets, reps, restTime, useReps, age, heartRate
                );

                StringBuilder output = new StringBuilder();
                output.append("Workout: Jump Rope\n");
                output.append("Date: ").append(dateTime.toLocalDate()).append("\n");
                output.append("Time: ").append(dateTime.toLocalTime().withSecond(0).withNano(0)).append("\n");
                output.append("Calories burned: ").append(String.format("%.2f", rope.calculateCaloriesBurned())).append("\n");
                output.append("Sets: ").append(rope.getSets()).append("\n");
                output.append("Reps per set: ").append(rope.getReps()).append("\n");
                output.append("Rest time: ").append(rope.getRestTimeSeconds()).append(" sec\n");
                output.append("Use reps to calculate: ").append(rope.isUseReps() ? "Yes" : "No").append("\n");
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
    }

    public void clearFields()
    {
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

