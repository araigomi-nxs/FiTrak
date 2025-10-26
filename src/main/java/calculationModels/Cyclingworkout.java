package calculationModels;

public class Cyclingworkout extends Workout{
    
    private double avSpeed;
    private double distanceKMC;
    
    public Cyclingworkout (double duration, double weight, String date,double avSpeed, double distanceKMC){
        
        super (duration, weight, date, avSpeed < 16 ? 4.0 : (avSpeed <= 20 ? 6.8 : 8.0));
        this.avSpeed = avSpeed;
        this.distanceKMC= distanceKMC;
    }


    @Override
    public double calculateCaloriesBurned() {
        return 0;
    }
}
