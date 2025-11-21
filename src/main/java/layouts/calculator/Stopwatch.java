package layouts.calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Stopwatch {
    private JPanel panel1;
    private JLabel startStopButton;
    private JLabel timeLabel;


    private Timer timer;
    private long startTime;
    private long elapsedTime;
    private boolean running = false;


   public Stopwatch(){
       //setContentPane(panel1);
       //setSize(300, 150);
       //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


       timer = new Timer(100, new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               long now = System.currentTimeMillis();
               elapsedTime = now - startTime;
               updateDisplay();
           }
       });


       startStopButton.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {
               if (!running) {
                   startTime = System.currentTimeMillis() - elapsedTime;
                   timer.start();
                   running = true;

                   startStopButton.setIcon(new javax.swing.ImageIcon("src/main/resources/images/stopButton.png"));
               } else {
                   // Stop
                   timer.stop();
                   running = false;
                   startStopButton.setIcon(new javax.swing.ImageIcon("src/main/resources/images/playButton.png"));
               }
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

    public double getDuration(){
        return elapsedTime / 60000.0;
    }

    public JPanel getPanel(){
       return panel1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Stopwatch sw = new Stopwatch();
           // sw.setVisible(true);
        });
    }

    /// return jBUTTON method





    private void createUIComponents() {
        // TODO: place custom component creation code here
    }



}




