package layouts.calculator;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.event.*;
import java.time.LocalDateTime;

public class Stopwatch {
    private JPanel panel1;
    private JLabel startPauseButton;
    private JLabel timeLabel;
    private JLabel stopButton;

    private Timer timer;
    private long startTime = 0;       // actual time-run starting point
    private long elapsedTime = 0;     // accumulated time
    private boolean running = false;
    private boolean runOnce = false;
    private LocalDateTime startDT;
    private LocalDateTime endDT;
    private String durationMinutes = "0";

    public Stopwatch() {

        stopButton.setVisible(false);
        panel1.putClientProperty(FlatClientProperties.STYLE, "arc:20");

        timer = new Timer(100, e -> {
            long now = System.currentTimeMillis();
            elapsedTime = now - startTime;
            updateDisplay();
        });

        // START / PAUSE button
        startPauseButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                // First-ever start
                if (!runOnce) {
                    runOnce = true;
                    startDT = LocalDateTime.now();
                }

                if (!running) {
                    if (!runOnce || elapsedTime == 0) {
                        elapsedTime = 0;
                        startDT = LocalDateTime.now();
                    }

                    startTime = System.currentTimeMillis() - elapsedTime;
                    timer.start();
                    running = true;

                    startPauseButton.setIcon(new ImageIcon("src/main/resources/images/pauseButton.png"));
                    stopButton.setVisible(false);

                } else {
                    // Pause
                    timer.stop();
                    running = false;

                    startPauseButton.setIcon(new ImageIcon("src/main/resources/images/playButton.png"));
                    stopButton.setVisible(true);
                }
            }
        });

        // STOP BUTTON
        stopButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                timer.stop();
                running = false;

                endDT = LocalDateTime.now();

                durationMinutes = String.format("%.2f", elapsedTime / 60000.0);

                stopButton.setVisible(false);

                // Fully reset displayed time
                elapsedTime = 0;
                updateDisplay();
            }
        });
    }

    private void updateDisplay() {
        long totalSeconds = elapsedTime / 1000;
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        timeLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }

    public double getDuration() {
        return elapsedTime / 60000.0;
    }

    public JPanel getPanel() {
        return panel1;
    }

    // Required by your calculators
    public JLabel getStopButton() { return stopButton; }
    public boolean isRunning() { return running; }
    public LocalDateTime getStartDT() { return startDT; }
    public LocalDateTime getEndDT() { return endDT; }
    public String getDurationMinutes() { return durationMinutes; }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Stopwatch Test");
            Stopwatch sw = new Stopwatch();
            f.setContentPane(sw.getPanel());
            f.setSize(300,150);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setVisible(true);
        });
    }

    private void createUIComponents() {
        // IntelliJ GUI Designer placeholder
    }
}
