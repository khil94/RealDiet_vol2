package org.androidtown.dietapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference userHistoryRef;
    View.OnClickListener listener;
    Button buttonSearch;
    RecyclerView recyclerView;
    ArrayList<String> uidList;
    FoodAdapter adapter;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
/*
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        userHistoryRef =database.getReference().child("user").child("QieIMbdw8nUWSPV74dAnj9r590r1").child("history");
        uidList=new ArrayList<>();
        recyclerView=(RecyclerView)findViewById(R.id.user_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lim = new LinearLayoutManager(this);
        lim.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lim);
        updateUIDList();
        adapter = new FoodAdapter(uidList);
        recyclerView.setAdapter(adapter);

        buttonSearch=(Button)findViewById(R.id.buttonSearch);
        listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId())
                {
                    case R.id.buttonSearch:
                        //나중에 검색버튼으로 추가
                        Toast.makeText(MenuActivity.this, "검색되었습니다", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        */
    }
    /*
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
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    */
}
