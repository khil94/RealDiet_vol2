package org.androidtown.dietapp.Chart;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.androidtown.dietapp.R;


/**
 * Created by zidru on 2017-10-07.
 */


public class ViewHistoryDataActivity extends AppCompatActivity {
    Toolbar toolbar;
     ViewAllCalendarActivity view_line;
     ViewAllCalendarActivity_byPie view_pie;
     CommentInChartFragment comment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history_of_user);

        toolbar = (Toolbar) findViewById(R.id.toolbar_on_historyview);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);


        view_line = new ViewAllCalendarActivity();
        view_pie = new ViewAllCalendarActivity_byPie();
        comment = new CommentInChartFragment();


        TabLayout tabs = (TabLayout)findViewById(R.id.tabs_on_historyview);
        tabs.addTab(tabs.newTab().setText("칼로리로 보기"));
        tabs.addTab(tabs.newTab().setText("파이그래프로 영양가분석"));
        tabs.addTab(tabs.newTab().setText(""));

        getSupportFragmentManager().beginTransaction().replace(R.id.container_of_historyview, view_line).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_of_text_in_historyview, comment).commit();
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Log.d("ViewHistoryDataActivy","선택된 탭 : " + position);
                android.support.v4.app.Fragment selected = null;
                if (position ==0){
                    selected = view_line;
                    text_to_line(view_line);
                }else if(position == 1){
                    selected = view_pie;
                    text_to_pie(view_pie);
                }else selected = view_line;

                getSupportFragmentManager().beginTransaction().replace(R.id.container_of_historyview, selected).commit();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });




    }
    private void text_to_line(ViewAllCalendarActivity view_line){


    }

    private void text_to_pie(ViewAllCalendarActivity_byPie view_pie){

    }
}
