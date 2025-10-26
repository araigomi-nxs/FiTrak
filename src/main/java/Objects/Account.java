package Objects;

public class Account {


    private long id;
    private String name;
    private String username;

    private String password;
    private String email;
    private int privilege;

    private double weight;
    private double height;

    //private double BMI


    Account(long id, String email, String password, int privilege )
    {
            this.id = id;
            this.email = email;
            this.password = password;
            this.privilege = privilege;
    }


    public int getPrivelege()
    {
             return privilege;
    }



    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }



    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }



    public static void  calculateBMI()
    {
        //MetricCalculator calculateBMI = new MetricsCalculator
        //calculateBMI(this.height , this.weight);

    }

   // public Boolean isVerifiedEmail() {return VerifiedEmail;}


}
