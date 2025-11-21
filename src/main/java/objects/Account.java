package objects;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Account {


    private long id;
    private String email;
    private String password;
    private int privilege;
    private String username;
    private double weight;
    private double height;
    private double BMI;
    private String sex;
    private int age;
    private String serverOrigin;
    private int preference;
    private String creationDT;



    public Account(long id, String email, String password, int privilege , String creationDT )
    {
            this.id = id;
            this.email = email;
            this.password = password;
            this.privilege = privilege;
            this.creationDT =creationDT;

    }

    public Account(long id, String email, String password, int privilege,  String username,String creationDT,  double weight, double height, double BMI, int age, String sex, String serverOrigin, int preference )
    {
        this.id = id;
        this.email = email;
        this.password = password;
        this.privilege = privilege;
        this.username = username;
        this.weight = weight;
        this.height = height;
        this.BMI = BMI;
        this.age= age;
        this.sex = sex;
        this.serverOrigin = serverOrigin;
        this.preference = preference;
        this.creationDT = creationDT;

    }

    public void setupAccount( String username , String sex, int age, double weight, double height, double BMI, String serverOrigin,  int preference) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.BMI = BMI;
        this.preference = preference;
    }

    public void setBMI(double weight, double height)
    {
        this.BMI = weight/height * height;
    }

    public void setServerOrigin() {
        this.serverOrigin = "Client-JAM-PC-001";
    }


    public int getPrivilege()
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

    public String getServerOrigin() {
        return serverOrigin;
    }

    public double getBMI() {
        return BMI;
    }

    public double getHeight() {
        return height;
    }



    public double getWeight() {
        return weight;
    }


    public int getAge() {
        return age;
    }

    public String getSex() {
        return sex;
    }

    public String getCreationDT() {
        return creationDT;
    }



}
