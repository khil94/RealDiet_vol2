package org.androidtown.dietapp.Chart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import org.androidtown.dietapp.R;

/**
 * Created by zidru on 2017-09-18.
 */

public class ChartActivity extends Activity {
    Button button_to_all;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        button_to_all = (Button) findViewById(R.id.button_to_all_chart);

        CalendarView calendar = (CalendarView) findViewById(R.id.calendar);

        //리스너
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Intent intent = new Intent(ChartActivity.this, ViewCalendarActivity.class);
                intent.putExtra("Year", year);
                intent.putExtra("Month", month);
                intent.putExtra("Day", dayOfMonth);

                startActivity(intent);
            }
        });


        //리스너
      button_to_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()) {
                    case R.id.button_to_all_chart:
                        Intent intent = new Intent(ChartActivity.this, ViewHistoryDataActivity.class);
                        startActivity(intent); break;
                }
            }
        });
    }
}
