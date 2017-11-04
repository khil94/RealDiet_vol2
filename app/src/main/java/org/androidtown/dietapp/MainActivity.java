package org.androidtown.dietapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.androidtown.dietapp.Chart.ChartActivity;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "!!!!!!!!!!MAIN!!!!!";
    //날짜 관련
    private long now;
    private SimpleDateFormat dateFormat;
    private Date date;
    private String dateStr;

    //프로그레스바 관련
    private DatabaseReference basicCalRef;
    int todayCal;
    private int basicCal;
    private int progress;
    //프로그레스바 끝

    //리사이클러 뷰 시작
    private RecyclerView recyclerView;
    private List<FoodItem> historyList;
    private HistoryAdapter adapter;
    //리사이클러 뷰 끝

    //데이터베이스 시작
    private FirebaseDatabase database;
    private DatabaseReference myHistoryRef;
    private FirebaseUser user;
    //데이터베이스 끝

    //레이아웃
    private Button userInfo_btn;
    private Button menu_btn;
    private Button chart_btn;
    private ProgressBar calorie_pbar;
    private TextView percentage_view;
    private View.OnClickListener listener;
    //레이아웃 끝

    //기타 변수
    public static Context mainContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getDate();


        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser(); //위로뺌;
        if(user==null) {
            Intent AuthIntent= new Intent(MainActivity.this, EmailPasswordActivity.class);
            startActivity(AuthIntent);
            user=mAuth.getCurrentUser();
        }else {
            database = FirebaseDatabase.getInstance();
            basicCalRef = database.getReference().child("user").child(user.getUid()).child("basicCalorie");
            myHistoryRef = database.getReference().child("userHistory").child(user.getUid()).child(dateStr);
        }

        mainContext=this;



        //리사이클러뷰 시작
        historyList=new ArrayList<>();
        recyclerView=(RecyclerView)findViewById(R.id.user_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lim = new LinearLayoutManager(this);
        lim.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lim);
        //리사이클러뷰 끝\


        //데이터 베이스 시작
        if(user!=mAuth.getCurrentUser())initDatabase();
        //데이터 베이스 끝


        userInfo_btn=(Button) findViewById(R.id.btn_userinfo);
        menu_btn=(Button)findViewById(R.id.btn_menu);
        chart_btn=(Button)findViewById(R.id.btn_chart);
        calorie_pbar=(ProgressBar)findViewById(R.id.pbar_calorie);
        percentage_view=(TextView)findViewById(R.id.view_percentage);

        adapter = new HistoryAdapter(historyList);
        recyclerView.setAdapter(adapter);
        updateHistoryList();
        progress=0;
        setProgress();

        listener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_userinfo:
                        Intent AuthIntent = new Intent(MainActivity.this,UserInfoActivity.class);
                        startActivity(AuthIntent);
                        initDatabase();
                        finish();
                        break;
                    case R.id.btn_menu:
                        Intent menuIntent = new Intent(MainActivity.this,MenuActivity.class);
                        menuIntent.putExtra("dateStr",dateStr);
                        startActivity(menuIntent);
                        break;
                    case R.id.btn_chart:
                        Intent chartIntent = new Intent(MainActivity.this,ChartActivity.class);
                        startActivity(chartIntent);
                    break;
                }
            }
        };
        userInfo_btn.setOnClickListener(listener);
        menu_btn.setOnClickListener(listener);
        chart_btn.setOnClickListener(listener);
    }


    private void setProgress()
    {
        if(basicCalRef==null){
            return;
        }
        basicCalRef.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                basicCal = dataSnapshot.getValue(int.class);
                calculateTodayCal();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void calculateTodayCal(){
        if(basicCal==0)return;
        todayCal=0;
        for(int i=0;i<historyList.size();i++){
            int cal = historyList.get(i).getCalorie();
            todayCal = todayCal+cal;
        }
        calorie_pbar.setMax(100);
        progress = todayCal*100;
        progress = progress/basicCal;
        calorie_pbar.setProgress(progress);
        percentage_view.setText(progress + "%");
    }

    private void updateHistoryList() {
        if(myHistoryRef==null){
            return;
        }
        myHistoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                historyList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FoodItem food = snapshot.getValue(FoodItem.class);
                    historyList.add(food);
                }
                calculateTodayCal();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void initDatabase(){
        database = FirebaseDatabase.getInstance();
        basicCalRef = database.getReference().child("user").child(user.getUid()).child("basicCalorie");
        myHistoryRef = database.getReference().child("userHistory").child(user.getUid()).child(dateStr);
    }

    private void getDate(){
        now=System.currentTimeMillis();
        dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        date = new Date(now);
        dateStr =  dateFormat.format(date);
    }

}
