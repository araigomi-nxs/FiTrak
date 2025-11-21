package layouts.calculator.basic;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.util.Scanner;

public class RunBasicUI {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);


        FlatLightLaf.setup();
        switch (sc.nextInt()) {
            case 1:
                SwingUtilities.invokeLater(() -> {
                 //   WalkingWorkoutCalculator frame = new WalkingWorkoutCalculator();
                  //  frame.setVisible(true);
                });

                break;
            case 2:
                SwingUtilities.invokeLater(() -> {
                    //CyclingWorkoutCalculator frame = new CyclingWorkoutCalculator();
                    //frame.setVisible(true);
                });

                break;
            case 3:
                SwingUtilities.invokeLater(() -> {
                    //RunningWorkoutCalculator frame = new RunningWorkoutCalculator();
                    //frame.setVisible(true);
                });

                break;
            default:
                    break;
        }


    }
}

