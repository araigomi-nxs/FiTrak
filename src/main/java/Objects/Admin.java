package Objects;

public class Admin extends Account {


    public Admin( String email, String password) {

        super(generateAccountID(), email, password, 1);
    }


    public static long generateAccountID() {
        long timestamp = System.currentTimeMillis(); // 13-digit value
        return timestamp;
    }




}
