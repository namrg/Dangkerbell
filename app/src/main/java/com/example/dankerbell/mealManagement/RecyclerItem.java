package com.example.dankerbell.mealManagement;

public class RecyclerItem {
    String food;
    String kcal;
    boolean isSelected;
    public RecyclerItem(String food,String kcal,boolean isSelected){
        this.food=food;
        this.kcal=kcal;
        this.isSelected=isSelected;
    }
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
    public boolean isSelected(){
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
