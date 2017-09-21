package org.androidtown.dietapp;

/**
 * Created by azxca on 2017-09-22.
 */

/**
 * 이 부분은 FoodItem과 같은 역할을 하는 클래스
 * 나중에 삭제해야 함
 **/

public class UserModel {

    String firstName, lastName,job, key;
    int age;

    public UserModel(){

    }

    public UserModel(String firstName, String lastName, String job, int age, String key) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.job = job;
        this.age = age;
        this.key = key;
    }
}
