package org.androidtown.dietapp.Chart;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.androidtown.chart.ChartData;
import org.androidtown.chart.PieChart;
import org.androidtown.dietapp.FoodItem;
import org.androidtown.dietapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zidru on 2017-09-18.
 */

public class ViewCalendarActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    PieChart pieChart;
    float carbo, protein, fat, sum=0;
    float calorie=0;
    float rat_carbo, rat_protein, rat_fat=0;
    List<ChartData> data;
    String date;
    List<FoodItem> userHistoryData ;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_calender);
        pieChart = (PieChart) findViewById(R.id.pie_chart);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference userData = mDatabase.child("user").child(user.getUid());
        DatabaseReference userHistoryRef = mDatabase.child("userHistory").child(user.getUid());


        Intent intent = getIntent();
        int year = intent.getExtras().getInt("Year");
        int month = intent.getExtras().getInt("Month");
        int day = intent.getExtras().getInt("Day");
        date = String.valueOf(year)+String.valueOf(month)+String.valueOf(day);

        DatabaseReference UserHistory = userHistoryRef.child(date);

        if(UserHistory==null){
            Log.d("","its null");
        }
        else{
        UserHistory.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userHistoryData.clear();
                for(DataSnapshot snap : dataSnapshot.getChildren()){
                    userHistoryData.add(snap.getValue(FoodItem.class));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
            for(int i=0; i<userHistoryData.size();i++){
                carbo = carbo + userHistoryData.get(i).getCarbohydrate();
                protein += userHistoryData.get(i).getProtein();
                fat += userHistoryData.get(i).getFat();
                calorie += userHistoryData.get(i).getCalorie();
            }
        }



        //db받은후 탄단지 입력.
        sum = carbo + protein + fat;
        rat_carbo = (carbo/sum)*100;
        rat_protein = (protein/sum)*100;
        rat_fat = (fat/sum)*100;

        data = new ArrayList<>();
        data.add(new ChartData("탄수화물 "+String.valueOf(rat_carbo+"%"), rat_carbo, Color.WHITE, Color.parseColor("#0091EA")));
        data.add(new ChartData("단백질  "+String.valueOf(rat_protein+"%"), rat_protein, Color.WHITE, Color.parseColor("#33691E")));
        data.add(new ChartData("지방  "+String.valueOf(rat_fat+"%"), rat_fat, Color.DKGRAY, Color.parseColor("#F57F17")));

        pieChart.setChartData(data);
        pieChart.partitionWithPercent(true);


    }
}