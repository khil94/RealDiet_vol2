package org.androidtown.dietapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private DatabaseReference foodRef;
    private View.OnClickListener listener;
    private Button buttonSearch;
    private EditText edit;
    private RecyclerView recyclerView;
    private FoodAdapter adapter;
    private FirebaseUser user;
    private ArrayList<FoodItem> foodItemList;
    private datastructure data;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        userHistoryRef =database.getReference().child("user").child(user.getUid()).child("history");
        foodRef = database.getReference().child("food");

        foodItemList = new ArrayList<FoodItem>();

        recyclerView=(RecyclerView)findViewById(R.id.user_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lim = new LinearLayoutManager(this);
        lim.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lim);
        adapter = new FoodAdapter(foodItemList);
        adapter.setHistoryRef(userHistoryRef);
        recyclerView.setAdapter(adapter);
        updateFoodList();

        data.getInstance();
        data.setFoodList(foodItemList);

        buttonSearch=(Button)findViewById(R.id.buttonSearch);
        edit=(EditText)findViewById(R.id.edit);

        listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.buttonSearch:
                        //나중에 검색버튼으로 추가
                        Toast.makeText(MenuActivity.this, "검색되었습니다", Toast.LENGTH_SHORT).show();
                        data.search(edit.getText().toString());
                        break;
                    case R.id.user_list:
                }
            }
        };


        buttonSearch.setOnClickListener(listener);

    /*잠시 주석!!!!
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(),e.getY());
                if(child != null){
                    TextView tv = (TextView) rv.getChildViewHolder(child).itemView.findViewById(R.id.foodName);
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        */
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
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
