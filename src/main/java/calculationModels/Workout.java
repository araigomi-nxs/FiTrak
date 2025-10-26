package calculationModels;

public abstract class Workout{
    
    protected double durationMinutes;
    protected double caloriesBurned;
    protected String date;
    protected double metValue;
    protected double weight;
    
    public Workout(double durationMinutes, double weight, String date,double metValue){
        this.durationMinutes = durationMinutes;
        this.weight = weight;
        this.metValue = metValue;
        this.date = date; 
        this.caloriesBurned = calculateCaloriesBurned();
    }
     
    public abstract double calculateCaloriesBurned();
    
    public double getDurationMinutes() {
        return durationMinutes;
    }
    
    public double getCaloriesBurned(){
        return caloriesBurned;
    }
    
    public String getDate(){
        return date;
    }
    
    public double getMetValue(){
        return metValue;
    }
    
    public double getWeight(){
        return weight;
    }
    
    public void setDurationMinutes(double durationMinutes){
        this.durationMinutes = durationMinutes;
        this.caloriesBurned = calculateCaloriesBurned();
    }
    
    public void setDate(String date){
        this.date = date;
    }
}

