package com.example.e_commerce.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.e_commerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import jp.wasabeef.blurry.Blurry;

public class CheckOutActivity extends AppCompatActivity {

    ImageView img;
    long prc;
    TextView subTotal,total,location;
    private int REQUEST_CODE = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        img = findViewById(R.id.img);
        subTotal = findViewById(R.id.subtotal);
        total = findViewById(R.id.total);
        location = findViewById(R.id.location);

        prc = getIntent().getLongExtra("price",0);
        subTotal.setText(prc + " EGP");
        total.setText((prc+25) + " EGP");
        Blurry.with(this).sampling(2).from(BitmapFactory.decodeResource(getResources(),R.drawable.shop_2)).into(img);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CheckOutActivity.this, CardActivity.class);
        startActivity(intent);
        Animatoo.animateSlideRight(CheckOutActivity.this);
        finish();
    }

    public void onComponentClick(View view) {
        int id = view.getId();
        if(id == R.id.confirm){
            if(location.getText().toString().equals(getResources().getString(R.string.location))){
                Toast.makeText(this, "You Must Select Your Location..", Toast.LENGTH_SHORT).show();
                return;
            }
            FirebaseDatabase.getInstance().getReference("Card").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(CheckOutActivity.this, "Your Order Is Completed..", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CheckOutActivity.this,CardActivity.class);
                                startActivity(intent);
                                Animatoo.animateSlideRight(CheckOutActivity.this);
                                finish();
                            }
                        }
                    });
        }
        else if(id == R.id.location){
            Intent intent = new Intent(CheckOutActivity.this,MapActivity.class);
            startActivityForResult(intent,REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            location.setText(data.getStringExtra("address"));
        }
    }
}