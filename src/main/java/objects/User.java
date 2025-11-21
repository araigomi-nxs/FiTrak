package objects;

import java.time.LocalDateTime;

public class User extends Account{


    public User( String email, String password, String creationDT) {

        super( generateAccountID(), email, password, 0 ,creationDT);
    }
    public User( long userID,String email, String password, String creationDT) {
        super(userID, email, password, 0,   creationDT);
    }

    public static long generateAccountID() {
        long timestamp = System.currentTimeMillis(); // 13-digit value
        return timestamp;
    }

}
