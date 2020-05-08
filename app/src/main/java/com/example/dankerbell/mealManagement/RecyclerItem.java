package com.example.dankerbell.mealManagement;


public class RecyclerItem {
    String food;
    String kcal;

    public void setFood(String food){
        this.food=food;
    }
    public void setKcal(String kcal){
        this.kcal=kcal;
    }
    public String getFood(){
        return this.food;
    }
    public String getKcal(){
        return this.kcal;
    }

}

