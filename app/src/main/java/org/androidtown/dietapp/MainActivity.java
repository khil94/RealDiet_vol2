package org.androidtown.dietapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //리사이클러 뷰 시작
    private RecyclerView recyclerView;
    private List<UserModel> result;
    private UserAdapter adapter;
    //리사이클러 뷰 끝

    //데이터베이스 시작
    private  FirebaseDatabase database;
    private  DatabaseReference reference;
    //데이터베이스 끝

    Button userInfo_btn;
    Button menu_btn;
    Button chart_btn;
    Button plus_btn;
    Button minus_btn;
    ProgressBar calorie_pbar;
    TextView percentage_view;
    int progress;
    View.OnClickListener listener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //리사이클러뷰 시작
        result= new ArrayList<>();

        recyclerView=(RecyclerView)findViewById(R.id.user_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lim = new LinearLayoutManager(this);
        lim.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(lim);


        adapter= new UserAdapter(result);
        recyclerView.setAdapter(adapter);

        //리사이클러뷰 끝\


        //데이터 베이스 시작
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");

        //데이터 베이스 끝


        userInfo_btn=(Button) findViewById(R.id.btn_userinfo);
        menu_btn=(Button)findViewById(R.id.btn_menu);
        chart_btn=(Button)findViewById(R.id.btn_chart);
        plus_btn=(Button)findViewById(R.id.btn_plus);
        minus_btn=(Button)findViewById(R.id.btn_minus);
        calorie_pbar=(ProgressBar)findViewById(R.id.pbar_calorie);
        percentage_view=(TextView)findViewById(R.id.view_percentage);
        //현재 만지고 있는 부분 수정하지 말고 주석 풀지도 말 것

        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user==null) {
            Intent AuthIntent = new Intent(MainActivity.this,EmailPasswordActivity.class);
            startActivity(AuthIntent);
        }
        else {

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
        });*/

      //리스트뷰에 보여줄 아이템을 추가하는 부분 지금은 예시라 스트링을 넣었음 추후에 db에서 가져온 오늘 먹은 음식의 이름을 추가하게 변경예정



        //어댑터 객체화후 리스트뷰에 추가하는 부분
        progress=0;

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
                    case R.id.btn_plus:
                        if (progress<100)
                        {
                            progress++;
                            percentage_view.setText(progress+"%");
                            calorie_pbar.setProgress(progress);
                        }
                        break;
                    case R.id.btn_minus:
                        if(progress>0)
                        {
                            progress--;
                            percentage_view.setText(progress+"%");
                            calorie_pbar.setProgress(progress);
                        }
                        break;
                }
            }
        };
        userInfo_btn.setOnClickListener(listener);
        menu_btn.setOnClickListener(listener);
        chart_btn.setOnClickListener(listener);
        plus_btn.setOnClickListener(listener);
        minus_btn.setOnClickListener(listener);

        updateList();
    }

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

    private void updateList(){
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                result.add(dataSnapshot.getValue(UserModel.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                UserModel model = dataSnapshot.getValue(UserModel.class);

                int index = getItemIndex(model);

                result.set(index, model);
                adapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                UserModel model = dataSnapshot.getValue(UserModel.class);

                int index = getItemIndex(model);

                result.remove(index);
                adapter.notifyItemRemoved(index);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private int getItemIndex(UserModel user){

        int index = -1;

        for(int i=0; i<result.size(); i++){
            if(result.get(i).key.equals(user.key)){
                index=i;
                break;
            }
        }
        return index;

    }

}
