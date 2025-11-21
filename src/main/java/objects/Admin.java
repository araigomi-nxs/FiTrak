package objects;

import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.time.LocalDateTime;


public class Admin extends Account {


    public Admin( String email, String password, String creationDT) { //account creation


        super(generateAccountID(), email, password, 1,  creationDT );


    }
    public Admin( long userID,String email, String password, String creationDT) {
        super(userID, email, password, 1,  creationDT);
    }


    public static long generateAccountID() {

        long timestamp = System.currentTimeMillis(); // 13-digit value
        return timestamp;
    }




}
