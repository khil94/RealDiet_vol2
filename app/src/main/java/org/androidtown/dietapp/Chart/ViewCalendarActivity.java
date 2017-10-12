package org.androidtown.dietapp.Chart;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    float calorie=0;
    float rat_carbo, rat_protein, rat_fat;
    int carbo,protein,fat;
    ArrayList<ChartData> data;
    String date;
    int contains;
    ArrayList<FoodItem> userHistoryData ;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_calender);
        final TextView textView = (TextView)findViewById(R.id.message_to_calender_viewer);
        pieChart = (PieChart) findViewById(R.id.pie_chart);

        contains = 0;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference userData = mDatabase.child("user").child(user.getUid());
        DatabaseReference userHistoryRef = mDatabase.child("userHistory").child(user.getUid());

        Intent intent = getIntent();
        int year = intent.getExtras().getInt("Year");
        int month = intent.getExtras().getInt("Month")+1;
        int day = intent.getExtras().getInt("Day");

        if(month<10 && day<10){
            date = String.valueOf(year)+"0"+String.valueOf(month)+"0"+String.valueOf(day);
        }else if(month<10 && day>=10){
            date = String.valueOf(year)+"0"+String.valueOf(month)+String.valueOf(day);
        }else if(day<10){
            date = String.valueOf(year)+String.valueOf(month)+"0"+String.valueOf(day);
        }else date = String.valueOf(year)+String.valueOf(month)+String.valueOf(day);

        DatabaseReference UserHistory = userHistoryRef.child(date);


        userHistoryData = new ArrayList<>();

        UserHistory.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setCarbo(0);
                setProtein(0);
                setFat(0);
                int i=0;
                userHistoryData.clear();
                Log.d("","clear");
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FoodItem foodItem = snapshot.getValue(FoodItem.class);
                    userHistoryData.add(foodItem);
                    setCarbo(getCarbo() + userHistoryData.get(i).getCarbohydrate());
                    setProtein(getProtein() + userHistoryData.get(i).getProtein());
                    setFat(getFat() + userHistoryData.get(i).getFat());
                    Log.d("","contains");
                    setContains(getContains() + 1);
                }
                if(getContains()==0)
                {
                    Toast.makeText(getApplicationContext(), "선택하신 날짜에는 먹은 음식이 없습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }else{

                    float sum = getCarbo() + getProtein() + getFat();
                    rat_carbo = ((float)carbo/sum)*100;
                    rat_protein = ((float)protein/sum)*100;
                    rat_fat = ((float)fat/sum)*100;

                    data = new ArrayList<>();
                    data.add(new ChartData("탄수화물 "+String.valueOf(rat_carbo+"%"), rat_carbo, Color.WHITE, Color.parseColor("#0091EA")));
                    data.add(new ChartData("단백질  "+String.valueOf(rat_protein+"%"), rat_protein, Color.WHITE, Color.parseColor("#33691E")));
                    data.add(new ChartData("지방  "+String.valueOf(rat_fat+"%"), rat_fat, Color.DKGRAY, Color.parseColor("#F57F17")));

                    pieChart.setChartData(data);
                    pieChart.partitionWithPercent(true);

                    if(rat_carbo>=45){
                        textView.setText("too many 탄수화물");
                    }else if(rat_protein>=60){
                        textView.setText("too many 단백질");
                    }else if(rat_fat>=35){
                        textView.setText("too many 지방");
                    }else if(rat_protein<40){
                        textView.setText("지방과 탄수화물을 너무 많이 먹고 있습니다! 단백질이 먹어달라고 울고있어요.");
                    }else if(rat_fat<15){
                        textView.setText("아무리 지방이 안좋아보여도 그렇게 드시면 안됩니다.");
                    }else if(rat_carbo<25) {
                        textView.setText("밀가루음식이나 흰쌀밥등이 아니라면 탄수화물은 식단의 30%는 먹는것이 좋습니다.");
                    }else textView.setText("적당히 균형잡힌 식단이군요.");

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }

    public int getContains() {return contains;}

    public void setContains(int contains) {this.contains = contains;}

    public int getCarbo() {
        return carbo;
    }

    public void setCarbo(int carbo) {
        this.carbo = carbo;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }
}