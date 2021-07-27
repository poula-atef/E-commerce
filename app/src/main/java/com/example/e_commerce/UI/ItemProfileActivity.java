package com.example.e_commerce.UI;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.e_commerce.Pojo.DummyData;
import com.example.e_commerce.Pojo.ItemClass;
import com.example.e_commerce.Pojo.MyAdapter;
import com.example.e_commerce.R;

import java.io.Serializable;
import java.util.ArrayList;

import top.defaults.drawabletoolbox.DrawableBuilder;

@RequiresApi(api = Build.VERSION_CODES.M)
public class ItemProfileActivity extends AppCompatActivity implements MyAdapter.onItemClickListener {

    RecyclerView rec;
    ImageView img;
    Button addBtn;
    ItemClass item;
    DummyData dd;
    ScrollView scrollView;
    ArrayList<ItemClass>arr;
    ImageView qr;
    ConstraintLayout layout;
    TextView title,price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_profile);

        arr = new ArrayList<>();
        item = (ItemClass) getIntent().getSerializableExtra("item");

        rec = findViewById(R.id.rec);
        img = findViewById(R.id.img);
        qr = findViewById(R.id.qr);
        title = findViewById(R.id.item_name);
        price = findViewById(R.id.item_price);
        addBtn = findViewById(R.id.button);
        scrollView = findViewById(R.id.scroll_view);

        getComponentsData();


    }
    void getComponentsData(){

        title.setText(item.getName());
        price.setText(item.getPrice() + " EGP");

        dd = ViewModelProviders.of(this).get(DummyData.class);
        Glide.with(this).load(item.getImage()).into(img);
        layout = findViewById(R.id.main_layout);
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY > oldScrollY)
                    addBtn.setVisibility(View.GONE);
                else
                    addBtn.setVisibility(View.VISIBLE);
            }
        });


        if(item.getName().contains("Laptop")){
            dd.Laptops();
            dd.getLaptopsLiveData().observe(this, new Observer<ArrayList<ItemClass>>() {
                @Override
                public void onChanged(ArrayList<ItemClass> itemClasses) {
                    arr = itemClasses;
                    setComponentsData();
                }
            });
        }
        else if(item.getName().contains("Perfume")){
            dd.Perfumes();
            dd.getPerfumesLiveData().observe(this, new Observer<ArrayList<ItemClass>>() {
                @Override
                public void onChanged(ArrayList<ItemClass> itemClasses) {
                    arr = itemClasses;
                    setComponentsData();
                }
            });
        }
        else if(item.getName().contains("Phone")){
            dd.Phones();
            dd.getPhonesLiveData().observe(this, new Observer<ArrayList<ItemClass>>() {
                @Override
                public void onChanged(ArrayList<ItemClass> itemClasses) {
                    arr = itemClasses;
                    setComponentsData();
                }
            });
        }

    }

    public void onComponentClick(View view) {
        int id = view.getId();
        if(id == R.id.qr){
            Intent intent = new Intent(ItemProfileActivity.this, ItemQrCodeActivity.class);
            intent.putExtra("itemName",item.getName());
            startActivity(intent);
            Animatoo.animateSlideLeft(this);
        }
    }

    void setComponentsData(){
        Glide.with(ItemProfileActivity.this).asBitmap().load(item.getImage()).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                Palette palette = Palette.from(resource).generate();
                layout.setBackgroundColor(palette.getDominantSwatch().getRgb());
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
                        .gradient()
                        .linearGradient()
                        .startColor(dark)
                        .endColor(light)
                        .ripple()
                        .rippleColor(dark);

                addBtn.setBackground(drawable.angle(0).build());

                MyAdapter adapter = new MyAdapter();
                adapter.setItems(arr);
                adapter.setListener(ItemProfileActivity.this);
                adapter.setBuilder(drawable);
                rec.setLayoutManager(new LinearLayoutManager(ItemProfileActivity.this));
                rec.setHasFixedSize(true);
                rec.setAdapter(adapter);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }


    @Override
    public void onItemClick(ItemClass item, ImageView img) {
        Intent intent = new Intent(ItemProfileActivity.this,ItemProfileActivity.class);
        intent.putExtra("item",(Serializable)item);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,img,"item_name");
        startActivity(intent,options.toBundle());
    }
}