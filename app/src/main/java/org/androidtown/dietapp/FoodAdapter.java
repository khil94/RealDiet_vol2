package org.androidtown.dietapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by azxca on 2017-09-21.
 */

/**
 *  현재 예시로 보여주는 부분
 *  UserModel을 DB에 전달해주는 매체 DTO로 구현
 *  후에 FOODITEM이든 USERITEM이든 넣을수 있음
 **/

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder>{

    //    private List<UserModel> list; 원본
    //   private List<FoodItem> list;
    private List<String> uidList;

    /* 원본
    public UserAdapter(List<UserModel> list) {
        this.list = list;
    }
    */
    /* 수정
    public FoodAdapter(List<FoodItem> list) {
        this.list=list;

    }
*/
    public FoodAdapter(List<String> uidList) {
        this.uidList=uidList;

    }

    public void setUidList(List<String> uidList) {
        this.uidList = uidList;
    }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FoodViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item,parent,false));
    }

    @Override
    public void onBindViewHolder(final FoodViewHolder holder, int position) {
        //  UserModel user= list.get(position);원본
        //   FoodItem user = list.get(position); 수정
        String user = uidList.get(position);

        /*원본
        holder.textName.setText(user.firstName + " " + user.lastName );
        holder.textAge.setText(user.age+"");
        holder.textJob.setText(user.job);
        */
        //     holder.textName.setText(user.getName()); 수정
        //    holder.textCal.setText(user.getCalorie()); 수정


        holder.textName.setText(user);
        holder.textCal.setText("20");
    }

        /* 머름
        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener(){
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(holder.getAdapterPosition(), 0, 0, "무슨말");
                menu.add(holder.getAdapterPosition(), 1, 0, "모름");
            }
        });

    }
*/


    @Override
    public int getItemCount() {
        //    return list.size(); 수정
        return uidList.size();
    }

    class FoodViewHolder extends RecyclerView.ViewHolder{
        TextView textName,textCal;

        public FoodViewHolder(View itemView){
            super(itemView);

            //    textAge=(TextView)itemView.findViewById(R.id.text_age);
            textName=(TextView)itemView.findViewById(R.id.text_name);
            textCal=(TextView)itemView.findViewById(R.id.text_cal);
            //    textJob=(TextView)itemView.findViewById(R.id.text_job);
        }
    }
}
