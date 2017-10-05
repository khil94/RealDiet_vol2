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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference baseCalRef;
    int todayCal;
    private int me;


    //리사이클러 뷰 시작
    private RecyclerView recyclerView;
    private List<String> uidList;
    private HistoryAdapter adapter;
    //리사이클러 뷰 끝

    //데이터베이스 시작
    private FirebaseDatabase database;
    private DatabaseReference userHistoryRef;//history에서 음식의 목록을 임시로 가져옴 만약에 userItem을 통째로 가져온다면 필요없을 부분
    private DatabaseReference myRef;//나이 UserItem을 가져와야 처리할수 있는 일이 많음
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
    private int progress;
    public static Context mainContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //최상위 건드리지 말것 시작
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser(); //위로뺌;
        if(user==null) {
            Intent AuthIntent= new Intent(MainActivity.this, EmailPasswordActivity.class);
            startActivity(AuthIntent);
            user=mAuth.getCurrentUser();
        }else{
            database = FirebaseDatabase.getInstance();
            myRef=database.getReference().child("user").child(user.getUid());
            baseCalRef = database.getReference().child("user").child(user.getUid()).child("basicCalorie");
            userHistoryRef=myRef.child("history");
        }
        //건드리지 말것 끝
        mainContext=this;
        //리사이클러뷰 시작
        uidList=new ArrayList<>();
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

        adapter = new HistoryAdapter(uidList);
        recyclerView.setAdapter(adapter);
        updateUIDList();
        progress=0;
        setProgress();

        //변경할 사항 리스트 뷰의 아이템을 눌렀을때 자세한 액티비티를 보여줄 부분이 필요함
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
                        startActivity(menuIntent);
                        break;
                    case R.id.btn_chart:
                    {
                        Intent chartIntent = new Intent(MainActivity.this,ChartActivity.class);
                        startActivity(chartIntent);
                    }
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
        if(baseCalRef==null){
            return;
        }
        baseCalRef.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                me = dataSnapshot.getValue(int.class);
                calculateTodayCal();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //지금 현재는 음식이 데이터베이스에 들어있지 않으니 임시로 음식하나당 20cal로 계산합니당 추후 변경해야함
    private void calculateTodayCal(){
        if(me==0)return;
        todayCal=uidList.size()*20;
        calorie_pbar.setMax(100);
        progress = todayCal*100;
        progress = progress/me;
        calorie_pbar.setProgress(progress);
        percentage_view.setText(progress + "%");
    }

    private void updateUIDList() {
        //차일드 리스너로 바꾸는게 적당할듯? -> 바꾸면 에러 쌈박하게 터짐
        if(userHistoryRef==null){
            return;
        }
        userHistoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uidList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String uid = snapshot.getValue(String.class);
                    uidList.add(uid);
                    calculateTodayCal();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void initDatabase(){
        database = FirebaseDatabase.getInstance();
        myRef=database.getReference().child("user").child(user.getUid());
        baseCalRef = database.getReference().child("user").child(user.getUid()).child("basicCalorie");
        userHistoryRef=myRef.child("history");
    }
}
