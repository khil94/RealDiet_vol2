package org.androidtown.dietapp.Chart;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidtown.dietapp.R;


/**
 * Created by zidru on 2017-10-07.
 */

public class ViewHistoryDataActivity extends AppCompatActivity {

     ViewAllCalendarActivity view_line;;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history_of_user);

        view_line = (ViewAllCalendarActivity)getSupportFragmentManager().findFragmentById(R.id.line_graph_fragment);
    }
}
