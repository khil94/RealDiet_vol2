// 차트 전체보기.

package org.androidtown.dietapp.Chart;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import org.androidtown.dietapp.EmailPasswordActivity;
import org.androidtown.dietapp.FoodItem;
import org.androidtown.dietapp.MainActivity;
import org.androidtown.dietapp.R;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zidru on 2017-09-27.
 */

public class ViewAllCalendarActivity extends android.support.v4.app.Fragment{
    private ViewGroup layoutGraphView;

    List<FoodItem> datas = new ArrayList<>();

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    int user_calorie;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        layoutGraphView = (ViewGroup) inflater.inflate(R.layout.activity_view_all_calendar, container, false);
        if (user != null) {
        } else {
        }
        DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = RootRef.child("user").child(uid).child("basicCalorie");
        DatabaseReference historyRef = RootRef.child("userHistory").child(uid);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user_calorie = dataSnapshot.getValue(int.class);
                Log.d("", "");
                setLineGraph();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        historyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                datas.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FoodItem data = snapshot.getValue(FoodItem.class);
                    datas.add(data);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return layoutGraphView;

    }



    private void setLineGraph() {
        //all setting
        LineGraphVO vo = makeLineGraphAllSetting();
        layoutGraphView.addView(new LineGraphView(getContext(), vo));
    }
    private int getUserCalorie(){
        return user_calorie;
    }


    private LineGraphVO makeLineGraphAllSetting() {
        //BASIC LAYOUT SETTING
        //padding
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
        int date=datas.size();
        ViewAllCalendarActivity users = new ViewAllCalendarActivity();
        String[] legendArr = new String[5];
        int[] graph1 = new int[5];
        int[] graph2 = new int[5];
        for(int i=0; i<5;i++){
            legendArr[i] = String.valueOf(i+1);
            graph1[i] = 4000+(100*i);
            graph2[i] = user_calorie;
        }


        List<LineGraph> arrGraph = new ArrayList<LineGraph>();

        arrGraph.add(new LineGraph("Calorie", 0xaa66ff33, graph1));
        arrGraph.add(new LineGraph("user_calorie", 0xaa00ffff, graph2));


        LineGraphVO vo = new LineGraphVO(
                paddingBottom, paddingTop, paddingLeft, paddingRight,
                marginTop, marginRight, maxValue, increment, legendArr, arrGraph);

        //set animation
        vo.setAnimation(new GraphAnimation(GraphAnimation.LINEAR_ANIMATION, GraphAnimation.DEFAULT_DURATION));
        //set graph name box
        vo.setGraphNameBox(new GraphNameBox());

        return vo;
    }
}
