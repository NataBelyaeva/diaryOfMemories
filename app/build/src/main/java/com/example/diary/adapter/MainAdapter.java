package com.example.diary.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diary.Note;
import com.example.diary.R;
import com.example.diary.db.MyConstants;
import com.example.diary.db.MyDbManager;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {
    private Context context;
    private List<ListItem> mainArray;
    private RecyclerView rcVeiw;

    public MainAdapter(Context context) {
        this.context = context;
        mainArray = new ArrayList<>();

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_layout, parent, false);
        return new MyViewHolder(view, context, mainArray);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setData(mainArray.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mainArray.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvTitle;
        private Context context;
        private List<ListItem> mainArray;

        public MyViewHolder(@NonNull View itemView, Context context, List<ListItem> mainArray) {

            super(itemView);
            this.context = context;
            this.mainArray = mainArray;
            tvTitle = itemView.findViewById(R.id.tvTitle);
            itemView.setOnClickListener(this);
        }
        public void setData(String title){

            tvTitle.setText(title);
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(context, Note.class);
            i.putExtra(MyConstants.LIST_ITEM_INTENT, mainArray.get(getAdapterPosition()));
            i.putExtra(MyConstants.EDIT_STATE, false);
            context.startActivity(i);
        }
    }
    public void updateAdapter(List<ListItem> newList){
        mainArray.clear();
        mainArray.addAll(newList);
        notifyDataSetChanged();
    }

    public void removeItem(int pos, MyDbManager dbManager){
        dbManager.delete(mainArray.get(pos).getId());
        mainArray.remove(pos);
        notifyItemChanged(0, mainArray.size());
        notifyItemRemoved(pos);
    }
}
