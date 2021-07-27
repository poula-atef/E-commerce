package com.example.e_commerce.Pojo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_commerce.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@SuppressLint("ResourceType")
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private Drawable background;
    private Context context;
    private ArrayList<ItemClass> items;
    private onItemClickListener listener;
    @NonNull
    @NotNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new CardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CardAdapter.CardViewHolder holder, int position) {
        holder.itemView.setAnimation(AnimationUtils.loadAnimation(context,R.animator.to_left));
        holder.itemView.setTag(items.get(position).getName());
        Glide.with(context).load(items.get(position).getImage()).into(holder.img);
        holder.title.setText(items.get(position).getName());
        holder.quantity.setText(items.get(position).getQuantity() + "");
        holder.price.setText(items.get(position).getPrice() + " EGP");
        holder.minus.setBackground(background);
        holder.plus.setBackground(background);
    }

    public void setItems(ArrayList<ItemClass> items) {
        this.items = items;
    }

    public ArrayList<ItemClass> getItems() {
        return items;
    }

    public void setListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public void setBackground(Drawable background) {
        this.background = background;
    }

    @Override
    public int getItemCount() {
        if(items == null)
            return 0;
        return items.size();
    }

    public interface onItemClickListener{
        void onMinusClick(ItemClass item);
        void onPlusClick(ItemClass item);
    }

    class CardViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView title,quantity,price;
        Button plus,minus;
        public CardViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.quantity);
            title = itemView.findViewById(R.id.title);
            plus = itemView.findViewById(R.id.plus_btn);
            minus = itemView.findViewById(R.id.min_btn);
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    items.get(getAdapterPosition()).setQuantity(items.get(getAdapterPosition()).getQuantity()+1);
                    quantity.setText(items.get(getAdapterPosition()).getQuantity() + "");
                    listener.onPlusClick(items.get(getAdapterPosition()));
                }
            });
            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(items.get(getAdapterPosition()).getQuantity() > 0){
                        items.get(getAdapterPosition()).setQuantity(items.get(getAdapterPosition()).getQuantity()-1);
                        quantity.setText(items.get(getAdapterPosition()).getQuantity() + "");
                        listener.onMinusClick(items.get(getAdapterPosition()));
                    }
                }
            });
        }
    }
}
