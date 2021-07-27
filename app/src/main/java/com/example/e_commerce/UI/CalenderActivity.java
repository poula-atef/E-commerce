package com.example.e_commerce.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ImageView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.e_commerce.R;

import jp.wasabeef.blurry.Blurry;

public class CalenderActivity extends AppCompatActivity {

    ImageView img;
    CalendarView calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        img = findViewById(R.id.img);
        calendar = findViewById(R.id.calender);
        Blurry.with(this).sampling(2).from(BitmapFactory.decodeResource(getResources(),R.drawable.shop_2)).into(img);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Intent i = new Intent(CalenderActivity.this, LoginActivity.class);
                i.putExtra("date",String.valueOf(dayOfMonth + "/" + month + "/" + year));
                setResult(5,i);
                finish();
                Animatoo.animateSlideLeft(CalenderActivity.this);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSlideLeft(this);
    }
}