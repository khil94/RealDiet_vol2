package org.androidtown.dietapp.Chart;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.handstudio.android.hzgrapherlib.animation.GraphAnimation;
import com.handstudio.android.hzgrapherlib.graphview.CircleGraphView;
import com.handstudio.android.hzgrapherlib.vo.GraphNameBox;
import com.handstudio.android.hzgrapherlib.vo.circlegraph.CircleGraph;
import com.handstudio.android.hzgrapherlib.vo.circlegraph.CircleGraphVO;

import org.androidtown.dietapp.FoodItem;
import org.androidtown.dietapp.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zidru on 2017-09-18.
 */

public class ViewCalendarActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    float rat_carbo, rat_protein, rat_fat;
    int carbo,protein,fat;
    String date;
    int sum;
    int contains;
    ArrayList<FoodItem> userHistoryData ;
    private ViewGroup layoutGraphView;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_calender);
        final TextView textView = (TextView)findViewById(R.id.message_to_calender_viewer);

        layoutGraphView = (ViewGroup) findViewById(R.id.pie_chart);
        contains = 0; sum=0;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference userData = mDatabase.child("user").child(user.getUid());
        DatabaseReference userHistoryRef = mDatabase.child("userHistory").child(user.getUid());

        Intent intent = getIntent();
        int year = intent.getExtras().getInt("Year");
        int month = intent.getExtras().getInt("Month")+1;
        int day = intent.getExtras().getInt("Day");

        if(month<10 && day<10){
            date = String.valueOf(year)+"0"+String.valueOf(month)+"0"+String.valueOf(day);
        }else if(month<10 && day>=10){
            date = String.valueOf(year)+"0"+String.valueOf(month)+String.valueOf(day);
        }else if(day<10){
            date = String.valueOf(year)+String.valueOf(month)+"0"+String.valueOf(day);
        }else date = String.valueOf(year)+String.valueOf(month)+String.valueOf(day);

        DatabaseReference UserHistory = userHistoryRef.child(date);


        userHistoryData = new ArrayList<>();

        UserHistory.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setzero();
                int i=0;
                userHistoryData.clear();
                Log.d("","clear");
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FoodItem foodItem = snapshot.getValue(FoodItem.class);
                    userHistoryData.add(foodItem);
                    setCarbo(getCarbo() + userHistoryData.get(i).getCarbohydrate());
                    setProtein(getProtein() + userHistoryData.get(i).getProtein());
                    setFat(getFat() + userHistoryData.get(i).getFat());
                    setSum(sum + getCarbo() + getFat() + getProtein());
                    Log.d("","contains");
                    setContains(getContains() + 1);
                }
                if(getContains()==0)
                {
                    Toast.makeText(getApplicationContext(), "선택하신 날짜에는 먹은 음식이 없습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    setCircleGraph();

                    rat_carbo =( (float)getCarbo()/ (float)getSum())*100;
                    rat_fat = ( (float)getFat()/ (float)getSum())*100;
                    rat_protein = ( (float)getProtein()/ (float)getSum())*100;

                    Toast.makeText(getApplicationContext(), String.valueOf(getCarbo()), Toast.LENGTH_SHORT).show();
                    if(rat_carbo>=45){
                        textView.setText("too many 탄수화물");
                    }else if(rat_protein>=60){
                        textView.setText("too many 단백질");
                    }else if(rat_fat>=35){
                        textView.setText("too many 지방");
                    }else if(rat_protein<40){
                        textView.setText("지방과 탄수화물을 너무 많이 먹고 있습니다! 단백질이 먹어달라고 울고있어요.");
                    }else if(rat_fat<15){
                        textView.setText("아무리 지방이 안좋아보여도 그렇게 드시면 안됩니다.");
                    }else if(rat_carbo<25) {
                        textView.setText("밀가루음식이나 흰쌀밥등이 아니라면 탄수화물은 식단의 30%는 먹는것이 좋습니다.");
                    }else textView.setText("적당히 균형잡힌 식단이군요.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void setCircleGraph() {
        CircleGraphVO vo = makeLineGraphAllSetting();
        layoutGraphView.addView(new CircleGraphView(this,vo));
    }

    // make circle graph
    private CircleGraphVO makeLineGraphAllSetting() {
        //BASIC LAYOUT SETTING
        //padding
        int paddingBottom 	= CircleGraphVO.DEFAULT_PADDING;
        int paddingTop 		= CircleGraphVO.DEFAULT_PADDING;
        int paddingLeft 	= CircleGraphVO.DEFAULT_PADDING;
        int paddingRight 	= CircleGraphVO.DEFAULT_PADDING;


        //graph margin
        int marginTop 		= CircleGraphVO.DEFAULT_MARGIN_TOP;
        int marginRight 	= CircleGraphVO.DEFAULT_MARGIN_RIGHT;

        // radius setting
        int radius = 130;

        List<CircleGraph> arrGraph 	= new ArrayList<CircleGraph>();


        //GRAPH SETTING
        ViewAllCalendarActivity_byPie users = new ViewAllCalendarActivity_byPie();

        arrGraph.add(new CircleGraph("단백질", Color.GREEN, getCarbo()));
        arrGraph.add(new CircleGraph("탄수화물", Color.RED, getCarbo()));
        arrGraph.add(new CircleGraph("지방", Color.BLUE, getFat()));

        CircleGraphVO vo = new CircleGraphVO(paddingBottom, paddingTop, paddingLeft, paddingRight,marginTop, marginRight,radius, arrGraph);

        // circle Line
        vo.setLineColor(Color.WHITE);

        // set text setting
        vo.setTextColor(Color.BLACK);
        vo.setTextSize(40);

        // set circle center move X ,Y
        vo.setCenterX(0);
        vo.setCenterY(0);

        //set animation
        vo.setAnimation(new GraphAnimation(GraphAnimation.LINEAR_ANIMATION, 2000));
        //set graph name box

        vo.setPieChart(true);

        GraphNameBox graphNameBox = new GraphNameBox();

        // nameBox
        graphNameBox.setNameboxMarginTop(25);
        graphNameBox.setNameboxMarginRight(25);

        vo.setGraphNameBox(graphNameBox);

        return vo;
    }

    public void setzero(){
        setCarbo(0);
        setProtein(0);
        setFat(0);
    }

    public int getSum() {return sum;}
    public void setSum(int sum) {this.sum = sum;}

    public int getContains() {return contains;}

    public void setContains(int contains) {this.contains = contains;}

    public int getCarbo() {
        return carbo;
    }

    public void setCarbo(int carbo) {
        this.carbo = carbo;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }
}