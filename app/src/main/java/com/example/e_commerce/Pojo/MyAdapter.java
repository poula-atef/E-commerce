package com.example.e_commerce.Pojo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_commerce.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import top.defaults.drawabletoolbox.DrawableBuilder;

@SuppressLint("ResourceType")
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private DrawableBuilder builder;
    private ArrayList<ItemClass>items;
    private DatabaseReference ref;
    private onItemClickListener listener;
    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        holder.itemView.setAnimation(AnimationUtils.loadAnimation(context,R.animator.to_left));
        holder.add_btn.setBackground(builder.oval().angle(90).build());
        Glide.with(context).load(items.get(position).getImage()).into(holder.img);
        holder.title.setText(items.get(position).getName());
        holder.price.setText(items.get(position).getPrice() + " EGP");
    }

    public void setBuilder(DrawableBuilder builder) {
        this.builder = builder;
    }

    public void setItems(ArrayList<ItemClass> items) {
        this.items = items;
    }

    public void setListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public interface onItemClickListener{
        void onItemClick(ItemClass item,ImageView img);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button add_btn;
        TextView title,price;
        ImageView img;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            add_btn = itemView.findViewById(R.id.add);
            img = itemView.findViewById(R.id.img);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            itemView.setOnClickListener(this);
            ref = FirebaseDatabase.getInstance().getReference("Card").child(FirebaseAuth.getInstance().getUid());

            add_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int quant = 0;
                            ItemClass it = items.get(getAdapterPosition());
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                ItemClass item = ds.getValue(ItemClass.class);
                                if(item.getName().equals(it.getName())){
                                    quant = item.getQuantity();
                                    break;
                                }
                            }
                            ref.child(it.getName())
                                    .setValue(new ItemClass(it.getImage(),1 + quant,it.getPrice(),it.getName()));
                            Toast.makeText(context, "Added!!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            });
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(items.get(getAdapterPosition()),img);
        }
    }
}
