package checker;

public class InputValidator {

    public static boolean isValidString(String input) {
        if (input == null) return false;
        return input.matches("[A-Za-z]+");
    }

    // Validates if a string is a valid integer
    public static boolean isValidInteger(String input) {
        if (input == null) return false;
        try {
            Integer.parseInt(input.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Validates if a string is a valid double
    public static boolean isValidDouble(String input) {
        if (input == null) return false;
        try {
            Double.parseDouble(input.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}