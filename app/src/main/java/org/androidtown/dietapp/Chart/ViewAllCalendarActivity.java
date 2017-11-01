// 차트 전체보기.

package org.androidtown.dietapp.Chart;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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
import com.handstudio.android.hzgrapherlib.graphview.LineGraphView;
import com.handstudio.android.hzgrapherlib.vo.GraphNameBox;
import com.handstudio.android.hzgrapherlib.vo.linegraph.LineGraph;
import com.handstudio.android.hzgrapherlib.vo.linegraph.LineGraphVO;

import org.androidtown.dietapp.FoodItem;
import org.androidtown.dietapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zidru on 2017-09-27.
 */

public class ViewAllCalendarActivity extends android.support.v4.app.Fragment{
    private ViewGroup layoutGraphView;

    List<FoodItem> datas = new ArrayList<>();
    int sum_of_calorie[];
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    int user_calorie;
    int dates;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        dates=0;
        sum_of_calorie = new int[30];
        layoutGraphView = (ViewGroup) inflater.inflate(R.layout.activity_view_all_calendar, container, false);
        if (user != null) {
        } else {
        }
        DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference userRef = RootRef.child("user").child(uid).child("basicCalorie");

        DatabaseReference historyRef = RootRef.child("userHistory").child(uid);

        historyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int j=0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    int i=0; datas.clear();
                    for(DataSnapshot snapshot2 : snapshot.getChildren()){
                        FoodItem data = snapshot2.getValue(FoodItem.class);
                        datas.add(data);
                        setSum_of_calorie(j,getSum_of_calorie(j)+datas.get(i).getCalorie());
                        i++;
                    }
                    j++;
                    setDates(getDates()+1);
                }
                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int u_cal = dataSnapshot.getValue(int.class);
                        Log.d("", "");
                        setUser_calorie(u_cal);
                        setLineGraph();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return layoutGraphView;
    }

    // make line graph
    private LineGraphVO makeLineGraphAllSetting() {

        int paddingBottom 	= LineGraphVO.DEFAULT_PADDING;
        int paddingTop 		= LineGraphVO.DEFAULT_PADDING;
        int paddingLeft 	= LineGraphVO.DEFAULT_PADDING;
        int paddingRight 	= LineGraphVO.DEFAULT_PADDING;

        //graph margin
        int marginTop 		= LineGraphVO.DEFAULT_MARGIN_TOP;
        int marginRight 	= LineGraphVO.DEFAULT_MARGIN_RIGHT;

        //max value
        int maxValue 		= LineGraphVO.DEFAULT_MAX_VALUE;

        //increment
        int increment 		= LineGraphVO.DEFAULT_INCREMENT;

        //GRAPH SETTING

        ViewAllCalendarActivity users = new ViewAllCalendarActivity();
        String[] legendArr = new String[getDates()];
        int[] graph1 = new int[getDates()];
        int[] graph2 = new int[getDates()];
        for(int i=0; i<getDates();i++){
            legendArr[i] = String.valueOf(i+1)+"일차";
            graph1[i] = sum_of_calorie[i];
            graph2[i] = getUser_calorie();
        }

        List<LineGraph> arrGraph = new ArrayList<LineGraph>();

        arrGraph.add(new LineGraph("Calorie", Color.RED, graph1));
        arrGraph.add(new LineGraph("user_calorie", Color.BLACK, graph2));

        LineGraphVO vo = new LineGraphVO(
                paddingBottom, paddingTop, paddingLeft, paddingRight,
                marginTop, marginRight, maxValue, increment, legendArr, arrGraph);

        //set animation
        vo.setAnimation(new GraphAnimation(GraphAnimation.LINEAR_ANIMATION, GraphAnimation.DEFAULT_DURATION));
        //set graph name box
        vo.setGraphNameBox(new GraphNameBox());

        return vo;
    }

    //getter and setter
    public void setUser_calorie(int u_cal){
        this.user_calorie = u_cal;
    }
    public void setDates(int dates){
        this.dates = dates;
    }
    public int getUser_calorie(){
        return user_calorie;
    }
    public int getDates(){
        return dates;
    }
    public int getSum_of_calorie(int index) {
        return sum_of_calorie[index];
    }
    public void setSum_of_calorie(int index, int value){
        sum_of_calorie[index] = value;
    }

    //set drawing graph
    private void setLineGraph() {
        LineGraphVO vo = makeLineGraphAllSetting();
        layoutGraphView.addView(new LineGraphView(getContext(), vo));
    }
}
