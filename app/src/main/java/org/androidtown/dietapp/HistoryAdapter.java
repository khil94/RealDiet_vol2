package org.androidtown.dietapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.FoodViewHolder>{

    private List<FoodItem> historyList;

    public HistoryAdapter(List<FoodItem> historyList) {
        this.historyList=historyList;

    }

    public void setUidList(List<FoodItem> historyList) {
        this.historyList = historyList;
    }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FoodViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item,parent,false));
    }

    @Override
    public void onBindViewHolder(final FoodViewHolder holder, int position) {

        FoodItem food = historyList.get(position);

        holder.textName.setText(food.getName());
        holder.textCal.setText(String.valueOf(food.getCalorie()));
    }


    @Override
    public int getItemCount() {
        return historyList.size();
    }

    class FoodViewHolder extends RecyclerView.ViewHolder{
        TextView textName,textCal;

        public FoodViewHolder(View itemView){
            super(itemView);

            textName=(TextView)itemView.findViewById(R.id.text_name);
            textCal=(TextView)itemView.findViewById(R.id.text_cal);
        }
    }
}
