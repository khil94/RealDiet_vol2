package org.androidtown.dietapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by azxca on 2017-10-05.
 */

public class DataStructure {
    private static DataStructure dataStructure;
    private ArrayList<FoodItem> foodList;
    private ArrayList<FoodItem> searchList;

    private DataStructure() {
        searchList=new ArrayList<FoodItem>();
    }

    public static DataStructure getInstance(){
        if(dataStructure==null)dataStructure=new DataStructure();
        return dataStructure;
    }

    public void setFoodList(ArrayList<FoodItem> foodList) {
        this.foodList = foodList;
    }

    public ArrayList<FoodItem> getFoodList() {
        return foodList;
    }

    public void sort(){
        Collections.sort(foodList, new Comparator<FoodItem>() {
            @Override
            public int compare(FoodItem o1, FoodItem o2) {
                // TODO Auto-generated method stub
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    public ArrayList<FoodItem> search(String searchedString){
        searchList.clear();
        for(FoodItem item : foodList){
            if(item.getName().contains(searchedString))searchList.add(item);
        }
        return searchList;
    }
}

