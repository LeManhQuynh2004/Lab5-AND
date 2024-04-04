package com.quynhlm.dev.lab5.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.quynhlm.dev.lab5.Model.Fruits;
import com.quynhlm.dev.lab5.R;

import java.util.List;

public class FruitsAdapter extends RecyclerView.Adapter<FruitsAdapter.FruitsViewHolder>{
    Context context;
    List<Fruits> list;

    public FruitsAdapter(Context context, List<Fruits> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FruitsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_fruits,parent,false);
        return new FruitsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FruitsViewHolder holder, int position) {
        Fruits fruits = list.get(position);
        holder.tv_name.setText(fruits.getName());
        holder.tv_price.setText(fruits.getPrice()+"");
        holder.tv_quantity.setText(fruits.getQuantity()+"");
        holder.tv_status.setText(fruits.getStatus()+"");
        Glide
                .with(context)
                .load(fruits.getImage()[0])
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imgFruits);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class FruitsViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name,tv_price,tv_quantity,tv_status,tv_distribute_fruits;
        ImageView imgFruits;
        public FruitsViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name_fruits);
            tv_quantity = itemView.findViewById(R.id.tv_quantity_fruits);
            tv_price = itemView.findViewById(R.id.tv_price_fruits);
            tv_distribute_fruits = itemView.findViewById(R.id.tv_distrbute_fruits);
            tv_status = itemView.findViewById(R.id.tv_status_fruits);
            imgFruits = itemView.findViewById(R.id.img_fruits);
        }
    }
}
