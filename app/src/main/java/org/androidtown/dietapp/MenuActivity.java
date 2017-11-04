package org.androidtown.dietapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//가져올때 heap 구조
 public class MenuActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference userHistoryRef;
    private DatabaseReference foodRef;
    private View.OnClickListener listener;
    private Button buttonSearch;
    private Button buttonAddMenu;
    private EditText edit;
    private RecyclerView recyclerView;
    private FoodAdapter adapter;
    private FirebaseUser user;
    private String dateStr;
    public ArrayList<FoodItem> foodItemList;


    //자료구조 데모 선언 시작
    private DataStructure datastructure;
    private ArrayList<FoodItem> searchedItemList;
    //자료구조 데모 선언 끝

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        dateStr = getIntent().getStringExtra("dateStr");
        database = FirebaseDatabase.getInstance();
        userHistoryRef =database.getReference().child("userHistory").child(user.getUid()).child(dateStr);
        foodRef = database.getReference().child("food");

        foodItemList = new ArrayList<FoodItem>();

        //자료구조 데모 init
        datastructure=DataStructure.getInstance();
        datastructure.setFoodList(foodItemList);
        //자료구조 데모 init 끝

        recyclerView=(RecyclerView)findViewById(R.id.user_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lim = new LinearLayoutManager(this);
        lim.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lim);
        adapter = new FoodAdapter(foodItemList);
        adapter.setHistoryRef(userHistoryRef);
        recyclerView.setAdapter(adapter);
        updateFoodList();

        buttonSearch=(Button)findViewById(R.id.buttonSearch);
        buttonAddMenu=(Button)findViewById(R.id.buttonAddMenu);
        edit=(EditText)findViewById(R.id.edit);
        buttonAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(MenuActivity.this,ItemAddActivity.class);
                startActivity(addIntent);
            }
        });
        listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.buttonSearch:
                        //검색이 됨 그리고 반환을 searchedItemList로 반환
                        //
                        searchedItemList=datastructure.search(edit.getText().toString());
                        adapter.setFoodList(searchedItemList);
                        adapter.notifyDataSetChanged();
                        //adapter.setFoodList(foodItemList);
                        break;
                    case R.id.user_list:
                }
            }
        };


        buttonSearch.setOnClickListener(listener);
    }


    private  void updateFoodList(){
        if(foodRef == null){
            return;
        }
        foodRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                foodItemList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    FoodItem foodItem = snapshot.getValue(FoodItem.class);
                    if(foodItem!= null) {
                        foodItemList.add(foodItem);
                    }
                }
                //sort부분 시작
                //data의 수정이 일어날때 마다 sorting 함
                datastructure.sort();
                //sort부분 끝
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
