package Objects;

public class User extends Account{


    public User( String email, String password) {

        super( generateAccountID(), email, password, 0 );
    }

    public static long generateAccountID() {
        long timestamp = System.currentTimeMillis(); // 13-digit value
        return timestamp;
    }

}
