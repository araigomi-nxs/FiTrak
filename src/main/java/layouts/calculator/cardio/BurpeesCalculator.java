package layouts.calculator.cardio;

import calculationModels.cardio.Burpees;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import objects.Account;

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
    private JTextArea OutputTextArea;
    private JLabel DurationLabel;
    private JLabel IntensityLabel;
    private JLabel SetsLabel;
    private JLabel RepsLabel;
    private JLabel RestTimeLabel;
    private JLabel HeartRateLabel;

    private boolean hasCalculated = false;

    public BurpeesCalculator(Account account) {
        setContentPane(MainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 400);
        setLocationRelativeTo(null);

        if (OutputTextArea != null) {
            OutputTextArea.setLayout(new BorderLayout());
            OutputTextArea.setLineWrap(true);
            OutputTextArea.setWrapStyleWord(true);
        }

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
                int reps = Integer.parseInt(RepsField.getText());
                int restTime = Integer.parseInt(RestTimeField.getText());
                double age = account.getAge();
                double heartRate = HeartRateField.getText().isEmpty() ? 0 : Double.parseDouble(HeartRateField.getText());
                String intensity = (String) IntensityComboB.getSelectedItem();

                LocalDateTime dateTime = LocalDateTime.now();

                Burpees burpees = new Burpees(duration, weight, dateTime,dateTime, intensity,sets, reps, restTime, age, heartRate);

                StringBuilder output = new StringBuilder();
                output.append("Workout: Burpees\n");
                output.append("Date: ").append(dateTime.toLocalDate()).append("\n");
                output.append("Time: ").append(dateTime.toLocalTime().withSecond(0).withNano(0)).append("\n");
                output.append("Calories burned: ").append(String.format("%.2f", burpees.calculateCaloriesBurned())).append("\n");
                output.append("Sets: ").append(burpees.getSets()).append("\n");
                output.append("Reps per set: ").append(burpees.getReps()).append("\n");
                output.append("Rest time: ").append(burpees.getRestTimeSeconds()).append(" sec\n");
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
        public void clearFields() {
            DurationDisplay.setText("");
            SetsField.setText("");
            RepsField.setText("");
            RestTimeField.setText("");
            HeartRateField.setText("");
            IntensityComboB.setSelectedIndex(0);
        }
        public JPanel getPanel() {
            return MainPanel;
        }
    }

