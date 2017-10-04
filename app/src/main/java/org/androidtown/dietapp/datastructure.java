package org.androidtown.dietapp;

import java.util.ArrayList;

/**
 * Created by azxca on 2017-10-02.
 */

public class datastructure<T> {
    private static datastructure dataStructure;
    private ArrayList<T> foodList;

    private datastructure() {
    }

    public static datastructure getInstance(){
        if(dataStructure==null)dataStructure=new datastructure();
        return dataStructure;
    }

    public void setFoodList(ArrayList<T> foodList) {
        this.foodList = foodList;
    }

    public ArrayList<T> getFoodList() {
        return foodList;
    }

    public void sort(){

    }
}
