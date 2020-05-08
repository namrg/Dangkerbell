package com.example.dankerbell.mealManagement;

public class RecyclermyfoodItem {
    String myfood;
    String mykcal;
    boolean isDeleted;
    public RecyclermyfoodItem(String myfood, String mykcal,boolean isDeleted){
        this.myfood=myfood;
        this.mykcal=mykcal;
        this.isDeleted=isDeleted;
    }

    public String getMyfood() {
        return myfood;
    }

    public void setMyfood(String myfood) {
        this.myfood = myfood;
    }

    public String getMykcal() {
        return mykcal;
    }

    public void setMykcal(String mykcal) {
        this.mykcal = mykcal;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setisDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
