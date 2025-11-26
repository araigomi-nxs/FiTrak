package calculationModels.metrics;

public class MetricsCalculator {

    public static double calculateCalories(double metValue, double weight, double durationMinutes) {
        if (metValue <= 0 || weight <= 0 || durationMinutes <= 0) return 0;
        double hours = durationMinutes / 60.0;
        return metValue * weight * hours;
    }


    public static double convertToMeters(double heightCentimeters)
    {
          return  heightCentimeters / 100;
    }

    public static double calculateBMI(double weightKg, double heightMeters) {
                return Math.round(( weightKg / (heightMeters * heightMeters)) * 100.0) / 100.0;
    }

    public static double calculateCalPerStep(double caloriedBurned, int steps){
        return Math.round((caloriedBurned / steps) * 1000.0)/ 1000.0;
    }

    public static double calculateMaxHeartRate(double age) {
        if (age <= 0) return 0;
        return 220 - age;
    }

    public static String getHeartRateZone(double age, double currentHeartRate) {
        double maxHR = calculateMaxHeartRate(age);
        if (maxHR <= 0 || currentHeartRate <= 0) return "Unknown";

        double percent = (currentHeartRate / maxHR) * 100;

        if (percent < 60) return "Light (Warm-up Zone)";
        else if (percent < 75) return "Moderate (Fat-Burn Zone)";
        else if (percent < 90) return "Vigorous (Cardio Zone)";
        else return "Maximum Effort (Anaerobic Zone)";
    }

// Optional lng
    public static String suggestDifficulty(double bmi) {
        if (bmi == 0) return "Unknown";
        if (bmi < 18.5) return "Light – focus on endurance";
        else if (bmi < 25) return "Moderate – balanced workout";
        else if (bmi < 30) return "Challenging – increase cardio";
        else return "Heavy – focus on weight management and low impact";
    }


    //Basal Metabolic Rate
    public static double computeBMR ( double weight, double height , int age, String sex ) {
        double BMR;

        BMR  =(10 * weight) + (6.25 * (height * 100)) - ( 5 * age );
        BMR = sex.equals("male")? BMR+5:BMR-161;
        return BMR;
    }

    // Total Daily Energy Expenditure (TDEE)
    public static double computeTDEE(double BMR, double dailyExerciseCalBurn, int preference) {
        double activityFactor;
        switch (preference) {
              case 2: // Moderately Active
                activityFactor = 1.55;
                break;
            case 3: // Very Active
                activityFactor = 1.725;
                break;
            default: // Sedentary
                activityFactor = 1.2;
        }

        double TDEE = BMR * activityFactor;

        if (dailyExerciseCalBurn > 0) {
            TDEE += dailyExerciseCalBurn;
        }
        return TDEE;
    }



    //Daily Cal Deficit assume calIntake for now edit later
    public static double computeDailyCalDeficit(double TDEE, double calIntake) {
        return  TDEE - calIntake;
    }

    //Daily FatLoss in grams
    public static double computeFatLoss(double dailyCalDeficit)
    {
        // 1 kilogram of fat ≈ 7,700 kcal average
        double fatLoss = dailyCalDeficit / 7700.0;

        // Round to nearest hundredth (2 decimal places)
        return Math.round(fatLoss * 100.0) / 100.0;
    }


    //wag muna display ang weightloss per day - calculator/update ng user. separate rin ang weigh in per week

}
