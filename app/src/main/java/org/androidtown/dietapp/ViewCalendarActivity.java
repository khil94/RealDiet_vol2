package org.androidtown.dietapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.androidtown.chart.ChartData;
import org.androidtown.chart.PieChart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zidru on 2017-09-18.
 */

public class ViewCalendarActivity extends Activity {
    PieChart pieChart;
    float carbo, protein, fat, sum;
    float rat_carbo, rat_protein, rat_fat;
    List<ChartData> data;
    View.OnClickListener listener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_calender);
        pieChart = (PieChart) findViewById(R.id.pie_chart);

        Intent intent = getIntent();
        int yaer = intent.getExtras().getInt("Year");
        int month = intent.getExtras().getInt("Month");
        int day = intent.getExtras().getInt("Day");

        //db받은후 탄단지 입력.
        carbo = 700;
        protein = 400;
        fat = 800;
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


        Button to_chart_button = (Button) findViewById(R.id.button_to_chart);
        listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.button_to_chart:
                        finish();
                        break;
                }
            }
        };
        to_chart_button.setOnClickListener(listener);


    }
}