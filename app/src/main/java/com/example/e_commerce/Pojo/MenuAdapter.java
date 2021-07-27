package com.example.e_commerce.Pojo;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.e_commerce.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import top.defaults.drawabletoolbox.DrawableBuilder;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private String type;
    private Drawable background;
    private Context context;
    private ArrayList<ItemClass> items;
    private onItemClickListener listener;
    private DatabaseReference ref;
    @NonNull
    @NotNull
    @Override
    public MenuAdapter.MenuViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new MenuViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.MenuViewHolder holder, int position) {
        Glide.with(context).load(items.get(position).getImage()).into(holder.img);
        holder.title.setText(items.get(position).getName());
        holder.quantity.setText(items.get(position).getQuantity() + "");
        holder.price.setText(items.get(position).getPrice() + " EGP");
        Bitmap bitmap=null;
        Glide.with(context).asBitmap().load(items.get(position).getImage()).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                Palette palette = Palette.from(resource).generate();
                int dark = 0, light = 0;
                if(palette.getDarkVibrantSwatch() != null)
                    dark = palette.getDarkVibrantSwatch().getRgb();
                else
                    dark = palette.getDarkMutedSwatch().getRgb();

                if(palette.getLightVibrantSwatch() != null)
                    light = palette.getLightVibrantSwatch().getRgb();
                else
                    light = palette.getLightMutedSwatch().getRgb();


                DrawableBuilder drawable = new DrawableBuilder()
                        .solidColor(dark)
                        .ripple()
                        .cornerRadius(50)
                        .rippleColor(light);
                holder.minus.setBackground(drawable.build());
                holder.plus.setBackground(drawable.build());
                holder.add.setBackground(drawable.build());
                holder.container.setBackgroundTintList(ColorStateList.valueOf(dark));
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
                Log.i("tag","Load Cleared !!!");
            }

            @Override
            public void onLoadStarted(@Nullable Drawable placeholder) {
                super.onLoadStarted(placeholder);
                Log.i("tag","Load Started !!!");

            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                Log.i("tag","Load Failed !!!");

            }
        });
    }

    @Override
    public int getItemCount() {
        if(items == null)
            return 0;
        return items.size();
    }

    public void setItems(ArrayList<ItemClass> items) {
        this.items = items;
    }

    public interface onItemClickListener{
        void onItemClick(ItemClass item,ImageView img);
    }

    public void setListener(onItemClickListener listener) {
        this.listener = listener;
    }

    class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img;
        TextView title,quantity,price;
        Button plus,minus,add;
        LinearLayout container;
        public MenuViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.item_container);
            img = itemView.findViewById(R.id.card_img);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.quantity);
            title = itemView.findViewById(R.id.title);
            plus = itemView.findViewById(R.id.plus_btn);
            minus = itemView.findViewById(R.id.min_btn);
            add = itemView.findViewById(R.id.add_to_card);
            ref = FirebaseDatabase.getInstance().getReference("Card").child(FirebaseAuth.getInstance().getUid());
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    items.get(getAdapterPosition()).setQuantity(items.get(getAdapterPosition()).getQuantity()+1);
                    quantity.setText(items.get(getAdapterPosition()).getQuantity() + "");
                }
            });
            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(items.get(getAdapterPosition()).getQuantity() > 0){
                        items.get(getAdapterPosition()).setQuantity(items.get(getAdapterPosition()).getQuantity()-1);
                        quantity.setText(items.get(getAdapterPosition()).getQuantity() + "");
                    }
                }
            });
            add.setOnClickListener(new View.OnClickListener() {
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
                                    .setValue(new ItemClass(it.getImage(),Integer.valueOf(quantity.getText().toString())+quant,it.getPrice(),it.getName()));
                            Toast.makeText(context, "Added!!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            });
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(items.get(getAdapterPosition()),img);
        }
    }
}
