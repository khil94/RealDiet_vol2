package org.androidtown.dietapp;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;


public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder>{

    private List<FoodItem> foodList;

    public void setFoodList(List<FoodItem> foodList) {
        this.foodList = foodList;
    }

    public void setHistoryRef(DatabaseReference historyRef) {
        this.historyRef = historyRef;
    }

    private DatabaseReference historyRef;
    public FoodAdapter(ArrayList<FoodItem> foodList) {
        this.foodList = foodList;

    }

    public void setUidList(ArrayList<FoodItem> foodList) {
        this.foodList =foodList;
    }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FoodViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_menu,parent,false));
    }

    @Override
    public void onBindViewHolder(final FoodViewHolder holder, int position) {
        FoodItem foodItem = foodList.get(position);

        holder.textName.setText(foodItem.getName());
        holder.textCategory.setText("카테고리: "+foodItem.getCategory());
        holder.textCal.setText("칼로리: "+String.valueOf(foodItem.getCalorie()));
        holder.textProtain.setText("단백질: "+String.valueOf(foodItem.getProtein()));
        holder.textCarbohydrate.setText("탄수화물: "+String.valueOf(foodItem.getCarbohydrate()));
        holder.textFat.setText("지방: "+String.valueOf(foodItem.getFat()));

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
        return foodList.size();
    }

    class FoodViewHolder extends RecyclerView.ViewHolder{
        TextView textName,textCategory,textCal,textProtain,textCarbohydrate,textFat;

        public FoodViewHolder(View itemView){
            super(itemView);

            textName=(TextView)itemView.findViewById(R.id.foodName);
            textCategory=(TextView)itemView.findViewById(R.id.foodCategory);
            textCal=(TextView)itemView.findViewById(R.id.foodCal);
            textProtain=(TextView)itemView.findViewById(R.id.foodProtain);
            textCarbohydrate=(TextView)itemView.findViewById(R.id.foodCarbohydrate);
            textFat=(TextView)itemView.findViewById(R.id.foodFat);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Snackbar.make(v,textName.getText()+" 선택",Snackbar.LENGTH_LONG).setAction("add to history", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(historyRef!=null){
                                historyRef.push().setValue(textName.getText());
                            }
                        }
                    }).show();
                }
            });
        }
    }
}
