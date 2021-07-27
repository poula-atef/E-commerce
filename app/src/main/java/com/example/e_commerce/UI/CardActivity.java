package com.example.e_commerce.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.e_commerce.Pojo.CardAdapter;
import com.example.e_commerce.Pojo.DummyData;
import com.example.e_commerce.Pojo.ItemClass;
import com.example.e_commerce.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import top.defaults.drawabletoolbox.DrawableBuilder;

public class CardActivity extends AppCompatActivity implements CardAdapter.onItemClickListener {

    RecyclerView rec;
    Button btn;
    DummyData dd;
    TextView subTotal,total;
    long prc = 0;
    DrawableBuilder drawable;
    CardAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        rec = findViewById(R.id.rec);
        btn = findViewById(R.id.button);
        subTotal = findViewById(R.id.subtotal);
        total = findViewById(R.id.total);
        drawable = new DrawableBuilder()
                .gradient()
                .linearGradient()
                .startColor(getResources().getColor(R.color.purple_500))
                .endColor(getResources().getColor(R.color.purple_200))
                .ripple()
                .rippleColor(getResources().getColor(R.color.purple_500));

        btn.setBackground(drawable.angle(0).build());


        addSwipAnimationOnRecyclerView();

        getCardRecyclerViewData();

    }

    void getCardRecyclerViewData(){
        adapter = new CardAdapter();
        dd = ViewModelProviders.of(this).get(DummyData.class);
        dd.Card();

        dd.getCardLiveData().observe(this, new Observer<ArrayList<ItemClass>>() {
            @Override
            public void onChanged(ArrayList<ItemClass> itemClasses) {
                for(int i = 0;i < itemClasses.size();i++){
                    prc += (itemClasses.get(i).getPrice() * itemClasses.get(i).getQuantity());
                }
                subTotal.setText(Float.valueOf(prc) + " EGP");
                total.setText(Float.valueOf(prc+25) + " EGP");

                adapter.setItems(itemClasses);
                adapter.setListener(CardActivity.this);
                adapter.setBackground(drawable.oval().angle(90).build());
                rec.setAdapter(adapter);
                rec.setHasFixedSize(true);
                rec.setLayoutManager(new LinearLayoutManager(CardActivity.this));
            }
        });

    }

    void addSwipAnimationOnRecyclerView(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                rec.getAdapter().notifyItemRemoved(viewHolder.getPosition());
                adapter.getItems().remove(viewHolder.getPosition());
                FirebaseDatabase.getInstance().getReference("Card").child(FirebaseAuth.getInstance().getUid()).child(viewHolder.itemView.getTag().toString()).removeValue();
            }
        }).attachToRecyclerView(rec);
    }


    @Override
    public void onPlusClick(ItemClass item) {
        FirebaseDatabase.getInstance().getReference("Card").child(FirebaseAuth.getInstance().getUid()).child(item.getName()).child("quantity").setValue(item.getQuantity());
        prc += item.getPrice();
        subTotal.setText(Float.valueOf(prc) + " EGP");
        total.setText(Float.valueOf(prc+25) + " EGP");
    }

    @Override
    public void onMinusClick(ItemClass item) {
        FirebaseDatabase.getInstance().getReference("Card").child(FirebaseAuth.getInstance().getUid()).child(item.getName()).child("quantity").setValue(item.getQuantity());
        prc -= item.getPrice();
        subTotal.setText(Float.valueOf(prc) + " EGP");
        total.setText(Float.valueOf(prc+25) + " EGP");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CardActivity.this, MenuActivity.class);
        startActivity(intent);
        Animatoo.animateSlideRight(this);
        finish();
    }

    public void onComponentClick(View view) {
        int id = view.getId();
        if(id == R.id.button){
            Intent intent = new Intent(CardActivity.this, CheckOutActivity.class);
            intent.putExtra("price",prc);
            startActivity(intent);
            Animatoo.animateSlideLeft(this);
            finish();
        }
    }
}