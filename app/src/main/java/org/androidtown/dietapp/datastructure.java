package org.androidtown.dietapp;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by azxca on 2017-10-02.
 */

public class datastructure {
    private static datastructure dataStructure;
    private ArrayList<FoodItem> foodList;
    private ArrayList<FoodItem> searchList;

    private datastructure() {
    }

    public static datastructure getInstance(){
        if(dataStructure==null)dataStructure=new datastructure();
        return dataStructure;
    }

    public void setFoodList(ArrayList<FoodItem> foodList) {
        this.foodList = foodList;
    }

    public ArrayList<FoodItem> getFoodList() {
        return foodList;
    }

    public void sort(){
        Collections.sort(foodList);
    }

    public ArrayList<FoodItem> search(String searchedString){
        searchList.clear();
        for(FoodItem item : foodList){
            if(item.getName().startsWith(searchedString))searchList.add(item);
        }
        return searchList;
    }
}
