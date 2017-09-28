package org.androidtown.dietapp;

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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private DatabaseReference baseCalRef;
    int todayCal;
    private int me;
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@


    //리사이클러 뷰 시작
    private RecyclerView recyclerView;
    private List<String> uidList;
    private HistoryAdapter adapter;
    //리사이클러 뷰 끝

    //데이터베이스 시작
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private DatabaseReference userHistoryRef;//history에서 음식의 목록을 임시로 가져옴 만약에 userItem을 통째로 가져온다면 필요없을 부분
    private DatabaseReference myRef;//나이 UserItem을 가져와야 처리할수 있는 일이 많음
    private DatabaseReference userRef;
    private FirebaseUser user; //변경사항 위로올림!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //리사이클러뷰 시작
        uidList=new ArrayList<>();
        recyclerView=(RecyclerView)findViewById(R.id.user_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lim = new LinearLayoutManager(this);
        lim.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lim);
        //리사이클러뷰 끝\


        //데이터 베이스 시작
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");
        //데이터 베이스 끝


        userInfo_btn=(Button) findViewById(R.id.btn_userinfo);
        menu_btn=(Button)findViewById(R.id.btn_menu);
        chart_btn=(Button)findViewById(R.id.btn_chart);
        calorie_pbar=(ProgressBar)findViewById(R.id.pbar_calorie);
        percentage_view=(TextView)findViewById(R.id.view_percentage);
        //현재 만지고 있는 부분 수정하지 말고 주석 풀지도 말 것

        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser(); //위로뺌;
        if(user==null) {
            Intent AuthIntent = new Intent(MainActivity.this,EmailPasswordActivity.class);
            startActivity(AuthIntent);
        }
        else {
            myRef=database.getReference().child("user").child(user.getUid());
            baseCalRef = database.getReference().child("user").child(user.getUid()).child("basicCalorie");
            userHistoryRef=myRef.child("history");
            userRef=database.getReference().child("user");
        }
        /*
        uid = user.getUid();
        DatabaseReference mMyInfoRef = mRef.child("user").child(uid);
        //기본 선언 끝
        //name basicCalorie weight 받아오는구간
        mMyInfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myInfo = dataSnapshot.getValue(UsersItem.class);
                name = myInfo.getName();
                basicCalorie = myInfo.getBasicCalorie();
                weight = myInfo.getWeight();
               // textView.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                name = "실패";
                Log.i("파이어베이스 실패","Fuckyou");
            }
        });
        */

        adapter = new HistoryAdapter(uidList);
        recyclerView.setAdapter(adapter);
        updateUIDList();
        progress=0;

        // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        setProgress();
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@


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

    /*지금당장 무쓸모 노이해
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case 0:
                break;

            case 1:
                break;
        }

        return super.onContextItemSelected(item);
    }
*/

    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2
    private void setProgress()
    {
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
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2s

//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //지금 현재는 음식이 데이터베이스에 들어있지 않으니 임시로 음식하나당 20cal로 계산합니당 추후 변경해야함
    private void calculateTodayCal(){

        todayCal=uidList.size()*20;
        calorie_pbar.setMax(100);
        progress = todayCal*100;
        progress = progress/me;
        calorie_pbar.setProgress(progress);

        percentage_view.setText(String.valueOf(todayCal)+"/"+String.valueOf(me)+"//"+String.valueOf(progress));
    }
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@22
    //아래함수와 유사하게 나의 UserItem을 가져와서 pbar와 관련된 처리도 해야하고
    //위와 마찬가지로 음식의 목록을 푸드 트리에서 찾아가거나 아에 history가 FoodItem의 어레이리스트 여야한다.
    private void updateUIDList() {
        //차일드 리스너로 바꾸는게 적당할듯? -> 바꾸면 에러 쌈박하게 터짐
        userHistoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uidList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
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
    //나의 유저정보를 어떻게 가져올까? 배열로 가져오는데 나는 무슨 배열로 받아야 알수있을까?
    //user 노드에서 usetsItem을 배열로 가져와서 그 중에 uid가 같은걸 찾아서 붙여야 하는듯?
    //만약 그런거면 아래 래퍼런스에 리스너 선언 부분을 바꾸어야함
    //한번만 가져오는 함수가 있으면 그걸로 바꾸면됨

    /* 일단 주석 구동과 상관없다 브로
    private int getItemIndex(FoodItem user){

        int index = -1;

        for(int i=0; i<foodItemList.size(); i++){
            if(foodItemList.get(i).uid.equals(user.uid)){
                index=i;
                break;
            }
        }
        return index;

    }
*/

}
