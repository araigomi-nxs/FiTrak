package calculationModels;

import java.util.Scanner;

public class Try {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        boolean continueProgram = true;

        System.out.println("=== FITRAK WORKOUT TRACKER ===");

        while (continueProgram) {

            System.out.print("\nEnter workout type (walking, running, cycling, strength): ");
            String type = sc.nextLine().trim().toLowerCase();

            System.out.print("Enter your weight (kg): ");
            double weight = sc.nextDouble();

            System.out.print("Enter duration (minutes): ");
            double duration = sc.nextDouble();
            sc.nextLine(); 

            System.out.print("Enter date (YYYY-MM-DD): ");
            String date = sc.nextLine();

            Workout workout = null;

            switch (type) {

                
                case "walking":
                    System.out.print("Enter distance (km): ");
                    double walkDist = sc.nextDouble();
                    System.out.print("Enter total steps: ");
                    int walkSteps = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter intensity (calm/brisk): ");
                    String walkIntensity = sc.nextLine();
                    workout = new Walkingworkout(duration, weight, date, walkDist, walkSteps, walkIntensity);
                    break;
                    
                case "running":
                    System.out.print("Enter distance (km): ");
                    double runDistance = sc.nextDouble();
                    sc.nextLine();
                    System.out.print("Enter intensity (moderate/vigorous): ");
                    String runIntensity = sc.nextLine();
                    System.out.print("Enter terrain (flat/uphill/trail): ");
                    String runTerrain = sc.nextLine();
                    System.out.print("Enter jogging speed (slow/moderate/fast): ");
                    String runSpeed = sc.nextLine();
                    workout = new Runningworkout(duration, weight, date, runDistance, runIntensity,runTerrain,runSpeed);
                    break;

                case "cycling":
                    System.out.print("Enter average speed (km/h): ");
                    double speed = sc.nextDouble();
                    System.out.print("Enter distance (km): ");
                    double dist = sc.nextDouble();
                    sc.nextLine();
                    workout = new Cyclingworkout(duration, weight, date, speed, dist);
                    break;

                case "strength":
                    System.out.print("Enter sets: ");
                    int sets = sc.nextInt();
                    System.out.print("Enter reps: ");
                    int reps = sc.nextInt();
                    System.out.print("Enter weight lifted (kg): ");
                    double lifted = sc.nextDouble();
                    sc.nextLine();
                    System.out.print("Enter rest time between sets (seconds): ");
                    int rest = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter intensity (light/vigorous): ");
                    String intensity = sc.nextLine();
                    System.out.print("Enter muscle group (push/pull/legs): ");
                    String muscleGroup = sc.nextLine();
                    workout = new Strengthworkout(duration, weight, date, sets, reps, lifted, intensity,rest, muscleGroup, 0);
                    break;

                default:
                    System.out.println(" Invalid workout type entered. Please try again.");
                    break;
            }

            if (workout != null) {
                System.out.println("\n--- WORKOUT SUMMARY ---");
                System.out.println("Workout Type: " + type.toUpperCase());
                System.out.println("Date: " + workout.getDate());
                System.out.println("Duration: " + workout.getDurationMinutes() + " minutes");
                System.out.printf("Calories Burned: %.2f%n", workout.getCaloriesBurned());
                System.out.println("------------------------");
            }

            System.out.print("\nDo you want to log another workout? (yes/no): ");
            String answer = sc.nextLine().trim().toLowerCase();
            if (!answer.equals("yes")) {
                continueProgram = false;
            }
        }

        System.out.println("\nThank you for using FITRAK! Stay fit and healthy! ");
        sc.close();
    }
}
