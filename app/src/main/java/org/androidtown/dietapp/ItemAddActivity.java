package org.androidtown.dietapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class ItemAddActivity extends AppCompatActivity {
    private EditText editTextName;
    private EditText editTextCategory;
    private EditText editTextCalorie;
    private EditText editTextCarb;
    private EditText editTextProtein;
    private EditText editTextFat;
    private Button buttonSubmit;
    private String uuid;

    DatabaseReference mFoodRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_add);

        //findid start
        editTextName=(EditText)findViewById(R.id.editTextName);
        editTextCategory=(EditText)findViewById(R.id.editTextCategory);
        editTextCalorie=(EditText)findViewById(R.id.editTextCalorie);
        editTextCarb=(EditText)findViewById(R.id.editTextCarb);
        editTextProtein=(EditText)findViewById(R.id.editTextProtein);
        editTextFat=(EditText)findViewById(R.id.editTextFat);
        buttonSubmit=(Button) findViewById(R.id.buttonSubmit);
        //find id end

        //database & uuid init start
        uuid=UUID.randomUUID().toString();
        mFoodRef= FirebaseDatabase.getInstance().getReference().child("food").child(uuid);
        //init end
        // public FoodItem(String uid,String category, String name,int calorie, int carbohydrate, int protein, int fat) {
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=editTextName.getText().toString();
                String category=editTextCategory.getText().toString();
                int calorie=Integer.parseInt(editTextCalorie.getText().toString());
                int carb=Integer.parseInt(editTextCarb.getText().toString());
                int protein=Integer.parseInt(editTextProtein.getText().toString());
                int fat=Integer.parseInt(editTextFat.getText().toString());
                mFoodRef.setValue(new FoodItem(uuid,category,name,calorie,carb,protein,fat));
                finish();
            }
        });


    }
}
