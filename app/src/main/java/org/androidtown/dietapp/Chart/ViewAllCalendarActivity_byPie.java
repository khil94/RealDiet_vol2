// 차트 전체보기.

package org.androidtown.dietapp.Chart;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
 * Created by zidru on 2017-09-27.
 */

public class ViewAllCalendarActivity_byPie extends android.support.v4.app.Fragment{
    private ViewGroup layoutGraphView;

    ArrayList<FoodItem> foods = new ArrayList<FoodItem>();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    int carbo,protein,fat;
    int user_calorie;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        layoutGraphView = (ViewGroup) inflater.inflate(R.layout.activity_view_all_calendar_bypie, container, false);

        DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = RootRef.child("user").child(uid).child("basicCalorie");
        final DatabaseReference historyRef = RootRef.child("userHistory").child(uid);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user_calorie = dataSnapshot.getValue(int.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        historyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setCarbo(0);
                setProtein(0);
                setFat(0);
                int j=0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    foods.clear();
                    for(DataSnapshot snapshot_food : snapshot.getChildren()){
                        FoodItem data = snapshot_food.getValue(FoodItem.class);
                        foods.add(data);
                        setCarbo(getCarbo() + foods.get(j).getCarbohydrate());
                        setProtein(getProtein() + foods.get(j).getProtein());
                        setFat(getFat() + foods.get(j).getFat());
                        j++;
                    }
                    j=0;
                }
                setCircleGraph();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return layoutGraphView;
    }

    // drawing circle graph
    private void setCircleGraph() {
        CircleGraphVO vo = makeLineGraphAllSetting();
        layoutGraphView.addView(new CircleGraphView(getContext(),vo));
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
        vo.setLineColor(Color.BLACK);

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

    // getter and setter
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
