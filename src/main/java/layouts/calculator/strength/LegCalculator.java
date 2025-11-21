package layouts.calculator.strength;

import calculationModels.strength.LegWorkout;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class LegCalculator extends JFrame {

    private JPanel MainPanel;
    private JPanel JPanel2;
    private JLabel TitleLabel;
    private JLabel UsingEquipmentsLabel;
    private JCheckBox yesCheckBox;
    private JCheckBox noCheckBox;
    private JButton calculateButton;
    private JTextArea OutputTextArea;
    private JLabel DurationLabel;
    private JLabel WeightLabel;
    private JLabel SetsLabel;
    private JLabel RepsLabel;
    private JLabel WeightLiftedLabel;
    private JLabel IntensityLabel;
    private JLabel RestTimeLabel;
    private JTextField DurationField;
    private JTextField WeightField;
    private JTextField SetsField;
    private JTextField RepsField;
    private JTextField WeightLiftedField;
    private JComboBox IntensityComboB;
    private JTextField RestTimeField;

    private boolean hasCalculated = false;

    public LegCalculator() {
        setContentPane(JPanel2);
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
                double duration = Double.parseDouble(DurationField.getText());
                double bodyWeight = Double.parseDouble(WeightField.getText());
                int sets = Integer.parseInt(SetsField.getText());
                int reps = Integer.parseInt(RepsField.getText());
                int restTime = Integer.parseInt(RestTimeField.getText());
                double weightLifted = WeightLiftedField.getText().isEmpty() ? 0 : Double.parseDouble(WeightLiftedField.getText());
                String intensity = (String) IntensityComboB.getSelectedItem();
                boolean useEquipment = yesCheckBox.isSelected();

                LocalDateTime dateTime = LocalDateTime.now();

                LegWorkout leg = new LegWorkout(duration, bodyWeight, dateTime,dateTime,
                        sets, reps, weightLifted, intensity, restTime, useEquipment);

                StringBuilder output = new StringBuilder();
                output.append("Workout: Legs\n");
                output.append("Date: ").append(dateTime.toLocalDate()).append("\n");
                output.append("Time: ").append(dateTime.toLocalTime().withSecond(0).withNano(0)).append("\n");
                output.append("Calories burned: ").append(String.format("%.2f", leg.calculateCaloriesBurned())).append("\n");
                output.append("Sets: ").append(leg.getSets()).append("\n");
                output.append("Reps per set: ").append(leg.getReps()).append("\n");
                output.append("Weight Lifted: ").append(leg.getWeightLiftedKG()).append(" kg\n");
                output.append("Rest Time: ").append(leg.getRestTimeSeconds()).append(" sec\n");
                output.append("Intensity: ").append(leg.getIntensity()).append("\n");
                output.append("Use Equipment: ").append(useEquipment ? "Yes" : "No").append("\n");

                OutputTextArea.setText(output.toString());
                OutputTextArea.revalidate();
                OutputTextArea.repaint();

                clearField();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(MainPanel,
                        "Error: Please fill all fields correctly.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
        public void clearField() {
            DurationField.setText("");
            WeightField.setText("");
            SetsField.setText("");
            RepsField.setText("");
            WeightLiftedField.setText("");
            RestTimeField.setText("");
            IntensityComboB.setSelectedIndex(0);
            yesCheckBox.setSelected(false);
            noCheckBox.setSelected(true);
            WeightLiftedField.setEnabled(false);
        }
    }

