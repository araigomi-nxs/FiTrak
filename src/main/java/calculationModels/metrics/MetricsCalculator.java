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
                return   Math.round(( weightKg / (heightMeters * heightMeters)) * 100.0) / 100.0;
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
}
