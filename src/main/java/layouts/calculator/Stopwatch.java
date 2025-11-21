package layouts.calculator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Stopwatch {
    private JPanel panel1;
    private JLabel startPauseButton;
    private JLabel timeLabel;
    private JLabel stopButton;


    private Timer timer;
    private long startTime;
    private long elapsedTime;
    private boolean running = false;
    private boolean runOnce = false;
    private LocalDateTime startDT;
    private LocalDateTime endDT;
    private String durationMinutes;


   public Stopwatch(){
       //setContentPane(panel1);
       //setSize(300, 150);
       //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       stopButton.setVisible(false);

       timer = new Timer(100, new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               long now = System.currentTimeMillis();
               elapsedTime = now - startTime;
               updateDisplay();
           }
       });
       startPauseButton.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {
               if(!runOnce){
                   runOnce = true;
                   startDT =LocalDateTime.now();
                   //System.out.println(startDT.toString());
               }
               if (!running) {
                   startTime = System.currentTimeMillis() - elapsedTime;
                   timer.start();

                   running = true;
                   startPauseButton.setIcon(new javax.swing.ImageIcon("src/main/resources/images/pauseButton.png"));
                   stopButton.setVisible(false);
               } else {
                   // Stop
                   timer.stop();
                   running = false;
                   startPauseButton.setIcon(new javax.swing.ImageIcon("src/main/resources/images/playButton.png"));
                   stopButton.setVisible(true);


               }
           }
       });
       stopButton.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {
               timer.stop();
               endDT =  LocalDateTime.now();
               stopButton.setVisible(false);
               durationMinutes =String.format("%.2f", (elapsedTime / 60000.0));

               //System.out.println(endDT.toString());
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

   public JLabel getStartStopButton(){
            return stopButton;
   }

   public boolean isRunning(){
       return running;
   }

   public LocalDateTime getStartDT(){
       return startDT;
   }
   public LocalDateTime getEndDT(){
       return endDT;
   }
   public String getDurationMinutes(){
       return durationMinutes;
   }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }



}




