package org.androidtown.dietapp;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
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

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{

    private List<UserModel> list;

    public UserAdapter(List<UserModel> list) {
        this.list = list;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item,parent,false));
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, int position) {
        UserModel user= list.get(position);

        holder.textName.setText(user.firstName + " " + user.lastName );
        holder.textAge.setText(user.age+"");
        holder.textJob.setText(user.job);

        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener(){
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(holder.getAdapterPosition(), 0, 0, "무슨말");
                menu.add(holder.getAdapterPosition(), 1, 0, "모름");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{
        TextView textName,textAge,textJob;

        public UserViewHolder(View itemView){
            super(itemView);

            textAge=(TextView)itemView.findViewById(R.id.text_age);
            textName=(TextView)itemView.findViewById(R.id.text_name);
            textJob=(TextView)itemView.findViewById(R.id.text_job);
        }
    }
}
